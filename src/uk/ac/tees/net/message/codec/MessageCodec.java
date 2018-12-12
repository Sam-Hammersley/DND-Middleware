package uk.ac.tees.net.message.codec;

import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.net.message.MessageType;

public final class MessageCodec {
	
	//prevent instantiation
	private MessageCodec() {
		
	}
	
	private static final Map<MessageType, MessageEncoder<?>> ENCODERS;
	
	private static final Map<MessageType, MessageDecoder<?>> DECODERS;
	
	static {
		ENCODERS = new HashMap<>();
		DECODERS = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> MessageEncoder<T> encoder(MessageType type) {
		return (MessageEncoder<T>) ENCODERS.get(type);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> MessageDecoder<T> decoder(MessageType type) {
		return (MessageDecoder<T>) DECODERS.get(type);
	}
	
}