package tools.mikandi.dev.utils;


import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import tools.mikandi.dev.utils.Logger;
import tools.mikandi.dev.utils.Link;


/**
 * @author rekaszeru
 * @recycled by Mike Docherty 
 * 
 */
public class LinkView extends TextView
{
	public LinkView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public LinkView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public LinkView(Context context)
	{
		super(context);
	}
	
	public void addLinks(final MovementMethod method, final Link.OnClickListener listener, String... links)
	{
		if (method == null)
		{
			if (Logger.isDebug) Log.d(Logger.TAG, "Not adding links " + links + ", movement method is null!");
			return;
		}
		for (final String link : links)
			clickify(link, listener);
		this.setMovementMethod(method);
	}

	public void clickify(final String clickableText, final Link.OnClickListener listener)
	{
		CharSequence text = this.getText();
		String stringToLink = text.toString();
		Link linkSpan = new Link(listener, clickableText);

		int start = stringToLink.indexOf(clickableText);
		int end = start + clickableText.length();
		if (start == -1)
			return;

		if (text instanceof Spannable)
		{
			((Spannable) text).setSpan(linkSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		else
		{
			SpannableString s = SpannableString.valueOf(text);
			s.setSpan(linkSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			this.setText(s);
		}
	}
}
