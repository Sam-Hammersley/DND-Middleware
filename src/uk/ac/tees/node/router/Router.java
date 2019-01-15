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
import uk.ac.tees.node.router.message.RouterMessageHandler;
import uk.ac.tees.node.router.message.RouterMessageHandlers;

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
	
	public ConnectionManager getConnectionManager() {
		return connections;
	}

	@Override
	public void receiveMessages() {
		ServerSocket serverSocket = SocketUtility.createServerSocket(ServerSocketFactory.getDefault(), port);

		System.out.println("Listening on port " + port);
		for (;;) {
			try {
				Socket socket = serverSocket.accept();

				readSocket(socket);
			} catch (IOException e) {
				throw new RuntimeException("Socket exception thrown", e);
			}
		}
	}

	/**
	 * Receives a {@link Message} from a {@link Socket}.
	 * 
	 * Thread-per-Connection design.
	 * 
	 * @param socket the socket to receive a message from.
	 */
	public final void readSocket(Socket socket) {
		executor.submit(() -> {
			
			try (Connection connection = new Connection(socket)) {

				System.out.println("Connection established with " + connection.getAddress());
				Message message = connection.read();
				
				if (message.getType().equals(MessageType.ADD_PORTAL)) {// special case.
					if (!connections.storeConnection(message.getSource(), connection)) {
						connection.write(new StringMessage(uid, message.getSource(), "Already contains connection for " + uid));
						connection.close();
						return;
					}

				} else if (!connections.hasConnection(message.getSource())) {
					connection.write(new StringMessage(uid, message.getSource(), "Connection refused, handshake required"));
					connection.close();
					return;
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
		Optional<Connection> connection = connections.getConnection(message.getDestination());
		

		if (connection.isPresent()) {
			connection.ifPresent(c -> c.write(message));
			
		}
		
	}

	@Override
	public void handleMessage(Message message) {

		if (!message.getDestination().equals(uid)) { // destination is not this router
			send(message);
			
		} else {
			RouterMessageHandler handler = RouterMessageHandlers.get(message.getType());
			
			if (handler != null) {
				handler.handleMessage(this, message);
			}
		}
	}

}