package com.automation.device;

import static com.automation.util.PropertiesService.propertiesService;

public class Light extends AbstractDevice {

    public Light(DeviceState deviceState) {
        super(deviceState);
    }

    @Override
    public int getPowerConsumption() {
        return deviceState == DeviceState.OFF ? 0 : propertiesService().getLightPowerConsumption();
    }

    @Override
    public DeviceType deviceType() {
        return DeviceType.LIGHT;
    }
}