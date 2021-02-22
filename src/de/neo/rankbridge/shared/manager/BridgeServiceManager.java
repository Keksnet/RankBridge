package de.neo.rankbridge.shared.manager;

import java.util.HashMap;

import de.neo.rankbridge.shared.manager.services.BridgeService;

/**
 * The Manager for BridgeServices.
 * 
 * @author Neo8
 * @version 1.0
 */
public class BridgeServiceManager {
	
	private HashMap<Class<? extends BridgeService>, BridgeService> services;
	
	/**
	 * New Instance.
	 */
	public BridgeServiceManager() {
		this.services = new HashMap<>();
	}
	
	/**
	 * Registers a BridgeService.
	 * 
	 * @param service the BridgeService.
	 */
	public void register(BridgeService service) {
		this.services.put(service.getClass(), service);
		System.out.println("[ServiceManager] Registered Service: " + service.getServiceName() + " (" + service.getClass().getName() + ")");
	}
	
	/**
	 * Unregisters a BridgeService.
	 * 
	 * @param service the Class of the BridgeService.
	 */
	public void unregister(Class<? extends BridgeService> service) {
		this.services.remove(service);
	}
	
	/**
	 * Returns a Service.
	 * 
	 * @param service the Class of the Service.
	 * @return the Instance of the Service.
	 */
	public BridgeService getService(Class<? extends BridgeService> service) {
		return this.services.get(service);
	}
	
	/**
	 * Is the service registered.
	 * 
	 * @param service the Class of the service.
	 * @return Boolean whether the service is registered or not.
	 */
	public Boolean isServiceRegistered(Class<? extends BridgeService> service) {
		return this.services.containsKey(service);
	}
}
