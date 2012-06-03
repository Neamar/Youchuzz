package fr.youchuzz;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import fr.youchuzz.core.API;
import fr.youchuzz.core.Chuzz;

/**
 * Display comments for a chuzz.
 * Uses an intent with two keys, "title" (string) and "id" (int).
 * 
 * @author neamar
 *
 */
public class CommentActivity extends BaseActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		API.updateActivity(this);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_comment);
		
		aq = new AQuery(this);
		
		if(getIntent() == null)
		{
			error("Displaying comments without picking any chuzz.");
			finish();
		}
		else
		{
			setTitle(getIntent().getStringExtra("title"));
			aq.id(R.id.comment_detail).text(getIntent().getStringExtra("details"));
			aq.id(R.id.comment_chuzz).clicked(this, "onChuzzClicked");
		}
	}
	
	public void onChuzzClicked(View v)
	{
		finish();
	}
}