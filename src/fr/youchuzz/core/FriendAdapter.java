package fr.youchuzz.core;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;

import fr.youchuzz.R;

public class FriendAdapter extends ArrayAdapter<Friend> {

	private ArrayList<Friend> items;

	public FriendAdapter(Context context, int textViewResourceId, ArrayList<Friend> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_friends_friend, null);
		}
		Friend f = items.get(position);
		if (f != null) {
			AQuery aq = new AQuery(v);
			aq.id(R.id.friends_item_friend_name).text(f.getName());
			aq.id(R.id.friends_item_friend_check).checked(f.selected);
			//TODO: test again
			if(!aq.shouldDelay(position, convertView, parent, f.imageUrl))
				aq.id(R.id.friends_item_friend_image).image(f.imageUrl, true, true, 0, R.drawable.friend_placeholder);
		}
		
		return v;
	}
	
	public Friend getFriend(int pos)
	{
		return items.get(pos);
	}
}