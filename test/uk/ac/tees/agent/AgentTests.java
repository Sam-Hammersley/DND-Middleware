package uk.ac.tees.agent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.tees.application.SomeUserAgent;

/**
 *
 * @author s6263464
 */
public class AgentTests {

	@Test
	public void createUserAgent() {

		SomeUserAgent agent = new SomeUserAgent("user2");

		String expectedResult = "user2";

		String actualResult = agent.getUid();

		assertEquals(expectedResult, actualResult);

	}

	public void removeUser() {

		// todo
	}

}