package uk.ac.tees;

import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import uk.ac.tees.net.NetworkConstants;

public final class EntryPoint {

	public static void main(String[] args) throws UnknownHostException {
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		
		Router router = new Router(NetworkConstants.SERVER_PORT, threadFactory);
		router.start();
	}

}