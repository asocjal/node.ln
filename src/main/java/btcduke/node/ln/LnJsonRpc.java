package btcduke.node.ln;

import java.io.File; 
import java.io.IOException; 
import java.net.ServerSocket; 
import java.net.URL; 
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import java.util.UUID; 
 
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
 
//import com.googlecode.jsonrpc4j.Base64; 
import com.googlecode.jsonrpc4j.JsonRpcHttpClient; 
import com.googlecode.jsonrpc4j.ProxyUtil; 



import com.googlecode.jsonrpc4j.JsonRpcHttpClient;


public class LnJsonRpc {

	public static void main(String[] args) throws Throwable {
		
//		String user = "cd";
//		String pw = "cd";
		
		JsonRpcHttpClient client = new JsonRpcHttpClient(
			    new URL("file:/home/cd/.lightning/lightning-rpc"));
//		
//		  String cred = Base64.getEncoder().encodeToString((user + ":" + pw).getBytes()); 
//		  Map<String, String> headers = new HashMap<>(1); 
//		  headers.put("Authorization", "Basic " + cred); 
//		  JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http://localhost:8332/"), headers); 
//		  
		  
		  
//		JsonRpcHttpClient client = new JsonRpcHttpClient(
//			    new URL("http://localhost:9735/lightning-rpc"));


		//	String user = client.invoke("createUser", new Object[] { "bob", "the builder" }, String.class);
			String user = client.invoke("help", new Object[] {}, String.class);

	}
	
	

}
