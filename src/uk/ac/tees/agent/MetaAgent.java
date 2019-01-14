package uk.ac.tees.agent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import uk.ac.tees.net.message.Message;

/**
 * Represents an agent that can communicate with other agents that are a part of the system.
 * 
 * The meta-agent queues messages and handles them when available. 
 * 
 * @author Sam Hammersley (q5315908)
 */
public abstract class MetaAgent {

	/**
	 * Queued incoming {@link Message}s.
	 */
	private final BlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();

	/**
	 * Executes runnables tasks.
	 */
	private final ExecutorService executor = Executors.newCachedThreadPool();

	/**
	 * A field to uniquely identify agents.
	 */
	protected final String uid;

	/**
	 * Constructs a new {@link MetaAgent}.
	 * 
	 * @param uid unique identifier.
	 */
	public MetaAgent(String uid) {
		this.uid = uid;
	}

	/**
	 * Waits for and takes {@link Message}s from the queue.
	 */
	private void processMessages() {
		try {
			while (true) {
				handle(messages.take());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * To be called to start receiving and processing messages.
	 */
	public final void start() {
		executor.submit(this::receiveMessages);
		
		executor.submit(this::processMessages);
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
	 * Sends a string message to the given destination.
	 * 
	 * @param destination the destination of the message.
	 * @param contents the contents of the message.
	 */
	public abstract void send(Message message);

	/**
	 * Handles a message
	 * 
	 * @param message
	 */
	protected abstract void handle(Message message);

	/**
	 * Receives messages, this is called in a new {@link Thread}.
	 */
	protected abstract void receiveMessages();
	
	/**
	 * Queues a message to be processed.
	 * 
	 * @param message the message to be processed.
	 * @return {@code true} if the message was queued.
	 */
	public final boolean queue(Message message) {
		return messages.offer(message);
	}

}