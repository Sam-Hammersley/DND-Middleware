package uk.ac.tees.node.portal;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.net.SocketFactory;

import uk.ac.tees.agent.UserAgent;
import uk.ac.tees.net.Connection;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;
import uk.ac.tees.net.message.impl.AddAgentMessage;
import uk.ac.tees.net.message.impl.StringMessage;
import uk.ac.tees.net.util.SocketUtility;
import uk.ac.tees.node.Node;
import uk.ac.tees.node.NodeObserver;

/**
 * Represents a portal within the middleware.
 * 
 * @author Sam Hammersley (q5315908)
 */
public class Portal extends Node {

	/**
	 * {@link UserAgent}s mapped to their unique identifier.
	 */
	private final Map<String, UserAgent> agents = new HashMap<>();

	/**
	 * Optional connection to a router.
	 */
	private Connection connection;

	/**
	 * Constructs a new Portal with the given queue of messages.
	 * 
	 * @param name name of this portal.
	 */
	public Portal(String uid, NodeObserver observer) {
		super(uid, observer);
	}

	/**
	 * Connects to a router at the given address and port and sends an add portal message to the router.
	 * 
	 * @param host the host address to connect to.
	 * @param port the port to connect on.
	 * @return the connection between this portal and the router.
	 */
	public void connectToRouter(String host, int port) {
		Socket socket = SocketUtility.createSocket(SocketFactory.getDefault(), host, port);
		
		connection = new Connection(socket);
		
		connection.write(new Message(MessageType.ADD_PORTAL, uid, host, new byte[0]));
		
		// send an Add agent message for each agent to notify the router of the users existence
		agents.values().forEach(a -> connection.write(new AddAgentMessage(uid, host, a.getUid())));
	}

	/**
	 * Adds an agent to this portal to communicate with other agents.
	 * 
	 * @param agent the agent to add.
	 */
	public void addAgent(UserAgent agent) {
		if (connection != null) {
			connection.write(new AddAgentMessage(uid, connection.getAddress(), agent.getUid()));
		}
		
		agents.put(agent.getUid(), agent);
		
		agent.setPortal(this);
		agent.start();
	}
	
	/**
	 * Gets the agent count.
	 * 
	 * @return the agent count.
	 */
	public int getAgentCount() {
		return agents.size();
	}
	
	/**
	 * Gets an agent of this portal by it's uid.
	 * 
	 * @param uid the uid associated with an agent.
	 * @return an agent if there is one assoicated with the given uid
	 */
	public UserAgent getAgent(String uid) {
		return agents.get(uid);
	}
	
	/**
	 * Checks {@link #agents} for the given key.
	 * 
	 * @param uid the uid of the agent to look for.
	 * @return {@code true} if the agent is attached to this portal.
	 */
	public boolean containsAgent(String uid) {
		return agents.containsKey(uid);
	}

	@Override
	public void send(Message message) {
		if (containsAgent(message.getDestination())) { // if agent is registered to this portal send it to them
			agents.get(message.getDestination()).queue(message);
			
		} else if (connection != null) { // otherwise send it off to the router if connected
			connection.write(message);
			
		} else if (containsAgent(message.getSource())) { // send a message to source to say couldn't find agent
			StringMessage response = new StringMessage(uid, message.getSource(), "Couldn't find agent with id " + message.getDestination());
			agents.get(message.getSource()).queue(response);
			
		}
	}

	@Override
	public void handleMessage(Message message) {
		if (!message.getDestination().equals(uid)) {
			send(message); // pass on the message

		} else {
			//System.out.println(message); // TODO appropriately handle messages
		}
	}

	@Override
	protected void receiveMessages() {
		while (true && connection != null) { // only receive messages here if connected

			Message message = connection.read();

			if (message.getType().equals(MessageType.TERMINATION)) { // nothing more to read from the connection
				break;
			}

			queue(message);
		}
	}

}