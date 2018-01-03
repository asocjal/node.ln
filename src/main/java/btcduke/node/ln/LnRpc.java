package btcduke.node.ln;

import java.io.File;
import java.util.Iterator;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import lnrpc.LightningGrpc;
import lnrpc.Rpc.AddInvoiceResponse;
import lnrpc.Rpc.ChannelBalanceRequest;
import lnrpc.Rpc.ChannelBalanceResponse;
import lnrpc.Rpc.ChannelPoint;
import lnrpc.Rpc.CloseChannelRequest;
import lnrpc.Rpc.CloseStatusUpdate;
import lnrpc.Rpc.ConnectPeerRequest;
import lnrpc.Rpc.ConnectPeerResponse;
import lnrpc.Rpc.DisconnectPeerRequest;
import lnrpc.Rpc.DisconnectPeerResponse;
import lnrpc.Rpc.GetInfoRequest;
import lnrpc.Rpc.GetInfoResponse;
import lnrpc.Rpc.Invoice;
import lnrpc.Rpc.LightningAddress;
import lnrpc.Rpc.ListPeersRequest;
import lnrpc.Rpc.ListPeersResponse;
import lnrpc.Rpc.OpenChannelRequest;
import lnrpc.Rpc.PendingChannelRequest;
import lnrpc.Rpc.PendingChannelResponse;
import lnrpc.Rpc.WalletBalanceRequest;
import lnrpc.Rpc.WalletBalanceResponse;

public class LnRpc {

	private ManagedChannel channel;
	private LightningGrpc.LightningBlockingStub blockingStub;
	private boolean connected = false;
	private boolean connectedCheckEnabled = true;

	private void checkForConnected(final String methodName) throws LnRpcException {
		if (connectedCheckEnabled == false) {
			return;
		}
		if (connected == false) {
			throw new LnRpcException("Cannot call '" + methodName + "'. You need to call 'connect' method first");
		}
		if (channel == null) {
			throw new LnRpcException("Cannot call '" + methodName + "'. Internel eception. Channel is null");
		}

		if (blockingStub == null) {
			throw new LnRpcException("Cannot call '" + methodName + "'. Internel eception. Blocking stub is null");

		}

	}

