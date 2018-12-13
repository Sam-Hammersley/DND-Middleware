package uk.ac.tees;

import uk.ac.tees.agent.UserAgent;

public class SomeUserAgent extends UserAgent {

	protected SomeUserAgent(String uid) {
		super(uid, System.out::println);
	}

}