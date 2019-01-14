package uk.ac.tees.portal;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.net.SocketFactory;

import uk.ac.tees.agent.MetaAgent;
import uk.ac.tees.agent.UserAgent;
import uk.ac.tees.net.Connection;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;
import uk.ac.tees.net.util.SocketUtility;

/**
 * Represents a portal within the middleware.
 * 
 * @author Sam Hammersley (q5315908)
 */
public class Portal extends MetaAgent {

	/**
	 * {@link MetaAgent}s mapped to their unique identifier.
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
	public Portal(String uid) {
		super(uid);
	}

	/**
	 * Connects to a router at the given address and port and sends an add portal message to the router.
	 * 
	 * @param host the host address to connect to.
	 * @param port the port to connect on.
	 * @return the connection between this portal and the router.
	 */
	public void connectToRouter(String host, int port) {
		Socket socket = SocketUtility.createSocket(SocketFactory.getDefault(), "localhost", port);
		
		connection = new Connection(socket);
		
		connection.write(new Message(MessageType.ADD_PORTAL_MESSAGE, uid, host, new byte[0]));
		
		agents.values().forEach(a -> connection.write(new Message(MessageType.ADD_AGENT, uid, host, a.getUid().getBytes())));
	}

	/**
	 * Adds an agent to this portal to communicate with other agents.
	 * 
	 * @param agent the agent to add.
	 */
	public void addAgent(UserAgent agent) {
		if (connection != null) {
			connection.write(new Message(MessageType.ADD_AGENT, uid, connection.getAddress(), agent.getUid().getBytes()));
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

	/**
	 * Sends the specified message to the appropriate user agent.
	 * 
	 * @param message the message to send to the agent.
	 */
	private void sendToAgent(Message message) {
		UserAgent agent = agents.get(message.getDestination());
		
		agent.queue(message);
	}

	@Override
	public void send(Message message) {
		if (containsAgent(message.getDestination())) {
			sendToAgent(message);
			
		} else {
			connection.write(message);
		}
	}

	@Override
	public void handle(Message message) {
		if (message.getDestination().equals(uid)) {
			System.out.println(message);

		} else if (containsAgent(message.getDestination())) {
			sendToAgent(message);

		} else {
			connection.write(message);

		}
	}

	@Override
	public void receiveMessages() {
		while (true) {
			if (connection == null) {
				continue;
			}

			Message message = connection.read();

			if (message.getType().equals(MessageType.TERMINATION)) {
				break;
			}

			queue(message);
		}
	}

}