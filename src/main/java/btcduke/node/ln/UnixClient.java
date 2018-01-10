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
        String data = "{\"id\": 1, \"jsonrpc\":\"2.0\", \"method\": \"help\", \"params\": [\"hello\"]}";
        UnixSocketAddress address = new UnixSocketAddress(path);
        UnixSocketChannel channel = UnixSocketChannel.open(address);
        System.out.println("connected to " + channel.getRemoteSocketAddress());
        PrintWriter w = new PrintWriter(Channels.newOutputStream(channel));
        w.print(data);
        w.flush();

        InputStreamReader r = new InputStreamReader(Channels.newInputStream(channel));
        CharBuffer result = CharBuffer.allocate(1024);
        while(r.read(result) > 0) {
        	result.flip();
        	System.out.println("read from server: " + result.toString());
        }
//        final int status;
//        if (!result.toString().equals(data)) {
//            System.out.println("ERROR: data mismatch");
//            status = -1;
//        } else {
//            System.out.println("SUCCESS");
//            status = 0;
//        }
//        System.exit(status);
    }
}
