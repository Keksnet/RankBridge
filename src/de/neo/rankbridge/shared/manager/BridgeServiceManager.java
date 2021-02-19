package de.neo.rankbridge.shared.manager;

import java.util.HashMap;

import de.neo.rankbridge.shared.manager.services.BridgeService;

public class BridgeServiceManager {
	
	private HashMap<Class<? extends BridgeService>, BridgeService> services;
	
	public BridgeServiceManager() {
		this.services = new HashMap<>();
	}
	
	public void register(BridgeService service) {
		this.services.put(service.getClass(), service);
		System.out.println("[ServiceManager] Registered Service: " + service.getServiceName() + "(" + service.getClass().getName() + ")");
	}
	
	public void unregister(Class<? extends BridgeService> service) {
		this.services.remove(service);
	}
	
	public BridgeService getService(Class<? extends BridgeService> service) {
		return this.services.get(service);
	}
	
	public Boolean isServiceRegistered(Class<? extends BridgeService> service) {
		return this.services.containsKey(service);
	}
}
