package uk.ac.tees.net.message;

import uk.ac.tees.node.Node;

public abstract class MessageReceiver<T extends Node> implements Runnable {

	protected final T node;
	
	public MessageReceiver(T node) {
		this.node = node;
	}
	
	@Override
	public void run() {
		while (true) {
			receive();
		}
	}
	
	protected abstract void receive();

}