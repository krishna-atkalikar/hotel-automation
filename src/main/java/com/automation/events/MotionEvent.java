package com.automation.events;

import com.automation.device.ElectronicDevice;
import com.automation.hotel.Hotel;

import java.util.List;

/**
 * This event is sent when a motion is detected.
 */
public class MotionEvent {

    private Hotel hotel;
    private int floorNumber;
    private int subCorridorNumber;
    private List<ElectronicDevice> turnedOnDevices;
    private List<ElectronicDevice> turnedOffDevices;

    public MotionEvent(Hotel hotel, List<ElectronicDevice> turnedOnDevices, List<ElectronicDevice> turnedOffDevices
            , int floorNumber, int subCorridorNumber) {
        this.hotel = hotel;
        this.turnedOnDevices = turnedOnDevices;
        this.turnedOffDevices = turnedOffDevices;
        this.floorNumber = floorNumber;
        this.subCorridorNumber = subCorridorNumber;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public int getSubCorridorNumber() {
        return subCorridorNumber;
    }

    public List<ElectronicDevice> getTurnedOnDevices() {
        return turnedOnDevices;
    }

    public List<ElectronicDevice> getTurnedOffDevices() {
        return turnedOffDevices;
    }
}