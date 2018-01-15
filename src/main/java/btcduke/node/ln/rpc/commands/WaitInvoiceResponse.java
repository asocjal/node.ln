package btcduke.node.ln.rpc.commands;

import btcduke.node.rpc.NodeRpcResponse;

public class WaitInvoiceResponse extends NodeRpcResponse {

	public class WaitInvoiceResult {
		public String rhash;
		public long expiry_time;
		public String bolt11;
	}

	public WaitInvoiceResult result;
}
