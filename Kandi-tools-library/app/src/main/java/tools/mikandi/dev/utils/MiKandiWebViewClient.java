package tools.mikandi.dev.utils;

import com.mikandi.developertools.R;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Custom {@link WebViewClient} extension to prevent navigating 
 * outside the webview
 * 
 * @author rekaszeru
 *
 */
public final class MiKandiWebViewClient extends WebViewClient
{
	/* (non-Javadoc)
	 * @see android.webkit.WebViewClient#shouldOverrideKeyEvent(android.webkit.WebView, android.view.KeyEvent)
	 */
	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event)
	{
		if (Logger.isDebug) Log.w(Logger.TAG, "got key " + event);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url)
	{
		if (url == null)
			return true;
		if (Logger.isDebug) Log.w(Logger.TAG, "got url " + url);
		final String mailtoPrefix = view.getContext().getString(R.string.intent_scheme_mailto) + ":";
		if (url.startsWith(mailtoPrefix))
		{
			final String address = url.replace(mailtoPrefix, "");
			// open mail client
			if (Logger.isDebug) Log.d(Logger.TAG, "open mail client for " + address);
			
			final Intent mailIntent = new Intent(Intent.ACTION_SEND);
			mailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mailIntent.setType(view.getContext().getString(R.string.dev_email_type));
			mailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{address});
			try
			{
				view.getContext().startActivity(mailIntent);
			}
			catch (Exception e)
			{
				if (Logger.isDebug) Log.d(Logger.TAG, "error opening " + url + " --> " + e.getMessage());
				Toast.makeText(view.getContext(), String.format(view.getContext().getString(
						R.string.toast_cant_open_client), "mail", address), Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			if (Logger.isDebug) Log.d(Logger.TAG, "open web browser for " + url);
			
			final Intent webIntent = new Intent(Intent.ACTION_VIEW);
			webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			webIntent.setData(Uri.parse(url));
			try
			{
				view.getContext().startActivity(webIntent);
			}
			catch (Exception e)
			{
				if (Logger.isDebug) Log.e(Logger.TAG, "error opening " + url + " --> " + e.getMessage());
				Toast.makeText(view.getContext(), String.format(view.getContext().getString(
						R.string.toast_cant_open_client), "web", url), Toast.LENGTH_LONG).show();
			}
		}
		return true;
	}
}