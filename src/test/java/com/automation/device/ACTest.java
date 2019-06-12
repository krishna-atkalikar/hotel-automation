package com.automation.device;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ACTest {

    @Test
    public void turnOff_changesStateToOff() {
        AC ac = new AC(DeviceState.ON);

        ac.turnOff();

        assertEquals(DeviceState.OFF, ac.getState());
    }

    @Test
    public void turnOn_changesDeviceStateToOn() {
        AC ac = new AC(DeviceState.OFF);

        ac.turnOn();

        assertEquals(DeviceState.ON, ac.getState());
    }

    @Test
    public void getPowerConsumption_returnsZeroIfDeviceIsOFF() {
        AC ac = new AC(DeviceState.OFF);

        assertEquals(0, ac.getPowerConsumption());
    }

    @Test
    public void getPowerConsumption_returnsZeroIfDeviceIsON() {
        AC ac = new AC(DeviceState.ON);

        assertEquals(10, ac.getPowerConsumption());
    }

    @Test
    public void deviceType_returnAC() {
        AC ac = new AC(DeviceState.OFF);

        assertEquals(DeviceType.AC, ac.deviceType());

    }
}