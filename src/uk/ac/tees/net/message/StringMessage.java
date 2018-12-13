package uk.ac.tees.net.message;

public class StringMessage extends Message {

	public StringMessage(String source, String destination, String message) {
		super(MessageType.STRING_MESSAGE, source, destination, message.getBytes());
	}

}