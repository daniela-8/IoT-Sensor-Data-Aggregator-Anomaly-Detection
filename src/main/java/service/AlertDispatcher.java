package service;

import domain.SensorData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlertDispatcher {
    private static final Logger logger = LogManager.getLogger(AlertDispatcher.class);
    private static final String RED_ALERT = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public void dispatchAnomalyAlert(SensorData data, double rollingAverage) {
        String alertMsg = String.format("%s[CRITICAL ALERT] Anomaly detected for %s! Reading: %.2f (Avg: %.2f)%s",
                RED_ALERT, data.sensorId(), data.value(), rollingAverage, RESET);
        logger.warn(alertMsg);
    }
}