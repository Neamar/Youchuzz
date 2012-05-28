package fr.youchuzz;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.android.Facebook;

/**
 * BaseActivity offers a few helper functions :
 * - AQuery object
 * - error() primitive for toast and log
 * - getString() and getInt() for JSON access without Exception garbling
 * - modalLoad() / endModalLoad() to display spinner while loading.
 * 
 * Remove dalvikvm tag : "(?!dalvikvm\b)\b\w+"
 * @author neamar
 *
 */
public class BaseActivity extends Activity {
	protected Facebook facebook = new Facebook("297600333614254");
	protected AQuery aq;
	protected ProgressDialog progressDialog = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(facebook.isSessionValid())
			facebook.extendAccessTokenIfNeeded(this, null);
	}
	
	public String getString(JSONObject json, String key)
	{
		try {
			return json.getString(key);
		} catch (JSONException e) {
			Log.e("yc", "Error reading JSON string-key " + key + ". Aborting.");
		}
		
		return "";
	}
	
	public int getInt(JSONObject json, String key)
	{
		try {
			return json.getInt(key);
		} catch (JSONException e) {
			Log.e("yc", "Error reading JSON int-key " + key + ". Aborting.");
		}
		
		return -1;
	}
	
	/**
	 * Display a spinner while loading content.
	 */
	protected void modalLoad(String title, String msg)
	{
		try
		{
			progressDialog = ProgressDialog.show(BaseActivity.this, title, msg, true);
		} catch(Exception e)
		{}
	}
	
	/**
	 * Hide spinned displayed using modalLoad
	 */
	protected void endModalLoad()
	{
		try
		{
			if(progressDialog != null)
			{
				progressDialog.dismiss();
			}
		} catch(Exception e)
		{}
	}
	
	/**
	 * Called when json returns is null, indicating a problem in data transfer.
	 * @param msg msg to display as a toast and to log.
	 */
	public void error(String msg)
	{
		Log.e("yc", msg);
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
	}
}