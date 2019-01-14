package uk.ac.tees.node.router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;

import uk.ac.tees.net.Connection;
import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;
import uk.ac.tees.net.message.handler.RouterMessageHandler;
import uk.ac.tees.net.message.handler.RouterMessageHandlers;
import uk.ac.tees.net.message.impl.StringMessage;
import uk.ac.tees.net.util.SocketUtility;
import uk.ac.tees.node.Node;
import uk.ac.tees.node.NodeObserver;

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
	 * Maps uids to connections, includes a portals user agent ids.
	 */
	private final Map<HashSet<String>, Connection> cachedConnections = new ConcurrentHashMap<>();

	/**
	 * Constructs a new {@link Router}
	 *
	 * @param port the port to run this service on.
	 */
	public Router(int port, NodeObserver observer) {
		super(NetworkConstants.HOST_ADDRESS, observer);
		
		this.port = port;
	}

	/**
	 * Gets a connection for an id
	 * 
	 * @param uid the id
	 * @return a {@link Connection}
	 */
	public Optional<Connection> getConnection(String uid) {
		return cachedConnections.entrySet().stream().filter(e -> e.getKey().contains(uid)).map(e -> e.getValue()).findAny();
	}

	/**
	 * Gets the whole key of a connection for a uid.
	 * 
	 * @param uid one of the ids associated with the connection.
	 * @return an associated {@link Connection} with the given uid. 
	 */
	public Optional<HashSet<String>> getConnectionKey(String uid) {
		return cachedConnections.keySet().stream().filter(s -> s.contains(uid)).findAny();
	}

	/**
	 * Stores a {@link Connection}.
	 * 
	 * @param uids the uids associated with the given {@link Connection}.
	 * @param connection the {@link Connection}.
	 */
	public boolean storeConnection(String uid, Connection connection) {
		Optional<HashSet<String>> ids = getConnectionKey(uid);
		
		if (!ids.isPresent()) {
			HashSet<String> set = new HashSet<>();
			set.add(uid);
			
			cachedConnections.put(set, connection);
		}
		
		return !ids.isPresent();
	}

	/**
	 * Determines whether the uid is associated with any connections.
	 * 
	 * @param uid the id to check connections for.
	 * @return {@code true} if there is an associated connection.
	 */
	public boolean hasConnection(String uid) {
		return cachedConnections.keySet().stream().anyMatch(s -> s.contains(uid));
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
					if (!storeConnection(message.getSource(), connection)) {
						connection.write(new StringMessage(uid, message.getSource(), "Already contains connection for " + uid));
						connection.close();
						return;
					}

				} else if (!hasConnection(message.getSource())) {
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
		Optional<Connection> connection = getConnection(message.getDestination());
		
		connection.ifPresent(c -> c.write(message));
	}

	@Override
	public void handleMessage(Message message) {
		RouterMessageHandler handler = RouterMessageHandlers.get(message.getType());

		if (handler != null) {
			handler.handleMessage(this, message);
		} else {
			System.out.println(message);
		}

		if (!message.getDestination().equals(uid)) {
			send(message);
		}
	}

}