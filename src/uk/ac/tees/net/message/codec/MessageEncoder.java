package uk.ac.tees.net.message.codec;

import uk.ac.tees.net.message.Message;

/**
 * Encodes an object as a message
 * 
 * @author Sam Hammersley (q5315908)
 *
 * @param <T> the type of object to encode
 */
public interface MessageEncoder<T> {

	/**
	 * Encodes an object of type {@link T} as a message
	 * 
	 * @param t the object to encode
	 * 
	 * @return a {@link Message} that can be sent to an agent
	 */
	public Message encode(T t);

}