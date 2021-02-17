package de.neo.rankbridge.shared.message;

import de.neo.rankbridge.shared.util.UniversalType;

/**
 * A message between the services.
 * 
 * @author Neo8
 * @version 1.0
 */
public class BridgeMessage<T> {
	
	/**
	 * Types of members of a conversation.
	 * 
	 * @author Neo8
	 * @version 1.0
	 */
	public enum ConversationMember{
		API,
		DISCORD,
		MINECRAFT,
		TEAMSPEAK
	}
	
	private ConversationMember sender;
	private ConversationMember[] receiver;
	private T content;
	private UniversalType<T> universal;
	
	/**
	 * New BridgeMessage to all services.
	 * 
	 * @param sender Sender of the message.
	 */
	public BridgeMessage(ConversationMember sender) {
		this.sender = sender;
		this.receiver = new ConversationMember[] {
				ConversationMember.API,
				ConversationMember.DISCORD,
				ConversationMember.MINECRAFT,
				ConversationMember.TEAMSPEAK};
	}
	
	/**
	 * New BridgeMessage to one receiver.
	 * 
	 * @param sender Sender of the message.
	 * @param receiver Receiver of the message.
	 */
	public BridgeMessage(ConversationMember sender, ConversationMember receiver) {
		this.sender = sender;
		this.receiver = new ConversationMember[] {receiver};
	}
	
	/**
	 * New BridgeMessage to more receiver.
	 * 
	 * @param sender Sender of the message.
	 * @param receiver Receivers of the message.
	 */
	public BridgeMessage(ConversationMember sender, ConversationMember[] receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}
	
	/**
	 * Gets the sender of the message.
	 * 
	 * @return the sender of the message.
	 */
	public ConversationMember getSender() {
		return this.sender;
	}
	
	/**
	 * Gets the receiver of the message.
	 * 
	 * @return the receiver of the message.
	 */
	public ConversationMember[] getReceiver() {
		return this.receiver;
	}
	
	/**
	 * Sets the content of the message.
	 * 
	 * @param content The new content.
	 */
	public void setContent(T content) {
		this.content = content;
		this.universal = new UniversalType<T>(content);
	}
	
	/**
	 * Sets the content of the message.
	 * 
	 * @param universal The new content of the message as UniversalType.
	 */
	@SuppressWarnings("unchecked")
	public void setContentUniversal(UniversalType<?> universal) {
		this.universal = universal.getAsGeneric(this.universal.getClass());
		this.content = this.universal.getAsOriginal();
	}
	
	/**
	 * Gets the content of the message.
	 * 
	 * @return the original content.
	 */
	public T getContent() {
		return this.content;
	}
	
	/**
	 * Gets the content of the message as UniversalType.
	 * 
	 * @return the UniversalType content.
	 */
	public UniversalType<T> getContentUniversal() {
		return this.universal;
	}
}
