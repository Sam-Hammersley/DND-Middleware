package uk.ac.tees.agent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageComparator;

/**
 * Represents an agent that can communicate with other agents that are a part of the system.
 * 
 * @author Sam Hammersley (q5315908)
 */
public abstract class MetaAgent implements Runnable {

	/**
	 * Queued incoming {@link Message}s.
	 */
	private final BlockingQueue<Message> messages = new PriorityBlockingQueue<Message>(10, new MessageComparator());
	
	/**
	 * Creates a {@link Thread} for this meta-agent.
	 */
	private final ThreadFactory threadFactory;
	
	/**
	 * A field to uniquely identify agents.
	 */
	protected final String uid;
	
	/**
	 * Denotes the running state of this portal.
	 */
	private volatile boolean running = true;
	
	/**
	 * Constructs a new {@link MetaAgent}.
	 * 
	 * @param uid unique identifier.
	 */
	public MetaAgent(String uid, ThreadFactory threadFactory) {
		this.uid = uid;
		this.threadFactory = threadFactory;
	}
	
	public void start() {
		threadFactory.newThread(this).start();
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
	 * 
	 * 
	 * @param message
	 */
	protected abstract void receive(Message message);
	
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
	 * Sets {@link #running} to false.
	 */
	public void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		try {
			while (running) {
				receive(messages.take());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}