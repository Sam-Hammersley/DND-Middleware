package uk.ac.tees.net.message;

public class StringMessage extends Message {

	public StringMessage(String message) {
		super(MessageType.STRING_MESSAGE, message.length(), message.getBytes());
	}

}