package fr.youchuzz;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import fr.youchuzz.core.API;

public class LoginActivity extends BaseActivity {
	private SharedPreferences mPrefs;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.login_title);
		
		//Initialize API object for all future access.
		API.init(this);
		
		setContentView(R.layout.activity_login);
		
		aq = new AQuery(this);
		aq.id(R.id.login_step2).invisible();
		aq.id(R.id.login_facebook).clicked(this, "onFacebookButtonClicked");
		
		/*
		 * Get existing access_token if any
		 */
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if(access_token != null) {
			facebook.setAccessToken(access_token);
			Log.i("yc", "Trying to reuse existing FB token...");
		}
		if(expires != 0) {
			facebook.setAccessExpires(expires);
		}
		
		if(facebook.isSessionValid())
		{
			Log.i("yc", "Reusing existing FB token.");
			onFacebookLogged(facebook.getAccessToken());
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		facebook.authorizeCallback(requestCode, resultCode, data);
	}
	
	
	/**
	 * Click on facebook login : lauch Facebook auth
	 * @param v
	 */
	public void onFacebookButtonClicked(View v)
	{
		Toast.makeText(getBaseContext(), "Logging in...", Toast.LENGTH_LONG).show();
		//Avoid double click while loading
		aq.id(R.id.login_facebook).invisible();
		
		Log.i("yc", "Waiting for FB authorization.");
		facebook.authorize(this, new String[] {}, Facebook.FORCE_DIALOG_AUTH, new DialogListener() {
			@Override
			public void onComplete(Bundle values) {
				//Register token for future usage
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("access_token", facebook.getAccessToken());
				editor.putLong("access_expires", facebook.getAccessExpires());
				editor.commit();

				// Log into youchuzz
				onFacebookLogged(facebook.getAccessToken());
			}
			
			@Override
			public void onFacebookError(FacebookError error) {
				Toast.makeText(getBaseContext(), error.getErrorType(), Toast.LENGTH_LONG).show();
				aq.id(R.id.login_facebook).visible();
			}
			
			@Override
			public void onError(DialogError e) {
				Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				aq.id(R.id.login_facebook).visible();
			}
			
			@Override
			public void onCancel() {
				Toast.makeText(getBaseContext(), R.string.login_facebook_clicked_cancel, Toast.LENGTH_LONG).show();
				aq.id(R.id.login_facebook).visible();
			}
		});
	}
	
	/**
	 * Called once successfully logged onto Facebook : now logging on Youchuzz
	 * using Facebook token
	 * 
	 * @param facebookToken
	 */
	public void onFacebookLogged(String facebookToken)
	{
		load();
		Log.i("yc", "Logged onto FB.");
		aq.id(R.id.login_facebook).invisible();
		aq.id(R.id.login_step2).visible();
		
		API.getInstance().login(facebook.getAccessToken(), this, "youchuzzLogged");
		
		//TODO: display spinner
	}
	
	/**
	 * Called once successfully logged onto Youchuzz
	 * Register session_id and open HomeActivity
	 * 
	 * @param url
	 * @param json
	 * @param status
	 */
	public void youchuzzLogged(String url, JSONObject json, AjaxStatus status) {
		
		//Check for errors
		if(json == null)
		{
			//Remove all tokens, in case it has been corrupted.
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putString("access_token", "(old)");
			editor.putLong("access_expires", -1);
			editor.commit();
			
			finish();
		}
		else
		{
			endLoad();
			
			//Remember session_id for all application life
			API.getInstance().setSessionId(getString(json, "id_session"));
			
			Log.i("yc", "Logged into Youchuzz, session_id=" + getString(json, "id_session"));

			//Start Home Activity
			Intent myIntent = new Intent(this, HomeActivity.class);
			startActivity(myIntent);
		}
	}
	
	public void onChuzzCreated(String url, JSONObject json, AjaxStatus status)
	{
		//Check for errors
		if(json == null)
		{
			error("Error while posting chuzz : err. " + status.getCode());
		}
		else
		{
			Log.i("yc", "New chuzz created with id " + getString(json, "chuzz_id"));
			
			Toast.makeText(getApplicationContext(), "CE CHUZZ A ÉTÉ CRÉÉ !", Toast.LENGTH_LONG);
			Intent myIntent = new Intent(this, HomeActivity.class);
			startActivity(myIntent);
		}
	}
}