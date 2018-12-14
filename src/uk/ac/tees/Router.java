package uk.ac.tees;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import uk.ac.tees.net.Connection;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;
import uk.ac.tees.net.message.handler.MessageHandlers;

public final class Router implements Runnable {

	private final ThreadFactory threadFactory;

	private final ServerSocket serverSocket;

	private final ExecutorService workerExecutor;
	
	private final Map<String, Connection> portalConnections = new HashMap<>();

	public Router(ThreadFactory threadFactory, ServerSocket serverSocket, ExecutorService workerExecutor) {
		this.threadFactory = threadFactory;
		this.serverSocket = serverSocket;
		this.workerExecutor = workerExecutor;
	}
	
	public void addPortalConnection(String portalUid, Connection connection) {
		portalConnections.put(portalUid, connection);
	}

	public void start() {
		threadFactory.newThread(this).start();
	}

	@Override
	public void run() {
		for (;;) {
			try (Socket socket = serverSocket.accept(); Connection connection = new Connection(socket)) {

				workerExecutor.submit(() -> {
					while (true) {
						Message message = connection.read();

						if (message.getType() == MessageType.TERMINATION) {
							break;
						}
						
						MessageType type = message.getType();
						MessageHandlers.get(type).handleMessage(message, this);
					}
				});
			} catch (IOException e) {
				throw new RuntimeException("Exception thrown when receiving messages", e);
			}
		}
	}

}