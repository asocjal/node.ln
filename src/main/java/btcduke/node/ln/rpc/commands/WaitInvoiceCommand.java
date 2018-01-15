package btcduke.node.ln.rpc.commands;

import btcduke.node.rpc.Command;
import btcduke.node.rpc.NodeRpcRequest;

public class WaitInvoiceCommand extends Command<InvoiceResponse> {

	public WaitInvoiceCommand(String label) {
		request = new NodeRpcRequest("waitinvoice");
		request.params = new String[1];
		request.params[0] = label;
		response = new InvoiceResponse();
	}

}
