/**
 * 
 */
package tools.mikandi.dev.inapp;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

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

/**
 * @author rekaszeru
 * 
 */

@Type(version = 1, type = Type.JSONDataType.OBJECT)
public class AccountBalancePoint implements IReturnable {

	// TODO: Should this be a weak reference
	// private AccountBalancePointCache mCache = null;
	//
	// Fields
	//

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
		return new AccountBalencePointParser(); 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#describeContents()
	 */
	

	
	private final class AccountBalencePointParser implements IParser<AccountBalancePoint> {
		private long sTotalTime = 0;
		private final String sPointBalence = "point_balance";
		private final String sTimeStamp = "time_stamp";
		private final String TAG = "AccountBalencePointParser"; 
		
		@Override
		public <T> boolean parse(JSONObject jo, T empty) {
			boolean ret = true; 
			long startTime = System.currentTimeMillis();
			final ParserUtils p = new ParserUtils(jo);
			AccountBalancePoint obj = (AccountBalancePoint) empty;
			final int fallBackBalance = -1; 
			final int fallBackTimestamp = 0;
			if (Logger.parserDebug) Log.d(TAG, "Parsing" + empty.getClass().getSimpleName());
			
			try {
					obj.mBalance = p.loadInteger(sPointBalence, fallBackBalance);
					obj.mTimestamp = p.loadLong(sTimeStamp, fallBackTimestamp);
					Log.e("printing the Balance : " , "" +  obj.mBalance);
					Log.e("printing timeStamp " , "" + obj.mTimestamp);			
			
			} catch (Exception e) {
				if (Logger.parserDebug) Log.e(TAG, e.getMessage(), e);
		} 
		
		long interval = (System.currentTimeMillis() - startTime); 
		if (Logger.parserDebug) sTotalTime += interval; 
		if (Logger.parserDebug) Log.e("SpeedTest" , " Parsed a " + empty.getClass().getSimpleName() 
				+ " in " + interval + " ms (time total " + sTotalTime + " ms)");

		return ret; 

		} 
			}

	
	// Unusued aldebran function
	@Override
	public IReturnableCache<? extends IReturnable> getCache(Context arg0) {
		return null;
	}


				}
