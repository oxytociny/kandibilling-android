package tools.mikandi.dev.login;

import tools.mikandi.dev.login.LibraryLoginResult;

/**
 * Listener interface for providing feedback about a login process
 * @author rekaszeru
 *
 */
public interface OnLoginResultListener
{
	/**
	 * Called when the login was successful. 
	 * @param result the LoginResult instance associated with this login
	 */
	void onLoginSuccess(LibraryLoginResult result);
	
	/**
	 * Called when the login failed. (not user fault) 
	 * @param rCode 
	 * @param errorCode the code returned by the server in the login response.
	 */
	void onLoginFailed(int rCode);
	
	/** 
	 * called when the user supplies incorrect credentials. 
	 * @param resultCode
	 */
	void onLoginUnsuccessful(int resultCode);
	
}
