package com.automation.hotel;

public enum CorridorType {

    MAIN("Main"), SUB("Sub");

    String name;

    CorridorType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}