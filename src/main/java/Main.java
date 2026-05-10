import domain.Sensor;
import domain.SensorData;
import domain.TemperatureSensor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.SensorWindowRepository;
import service.AggregatorService;
import service.AlertDispatcher;

import java.util.List;
import java.util.concurrent.*;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        BlockingQueue<SensorData> dataQueue = new LinkedBlockingQueue<>();

        SensorWindowRepository repository = new SensorWindowRepository(5);
        AlertDispatcher dispatcher = new AlertDispatcher();
        AggregatorService aggregator = new AggregatorService(repository, dispatcher);

        List<Sensor> sensors = List.of(
                new TemperatureSensor("TEMP-ZONE-A"),
                new TemperatureSensor("TEMP-ZONE-B")
        );

        ScheduledExecutorService producerPool = Executors.newScheduledThreadPool(sensors.size());
        ExecutorService consumerPool = Executors.newSingleThreadExecutor();

        consumerPool.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SensorData data = dataQueue.take();
                    aggregator.processData(data);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("Consumer interrupted", e);
                }
            }
        });

        for (Sensor sensor : sensors) {
            producerPool.scheduleAtFixedRate(() -> {
                SensorData data = sensor.generateData();
                dataQueue.offer(data);
            }, 0, 1, TimeUnit.SECONDS);
        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            producerPool.shutdown();
            consumerPool.shutdownNow();
        }
    }
}