package uk.ac.tees.node;

import uk.ac.tees.net.message.Message;

public interface NodeObserver {

	public void update(Node node, Message message);

}