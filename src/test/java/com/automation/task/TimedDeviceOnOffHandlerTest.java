package com.automation.task;

import com.automation.device.DeviceState;
import com.automation.device.DeviceType;
import com.automation.events.ApplicationStartedEvent;
import com.automation.hotel.Corridor;
import com.automation.hotel.Hotel;
import com.automation.util.PropertiesService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Shrikrushna Atkalikar (atkalikar@optymyze.com)
 */
public class TimedDeviceOnOffHandlerTest {

    private static PropertiesService propertiesService = PropertiesService.propertiesService();
    private static PropertiesService mockedPropertiesService = mock(PropertiesService.class);

    @BeforeClass
    public static void init() {
        PropertiesService.setPropertiesService(mockedPropertiesService);
    }

    /**
     * Mocking of PropertiesService requires PowerMockito libraries to be included, doing this simple hack to avoid adding
     * so many libraries. However using PowerMockito is cleaner solution.
     */
    @AfterClass
    public static void reset() {
        PropertiesService.setPropertiesService(propertiesService);
    }

    @Test
    public void handleEvent_schedulesTaskToTurnOnDevices() throws InterruptedException {
        mockPropertiesService(false, LocalTime.now().minus(2, ChronoUnit.SECONDS),
                LocalTime.now().plus(59, ChronoUnit.SECONDS));

        Hotel hotel = new Hotel(1, 1, 1);
        assertCorridorLights(hotel.getMainCorridor(1, 1), DeviceState.OFF);

        new TimedDeviceOnOffHandler().handle(new ApplicationStartedEvent(hotel));

        waitMotionTaskToFinish(2);
        assertCorridorLights(hotel.getMainCorridor(1, 1), DeviceState.ON);
    }

    @Test
    public void handleEvent_schedulesTaskToTurnOFFDevices() throws InterruptedException {
        mockPropertiesService(true, LocalTime.now().plus(59, ChronoUnit.SECONDS),
                LocalTime.now().minus(59, ChronoUnit.SECONDS));

        Hotel hotel = new Hotel(1, 1, 1);
        assertCorridorLights(hotel.getMainCorridor(1, 1), DeviceState.ON);

        new TimedDeviceOnOffHandler().handle(new ApplicationStartedEvent(hotel));

        waitMotionTaskToFinish(2);
        assertCorridorLights(hotel.getMainCorridor(1, 1), DeviceState.OFF);
    }

    private void assertCorridorLights(Corridor mainCorridor, DeviceState deviceState) {
        mainCorridor.getDevicesByType(DeviceType.LIGHT).forEach(d -> assertEquals(deviceState, d.getState()));
    }

    private void mockPropertiesService(boolean isNightTime, LocalTime turnOnTime, LocalTime turnOffTime) {
        when(mockedPropertiesService.lightsTurnOnTime()).thenReturn(turnOnTime);
        when(mockedPropertiesService.lightsTurnOffTime()).thenReturn(turnOffTime);
        when(mockedPropertiesService.isNightTime()).thenReturn(isNightTime);
    }

    private void waitMotionTaskToFinish(int waitSeconds) throws InterruptedException {
        Thread.sleep(waitSeconds * 1000);
    }
}