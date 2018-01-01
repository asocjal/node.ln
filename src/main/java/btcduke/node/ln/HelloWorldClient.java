package btcduke.node.ln;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import lnrpc.LightningGrpc;
import lnrpc.Rpc.GetInfoRequest;
import lnrpc.Rpc.GetInfoResponse;

/**
 * A simple client that requests a greeting from the {@link HelloWorldServer}.
 */
public class HelloWorldClient {
  private static final Logger logger = Logger.getLogger(HelloWorldClient.class.getName());

  public static void getInfo(LightningGrpc.LightningBlockingStub blockingStub) {
    System.out.println("Will try to get info... ");
    GetInfoRequest request = GetInfoRequest.newBuilder().getDefaultInstanceForType();
    GetInfoResponse response;
    try {
      response = blockingStub.getInfo(request);
    } catch (StatusRuntimeException e) {
      e.printStackTrace();
      return;
    }
    System.out.println("Response: " + response);
  }
  
  private static LightningGrpc.LightningBlockingStub buildConnection() throws IOException {
	  SslContextBuilder clientContextBuilder = GrpcSslContexts.configure(SslContextBuilder.forClient(), SslProvider.OPENSSL);
  
	  File clientCertChainFile = new File("/home/cd/.lnd/tls.cert");
	  
	  SslContext sslContext = clientContextBuilder
			  .trustManager(clientCertChainFile)
			  .build();
	  
	  ManagedChannel channel = clientChannel(10009, sslContext);
	  LightningGrpc.LightningBlockingStub blockingStub = LightningGrpc.newBlockingStub(channel);
	  return blockingStub;
  }
  
  private static ManagedChannel clientChannel(int port, SslContext sslContext) throws IOException {
	    return NettyChannelBuilder.forAddress("localhost", port)
	        .negotiationType(NegotiationType.TLS)
	        .sslContext(sslContext)
	        .build();
  }

  public static void main(String[] args) throws Exception {
	  LightningGrpc.LightningBlockingStub blockingStub = buildConnection();
	  getInfo(blockingStub);
  }
  
}