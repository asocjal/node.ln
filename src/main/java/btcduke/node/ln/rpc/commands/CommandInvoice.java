package btcduke.node.ln.rpc.commands;

import btcduke.node.rpc.Command;
import btcduke.node.rpc.NodeRpcRequest;

public class CommandInvoice extends Command<InvoiceResponse> {

	// { "command" : "invoice", "description" : "Create invoice for {msatoshi}
	// with {label} and {description} with optional {expiry} seconds (default 1
	// hour)" },

	public CommandInvoice(long msatoshi, String label, String description, int expiry) {
		request = new NodeRpcRequest("invoice");
		request.params = new String[4];
		request.params[0] = Long.toOctalString(msatoshi);
		request.params[1] = label;
		request.params[2] = description;
		request.params[3] = Integer.toOctalString(expiry);
		response = new InvoiceResponse();
	}

}
