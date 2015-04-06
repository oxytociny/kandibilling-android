package tools.mikandi.dev.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import tools.mikandi.dev.utils.*;
import com.saguarodigital.returnable.IParser;
import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.IReturnableCache;
import com.saguarodigital.returnable.annotation.Field;
import com.saguarodigital.returnable.annotation.Type;
import com.saguarodigital.returnable.defaultimpl.AutoParser;
import com.saguarodigital.returnable.defaultimpl.EmptyCache;

/**
 * Represents the "session" result of a call to the login API.
 * @author Christopher O'Connell
 */
@Type(
	version = 1,
	base = "user_info"
)
public class LoginSession implements IReturnable {
	
	@Field(type = Field.Type.TEXT, constraint = Field.Constraint.NOT_NULL, json_name = "user_auth_hash")
	protected String mSessionId;

	@Field(type = Field.Type.NUMBER, constraint = Field.Constraint.PRIMARY_KEY, json_name = "user_id")
	protected int mUserId;

	@Field(type = Field.Type.NUMBER, constraint = Field.Constraint.NOT_NULL, json_name = "user_auth_expire_time")
	protected long mExpires;

	@Field(type = Field.Type.TEXT, constraint = Field.Constraint.NOT_NULL)
	protected String mEmail;

	@Field(type = Field.Type.NUMBER, constraint = Field.Constraint.NOT_NULL, json_name = "point_balance")
	protected int mBalance;

	@Field(type = Field.Type.LIST)
	protected List<Integer> mApps;

	@Field(type = Field.Type.LIST) 
	protected List<String> mPurchases;
	
	@Field(type = Field.Type.TEXT)
	protected String mUsername;
	
	public String getUsername(){
		return this.mUsername;
	}
	
	public List<String> getPurchases() { 
		return this.mPurchases;
	}
	
	public boolean isValid() {
		return System.currentTimeMillis() < this.mExpires;
	}

	public String getAuthHash() {
		return this.mSessionId;
	}

	public int getUserId() {
		return this.mUserId;
	}

	public List<Integer> getApps() {
		return this.mApps;
	}

	@Override
	public IParser<? extends IReturnable> getParser() {
		return new LoginSessionParser();
	}

	private static final char[] sHexChars = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	@Override
	public String getUri(Map<String, String> args) {

		if (!args.containsKey("login") || !args.containsKey("password")) {
			return null;
		}
		final StringBuilder sb = new StringBuilder("https://billing.mikandi.com/v1/user/login");
		sb.append("?username=").append(args.get("login"));
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		md.update(args.get("password").getBytes());
		sb.append("&password_md5=");
		final byte[] hashBytes = md.digest();
		
		for (final Byte b : hashBytes) {
			sb.append(sHexChars[(b & 0xF0) >> 4]).append(sHexChars[b & 0x0F]);
		}
		return sb.toString();

	}

	public IReturnableCache<? extends IReturnable> getCache(Context ctx) {
		return new EmptyCache<LoginSession>();
	}
}

class LoginSessionParser implements IParser<LoginSession> {
	
	private boolean PARSER = false;

	public <T> boolean parse(JSONObject jo, T empty) {
		boolean ret = true;
		long startTime = System.currentTimeMillis();
		final ParserUtils p = new ParserUtils(jo);
		long sTotalTime = 0;
		LoginSession obj = (LoginSession) empty;
		try {
			
			obj.mSessionId = p.requireString("user_auth_hash");
			obj.mUserId = p.requireInteger("user_id");
			obj.mExpires = p.loadLong("user_auth_expires_time", 0);
			obj.mBalance = p.loadInteger("point_balance", -1);
			obj.mEmail = p.loadString("email", "no-email");
			
			if (jo.has("apps")) {
				obj.mApps = p.loadIntegerList("apps", null);
			}
			if (jo.has("purchases")){
				obj.mPurchases = p.loadStringList("purchases", null);
			}
			
		} catch (Exception e) {
			ret = false;
		}
		long interval = (System.currentTimeMillis() - startTime);
		return ret;
	}
}
