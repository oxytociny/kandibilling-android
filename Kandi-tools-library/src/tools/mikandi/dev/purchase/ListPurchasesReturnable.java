package tools.mikandi.dev.purchase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import tools.mikandi.dev.library.KandiLibs;
import tools.mikandi.dev.login.AAppReturnable;
import tools.mikandi.dev.utils.ParserUtils;
import tools.mikandi.dev.validation.ValidateUserReturnable;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.saguarodigital.returnable.IParser;
import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.IReturnableCache;
import com.saguarodigital.returnable.annotation.Field;
import com.saguarodigital.returnable.annotation.Type;
import com.saguarodigital.returnable.defaultimpl.AutoParser;
import com.saguarodigital.returnable.defaultimpl.EmptyCache;

@Type(version = 1, type = Type.JSONDataType.OBJECT, base = "data")
public class ListPurchasesReturnable extends AAppReturnable {

	// /
	// / Fields
	// /

	@Field(type = Field.Type.LIST, json_name = "purchases")
	protected List<String> mTokens;

	// /
	// / Accessors
	// /

	public List<String> getTokens() {
		return this.mTokens;
	}

	// /
	// / IReturnable Methods
	// /

	@Override
	public IParser<? extends IReturnable> getParser() {
		return new ListPurchasesReturnableParser();
	}

	@Override
	public String getUri(Map<String, String> args) {
		final StringBuilder sb = new StringBuilder(
				"https://billing.mikandi.com/v1/in_app/list_purchases");
		ensureElements(args, APP_ID, USER_ID, AUTH_HASH, AUTH_EXPIRES,APP_SECRET);
		sb.append('?').append(APP_ID).append('=').append(args.get(APP_ID));
		sb.append('&').append(USER_ID).append('=').append(args.get(USER_ID));
		sb.append('&').append(AUTH_HASH).append('=').append(args.get(AUTH_HASH));
		sb.append('&').append(AUTH_EXPIRES).append('=').append(args.get(AUTH_EXPIRES));
		sb.append('&').append(SIGNATURE).append('=');
		sb.append(this.computeSHA256(args.get(USER_ID), args.get(APP_ID),
				args.get(APP_SECRET)));
		if (KandiLibs.debug) Log.i("List Purchases url: " , sb.toString()); 
		return sb.toString();
	}

	@Override
	public IReturnableCache<? extends IReturnable> getCache(Context ctx) {
		return new EmptyCache<ListPurchasesReturnable>();
	}
	
	private class ListPurchasesReturnableParser implements IParser<ListPurchasesReturnable> {
		private long sTotalTime = 0;
		
		@Override
		public <T> boolean parse(JSONObject jo, T empty) {
			boolean ret = true; 
			long startTime = System.currentTimeMillis();
			final ParserUtils p = new ParserUtils(jo);
			ListPurchasesReturnable obj = (ListPurchasesReturnable) empty;
	
			try {
			
				if (jo.toString().equals("{\"purchases\":[]}")) {  
					Log.i("listPurchases " , " Purchases not returned");
				}
				else { 
					obj.mTokens = p.loadStringList("purchases",(String[]) null);
				}
			}
			catch (Exception E) { 
				ret = false; 
				E.printStackTrace();
				Log.e("AuthorizePurchaseParser" , "Error: " +  E);
			}
			long interval = (System.currentTimeMillis() - startTime); 
			 sTotalTime += interval; 
			return ret;
		}
	}

	public List<String> getArrayListTokens() {
		List<String> myString = new LinkedList<String>();
		if (mTokens != null) {
		for (String s: mTokens) { 
			myString.add(s);
			}
		}
		return myString;
	}
	
	
	
}