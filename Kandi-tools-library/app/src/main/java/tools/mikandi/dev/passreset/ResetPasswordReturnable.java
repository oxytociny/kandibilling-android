package tools.mikandi.dev.passreset;

import java.net.URLEncoder;
import java.util.Map;

import android.content.Context;

import tools.mikandi.dev.login.AAppReturnable;
import com.saguarodigital.returnable.IParser;
import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.IReturnableCache;
import com.saguarodigital.returnable.annotation.Type;
import com.saguarodigital.returnable.defaultimpl.AutoParser;
import com.saguarodigital.returnable.defaultimpl.EmptyCache;

@Type(version = 1, type = Type.JSONDataType.EMPTY)
public class ResetPasswordReturnable extends AAppReturnable {

	@Override
	public IParser<? extends IReturnable> getParser() {
		return new AutoParser<ResetPasswordReturnable>();
	}

	@Override
	public String getUri(Map<String, String> args) {
		ensureElements(args, EMAIL);
		final StringBuilder sb = new StringBuilder(
				"https://billing.mikandi.com/v1/user/reset_password");
		sb.append("?email=").append(URLEncoder.encode(args.get(EMAIL).trim()));
		return sb.toString();
	}

	@Override
	public IReturnableCache<? extends IReturnable> getCache(Context ctx) {
		return new EmptyCache<ResetPasswordReturnable>();
	}
}