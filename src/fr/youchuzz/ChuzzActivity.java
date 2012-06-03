package fr.youchuzz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
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
			updateOrientation(getResources().getConfiguration());
			
			API.getInstance().getChuzz(this, "onChuzzLoaded", getIntent().getIntExtra("id", -1));
			
			load();
			
			for(int i = 0; i < CONTENTS_ID.length; i++)
			{
				aq.id(CONTENTS_ID[i]).gone();
			}
			
			aq.id(R.id.chuzz_comments).clicked(this, "onCommentsClicked");
		}
	}
	
	/**
	 * Handle rotation to display properly the layout.
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		updateOrientation(newConfig);
	}
	
	/**
	 * Update the UI according to current orientation
	 * @param config
	 */
	protected void updateOrientation(Configuration config)
	{
		((LinearLayout) findViewById(R.id.chuzz_content_layout))
		.setOrientation(config.orientation==Configuration.ORIENTATION_PORTRAIT ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
	}
	
	public void onChuzzLoaded(String url, JSONObject json, AjaxStatus status) {
		endLoad();
		if(json == null)
		{
			error("Unable to load chuzz.");
			finish();
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
				
				if(maxVotes > 0)
				{
					localAq.recycle(aq.id(CONTENTS_ID[maxVotesId]).getView());
					localAq.id(R.id.chuzz_item_chuzz_desc).backgroundColor(Color.GREEN);
				}
				
				aq.id(R.id.chuzz_details).text(chuzz.getShortDesc(this));
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