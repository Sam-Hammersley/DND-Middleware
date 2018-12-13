package uk.ac.tees.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.StringMessage;
import uk.ac.tees.portal.Portal;

/**
 * Represents an agent that can communicate with other agents that are a part of the system.
 * 
 * @author Sam Hammersley (q5315908)
 */
public class MetaAgent {

	/**
	 * A field to uniquely identify agents.
	 */
	private final String uid;
	
	/**
	 * {@link MessageHandler}s for incoming messages.
	 */
	private final List<MessageHandler> messageHandlers = new ArrayList<>();
	
	/**
	 * A portal that facilitates communication with other agents.
	 */
	private Portal portal;
	
	/**
	 * Constructs a new {@link MetaAgent}.
	 * 
	 * @param uid unique identifier.
	 */
	public MetaAgent(String uid, MessageHandler...handlers) {
		this.uid = uid;
		Arrays.stream(handlers).forEach(messageHandlers::add);
	}
	
	/**
	 * Gets the uid of this agent.
	 * 
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	
	/**
	 * Assigns this agent's portal reference to the given instance.
	 * 
	 * @param portal the portal to send messages through.
	 */
	public void attach(Portal portal) {
		this.portal = portal;
	}
	
	/**
	 * Adds a {@link MessageHandler} to {@link #messageHandlers}.
	 */
	public void addMessageHandler(MessageHandler messageHandler) {
		messageHandlers.add(messageHandler);
	}
	
	/**
	 * Sends a string message to the given destination.
	 * 
	 * @param destination the destination of the message.
	 * @param contents the contents of the message.
	 */
	public void send(String destination, String contents) {
		if (portal == null) {
			throw new NullPointerException("Not attached to a portal!");
		}
		
		if (destination.equals(uid)) {
			throw new RuntimeException("Can't send message to self!");
		}
		
		Message message = new StringMessage(uid, destination, contents);
		portal.queue(message);
	}
	
	/**
	 * Receives the given message and passes the message to the {@link #messageHandlers}  to be handled.
	 * 
	 * @param message the message received.
	 */
	public void receive(Message message) {
		messageHandlers.forEach(h -> h.handleMessage(message));
	}
	
}