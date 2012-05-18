package fr.youchuzz.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;

/**
 * Communication avec l'API Youchuzz
 * @author neamar
 *
 */
public class API {
	private static API api = null;
	
	public static void init(Activity ac)
	{
		if(API.api != null)
			Log.w("youchuzz", "API constructor called twice.");
		
		API.api = new API(ac);
	}
	
	public static API getInstance()
	{
		if(API.api == null)
			Log.e("youchuzz", "Calling API.getInstance before initializing with API.init(Activity).");
		
		return API.api;
	}







	/**
	 * Baseurl for all API calls
	 */
	private String baseUrl = "http://api.youchuzz.com";
	
	public String sessionId;
	
	/**
	 * Token to be used for next connexion.
	 */
	public String nextToken;
	
	/**
	 * AQuery object for api-calls.
	 * @see http://code.google.com/p/android-query/
	 */
	private AQuery aq;

	/**
	 * Create new api.
	 * Should be called only once -- not a real singleton pattern.
	 * 
	 * @param ac default activity
	 */
	private API(Activity ac)
	{
		aq = new AQuery(ac);
	}
	
	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
	
	public void setNextToken(String nextToken)
	{
		this.nextToken = nextToken;
		Log.e("yc", nextToken);
	}
	
	
	/**
	 * Get back session id for youchuzz.
	 * @param facebookToken
	 * @return youchuzzToken
	 */
	public void login(String facebookToken, Object handler, String callback)
	{
		String url = baseUrl + "/user/login_fb?fb_token=" + facebookToken;

		aq.ajax(url, JSONObject.class, handler, callback);
	}
}
