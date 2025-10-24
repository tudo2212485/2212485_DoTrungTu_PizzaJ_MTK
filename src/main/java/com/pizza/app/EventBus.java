package com.pizza.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Simple Event Bus for implementing Observer pattern.
 * DESIGN PATTERN: Observer Pattern
 * 
 * Allows objects to subscribe to events and be notified when those events occur.
 * This decouples the event publisher from subscribers, promoting loose coupling.
 */
public class EventBus {
    private static EventBus instance;
    private final Map<String, List<Consumer<String>>> subscribers;
    
    private EventBus() {
        this.subscribers = new HashMap<>();
    }
    
    /**
     * Get singleton instance of EventBus.
     */
    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }
    
    /**
     * Subscribe to an event.
     * 
     * @param event The event name to subscribe to
     * @param listener The callback to invoke when event is published
     */
    public void subscribe(String event, Consumer<String> listener) {
        subscribers.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }
    
    /**
     * Publish an event to all subscribers.
     * 
     * @param event The event name
     */
    public void publish(String event) {
        publish(event, null);
    }
    
    /**
     * Publish an event with data to all subscribers.
     * 
     * @param event The event name
     * @param data Optional data to pass to subscribers
     */
    public void publish(String event, String data) {
        List<Consumer<String>> listeners = subscribers.get(event);
        if (listeners != null) {
            for (Consumer<String> listener : listeners) {
                listener.accept(data);
            }
        }
    }
    
    /**
     * Unsubscribe from an event.
     */
    public void unsubscribe(String event, Consumer<String> listener) {
        List<Consumer<String>> listeners = subscribers.get(event);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }
    
    /**
     * Clear all subscribers for testing purposes.
     */
    public void clearAll() {
        subscribers.clear();
    }
}






