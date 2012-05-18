package fr.youchuzz;

import android.app.Activity;
import android.os.Bundle;

import com.androidquery.AQuery;

public class CreateContentActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_create_content);
		
		AQuery aq = new AQuery(this);
		
		//http://developer.android.com/reference/android/content/Intent.html#ACTION_GET_CONTENT
	}
}