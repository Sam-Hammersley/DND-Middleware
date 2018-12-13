package uk.ac.tees.net.message;

import java.util.Comparator;

public class MessageComparator implements Comparator<Message> {

	@Override
	public int compare(Message m0, Message m1) {
		return m0.getType().getType() - m1.getType().getType();
	}

}