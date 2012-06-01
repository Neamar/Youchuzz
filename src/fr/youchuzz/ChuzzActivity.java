package fr.youchuzz;

import java.io.File;

import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
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

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import fr.youchuzz.core.API;

/**
 * Create a new chuzz.
 * 
 * Ask the user to choose a title and to pick two contents from his phone.
 * 
 * Typical workflow :
 * - onCreate, then you click on a content picker :
 * - onPickContent, then you choose content type from the dialog leading you to
 * - onContextItemSelected, then you pick content using whatever method you asked, returning to
 * - onActivityResult, which will start upload and then call
 * - onContentUploaded. Once you've done that twice, you may call
 * - onSave, which will display friend list before saving.
 * @author neamar
 *
 */
public class ChuzzActivity extends BaseActivity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		API.updateActivity(this);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_chuzz);
		
		aq = new AQuery(this);
		
		if(getIntent() == null)
		{
			error("Loading activity without picking any chuzz.");
			finish();
		}
		else
		{
			setTitle(getIntent().getStringExtra("title"));
		}
	}
}