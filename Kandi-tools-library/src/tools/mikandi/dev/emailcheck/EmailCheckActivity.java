package tools.mikandi.dev.emailcheck;

import com.mikandi.developertools.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import tools.mikandi.dev.login.*;

/**
 * 
 * @author Mike Docherty - March 2014
 */

public class EmailCheckActivity extends Activity {

	// EditText forgotPassword_email_input;
	TextView emailCheck_text;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.emailcheck_layout);
		final Context context = getApplicationContext();

		Button EmailCheckActivity_btn_cancel = (Button) findViewById(R.id.emailCheck_btn_cancel);
		Button EmailCheckActivity_btn_login = (Button) findViewById(R.id.emailCheck_bt_login);

		EmailCheckActivity_btn_cancel.setEnabled(true);
		EmailCheckActivity_btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishActivity();
			}
		});

		EmailCheckActivity_btn_login.setEnabled(true);
		EmailCheckActivity_btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishActivityAndReturnToLogin(context);
			}
		});

	}

	private void finishActivity() {
		EmailCheckActivity.this.finish();
	}

	private void finishActivityAndReturnToLogin(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		startActivity(intent);
		EmailCheckActivity.this.finish();
	}

	/**
	 * not used at the moment, probabaly later will add a clickable link to the
	 * register message
	 * 
	 * @param context
	 */

}
