package de.neo.rankbridge;

public class SyncService {
	
	private static Boolean isGlobalManager = false;
	
	public static Boolean isGlobalManagerRegistered() {
		return isGlobalManager;
	}
	
	public static void registerGlobalManager() {
		isGlobalManager = true;
	}
}
