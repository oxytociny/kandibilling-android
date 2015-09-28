/**
 * 
 */
package tools.mikandi.dev.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tools.mikandi.dev.login.AAppReturnable;
import tools.mikandi.dev.login.LibraryLoginResult;
import tools.mikandi.dev.login.LoginStorageUtils;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;


/**
 * Utility class for handy static methods
 * @author rekaszeru
 *
 */
public final class MiKandiUtils {
	public static final String EMPTY = "";

	private static final String SHA256_ALGORITHM = "SHA-256";
	public static final String installer = "com.mikandi.vending";
	public static final SimpleDateFormat SDF = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
	public static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	public static final SimpleDateFormat SDF_HHMMSS = new SimpleDateFormat("HH:mm:ss", Locale.US);
	public static final SimpleDateFormat SDF_MMSS = new SimpleDateFormat("mm:ss", Locale.US);
	/**
	 * Simple date format for Google Analytics reports
	 * @target: 2013-02-17T16:42:18
	 */
	public static final SimpleDateFormat SDF_GA = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
	
	private static Bitmap dummy;
	
	/**
	 * Checks whether the application denoted by the passed package name is installed or not
	 * @param context the context from where to load the package manager
	 * @param packageName the application's package name
	 * @return true if the application is installed, false otherwise
	 */
	public static boolean isApplicationInstalled(/*final Context context,*/ final String packageName) {
		if (packageName == null || packageName.equals("none")) {
			return false;
		}
//		SIC: NPE thrown during debugging session, it shouldn't appear in real context.
//		SIC: it just appeared :(
//		if (sInstalledApps == null)
//			initInstalledAppCache(context, true);
		return sInstalledApps != null && sInstalledApps.containsKey(packageName);
	}
	
