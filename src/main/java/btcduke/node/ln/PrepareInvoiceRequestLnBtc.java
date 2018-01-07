package btcduke.node.ln;

import btcduke.node.api.PrepareInvoiceRequest;

public class PrepareInvoiceRequestLnBtc implements PrepareInvoiceRequest {
	private long value;

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
}
