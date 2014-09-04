/**
 * 
 */
package tools.mikandi.dev.passreset;

import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.defaultimpl.JSONResponse;

/**
 * @author rekaszeru
 *
 */
public interface OnJSONResponseLoadedListener<T extends IReturnable>
{
	void onJSONLoaded(JSONResponse<T> jsonResponse);
}
