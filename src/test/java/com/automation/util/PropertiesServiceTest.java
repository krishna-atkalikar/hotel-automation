package com.automation.util;

import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class PropertiesServiceTest {

    private PropertiesService propertiesService = PropertiesService.propertiesService();

    @Test
    public void lightsTurnOnTime() {
        LocalTime time = propertiesService.lightsTurnOnTime();

        assertEquals(LocalTime.of(18, 0), time);
    }

    @Test
    public void lightsTurnOffTime() {
        LocalTime time = propertiesService.lightsTurnOffTime();

        assertEquals(LocalTime.of(6, 0), time);
    }

    @Test
    public void getACPowerConsumption() {
        assertEquals(10, propertiesService.getACPowerConsumption());
    }

    @Test
    public void getLightPowerConsumption() {
        assertEquals(5, propertiesService.getLightPowerConsumption());
    }

    @Test
    public void getMaxAllowedMainCorridorConsumption() {
        assertEquals(15, propertiesService.getMaxAllowedMainCorridorConsumption());
    }

    @Test
    public void getMaxAllowedSubCorridorConsumption() {
        assertEquals(10, propertiesService.getMaxAllowedSubCorridorConsumption());
    }

    @Test
    public void motionDetectionThresholdSeconds() {
        assertEquals(60, propertiesService.motionDetectionThresholdSeconds());
    }

    @Test
    public void pollDelayMillis() {
        assertEquals(5000, propertiesService.pollDelayMillis());
    }
}