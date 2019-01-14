package uk.ac.tees.net.message.impl;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;

public class StringMessage extends Message {

	public StringMessage(String source, String destination, String message) {
		super(MessageType.STRING, source, destination, message.getBytes());
	}

}