package tools.mikandi.dev.passreset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.mikandi.developertools.R;
import com.saguarodigital.returnable.defaultimpl.JSONResponse;

import tools.mikandi.dev.emailcheck.EmailCheckActivity;
import tools.mikandi.dev.login.LoginActivity;
import tools.mikandi.dev.login.AAppReturnable;
import tools.mikandi.dev.passreset.ResetPasswordReturnable;
/**
 * This is the PasswordResetActivity and related xml, returnables, handlers and data. This class is only called from the 
 * forgot password button by hitting the forgotten button. It should appear (ideally) once or twice for user's trying 
 * to log on. 
 * @author devMike June/2014
 *
 */
public class PasswordResetActivity extends Activity implements OnJSONResponseLoadedListener<ResetPasswordReturnable> {
	
	private static final String PASSWORD_RESET = "pass-reset";
	private ProgressDialog mProgress;
	//AutoCompleteTextView forgotPassword_email_input;
	EditText forgotPassword_email_input;

	TextView warningMessage, inuse;
	boolean isDebug = true;
	Button password_reset_cancel;
	Context context;
	boolean otherChosen = false;
	
	
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.passwordreset_layout);
		context = getApplicationContext();
		setTitle("Reset Password");
		this.setTitleColor(Color.BLACK);


		inuse = (TextView) findViewById(R.id.redText_emailInUse);
		inuse.setVisibility(View.GONE);

		warningMessage = (TextView) findViewById(R.id.passwordReset_warningTextView);
		warningMessage.setVisibility(View.GONE);

		// need a counter for multiple failed entries

		if (this.getIntent() != null && this.getIntent().getAction() != null) {
			if (this.getIntent().getAction().equals(PASSWORD_RESET)) 
			{
				inuse.setVisibility(View.VISIBLE);
			} 
		}
		
		//forgotPassword_email_input = (AutoCompleteTextView) findViewById(R.id.passwordReset_emailField);
		forgotPassword_email_input = (EditText) findViewById(R.id.passwordReset_emailField);
		password_reset_cancel = (Button) findViewById(R.id.passwordReset_btn_cancel);
		password_reset_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finishActivity();
			}
		});

		Button password_reset_send = (Button) findViewById(R.id.passwordReset_btn_submit);
		password_reset_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String input = forgotPassword_email_input.getText().toString().trim();	
				onResetAccount(input);
			}
		});

		Button password_reset_login = (Button) findViewById(R.id.passwordReset_btn_login);
		password_reset_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finishActivityAndReturnToLogin(context);
			}
		});
	
	}
	/*
	 * Takes in the email address and passes it to the password reset task
	 */
	public void onResetAccount(String email) {
		this.mProgress = ProgressDialog.show(this, "Requesting Reset", "Requesting password reset...");
		Map<String, String> args = new HashMap<String, String>();
		args.put(AAppReturnable.EMAIL, email);
		try {
			(new DefaultJSONAsyncTask<ResetPasswordReturnable>(
					ResetPasswordReturnable.class, this, this, args)).execute();
		} catch (Exception e) {
			Log.e("Email Reset Activity" , "Exception thrown in pass reset activity");
			
		}
	}

	/**
	 * JSON Handler, handles response code and determines if the warning text is shown 
	 */
	@Override
	public void onJSONLoaded(JSONResponse<ResetPasswordReturnable> jsonResponse) {
		if (this.mProgress != null && this.mProgress.isShowing()) {
			this.mProgress.dismiss();
		}
		
		if (jsonResponse == null || jsonResponse.getCode() != 200) {
			Toast.makeText(this, "There was an error contacting MiKandi. Please try again", Toast.LENGTH_LONG).show();
			Log.i("PassresetActivity", "code" + jsonResponse.getCode());
		}
		
		if (jsonResponse.getCode() == 492){
			warningMessage.setVisibility(View.VISIBLE);;
				
		} else {
			Toast.makeText(context, "Sucessful passchange", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(context,EmailCheckActivity.class);
			startActivity(intent);
			PasswordResetActivity.this.finish();
			
		}
	}

	/**
	 * These methods allow this class to be intergrated properly into the main application 
	 */
	private void finishActivity() {
		PasswordResetActivity.this.finish();
	}

	private void finishActivityAndReturnToLogin(Context context){
		Intent intent = new Intent(context,LoginActivity.class);
		startActivity(intent);
		PasswordResetActivity.this.finish();
	}

}
