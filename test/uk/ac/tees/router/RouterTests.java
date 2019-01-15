package uk.ac.tees.router;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.node.router.Router;

/**
 *
 * @author s6263464
 */
public class RouterTests {

	@Test
	public void connectToRouter() {
		Router router = new Router(NetworkConstants.SERVER_PORT, null);
		router.start();

		String expectedResult = "152.105.67.102";
		String actualResult = router.getUid();

		assertEquals(expectedResult, actualResult);

	}

}
