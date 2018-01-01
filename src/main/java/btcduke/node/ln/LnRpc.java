package btcduke.node.ln;

import java.io.File;

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
import lnrpc.Rpc.ListPeersRequest;
import lnrpc.Rpc.ListPeersResponse;

public class LnRpc {

	private ManagedChannel channel;
	private LightningGrpc.LightningBlockingStub blockingStub;
	private boolean connected = false;
	private boolean connectedCheckEnabled = true;

	private void checkForConnected(String methodName) throws LnRpcException {
		if(connectedCheckEnabled == false) {
			return;
		}
		if(connected == false) {
			throw new LnRpcException("Cannot call '" + methodName + "'. You need to call 'connect' method first");
		}
		if(channel == null) {
			throw new LnRpcException("Cannot call '" + methodName + "'. Internel eception. Channel is null");
		}
		
		if(blockingStub == null)  {
			throw new LnRpcException("Cannot call '" + methodName + "'. Internel eception. Blocking stub is null");
			
		}
	
	}
	
	public void connect(File certificate, String host, int port) throws LnRpcException {
		try {
			connectedCheckEnabled = false;
			SslContextBuilder clientContextBuilder = GrpcSslContexts.configure(SslContextBuilder.forClient(),
					SslProvider.OPENSSL);

			File clientCertChainFile = certificate;

			SslContext sslContext = clientContextBuilder.trustManager(clientCertChainFile).build();

			channel = NettyChannelBuilder.forAddress(host, port).negotiationType(NegotiationType.TLS)
					.sslContext(sslContext).build();

			blockingStub = LightningGrpc.newBlockingStub(channel);
			
			GetInfoResponse response = getInfo();
			System.out.println("Connected to LND. Node ID: " + response.getIdentityPubkey());

			connected = true;
			connectedCheckEnabled = true;
		} catch (Throwable throwable) {
			throw new LnRpcException("Cannot connect to LND", throwable);
		}
	}

	public GetInfoResponse getInfo() throws LnRpcException {
		checkForConnected("getInfo");
		try {
			GetInfoRequest request = GetInfoRequest.newBuilder().getDefaultInstanceForType();
			GetInfoResponse response;
			response = blockingStub.getInfo(request);
			return response;
		} catch (StatusRuntimeException e) {
			throw new LnRpcException("getInfo failed", e);
		}
	}
	
	public ListPeersResponse listPeers() throws LnRpcException {
		checkForConnected("listPeers");
		try {
			ListPeersRequest request = ListPeersRequest.newBuilder().getDefaultInstanceForType();
			ListPeersResponse response;
			response = blockingStub.listPeers(request);
			return response;
		} catch (StatusRuntimeException e) {
			throw new LnRpcException("ListPeers failed", e);
		}
	}

	public static void main(String[] args) throws Exception {
		LnRpc lnRpc = new LnRpc();
		lnRpc.connect(new File("/home/cd/.lnd/tls.cert"), "localhost", 10009);
		ListPeersResponse resp = lnRpc.listPeers();
		System.out.println(resp);
	}
}
