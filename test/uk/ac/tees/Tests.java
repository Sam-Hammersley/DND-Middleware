package uk.ac.tees;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import uk.ac.tees.agent.UserAgent;
import uk.ac.tees.application.SomeUserAgent;
import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.impl.StringMessage;
import uk.ac.tees.node.portal.Portal;
import uk.ac.tees.node.router.Router;

public class Tests {

	public static void main(String[] args) {
		test2();
	}
	
	static void test2() {
		Router router = new Router(NetworkConstants.SERVER_PORT, null);
		router.start();
		
		Portal portal1 = new Portal("portal1", null);
		portal1.start();
		portal1.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		
		Portal portal2 = new Portal("portal2", null);
		portal2.start();
		portal2.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		
		Portal portal3 = new Portal("portal3", null);
		portal3.start();
		portal3.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		
		Portal portal4 = new Portal("portal4", null);
		portal4.start();
		portal4.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		
		Portal portal5 = new Portal("portal5", null);
		portal5.start();
		portal5.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		
		Portal portal6 = new Portal("portal6", null);
		portal6.start();
		portal6.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		
		Portal portal7 = new Portal("portal7", null);
		portal7.start();
		portal7.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		
		Portal portal8 = new Portal("portal8", null);
		portal8.start();
		portal8.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		
		Portal portal9 = new Portal("portal9", null);
		portal9.start();
		portal9.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		
		Portal portal10 = new Portal("portal10", null);
		portal10.start();
		portal10.connectToRouter(NetworkConstants.HOST_ADDRESS, NetworkConstants.SERVER_PORT);
		

		for (int i = 0; i < 50; i++) {
			SomeUserAgent a1 = new SomeUserAgent("portal1agent" + i);
			SomeUserAgent a2 = new SomeUserAgent("portal2agent" + i);
			SomeUserAgent a3 = new SomeUserAgent("portal3agent" + i);
			SomeUserAgent a4 = new SomeUserAgent("portal4agent" + i);
			SomeUserAgent a5 = new SomeUserAgent("portal5agent" + i);
			SomeUserAgent a6 = new SomeUserAgent("portal6agent" + i);
			SomeUserAgent a7 = new SomeUserAgent("portal7agent" + i);
			SomeUserAgent a8 = new SomeUserAgent("portal8agent" + i);
			SomeUserAgent a9 = new SomeUserAgent("portal9agent" + i);
			SomeUserAgent a10 = new SomeUserAgent("portal10agent" + i);
			
			portal1.addAgent(a1);
			portal2.addAgent(a2);
			portal3.addAgent(a3);
			portal4.addAgent(a4);
			portal5.addAgent(a5);
			portal6.addAgent(a6);
			portal7.addAgent(a7);
			portal8.addAgent(a8);
			portal9.addAgent(a9);
			portal10.addAgent(a10);
		}

		/**
		 * Warm up tests.
		 */
		for (int i = 0; i < 50; i++) {
			UserAgent a1 = portal1.getAgent("portal1agent" + i);
			UserAgent a2 = portal2.getAgent("portal2agent" + i);
			UserAgent a3 = portal3.getAgent("portal3agent" + i);
			UserAgent a4 = portal4.getAgent("portal4agent" + i);
			UserAgent a5 = portal5.getAgent("portal5agent" + i);
			UserAgent a6 = portal6.getAgent("portal6agent" + i);
			UserAgent a7 = portal7.getAgent("portal7agent" + i);
			UserAgent a8 = portal8.getAgent("portal8agent" + i);
			UserAgent a9 = portal9.getAgent("portal9agent" + i);
			UserAgent a10 = portal10.getAgent("portal10agent" + i);
			
			for (int j = 0; j < 100; j++) {
				a1.send(new StringMessage(a1.getUid(), "portal2agent" + (int) (Math.random() * 5), "" + i*j));
				a2.send(new StringMessage(a2.getUid(), "portal1agent" + (int) (Math.random() * 5), "" + i*j));
				a3.send(new StringMessage(a3.getUid(), "portal5agent" + (int) (Math.random() * 5), "" + i*j));
				a4.send(new StringMessage(a4.getUid(), "portal6agent" + (int) (Math.random() * 5), "" + i*j));
				a5.send(new StringMessage(a5.getUid(), "portal7agent" + (int) (Math.random() * 5), "" + i*j));
				a6.send(new StringMessage(a6.getUid(), "portal8agent" + (int) (Math.random() * 5), "" + i*j));
				a7.send(new StringMessage(a7.getUid(), "portal9agent" + (int) (Math.random() * 5), "" + i*j));
				a8.send(new StringMessage(a8.getUid(), "portal3agent" + (int) (Math.random() * 5), "" + i*j));
				a9.send(new StringMessage(a9.getUid(), "portal10agent" + (int) (Math.random() * 5), "" + i*j));
				a10.send(new StringMessage(a10.getUid(), "portal4agent" + (int) (Math.random() * 5), "" + i*j));
			}
		}
		
		long total = 0;
		int numberOfTests = 10;
		
		for (int x = 0; x < numberOfTests; x++) {
			long now = System.currentTimeMillis();
	
			for (int i = 0; i < 50; i++) {
				UserAgent a1 = portal1.getAgent("portal1agent" + i);
				UserAgent a2 = portal2.getAgent("portal2agent" + i);
				UserAgent a3 = portal3.getAgent("portal3agent" + i);
				UserAgent a4 = portal4.getAgent("portal4agent" + i);
				UserAgent a5 = portal5.getAgent("portal5agent" + i);
				UserAgent a6 = portal6.getAgent("portal6agent" + i);
				UserAgent a7 = portal7.getAgent("portal7agent" + i);
				UserAgent a8 = portal8.getAgent("portal8agent" + i);
				UserAgent a9 = portal9.getAgent("portal9agent" + i);
				UserAgent a10 = portal10.getAgent("portal10agent" + i);
				
				for (int j = 0; j < 2000; j++) {
					a1.send(new StringMessage(a1.getUid(), "portal2agent" + (int) (Math.random() * 50), "" + i*j));
					a2.send(new StringMessage(a2.getUid(), "portal1agent" + (int) (Math.random() * 50), "" + i*j));
					a3.send(new StringMessage(a3.getUid(), "portal5agent" + (int) (Math.random() * 50), "" + i*j));
					a4.send(new StringMessage(a4.getUid(), "portal6agent" + (int) (Math.random() * 50), "" + i*j));
					a5.send(new StringMessage(a5.getUid(), "portal7agent" + (int) (Math.random() * 50), "" + i*j));
					a6.send(new StringMessage(a6.getUid(), "portal8agent" + (int) (Math.random() * 50), "" + i*j));
					a7.send(new StringMessage(a7.getUid(), "portal9agent" + (int) (Math.random() * 50), "" + i*j));
					a8.send(new StringMessage(a8.getUid(), "portal3agent" + (int) (Math.random() * 50), "" + i*j));
					a9.send(new StringMessage(a9.getUid(), "portal10agent" + (int) (Math.random() * 50), "" + i*j));
					a10.send(new StringMessage(a10.getUid(), "portal4agent" + (int) (Math.random() * 50), "" + i*j));
				}
			}
	
			total += System.currentTimeMillis() - now;
		}

		System.out.println(total / numberOfTests);
	}

