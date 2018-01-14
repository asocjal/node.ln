package btcduke.node.ln.rpc.commands;

import btcduke.node.rpc.Command;
import btcduke.node.rpc.NodeRpcRequest;

public class DelInvoiceCommand extends Command<DelInvoiceResponse> {

	// { "command" : "invoice", "description" : "Create invoice for {msatoshi}
	// with {label} and {description} with optional {expiry} seconds (default 1
	// hour)" },

	public DelInvoiceCommand(String label) {
		request = new NodeRpcRequest("delinvoice");
		request.params = new String[1];
		request.params[0] = label;
		response = new DelInvoiceResponse();
	}

}
