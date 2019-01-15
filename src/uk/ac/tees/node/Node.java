package uk.ac.tees.node;

import uk.ac.tees.agent.MetaAgent;
import uk.ac.tees.net.message.Message;

/**
 * Represents a node (router or portal)
 * 
 * @author q5315908
 */
public abstract class Node extends MetaAgent {

	/**
	 * Observer of this node, notified when message is handled.
	 */
	private final NodeObserver observer;
	
	public Node(String uid, NodeObserver observer) {
		super(uid);
		this.observer = observer;
		this.addProcess(() -> receiveMessages());
	}

	@Override
	public final void handle(Message message) {
		if (observer != null) {
			observer.update(this, message);
		}
		
		if (!message.getDestination().equals(uid)) { // destination is not this router
			send(message);
		}
	}
	
	/**
	 * Receives messages, this is called in a new {@link Thread}.
	 */
	protected abstract void receiveMessages();
	
}