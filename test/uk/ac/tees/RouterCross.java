package uk.ac.tees;

import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.node.router.Router;

public class RouterCross {

	public static void main(String[] args) {
		Router router = new Router(NetworkConstants.SERVER_PORT, (n, m) -> System.out.println("NodeObserver: " + n.getUid() + " RECEIVED " + m));
		router.start();
	}

}
