package fr.youchuzz.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

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
	
	/**
	 * Session id, to be used with each API call (with id_session parameter)
	 */
	public String sessionId = "";

	
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
	
	
	/**
	 * Get back session id for youchuzz.
	 * 
	 * @param facebookToken
	 * @param handler
	 * @param callback as requested by AQuery
	 */
	public void login(String facebookToken, Object handler, String callback)
	{
		String url = baseUrl + "/user/login_fb?fb_token=" + facebookToken;

		aq.ajax(url, JSONObject.class, handler, callback);
	}
	
	/**
	 * List all chuzz for selected user
	 * 
	 * @param handler
	 * @param callback as requested by AQuery
	 */
	public void getChuzzs(Object handler, String callback)
	{
		String url = buildUrl("/user/chuzzs", "");

		aq.ajax(url, JSONArray.class, handler, callback);
	}
	
	/**
	 * List all friends for selected user
	 * 
	 * @param handler
	 * @param callback as requested by AQuery
	 */
	public void getFriends(Object handler, String callback)
	{
		String url = buildUrl("/user/friends", "");

		aq.ajax(url, JSONArray.class, handler, callback);
	}
	
	
	public void uploadContent(Object handler, String callback, File content)
	{
		String url = buildUrl("/chuzz/add_content", "");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("content", content);

		aq.ajax(url, params, JSONObject.class, handler, callback);
	}
	
	/**
	 * Build an API-URL
	 * @param url the url to be used, e.g. "/user/chuzzs"
	 * @param getParams params urlencoded, e.g. foo=bar&bar=foo
	 * @return URL
	 */
	protected String buildUrl(String url, String getParams)
	{
		if(getParams.length() > 0)
			getParams = "&" + getParams;
		
		Log.i("yc", "Retrieving " + baseUrl + url + "?id_session=" + sessionId + getParams);
		return baseUrl + url + "?id_session=" + sessionId + "&" + getParams;
	}
}
