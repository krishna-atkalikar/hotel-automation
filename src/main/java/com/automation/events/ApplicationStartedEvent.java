package com.automation.events;

import com.automation.hotel.Hotel;

/**
 * This event is thrown when application is started and Hotel's instance is created.
 */
public class ApplicationStartedEvent {

    private Hotel hotel;

    public ApplicationStartedEvent(Hotel hotel) {
        this.hotel = hotel;
    }

    public Hotel getHotel() {
        return hotel;
    }
}