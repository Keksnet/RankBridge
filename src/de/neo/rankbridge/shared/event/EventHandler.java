package de.neo.rankbridge.shared.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The EventHandler fires and listens for a BridgeEvent.
 * 
 * @author Neo8
 * @version 1.0
 * @see de.neo.rankbridge.shared.event.BridgeEvent
 */
public class EventHandler {
	
	private HashMap<Enum<? extends BridgeEventType>, ArrayList<Class<? extends BridgeEventListener>>> listener;
	
	public EventHandler() {
		this.listener = new HashMap<>();
	}
	
	public void executeEvent(Class<? extends BridgeEvent> event) {
		BridgeEventType type = BridgeEvent.class.cast(event).getType();
		
	}
	
	public void registerEvent(Enum<? extends BridgeEventType> event) {
		this.listener.put(event, new ArrayList<>());
	}
	
	public void unregisterEvent(Enum<? extends BridgeEventType> event) {
		this.listener.remove(event);
	}
	
	public void registerListener(Class<? extends BridgeEventListener> listener) {
		if(listener.getMethods().length > 0) {
			for(Method m : listener.getMethods()) {
				if(m.isAnnotationPresent(BridgeEventHandler.class) && m.getParameterCount() == 1) {
					if(m.getParameterTypes()[0].getClass().getPackageName().equals(BridgeEvent.class.getPackageName())) {
						BridgeEventType type = BridgeEvent.class.cast(m.getParameterTypes()[0].getClass()).getType();
						ArrayList<Class<? extends BridgeEventListener>> l = this.listener.get(type);
						l.add(listener);
						this.listener.put(type, l);
					}
				}
			}
		}
	}
	
	public void unregisterListener(Class<? extends BridgeEventListener> listener) {
		if(listener.getMethods().length > 0) {
			for(Method m : listener.getMethods()) {
				if(m.isAnnotationPresent(BridgeEventHandler.class) && m.getParameterCount() == 1) {
					if(m.getParameterTypes()[0].getClass().getPackageName().equals(BridgeEvent.class.getPackageName())) {
						BridgeEventType type = BridgeEvent.class.cast(m.getParameterTypes()[0].getClass()).getType();
						ArrayList<Class<? extends BridgeEventListener>> l = this.listener.get(type);
						l.remove(listener);
						this.listener.put(type, l);
					}
				}
			}
		}
	}
}
