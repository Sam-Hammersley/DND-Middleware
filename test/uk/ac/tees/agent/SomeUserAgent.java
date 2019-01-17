package uk.ac.tees.agent;

import uk.ac.tees.agent.UserAgent;

public class SomeUserAgent extends UserAgent {

	public SomeUserAgent(String uid) {
		super(uid);
		addMessageConsumer(System.out::println);
	}

}