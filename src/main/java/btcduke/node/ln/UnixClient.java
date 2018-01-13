package btcduke.node.ln;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import btcduke.node.ln.rpc.commands.CommandDecodePay;
import btcduke.node.rpc.DecodePayResponse;
import btcduke.node.rpc.NodeRpcException;
import btcduke.node.rpc.NodeRpcRequest;
import btcduke.node.rpc.UnixSocket;
import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;

public class UnixClient {
	
	private static String readJson(String request, UnixSocketChannel channel) throws IOException {
//        String data = "{\"id\": 1, \"jsonrpc\":\"2.0\", \"method\": \"help\", \"params\": [\"hello\"]}";
        System.out.println("connected to " + channel.getRemoteSocketAddress());
        PrintWriter w = new PrintWriter(Channels.newOutputStream(channel));
        w.print(request);
        w.flush();

        InputStreamReader r = new InputStreamReader(Channels.newInputStream(channel));
        CharBuffer result = CharBuffer.allocate(1024);
        StringBuilder builder = new StringBuilder();
        while(true) {
        	int bytesRead = r.read(result);
        	result.flip();
        	builder.append(result);
        	
        	if(bytesRead < 1024) {
        		break;
        	}
        }
        return builder.toString();
	}
	
	
    public static void main(String[] args) {
   
	    	CommandDecodePay cmdDecodePay = new CommandDecodePay("lntb19u1pd93kn4pp5zvlshe4zl73kzf8q0cc62m3ak3y9hw70ea8z0hyxqptpwjh5e9wsdp8xys9xcmpd3sjqsmgd9czq3njv9c8qatrvd5kumcrshr5j7ewzpkqk3yssdh9y528w8h2urcxsdhkg6puhkp0djh76jhs93akzg82v0qlzt6hg4x0w6hfmkpdgy58wr96zvw4d2w9wn8gycqzce23w");
	
	    	try(UnixSocket sock = new UnixSocket("/home/cd/.lightning/lightning-rpc")) {
	    		for(int i=0; i<100000; i++) {
	    		sock.execute(cmdDecodePay);
	    		if(i % 1000 == 0) {
	    			System.out.println("" + i + " : " + cmdDecodePay.getResponse().result.msatoshi);
	    		}
	    		}
	    	} catch(NodeRpcException ex) {
	    		ex.printStackTrace();
	    	}
    	
    	
//        java.io.File path = new java.io.File("/home/cd/.lightning/lightning-rpc");
//        int retries = 0;
//        while (!path.exists()) {
//            TimeUnit.MILLISECONDS.sleep(500L);
//            retries++;
//            if (retries > 10) {
//                throw new IOException(
//                    String.format(
//                        "File %s does not exist after retry",
//                        path.getAbsolutePath()
//                    )
//                );
//            }
//        }
//        
//        UnixSocketAddress address = new UnixSocketAddress(path);
//        UnixSocketChannel channel = UnixSocketChannel.open(address);
//
//        System.out.println("read from server: " + readJson("{\"id\": 1, \"jsonrpc\":\"2.0\", \"method\": \"help\", \"params\": [\"hello\"]}", channel));;
//
//        channel.close();
        
    }
}
