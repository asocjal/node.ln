package btcduke.node.ln;

import java.io.File;

import btcduke.node.api.ActionId;
import btcduke.node.api.Node;
import btcduke.node.api.NodeException;

public class NodeLnBtc implements Node<PrepareInvoiceResponseLnBtc, PrepareInvoiceRequestLnBtc, SendPaymentResponseLnBtc, SendPaymentRequestLnBtc> {

	private LnRpc lnRpc;
	private File rpcCertificate = new File("/home/cd/.lnd/tls.cert");
	private String rpcHost = "localhost";
	private int rpcPort = 10009;

	public NodeLnBtc() {
		
	}
	
	public void init() throws NodeException {
		try {
			lnRpc = new LnRpc();
			lnRpc.rpcConnect(rpcCertificate, rpcHost, rpcPort);
		} catch(LnRpcException ex) {
			throw new NodeException("Cannot initialize node", ex);
		}
	}
	
	@Override
	public PrepareInvoiceResponseLnBtc prepareInvoice(final ActionId id, final PrepareInvoiceRequestLnBtc request) throws NodeException {
		try {
			lnRpc.addInvoice(request.getValue());
			return null;
		// TODO Auto-generated method stub
		} catch(LnRpcException ex) {
			throw new NodeException("Cannot prepare invoice", ex);
		}
	}

	@Override
	public SendPaymentResponseLnBtc sendPayment(final ActionId id, final SendPaymentRequestLnBtc request) throws NodeException {
		// TODO Auto-generated method stub
		return null;
	}



}
