package fr.youchuzz;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import fr.youchuzz.core.API;
import fr.youchuzz.core.Friend;
import fr.youchuzz.core.FriendAdapter;

/**
 * Display your friend list, then send your chuzz to the selected ones.
 * 
 * @author neamar
 *
 */
public class FriendsActivity extends BaseActivity {

	/**
	 * Intent calling this activity
	 */
	private Intent callingIntent;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		callingIntent = getIntent();
		if(!callingIntent.hasExtra("title"))
		{
			error("Displaying Friends list before picking content. Aborting launch.");
			finish();
		}

		setContentView(R.layout.activity_friends);

		aq = new AQuery(this);
		
		API.getInstance().getFriends(this, "onFriendLoaded");
		
		modalLoad("", getString(R.string.friends_loading));
		
		aq.id(R.id.friends_list).itemClicked(this, "onItemClicked");
		
		aq.id(R.id.friends_publish_wall).clicked(this, "onWallClicked");
		
		aq.id(R.id.friends_send).clicked(this, "onSend");
	}
	
	public void onFriendLoaded(String url, JSONArray json, AjaxStatus status)
	{
		endModalLoad();
		if(json == null)
		{
			error("Error while retrieving friends : err. " + status.getCode());
		}
		else
		{
			ArrayList<Friend> friendsList = new ArrayList<Friend>();
			
			Log.i("yc", "Retrieved " + Integer.toString(json.length()) + " friend(s).");
			for(int i = 0; i < json.length(); i++)
			{
				JSONObject jsonFriend = new JSONObject();
				try {
					jsonFriend = json.getJSONObject(i);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Friend friend = new Friend();
				friend.id = getInt(jsonFriend, "id");
				friend.name = getString(jsonFriend, "name");
				friend.firstName = getString(jsonFriend, "firstname");
				friend.email = getString(jsonFriend, "email");
				friend.imageUrl = getString(jsonFriend, "picture");

				friendsList.add(friend);
			}
	
			//TODO: allow filtering?
			ListView lv = (ListView) findViewById(R.id.friends_list);
			FriendAdapter adapter = new FriendAdapter(this, R.layout.item_friends_friend, friendsList);
			lv.setAdapter(adapter);
		}
	}
	
	public void onWallClicked(View v)
	{
		//TODO: select all items in list ?
		if(aq.id(R.id.friends_publish_wall).isChecked())
		{
			aq.id(R.id.friends_list).gone();
		}
		else
		{
			aq.id(R.id.friends_list).visible();
		}
	}
	
	/**
	 * Called whend a friend is clicked for selection / deselection
	 * @param parent
	 * @param v
	 * @param pos
	 * @param id
	 */
	public void onItemClicked(AdapterView<FriendAdapter> parent, View v, int pos, long id)
	{
		//Friend is clicked : check checkbox
		AQuery localAq = new AQuery(v);
		AQuery checkbox = localAq.id(R.id.friends_item_friend_check);
		boolean checked = !checkbox.isChecked();
		checkbox.checked(checked);
		
		parent.getAdapter().getFriend(pos).selected = checked;
	}
	
	public void onSend(View v)
	{
		
	}

}