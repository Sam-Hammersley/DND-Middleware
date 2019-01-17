package uk.ac.tees.router;

import java.net.UnknownHostException;

import uk.ac.tees.agent.SomeUserAgent;
import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.node.portal.Portal;
import uk.ac.tees.node.router.Router;

public final class PortalToPortalTest {

	public static void main(String[] args) throws UnknownHostException {
		Router router = new Router(NetworkConstants.SERVER_PORT, (n, m) -> System.out.println("NodeObserver: " + n.getUid() + " HANDLED:" + m));
		router.start();
		
		Portal portal1 = new Portal("portal1", null);
		SomeUserAgent agent1 = new SomeUserAgent("u1");
		portal1.addAgent(agent1);
		portal1.start();
		portal1.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		
		Portal portal2 = new Portal("portal2", null);
		SomeUserAgent agent2 = new SomeUserAgent("u2");
		portal2.addAgent(agent2);
		portal2.start();
		portal2.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		
		agent1.send("u2", "hello");
	}

}