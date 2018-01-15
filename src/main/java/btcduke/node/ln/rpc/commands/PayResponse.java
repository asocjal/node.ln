package btcduke.node.ln.rpc.commands;

import btcduke.node.rpc.NodeRpcResponse;

public class PayResponse extends NodeRpcResponse {

	// "preimage" : "00e78b27b0417d7694c780d59e21329c9215dceecb2c4998598c7749cf2475cb"
	
	public class PayResult {
		public String preimage;
	}

	public PayResult result;
}
