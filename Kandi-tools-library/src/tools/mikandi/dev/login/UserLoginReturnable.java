package tools.mikandi.dev.login;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import tools.mikandi.dev.library.KandiLibs;
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
		final boolean first = this.mFirstName == null ? false : this.mFirstName
				.length() > 1;
		final boolean last = this.mLastName == null ? false : this.mLastName
				.length() > 1;
		if (!first && !last) {
			return null;
		}
		if (!last) {
			return this.mFirstName;
		}
		if (!first) {
			return this.mLastName;
		}
		return this.mFirstName + " " + this.mLastName;
	}

	// /
	// / IReturnable Methods
	// /

	@Override
	public IParser<? extends IReturnable> getParser() {
		return new AutoParser<UserLoginReturnable>();
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

}