package uk.ac.tees.net;

import java.io.IOException;
import java.net.InetAddress;

public final class NetworkConstants {

	/**
	 * Prevents instantiation
	 */
	private NetworkConstants() {
		
	}
	
	public static final int SERVER_PORT = 44455;
	
	public static final String HOST_ADDRESS;
	
	static {
		try {
			HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();
		} catch (IOException e) {
			throw new RuntimeException("Failed to get host address", e);
		}
	}
	
}