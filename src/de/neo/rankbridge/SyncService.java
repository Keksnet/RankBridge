package de.neo.rankbridge;

/**
 * SyncService to sync the GlobalManager instance.
 * 
 * @author Neo8
 * @version 1.0
 */
public class SyncService {
	
	private static Boolean isGlobalManager = false;
	
	/**
	 * Returns if the GlobalManager is registered.
	 * 
	 * @return Boolean whether the GlobalManager is registered.
	 */
	public static Boolean isGlobalManagerRegistered() {
		return isGlobalManager;
	}
	
	/**
	 * Mark the GlobalManager as registered.
	 */
	public static void registerGlobalManager() {
		isGlobalManager = true;
	}
}
