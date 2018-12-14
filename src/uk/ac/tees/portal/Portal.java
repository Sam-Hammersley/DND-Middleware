package uk.ac.tees.portal;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

import javax.net.SocketFactory;

import uk.ac.tees.agent.MetaAgent;
import uk.ac.tees.agent.UserAgent;
import uk.ac.tees.net.Connection;
import uk.ac.tees.net.NetworkConstants;
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
	 * Maximum number of agents per portal.
	 */
	private static final int MAX_AGENTS = 10;
	
	/**
	 * {@link MetaAgent}s mapped to their unique identifier.
	 */
	private final Map<String, UserAgent> agents = new HashMap<>();

	/**
	 * Constructs a new Portal with the given queue of messages.
	 * 
	 * @param name name of this portal.
	 * @param threadFactory threadFactory for creating threads.
	 */
	public Portal(String uid, ThreadFactory threadFactory) {
		super(uid, threadFactory);
	}

	/**
	 * Adds an agent to this portal to communicate with other agents.
	 * 
	 * @param agent the agent to add.
	 */
	public void addAgent(UserAgent agent) {

		/*if (agents.size() >= MAX_AGENTS) { //TODO: relocate
			Portal portal = new Portal(new LinkedBlockingQueue<>());
			portal.addAgent(agent);
			return;
		}*/
		agents.put(agent.getUid(), agent);
		agent.attach(this);
		
		agent.start();
	}
	
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
	 * Connects to a router at the given address and port and sends an add portal message to the router.
	 * 
	 * @param host the host address to connect to.
	 * @param port the port to connect on.
	 * @return the connection between this portal and the router.
	 */
	public Connection connectToRouter(String host, int port) {
		Socket socket = SocketUtility.createSocket(SocketFactory.getDefault(), "localhost", NetworkConstants.SERVER_PORT);
		
		Connection connection = new Connection(socket);
		
		connection.write(new Message(MessageType.ADD_PORTAL_MESSAGE, uid, host, new byte[0]));
		
		return connection;
	}

	@Override
	public void send(Message message) {
		queue(message);
	}

	@Override
	public void receive(Message message) {
		if (containsAgent(message.getDestination())) {
			UserAgent agent = agents.get(message.getDestination());
			
			agent.queue(message);
		} else {
			
		}
	}
	
}