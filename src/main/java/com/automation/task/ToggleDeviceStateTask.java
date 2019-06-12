package com.automation.task;

import com.automation.device.ElectronicDevice;
import com.automation.events.MotionEvent;
import com.automation.logger.ConsoleLogger;
import com.automation.util.PropertiesService;
import com.google.common.base.Stopwatch;

import java.util.concurrent.CompletableFuture;

public class ToggleDeviceStateTask implements Runnable {

    private final MotionEvent event;
    private Stopwatch stopwatch;
    private PropertiesService propertiesService = PropertiesService.propertiesService();

    public ToggleDeviceStateTask(MotionEvent event) {
        this.event = event;
        CompletableFuture.runAsync(this);
    }

    @Override
    public void run() {
        stopwatch = Stopwatch.createStarted();
        int motionDetectionThresholdSeconds = propertiesService.motionDetectionThresholdSeconds();
        ConsoleLogger.log("Started auto device turn off thread for " + motionDetectionThresholdSeconds);
        while (stopwatch.elapsed().getSeconds() < motionDetectionThresholdSeconds) {
            try {
                int pollDelayMillis = propertiesService.pollDelayMillis();
                ConsoleLogger.log("Sleeping for " + pollDelayMillis);
                Thread.sleep(pollDelayMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ConsoleLogger.log("No motion detected for " + motionDetectionThresholdSeconds + " seconds hence toggling devices states for floor " +
                event.getFloorNumber() + " subCorridor " + event.getSubCorridorNumber() + "\n" + event.getHotel());
        event.getTurnedOnDevices().forEach(ElectronicDevice::turnOff);
        event.getTurnedOffDevices().forEach(ElectronicDevice::turnOn);
        stopwatch.stop();
        ConsoleLogger.log("Task completed for floor " + event.getFloorNumber() + " subCorridor " +
                event.getSubCorridorNumber() + "\n" + event.getHotel());

    }

    public void reset() {
        ConsoleLogger.log("Resetting timer as the motion is detected on floor " + event.getFloorNumber() + " subCorridor " + event.getSubCorridorNumber());
        stopwatch.reset();
        stopwatch.start();
    }

    public boolean isCompleted() {
        return !stopwatch.isRunning();
    }
}