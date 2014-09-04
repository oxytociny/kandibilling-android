package tools.mikandi.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.saguarodigital.returnable.defaultimpl.JSONResponse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import tools.mikandi.dev.inapp.AuthorizePurchaseReturnable;
import tools.mikandi.dev.inapp.OnAuthorizeInAppListener;
import tools.mikandi.dev.inapp.OnValidationListener;
import tools.mikandi.dev.inapp.ValidateUserReturnable;
import tools.mikandi.dev.inapp.onPurchaseHistoryListener;
import tools.mikandi.dev.inapp.onUserVerificationListener;
import tools.mikandi.dev.login.AAppReturnable;
import tools.mikandi.dev.login.LoginActivity;
import tools.mikandi.dev.login.LoginResult;
import tools.mikandi.dev.login.LoginStorageUtils;
import tools.mikandi.dev.passreset.DefaultJSONAsyncTask;
import tools.mikandi.dev.passreset.OnJSONResponseLoadedListener;
import tools.mikandi.dev.purchasehistory.ListPurchasesReturnable;
import tools.mikandi.dev.utils.UserInfoObject;
import tools.mikandi.dev.inapp.ValidatePurchaseReturnable;

/**
 * The main KandiLibs class. All API calls use a method from this class.
 * 
 * Feel free to go look about the source code, but it gets complicated quickly.
 * 
 * You <b> will regret </b> autoformatting this page! 
 * 
 */
public class KandiLibs extends Activity {

	public static final int REQUEST_LOGIN = 0x100;
	public static final int RESULT_LOGIN = 0x101;
	
	public static final int REQUEST_PASSRESET = 0x102;
	public static final int RESULT_PASSRESET = 0x103;

	public static final int REQUEST_REGISTRATION = 0x104;
	public static final int RESULT_REGISTRATION = 0x105;

	public static final int REQUEST_BUYGOLD = 0x106;
	public static final int RESULT_BUYGOLD = 0x106;

	public static final String ACTION_LOGIN = "login";
	public static final String ACTION_PASSRESET = "pass-reset";
	public static final String ACTION_REGISTER = "registration";
	public static final String ACTION_BUYGOLD = "buy-gold";

	public static final boolean debug = true;
	public static final boolean balanceDebug = true;

	private static onPurchaseHistoryListener sPurchaseHistory = null;
	private static OnValidationListener sValidate = null;
	private static onUserVerificationListener sOnUserVerification = null;
	private static OnAuthorizeInAppListener sAuthInAppListener = null;	
	public static final int GRABTOKENS_FROMDEVICE = 0x300;
	public static final int GRABTOKENS_FROMSERVER = 0x301;
	public static final boolean mikeLibsBoolean = true;
	public static final int RESULT_BAILEDLOGIN = 26; 
	public static boolean ownedBoolean = false;
	
	/***
	 * AUTO FORMAT AT OWN RISK!!!!!!!!!!!! 
	 */
	
	/**
	 * 
	 * Welcome to Kandilibs!
	 * This is the api/interface/intermediate between you (the developer) and
	 * the Mikandi apis. We have built in functionality, that allows you to log
	 * in users to Mikandi, and verify users while the user is using your app
	 * 
	 * Validating Mikandi user's allows developers to not worry too much about
	 * DRM, and people illegally using you're work, but allows you to focus on
	 * improvement and delievering high quality content. We hope to create an
	 * easy to use implementation of our billing service that allows you the
	 * developer to get the most out of our service, create the best content and
	 * make you the most money possible!
	 * 
	 * Dev Notes: - In-app buying gold, is currently not yet implemented. I have
	 * demonstrated an easy way to redirect the user to the buy gold activity
	 * within the mikandi client. This is currently a work in progress however
	 * and in later versions we hope to be able to support within-app buy gold.
	 *  
	 * We have extensive notes on the implementation, design and use of this
	 * library and that material can be found at:
	 * 
	 *  http://tools.mikandi.com/confluence/display/KANDI/KandiLibs+Documentation
	 * 
	 * and i (Mike, the library developer) can be reached at: kandilibs@mikandi.com
	 * 
	 *  I encourage you too reach out if you have any trouble and more importantly 
	 *  if you could recommend any improvements, or any features that as developers, would
	 *  help you out.
	 * 
	 */

