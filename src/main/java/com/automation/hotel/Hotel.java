package com.automation.hotel;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

public class Hotel {

    private Map<Integer, Floor> floors;

    public Hotel(int noOfFloors, int noOfMainCorridors, int noOfSubCorridors) {
        if (noOfFloors < 1) {
            throw new InvalidHotelStateException();
        }
        if (noOfMainCorridors < 1) {
            throw new InvalidHotelStateException();
        }
        if (noOfSubCorridors < 1) {
            throw new InvalidHotelStateException();
        }
        this.floors = IntStream.rangeClosed(1, noOfFloors)
                .mapToObj(i -> new Floor(i, noOfMainCorridors, noOfSubCorridors))
                .collect(Collectors.toMap(Floor::getFloorNumber, f -> f));
    }

    public int getTotalNoOfFloors() {
        return floors.size();
    }

    public Collection<Floor> getFloors() {
        return floors.values();
    }

    public Floor getFloor(int floorNumber) {
        checkArgument(floors.containsKey(floorNumber), "Floor number does not exist in the Hotel.");
        return floors.get(floorNumber);
    }

    public Corridor getMainCorridor(int floorNumber, int mainCorridorNumber) {
        checkArgument(floors.containsKey(floorNumber), "Floor number does not exist in the Hotel.");
        Floor floor = floors.get(floorNumber);
        checkArgument(floor.mainCorridorExists(mainCorridorNumber), "MainCorridor does not exist.");
        return floor.getMainCorridor(mainCorridorNumber);
    }

    public Corridor getSubCorridor(int floorNumber, int subCorridorNumber) {
        checkArgument(floors.containsKey(floorNumber), "Floor number does not exist in the Hotel.");
        Floor floor = floors.get(floorNumber);
        checkArgument(floor.subCorridorExists(subCorridorNumber), "SubCorridor does not exist.");
        return floor.getSubCorridor(subCorridorNumber);
    }

    public Collection<Corridor> getSubCorridors(int floorNumber) {
        checkArgument(floors.containsKey(floorNumber), "Floor number does not exist in the Hotel.");
        return floors.get(floorNumber).getSubCorridors();
    }

    public Collection<Corridor> getSubCorridors(int floorNumber, int excludedSubCorridor) {
        checkArgument(floors.containsKey(floorNumber), "Floor number does not exist in the Hotel.");
        return floors.get(floorNumber).getSubCorridors().stream().filter(sc -> sc.getCorridorNumber() != excludedSubCorridor)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Floor floor : floors.values()) {
            sb.append(floor).append("\n");
        }
        return sb.toString();
    }
}