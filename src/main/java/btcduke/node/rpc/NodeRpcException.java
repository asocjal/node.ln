package btcduke.node.rpc;

public class NodeRpcException extends Exception {
	
	private static final long serialVersionUID = -7716886300450288130L;
	
	private RpcServerException serverCause = null;

	public NodeRpcException(String message) {
		super(message);
	}
	
	public NodeRpcException(String message, Throwable cause) {
		super(message, cause);
		if(cause instanceof RpcServerException) {
			serverCause = (RpcServerException) cause;
		}
	}
	
	public RpcServerException getServerCause() {
		return serverCause;
	}
}
