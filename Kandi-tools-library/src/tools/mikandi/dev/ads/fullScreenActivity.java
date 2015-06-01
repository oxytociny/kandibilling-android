package tools.mikandi.dev.ads;


import com.mikandi.developertools.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class fullScreenActivity extends Activity {

	WebView wv;
	
	@Override
	public void finish() {
		super.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		getActionBar().hide();
		setContentView(R.layout.fullscreen);
		Intent i = getIntent(); 
		
		//String url = "http://as.sexad.net/as/pu?p=mikandi&v=3954";
		String url = "http://fap.ninja/testad";
		
		if (i.hasCategory("ref")) { 
			url = url + "&hn=1587633";
		}
		
		wv = (WebView) findViewById(R.id.webview);
		wv.setScrollContainer(false);
		wv.setVerticalScrollBarEnabled(false);
		wv.setHorizontalScrollBarEnabled(false);
		wv.getSettings().setLoadWithOverviewMode(true);
	    wv.getSettings().setUseWideViewPort(true);
	    wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	    wv.getSettings().setJavaScriptEnabled(true);
	    wv.loadUrl(url);	
		
		ImageView v = (ImageView) findViewById(R.id.btn_quit);
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Quit testing", Toast.LENGTH_SHORT).show(); 
				finish();
			}});	
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}	
}
