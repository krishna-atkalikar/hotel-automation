package com.automation.controller;

import com.automation.device.DeviceState;
import com.automation.device.DeviceType;
import com.automation.events.Events;
import com.automation.hotel.Corridor;
import com.automation.hotel.Hotel;
import com.automation.task.MotionDetectedPlaces;
import com.automation.util.PropertiesService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HotelAutomationControllerTest {
    private static PropertiesService propertiesService = PropertiesService.propertiesService();
    private PropertiesService mockedPropertiesService = Mockito.spy(PropertiesService.class);

    /**
     * Mocking of PropertiesService requires PowerMockito libraries to be included, doing this simple hack to avoid adding
     * so many libraries. However using PowerMockito is cleaner solution.
     */
    @AfterClass
    public static void reset() {
        PropertiesService.setPropertiesService(propertiesService);
    }

    @Before
    public void setUp() {
        initMocks(this);
        Events.register(new MotionDetectedPlaces());
        PropertiesService.setPropertiesService(mockedPropertiesService);
    }

    @Test
    public void recordMovement_doesNothing_ifItsNotANightTime() {
        when(mockedPropertiesService.isNightTime()).thenReturn(false);
        Hotel hotel = new Hotel(2, 1, 2);
        HotelAutomationController controller = new HotelAutomationController(hotel);

        boolean isRecorded = controller.recordMovement(1, 2);
        assertFalse(isRecorded);
    }

    @Test
    public void recordMovement_turnsOnLightInSubCorridor() throws InterruptedException {
        mockPropertiesService();
        Hotel hotel = new Hotel(2, 1, 2);
        HotelAutomationController controller = new HotelAutomationController(hotel);

        boolean isRecorded = controller.recordMovement(1, 2);

        assertTrue(isRecorded);
        Corridor subCorridor = controller.getHotel().getSubCorridor(1, 2);
        assertCorridorState(subCorridor, DeviceType.LIGHT, DeviceState.ON);
        assertCorridorState(subCorridor, DeviceType.AC, DeviceState.ON);

        Corridor subCorridor1 = controller.getHotel().getSubCorridor(1, 1);
        assertCorridorState(subCorridor1, DeviceType.LIGHT, DeviceState.OFF);
        assertCorridorState(subCorridor1, DeviceType.AC, DeviceState.OFF);

        waitMotionTaskToFinish(6);

        assertCorridorState(subCorridor, DeviceType.LIGHT, DeviceState.OFF);
        assertCorridorState(subCorridor, DeviceType.AC, DeviceState.ON);
        assertCorridorState(subCorridor1, DeviceType.LIGHT, DeviceState.OFF);
        assertCorridorState(subCorridor1, DeviceType.AC, DeviceState.ON);
    }

    @Test
    public void recordMovement_turnsOnLightInSubCorridor_with3Subcorridor() throws InterruptedException {
        mockPropertiesService();
        HotelAutomationController c = new HotelAutomationController(new Hotel(2, 1, 3));

        boolean isRecorded = c.recordMovement(1, 2);

        assertTrue(isRecorded);
        Corridor subCorridor2 = c.getHotel().getSubCorridor(1, 2);
        assertCorridorState(subCorridor2, DeviceType.LIGHT, DeviceState.ON);
        assertCorridorState(subCorridor2, DeviceType.AC, DeviceState.ON);

        Corridor subCorridor1 = c.getHotel().getSubCorridor(1, 1);
        assertCorridorState(subCorridor1, DeviceType.LIGHT, DeviceState.OFF);
        assertCorridorState(subCorridor1, DeviceType.AC, DeviceState.OFF);

        Corridor subCorridor3 = c.getHotel().getSubCorridor(1, 3);
        assertCorridorState(subCorridor3, DeviceType.LIGHT, DeviceState.OFF);
        assertCorridorState(subCorridor3, DeviceType.AC, DeviceState.ON);

        waitMotionTaskToFinish(6);


        assertCorridorState(subCorridor2, DeviceType.LIGHT, DeviceState.OFF);
        assertCorridorState(subCorridor2, DeviceType.AC, DeviceState.ON);

        assertCorridorState(subCorridor1, DeviceType.LIGHT, DeviceState.OFF);
        assertCorridorState(subCorridor1, DeviceType.AC, DeviceState.ON);

        assertCorridorState(subCorridor3, DeviceType.LIGHT, DeviceState.OFF);
        assertCorridorState(subCorridor3, DeviceType.AC, DeviceState.ON);
    }

    @Test
    public void recordMovement_resetsTimerIfMotionIsDetectedAgainAtSamePlace() throws InterruptedException {
        mockPropertiesService();
        HotelAutomationController c = new HotelAutomationController(new Hotel(1, 1, 2));

        boolean isRecorded = c.recordMovement(1, 2);

        assertTrue(isRecorded);

        Corridor subCorridor2 = c.getHotel().getSubCorridor(1, 2);
        assertCorridorState(subCorridor2, DeviceType.LIGHT, DeviceState.ON);
        assertCorridorState(subCorridor2, DeviceType.AC, DeviceState.ON);

        Corridor subCorridor1 = c.getHotel().getSubCorridor(1, 1);
        assertCorridorState(subCorridor1, DeviceType.LIGHT, DeviceState.OFF);
        assertCorridorState(subCorridor1, DeviceType.AC, DeviceState.OFF);

        //Following assertions make sure timer is reset when motion is recorded in same place
        //motionDetectionThreshold is 5 seconds
        waitMotionTaskToFinish(3);
        //waited for 3 seconds and a motion is recorded again in same place
        c.recordMovement(1, 2);
        waitMotionTaskToFinish(2);
        //waited for 2 more seconds and did assertions that the device state is still not altered
        assertCorridorState(subCorridor2, DeviceType.LIGHT, DeviceState.ON);
        assertCorridorState(subCorridor1, DeviceType.AC, DeviceState.OFF);
        waitMotionTaskToFinish(4);
        //waited for 4 more seconds and did assertions that the device state is altered, which means timer was reset.
        assertCorridorState(subCorridor2, DeviceType.LIGHT, DeviceState.OFF);
        assertCorridorState(subCorridor1, DeviceType.AC, DeviceState.ON);
    }

    private void mockPropertiesService() {
        when(mockedPropertiesService.isNightTime()).thenReturn(true);

        when(mockedPropertiesService.motionDetectionThresholdSeconds()).thenReturn(5);
        when(mockedPropertiesService.pollDelayMillis()).thenReturn(1000);
    }

    private void assertCorridorState(Corridor subCorridor, DeviceType deviceType, DeviceState deviceState) {
        subCorridor.getDevicesByType(deviceType)
                .forEach(light -> assertEquals(deviceState, light.getState()));
    }

    private void waitMotionTaskToFinish(int waitSeconds) throws InterruptedException {
        Thread.sleep(waitSeconds * 1000);
    }
}