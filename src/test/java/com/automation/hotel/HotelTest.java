package com.automation.hotel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HotelTest {

    @Test(expected = InvalidHotelStateException.class)
    public void hotelCreationThrowsEx_whenFloorSizeIsLessThanOne() {
        Hotel hotel = new Hotel(0, 1, 2);
    }

    @Test(expected = InvalidHotelStateException.class)
    public void hotelCreationThrowsEx_whenMainCorridorsIsLessThanOne() {
        Hotel hotel = new Hotel(1, 0, 2);
    }

    @Test(expected = InvalidHotelStateException.class)
    public void hotelCreationThrowsEx_whenSubCorridorIsLessThanOne() {
        Hotel hotel = new Hotel(1, 1, 0);
    }

    @Test
    public void hotelCreation_createsWithCorrectFloorSize() {
        Hotel hotel = new Hotel(1, 1, 1);

        assertEquals(1, hotel.getTotalNoOfFloors());
    }
}