package btcduke.node.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;

public class UnixSocket implements AutoCloseable {

	private final UnixSocketAddress address;
	private final UnixSocketChannel channel;
	private final static int bufferSize = 10000;
	private final Gson gson;

	public UnixSocket(String socketFilePath) throws NodeRpcException {
		try {
			address = generateAddress(socketFilePath);
			channel = newChannel(address);
			gson = new Gson();
		} catch (Throwable ex) {
			throw new NodeRpcException("Cannot create unix socket", ex);
		}
	}

	public static UnixSocketAddress generateAddress(String socketFilePath) throws NodeRpcException {
		try {

			java.io.File path = new java.io.File(socketFilePath);
			int retries = 0;
			while (!path.exists()) {
				TimeUnit.MILLISECONDS.sleep(100L);
				retries++;
				if (retries > 10) {
					throw new IOException(String.format("File %s does not exist after retry", path.getAbsolutePath()));
				}
			}
			return new UnixSocketAddress(path);

		} catch (Throwable ex) {
			throw new NodeRpcException("Cannot connect to unix socket to file '" + socketFilePath + "'.", ex);
		}
	}

    public static <T> T load(final InputStreamReader inputStreamReader, final Class<T> clazz) throws NodeRpcException {
        try {
            final Gson gson = new Gson();
            while(inputStreamReader.read() != -1) {
            	
            }
            final BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            Iterator<String> it = reader.lines().iterator();
            while(it.hasNext()) {
            	stringBuilder.append(it.next());
            }
//            while((lineTmp = reader.readLine()) != null) {
//            	stringBuilder.append(lineTmp);
//            }
            return gson.fromJson(stringBuilder.toString(), clazz);
        } catch (final Throwable ex) {
        	throw new NodeRpcException("Cannot read reply from RPC server", ex);
        }
    }
	private static UnixSocketChannel newChannel(UnixSocketAddress address) throws NodeRpcException {
		try {
			return UnixSocketChannel.open(address);
		} catch (Throwable ex) {
			throw new NodeRpcException("Cannot open new channel for address '" + address + "'.", ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" }) // TODO: Is there better way?
	public void execute(Command command) throws NodeRpcException {
		String requestStr = null;
		try {
			requestStr = gson.toJson(command.getRequest());
			System.out.println("REQUEST: " + requestStr);

			PrintWriter w = new PrintWriter(Channels.newOutputStream(channel));
			w.print(requestStr);
			w.flush();
			
			InputStreamReader r = new InputStreamReader(Channels.newInputStream(channel));
			
			CharBuffer respChBuf = CharBuffer.allocate(bufferSize);
			int bytesRead = r.read(respChBuf);
			if(bytesRead == bufferSize) {
				throw new NodeRpcException("Response from server out of buffer");
			}
			respChBuf.flip();
			String respStr = respChBuf.toString();
			System.out.println("RESPONSE: " + respStr);
			command.response = gson.fromJson(respStr, command.getResponse().getClass());
			
			if(command.getResponse().error != null) {
				throw new RpcServerException(command.getResponse().error, requestStr, respStr, command);
			}

		} catch(Throwable ex) {
			throw new NodeRpcException("Cannot execute request '" + requestStr + "'", ex);
		}
	}

	@Override
	public void close() throws NodeRpcException {
		try {
			System.out.print("Closing");
			if (channel != null) {
				channel.close();
			}
		} catch(Throwable ex) {
			throw new NodeRpcException("Cannot close RPC connection", ex);
		}

	}

}
