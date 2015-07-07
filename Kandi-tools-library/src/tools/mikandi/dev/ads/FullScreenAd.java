package tools.mikandi.dev.ads;


import tools.mikandi.dev.library.KandiLibs;
import tools.mikandi.dev.utils.UserInfoObject;

import com.mikandi.developertools.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.webkit.JavascriptInterface;

public class FullScreenAd extends Activity   {

	WebView wv;
	String url = "http://as.sexad.net/as/pu?p=mikandi&";
	ImageView v;
	RelativeLayout rl;
	OnFullScreenAdDisplayedListener mListener;
	String AppIdVariable  = "cid";
	String pubIdVar = "hn";
	Context c;
	WebAppinterface wai;

	public void finish() {
		if (wv != null)	wv.loadUrl("about:blank");
		if (this.mListener != null) this.mListener.AdFinished(); 
		UserInfoObject.getInstance(this).setFullScreenAdListener(null);   // remove listener once closed. 
		super.finish();
	}

	@SuppressLint({"ResourceAsColor", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		try {
		getActionBar().hide();
		setContentView(R.layout.fullscreen);
		rl = (RelativeLayout) findViewById(R.id.relative_layout);
	
		this.mListener = UserInfoObject.getInstance(this).getAdListener();
		if (mListener == null) { 
			Log.e("fullScreenAd" , "No listener instantiated , make sure to setfullScreenAdDisplayedListener "); 
			finish();
		}
		
		rl.setBackgroundColor(R.color.black);
		wv = (WebView) findViewById(R.id.webview);
		v = (ImageView) findViewById(R.id.btn_quit);
		wv.setScrollContainer(false);
		wv.setVerticalScrollBarEnabled(false);
		wv.setHorizontalScrollBarEnabled(false);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	    wv.getSettings().setUseWideViewPort(true);
	    wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	   	wv.getSettings().setJavaScriptEnabled(true);

	  	c = (Context) this;
	   	wai = new WebAppinterface(this);
	   	wv.addJavascriptInterface(wai, "Android");
	   	
	   	wv.setWebViewClient(new WebViewClient() {
	   		@Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        	// false the webview handles it 
	        	return false;
	        }

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}	        
	    });
	   	
	    wv.setInitialScale(1) ; 
	    if (Build.VERSION.SDK_INT >= 11){
	        wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	        // this stops a background glitch on nexus phones 
	    }
	    
	    // http://as.sexad.net/as/pu?p=mikandi&v=3954&hn=1487872&cid=13083
	    String appId = UserInfoObject.getInstance(this).getAppId();
	    String publisherId = UserInfoObject.getInstance(this).getPublisherId();
	    
	    url = new StringBuilder(url).
	    		append("&").append(pubIdVar).append("=").append(publisherId).
	    		append("&").append(AppIdVariable).append("=").append(appId)
	    		.toString();
	    
	    wv.loadUrl(url);	
	    v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}});	
	    
	}
	    catch (Exception E) { 
	    	E.printStackTrace();finish();
	    }
	}


	

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onResume() {
		super.onResume();
		rl.setBackgroundColor(R.color.white);
	}	
	
	
	public class WebAppinterface { 
		
		Context mContext; 
		
		public WebAppinterface(Context c) { 
			this.mContext = c; 
		}
		
		@JavascriptInterface
		public void launch(String url) {
			Log.i("WebAppInterface" , "url called" + url);
			Uri u = Uri.parse(url);
			
			if (u == null){
				Toast.makeText(mContext, "Bad url, can't open " , Toast.LENGTH_SHORT).show();
				finish(); 
			}
			
			mContext.startActivity(new Intent(Intent.ACTION_VIEW, u));
		}
	}
	
}