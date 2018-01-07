package btcduke.node.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActionId {
	String sender;
	String customer;
	Date creationDate;
	long index;
	String additionalInfo;
	List<ActionId> relatedActions;
	
	
	public ActionId(final String sender, final String customer, long index, String additionalInfo, ActionId ... relatedActions) {
		this.sender = sender;
		this.customer = customer;
		this.creationDate = new Date();
		this.index = index;
		this.additionalInfo = additionalInfo;
		this.relatedActions = new ArrayList<ActionId>();
		for(ActionId related : relatedActions) {
			this.relatedActions.add(related);
		}
	}
	

}
