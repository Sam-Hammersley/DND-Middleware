package uk.ac.tees.portal;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.net.SocketFactory;

import uk.ac.tees.agent.MetaAgent;
import uk.ac.tees.net.Connection;
import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.util.SocketUtility;

/**
 * Represents a portal within the middleware.
 * 
 * @author Sam Hammersley (q5315908)
 */
public class Portal implements Runnable {
	
	/**
	 * Maximum number of agents per portal.
	 */
	private static final int MAX_AGENTS = 10;
	
	/**
	 * {@link MetaAgent}s mapped to their unique identifier.
	 */
	private final Map<String, MetaAgent> agents = new HashMap<>();
	
	/**
	 * Denotes the running state of this portal.
	 */
	private volatile boolean running = true;
	
	/**
	 * A {@link BlockingQueue} of {@link Message}s.
	 */
	private final BlockingQueue<Message> messages;
	
	/**
	 * Constructs a new Portal with the given queue of messages.
	 * 
	 * @param messages a queue of messages. 
	 */
	public Portal(BlockingQueue<Message> messages) {
		this.messages = messages;
	}

	/**
	 * Queues a message to be processed.
	 * 
	 * @param message the message to be processed.
	 * @return {@code true} if the message was queued.
	 */
	public boolean queue(Message message) {
		return messages.offer(message);
	}
	
	/**
	 * Adds an agent to this portal to communicate with other agents.
	 * 
	 * @param agent the agent to add.
	 */
	public void addAgent(MetaAgent agent) {
		
		if (agents.size() >= MAX_AGENTS) { //TODO: relocate
			Portal portal = new Portal(new LinkedBlockingQueue<>());
			portal.addAgent(agent);
			return;
		}
		
		agents.put(agent.getUid(), agent);
		agent.attach(this);
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
	public void run() {
		// while this portal is running wait for an available message in the queue and process once there is an available message.
		while (running) {
			try {
				Message message = messages.take();
				
				if (agents.containsKey(message.getDestination())) {
					agents.get(message.getDestination()).receive(message);
				} else {
					
					Socket socket = SocketUtility.createSocket(SocketFactory.getDefault(), "localhost", NetworkConstants.SERVER_PORT);
					
					try (Connection connection = new Connection(socket)) {
						connection.write(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			} catch(InterruptedException e) {
				throw new RuntimeException("Thread interrupted while processing messages!", e);
			}
		}
	}
	
}