package uk.ac.tees.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import uk.ac.tees.net.message.Message;

/**
 * Represents a TCP connection and encapsulate all connection functionality.
 * 
 * @author Sam Hammersley (q5315908)
 */
public final class Connection implements Closeable {

	/**
	 * A {@link Socket} to transmit data
	 */
	private final Socket socket;
	
	/**
	 * Constructs a new {@link Connection} with the specified {@link Socket}.
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public Connection(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Closes the connection.
	 */
	@Override
	public void close() throws IOException {
		socket.close();
	}
	
	/**
	 * Reads a message from the socket
	 * 
	 * @return A decodable {@link Message}
	 */
	public Message read() {
		try (InputStream in = socket.getInputStream()) {
			
			int type = in.read();
			int length = in.read();
			
			byte[] contents = new byte[length];
			
			in.read(contents);
			
			return new Message(type, length, contents);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read message from socket", e);
		}
	}
	
	/**
	 * Writes a message to the socket.
	 * 
	 * @param message the message to write to the socket
	 */
	public void write(Message message) {
		try (OutputStream out = socket.getOutputStream()) {			
			
			out.write(message.getType());
			out.write(message.getLength());
			out.write(message.getContents());
		} catch (IOException e) {
			throw new RuntimeException("Failed to write message to socket", e);
		}
	}
	
}