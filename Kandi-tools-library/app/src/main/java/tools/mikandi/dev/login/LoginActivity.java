package tools.mikandi.dev.login;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mikandi.developertools.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import tools.mikandi.dev.library.KandiLibs;
import tools.mikandi.dev.passreset.PasswordResetActivity;
import tools.mikandi.dev.utils.UserInfoObject;



//Mike Docherty 3/2014

/*
 * Login Activity
 * 
 * Pretty generic login really, does everything a login activity is meant to do. The user is redirected to the forgotten password activity 
 * after the third incorrect attempt. We do all the processing here on the server side and just sanitise the input to determine only valid characters 
 * are used. Everything else is done server side, and upon any incorrect attempt both fields are set to "". 
 */

public class LoginActivity extends Activity implements OnLoginResultListener, OnClickListener, OnEditorActionListener {

	final boolean isDebug = false;
	EditText loginActivity_field_Username;
	EditText loginActivity_field_Password;
	Button loginActivity_btn_login, loginActivity_btn_forgot, loginActivity_btn_register;
	private String usernameClean, tempString_passwordCheck;
	String title = "Login to Mikandi";
	ProgressDialog progressD;
	int errorCounter;
	String mAppId, mSecret;

	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);
		
		Intent lIntent = getIntent();

		mSecret = lIntent.getStringExtra("secretkey");
		mAppId = lIntent.getStringExtra("appid");
		// ^^^^^^^^^ needing the stuff in drawables ...
		setTitle(title);
		setTitleColor(Color.BLACK);
		errorCounter = 0;
		
		loginActivity_field_Username = (EditText) findViewById(R.id.Login_usernameField);
		loginActivity_field_Username.setHint("");
		loginActivity_field_Password = (EditText) findViewById(R.id.PasswordField);
		loginActivity_field_Password.setHint("");
		
		loginActivity_btn_forgot = (Button) findViewById(R.id.login_btn_forgot);
		loginActivity_btn_forgot.setOnClickListener(this);

		loginActivity_btn_forgot.setEnabled(true);
		loginActivity_btn_login = (Button) findViewById(R.id.login_btn_login);
		loginActivity_btn_login.setOnClickListener(this);
		loginActivity_btn_login.setEnabled(true);
		loginActivity_field_Password.setOnEditorActionListener(this);

	}

	@Override
	public void onBackPressed() {
		dismissDialog();
		this.finish();
	}

	/**
	 * Click handler for all the buttons 
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		int id = v.getId();
		if (id == R.id.login_btn_forgot) { 
		
		dismissDialog();
		Intent intent = new Intent(LoginActivity.this, PasswordResetActivity.class);
		startActivity(intent); 
		if (isDebug) Log.i("Login Activity " , "User Directed to PasswordReset Activity "); 
		
		}
		
		if (id == R.id.login_btn_login) {
			String username = loginActivity_field_Username.getText().toString().trim();
			String password = loginActivity_field_Password.getText().toString().trim();
			String usernameError = getResources().getString(R.string.onetapreg_username_error);
			boolean okUsername = usernameCheck();
			
			if (!okUsername) {
				loginActivity_field_Username.setError(usernameError);
			}
				
		    if (okUsername == true && passwordCheck(password) == true) {
					progressD = ProgressDialog.show(LoginActivity.this,
							"Logging in!", "Logging in your device");
					loginUser(username, password, mAppId);
				}
			} else {
				loginActivity_field_Password
						.setError(getString(R.string.login_string_passError));
			}
		}

	
	// Handles the logging in of the user
	public void loginUser(String username, String password, String appId) {
		final HashMap<String, String> args = new HashMap<String, String>();
		args.put(AAppReturnable.PASSWORD, password);
		args.put(AAppReturnable.USERNAME, username);
		args.put(AAppReturnable.APP_ID, appId);
		final LoginAsyncTask task = new LoginAsyncTask(args, this);
		task.addListener((OnLoginResultListener) this); // <--------------- does it need casting?
	    task.execute();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.TextView.OnEditorActionListener#onEditorAction(android
	 * .widget.TextView, int, android.view.KeyEvent)
	 */
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_GO) {
			loginActivity_btn_login.performClick();
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mikandi.android.v4.listeners.OnLoginResultListener#onLoginSuccess
	 * (com.mikandi.android.lib.v4.LoginResult)
	 */
	public void onLoginSuccess(LibraryLoginResult result) {
		dismissDialog();
		final Context context = getApplicationContext();
		LoginStorageUtils.setLogin(context, result);
		this.finish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mikandi.android.v4.listeners.OnLoginResultListener#onLoginFailed(int)
	 */
	public void onLoginFailed(int errorCode) {
		dismissDialog();

		Toast.makeText(getApplicationContext(), "Login failed check network and try again!", Toast.LENGTH_LONG).show();
		Log.e("Login Failed (not just unsucessful) : " , "error code : " + errorCode ); 
	}

	
	@Override
	public void onLoginUnsuccessful(int resultCode) {
			dismissDialog();
			Log.i("LoginTest" , "Login was unsucessful, errorcode is " + resultCode);
			
			// if failed to login too many times, redirect user to password reset activity
			if (errorCounter == 2) {
				Intent intent = new Intent(LoginActivity.this, PasswordResetActivity.class);
				startActivity(intent); 
			}
			
			switch (resultCode) {
			case 492:
				errorCounter++;
				loginActivity_field_Username
						.setError("Username not recognised. You should Register! ");
				break;
			case 493:
				errorCounter++;
				if (isDebug)Log.i("Login Activity", "incorrect credentials");
				loginActivity_field_Username.setText("");
				loginActivity_field_Password.setText("");
				loginActivity_field_Username.setError("Incorrect credentials");
				loginActivity_field_Password.setError("Incorrect credentials");
				
				
				break;
			default:
				if (isDebug)
					Log.i("Login Activity", "something went wrong, error code "
							+ resultCode);
				break;
				
			
			}
		}
	// Login Activity Utils ---------------------------------------------------------------------
	
	// Checks password length is appropriate
	private boolean passwordCheck(String password) {
		this.tempString_passwordCheck = password;

		if (tempString_passwordCheck.length() > 5) {
			return true;
		} else {
			loginActivity_field_Password.setError("Password not long enough!");
			return false;
		}
	}

	// checks username is appropriate, by checking chars, and length
	private String usernameCleanup(String username) {
		this.usernameClean = username;
		usernameClean.trim();

		char[] usernameCharArray = usernameClean.toCharArray();
		for (int i = 0; i < usernameCharArray.length; i++) {
			if (usernameCharArray[i] == ' ')
				usernameCharArray[i] = '_';
		}

		String usernameCleaned = new String(usernameCharArray);
		return usernameCleaned;
	}

	private void dismissDialog() {
		if (progressD != null) {
			progressD.dismiss();
			progressD = null;
		}
	}

	private boolean usernameCheck() { // Tested and works!
		String temp = usernameCleanup(loginActivity_field_Username.getText()
				.toString().trim().toLowerCase(Locale.getDefault()));
		return checkUsernameAgainstPattern(temp) ? true : false;
	}
	

	// Check given String against Username regex
	private boolean checkUsernameAgainstPattern(String username) {

		final String USERNAME_PATTERN = "[a-zA-Z0-9._-]{5,}";
		Pattern pattern = null;
		Matcher matcher = null;

		pattern = Pattern.compile(USERNAME_PATTERN);
		matcher = pattern.matcher(username);
	
		return matcher.matches();	
	}
}
