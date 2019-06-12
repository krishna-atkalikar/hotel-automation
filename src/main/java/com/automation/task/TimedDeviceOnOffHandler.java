package com.automation.task;

import com.automation.device.DeviceType;
import com.automation.device.ElectronicDevice;
import com.automation.events.ApplicationStartedEvent;
import com.automation.hotel.Corridor;
import com.automation.hotel.Floor;
import com.automation.hotel.Hotel;
import com.automation.logger.ConsoleLogger;
import com.automation.util.PropertiesService;
import com.google.common.eventbus.Subscribe;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Handles Application started event and schedules tasks to turn on and off devices based on time constraints configured in
 * PropertiesService.
 */
public class TimedDeviceOnOffHandler {

    private Timer timer = new Timer("Device-On-Off-Timer");

    private static void toggleDeviceState(Hotel hotel, Consumer<ElectronicDevice> deviceStateToggle) {
        Collection<Floor> floors = hotel.getFloors();
        for (Floor floor : floors) {
            Collection<Corridor> mainCorridors = floor.getMainCorridors();
            for (Corridor mainCorridor : mainCorridors) {
                mainCorridor.getDevicesByType(DeviceType.LIGHT).forEach(deviceStateToggle);
            }
        }
    }

    @Subscribe
    public void handle(ApplicationStartedEvent event) {
        PropertiesService propertiesService = PropertiesService.propertiesService();
        scheduleTask(new TurnLightsOnTask(event.getHotel()), propertiesService.lightsTurnOnTime());
        scheduleTask(new TurnLightsOffTask(event.getHotel()), propertiesService.lightsTurnOffTime());
    }

    private void scheduleTask(TimerTask turnLightsOnTask, LocalTime time) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, time.getHour());
        today.set(Calendar.MINUTE, time.getMinute());
        today.set(Calendar.SECOND, 0);
        //Schedules task daily for given time
        timer.schedule(turnLightsOnTask, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }

    private static class TurnLightsOnTask extends TimerTask {


        private Hotel hotel;

        public TurnLightsOnTask(Hotel hotel) {
            this.hotel = hotel;
        }

        @Override
        public void run() {
            ConsoleLogger.log("Turning on all main corridor lights ..\n" + hotel);
            toggleDeviceState(hotel, ElectronicDevice::turnOn);
            ConsoleLogger.log("Turning on all main corridor lights .. done.\n" + hotel);
        }
    }

    private static class TurnLightsOffTask extends TimerTask {


        private Hotel hotel;

        public TurnLightsOffTask(Hotel hotel) {
            this.hotel = hotel;
        }

        @Override
        public void run() {
            ConsoleLogger.log("Turning off all main corridor lights ..\n" + hotel);
            toggleDeviceState(hotel, ElectronicDevice::turnOff);
            ConsoleLogger.log("Turning off all main corridor lights .. done.\n" + hotel);
        }
    }
}