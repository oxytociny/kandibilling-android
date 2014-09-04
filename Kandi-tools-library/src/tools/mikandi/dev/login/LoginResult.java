package tools.mikandi.dev.login;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The result retrieved from a previous call to {@link KandiBillingClient#login}
 * 
 * @author MiKandi, LLC
 */
public final class LoginResult implements Parcelable {

	protected ArrayList<String> mTokens = new ArrayList<String>();

	/**
	 * Gets a list of any purchase IDs which the logged in user has in their
	 * account.
	 * 
	 * @return An array of foreign in-app purchase ids
	 */
	@Deprecated
	public String[] getPurchaseIds() {
		return this.getTokens();
	}

	
	
	public void setArrayListTokens(ArrayList<String> myArrayList){
		this.mTokens = myArrayList;
	}
	
	
	/**
	 * Gets a list of any tokens which the logged in user has in their
	 * account.
	 * 
	 * @return An array of tokens
	 */
	public String[] getTokens() {
		if (mTokens == null) {
			return null;
		}
		String[] ret = new String[mTokens.size()];
		for (int i = 0; i < mTokens.size(); i++) {
			ret[i] = mTokens.get(i);
		}
		return ret;
	}
	
	public ArrayList<String> getArrayListTokens() { 
		return this.mTokens;
	}
	
	public void setTokensListToArray(List<String> tokens) { 
	 for (String s : tokens) { 
		 mTokens.add(s);
	 }
	}
	/**
	 * Used to add token to the token list stored in the login result
	 * 
	 */
	public void addToTokens(String s) { 
		if (mTokens.contains(s)){
			return; 
		} else { 
			mTokens.add(s);
		}
	}
	protected int mUserId;

	/**
	 * Gets the numeric user ID of the user who logged in (if the login was
	 * successful).
	 * 
	 * @return
	 */
	public int getUserId() {
		return mUserId;
	}

	
	@Override 
	public String toString() { 
		StringBuilder sb = new StringBuilder("Printing Login: ");
		sb.append("username :").append(this.getDisplayName()); 
		sb.append("UserId").append(this.getUserId());
		sb.append("User Auth Hash :").append(this.mUserAuthHash);
		sb.append("UserAuth Expires : " ).append(this.mUserAuthExpires);
		return sb.toString();
	}
	
	public String getUserIdString() { 
		String userIdString = Integer.toString(getUserId());
		return userIdString; 
	}
	
	protected int mResult;

	/**
	 * Get the specific result code associated with the login request.
	 */
	public int getResult() {
		return mResult;
	}

	/**
	 * Returns whether the login was successful.
	 */
	public boolean isSuccessful() {
		return mResult == RESULT_LOGIN_SUCCESS;
	}
	
	protected String mUserAuthHash;
	
	/**
	 * Get the User Auth Hash associated with the user session
	 */
	public String getUserAuthHash() {
		return mUserAuthHash;
	}
	
	protected String mUserAuthExpires;
	
	/**
	 * Gets the User Auth Expiration associated with the user session
	 */
	public String getUserAuthExpires() {
		return mUserAuthExpires;
	}
	
	protected String mDisplayName;
	
	/**
	 * Gets the name to call the user when referring to them
	 */
	public String getDisplayName() {
		return this.mDisplayName;
	}

	/**
	 * There was an unspecified error in the login process.
	 */
	public static final int RESULT_LOGIN_ERROR = 0x00;
	/**
	 * The user declined the login prompt.
	 */
	public static final int RESULT_LOGIN_CANCELED = 0x01;
	/**
	 * The user logged in successfully. A unique "user ID" can be obtained by
	 * calling {@link #getUserID()}.
	 */
	public static final int RESULT_LOGIN_SUCCESS = 0x02;

	/**
	 * Produces a "human readable" status message from the result code. This is
	 * not intended as a message to end users, but rather a convenience for
	 * debugging.
	 * 
	 * @return The status message.
	 */
	public String getStatusMessage() {
		switch (mResult) {
		case RESULT_LOGIN_ERROR:
			return "There was an unspecified error in the login process";
		case RESULT_LOGIN_CANCELED:
			return "The user declined the login prompt";
		case RESULT_LOGIN_SUCCESS:
			return "The user successfully logged in";
		default:
			return "There was an unknown error in the login process";
		}
	}
	
	public LoginResult(int result, int user_id, String[] tokens, String userAuthHash, String userAuthExpires, String displayName) {
		mResult = result;
		mUserId = user_id;
		mUserAuthHash = userAuthHash;
		mUserAuthExpires = userAuthExpires;
		mDisplayName = displayName;
		if (tokens != null) {
			for (String p : tokens) {
				mTokens.add(p);
			}
		}
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mResult);
		dest.writeInt(mUserId);
		dest.writeStringList(mTokens);
		dest.writeString(mUserAuthHash);
		dest.writeString(mUserAuthExpires);
		dest.writeString(mDisplayName);
	}

	private LoginResult(Parcel in) {
		mResult = in.readInt();
		mUserId = in.readInt();
		mTokens.clear();
		in.readStringList(mTokens);
		mUserAuthHash = in.readString();
		mUserAuthExpires = in.readString();
		mDisplayName = in.readString();
	}

	public static final Parcelable.Creator<LoginResult> CREATOR = new Parcelable.Creator<LoginResult>() {
		public LoginResult createFromParcel(Parcel in) {
			return new LoginResult(in);
		}

		public LoginResult[] newArray(int size) {
			return new LoginResult[size];
		}
	};
}
