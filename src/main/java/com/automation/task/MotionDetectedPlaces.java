package com.automation.task;

import com.automation.events.MotionEvent;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.eventbus.Subscribe;

/**
 * Keeps track of all the ToggleDeviceStateTasks
 */
public class MotionDetectedPlaces {

    private Table<Integer, Integer, ToggleDeviceStateTask> floorNoVsCorridorNoVsTaskTable;

    public MotionDetectedPlaces() {
        this.floorNoVsCorridorNoVsTaskTable = HashBasedTable.create();
    }

    @Subscribe
    public void detectMotion(MotionEvent event) {
        if (!floorNoVsCorridorNoVsTaskTable.contains(event.getFloorNumber(), event.getSubCorridorNumber())) {
            floorNoVsCorridorNoVsTaskTable.put(event.getFloorNumber(), event.getSubCorridorNumber(),
                    new ToggleDeviceStateTask(event));
        } else {
            ToggleDeviceStateTask task = floorNoVsCorridorNoVsTaskTable.get(event.getFloorNumber(), event.getSubCorridorNumber());
            if (task.isCompleted()) {
                floorNoVsCorridorNoVsTaskTable.put(event.getFloorNumber(), event.getSubCorridorNumber(),
                        new ToggleDeviceStateTask(event));
            } else {
                task.reset();
            }
        }
    }
}