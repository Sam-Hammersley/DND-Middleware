package uk.ac.tees;

import java.util.Scanner;

import uk.ac.tees.agent.UserAgent;
import uk.ac.tees.net.message.StringMessage;
import uk.ac.tees.portal.Portal;

public final class EntryPoint {

	public static void main(String[] args) {
		Portal portal = new Portal("portal1");
		new Thread(portal).start();
		
		UserAgent userAgent1 = new SomeUserAgent("1");
		UserAgent userAgent2 = new SomeUserAgent("2");
		
		portal.addAgent(userAgent1);
		portal.addAgent(userAgent2);
		
		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNext()) {
				String msg = scanner.nextLine();
				userAgent1.send(new StringMessage("1", "2", msg));				
			}
		}
	}

}