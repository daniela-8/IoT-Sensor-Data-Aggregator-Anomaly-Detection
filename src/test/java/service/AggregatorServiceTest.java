package service;

import domain.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.SensorWindowRepository;

import static org.mockito.Mockito.*;

class AggregatorServiceTest {

    private AlertDispatcher mockDispatcher;
    private SensorWindowRepository repository;
    private AggregatorService aggregatorService;

    @BeforeEach
    void setUp() {
        mockDispatcher = mock(AlertDispatcher.class);

        repository = new SensorWindowRepository(5);

        aggregatorService = new AggregatorService(repository, mockDispatcher);
    }

    @Test
    void testNormalReadingsDoNotTriggerAlert() {
        String sensorId = "TEST-SENSOR";

        for (int i = 0; i < 5; i++) {
            aggregatorService.processData(new SensorData(sensorId, "TEMPERATURE", 20.0, System.currentTimeMillis()));
        }

        aggregatorService.processData(new SensorData(sensorId, "TEMPERATURE", 21.0, System.currentTimeMillis()));

        verify(mockDispatcher, never()).dispatchAnomalyAlert(any(), anyDouble());
    }

    @Test
    void testSpikeTriggersAnomalyAlert() {
        String sensorId = "TEST-SENSOR";

        for (int i = 0; i < 5; i++) {
            aggregatorService.processData(new SensorData(sensorId, "TEMPERATURE", 20.0, System.currentTimeMillis()));
        }

        SensorData anomalyData = new SensorData(sensorId, "TEMPERATURE", 30.0, System.currentTimeMillis());
        aggregatorService.processData(anomalyData);

        verify(mockDispatcher, times(1)).dispatchAnomalyAlert(eq(anomalyData), eq(20.0));
    }
}