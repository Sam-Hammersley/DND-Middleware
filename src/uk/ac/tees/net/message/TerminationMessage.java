package uk.ac.tees.net.message;

public final class TerminationMessage extends Message {

	public TerminationMessage() {
		super(MessageType.TERMINATION, 0, new byte[0]);
	}

}