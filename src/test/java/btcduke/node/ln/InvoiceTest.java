package btcduke.node.ln;

import java.util.Date;

import org.junit.Assert;

import btcduke.node.ln.rpc.commands.CommandInvoice;
import btcduke.node.rpc.NodeRpcException;
import btcduke.node.rpc.UnixSocket;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class InvoiceTest extends TestCase {

	public InvoiceTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(InvoiceTest.class);
	}

	public void testInvoice() {
		CommandInvoice cmdInvoice = new CommandInvoice(134001, "Czysta", "2 cwiarti czystej", 30);
		/*
		 * { "rhash" :
		 * "38daf8692a600c3a33eb66ec20bd069d463ea2b3295bf02e9aac981240e79812",
		 * "expiry_time" : 1515932050, "bolt11" :
		 * "38daf8692a600c3a33eb66ec20bd069d463ea2b3295bf02e9aac981240e7981238daf8692a600c3a33eb66ec20bd069d463ea2b3295bf02e9aac981240e7981238daf8692a600c3a33eb66ec20bd069d463ea2b3295bf02e9aac981240e79812
		 * "lntb100n1pd9kwuzpp58rd0s6f2vqxr5vltvmkzp0gxn4rrag4n99dlqt564jvpys88nqfqdq42d6hqetjyp4kzam4de5kzcqpxn6er444srlpnq2lqryxfsqdntzq6dgqk8xvfddp3lkr84z8z6jerghal2yqe6jcejczqr7rpce73an2h8nwfdqa8w9nyd8tqx3uefmsp7xw7t5"
		 * }
		 * 
		 */

		try (UnixSocket sock = new UnixSocket("/home/cd/.lightning/lightning-rpc")) {

			long minExpiration = ((new Date()).getTime()/1000) + 30;
			System.out.println(minExpiration);
			
			sock.execute(cmdInvoice);
			
			Assert.assertNotEquals("Command is null", null, cmdInvoice);
			Assert.assertNotEquals("Command's request is null", null, cmdInvoice.getRequest());
			Assert.assertNotEquals("Command's response is null", null, cmdInvoice.getResponse());
			Assert.assertNotEquals("Command's result is null", null, cmdInvoice.getResponse().result);
			Assert.assertEquals("Incorrect rhash length", 64, cmdInvoice.getResponse().result.rhash.length());																						// sofisticated
			Assert.assertTrue("Incorrect expiry time", cmdInvoice.getResponse().result.expiry_time > minExpiration); // TODO:
			Assert.assertTrue("Incorrect bolt11 length", cmdInvoice.getResponse().result.bolt11.length() > 196);

		} catch (NodeRpcException ex) {
			ex.printStackTrace();
			Assert.fail("Exception thrown: '" + ex.getMessage() + "'.");
		}
	}


}
