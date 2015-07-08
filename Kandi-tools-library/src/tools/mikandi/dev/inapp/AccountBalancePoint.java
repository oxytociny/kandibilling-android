/**
 * 
 */
package tools.mikandi.dev.inapp;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

import tools.mikandi.dev.login.UserLoginReturnable;
import tools.mikandi.dev.utils.GetDeviceAndUserInfo;
import tools.mikandi.dev.utils.Logger;
import tools.mikandi.dev.utils.ParserUtils;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.saguarodigital.returnable.IParser;
import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.IReturnableCache;
import com.saguarodigital.returnable.annotation.Field;
import com.saguarodigital.returnable.annotation.Type;
import com.saguarodigital.returnable.defaultimpl.AutoParser;
import com.saguarodigital.returnable.defaultimpl.EmptyCache;

/**
 * @author rekaszeru
 * 
 */

@Type(version = 1, type = Type.JSONDataType.OBJECT)
public class AccountBalancePoint implements IReturnable {

	@Field(json_name = "point_balance", type = Field.Type.NUMBER, constraint = Field.Constraint.NOT_NULL)
	protected int mBalance;

	@Field(type = Field.Type.NUMBER)
	protected long mTimestamp;

	// /
	// / Accessors
	// /

	/**
	 * Fetches the gold amount currently owned by the user
	 * 
	 * @return The number of MiKandi Gold for this user.
	 */
	public int getBalance() {
		return this.mBalance;
	}

	/**
	 * Returns the timestamp of (FIXME (r):) [the fetch / last modification] for
	 * this balance<br />
	 * This value should be x1000 and then read in as a {@link Date} object!
	 * 
	 * @return the mTimestamp
	 */
	public long getTimestamp() {
		return mTimestamp;
	}

	// /
	// / IReturnable Methods
	// /
	@Override
	public IParser<? extends IReturnable> getParser() {
		return new AutoParser<AccountBalancePoint>();
	}

	@Override
	public String getUri(Map<String, String> args) {
		return GetDeviceAndUserInfo.buildQueryString(
				"https://billing.mikandi.com/v1/user/point_balance", args);
	}

	// /
	// / Object methods
	// /
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof AccountBalancePoint))
			return false;
		return this.mBalance == ((AccountBalancePoint) other).mBalance
				&& this.mTimestamp == ((AccountBalancePoint) other).mTimestamp;
	}

	@Override
	public String toString() {
		return "Balance is : " + mBalance + "checked at :" + mTimestamp;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	@Override
	public IReturnableCache<? extends IReturnable> getCache(Context ctx) {
	return new EmptyCache<UserLoginReturnable>();
	}

}
