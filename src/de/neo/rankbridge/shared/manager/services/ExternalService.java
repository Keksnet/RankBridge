package de.neo.rankbridge.shared.manager.services;

/**
 * ExternalService for a BridgeService.
 * 
 * @author Neo8
 * @version 1.0
 * @param <T> Class of the main for the ExternalService.
 */
public abstract class ExternalService<T> extends BridgeService{
	
	private T main;
	
	/**
	 * New ExternalService.
	 * 
	 * @param main The main instance for the service.
	 */
	public ExternalService(String name, T main) {
		super(name);
		this.main = main;
	}
	
	/**
	 * Gets the instance for the ExternalService.
	 * 
	 * @return the instance for the ExternalService.
	 */
	public T getMain() {
		return this.main;
	}
	
	/**
	 * Gets the Class of the Main.
	 * 
	 * @return the Class of the Main.
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getMainClass() {
		return (Class<T>) this.main.getClass();
	}
}
