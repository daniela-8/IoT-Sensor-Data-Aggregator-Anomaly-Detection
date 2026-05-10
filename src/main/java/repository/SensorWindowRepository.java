package repository;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SensorWindowRepository {
    private final Map<String, LinkedList<Double>> sensorWindows = new ConcurrentHashMap<>();
    private final int windowSize;

    public SensorWindowRepository(int windowSize) {
        this.windowSize = windowSize;
    }

    public LinkedList<Double> getWindow(String sensorId) {
        return sensorWindows.computeIfAbsent(sensorId, k -> new LinkedList<>());
    }

    public void addReading(String sensorId, double value) {
        LinkedList<Double> window = getWindow(sensorId);
        synchronized (window) {
            if (window.size() == windowSize) {
                window.removeFirst();
            }
            window.addLast(value);
        }
    }
}