package btcduke.node.api;

public interface Node<IRESP extends PrepareInvoiceResponse, IREQ extends PrepareInvoiceRequest, PRESP extends SendPaymentResponse, PREQ extends SendPaymentRequest> {
	
	IRESP prepareInvoice(ActionId id, IREQ request) throws NodeException;
	PRESP sendPayment(ActionId id, PREQ request) throws NodeException;

}
