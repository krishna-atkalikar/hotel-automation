package com.automation.controller;

import com.automation.device.DeviceState;
import com.automation.device.DeviceType;
import com.automation.device.ElectronicDevice;
import com.automation.hotel.Corridor;
import com.automation.hotel.Floor;
import com.automation.hotel.Hotel;
import com.google.common.collect.ImmutableCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.automation.util.PropertiesService.propertiesService;

public class SubCorridorDeviceStateManager implements DeviceStateManager {

    @Override
    public List<ElectronicDevice> turnOnDevices(Hotel hotel, int floorNumber, int subCorridorNumber) {
        Corridor subCorridor = hotel.getSubCorridor(floorNumber, subCorridorNumber);
        ImmutableCollection<ElectronicDevice> lights = subCorridor.getDevices().get(DeviceType.LIGHT);
        return turnOnLights(lights);
    }

    private List<ElectronicDevice> turnOnLights(ImmutableCollection<ElectronicDevice> lights) {
        List<ElectronicDevice> turnedOnDevices = new ArrayList<>();
        for (ElectronicDevice light : lights) {
            light.turnOn();
            turnedOnDevices.add(light);
        }
        return turnedOnDevices;
    }

    @Override
    public List<ElectronicDevice> maintainConsumptionThreshold(Hotel hotel, int motionDetectedFloorNumber, int motionDetectedSubCorridor) {
        int powerConsumptionThreshold = PowerConsumptionCalculator.getPowerConsumptionThreshold(hotel, motionDetectedFloorNumber);
        int extraPowerConsumption = getExtraPowerConsumption(hotel, motionDetectedFloorNumber, powerConsumptionThreshold);
        int numberOfACsToBeTurnedOf = calculateACsToBeTurnedOFF(extraPowerConsumption);
        return turnOffDevicesIfNeeded(hotel.getSubCorridors(motionDetectedFloorNumber, motionDetectedSubCorridor), numberOfACsToBeTurnedOf);
    }

    private int getExtraPowerConsumption(Hotel hotel, int motionDetectedFloorNumber, int powerConsumptionThreshold) {
        Floor floor = hotel.getFloor(motionDetectedFloorNumber);
        return floor.getPowerConsumption() - powerConsumptionThreshold;
    }

    private int calculateACsToBeTurnedOFF(int extraPowerConsumption) {
        int acsToBeTurnedOff = 0;
        while (extraPowerConsumption > 0) {
            extraPowerConsumption -= propertiesService().getACPowerConsumption();
            acsToBeTurnedOff++;
        }
        return acsToBeTurnedOff;
    }

    private List<ElectronicDevice> turnOffDevicesIfNeeded(Collection<Corridor> subCorridors, int numberOfACsToBeTurnedOFF) {
        List<ElectronicDevice> turnedOffDevices = new ArrayList<>();
        for (Corridor subCorridor : subCorridors) {
            List<ElectronicDevice> acList = getONACs(subCorridor);
            for (int i = 0; i < acList.size() && numberOfACsToBeTurnedOFF > 0; i++) {
                ElectronicDevice ac = acList.get(i);
                ac.turnOff();
                turnedOffDevices.add(ac);
                numberOfACsToBeTurnedOFF--;
            }
        }
        return turnedOffDevices;
    }

    private List<ElectronicDevice> getONACs(Corridor subCorridor) {
        return subCorridor.getDevicesByType(DeviceType.AC).stream()
                .filter(ac -> ac.getState() == DeviceState.ON)
                .collect(Collectors.toList());
    }
}