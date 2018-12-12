package uk.ac.tees.net.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

public class SocketUtil {

	private SocketUtil() {
		
	}
	
	public static ServerSocket createServerSocket(int port) {
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		
		try {
			return factory.createServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException("Failed to create server socket on port " + port, e);
		}
	}
	
	public static Socket createSocket(String host, int port) {
		SocketFactory factory = SocketFactory.getDefault();
		
		try {
			return factory.createSocket(host, port);
		} catch (IOException e) {
			throw new RuntimeException("Failed to create socket on host " + host + " and port " + port);
		}
	}
	
}