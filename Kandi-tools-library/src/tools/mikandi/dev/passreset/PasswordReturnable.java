/**
 * 
 */
package tools.mikandi.dev.passreset;

import java.util.Map;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;





import tools.mikandi.dev.login.*;
import com.saguarodigital.returnable.IParser;
import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.IReturnableCache;
import com.saguarodigital.returnable.annotation.Type;
import com.saguarodigital.returnable.defaultimpl.AutoParser;
import com.saguarodigital.returnable.defaultimpl.EmptyCache;

/**
 * 
 * @author rekaszeru
 *
 */
@Type(
		version = 1,
		type = Type.JSONDataType.OBJECT
)
public class PasswordReturnable implements IReturnable, Parcelable
{ 
	public static final String KEY_PASSWORD_OLD = "old_password";
	public static final String KEY_PASSWORD_NEW = "new_password";
	
	///
	/// IReturnable Methods
	///

	@Override
	public IParser<? extends IReturnable> getParser()
	{
		return new AutoParser<PasswordReturnable>();
	}

	@Override
	public String getUri(Map<String, String> args)
	{
		AAppReturnable.ensureElements(args, AAppReturnable.USER_ID, AAppReturnable.AUTH_HASH, AAppReturnable.AUTH_EXPIRES, 
				KEY_PASSWORD_OLD, KEY_PASSWORD_NEW);
		final StringBuilder sb = new StringBuilder("https://billing.mikandi.com/v1/user/change_password")
			.append('?').append(AAppReturnable.USER_ID).append('=').append(args.get(AAppReturnable.USER_ID))
			.append('&').append(AAppReturnable.AUTH_HASH).append('=').append(args.get(AAppReturnable.AUTH_HASH))
			.append('&').append(AAppReturnable.AUTH_EXPIRES).append('=').append(args.get(AAppReturnable.AUTH_EXPIRES))
			.append('&').append(KEY_PASSWORD_OLD).append('=').append(args.get(KEY_PASSWORD_OLD))
			.append('&').append(KEY_PASSWORD_NEW).append('=').append(args.get(KEY_PASSWORD_NEW));
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.saguarodigital.returnable.IReturnable#getCache(android.content.Context)
	 */
	@Override
	public IReturnableCache<? extends IReturnable> getCache(Context arg0)
	{
		//return new EmptyCache<AppOverview>();
		return null;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents()
	{
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{}

	public static final Parcelable.Creator<PasswordReturnable> CREATOR = new Parcelable.Creator<PasswordReturnable>()
	{
		@Override
		public PasswordReturnable createFromParcel(Parcel source)
		{
			final PasswordReturnable passwordReturnable = new PasswordReturnable();
			return passwordReturnable;
		}

		@Override
		public PasswordReturnable[] newArray(int arg0)
		{
			throw new UnsupportedOperationException();
		}
	};
}
