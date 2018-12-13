package uk.ac.tees.net.util;

import java.io.IOException;
import java.io.InputStream;

public final class StreamUtility {

	private StreamUtility() {
		// prevent instantiation
	}
	
	public static byte[] readByte(InputStream inputStream, int length) throws IOException {
		byte[] array = new byte[length];
		
		inputStream.read(array);
		
		return array;
	}
	
}