	static void test1() {
		Portal portal = new Portal("portal1", null);
		portal.start();
		
		for (int i = 0; i < 500; i++) {
			SomeUserAgent a = new SomeUserAgent("agent" + i);
			//a.addMessageConsumer(Tests::outputToFile);
			
			portal.addAgent(a);
		}

		/**
		 * Warm up tests.
		 */
		for (int i = 0; i < 50; i++) {
			UserAgent a = portal.getAgent("agent" + i);
			
			for (int j = 0; j < 1000; j++) {
				a.send(new StringMessage(a.getUid(), "agent" + (int) (Math.random() * 500), "" + i*j));
			}
		}
		
		long total = 0;
		int numberOfTests = 10;
		
		for (int x = 0; x < numberOfTests; x++) {
			long now = System.currentTimeMillis();
	
			for (int i = 0; i < 50; i++) {
				UserAgent a = portal.getAgent("agent" + i);
	
				for (int j = 0; j < 10000; j++) {
					a.send(new StringMessage(a.getUid(), "agent" + (int) (Math.random() * 500), "" + i * j));
				}
			}
	
			total += System.currentTimeMillis() - now;
		}

		System.out.println(total / numberOfTests);
	}
	
	static BufferedWriter writer = null;
	
	private static final void outputToFile(Message message) {
		try {
			if (writer == null) {
				writer = Files.newBufferedWriter(Paths.get("output.txt")); 
			}
			writer.append(message.toString() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}