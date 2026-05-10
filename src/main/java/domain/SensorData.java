package domain;

public record SensorData(String sensorId, String sensorType, double value, long timestamp) {
    @Override
    public String toString() {
        return String.format("[%s] %s: %.2f", sensorType, sensorId, value);
    }
}
