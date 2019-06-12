package com.automation.util;

import java.time.LocalTime;

public class PropertiesService {

    private static PropertiesService propertiesService = new PropertiesService();

    public static PropertiesService propertiesService() {
        return propertiesService;
    }

    public static void setPropertiesService(PropertiesService propertiesService) {
        PropertiesService.propertiesService = propertiesService;
    }

    public LocalTime lightsTurnOnTime() {
        return LocalTime.of(18, 0);
    }

    public LocalTime lightsTurnOffTime() {
        return LocalTime.of(6, 0);
    }

    public int getACPowerConsumption() {
        return 10;
    }

    public int getLightPowerConsumption() {
        return 5;
    }

    public int getMaxAllowedMainCorridorConsumption() {
        return 15;
    }

    public int getMaxAllowedSubCorridorConsumption() {
        return 10;
    }

    public int motionDetectionThresholdSeconds() {
        return 60;
    }

    public boolean isNightTime() {
        LocalTime from = lightsTurnOffTime();
        LocalTime to = lightsTurnOnTime();

        LocalTime now = LocalTime.now();
        return !now.isAfter(from) || !now.isBefore(to);
    }

    public int pollDelayMillis() {
        return 5000;
    }
}