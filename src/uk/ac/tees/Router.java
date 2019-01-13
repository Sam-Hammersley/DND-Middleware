package uk.ac.tees;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.net.ServerSocketFactory;

import uk.ac.tees.agent.MetaAgent;
import uk.ac.tees.net.Connection;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;
import uk.ac.tees.net.message.handler.MessageHandler;
import uk.ac.tees.net.message.handler.MessageHandlers;
import uk.ac.tees.net.message.impl.StringMessage;
import uk.ac.tees.net.util.SocketUtility;

public class Router extends MetaAgent {
	
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

	public Router(String uid, int port, ThreadFactory threadFactory) {
		super(uid, threadFactory);
		this.port = port;
	}
	
	/**
	 * Gets a connection for an id
	 * 
	 * @param uid the id
	 * @return a {@link Connection}
	 */
	public Optional<Connection> getConnection(String uid) {
		return cachedConnections.keySet().stream().filter(s -> s.contains(uid)).map(cachedConnections::get).findAny();
	}
	
	public Optional<HashSet<String>> getConnectionKey(String uid) {
		return cachedConnections.keySet().stream().filter(s -> s.contains(uid)).findAny();
	}
	
	public void storeConnection(HashSet<String> uids, Connection connection) {
		cachedConnections.put(uids, connection);
	}

	public boolean hasConnection(String uid) {
		return cachedConnections.keySet().stream().anyMatch(s -> s.contains(uid));
	}

	@Override
	public void start() {
		super.start();
		
		threadFactory.newThread(() -> {
			ServerSocket serverSocket = SocketUtility.createServerSocket(ServerSocketFactory.getDefault(), port);
			
			for (;;) {
				try {
					Socket socket = serverSocket.accept();
	
					readSocket(socket);
				} catch (IOException e) {
					throw new RuntimeException("Socket exception thrown", e);
				}
			}
		}).start();
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
				
				if (message.getType().equals(MessageType.ADD_PORTAL_MESSAGE)) {// special case.
					HashSet<String> ids = new HashSet<>();
					
					ids.add(message.getSource());
					storeConnection(ids, connection);
					
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
	protected void handle(Message message) {
		MessageHandler<Router> handler = MessageHandlers.get(message.getType());
		
		if (handler != null) {
			handler.handleMessage(this, message);
		} else {
			System.out.println(message);
		}
	}

}