package btcduke.node.ln.rpc.commands;

import btcduke.node.rpc.Command;
import btcduke.node.rpc.NodeRpcRequest;

public class CommandDecodePay extends Command<DecodePayResponse> {
	

	public CommandDecodePay(String invoice) {
		request = new NodeRpcRequest("decodepay");
		request.params = new String[1];
		request.params[0] = invoice;
		response = new DecodePayResponse();
	}
	

	
	
	

}
