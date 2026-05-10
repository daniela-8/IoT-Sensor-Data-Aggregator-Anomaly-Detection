package service;

import domain.SensorData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.SensorWindowRepository;

import java.util.LinkedList;

public class AggregatorService {
    private static final Logger logger = LogManager.getLogger(AggregatorService.class);
    private static final double ANOMALY_THRESHOLD_PERCENT = 1.20;
    private static final int REQUIRED_WINDOW_SIZE = 5;

    private final SensorWindowRepository repository;
    private final AlertDispatcher alertDispatcher;

    public AggregatorService(SensorWindowRepository repository, AlertDispatcher alertDispatcher) {
        this.repository = repository;
        this.alertDispatcher = alertDispatcher;
    }

    public void processData(SensorData data) {
        LinkedList<Double> window = repository.getWindow(data.sensorId());

        synchronized (window) {
            if (window.size() >= REQUIRED_WINDOW_SIZE) {
                double average = calculateAverage(window);
                if (isAnomaly(data.value(), average)) {
                    alertDispatcher.dispatchAnomalyAlert(data, average);
                } else {
                    logger.info("Normal reading processed: " + data);
                }
            } else {
                logger.info("Initializing baseline for: " + data);
            }

            repository.addReading(data.sensorId(), data.value());
        }
    }

    private double calculateAverage(LinkedList<Double> window) {
        return window.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private boolean isAnomaly(double currentValue, double average) {
        return currentValue > (average * ANOMALY_THRESHOLD_PERCENT);
    }
}