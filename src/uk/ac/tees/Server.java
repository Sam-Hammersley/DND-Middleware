package uk.ac.tees;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.net.ServerSocketFactory;

import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.net.util.SocketUtility;

public final class Server {

	private final ThreadFactory threadFactory;

	private final ServerSocket serverSocket;
	
	private final ExecutorService workerExecutor;

	public Server(ThreadFactory threadFactory, ServerSocket serverSocket, ExecutorService workerExecutor) {
		this.threadFactory = threadFactory;
		this.serverSocket = serverSocket;
		this.workerExecutor = workerExecutor;
	}

	public void start() {
		threadFactory.newThread(new ServerProcess(serverSocket, workerExecutor)).start();
	}

	public static void main(String[] args) {
		ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
		ServerSocket ss = SocketUtility.createServerSocket(serverSocketFactory, NetworkConstants.SERVER_PORT);

		Server server = new Server(Executors.defaultThreadFactory(), ss, Executors.newCachedThreadPool());
		server.start();
	}

}