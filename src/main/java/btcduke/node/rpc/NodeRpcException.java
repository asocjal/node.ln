package btcduke.node.rpc;

public class NodeRpcException extends Exception {
	
	private static final long serialVersionUID = -7716886300450288130L;

	public NodeRpcException(Throwable reason) {
		super(reason);
	}

	public NodeRpcException(String message) {
		super(message);
	}
	
	public NodeRpcException(String message, Throwable reason) {
		super(message, reason);
	}
}
