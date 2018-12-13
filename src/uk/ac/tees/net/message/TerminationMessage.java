package uk.ac.tees.net.message;

public final class TerminationMessage extends Message {

	public TerminationMessage() {
		super(MessageType.TERMINATION, "", "", new byte[0]);
	}

}