package tools.mikandi.dev.login;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.saguarodigital.returnable.defaultimpl.JSONAsyncTask;
import com.saguarodigital.returnable.defaultimpl.JSONResponse;

/**
 * A JSONAsyncTask extension for the login process
 * @author rekaszeru
 *
 */
public class LoginAsyncTask extends JSONAsyncTask<UserLoginReturnable>
{
	/**
	 * The list of listeners attached to this task
	 */
	private final ArrayList<OnLoginResultListener> mListeners = new ArrayList<OnLoginResultListener>();
	private Context mContext;

	public LoginAsyncTask(final HashMap<String, String> queryArgs, final Context context)
	{
		super(context, UserLoginReturnable.class, queryArgs);
		this.mContext = context;
	}

	protected void onPostExecute(JSONResponse<UserLoginReturnable> result)
	{
		final int resultCode = result.getCode();
		
		if (resultCode == 493 || resultCode == 492) { 
			for (final OnLoginResultListener listener : mListeners)
				listener.onLoginUnsuccessful(resultCode);
			return;
		}
		if (resultCode != 200)
		{
			// clear any active login data
			LoginStorageUtils.clear(mContext);
			Log.e("login return code not 200 ", "error code : " + resultCode);
			for (final OnLoginResultListener listener : mListeners)
				listener.onLoginFailed(resultCode);
			return;
		}
		
		// successful login:
		final UserLoginReturnable ret = (UserLoginReturnable) result.getOne();
		LibraryLoginResult loginResult = null;
		
		//check for tokens
		if (ret.getTokens() == null) 
		{
		loginResult =
			new LibraryLoginResult(LibraryLoginResult.RESULT_LOGIN_SUCCESS, ret.getUserId(), null
			, ret.getAuthHash(), ret.getAuthExpires(), ret.getUsername());
					
		}
		else {
			// Sucessful login but with no tokens
			loginResult =
				new LibraryLoginResult(LibraryLoginResult.RESULT_LOGIN_SUCCESS
						, ret.getUserId()
						, ret.getTokens().toArray(new String[ret.getTokens().size()])
						, ret.getAuthHash()
						, ret.getAuthExpires()
						, ret.getUsername());
		}
		
		LoginStorageUtils.setLogin(mContext, loginResult);
		for (final OnLoginResultListener listener : mListeners) listener.onLoginSuccess(loginResult);
	}

	/**
	 * Attach a listener to this task
	 * @param listener the login result listener to attach to this task
	 */
	public void addListener(final OnLoginResultListener listener)
	{
		mListeners.add(listener);
	}
}

