package fr.youchuzz;

import org.json.JSONObject;

import android.content.Intent;
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
	Facebook facebook = new Facebook("297600333614254");
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Initialize API object for all future access.
		API.init(this);
		
		setContentView(R.layout.activity_login);
		
		aq = new AQuery(this);
		aq.id(R.id.login_facebook).clicked(this, "onFacebookButtonClick");
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
	public void onFacebookButtonClick(View v)
	{
		facebook.authorize(this, new DialogListener() {
			@Override
			public void onComplete(Bundle values) {
				//Everything is OK
				youchuzzLogin(facebook.getAccessToken());
			}
			
			@Override
			public void onFacebookError(FacebookError error) {
				Toast.makeText(getBaseContext(), error.getErrorType(), Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onError(DialogError e) {}
			
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
	public void youchuzzLogin(String facebookToken)
	{
		API.getInstance().login(facebook.getAccessToken(), this, "youchuzzLogged");
		//TODO: display spinner
	}
	
	public void youchuzzLogged(String url, JSONObject json, AjaxStatus status) {
		if(json == null)
		{
			Log.e("yc", "Error while logging in." + status.getCode());
		}
		else
		{
			API.getInstance().setNextToken(getString(json, "token"));
			API.getInstance().setSessionId(getString(json, "id_session"));
			
			Intent myIntent = new Intent(this, HomeActivity.class);
			startActivity(myIntent);
		}
	}
}