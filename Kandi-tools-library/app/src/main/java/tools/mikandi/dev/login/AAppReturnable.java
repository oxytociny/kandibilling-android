package tools.mikandi.dev.login;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import android.content.Context;

import com.saguarodigital.returnable.IParser;
import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.IReturnableCache;

/**
 * Abstract base class for in app returnables which includes the signature
 * generation and password encryption algorithms.
 */
public abstract class AAppReturnable implements IReturnable {

	public static final String USER_ID = "user_id";
	public static final String AUTH_HASH = "user_auth_hash";
	public static final String AUTH_EXPIRES = "user_auth_expire_time";
	public static final String APP_ID = "app_id";
	public static final String TOKEN = "foreign_inapp_purchase_id";
	public static final String PASSWORD = "password";
	public static final String PASSWORD_MD5 = "password_md5";
	public static final String USERNAME = "username";
	public static final String SIGNATURE = "signature";
	public static final String APP_SECRET = "app_secret";
	public static final String DESCRIPTION = "description";
	public static final String AMOUNT = "amount";
	public static final String EMAIL = "email";
	public static final String SUBSCRIBED = "email_allowed";
	public static final String VIDEO_ID = "video_id";
	public static final String VIDEO_PRICE_POINT = "price_point";
	public static final String CHANNEL_ID = "channel_id";
	public static final String EBOOK_ID = "ebook_id";
	public static final String COMIC_ID = "comic_id";
	public static final String AUTHOR_ID = "author_id";
	public static final String EMPTYJSONSTRING = "empty";

	// /
	// / Crypto and Signature Methods
	// /

	/**
	 * Computes the in-app request signature using the specified arguments in
	 * order.
	 * 
	 * @param strings
	 *            The elements of the signature (not including the secret).
	 * @return The hashed signature
	 */
	protected String computeSHA256(String... strings) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			for (final String s : strings) {
				md.update(s.getBytes());
			}
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException("SHA-256 Cryptography Not Supported");
		}
		final BigInteger bigInt = new BigInteger(1, md.digest());
		return String.format("%064x", bigInt);
	}

	/**
	 * Computes an MD5 hash sum of the specified arguments.
	 * 
	 * @param string
	 *            The elements in the hash (in order)
	 * @return The hashed result
	 */
	protected String computeMD5(String... strings) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			for (final String s : strings) {
				md.update(s.getBytes());
			}
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException("MD5 Cryptography Not Supported");
		}
		final BigInteger bigInt = new BigInteger(1, md.digest());
		return String.format("%032x", bigInt);
	}

	public static void ensureElements(Map<String, String> args,
			String... required) {
		if (args == null) {
			if (required == null) {
				return;
			}
			if (required.length == 1 && required[0].equals("")) {
				return;
			}
			throw new RuntimeException("Required parameters '"
					+ Arrays.toString(required) + "' missing");
		}
		for (final String r : required) {
			if (!args.containsKey(r)) {
				throw new RuntimeException("Required parameter '" + r
						+ "' missing");
			}
		}
	}

	@Override
	public abstract IParser<? extends IReturnable> getParser();

	@Override
	public abstract String getUri(Map<String, String> args);

	@Override
	public abstract IReturnableCache<? extends IReturnable> getCache(Context ctx);
}