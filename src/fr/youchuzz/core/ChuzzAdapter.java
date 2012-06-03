package fr.youchuzz.core;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;

import fr.youchuzz.R;

public class ChuzzAdapter extends ArrayAdapter<Chuzz> {

	private ArrayList<Chuzz> items;
	private AQuery aq;
	public ChuzzAdapter(Context context, int textViewResourceId, ArrayList<Chuzz> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		
		aq = new AQuery(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_home_chuzz, null);
		}
		Chuzz c = items.get(position);
		if (c != null) {
			aq.recycle(v);
			aq.id(R.id.home_item_title).text(c.title);
			aq.id(R.id.home_item_desc).text(c.getDesc(v.getContext()));
			aq.id(R.id.home_item_img).image(c.imageUrl, true, true, 0, R.drawable.ic_menu_attachment);
		}
		
		v.setMinimumHeight(50);
		
		return v;
	}
	
	public Chuzz getChuzz(int pos)
	{
		return items.get(pos);
	}
}