package uk.ac.tees;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import uk.ac.tees.agent.SomeUserAgent;
import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.net.message.impl.StringMessage;
import uk.ac.tees.portal.Portal;

public final class Client {

	public static void main(String[] args) {
		
		ThreadFactory tf = Executors.defaultThreadFactory();
		Portal portal = new Portal("portal2", tf);
		
		SomeUserAgent agent = new SomeUserAgent("user2", tf);
		portal.addAgent(agent);
		
		portal.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		portal.start();
		
		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNextLine()) {
				String[] input = scanner.nextLine().split(",");
				
				agent.send(new StringMessage(agent.getUid(), input[0], input[1]));
			}
		}
	}
	
}