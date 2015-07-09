package tools.mikandi.dev.utils;

import java.util.HashMap;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import tools.mikandi.dev.ads.OnFullScreenAdDisplayedListener;
import tools.mikandi.dev.library.KandiLibs;
import tools.mikandi.dev.login.LibraryLoginResult;
import tools.mikandi.dev.login.LoginStorageUtils;

/**
 * this is a UserInfoObject, it is instantiate by passing in the HashMap
 * container of the two app id and app secret key aparameters pulled from the
 * androidManifest file. The object is fully completed, once the loginResult has
 * been passed into the the object from the device. This can either be passed
 * into the object upon the completion of a sucessful login activity or by
 * pulling the result from the stored login result.
 */
public class UserInfoObject {


	public static HashMap<String, String> mHashMap = null;

	private static UserInfoObject instance;
	private static boolean isDebug = true;
	private static Boolean hasInstance = false;
	private static boolean hasLogin = false;
	private static Context myContext = null;
	
	private OnFullScreenAdDisplayedListener adListener = null;
	
	public UserInfoObject() {
	}
	
	public void setFullScreenAdListener(OnFullScreenAdDisplayedListener a) { 
		this.adListener = a;
	}
	
	public OnFullScreenAdDisplayedListener getAdListener() { 
		return this.adListener;
	}
	

	public static UserInfoObject getInstance(final Context context) {

		synchronized (hasInstance) {
			// update content at each call
			updateContext(context);
			
			if (!hasInstance) {
				instance = new UserInfoObject();
				hasInstance = true;

				if (mHashMap == null) {
					try {	
						
						ApplicationInfo ai2 = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
						Object manifest_appid = (Object) ai2.metaData.get(KandiLibs.sAppId);
						Object manifest_secret = (Object) ai2.metaData.get(KandiLibs.sSecret);
						Object manifest_publisherid = (Object) ai2.metaData.get(KandiLibs.sPublisherId); 
						
						HashMap<String, String> hm = new HashMap<String, String>();
						if (manifest_publisherid != null) 	hm.put(KandiLibs.sPublisherId , manifest_publisherid.toString());
						if (manifest_appid != null) 		hm.put(KandiLibs.sAppId, manifest_appid.toString());
						if (manifest_secret != null) 		hm.put(KandiLibs.sSecret , manifest_secret.toString());
						setHashMap(hm);

					} catch (Exception E) {
						Log.e("Exception thrown in UserInfoInstantiator",
								"Printing Exception : " + E);
					}
				}
			}
		}
		return instance;
	}

	public boolean getLoginStatus() {	
		final boolean loginStatus = (LoginStorageUtils.getLogin(myContext) != null) ? true : false;
		return loginStatus;
	}

	public Context getContext() {
		return myContext;
	}

	private static void updateContext(Context c) { 
		myContext = c;
	}
	private static void setHashMap(HashMap<String, String> hm) {
		mHashMap = hm;
	}

	public void setLoginResult(LibraryLoginResult lr) {
		if (KandiLibs.debug) Log.i("should be setting login result", "about to print");
		LoginStorageUtils.setLogin(myContext, lr);
	}

	public LibraryLoginResult getLoginResult() { 
		return LoginStorageUtils.getLogin(myContext);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("printing UIO : ");
		final LibraryLoginResult tempLoginVar = getLoginResult();
		
		if (tempLoginVar == null) {
			sb.append("LoginResult is null");

		} else {
			sb.append("LoginResult is : ").append(tempLoginVar.toString());
		
		}
		sb.append("HashMap Vals - appid: ").append(mHashMap.get(KandiLibs.sAppId))
		.append("HashMap Vals - secretkey: ").append(mHashMap.get(KandiLibs.sSecret))
		.append("HashMap Vals - Publisher id:").append(mHashMap.get(KandiLibs.sPublisherId));
		return sb.toString();
	}

	public HashMap<String, String> getAppDetails() {
		return UserInfoObject.mHashMap;
	}

	/**
	 *  this may return null if user isn't logged in, i would check first 
	 */
	public String getUsername() { 	
		return LoginStorageUtils.getLogin(myContext).getUsername();
	}
	
	public String getAppId() {
		return fromManifest(KandiLibs.sAppId);
	}

	public String getSecretKey() {
		return fromManifest(KandiLibs.sSecret);
	}

	public String getPublisherId() { 
		return fromManifest(KandiLibs.sPublisherId);
	}
	
	private String fromManifest(String param) {
		return UserInfoObject.mHashMap.get(param);
	}
	
}
