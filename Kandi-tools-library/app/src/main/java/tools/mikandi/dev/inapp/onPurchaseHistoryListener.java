package tools.mikandi.dev.inapp;

import java.util.List;

public interface onPurchaseHistoryListener {

	public void onSucessfulHistoryRetrieved(List<String> mTokens); 
	
	public void onFailedHistoryRetrieved();
	
	public void onNoPurchases();
}
