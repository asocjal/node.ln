package btcduke.node.ln.rpc.commands;

import btcduke.node.rpc.NodeRpcResponse;

public class InvoiceResponse extends NodeRpcResponse {

	/*
	 * { "rhash" :
	 * "7b6e6d4cbd164fae1ce5e9b70364bdd0b73706867a95874910baf7b54df739d2",
	 * "expiry_time" : 1515931297, "bolt11" :
	 * "lntb100n1pd9kwy3pp50dhx6n9aze86u889axmsxe9a6zmnwp5x022cwjgshtmm2n0h88fqdq42d6hqetjyp4kzam4de5kzcqpx93ss3x89ex5wfpz8npuvfc82glcsag4txhhke2cu4cnw5l05rrc52dc7wfem9m9agxdwagzwa3hacwd8r42ehrcpzp4h8s5q7pqgkqcq6p69e8"
	 * }
	 */
	public class InvoiceResult {
		public String rhash;
		public long expiry_time;
		public String bolt11;
	}

	public InvoiceResult result;
}
