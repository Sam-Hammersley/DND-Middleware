package uk.ac.tees.net.message.impl;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;

public final class TerminationMessage extends Message {

	public TerminationMessage() {
		super(MessageType.TERMINATION, "", "", new byte[0]);
	}

}