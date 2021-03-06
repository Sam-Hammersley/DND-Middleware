package uk.ac.tees.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;
import uk.ac.tees.net.message.impl.TerminationMessage;
import uk.ac.tees.net.util.StreamUtility;

/**
 * Represents a connection and encapsulate all required message sending and receiving functionality.
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
	 */
	public Connection(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Gets the address connected to.
	 * 
	 * @return the connected to address
	 */
	public String getAddress() {
		return socket.getInetAddress().toString().substring(1);
	}
	
	/**
	 * Determines whether this connection is closed.
	 * 
	 * @return {@code true} if the connection is closed.
	 */
	public boolean isClosed() {
		return socket.isClosed();
	}
	
	/**
	 * Closes the connection.
	 */
	@Override
	public void close() {
		try {
			socket.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads a message from the socket
	 *
	 * @return A decodable {@link Message}
	 */
	public Message read() {
		try {
			InputStream in = socket.getInputStream();

			MessageType type = MessageType.forValue(in.read());

			if (type == MessageType.TERMINATION) {
				return new TerminationMessage();
			}

			String source = StreamUtility.readString(in, in.read());
			
			String destination = StreamUtility.readString(in, in.read());

			byte[] contents = StreamUtility.readBytes(in, in.read());

			return new Message(type, source, destination, contents);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read message from socket", e);
		}
	}

	/**
	 * Writes a message to the socket.
	 *
	 * @param message
	 *            the message to write to the socket
	 */
	public void write(Message message) {
		try {
			OutputStream out = socket.getOutputStream();

			out.write(message.getType().getIntValue());
			
			StreamUtility.writeString(out, message.getSource());
			
			StreamUtility.writeString(out, message.getDestination());
			
			out.write(message.getContents().length);
			out.write(message.getContents());
			
		} catch (IOException e) {
			throw new RuntimeException(message + " - failed to write message to socket", e);
		}
	}
	
	@Override
	public String toString() {
		return getAddress() + ":" + socket.getLocalPort();
	}
	
	@Override
	public int hashCode() {
		return socket.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Connection)) {
			return false;
		}
		
		Connection other = (Connection) obj;
		
		return socket.equals(other.socket);
	}
	
}