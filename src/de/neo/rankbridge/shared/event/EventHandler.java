package de.neo.rankbridge.shared.event;

import java.util.ArrayList;
import java.util.HashMap;

import de.neo.rankbridge.shared.event.events.BridgeEvent;

/**
 * The EventHandler fires and listens for a BridgeEvent.
 * 
 * @author Neo8
 * @version 1.0
 * @see de.neo.rankbridge.shared.event.events.BridgeEvent
 */
public class EventHandler {
	
	private HashMap<Class<? extends BridgeEvent>, ArrayList<BridgeEventListener>> listener;
	
	/**
	 * New EventHandler
	 */
	public EventHandler() {
		this.listener = new HashMap<>();
	}
	
	/**
	 * Executes a BridgeEvent.
	 * 
	 * @param event The BridgeEvent to execute.
	 */
	public void executeEvent(BridgeEvent event) {
		for(BridgeEventListener handler : this.listener.get(event.getClass())) {
			handler.execute(event);
		}
	}
	
	/**
	 * Registers a new BridgeEvent.
	 * The BridgeEvent must be registered so that a Plugin can listen to.
	 * 
	 * @param event The BridgeEvent to register.
	 */
	public void registerEvent(Class<? extends BridgeEvent> event) {
		this.listener.put(event, new ArrayList<>());
	}
	
	/**
	 * Unregisters a BridgeEvent.
	 * 
	 * @param event The BridgeEvent to unregister.
	 */
	public void unregisterEvent(Class<? extends BridgeEvent> event) {
		this.listener.remove(event);
	}
	
	/**
	 * Registers a Listener.
	 * 
	 * @param event The Event to listen for.
	 * @param listener The BridgeEventListener instance.
	 */
	public void registerListener(Class<? extends BridgeEvent> event, BridgeEventListener listener) {
		ArrayList<BridgeEventListener> l = this.listener.get(event);
		l.add(listener);
		this.listener.put(event, l);
	}
	
	/**
	 * Unregisters a Listener.
	 * 
	 * @param event The Event to stop listen for.
	 * @param listener The BridgeEventListener instance.
	 */
	public void unregisterListener(Class<? extends BridgeEvent> event, BridgeEventListener listener) {
		ArrayList<BridgeEventListener> l = this.listener.get(event);
		l.remove(listener);
		this.listener.put(event, l);
	}
}
