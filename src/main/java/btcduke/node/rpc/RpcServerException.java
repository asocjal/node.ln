package btcduke.node.rpc;

public class RpcServerException extends Exception {

	private static final long serialVersionUID = -7977999805733604059L;
	
	private String requestJson;
	private String responseJson;
	private Command<?> command;

	public RpcServerException(String message, String requestJson, String responseJson, Command<?> command) {
		super(message);
		this.requestJson = requestJson;
		this.responseJson = responseJson;
		this.command = command;
		// TODO Auto-generated constructor stub
	}
	
	public String getRequestJson() {
		return requestJson;
	}
	
	public String getResponseJson() {
		return responseJson;
	}
	
	public Command<?> getCommand() {
		return command;
	}

}
