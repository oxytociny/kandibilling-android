package tools.mikandi.dev.utils;

import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Custom Span extension for TextView texts containing html anchors
 * 
 * @author rekaszeru
 * 
 */
public class Link extends ClickableSpan
{
	/**
	 * The string that will behave like a link
	 */
	private final String mStringToLink;
	/**
	 * The click listener
	 */
	private OnClickListener mListener;

	public Link(OnClickListener listener, final String stringToLink)
	{
		this.mListener = listener;
		this.mStringToLink = stringToLink;
	}

	@Override
	public void onClick(View widget)
	{
		if (mListener != null)
			mListener.onClick(mStringToLink);
	}

	/**
	 * Custom listener interface for handling click events on link spans
	 * 
	 * @author rekaszeru
	 */
	public interface OnClickListener
	{
		/**
		 * Called when a link has been clicked
		 * @param linkedString the link that has been clicked
		 */
		void onClick(final String linkedString);
	}
}