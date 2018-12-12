package uk.ac.tees.net.message;

/**
 * Represents the messages sent from one agent to another
 * 
 * @author Sam Hammersley (q5315908)
 */
public class Message {

	/**
	 * The type of message
	 */
	private final MessageType type;
	
	/**
	 * The length of the contents of the message
	 */
	private final int length;
	
	/**
	 * The contents of the message
	 */
	private final byte[] contents;
	
	/**
	 * Constructs a new {@link Message} with the given parameters
	 * 
	 * @param type the message type.
	 * @param length the message length.
	 * @param contents the message contents.
	 */
	public Message(MessageType type, int length, byte[] contents) {
		this.type = type;
		this.length = length;
		this.contents = contents;
	}
	
	public MessageType getType() {
		return type;
	}
	
	public int getLength() {
		return length;
	}
	
	public byte[] getContents() {
		return contents;
	}
	
	@Override
	public String toString() {
		return type + ", " + length + ", " + new String(contents);
	}
	
}