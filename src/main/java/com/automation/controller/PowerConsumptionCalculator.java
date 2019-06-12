package com.automation.controller;

import com.automation.hotel.Floor;
import com.automation.hotel.Hotel;
import com.automation.util.PropertiesService;

import java.util.Map;
import java.util.stream.Collectors;

public class PowerConsumptionCalculator {

    public static int getPowerConsumptionThreshold(Hotel hotel, int floorNumber) {
        return powerConsumptionThreshold(hotel).get(floorNumber);
    }

    public static Map<Integer, Integer> powerConsumptionThreshold(Hotel hotel) {
        return hotel.getFloors().stream().collect(Collectors.toMap(Floor::getFloorNumber,
                PowerConsumptionCalculator::calculateThresholdForFloor));
    }

    private static int calculateThresholdForFloor(Floor f) {
        PropertiesService propertiesService = PropertiesService.propertiesService();
        return (f.getNoOfMainCorridors() * propertiesService.getMaxAllowedMainCorridorConsumption()) +
                (f.getNoOfSubCorridors() * propertiesService.getMaxAllowedSubCorridorConsumption());
    }
}