package btcduke.node.rpc;

public class Command<T extends NodeRpcResponse> {
	
	public NodeRpcRequest request;
	public T response;
	
}
