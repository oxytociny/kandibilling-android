package tools.mikandi.dev.purchase;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import tools.mikandi.dev.library.KandiLibs;
import tools.mikandi.dev.login.AAppReturnable;
import android.content.Context;
import android.util.Log;
import tools.mikandi.dev.utils.ParserUtils;

import com.saguarodigital.returnable.IParser;
import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.IReturnableCache;
import com.saguarodigital.returnable.annotation.Field;
import com.saguarodigital.returnable.annotation.Type;
import com.saguarodigital.returnable.defaultimpl.EmptyCache;

@Type(
	version = 1,
	type = Type.JSONDataType.OBJECT,
	base = "data"
)
public class AuthorizePurchaseReturnable extends AAppReturnable {
	///
	/// Fields
	///
	@Field(
		type = Field.Type.TEXT,
		constraint = Field.Constraint.NOT_NULL
	)
	private String mToken;
	
	@Field(
		type = Field.Type.LIST
	)
	private List<String> mTokens;

	///
	/// Accessors
	///
	public String getToken() {
		return this.mToken;
	}
	
	public List<String> getTokens() {
		return this.mTokens;
	}
	
	///
	/// IReturnable Methods
	///
	@Override
	public IParser<? extends IReturnable> getParser() {
		return new AuthorizePurchaseReturnableParser();
	}

	@Override
	public String getUri(Map<String, String> args) {
		String eDescription = null; 
		String eToken = null;
		try { 
		eDescription = URLEncoder.encode(args.get(DESCRIPTION), "UTF-8");
		eToken = URLEncoder.encode(args.get(TOKEN), "UTF-8");
		}
		catch (Exception E) {
			Log.e("Error thrown in AuthPurchRet","" + E);
		}
		final StringBuilder sb = new StringBuilder("https://billing.mikandi.com/v1/in_app/authorize_purchase");
		ensureElements(args, APP_ID, USER_ID, AUTH_HASH, AUTH_EXPIRES, APP_SECRET, TOKEN, AMOUNT, DESCRIPTION);
		sb.append('?').append(APP_ID).append('=').append(args.get(APP_ID));
		sb.append('&').append(USER_ID).append('=').append(args.get(USER_ID));
		sb.append('&').append(AUTH_HASH).append('=').append(args.get(AUTH_HASH));
		sb.append('&').append(AUTH_EXPIRES).append('=').append(args.get(AUTH_EXPIRES));
		sb.append('&').append(TOKEN).append('=').append(eToken);
		sb.append('&').append(AMOUNT).append('=').append(args.get(AMOUNT));
		sb.append('&').append(DESCRIPTION).append('=').append(eDescription);
		sb.append('&').append(SIGNATURE).append('=');
		sb.append(this.computeSHA256(args.get(USER_ID), args.get(APP_ID), args.get(TOKEN), args.get(AMOUNT), args.get(DESCRIPTION), args.get(APP_SECRET)));
		if (KandiLibs.debug) Log.i("App Authorize ", sb.toString());
		return sb.toString();
	}

	@Override
	public IReturnableCache<? extends IReturnable> getCache(Context ctx) {
		return new EmptyCache<AuthorizePurchaseReturnable>();
	}
	
	private class AuthorizePurchaseReturnableParser implements IParser<AuthorizePurchaseReturnable> {
		private long sTotalTime = 0;
		
		@Override
		public <T> boolean parse(JSONObject jo, T empty) {
			boolean ret = true; 
			long startTime = System.currentTimeMillis();
			final ParserUtils p = new ParserUtils(jo);
			AuthorizePurchaseReturnable obj = (AuthorizePurchaseReturnable) empty;
			try {
			if (jo.has("purchase_id")) {  
					Log.i("Auth Purchase Parser" , "has token");
					Log.i("Auth Purchase Parser" , "about to extract tokens");
					obj.mToken = p.loadString("purchase_id", AAppReturnable.EMPTYJSONSTRING);
					Log.i("printing mTokens" , mToken);
			}
			if (jo.has("purchases")) {
					Log.i("AuthPurchase Parser"  , "has tokens plural");
					Log.i("AuthPurchase Parser"  , "about to extract tokens");			
					obj.mTokens = getArrayList(jo.getJSONArray("purchases"));
					Log.i("printing mTokens" , mTokens.toString());
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
	
	public ArrayList<String> getArrayList(JSONArray jsonArray) {

		int len = jsonArray == null ? 0 : jsonArray.length();
		ArrayList<String> list = new ArrayList<String>(len);
		try {
			for (int i = 0; i < len; i++) {
				list.add(jsonArray.getString(i));
			}
		} catch (Exception E) {
			Log.e("Authorize Purchase returnable ", " Error retreiving array list ", E);
		}
		return list;
	}
	
}