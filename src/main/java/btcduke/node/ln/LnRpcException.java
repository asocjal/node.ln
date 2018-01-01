package btcduke.node.ln;

public class LnRpcException extends Exception {

	public LnRpcException(Throwable reason) {
		super(reason);
	}

	public LnRpcException(String message) {
		super(message);
	}
	
	public LnRpcException(String message, Throwable reason) {
		super(message, reason);
	}

}
