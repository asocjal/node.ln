package btcduke.node.ln.rpc.commands;

import btcduke.node.rpc.Command;
import btcduke.node.rpc.NodeRpcRequest;

public class PayCommand extends Command<PayResponse> {

	public PayCommand(String bolt11) {
		request = new NodeRpcRequest("pay");
		request.params = new String[1];
		request.params[0] = bolt11;
		response = new PayResponse();
	}

}