	// --------------------------------------------------Buy Gold Activity-----------------------------------------------------------------------
	/**
	 * Starts the buy gold activity, opens in Mikandi, unless Mikandi store not present in which the user 
	 * will be prompted to open the buy gold page in a url 
	 * 
	 * @param act - Reference to the activity in which this has been called. 
	 */
	public static final void requestBuyGold(final Activity act) {
		String url = "https://mikandi.com/buygold" ;
		
		Intent mIntent = new Intent(Intent.ACTION_VIEW);
		mIntent.setData(Uri.parse(url));
		act.startActivity(mIntent);
	
	}
	// -------------------------------------------------------- Buy Gold Activity End --------------------------------------------------------------

	// ------------------------------------------------------Login Activity----------------------------------------------------------------------------------
	/**
	 * this is the function that is to be started that starts the login activity, no call back is needed as there is a within this 
	 * that 
	 * @param act
	 * @param uio
	 * @param loginListener
	 */
	public static final void requestLogin(final Activity act, UserInfoObject uio) {
		Log.i("DEBUGGING XML ERROR: " , "Request Login in library");
		
		String mSecret = uio.fromAppDetails("secretkey");
		String mAppId = uio.fromAppDetails("appid");

		Log.i("DEBUGGING XML ERROR: " , "Secret key  " + mSecret  + " and appid " + mAppId);
		
		Intent mIntent = new Intent(act, LoginActivity.class);
		Log.i("DEBUGGING XML ERROR: " , "Login Intent created");
		
		mIntent.putExtra("secretkey", mSecret);
		mIntent.putExtra("appid", mAppId);
		Log.i("DEBUGGING XML ERROR: " , "starting login activity");
		
		act.startActivity(mIntent);
	
	}
	// ---------------------------------------------- Login Activity End----------------------------------------------------------------------------

