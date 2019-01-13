package uk.ac.tees.net.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public final class StreamUtility {

	private StreamUtility() {
		// prevent instantiation
	}
	
	public static String readString(InputStream inputStream, int length) throws IOException {
		return readString(inputStream, Charset.defaultCharset(), length);
	}
	
	public static String readString(InputStream inputStream, Charset charset, int length) throws IOException {
		byte[] array = new byte[length];
		
		inputStream.read(array);
		
		return new String(array, charset);
	}
	
	public static byte[] readBytes(InputStream inputStream, int length) throws IOException {
		byte[] array = new byte[length];
		
		inputStream.read(array);
		
		return array;
	}
	
	public static void writeString(OutputStream outputStream, String string) throws IOException {
		outputStream.write(string.length());
		outputStream.write(string.getBytes());
	}
	
}