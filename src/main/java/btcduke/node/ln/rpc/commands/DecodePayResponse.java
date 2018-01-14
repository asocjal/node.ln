package btcduke.node.ln.rpc.commands;

import btcduke.node.rpc.NodeRpcResponse;

public class DecodePayResponse extends NodeRpcResponse {

	public class DecodePayResult {
		public String currency;
		public int timestamp;
		public int expiry;
		public String payee;
		public long msatoshi;
		public String description;
		public String payment_hash;
		public String signature;
	}
	
	public DecodePayResult result;
}
