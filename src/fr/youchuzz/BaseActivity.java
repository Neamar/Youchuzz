package fr.youchuzz;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.androidquery.AQuery;

public class BaseActivity extends Activity {
	protected AQuery aq;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public String getString(JSONObject json, String key)
	{
		try {
			return json.getString(key);
		} catch (JSONException e) {
			Log.e("yc", "Error reading JSON result. Aborting.");
		}
		
		return "";
	}
}