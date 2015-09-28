package tools.mikandi.dev.validation;


import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.saguarodigital.returnable.IParser;
import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.IReturnableCache;
import com.saguarodigital.returnable.annotation.Field;
import com.saguarodigital.returnable.annotation.Type;
import com.saguarodigital.returnable.defaultimpl.EmptyCache;

import tools.mikandi.dev.login.AAppReturnable;
import tools.mikandi.dev.utils.Logger;
import tools.mikandi.dev.utils.ParserUtils;

@Type(version = 1, type = Type.JSONDataType.OBJECT)
public class ValidateUserReturnable extends AAppReturnable {
	
	@Field( json_name = "purchased", type = Field.Type.BOOLEAN)
	private boolean mPurchased; 
	
	public boolean isValidated() { 
		return this.mPurchased; 
	}
	
	@Override
	public String getUri(Map<String, String> args) {
		final StringBuilder sb = new StringBuilder("https://billing.mikandi.com/v1/in_app/validate_app_purchase");
		ensureElements(args, APP_ID, USER_ID, APP_SECRET);
		sb.append('?').append(APP_ID).append('=').append(args.get(APP_ID));
		sb.append('&').append(USER_ID).append('=').append(args.get(USER_ID));
		sb.append('&').append(SIGNATURE).append('=');
		sb.append(this.computeSHA256(args.get(USER_ID), args.get(APP_ID), args.get(APP_SECRET)));
		if (Logger.isDebug) Log.i("Validate User Returnable" , sb.toString()); 
		return sb.toString();	
	}

	// 
	@Override
	public IReturnableCache<? extends IReturnable> getCache(Context ctx) {
		return new EmptyCache<ValidateUserReturnable>();
	}

	@Override
	public IParser<? extends IReturnable> getParser() {
		return new ValidateUserReturnableParser();
	}

	
	private class ValidateUserReturnableParser implements IParser<ValidateUserReturnable> {
		private long sTotalTime = 0;
		
		@Override
		public <T> boolean parse(JSONObject jo, T empty) {
			boolean ret = true; 
			final ParserUtils p = new ParserUtils(jo);
			ValidateUserReturnable obj = (ValidateUserReturnable) empty;
	
			try {
				if (jo.has("purchased")) {  
						obj.mPurchased = jo.getBoolean("purchased");
				}
				else { 
					Log.e("Validating User json", "purchased not retreived"); 
			}
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
