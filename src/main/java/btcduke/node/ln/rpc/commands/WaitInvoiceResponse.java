package btcduke.node.ln.rpc.commands;

import btcduke.node.rpc.NodeRpcResponse;

public class WaitInvoiceResponse extends NodeRpcResponse {

	public class WaitInvoiceResult {
		public String label;
		public String rhash;
		public long msatoshi;
		public boolean complete;
		public int pay_index;
	}

	public WaitInvoiceResult result;
}
