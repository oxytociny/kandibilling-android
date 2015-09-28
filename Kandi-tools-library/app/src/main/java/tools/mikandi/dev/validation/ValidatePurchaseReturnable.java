package tools.mikandi.dev.validation;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import tools.mikandi.dev.library.KandiLibs;
import tools.mikandi.dev.login.AAppReturnable;
import tools.mikandi.dev.purchase.ListPurchasesReturnable;
import tools.mikandi.dev.utils.ParserUtils;
import android.content.Context;
import android.util.Log;

import com.saguarodigital.returnable.IParser;
import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.IReturnableCache;
import com.saguarodigital.returnable.annotation.Field;
import com.saguarodigital.returnable.annotation.Type;
import com.saguarodigital.returnable.defaultimpl.AutoParser;
import com.saguarodigital.returnable.defaultimpl.EmptyCache;

@Type(version = 1,  base = "data", type = Type.JSONDataType.OBJECT)
public class ValidatePurchaseReturnable extends AAppReturnable {

	/*
	 * one shot api, you wont be able to call isPurchased unless a sucessful result is
	 * received from the server, this would cause an aldebran error
	 */
	@Field(type = Field.Type.LIST   , json_name = "purchases")
	public List<String> purchases; 
		
	public boolean isPurchased() {
		return true;
	}

	public List<String> getPurchases() { 
		return purchases;
	}
	
	// / IReturnable Methods
	
	@Override
	public IParser<? extends IReturnable> getParser() {
		return new ValidatePurchaseReturnableReturnableParser();
	}

	@Override
	public String getUri(Map<String, String> args) {
		
		String eToken = null;
		
		try {
		eToken = URLEncoder.encode(args.get(TOKEN), "UTF-8");
		} catch (Exception E) { 
			E.printStackTrace();
		}
		
		final StringBuilder sb = new StringBuilder("https://billing.mikandi.com/v1/in_app/validate_purchase");
		ensureElements(args, APP_ID, USER_ID, AUTH_HASH, AUTH_EXPIRES,	APP_SECRET, TOKEN);
		sb.append('?').append(APP_ID).append('=').append(args.get(APP_ID).toString());
		sb.append('&').append(USER_ID).append('=').append(args.get(USER_ID));
		sb.append('&').append(AUTH_HASH).append('=').append(args.get(AUTH_HASH));
		sb.append('&').append(TOKEN).append('=').append(eToken);
		sb.append('&').append(SIGNATURE).append('=');
		sb.append(this.computeSHA256(args.get(USER_ID), args.get(APP_ID), args.get(TOKEN), args.get(APP_SECRET)));
		if (KandiLibs.debug) Log.i("Validate Purchase Returnable " , sb.toString());
		return sb.toString();
	}

	@Override
	public IReturnableCache<? extends IReturnable> getCache(Context ctx) {
		return new EmptyCache<ValidatePurchaseReturnable>(); 
	}
	
	
	// Need to print out the parser response 
	private class ValidatePurchaseReturnableReturnableParser implements IParser<ValidatePurchaseReturnable> {
		private long sTotalTime = 0;
		private String sPurchases  = "purchases";
		
		@Override
		public <T> boolean parse(JSONObject jo, T empty) {
						
			boolean ret = true; 
			long startTime = System.currentTimeMillis();
			final ParserUtils p = new ParserUtils(jo);
			ValidatePurchaseReturnable obj = (ValidatePurchaseReturnable) empty;
			try {
				obj.purchases = p.loadStringList(sPurchases ,(String[]) null);
			}
			catch (Exception E) { 
				ret = false; 
				E.printStackTrace();
				Log.e("AuthorizePurchaseParser" , "Error: " +  E);
			}
			return ret;
		}
	}
}