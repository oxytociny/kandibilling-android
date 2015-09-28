package tools.mikandi.dev.utils;

import java.util.Map;

import android.content.Context;
import android.telephony.TelephonyManager;

public class GetDeviceAndUserInfo {

	public static final Map<String, String> getDefaultArgs(final Context ctx) {
		final Map<String, String> ret = MiKandiUtils.getUserAuthArgs(ctx.getApplicationContext());
		final TelephonyManager telephony = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		ret.put("md_nc", verifyData(telephony.getNetworkCountryIso()));
		ret.put("md_no", telephony.getNetworkOperator());
		// https://github.com/mikandi/v4/blob/7551eae2a6dd7280ba2a558d5aef47fdd4f9645f/src/com/mikandi/android/v4/events/EventService.java
		return ret;
	}

	private static final String verifyData(String input) {
		return input == null || input.length() < 1 ? "empty" : input;
	}

    public static final String buildQueryString(String url, Map<String, String> args) {
    	final StringBuilder sbUrl = new StringBuilder(url);
    	char separator = '?';
    	for (Map.Entry<String, String> entry : args.entrySet()) {
    		// TODO: Check for invalid characters and URL escape as appropriate
    		// http://stackoverflow.com/questions/3286067/url-encoding-in-android
    		sbUrl.append(separator).append(entry.getKey()).append('=').append(entry.getValue());
    		if (separator == '?') {
    			separator = '&';
    		}
    	}
    	return sbUrl.toString();
    }

	
}