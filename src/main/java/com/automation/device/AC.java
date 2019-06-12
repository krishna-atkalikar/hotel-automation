package com.automation.device;

import static com.automation.util.PropertiesService.propertiesService;

public class AC extends AbstractDevice {

    public AC(DeviceState deviceState) {
        super(deviceState);
    }

    @Override
    public int getPowerConsumption() {
        return deviceState == DeviceState.OFF ? 0 : propertiesService().getACPowerConsumption();
    }

    @Override
    public DeviceType deviceType() {
        return DeviceType.AC;
    }
}