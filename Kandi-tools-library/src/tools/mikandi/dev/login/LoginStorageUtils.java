package tools.mikandi.dev.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import tools.mikandi.dev.login.LibraryLoginResult;

public final class LoginStorageUtils {
	private static final String sName = "kb-login";
	private static final String sLogin = "login";
	private static final String sCount = "count";
	private static final String sUid = "uid";
	private static final String sUAHash = "user-auth-hash";
	private static final String sUAExpires = "user-auth-expires";
	private static final String sUADisplayName = "user-display-name";
	private static final String sUsername = "username";
	
	
	public static boolean isLoggedIn(final Context ctx) {
		final SharedPreferences sp = ctx.getSharedPreferences(sName, 0);
		if (!sp.contains(sLogin)) {
			return false;
		}
		if (Long.parseLong(sp.getString(sUAExpires, "0"), 10) < (System
				.currentTimeMillis() / 1000)) {
			Log.i("MiKandiBilling", "Login session expired");
			clear(ctx);
			return false;
		}
		return true;
	}

	public static void clear(final Context ctx) {
		ctx.getSharedPreferences(sName, 0).edit().clear().commit();
	}

	public static void setLogin(Context ctx, final LibraryLoginResult lr) {
		final SharedPreferences.Editor ed = ctx.getSharedPreferences(sName, 0)
				.edit();
		ed.putBoolean(sLogin, true);
		ed.putInt(sUid, lr.mUserId);
		ed.putString(sUAExpires, lr.getUserAuthExpires());
		ed.putString(sUAHash, lr.getUserAuthHash());
		ed.putString(sUsername, lr.getUsername());
		final String[] tokens = lr.getTokens();
		final int tlen = tokens.length;
		ed.putInt(sCount, tlen);
		for (int i = 0; i < tlen; i += 1) {
			ed.putString(sCount + String.valueOf(i), tokens[i]);
		}
		ed.commit();
	}

	// Returns a library Login Result object, allowing user values to be retreived. 
	public static LibraryLoginResult getLogin(Context ctx) {
		if (!LoginStorageUtils.isLoggedIn(ctx)) {
			return null;
		}
		final SharedPreferences sp = ctx.getSharedPreferences(sName, 0);
		final int tlen = sp.getInt(sCount, 0);
		String[] tokens = null;
		if (tlen > 1) {
			tokens = new String[tlen];
			for (int i = 0; i < tlen; i += 1) {
				tokens[i] = sp.getString(sCount + String.valueOf(i), null);
			}
		}
		return new LibraryLoginResult(LibraryLoginResult.RESULT_LOGIN_SUCCESS, sp.getInt(
				sUid, -1), tokens, sp.getString(sUAHash, null), sp.getString(
				sUAExpires, null), sp.getString(sUsername , null));
	}

	public static boolean containsAnyTokens(Context ctx) {
		final LibraryLoginResult tempLogin = getLogin(ctx);
		if (tempLogin.getTokens().length > 0) {
			return true;
		} else {
			return true;
		}
	}

	/**
	 * for use when a new loginResult is returned from server.
	 * 
	 * @param ctx
	 * @param newestLr
	 */
	public static void updateTokens(Context ctx, LibraryLoginResult newestLr) {
		if (containsAnyTokens(ctx)) {
			final LibraryLoginResult tempLogin = LoginStorageUtils.getLogin(ctx);
			// swaps loginresult for newer one.
			if (newestLr.getTokens().length > tempLogin.getTokens().length) {
				LoginStorageUtils.setLogin(ctx, newestLr);
			}

		}
	}

	/**
	 * this is a function to force refresh tokens in loginResult
	 * 
	 */
	public static void refreshToken(Context ctx, String[] newTokens) {
		final LibraryLoginResult currentLR = LoginStorageUtils.getLogin(ctx);
		String[] currentLRTokens = currentLR.getTokens();

		for (String s : newTokens) {
			if (!containString(currentLRTokens, s)) {
				currentLR.addToTokens(s);
			}
		}
		LoginStorageUtils.setLogin(ctx, currentLR);
	}

	public static boolean containString(String[] stringArray, String s) {
		for (int i = 0; i < stringArray.length; i++) {
			if (stringArray[i].equalsIgnoreCase(s))
				return true;
		}
		return false;
	}

}
