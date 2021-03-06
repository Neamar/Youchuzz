package fr.youchuzz;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import fr.youchuzz.core.API;
import fr.youchuzz.core.Chuzz;
import fr.youchuzz.core.ChuzzAdapter;

public class HomeActivity extends BaseActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.home_title);
		setContentView(R.layout.activity_home);
		API.updateActivity(this);
		
		aq = new AQuery(this);
		aq.id(R.id.home_chuzzs).gone();
		aq.id(R.id.home_presentation).height(ViewGroup.LayoutParams.FILL_PARENT);
		
		aq.id(R.id.home_create).clicked(this, "onCreateChuzz");
		
		API.getInstance().getChuzzs(this, "onChuzzsLoaded");
	
		load();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		if(!isLoading)
		{
			API.updateActivity(this);
			Log.v("yc", "Refreshing chuzz list");
			load();
			API.getInstance().getChuzzs(this, "onChuzzsLoaded");
		}
	}
	
	/**
	 * Create new chuzz
	 * @param v
	 */
	public void onCreateChuzz(View v)
	{
		Intent myIntent = new Intent(v.getContext(), CreateActivity.class);
		startActivity(myIntent);
	}
	
	/**
	 * Gestion des menus standards, inflaté de res/menu/base.xml
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Récupérer le menu de base défini en XML
		/*MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_home, menu);*/
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public void onChuzzsLoaded2(String url, JSONArray json, AjaxStatus status)
	{
		Log.i("yc", "WTF3");
		onChuzzsLoaded(url, json, status);
	}
	
	/**
	 * Display all chuzz
	 * @param url
	 * @param json
	 * @param status
	 */
	public void onChuzzsLoaded(String url, JSONArray json, AjaxStatus status)
	{
		endLoad();
		if(json != null)
		{
			ArrayList<Chuzz> chuzzsList = new ArrayList<Chuzz>();
			
			if(json.length() > 0)
			{
				Log.i("yc", "Retrieved " + Integer.toString(json.length()) + " chuzz(s).");
				aq.id(R.id.home_chuzzs).itemClicked(this, "onChuzzClicked");
				aq.id(R.id.home_chuzzs).visible();
				aq.id(R.id.home_presentation).gone();
				for(int i = 0; i < json.length(); i++)
				{
					JSONObject jsonChuzz = new JSONObject();
					try {
						jsonChuzz = json.getJSONObject(i);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Chuzz chuzz = new Chuzz();
					chuzz.id = getInt(jsonChuzz, "chuzz_id");
					chuzz.title = getString(jsonChuzz, "title");
					chuzz.nbVoters = getInt(jsonChuzz, "voters");
					chuzz.creationDate = getString(jsonChuzz, "creation");
					chuzz.imageUrl = getString(jsonChuzz, "img");
	
					chuzzsList.add(chuzz);
				}
		
				ListView lv = (ListView) findViewById(R.id.home_chuzzs);
				ChuzzAdapter adapter = new ChuzzAdapter(this, R.layout.item_home_chuzz, chuzzsList);
				lv.setAdapter(adapter);
			}
		}
	}
	
	public void onChuzzClicked(AdapterView<ChuzzAdapter> parent, View v, int pos, long id)
	{
		Chuzz selectedChuzz = parent.getAdapter().getChuzz(pos);
		Intent chuzzIntent = new Intent(this, ChuzzActivity.class);
		chuzzIntent.putExtra("id", selectedChuzz.id);
		chuzzIntent.putExtra("title", selectedChuzz.title);
		startActivity(chuzzIntent);
	}
}