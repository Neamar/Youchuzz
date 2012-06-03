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
 * Display chuzz details.
 * Uses an intent with two keys, "title" (string) and "id" (int).
 * 
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
		API.getInstance().setSessionId("1338712110b26a3b40f2f02f4932b88569c6fa1ac20389680d");
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
			
			aq.id(R.id.chuzz_comments).clicked(this, "onCommentsClicked");
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
				
				int maxVotes = -1;
				int maxVotesId = 0;
				
				for(int i = 0; i < contents.length(); i++)
				{
					aq.id(CONTENTS_ID[i]).visible();
					
					JSONObject content = contents.getJSONObject(i);
					String contentUrl = getString(content, "url");
					int nbVotes = getInt(content, "votes");
					
					if(nbVotes > maxVotes)
					{
						maxVotes = nbVotes;
						maxVotesId = i;
					}
							
					localAq.recycle(aq.id(CONTENTS_ID[i]).getView());
					localAq.id(R.id.chuzz_item_chuzz_content).image(contentUrl, true, true, 0, R.drawable.ic_menu_attachment);
					localAq.id(R.id.chuzz_item_chuzz_desc).text(Integer.toString(nbVotes) + " vote" + (nbVotes > 1?"s":""));
					((TextView) localAq.id(R.id.chuzz_item_chuzz_desc).getView()).setShadowLayer(4, 0, 0, Color.BLACK);
				}
				
				localAq.recycle(aq.id(CONTENTS_ID[maxVotesId]).getView());
				localAq.id(R.id.chuzz_item_chuzz_desc).backgroundColor(Color.GREEN);
				
				aq.id(R.id.chuzz_details).text(chuzz.getDesc());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void onCommentsClicked(View v)
	{
		Intent commentIntent = new Intent(this, CommentActivity.class);
		//Copy initial intent
		commentIntent.putExtra("id", getIntent().getIntExtra("id", -1));
		commentIntent.putExtra("title", getIntent().getStringExtra("title"));
		//Add details
		
		commentIntent.putExtra("details", aq.id(R.id.chuzz_details).getText());
		
		startActivity(commentIntent);
	}
}