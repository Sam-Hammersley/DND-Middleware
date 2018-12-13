package uk.ac.tees.net.message;

/**
 * Represents the messages sent from one agent to another.
 * 
 * @author Sam Hammersley (q5315908)
 */
public class Message {

	/**
	 * The type of the message.
	 */
	private final MessageType type;
	
	/**
	 * The source of the message.
	 */
	private final String source;
	
	/**
	 * Intended destination of the message.
	 */
	private final String destination;
	
	/**
	 * The contents of the message.
	 */
	private final byte[] contents;
	
	/**
	 * Constructs a new {@link Message} with the given parameters.
	 * 
	 * @param type the message type.
	 * @param length the message length.
	 * @param contents the message contents.
	 */
	public Message(MessageType type, String source, String destination, byte[] contents) {
		this.type = type;
		this.source = source;
		this.destination = destination;
		this.contents = contents;
	}
	
	public String getSource() {
		return source;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public MessageType getType() {
		return type;
	}
	
	public byte[] getContents() {
		return contents;
	}
	
	@Override
	public String toString() {
		return source + " to " + destination + ": " + new String(contents);
	}
	
}