package btcduke.node.ln;

import org.junit.Assert;

import btcduke.node.ln.rpc.commands.WaitInvoiceCommand;
import btcduke.node.rpc.NodeRpcException;
import btcduke.node.rpc.UnixSocket;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WaitInvoiceTest extends TestCase {

	public WaitInvoiceTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(WaitInvoiceTest.class);
	}

	public void testWaitInvoice() {
		
		try (UnixSocket sock2 = new UnixSocket("/home/cd/.lightning/lightning-rpc")) {

			WaitInvoiceCommand waitInvoiceCommand = new WaitInvoiceCommand("kawka3");

			sock2.execute(waitInvoiceCommand);
			
			Assert.assertNotEquals("Command is null", null, waitInvoiceCommand);
			Assert.assertNotEquals("Command's request is null", null, waitInvoiceCommand.getRequest());
			Assert.assertNotEquals("Command's response is null", null, waitInvoiceCommand.getResponse());
			Assert.assertNotEquals("Command's result is null", null, waitInvoiceCommand.getResponse().result);
			Assert.assertNotEquals("Label is null", null, waitInvoiceCommand.getResponse().result.label);
			Assert.assertNotEquals("Label is empty", waitInvoiceCommand.getResponse().result.label.length() > 0);
			Assert.assertNotEquals("Rhash has wrong size", waitInvoiceCommand.getResponse().result.rhash.length() == 64);
			Assert.assertNotEquals("msatoshi is not positive value", waitInvoiceCommand.getResponse().result.msatoshi > 0);
			Assert.assertNotEquals("Pay index is negative value", waitInvoiceCommand.getResponse().result.pay_index >= 0);
			Assert.assertNotEquals("Complete is not true", waitInvoiceCommand.getResponse().result.complete == true);

			
			
		} catch (NodeRpcException ex) {
			ex.printStackTrace();
			Assert.fail("Exception thrown: '" + ex.getMessage() + "'.");
		}
	}


}
