package btcduke.node.ln;

import org.junit.Assert;

import btcduke.node.ln.rpc.commands.CommandDecodePay;
import btcduke.node.rpc.NodeRpcException;
import btcduke.node.rpc.UnixSocket;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DecodePayTest extends TestCase {

	public DecodePayTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(DecodePayTest.class);
	}


	public void testDecodePay() {
		CommandDecodePay cmdDecodePay = new CommandDecodePay(
				"lntb19u1pd93kn4pp5zvlshe4zl73kzf8q0cc62m3ak3y9hw70ea8z0hyxqptpwjh5e9wsdp8xys9xcmpd3sjqsmgd9czq3njv9c8qatrvd5kumcrshr5j7ewzpkqk3yssdh9y528w8h2urcxsdhkg6puhkp0djh76jhs93akzg82v0qlzt6hg4x0w6hfmkpdgy58wr96zvw4d2w9wn8gycqzce23w");

		/*
		 * { "currency" : "tb", "timestamp" : 1515772533, "expiry" : 3600,
		 * "payee" :
		 * "035b55e3e08538afeef6ff9804e3830293eec1c4a6a9570f1e96a478dad1c86fed",
		 * "msatoshi" : 1900000, "description" : "1 Scala Chip Frappuccino",
		 * "payment_hash" :
		 * "133f0be6a2ffa36124e07e31a56e3db4485bbbcfcf4e27dc860056174af4c95d",
		 * "signature" :
		 * "304402201c2e3a4bd97083605a24841b72928a3b8f757078341b7b2341e5ec17b657f6a5022078163db0907531e0f897aba2a67bb574eec16a0943b865d098eab54e2ba67413"
		 * }
		 */

		try (UnixSocket sock = new UnixSocket("/home/cd/.lightning/lightning-rpc")) {

			sock.execute(cmdDecodePay);
			Assert.assertNotEquals("Command is null", null, cmdDecodePay);
			Assert.assertNotEquals("Command's request is null", null, cmdDecodePay.getRequest());
			Assert.assertNotEquals("Command's response is null", null, cmdDecodePay.getResponse());
			Assert.assertNotEquals("Command's result is null", null, cmdDecodePay.getResponse().result);
			Assert.assertEquals("Incorrect currency", "tb", cmdDecodePay.getResponse().result.currency);
			Assert.assertEquals("Incorrect expiry", 3600, cmdDecodePay.getResponse().result.expiry);
			Assert.assertEquals("Incorrect payee",
					"035b55e3e08538afeef6ff9804e3830293eec1c4a6a9570f1e96a478dad1c86fed",
					cmdDecodePay.getResponse().result.payee);
			Assert.assertEquals("Incorrect amount (msatoshis)", 1900000, cmdDecodePay.getResponse().result.msatoshi);
			Assert.assertEquals("Incorrect description", "1 Scala Chip Frappuccino",
					cmdDecodePay.getResponse().result.description);
			Assert.assertEquals("Incorrect payment hash (hash of R number)",
					"133f0be6a2ffa36124e07e31a56e3db4485bbbcfcf4e27dc860056174af4c95d",
					cmdDecodePay.getResponse().result.payment_hash);
			Assert.assertEquals("Incorrect signature (needed to localize node)",
					"304402201c2e3a4bd97083605a24841b72928a3b8f757078341b7b2341e5ec17b657f6a5022078163db0907531e0f897aba2a67bb574eec16a0943b865d098eab54e2ba67413",
					cmdDecodePay.getResponse().result.signature);

		} catch (NodeRpcException ex) {
			ex.printStackTrace();
			Assert.fail("Exception thrown: '" + ex.getMessage() + "'.");
		}
	}
	
	public void testDecodeInvalidPay() {
		CommandDecodePay cmdDecodePay = new CommandDecodePay(
				"lntb19u1pd93kn4pp5zvlshe4zl73kzf8q0cc62m3ak3y9hw70ea8z0hyxqptpwjh5e9wsdp8xys9xcmpd3sjqsmgd9czq3njv9c8qatrvd5kumcrshr5j7ewzpkqk3yssdh9y528w8h2urcxsdhkg6puhkp0djh76jhs93akzg82v0qlzt6hg4x0w6hfmkpdgy58wr96zvw4d2w9wn8gycqzce23");

		try (UnixSocket sock = new UnixSocket("/home/cd/.lightning/lightning-rpc")) {

			sock.execute(cmdDecodePay);
			
			Assert.fail("Exception not thrown for decode invalid pay");
			
		} catch (NodeRpcException ex) {
			Assert.assertNotEquals("Command is null", null, cmdDecodePay);
			Assert.assertNotEquals("Command's request is null", null, cmdDecodePay.getRequest());
			Assert.assertNotEquals("Command's response is null", null, cmdDecodePay.getResponse());	
			Assert.assertEquals("Error message incorrect", "Invalid bolt11: Bad bech32 string", cmdDecodePay.getResponse().error);
			Assert.assertEquals("Command's result is not null", null, cmdDecodePay.getResponse().result);

			Assert.assertNotNull("Exception cause is null", ex.getCause());
			Assert.assertEquals("Server cause do not equals cause", ex.getCause(), ex.getServerCause());
			Assert.assertEquals("Exception message incorrect", "Invalid bolt11: Bad bech32 string", ex.getServerCause().getMessage());
//			Assert.assertEquals("Exception message incorrect", "{\"id\":183723173,\"jsonrpc\":\"2.0\",\"method\":\"decodepay\",\"params\":[\"lntb19u1pd93kn4pp5zvlshe4zl73kzf8q0cc62m3ak3y9hw70ea8z0hyxqptpwjh5e9wsdp8xys9xcmpd3sjqsmgd9czq3njv9c8qatrvd5kumcrshr5j7ewzpkqk3yssdh9y528w8h2urcxsdhkg6puhkp0djh76jhs93akzg82v0qlzt6hg4x0w6hfmkpdgy58wr96zvw4d2w9wn8gycqzce23\"]}", serverException.getRequestJson());
			Assert.assertEquals("Exception message incorrect", "{ \"jsonrpc\": \"2.0\",  \"error\" : \"Invalid bolt11: Bad bech32 string\", \"id\" : 183723173 }", ex.getServerCause().getResponseJson());
			Assert.assertEquals("Exception doesn't contain correct command", cmdDecodePay, ex.getServerCause().getCommand());
		
		}
	}
	
//	Na razie zacommentowane bo crashuje c-lightning
	
//	public void testDecodeEmptyPay() {
//		CommandDecodePay cmdDecodePay = new CommandDecodePay("");
//
//		try (UnixSocket sock = new UnixSocket("/home/cd/.lightning/lightning-rpc")) {
//
//			sock.execute(cmdDecodePay);
//			
//			Assert.assertNotEquals("Command is null", null, cmdDecodePay);
//			Assert.assertNotEquals("Command's request is null", null, cmdDecodePay.getRequest());
//			Assert.assertNotEquals("Command's response is null", null, cmdDecodePay.getResponse());	
//			Assert.assertEquals("Error message incorrect", "Invalid bolt11: Bad bech32 string", cmdDecodePay.getResponse().error);
//			Assert.assertEquals("Command's result is not null", null, cmdDecodePay.getResponse().result);
//
//		} catch (NodeRpcException ex) {
//			ex.printStackTrace();
//		}
//	}
}
