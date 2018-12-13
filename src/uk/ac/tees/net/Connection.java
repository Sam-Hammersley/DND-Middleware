package uk.ac.tees.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;
import uk.ac.tees.net.message.TerminationMessage;
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
		try {
			InputStream in = socket.getInputStream();

			MessageType type = MessageType.forType(in.read());

			if (type == MessageType.TERMINATION) {
				return new TerminationMessage();
			}
			
			byte[] srcRaw = StreamUtility.readByte(in, in.read());
			String source = new String(srcRaw);

			byte[] dstRaw = StreamUtility.readByte(in, in.read());
			String destination = new String(dstRaw);

			byte[] contents = StreamUtility.readByte(in, in.read());

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
		if (message.getType() == MessageType.TERMINATION) {
			throw new RuntimeException("Unsuppported message");
		}
		try {
			OutputStream out = socket.getOutputStream();

			out.write(message.getType().getType());
			out.write(message.getSource().getBytes());
			out.write(message.getDestination().getBytes());
			out.write(message.getDestination().getBytes());
			out.write(message.getContents());
		} catch (IOException e) {
			throw new RuntimeException("Failed to write message to socket", e);
		}
	}

}