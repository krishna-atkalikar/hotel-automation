package com.automation.controller;

import com.automation.device.ElectronicDevice;
import com.automation.hotel.Hotel;

import java.util.List;

/**
 * This interface should be implemented when a new strategy needs to be added to turn on or off devices.
 */
public interface DeviceStateManager {

    /**
     * Turns on devices for given floor number and sub corridor number
     *
     * @param hotel
     * @param floorNumber
     * @param subCorridorNumber
     * @return list of turned on devices.
     */
    List<ElectronicDevice> turnOnDevices(Hotel hotel, int floorNumber, int subCorridorNumber);

    /**
     * Turns off devices to maintain power consumption threshold.
     *
     * @param hotel
     * @param motionDetectedFloorNumber
     * @param motionDetectedSubCorridor
     * @return list of devices that are turned off to maintain power consumption threshold
     */
    List<ElectronicDevice> maintainConsumptionThreshold(Hotel hotel, int motionDetectedFloorNumber, int motionDetectedSubCorridor);
}