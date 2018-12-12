package uk.ac.tees.net.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

public final class SocketUtility {

	private SocketUtility() {
		// prevent instantiation	
	}
	
	public static ServerSocket createServerSocket(ServerSocketFactory serverSocketFactory, int port) {
		try {
			return serverSocketFactory.createServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException("Failed to create server socket on " + port, e);
		}
	}
	
	public static Socket createSocket(SocketFactory socketFactory, String host, int port) {
		try {
			return socketFactory.createSocket(host, port);
		} catch (IOException e) {
			throw new RuntimeException("Failed to create socket on " + host + ":" + port, e);
		}
	}
	
}