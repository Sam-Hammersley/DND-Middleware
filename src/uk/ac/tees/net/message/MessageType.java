package uk.ac.tees.net.message;

import java.util.Arrays;

public enum MessageType {

	STRING_MESSAGE(0),
	
	ADD_PORTAL_MESSAGE(1),
	
	ADD_AGENT(2),
	
	TERMINATION(-1);
	
	private final int type;
	
	MessageType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static MessageType forType(int type) {
		return Arrays.stream(values()).filter(v -> v.type == type).findAny().orElseThrow(() -> new RuntimeException("Unsupported message type"));
	}

}