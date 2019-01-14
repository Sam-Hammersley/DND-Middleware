package uk.ac.tees.net.message;

import java.util.Arrays;

public enum MessageType {

	STRING(0),
	
	ADD_PORTAL(1),
	
	ADD_AGENT(2),
	
	TERMINATION(-1);
	
	private final int intValue;
	
	MessageType(int intValue) {
		this.intValue = intValue;
	}
	
	public int getIntValue() {
		return intValue;
	}
	
	public static MessageType forValue(int value) {
		return Arrays.stream(values()).filter(m -> m.intValue == value).findAny().orElseThrow(RuntimeException::new);
	}
	
}