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
	private Facebook facebook = new Facebook("297600333614254");
	private SharedPreferences mPrefs;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
		}
		if(expires != 0) {
			facebook.setAccessExpires(expires);
		}
		
		if(facebook.isSessionValid())
		{
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
		//Avoid double click while loading
		aq.id(R.id.login_facebook).invisible();
		facebook.authorize(this, new DialogListener() {
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
			}
			
			@Override
			public void onError(DialogError e) {
				Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onCancel() {
				Toast.makeText(getBaseContext(), R.string.login_facebook_clicked_cancel, Toast.LENGTH_LONG).show();
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
			error("Error while logging in : err. " + status.getCode());

			//Remove all tokens, in case it has been corrupted.
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putString("access_token", "(old)");
			editor.putLong("access_expires", -1);
			editor.commit();
			
			finish();
		}
		else
		{
			//Remember session_id for all application life
			API.getInstance().setSessionId(getString(json, "id_session"));
			
			Log.i("yc", "Logged in, session_id=" + getString(json, "id_session"));

			//Start Home Activity
			Intent myIntent = new Intent(this, HomeActivity.class);
			startActivity(myIntent);
		}
	}
}