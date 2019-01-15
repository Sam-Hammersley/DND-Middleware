package uk.ac.tees.application;

import java.util.Scanner;

import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.net.message.impl.StringMessage;
import uk.ac.tees.node.portal.Portal;

public final class Client {

	public static void main(String[] args) {
		
		Portal portal = new Portal("portal1", (n, m) -> System.out.println("NodeObserver: " + n.getUid() + " HANDLED:\n\t" + m));
		
		SomeUserAgent agent = new SomeUserAgent("user1");
		agent.addMessageConsumer(System.out::println);
		
		portal.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		portal.start();
		portal.addAgent(agent);
		
		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNextLine()) {
				String[] input = scanner.nextLine().split(",");
				
				agent.send(new StringMessage(agent.getUid(), input[0], input[1]));
			}
		}
	}
	
}