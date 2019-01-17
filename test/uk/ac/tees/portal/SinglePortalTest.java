package uk.ac.tees.portal;

import java.util.Scanner;

import uk.ac.tees.agent.SomeUserAgent;
import uk.ac.tees.agent.UserAgent;
import uk.ac.tees.net.message.impl.StringMessage;
import uk.ac.tees.node.portal.Portal;

public final class SinglePortalTest {

	public static void main(String[] args) {
		Portal portal = new Portal("portal", (n, m) -> System.out.println("NodeObserver: " + n.getUid() + " HANDLED:\t" + m));
		portal.start();
		
		SomeUserAgent agent = new SomeUserAgent("user0");
		
		SomeUserAgent agent1 = new SomeUserAgent("user1");
		
		SomeUserAgent agent2 = new SomeUserAgent("user2");

		portal.addAgent(agent);
		portal.addAgent(agent1);
		portal.addAgent(agent2);
		
		System.out.println("Syntax: sourceId,destinationId,message");
		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNextLine()) {
				String[] input = scanner.nextLine().split(",");
				UserAgent a = portal.getAgent(input[0]);
				
				a.send(new StringMessage(a.getUid(), input[1], input[2]));
			}
		}
	}
	
}