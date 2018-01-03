package btcduke.node.ln;

public class LnRpcException extends Exception {

	private static final long serialVersionUID = -1526293152135906239L;

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
