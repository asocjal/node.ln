package btcduke.node.ln;

import java.util.Date;

import org.junit.Assert;

import btcduke.node.ln.rpc.commands.DelInvoiceCommand;
import btcduke.node.ln.rpc.commands.InvoiceCommand;
import btcduke.node.ln.rpc.commands.PayCommand;
import btcduke.node.rpc.NodeRpcException;
import btcduke.node.rpc.UnixSocket;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PayTest extends TestCase {

	public PayTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(PayTest.class);
	}

	public void testPay() {

		try (UnixSocket sock = new UnixSocket("/home/cd/.lightning/lightning-rpc")) {

			PayCommand payCommand = new PayCommand("lntb17u1pd9kmympp58uue5jtnax899dwzrw5p9mn63e64hnxkxfr2v6h4j87qtqyyafdqdpzxysy2umswfjhxum0yppk76twypgxzmnwvyz9pr0n6hnz9kty98sjcf9xht2a90k0vuu5za6m68eyj8zdpf82d470x8w3t9pf0jv5z3yv5c76n3hmw3qp2rs0puw5pyhzdgk9paqucqfvy4yc");
			
			long milis = (new Date()).getTime();
			sock.execute(payCommand);
			System.out.println("Execution time: " + ((new Date()).getTime() - milis));
			
			Assert.assertNotEquals("Command is null", null, payCommand);
			Assert.assertNotEquals("Command's request is null", null, payCommand.getRequest());
			Assert.assertNotEquals("Command's response is null", null, payCommand.getResponse());
			Assert.assertNotEquals("Command's result is null", null, payCommand.getResponse().result);
			Assert.assertEquals("Incorrect rhash length", 64, payCommand.getResponse().result.preimage.length());
			
		} catch (NodeRpcException ex) {
			ex.printStackTrace();
			Assert.fail("Exception thrown: '" + ex.getMessage() + "'.");
		}
	}


}
