package uk.ac.tees.agent;

import java.util.ArrayList;
import java.util.List;
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
	 * Executes runnable tasks.
	 */
	private final ExecutorService executor = Executors.newCachedThreadPool();
	
	/**
	 * {@link List} of {@link Runnable} tasks to be executed upon start up of this meta-agent
	 */
	private final List<Runnable> startUpProcesses = new ArrayList<>();
	
	private boolean started = false;

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
		this.startUpProcesses.add(new MessageProcessor(this));
	}

	/**
	 * Adds a {@link Runnable} task to be ran at start up.
	 * 
	 * @param runnable
	 */
	public void addProcess(Runnable runnable) {
		if (started) {
			return;
		}
		startUpProcesses.add(runnable);
	}
	
	/**
	 * To be called to start receiving and processing messages.
	 */
	public final void start() {
		started = true;
		startUpProcesses.forEach(executor::execute);
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
	 * Queues a message to be processed.
	 * 
	 * @param message the message to be processed.
	 * @return {@code true} if the message was queued.
	 */
	public final boolean queue(Message message) {
		return messages.offer(message);
	}
	
	/**
	 * A {@link Runnable} task that calls {@link BlockingQueue#take()} and blocks 
	 * until a message is available a which point it's handled by the abstract method.
	 * 
	 * @author Sam Hammersley (q5315908)
	 */
	final class MessageProcessor implements Runnable {

		/**
		 * The {@link MetaAgent} to process messages for.
		 */
		private final MetaAgent agent;
		
		public MessageProcessor(MetaAgent agent) {
			this.agent = agent;
		}
		
		@Override
		public void run() {
			try {
				while (true) {
					agent.handle(agent.messages.take());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}