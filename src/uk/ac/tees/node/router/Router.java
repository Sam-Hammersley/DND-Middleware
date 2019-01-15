package uk.ac.tees.node.router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;

import uk.ac.tees.net.Connection;
import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;
import uk.ac.tees.net.message.impl.StringMessage;
import uk.ac.tees.net.util.SocketUtility;
import uk.ac.tees.node.Node;
import uk.ac.tees.node.NodeObserver;
import uk.ac.tees.node.router.message.SystemMessageHandler;
import uk.ac.tees.node.router.message.SystemMessageHandlers;

/**
 * Represents a router that takes in connections
 * 
 * @author q5315908
 */
public class Router extends Node {
	
	/**
	 * {@link ExecutorService} to run concurrent tasks on.
	 */
	protected final ExecutorService executor = Executors.newCachedThreadPool();
	
	/**
	 * The port this router is running on.
	 */
	private final int port;
	
	/**
	 * Manages connections for this router.
	 */
	private final ConnectionManager connections = new ConnectionManager();

	/**
	 * Constructs a new {@link Router}
	 *
	 * @param port the port to run this service on.
	 */
	public Router(int port, NodeObserver observer) {
		super(NetworkConstants.HOST_ADDRESS, observer);
		this.port = port;
	}
	
	public ConnectionManager getConnections() {
		return connections;
	}

	@Override
	public void receiveMessages() {
		ServerSocket serverSocket = SocketUtility.createServerSocket(ServerSocketFactory.getDefault(), port);

		System.out.println("Listening on port " + port);
		for (;;) {
			try {
				Socket socket = serverSocket.accept();
				
				readFromSocket(socket);
			} catch (IOException e) {
				throw new RuntimeException("Socket exception thrown", e);
			}
		}
	}

	/**
	 * Creates thread for each connection and reads a {@link Message} 
	 * from the {@link Socket} via the connection.
	 * 
	 * Thread-per-Connection design.
	 * 
	 * @param socket the socket to receive a message from.
	 */
	public final void readFromSocket(Socket socket) {
		executor.submit(() -> {
			try (Connection connection = new Connection(socket)) {
				
				Message message = connection.read();

				SystemMessageHandler handler = SystemMessageHandlers.get(message.getType());

				if (handler != null) {
					handler.handleMessage(this, message, connection);
				}
				
				// receive messages whilst the type is not termination
				for (; !message.getType().equals(MessageType.TERMINATION); message = connection.read()) {
					queue(message);
				}
	
			} catch (Exception e) {
				throw new RuntimeException("Exception thrown during connection", e);
			}
		});
	}

	@Override
	public void send(Message message) {
		if (!connections.hasConnection(message.getDestination())) { // destination not found

			Optional<Connection> sourceConnection = connections.getConnection(message.getSource());
			
			if (sourceConnection.isPresent()) { // if we still have connection to source send back response
				StringMessage response = new StringMessage(message.getSource(), uid, "Destination (" + message.getDestination() + ") not found!");
				
				sourceConnection.get().write(response);
			}
			return;
		}
		
		Connection connection = connections.getConnection(message.getDestination()).get();
			
		connection.write(message);
	}

}