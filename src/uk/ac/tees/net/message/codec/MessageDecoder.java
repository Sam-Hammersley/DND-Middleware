package uk.ac.tees.net.message.codec;

import uk.ac.tees.net.message.Message;

/**
 * Decodes incoming messages
 * 
 * @author Sam Hammersley (q5315908)
 *
 * @param <T> the type of object to create from the received message
 */
public interface MessageDecoder<T> {

	/**
	 * Defines how a message should be decoded into an object of specified type
	 * 
	 * @param message the message to be decoded
	 * 
	 * @return an object of type {@link T}
	 */
	public T decode(Message message);
	
}