	/**
	 * Converts the passed float value (dip) into pixels based on the resource
	 * @param res the Resources to use for retrieving display metrics
	 * @param value the dip value to convert
	 * @return the passed dip's pixel equivalent.
	 * 
	 * @deprecated use getResources().getDimensionPixelSize(R.dimen.your_dimension_to_convert);
	 */
	@Deprecated
	public static float convertFromDip(final Resources res, final float value) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, res.getDisplayMetrics());
	}
	
	/// EXPAND - COLLAPSE

	/**
	 * Measures the height of the changeView based on the size 
	 * of the the view that should be expand, and returns it.
	 * 
	 * The two parameters are often the same (e.g. in case of a TextView,
	 * it's content changes, so it should be measured again, 
	 * and it will be the one that will be applied the animation to as well.
	 * 
	 * TODO: Add listener support for applied animation!
	 */
	private static int measureViewHeight(final View viewToExpand, final View changedView) {
		try {
			final Method m = changedView.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
			m.setAccessible(true);
			m.invoke(changedView, MeasureSpec.makeMeasureSpec(viewToExpand.getWidth(), MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		} catch (Exception e) {
			return -1;
		}
		int measuredHeight = changedView.getMeasuredHeight();
		return measuredHeight;
	}


	/**
	 * Fixes the broken tile mode (repeat) for config changes<br />
	 * Use for pattern backgrounds!
	 * 
	 * @param view the View who's background repeat should be fixed
	 * 
	 * @see {@link http://stackoverflow.com/questions/4077487/background-image-not-repeating-in-android-layout}
	 */
	public static void fixBackgroundTileMode(View view) {
		if (view == null)
			return;
		Drawable bg = view.getBackground();
		if (bg != null) {
			if (bg instanceof BitmapDrawable) {
				BitmapDrawable bitmap = (BitmapDrawable) bg;
				// make sure that we aren't sharing state anymore
				bitmap.mutate(); 
				bitmap.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			}
		}
	}
	
	public static Bitmap getDummyBitmap() {
		if (dummy == null) {
			dummy = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
		}
		return dummy;
	}

	private static long sInstalledAppsLastUpdated = System.currentTimeMillis();
	private static Map<String, Void> sInstalledApps = null;
	public static void initInstalledAppCache(final Context ctx, boolean forceRefresh) {
		if (sInstalledApps != null && !forceRefresh) {
			return;
		}
		final PackageManager pm = ctx.getPackageManager();
		final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		Map<String, Void> iapps = new HashMap<String, Void>(packages.size());
		for (final ApplicationInfo packageInfo : packages) {
			iapps.put(packageInfo.packageName, null);
		}
		sInstalledApps = iapps;
		sInstalledAppsLastUpdated = System.currentTimeMillis();
	}
	
	/**
	 * @return the sInstalledAppsLastUpdated
	 */
	public static long getInstalledAppsLastUpdated() {
		return sInstalledAppsLastUpdated;
	}
	
	public static Map<String, String> getUserAuthArgs(final Context ctx) {
		final Map<String, String> ret = new HashMap<String, String>();
		final LibraryLoginResult lr = LoginStorageUtils.getLogin(ctx);
		if (lr == null) {
			return ret;
		}
		ret.put(AAppReturnable.USER_ID, String.valueOf(lr.getUserId()));
		ret.put(AAppReturnable.AUTH_EXPIRES, lr.getUserAuthExpires());
		ret.put(AAppReturnable.AUTH_HASH, lr.getUserAuthHash());
		return ret;
	}
	
	public static void setBackgroundResource(final View v, final int resource) {
		final int padLeft = v.getPaddingLeft();
		final int padRight = v.getPaddingRight();
		final int padTop = v.getPaddingRight();
		final int padBottom = v.getPaddingBottom();
		v.setBackgroundResource(resource);
		v.setPadding(padLeft, padTop, padRight, padBottom);
	}

	/**
	 * Returns the SHA-256 checksum of the passed input stream
	 * @param fis the file input stream to check
	 * @return the SHA-256 checksum of the passed file input stream
	 */
	public static String getSha256(final FileInputStream fis)
	{
		try
		{
			MessageDigest digest = MessageDigest.getInstance(SHA256_ALGORITHM);
			byte[] dataBytes = new byte[1024];
			
			int nread = 0; 
			while ((nread = fis.read(dataBytes)) != -1)
			{
				digest.update(dataBytes, 0, nread);
			}
			final BigInteger bigInt = new BigInteger(1, digest.digest());
			return String.format("%064x", bigInt);
		}
		catch (NoSuchAlgorithmException e)
		{
			if (Logger.isDebug) Log.e(Logger.TAG, "SHA-256 Algorithm not supported");
		}
		catch (IOException e)
		{
			if (Logger.isDebug) Log.e(Logger.TAG, "SHA-256 File read error");
		}
		return null;
	}
	
	/**
	 * Checks whether the passed checksum is a valid SHA-256 checksum of the file
	 * @param fis the input stream of the file to check
	 * @param checksum the SHA-256 string to compare to
	 * @return true if the two checksums are equal, false otherwise.
	 */
	public static boolean checkSha256(final FileInputStream fis, final String checksum)
	{
		final String newChecksum = getSha256(fis);
		if (Logger.isDebug) Log.w(Logger.TAG, "Checksum1: " + newChecksum + "\nChecksum2: " + checksum);
		if (newChecksum == null || checksum == null)
			return false;
		return checksum.equalsIgnoreCase(newChecksum);
	}

	public static String getCdnImage(String baseUrl, int mViewWidth, int mViewHeight) {
		if (baseUrl == null) {
			Log.w("CDN URL", "is null");
			return baseUrl;
		}
		if (baseUrl.contains("s3.amazonaws.com")) {
			final StringBuilder sb = new StringBuilder("http://imageservice-a.mikandicdn.com");
			boolean succeeded = false;
			try {
				URL original = new URL(baseUrl);
				String stem = original.getHost().replace("s3.amazonaws.com", "");
				if (stem.length() > 0 && stem.charAt(stem.length() - 1) == '.') stem = stem.substring(0, stem.length() - 1);
				if (stem.length() > 0) {
					sb.append('/').append(stem); 
				}
				sb.append(original.getPath());
				char sep = '?';
				if (original.getQuery() != null) {
					sb.append(sep).append(original.getQuery());
					sep = '&';
				}
				sb.append(sep).append("w=").append(mViewWidth);
				sep = '&';
				sb.append(sep).append("h=").append(mViewHeight);
				succeeded = true;
			} catch (MalformedURLException e) {}
			//if (succeeded) Log.i("CDN URL", sb.toString());
			//else Log.i("CDN URL", "Failed: " + baseUrl);
			return succeeded ? sb.toString() : baseUrl;
		}
		if (baseUrl.contains("d28dazj2ply6nc.cloudfront.net")) {
			final StringBuilder sb = new StringBuilder("http://imageservice-a.mikandicdn.com/mikandicatalog");
			boolean succeeded = false;
			try {
				URL original = new URL(baseUrl);
				sb.append(original.getPath());
				char sep = '?';
				if (original.getQuery() != null) {
					sb.append(sep).append(original.getQuery());
					sep = '&';
				}
				sb.append(sep).append("w=").append(mViewWidth);
				sep = '&';
				sb.append(sep).append("h=").append(mViewHeight);
				succeeded = true;
			} catch (MalformedURLException e) {}
			//if (succeeded) Log.i("CDN URL", sb.toString());
			//else Log.i("CDN URL", "Failed: " + baseUrl);
			return succeeded ? sb.toString() : baseUrl;
		}
		Log.i("CDN URL", "Failed: " + baseUrl);
		return baseUrl;
	}
}
