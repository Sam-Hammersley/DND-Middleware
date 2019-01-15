package uk.ac.tees.net.message.impl;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;

public class AddAgentMessage extends Message {

	public AddAgentMessage(String source, String destination, String newAgent) {
		super(MessageType.ADD_AGENT, source, destination, newAgent.getBytes());
	}

}