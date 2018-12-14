package uk.ac.tees;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import uk.ac.tees.agent.SomeUserAgent;
import uk.ac.tees.agent.UserAgent;
import uk.ac.tees.net.message.StringMessage;
import uk.ac.tees.portal.Portal;

public final class EntryPoint {

	public static void main(String[] args) {
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		
		Portal portal = new Portal("portal1", threadFactory);
		portal.start();
		
		UserAgent userAgent1 = new SomeUserAgent("1", threadFactory);
		UserAgent userAgent2 = new SomeUserAgent("2", threadFactory);
		
		portal.addAgent(userAgent1);
		portal.addAgent(userAgent2);
		
		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNext()) {
				String[] msg = scanner.nextLine().split(":");
				
				UserAgent agent = portal.getAgent(msg[0]);
				
				if (agent == null) {
					System.out.println("Source agent " + msg[0] + " doesn't exist");
					continue;
				}
				
				if (!portal.containsAgent(msg[1])) {
					System.out.println("Destination agent" + msg[1] + " doesn't exist");
					continue;
				}
				
				agent.send(new StringMessage(agent.getUid(), msg[1], msg[2]));
			}
		}
	}

}