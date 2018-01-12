package btcduke.node.ln;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.util.concurrent.TimeUnit;

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
	
	
    public static void main(String[] args) throws IOException, InterruptedException {
        java.io.File path = new java.io.File("/home/cd/.lightning/lightning-rpc");
        int retries = 0;
        while (!path.exists()) {
            TimeUnit.MILLISECONDS.sleep(500L);
            retries++;
            if (retries > 10) {
                throw new IOException(
                    String.format(
                        "File %s does not exist after retry",
                        path.getAbsolutePath()
                    )
                );
            }
        }
        
        UnixSocketAddress address = new UnixSocketAddress(path);
        UnixSocketChannel channel = UnixSocketChannel.open(address);

        System.out.println("read from server: " + readJson("{\"id\": 1, \"jsonrpc\":\"2.0\", \"method\": \"help\", \"params\": [\"hello\"]}", channel));;

        channel.close();
        
    }
}
