package btcduke.node.rpc;

public class Command<T extends NodeRpcResponse> {
	
	protected NodeRpcRequest request;
	protected T response;
	
	public NodeRpcRequest getRequest() {
		return request;
	}
	
	public T getResponse() {
		return response;
	}
	
	
}
