package com.automation.events;

import com.google.common.eventbus.EventBus;

/**
 * Convenient class to send application events. This class internally uses {@link EventBus}
 */
public class Events {

    private static final EventBus eventBus = new EventBus("hotel-automation");

    public static void post(Object event) {
        eventBus.post(event);
    }

    public static void register(Object listener) {
        eventBus.register(listener);
    }

    public static void unregister(Object listener) {
        eventBus.unregister(listener);
    }
}