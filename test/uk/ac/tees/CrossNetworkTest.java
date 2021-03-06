package uk.ac.tees;

import java.util.Scanner;

import uk.ac.tees.agent.SomeUserAgent;
import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.net.message.impl.StringMessage;
import uk.ac.tees.node.portal.Portal;

public final class CrossNetworkTest {

	public static void main(String[] args) {
		
		Portal portal = new Portal("portal2", (n, m) -> System.out.println("NodeObserver: " + n.getUid() + " RECEIVED " + m));
		
		SomeUserAgent agent = new SomeUserAgent("user");
		
		portal.addAgent(agent);
		
		portal.connectToRouter("152.105.67.111", NetworkConstants.SERVER_PORT);
		portal.start();
		
		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNextLine()) {
				String[] input = scanner.nextLine().split(",");
				
				agent.send(new StringMessage(agent.getUid(), input[0], input[1]));
			}
		}

	}
	
}