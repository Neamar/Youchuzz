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
	public int[] CONTENTS_ID = new int[]{R.id.chuzz_content1, R.id.chuzz_content2};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//TODO : recycle activity
		API.init(this);
		API.getInstance().setSessionId("1338567801684ce76c3b182fb02f58db6998d47fa37d69a466");
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_chuzz);
		
		aq = new AQuery(this);
		
		/*if(getIntent() == null)
		{
			error("Loading activity without picking any chuzz.");
			finish();
		}
		else
		{*/
			//setTitle(getIntent().getStringExtra("title"));
			setTitle("Le nom de mon chuzz");
			
			API.getInstance().getChuzz(this, "onChuzzLoaded", 4);
			
			load();
			
			for(int i = 0; i < CONTENTS_ID.length; i++)
			{
				aq.id(CONTENTS_ID[i]).gone();
			}
		//}
	}
	
	public void onChuzzLoaded(String url, JSONObject json, AjaxStatus status) {
		endLoad();
		if(json == null)
		{
			error(":( TODO");
		}
		else
		{
			try {
				
				JSONArray contents = json.getJSONArray("content");
				
				Chuzz chuzz = new Chuzz();
				chuzz.json = json;
				chuzz.id = getInt(json, "chuzz_id");
				chuzz.creationDate = getString(json, "creation");
				chuzz.nbVoters = getInt(json, "voters");
				
				AQuery localAq = new AQuery(this);
				for(int i = 0; i < contents.length(); i++)
				{
					aq.id(CONTENTS_ID[i]).visible();
					
					JSONObject content = contents.getJSONObject(i);
					String contentUrl = getString(content, "url");
					int nbVotes = getInt(content, "votes");
							
					localAq.recycle(aq.id(CONTENTS_ID[i]).getView());
					localAq.id(R.id.chuzz_item_chuzz_content).image(contentUrl, true, true, 0, R.drawable.ic_menu_attachment);
					localAq.id(R.id.chuzz_item_chuzz_desc).text(Integer.toString(nbVotes) + " vote" + (nbVotes > 1?"s":""));
					((TextView) localAq.id(R.id.chuzz_item_chuzz_desc).getView()).setShadowLayer(4, 0, 0, Color.BLACK);
				}
				
				aq.id(R.id.chuzz_details).text(chuzz.getDesc());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}