	public void rpcConnect(final File certificate, final String host, final int port) throws LnRpcException {
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

	public ConnectPeerResponse connectPeer(final String pubKey, final String host) throws LnRpcException {
		checkForConnected("connectPeer");
		try {
			LightningAddress addr = LightningAddress.newBuilder().setPubkey(pubKey).setHost(host).build();
			ConnectPeerRequest request = ConnectPeerRequest.newBuilder().setAddr(addr).build();
			ConnectPeerResponse response;
			response = blockingStub.connectPeer(request);
			return response;
		} catch (StatusRuntimeException e) {
			throw new LnRpcException("connectPeer failed", e);
		}
	}

	public DisconnectPeerResponse disconnectPeer(final String pubKey) throws LnRpcException {
		checkForConnected("disconnectPeer");
		try {
			DisconnectPeerRequest request = DisconnectPeerRequest.newBuilder().setPubKey(pubKey).build();
			DisconnectPeerResponse response;
			response = blockingStub.disconnectPeer(request);
			return response;
		} catch (StatusRuntimeException e) {
			throw new LnRpcException("disconnectPeer failed", e);
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

	public ChannelPoint openChannel(final String nodePubKey, long localAmt, long pushAmt)
			throws LnRpcException {
		checkForConnected("openChannel");
		try {
			OpenChannelRequest request = OpenChannelRequest.newBuilder().setNodePubkeyString(nodePubKey)
					.setLocalFundingAmount(localAmt).setPushSat(pushAmt).build();
			ChannelPoint response;
			response = blockingStub.openChannelSync(request);
			return response;
		} catch (StatusRuntimeException e) {
			throw new LnRpcException("openChannel failed", e);
		}
	}
	
	/**
	 * TODO: Nie działa poprawnie
	 * 
	 * @param fundingTxId
	 * @return
	 * @throws LnRpcException
	 */
	public Iterator<CloseStatusUpdate> closeChannel(final String fundingTxId)
			throws LnRpcException {
		checkForConnected("CloseChannel");
		try {
			CloseChannelRequest request = CloseChannelRequest.newBuilder().setChannelPoint(ChannelPoint.newBuilder().setFundingTxidStr(fundingTxId)).build();
			Iterator<CloseStatusUpdate> response;
			response = blockingStub.closeChannel(request);
			return response;
		} catch (StatusRuntimeException e) {
			throw new LnRpcException("CloseChannel failed", e);
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
	
	public WalletBalanceResponse walletBalance() throws LnRpcException {
		checkForConnected("WalletBalance");
		try {
			WalletBalanceRequest request = WalletBalanceRequest.newBuilder().getDefaultInstanceForType();
			WalletBalanceResponse response;
			response = blockingStub.walletBalance(request);
			return response;
		} catch (StatusRuntimeException e) {
			throw new LnRpcException("WalletBalance failed", e);
		}
	}
	
	public AddInvoiceResponse addInvoice(final long value) throws LnRpcException {
		checkForConnected("AddInvoice");
		try {
			Invoice request = Invoice.newBuilder().setValue(value).build();
			AddInvoiceResponse response;
			response = blockingStub.addInvoice(request);
			return response;
		} catch (StatusRuntimeException e) {
			throw new LnRpcException("AddInvoice failed", e);
		}
	}
	
//	public PayInvoiceResponse PayInvoice(final long value) throws LnRpcException {
//		checkForConnected("PayInvoice");
//		try {
//			Invoice request = Invoice.newBuilder().setValue(value).build();
//			PayInvoiceResponse response;
//			response = blockingStub.PayInvoice(request);
//			return response;
//		} catch (StatusRuntimeException e) {
//			throw new LnRpcException("PayInvoice failed", e);
//		}
//	}	
	
	public ChannelBalanceResponse channelBalance() throws LnRpcException {
		checkForConnected("ChannelBalance");
		try {
			ChannelBalanceRequest request = ChannelBalanceRequest.newBuilder().getDefaultInstanceForType();
			ChannelBalanceResponse response;
			response = blockingStub.channelBalance(request);
			return response;	
		} catch (StatusRuntimeException e) {
			throw new LnRpcException("ChannelBalance failed", e);
		}
	}
	
	public PendingChannelResponse pendingChannels() throws LnRpcException {
		checkForConnected("PendingChannels");
		try {
			PendingChannelRequest request = PendingChannelRequest.newBuilder().getDefaultInstanceForType();
			PendingChannelResponse response;
			response = blockingStub.pendingChannels(request);
			return response;
		} catch (StatusRuntimeException e) {
			throw new LnRpcException("PendingChannels failed", e);
		}
	}

	public static void main(String[] args) throws Exception {
		LnRpc lnRpc1 = new LnRpc();
		lnRpc1.rpcConnect(new File("/home/cd/.lnd/tls.cert"), "localhost", 10009);

//		LnRpc lnRpc2 = new LnRpc();

//		lnRpc2.rpcConnect(new File("/home/cd/.lnd/tls.cert"), "localhost", 10010);

		System.out.println("LND1 Info: \n" + lnRpc1.getInfo());
//		System.out.println("LND2 Info: \n" + lnRpc2.getInfo());

//		System.out.println("List channels: \n" + lnRpc1.listChannels());

		System.out.println("Channel balance: \n" + lnRpc1.channelBalance());
		
		System.out.println("Add invoice: \n" + lnRpc1.addInvoice(1000));
		
//		System.out.println("Pending channels: \n" + lnRpc1.pendingChannels());
//		
//		lnRpc1.closeChannel("6fac4a6991f8e219673fb4413af80577e60d3a07365dc503f2413804ccfc5dda");

//		System.out.println("Connect peer resp:\n" +
//		lnRpc1.connectPeer("0338703b798e569fc82bd8d95abaea142af1704f76dd39124a3d99206a0ec24001",
//				"5.51.143.110:9735"));
//		
//		ChannelPoint resp = lnRpc1.openChannel("0338703b798e569fc82bd8d95abaea142af1704f76dd39124a3d99206a0ec24001", 10000, 3000);
//
//		System.out.println(resp);
//		
//		System.out.println("LND1 Info: \n" + lnRpc1.getInfo());
		
//		int i = 0;
//		while(resp.hasNext()) {
//			System.out.println("" + i + " : " + resp.next());
//			i++;
//		}
		
//		System.out.println("Disconnect peer resp:\n"
//				+ lnRpc1.disconnectPeer("0338703b798e569fc82bd8d95abaea142af1704f76dd39124a3d99206a0ec24001"));

//		System.out.println("List peers: \n" + lnRpc1.listPeers());
	}
}
