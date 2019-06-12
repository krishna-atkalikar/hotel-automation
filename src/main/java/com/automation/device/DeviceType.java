package com.automation.device;

public enum DeviceType {

    LIGHT("Light"), AC("Ac");

    private String name;

    DeviceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}