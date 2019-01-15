package uk.ac.tees.portal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.tees.application.SomeUserAgent;
import uk.ac.tees.node.portal.Portal;

/**
 *
 * @author s6263464
 */
public class PortalTests {

	@Test
	public void getConnectionAmont() {
		Portal portal = new Portal("portal2", null);

		SomeUserAgent agent = new SomeUserAgent("user");
		SomeUserAgent agent2 = new SomeUserAgent("user2");
		portal.addAgent(agent);
		portal.addAgent(agent2);

		portal.start();
		int expectedResult = 2;
		int actualResult = portal.getAgentCount();

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void addUser() {

		Portal portal = new Portal("portal2", null);

		SomeUserAgent agent = new SomeUserAgent("user");

		portal.addAgent(agent);

		portal.start();

		boolean expectedResult = true;
		boolean actualResult = portal.getAgent("user").equals(agent);

		assertEquals(expectedResult, actualResult);
	}
}