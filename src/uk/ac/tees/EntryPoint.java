package uk.ac.tees;

import java.net.UnknownHostException;

import uk.ac.tees.net.NetworkConstants;

public final class EntryPoint {

	public static void main(String[] args) throws UnknownHostException {
		Router router = new Router(NetworkConstants.SERVER_PORT);
		router.start();
	}

}