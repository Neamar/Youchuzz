package fr.youchuzz.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;

import fr.youchuzz.BaseActivity;

/**
 * Communication avec l'API Youchuzz
 * @author neamar
 *
 */
public class API {
	private static API api = null;
	
	/**
	 * Init new APIs.
	 * @param ac base activity
	 */
	public static void init(Activity ac)
	{
		if(API.api != null)
			Log.w("youchuzz", "API constructor called twice.");
		
		API.api = new API(ac);
	}
	
	/**
	 * Update activity to be able to manipulate view ids.
	 * @param ac
	 */
	public static void updateActivity(Activity ac)
	{
		api.aq = new AQuery(ac);
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
	public void login(String facebookToken, BaseActivity handler, String callback)
	{
		String url = baseUrl + "/user/login_fb?fb_token=" + facebookToken;
		
		aq.ajax(url, JSONObject.class, APIWrapper.createForObject(handler, callback));
	}
	
	/**
	 * List all chuzz for selected user
	 * 
	 * @param handler
	 * @param callback as requested by AQuery
	 */
	public void getChuzzs(BaseActivity handler, String callback)
	{
		String url = buildUrl("/user/chuzzs", "");

		aq.ajax(url, JSONArray.class, -1, APIWrapper.createForArray(handler, callback));
	}
	
	/**
	 * List all friends for selected user
	 * 
	 * @param handler
	 * @param callback as requested by AQuery
	 */
	public void getFriends(BaseActivity handler, String callback)
	{
		String url = buildUrl("/user/friends", "");

		aq.ajax(url, JSONArray.class, APIWrapper.createForArray(handler, callback));
	}
	
	/**
	 * Upload new content to be used with future chuzz.
	 * 
	 * @param handler
	 * @param callback as requested by AQuery
	 * @param content file to be uploaded
	 * @param content_number will this be content 1 or content 2 ?
	 */
	public void uploadContent(BaseActivity handler, String callback, File content, int content_number)
	{
		String url = buildUrl("/chuzz/add_content", "content=" + content_number);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("content", content);
		params.put("filename", content.getName());

		aq.ajax(url, params, JSONObject.class, APIWrapper.createForObject(handler, callback));
	}
	
	/**
	 * Create new chuzz
	 * 
	 * @param handler
	 * @param callback as requested by AQuery
	 * @param title title for the chuzz
	 * @param contents id-list for the contents, as returned by uploadContent
	 * @param friends id-list for the friends, as returned by getFriends
	 */
	public void createChuzz(BaseActivity handler, String callback, String title, ArrayList<String> contents, ArrayList<Integer> friends)
	{
		String url = buildUrl("/chuzz/create", "");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("title", title);
		params.put("contents", contents);
		params.put("friends", friends);

		aq.ajax(url, params, JSONObject.class, APIWrapper.createForObject(handler, callback));
	}
	
	/**
	 * List all friends for selected user
	 * 
	 * @param handler
	 * @param callback as requested by AQuery
	 */
	public void getChuzz(BaseActivity handler, String callback, int chuzzId)
	{
		String url = buildUrl("/chuzz/vote", "");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("chuzz_id", chuzzId);
		
		aq.ajax(url, params, JSONObject.class, APIWrapper.createForObject(handler, callback));
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

		return baseUrl + url + "?id_session=" + sessionId + getParams;
	}
	
	

}

/**
 * This class kinda acts like a closure around the defined callback function.
 * When getting back API results, we use an APIWrapper as a middleware between the API and the function.
 * This allos us to do some logging, and to check for errors.
 * 
 * @author neamar
 *
 * @param <T> Either JSONArray or JSONObject. Use static helper.
 */
class APIWrapper<T> extends AjaxCallback<T>
{
	private Class<T> type;
	private BaseActivity handler;
	private String callback;
	
	public static APIWrapper<JSONArray> createForArray(BaseActivity handler, String callback)
	{
		APIWrapper<JSONArray> wrapper = new APIWrapper<JSONArray>();
		wrapper.setHandler(JSONArray.class, handler, callback);
		
		return wrapper;
	}
	
	public static APIWrapper<JSONObject> createForObject(BaseActivity handler, String callback)
	{
		APIWrapper<JSONObject> wrapper = new APIWrapper<JSONObject>();
		wrapper.setHandler(JSONObject.class, handler, callback);
		
		return wrapper;
	}
	
	public void setHandler(Class<T> type, BaseActivity handler, String callback)
	{
		this.type = type;
		this.handler = handler;
		this.callback = callback;
	}
	
	public void callback(String url, T json, AjaxStatus status) {
		Log.v("yc_request", url);
		
		//Check for network error (or API failures)
		if(json != null)
			Log.v("yc_result", json.toString());
		else
		{
			Log.e("yc_result", "Network/api error : err. " + status.getCode());
			handler.error(status.getMessage());
			status.invalidate();
		}
		
		//Check for errors while using API
		//Uses "error" key.
		if(json instanceof JSONObject)
		{
			JSONObject ob = (JSONObject) json;
			
			try
			{
				//Will throw exception if key does not exist.
				String error = ob.getString("error");
				handler.error(error);
				Log.e("yc_result", "Error: " + error + ". Handler: " + handler.toString() + ". Callback: " + callback);
				json = null;
				status.invalidate();
			} catch (JSONException e) { }
		}
		
		Class<?>[] AJAX_SIG = {String.class, type, AjaxStatus.class};
		Class<?>[] DEFAULT_SIG = {String.class, Object.class, AjaxStatus.class};
		
		AQUtility.invokeHandler(handler, callback, true, false, AJAX_SIG, DEFAULT_SIG, url, json, status);
	}
}