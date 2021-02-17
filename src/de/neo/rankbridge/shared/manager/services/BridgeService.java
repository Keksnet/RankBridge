package de.neo.rankbridge.shared.manager.services;

/**
 * The manager for BridgeServices.
 * 
 * @author Neo8
 * @version 1.0
 */
public abstract class BridgeService {
	
	private String name;
	private Boolean externalHandler;
	private ExternalService<?> externalService;
	
	/**
	 * New BridgeService.
	 * 
	 * @param name the DisplayName of the service.
	 */
	public BridgeService(String name) {
		this.name = name;
		this.externalHandler = false;
	}
	
	/**
	 * New BridgeService.
	 * 
	 * @param <V> the Type of the ExternalService.
	 * @param name the name of the service.
	 * @param externalService the ExternalService.
	 */
	public <V> BridgeService(String name, ExternalService<V> externalService) {
		this.name = name;
		this.externalHandler = true;
		this.externalService = externalService;
	}
	
	/**
	 * Gets the name of the service.
	 * 
	 * @return the name of the service.
	 */
	public String getServiceName() {
		return this.name;
	}
	
	/**
	 * Is the service handled external?
	 * 
	 * @return Boolean whether the service is handled external or not.
	 */
	public Boolean handleExternal() {
		return this.externalHandler;
	}
	
	/**
	 * Gets the ExternalService.
	 * 
	 * @return the ExternalService.
	 */
	public ExternalService<?> getExternalService() {
		return this.externalService;
	}
}
