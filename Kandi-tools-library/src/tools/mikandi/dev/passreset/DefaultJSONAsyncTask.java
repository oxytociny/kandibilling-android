/**
 * 
 */
package tools.mikandi.dev.passreset;

import java.util.Map;

import android.content.Context;
import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.defaultimpl.JSONAsyncTask;
import com.saguarodigital.returnable.defaultimpl.JSONResponse;

/**
 * @author rekaszeru
 * 
 */
public class DefaultJSONAsyncTask<T extends IReturnable> extends JSONAsyncTask<T>
{
	private OnJSONResponseLoadedListener<T> mListener;

	public DefaultJSONAsyncTask(final Class<T> typeClass, final Context ctx, 
			final OnJSONResponseLoadedListener<T> listener, Map<String, String> args) throws Exception
	{
		super(ctx, typeClass, typeClass.newInstance().getUri(args));
		this.mListener = listener;
	}

	@Override
	protected void onPostExecute(JSONResponse<T> result)
	{
		this.mListener.onJSONLoaded(result);
	}
}
