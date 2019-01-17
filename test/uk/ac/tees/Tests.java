package uk.ac.tees;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import uk.ac.tees.agent.SomeUserAgent;
import uk.ac.tees.agent.UserAgent;
import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.node.portal.Portal;
import uk.ac.tees.node.router.Router;

public class Tests {

	public static void main(String[] args) {
		test(3000, 5, 50, 20);
	}

	static long send(UserAgent userAgent, String destination, String message) {
		long now = System.nanoTime();
		
		userAgent.send(destination, message);
		
		return System.nanoTime() - now;
	}
	
	private static final Random RANDOM = new Random();

	static void test(int messagesPerUser, int numberOfPortals, int numberOfUsers, int numberOfTests) {
		Router router = new Router(NetworkConstants.SERVER_PORT, null);
		router.start();
		
		Map<String, Portal> portals = new HashMap<>();
		
		for (int i = 0; i < numberOfPortals; i++) {
			Portal portal = new Portal("portal" + i, null);
			portal.start();
			portal.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
			
			portals.put(portal.getUid(), portal);
		}
		
		for (int i = 0; i < numberOfUsers; i++) {
			
			for (int j = 0; j < numberOfPortals; j++) {
				UserAgent a = new SomeUserAgent("portal" + j + "agent" + i);
				
				portals.get("portal"+j).addAgent(a);
			}
		}
		
		/**
		 * Warm up tests.
		 */
		Random random = new Random();

		for (int j = 0; j < numberOfPortals; j++) {
			Portal p = portals.get("portal" + j);

			for (int i = 0; i < numberOfUsers; i++) {
				UserAgent sender = p.getAgent(p.getUid() + "agent" + i);

				for (int k = 0; k < messagesPerUser; k++) {
					String portal = "portal" + random.nextInt(numberOfUsers);
					String recipient = portal + "agent" + random.nextInt(numberOfUsers);

					send(sender, recipient, "" + i * k);
				}
			}
		}
		
		/**
		 * Timed tests.
		 */
		long total = 0;	
		for (int x = 0; x < numberOfTests; x++) {
			
			long testTime = 0;
			for (int j = 0; j < numberOfPortals; j++) {
				Portal p = portals.get("portal" + j);

				for (int i = 0; i < numberOfUsers; i++) {
					UserAgent sender = p.getAgent(p.getUid() + "agent" + i);

					for (int k = 0; k < messagesPerUser; k++) {
						String portal = "portal" + RANDOM.nextInt(numberOfUsers);
						String recipient = portal + "agent" + RANDOM.nextInt(numberOfUsers);
					
						testTime += send(sender, recipient, "" + i * k);
					}
				}
			}
			total += testTime / (numberOfPortals * numberOfUsers * messagesPerUser);
			
		}
		System.out.println(total / numberOfTests);
	}

}