	//----------------------------------------- Logout --------------------------------------------------------------------------------------
	/** Function call to log user out of Mikandi and wipe the loginresults 
	 *  from the UserInfoObject.
	 *  @param uio  
	 */
	public static final void logOutUser(UserInfoObject uio) { 
			Context mContext = uio.getContext();
			LoginStorageUtils.clear(mContext);
			uio.setLoginResult(null);
		}
	// ---------------------------------------------- End Log out user ----------------------------------------------------------------------
	public static final boolean isLoggedIn(UserInfoObject uio) { 
		Context mContext = uio.getContext();
		boolean isLoggedIn = LoginStorageUtils.isLoggedIn(mContext);
		return isLoggedIn;
	}
	// ----------------------------------------------Validate purchase-----------------------------------------------------------------------------
	/**
	 * This function is to be called after the AuthInAppPurchase call, as an additional 
	 * validation, this provides a positive response if the purchase has been validated 
	 * on the servers and inform the developer that the purchase was completed properly. 
	 * 
	 * @param uio - UserInfoObject
	 * @param validateListener - Interface for call back s
	 * @param mToken	 - String purchaseId
	 * @param mDescription - A string description of the app (THESE SHOULD NEVER CHANGE) 
	 * @param mAmount - A String representation of the amount of Mikandi Gold
	 */
	public static void requestValidateInApp(UserInfoObject uio,
			final OnValidationListener validateListener, String mToken) {
		
		sValidate = validateListener;
		LoginResult lr = uio.getLoginResult(); 
		Context context = uio.getContext(); 
		HashMap<String, String> appDetails = uio.getAppDetails();
		
		if (lr == null) { 
			if (debug) Log.e("Request Validate In App ", "login Result is null!");
			return; 
		}
		try {
		
			// need to start the task here
			int userId = lr.getUserId();
			final String userAuth = lr.getUserAuthHash();
			final String userAuthExpires = lr.getUserAuthExpires();
			final String appId = appDetails.get("appid");
			if (debug) Log.i("appid is ", ""+ appId);
			final String secretKey = appDetails.get("secretkey");
			if (debug) Log.i("secret key is ", "" + secretKey);
			
			HashMap<String, String> args = new HashMap<String, String>();
			args.put(AAppReturnable.APP_ID, appId);
			args.put(AAppReturnable.APP_SECRET, secretKey);
			args.put(AAppReturnable.USER_ID, String.valueOf(userId));
			args.put(AAppReturnable.AUTH_HASH, userAuth);
			args.put(AAppReturnable.TOKEN, mToken);
			args.put(AAppReturnable.AUTH_EXPIRES, userAuthExpires);
			// Start task here, pass in context , hashmap
			
			new DefaultJSONAsyncTask<ValidatePurchaseReturnable>(
					ValidatePurchaseReturnable.class,
					context,
					new OnJSONResponseLoadedListener<ValidatePurchaseReturnable>() {
						@Override
						public void onJSONLoaded(
								JSONResponse<ValidatePurchaseReturnable> jsonResponse) {
							if (debug) Log.i("Validate response is : " , "" + jsonResponse.toString());
							
							ValidatePurchaseReturnable mVPR = jsonResponse.getOne();
							
							validateResponseHandler(jsonResponse);
							if (debug) Log.i("Returned ValidatePurchaseReturnable", 
									"responese : " + mVPR.toString());
						}
					}, args).execute();
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	protected static void validateResponseHandler(
			JSONResponse<ValidatePurchaseReturnable> jsonResponse) {

		JSONResponse<ValidatePurchaseReturnable> myResponse = jsonResponse;
		
	String resp = myResponse.toString(); 
		if (debug) Log.e("VALIDATEPURCHASE RESPONSE IS", "" + resp);
		if (myResponse.getCode() != 200) {
			if (debug) Log.e("Kandilibs/Networking error", myResponse.getCode() + " returned");
			sValidate.FailedValidation();
		} else {
			if (debug) Log.i("Validated Response : ","" + jsonResponse.getCode());
			sValidate.PositiveValidation();
		}
	}
	// ------------------------------------------Validate PurchaseEnd--------------------------------------------------------------------------------
	// ------------------------------------- Auth in app purchase Authorization -------------------------------- ---------------------------------------
	/**
	 * AuthInAppPurchase is the function called by the developer to authorize a in-app 
	 * purchase of some content within the application. The AuthInAppPurchase function is normally 
	 * called before the ValidateInAppPurchase function. 
	 * 
	 * AuthInApp takes in the UserInfoObject, and 3 string params and runs idempotent, so 
	 * repeated requests don't nessesarily mean repeated transaction.  
	 * 
	 * mToken is the purchase ID, constant across your app, each different available transaction 
	 * will have a different purchaseId. 
	 * 
	 * mDescription is a cleartext description of the transaction. Some examples would be : 
	 * 	ex1: "content pack - Asian" 
	 *  ex2: "Mikandi - Fun pack 1" 
	 *  ex3: "in-app purchase 2: Unlock membership"
	 * 
	 * mAmount is the amount of Mikandi Gold that the transaction will cost the user.
	 * this amount has to have been entered into the developer panel so our services
	 * can authorize payment accordingly this should just be placed into the function as a
	 * String 
	 * 
	 * @param uio - UserInfo Object 
	 * @param mToken - purchase ID 
	 * @param mDescription - String description of purchase
	 * @param mAmount - amount of Gold (as a string)
	 * @param authInAppListener - interface used as way of implementing callbacks
	 */
	public static void AuthInAppPuchase(UserInfoObject uio ,String mToken, String mDescription, String mAmount, 
			 OnAuthorizeInAppListener authInAppListener){
		sAuthInAppListener = authInAppListener;
		Context context = uio.getContext();
		LoginResult lr = uio.getLoginResult();
		HashMap<String,String> appDetails = uio.getAppDetails();
		
		if (lr == null) {
			if (debug) Log.e("authInAppPurchase " , "error! login result is null");
			Log.e("In-App purchase Error" , "User not logged in ! ");
			return;
			}
	try {
		
		// need to start the task here
		final int userId = lr.getUserId();
		final String userAuth = lr.getUserAuthHash();
		final String userAuthExpires = lr.getUserAuthExpires();
		final String appId = appDetails.get("appid");
		final String secretKey =  appDetails.get("secretkey");

		HashMap<String, String> args = new HashMap<String, String>();
		args.put(AAppReturnable.APP_ID, appId);
		args.put(AAppReturnable.APP_SECRET, secretKey);
		args.put(AAppReturnable.USER_ID, String.valueOf(userId));
		args.put(AAppReturnable.AUTH_HASH, userAuth);
		args.put(AAppReturnable.AUTH_EXPIRES, userAuthExpires);
		args.put(AAppReturnable.DESCRIPTION	,mDescription.trim().toLowerCase(Locale.getDefault()));
		args.put(AAppReturnable.TOKEN, mToken.trim().toLowerCase(Locale.getDefault()));
		args.put(AAppReturnable.AMOUNT, mAmount.trim().toLowerCase(Locale.getDefault()));
		
		new DefaultJSONAsyncTask<AuthorizePurchaseReturnable>(
			AuthorizePurchaseReturnable.class,
			context,
			new OnJSONResponseLoadedListener<AuthorizePurchaseReturnable>() {
				@Override
				public void onJSONLoaded(
					JSONResponse<AuthorizePurchaseReturnable> jsonResponse) {
								
					if (jsonResponse != null) {
					
					int code = jsonResponse.getCode();
					inappAuthorizationHandler(code);
					
						} else {
					if (debug) Log.e("Authorizing in app", "json is null");
						return;
									}
								}
							}, args).execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	/**
	 *  This is a callback handler for the AuthInApp function
	 *  @param code - this is the return code
	 */
	protected static void inappAuthorizationHandler(int code) {
		if (code == 200) { 
			sAuthInAppListener.Sucess();
		} else{ 
			sAuthInAppListener.Failed(code);
		}
	}
	// ------------------------------------ ENd in app Purchase Authorization ------------------------------------------------------

	// ------------------------------------------ Purchase History--------------------------------------------------------------------------------------
	/**
	 * This is a function that returns a boolean value representing wheather or not the given token has been purchased by the current 
	 * user. This is the easier and quickest way to check to see if a user has purchased user content! 
	 * 
	 * @param uio	= UserInfoObject, an instance should be collected previously and passed into this function! 
	 * @param token = String Id of the purchase
	 * @return
	 */
	public static boolean checkPurchase(final UserInfoObject uio, final String token){
		LoginResult lr = uio.getLoginResult();
		setOwned(false);
		if (lr != null){
		if (lr.getArrayListTokens().contains(token)){
				setOwned(true);
				return getOwned();
		}
		else {
			if (debug) Log.i("checking token ", "none from lr");
			}
		}
		
		requestPurchaseHistory(uio, new onPurchaseHistoryListener() {
			
			@Override
			public void onSucessfulHistoryRetrieved(List<String> mTokens) {
				setOwned(mTokens.contains(token));		
			}
			
			@Override
			public void onFailedHistoryRetrieved() {
				setOwned(false);
				if (debug) Log.i("token check " , "failed to retreive tokens list");
			}
		});
		boolean test = getOwned();
		return test;
	}
	
	/**
	 * This returns the purchase History of the User, this allows the developer
	 * to determine what in-app purchases or access to premium content the user
	 * has purchased.
	 * 
	 * There are two ways this can be used the first is that by modifying the
	 * which_action variable, one use of this function is to retreive tokens
	 * from the stored login result on the device, one use of function is to
	 * retreive the list from the server.
	 * 
	 * I would recommend that this from device is done on start up of the app,
	 * or even near the beging of the app life cycle. By doing this, you can
	 * determine if the user has purchased all you're premium content you offer,
	 * and if so, just not have the app-check in with the server at all.
	 * 
	 * 
	 * @param UserInfoObject uio - Stores all nessesary information needed for API
	 * @param purchaseHistoryListener - Interface for overall function
	 */
	public static void requestPurchaseHistory(final UserInfoObject uio ,final onPurchaseHistoryListener purchaseHistoryListener) {
		
		if (debug) Log.i("KandiLibs - RequestPurchaseHistory","method called");
		final Context context = uio.getContext();
		LoginResult lr = uio.getLoginResult();
		HashMap<String,String> appDetails = uio.getAppDetails();
		sPurchaseHistory = purchaseHistoryListener;
		
		if (lr == null) {
			if (debug) Log.e("RequestPurchase History " , "error! login result is null");
			Toast.makeText(context, "Request purchase History", Toast.LENGTH_LONG).show();
			return;
		}
		else {
			if (debug) Log.i("KandiLibs - RequestPurchaseHistory","Instantiating variables ");
				final int userId = lr.getUserId();
				final String userAuth = lr.getUserAuthHash();
				final String userAuthExpires = lr.getUserAuthExpires();
				final String appId = appDetails.get("appid");
				final String secretKey = appDetails.get("secretkey");

				HashMap<String, String> args = new HashMap<String, String>();
				args.put(AAppReturnable.APP_ID, appId);
				args.put(AAppReturnable.APP_SECRET, secretKey);
				args.put(AAppReturnable.USER_ID, String.valueOf(userId));
				args.put(AAppReturnable.AUTH_HASH, userAuth);
				args.put(AAppReturnable.AUTH_EXPIRES, userAuthExpires);
				
		try {
			new DefaultJSONAsyncTask<ListPurchasesReturnable>(
			ListPurchasesReturnable.class,
			context,
	new OnJSONResponseLoadedListener<ListPurchasesReturnable>() {

		@Override
		public void onJSONLoaded(
			JSONResponse<ListPurchasesReturnable> jsonResponse) {
			ArrayList<String> mTokens = null;
									
			if (jsonResponse != null) {
			if (debug) Log.i("Server response from get list" , "" + jsonResponse.getCode());
		
			ListPurchasesReturnable mList = (ListPurchasesReturnable) jsonResponse.getOne();
			mTokens = mList.getArrayListTokens();
			LoginResult lr = LoginStorageUtils.getLogin(uio.getContext());
			lr.setArrayListTokens(mTokens);
			LoginStorageUtils.setLogin(uio.getContext(), lr);
			purchaseHistoryHandler(mTokens);
			
			} else {
				if (debug) Log.e("ListPurchaseTask", "json is null");
			}
		}
	}, args).execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	/**
	 * This is the call back handler for the RequestPurchaseHistory application 
	 * tokens passed through to the on sucessful callback, void onSucessful included
	 * 
	 * @param mTokens
	 */
	protected static void purchaseHistoryHandler(List<String> mTokens) {
		if (mTokens != null) {
			if (debug) Log.i("printing out tokens" , "tokens :" + mTokens.toString());
			sPurchaseHistory.onSucessfulHistoryRetrieved(mTokens);
		} else {
			sPurchaseHistory.onFailedHistoryRetrieved();
		}
	}
	// --------------------------------------- End Purchase history--------------------------------------------------------------------------------------

	// ------------------------------------------------------ Verify User ---------------------------------------------------
	/**
	 * devDetails is a hashmap containing information passed from the app to the
	 * library (this class) that holds app specific information such as the
	 * appid and the app secret key. This information is pulled from the
	 * manifest file.
	 * 
	 * @param ctx
	 * @param devDetails
	 */
	public static void requestUserVerify(UserInfoObject uio, onUserVerificationListener userValidListener) {
			sOnUserVerification = userValidListener;
		
		try {
			if (uio.getLoginResult() == null)
			return; 
		} catch (Exception E) { 
			if (debug) Log.e("error thrown in retreiving userverify , when not logged in" , "printing error: " + E);
		}
			
		Context ctx = uio.getContext();
		LoginResult lr = uio.getLoginResult();
		HashMap<String,String> devDetails = uio.getAppDetails();
		
		if (debug) Log.i("Printing new lr " , "" + lr.toString());
		if (debug) Log.i("Printing new devDetails" , "" + devDetails.toString());
		if (debug) Log.i("Printing LoginResult", "" + lr);
		
		final int mUserId = lr.getUserId();
		final String mAuthHash = lr.getUserAuthHash();
		final String mAppId = devDetails.get("appid");
		final String mSecretKey = devDetails.get("secretkey");
		final HashMap<String, String> hm = new HashMap<String, String>();
		
		hm.put(AAppReturnable.USER_ID, "" + mUserId);
		hm.put(AAppReturnable.APP_ID, mAppId);
		hm.put(AAppReturnable.AUTH_HASH, mAuthHash);
		hm.put(AAppReturnable.APP_SECRET, mSecretKey);

		try {
			new DefaultJSONAsyncTask<ValidateUserReturnable>(
					ValidateUserReturnable.class, ctx,
					new OnJSONResponseLoadedListener<ValidateUserReturnable>() {

						public void onJSONLoaded(
								JSONResponse<ValidateUserReturnable> jsonResponse) {
							try {
								if (jsonResponse != null) { 
								ValidateUserReturnable mResponse = (ValidateUserReturnable) jsonResponse
										.getOne();
								if (debug) Log.e("Code from request USer verify : " , "" + jsonResponse.getCode());
								if (mResponse.isValidated()) {
									isValid(true, jsonResponse.getCode());
								} else {
									isValid(false, jsonResponse.getCode());
								}

								}
								else { 
									if (debug) Log.e("ValidateUserReturnable", "jsonResponse is null");
									}} catch (Exception E) {
								
			if (debug) Log.e("error thrown in request User verification",
					"Error is : "+ E);
							}
						}
					}, hm).execute();

		} catch (Exception E) {
			if (debug) Log.i("Error thrown in request userVerfication",
					"creating the task constructor");
		}
	}
	/**
	 * isValid is the callback handler for the RequestUserVerify function the isValid 
	 * method is called regardless of the result of the RequestUserVerify
	 * 
	 * @param sucessfulVerification this is a boolean value that determines which interface callback 
	 * is used 
	 * @param code this is the request Return code that is returned along with the failed 
	 * 	 */
	protected static void isValid(boolean sucessfulVerification, int code) {
		if (sucessfulVerification) {
			sOnUserVerification.userVerifiedSuccessfully();
		} else {
			sOnUserVerification.userVerifyFailed(code);
		}
	}
	// ------------------------------------------------------ End Verify User--------------------------------------------------------

	// -------------------------------------------------- Random functions -----------------------------------------------------------------
	private static void setOwned(boolean var) { 
		ownedBoolean = var;
	}
	public static  boolean getOwned() { 
		return ownedBoolean;
	}
	// -------------------------------------------------End Random Functions ---------------------------		
}