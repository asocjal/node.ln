package btcduke.node.api;

public class NodeException extends Exception {
	
	private static final long serialVersionUID = 7096503961250032683L;

	public NodeException(Throwable reason) {
		super(reason);
	}

	public NodeException(String message) {
		super(message);
	}
	
	public NodeException(String message, Throwable reason) {
		super(message, reason);
	}
}
