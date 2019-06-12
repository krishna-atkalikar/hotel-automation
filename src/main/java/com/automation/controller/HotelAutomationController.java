package com.automation.controller;

import com.automation.device.ElectronicDevice;
import com.automation.events.Events;
import com.automation.events.MotionEvent;
import com.automation.hotel.Hotel;
import com.automation.logger.ConsoleLogger;
import com.automation.util.PropertiesService;

import java.util.List;

public class HotelAutomationController {

    private Hotel hotel;
    private DeviceStateManager deviceStateManager = new SubCorridorDeviceStateManager();

    public HotelAutomationController(Hotel hotel) {
        this.hotel = hotel;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public boolean recordMovement(int floorNumber, int subCorridorNumber) {
        if (!PropertiesService.propertiesService().isNightTime()) {
            ConsoleLogger.log("Motion is detected on floor " + floorNumber + ", subcorridor " + subCorridorNumber +
                    "but its not a night time slot. Hence skipping.");
            return false;
        }

        ConsoleLogger.log("Altering device states for floor " + floorNumber + ", subcorridor " + subCorridorNumber);
        List<ElectronicDevice> turnedOnDevices = deviceStateManager.turnOnDevices(hotel, floorNumber, subCorridorNumber);
        List<ElectronicDevice> turnedOffDevices = deviceStateManager.maintainConsumptionThreshold(hotel, floorNumber, subCorridorNumber);
        ConsoleLogger.log("Done altering device states for floor " + floorNumber + ", subcorridor " + subCorridorNumber);
        Events.post(new MotionEvent(hotel, turnedOnDevices, turnedOffDevices, floorNumber, subCorridorNumber));
        return true;
    }
}