package domain;

import java.util.Random;

public class TemperatureSensor implements Sensor {
    private final String id;
    private final Random random = new Random();

    public TemperatureSensor(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public SensorData generateData() {
        double temp = 20.0 + (random.nextDouble() * 5.0);

        if (random.nextDouble() > 0.95) {
            temp += 15.0 + random.nextDouble() * 10.0;
        }

        return new SensorData(id, "TEMPERATURE", temp, System.currentTimeMillis());
    }
}