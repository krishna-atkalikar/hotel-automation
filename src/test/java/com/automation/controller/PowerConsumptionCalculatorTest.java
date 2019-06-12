package com.automation.controller;

import com.automation.hotel.Hotel;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PowerConsumptionCalculatorTest {

    @Test
    public void powerConsumptionThreshold_returnsMax() {
        Hotel h = new Hotel(2, 1, 2);

        PowerConsumptionCalculator calculator = new PowerConsumptionCalculator();
        Map<Integer, Integer> floorVsPowerConsumptionThreshold = PowerConsumptionCalculator.powerConsumptionThreshold(h);

        assertEquals(Integer.valueOf(35), floorVsPowerConsumptionThreshold.get(1));
        assertEquals(Integer.valueOf(35), floorVsPowerConsumptionThreshold.get(2));
    }
}