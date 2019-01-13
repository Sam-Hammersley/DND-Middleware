package uk.ac.tees.agent;

import java.util.concurrent.ThreadFactory;

public class SomeUserAgent extends UserAgent {

	public SomeUserAgent(String uid, ThreadFactory threadFactory) {
		super(uid, threadFactory);
	}

}