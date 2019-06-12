package com.automation.hotel;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Floor {

    private int floorNumber;
    private Map<Integer, Corridor> mainCorridors;
    private Map<Integer, Corridor> subCorridors;

    public Floor(int floorNumber, int noOfMainCorridors, int noOfSubCorridors) {
        this.floorNumber = floorNumber;
        this.mainCorridors = createMainCorridors(noOfMainCorridors, floorNumber);
        this.subCorridors = createSubCorridors(noOfSubCorridors, floorNumber);
    }

    private Map<Integer, Corridor> createMainCorridors(int total, int floorNumber) {
        return IntStream.rangeClosed(1, total).boxed()
                .collect(Collectors.toMap(Function.identity(), i -> Corridor.createMainCorridor(floorNumber, i)));
    }

    private Map<Integer, Corridor> createSubCorridors(int total, int floorNumber) {
        return IntStream.rangeClosed(1, total).boxed()
                .collect(Collectors.toMap(Function.identity(), i -> Corridor.createSubCorridor(floorNumber, i)));
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public Collection<Corridor> getMainCorridors() {
        return mainCorridors.values();
    }

    public Collection<Corridor> getSubCorridors() {
        return subCorridors.values();
    }

    public int getNoOfMainCorridors() {
        return mainCorridors.size();
    }

    public int getNoOfSubCorridors() {
        return subCorridors.size();
    }

    public Corridor getSubCorridor(int subCorridorNumber) {
        return subCorridors.get(subCorridorNumber);
    }

    public Corridor getMainCorridor(int mainCorridorNumber) {
        return mainCorridors.get(mainCorridorNumber);
    }

    public int getPowerConsumption() {
        Integer subCorridorTotal = subCorridors.values().stream().mapToInt(Corridor::getPowerConsumption).sum();
        Integer mainCorridorTotal = mainCorridors.values().stream().mapToInt(Corridor::getPowerConsumption).sum();
        return subCorridorTotal + mainCorridorTotal;
    }

    public boolean subCorridorExists(int subCorridor) {
        return subCorridors.containsKey(subCorridor);
    }

    public boolean mainCorridorExists(int mainCorridor) {
        return mainCorridors.containsKey(mainCorridor);
    }

    @Override
    public String toString() {
        return "Floor " + floorNumber + "\n" +
                appendCorridors(mainCorridors.values(), "Main") + "\n" +
                appendCorridors(subCorridors.values(), "Sub");
    }

    private String appendCorridors(Collection<Corridor> mainCorridors, String corridorName) {
        StringBuilder sb = new StringBuilder();
        int c = 1;
        for (Corridor corridor : mainCorridors) {
            sb.append(corridor);
            if (c++ != mainCorridors.size()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}