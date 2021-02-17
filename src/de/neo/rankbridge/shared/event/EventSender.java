package de.neo.rankbridge.shared.event;

/**
 * Represents a EventSender.
 * 
 * @author Neo8
 * @version 1.0
 */
public interface EventSender {
	
	/**
	 * Type of the EventSender.
	 * 
	 * @author Neo8
	 * @version 1.0
	 */
	public enum SenderType {
		API,
		DISCORD,
		MINECRAFT,
		TEAMSPEAK
	}
	
	/**
	 * Returns the SenderType.
	 * 
	 * @return Type of the sender.
	 */
	public SenderType getType();
}
