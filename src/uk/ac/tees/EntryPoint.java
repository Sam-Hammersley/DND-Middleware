package uk.ac.tees;

import java.net.UnknownHostException;

import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.node.router.Router;

public final class EntryPoint {

	public static void main(String[] args) throws UnknownHostException {
		Router router = new Router(NetworkConstants.SERVER_PORT, (n, m) -> System.out.println("NodeObserver: " + n.getUid() + " HANDLED:\n\t" + m));
		router.start();
	}

}