package btcduke.node.rpc;

public class NodeRpcRequest {
	
	public final int id = (int)(Math.random()*Integer.MAX_VALUE);
	public String jsonrpc = "2.0";
	public String method;
	public String[] params = new String[]{};
	
	public NodeRpcRequest(final String method) {
		this.method = method;
	}
	

	

}
