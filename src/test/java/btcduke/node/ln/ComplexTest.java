package btcduke.node.ln;

import org.junit.Assert;

import btcduke.node.ln.rpc.commands.InvoiceCommand;
import btcduke.node.ln.rpc.commands.PayCommand;
import btcduke.node.rpc.NodeRpcException;
import btcduke.node.rpc.UnixSocket;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ComplexTest extends TestCase {

	public ComplexTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ComplexTest.class);
	}

	public void testComplex1() {

		try (UnixSocket sock = new UnixSocket("/home/cd/.lightning/lightning-rpc"); UnixSocket sock2 = new UnixSocket("/home/cd/.lightning2/lightning-rpc");) {

			InvoiceCommand invoiceCommand = new InvoiceCommand(20000, "Invoice1", "Pierwszy invoice", 120);
			
			sock.execute(invoiceCommand);
			
			PayCommand payCommand = new PayCommand(invoiceCommand.getResponse().result.bolt11);
			
			sock2.execute(payCommand);
			
						
		} catch (NodeRpcException ex) {
			ex.printStackTrace();
			Assert.fail("Exception thrown: '" + ex.getMessage() + "'.");
		}
	}


}
