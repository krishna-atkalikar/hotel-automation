package com.automation;

import com.automation.controller.HotelAutomationController;
import com.automation.events.ApplicationStartedEvent;
import com.automation.events.Events;
import com.automation.hotel.Hotel;
import com.automation.task.MotionDetectedPlaces;
import com.automation.task.TimedDeviceOnOffHandler;

import java.util.Scanner;

public class MainApplication {

    public static void main(String[] args) {
        registerEventHandlers();


        Scanner s = new Scanner(System.in);
        System.out.print("Number of floors: ");
        int noOfFloors = s.nextInt();
        System.out.print("Main corridors per floor: ");
        int noOfMainCorridors = s.nextInt();
        System.out.print("Sub corridors per floor: ");
        int noOfSubCorridors = s.nextInt();

        Hotel hotel = new Hotel(noOfFloors, noOfMainCorridors, noOfSubCorridors);
        System.out.println(hotel);
        Events.post(new ApplicationStartedEvent(hotel));
        HotelAutomationController controller = new HotelAutomationController(hotel);
        controller.recordMovement(1, 2);
        int i = 1;
        while (i == 1) {
            System.out.println("enter choice");
            int c = s.nextInt();
            switch (c) {
                case 1:
                    System.out.println("enter floor number : ");
                    int f = s.nextInt();
                    int sc = s.nextInt();
                    controller.recordMovement(f, sc);
            }

            System.out.println("do you want to continue?");
            i = s.nextInt();
        }
    }

    private static void registerEventHandlers() {
        Events.register(new MotionDetectedPlaces());
        Events.register(new TimedDeviceOnOffHandler());
    }
}