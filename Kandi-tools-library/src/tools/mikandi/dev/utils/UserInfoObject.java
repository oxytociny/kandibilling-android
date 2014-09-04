package tools.mikandi.dev.utils;

import java.util.HashMap;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import tools.mikandi.dev.login.LoginResult;
import tools.mikandi.dev.login.LoginStorageUtils;

/**
 * this is a UserInfoObject, it is instantiate by passing in the HashMap container of the two
 * app id and app secret key aparameters pulled from the androidManifest file. 
 * The object is fully completed, once the loginResult has been passed into the the object
 * from the device. This can either be passed into the object upon 
 * the completion of a sucessful login activity or by pulling the result from 
 * the stored login result. 
 */
public class UserInfoObject {


	public static LoginResult myLogin = null;
	
	public static HashMap<String, String> myHashMap = null;
	
	private static UserInfoObject instance;
	private static boolean isDebug = true;
	private static Boolean hasInstance = false;
	private static boolean hasLogin = false;
	private static Context myContext = null;
	
	public UserInfoObject() {}
	
	public static UserInfoObject getInstance(final Context context) {
		
		synchronized (hasInstance) {
			myContext = context;
				if (!hasInstance) {
					instance = new UserInfoObject();
					hasInstance = true;
		
				if (myLogin == null && LoginStorageUtils.getLogin(context) != null) {
					myLogin = LoginStorageUtils.getLogin(context);
					if (isDebug) Log.i("get instance() ", "getting retreived login from LSU");
					setLogin(true);
				}
				
				if (getLoginStatus()) {
					myLogin = LoginStorageUtils.getLogin(context);
				}
				
				if (myHashMap == null){
					try {
						ApplicationInfo ai2 = context.getPackageManager().getApplicationInfo(context.getPackageName(),
								PackageManager.GET_META_DATA);
						Object manifest_appid = (Object) ai2.metaData.get("appid");
						Object manifest_secret = (Object) ai2.metaData.get("secretkey");

						HashMap<String, String> hm = new HashMap<String, String>();
						hm.put("appid", manifest_appid.toString());
						hm.put("secretkey", manifest_secret.toString());
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
			
	private static void setLogin(boolean b) {
		hasLogin = b;
	}

	private static boolean getLoginStatus(){
		return hasLogin;
	}
	
	public Context getContext() { 
		return myContext;
	}
	
	private static void setHashMap(HashMap<String,String> hm){ 
		myHashMap = hm;
	}
	
	public void setLoginResult(LoginResult lr) { 
		if (isDebug) Log.i("should be setting login result", "about to print");
		myLogin = lr;
	}

	public String toString() { 
		StringBuilder sb = new StringBuilder("printing UIO : ");
		
		if (myLogin == null) {
		sb.append("LoginResult is null");
		} else {
		sb.append("LoginResult is : ").append(myLogin.toString());
			}
		sb.append("HashMap Vals - appid: ").append( myHashMap.get("appid"));
		sb.append("HashMap Vals - secretkey: ").append( myHashMap.get("secretkey"));
		return sb.toString();
	}
	
	public LoginResult getLoginResult() {
		if (UserInfoObject.myLogin != null) { 
			return UserInfoObject.myLogin;
		}
	else return null;
	}
	
	public HashMap<String, String> getAppDetails() {
		return UserInfoObject.myHashMap;
	}
	 
	public String fromAppDetails(String param) { 
		return UserInfoObject.myHashMap.get(param.toString());
	}
}
