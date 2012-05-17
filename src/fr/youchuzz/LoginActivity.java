package fr.youchuzz;

import com.androidquery.AQuery;

import android.app.Activity;
import android.os.Bundle;

public class LoginActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Initialize API object for all future access.
		API.init(this);
		
		setContentView(R.layout.activity_login);
		

	}
}