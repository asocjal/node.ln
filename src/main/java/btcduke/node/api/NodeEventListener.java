package btcduke.node.api;

import java.math.BigDecimal;

public class NodeEventListener {
	void receivedFunds(ActionId actionId, BigDecimal amount, int type, String description) {
		
	}
	
	void sentPaymentStatus(ActionId actionId, BigDecimal amount, int status, String description) {
		
	}
}
