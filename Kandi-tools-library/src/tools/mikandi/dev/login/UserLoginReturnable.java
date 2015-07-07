package tools.mikandi.dev.login;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import tools.mikandi.dev.library.KandiLibs;
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

@Type(version = 1, type = Type.JSONDataType.OBJECT, base = "login_info")
public class UserLoginReturnable extends AAppReturnable {

	// /
	// / Fields
	// /
	@Field(type = Field.Type.TEXT, constraint = Field.Constraint.NOT_NULL, json_name = "user_auth_hash")
	protected String mAuthHash;

	@Field(type = Field.Type.NUMBER, constraint = Field.Constraint.PRIMARY_KEY, json_name = "user_id")
	protected int mUserId;

	@Field(type = Field.Type.NUMBER, constraint = Field.Constraint.NOT_NULL, json_name = "user_auth_expire_time")
	protected long mExpires;

	@Field(type = Field.Type.LIST, json_name = "apps")
	protected List<String> mTokens;

	@Field(type = Field.Type.LIST, json_name = "purchases")
	protected List<String> mPurchases;
	
	@Field(type = Field.Type.TEXT, json_name = "first_name", constraint = Field.Constraint.NONE)
	protected String mFirstName;

	@Field(type = Field.Type.TEXT, json_name = "last_name", constraint = Field.Constraint.NONE)
	protected String mLastName;

	protected String mUsername; 
	
	
	protected int mUserBalance;
	
	protected String mDisplayName;

	public String toString() { 
		StringBuilder sb = new StringBuilder(); 
		sb.append("username: " + getUserId() + " ");
		sb.append("tokens " + getTokens() + " ");
		sb.append("auth " + getAuthHash() + " ");
		sb.append("authExpires " + getAuthExpires() + " ");
		sb.append("displayname " + getDisplayName() + " ");
		return	sb.toString();
	}
	
	// /
	// / Accessors
	// /
	public boolean isValid() {
		return System.currentTimeMillis() < this.mExpires;
	}

	public String getAuthHash() {
		return this.mAuthHash;
	}

	public String getUsername() { 
		return this.mUsername;
	}
		
	public int getUserId() {
		return this.mUserId;
	}

	public List<String> getTokens() {
		return this.mTokens;
	}

	public String getAuthExpires() {
		return String.valueOf(this.mExpires);
	}

	public String getDisplayName() {
		if (mDisplayName != null) { 
			return mDisplayName;
		} 
		
		final boolean first = this.mFirstName == null ? false : this.mFirstName
				.length() > 1;
		final boolean last = this.mLastName == null ? false : this.mLastName.length() > 1;
		
		if (first && last) { 
			mDisplayName = this.mFirstName + " " + this.mLastName;
		}
		
		if (!first && !last) {
			mDisplayName = null;
		}
		if (!last) {
			mDisplayName = this.mFirstName;
		}
		if (!first) {
			mDisplayName = this.mLastName;
		}
		
		return mDisplayName;
	}

	// /
	// / IReturnable Methods
	// /

	@Override
	public IParser<? extends IReturnable> getParser() {
		return new UserLoginParser();
	}

	@Override
	public String getUri(Map<String, String> args) {
		final StringBuilder sb = new StringBuilder(
				"https://billing.mikandi.com/v1/in_app/login");
		ensureElements(args, USERNAME, PASSWORD, APP_ID);
		sb.append('?').append(USERNAME).append('=')
				.append(URLEncoder.encode(args.get(USERNAME).trim()));
		sb.append("&").append(APP_ID).append('=').append(args.get(APP_ID));
		sb.append('&').append(PASSWORD_MD5).append('=')
				.append(this.computeMD5(args.get(PASSWORD)));
		
		if (KandiLibs.debug) Log.i("Login Activity url : " , sb.toString());
		return sb.toString();
	}

	@Override
	public IReturnableCache<? extends IReturnable> getCache(Context ctx) {
		return new EmptyCache<UserLoginReturnable>();
	}

	
	
	class UserLoginParser implements IParser<UserLoginReturnable> {
		
		private boolean PARSER = false;

		public <T> boolean parse(JSONObject jo, T empty) {
			boolean ret = true;
			long startTime = System.currentTimeMillis();
			final ParserUtils p = new ParserUtils(jo);
			long sTotalTime = 0;
			UserLoginReturnable obj = (UserLoginReturnable) empty;
			try {
				
				obj.mUsername = p.loadString("username", null);
				obj.mUserBalance = p.loadInteger("point_balance", null);
				obj.mUserId = p.loadInteger("user_id", null);
				obj.mAuthHash = p.loadString("user_auth_hash", null); 
				obj.mFirstName = p.loadString("first_name", null); 
				obj.mLastName = p.loadString("last_name", null); 
				
				if (jo.has("purchases")){
					obj.mPurchases = p.loadStringList("purchases", (String[]) null);
				}
				
			} catch (Exception e) {
				ret = false;
			}
			long interval = (System.currentTimeMillis() - startTime);
			return ret;
		}
	}
	
	
}