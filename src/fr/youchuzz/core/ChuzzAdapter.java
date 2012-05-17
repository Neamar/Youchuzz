package fr.youchuzz.core;

import java.util.ArrayList;

import fr.youchuzz.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChuzzAdapter extends ArrayAdapter<Chuzz> {

	private ArrayList<Chuzz> items;

	public ChuzzAdapter(Context context, int textViewResourceId, ArrayList<Chuzz> items) {
		super(context, textViewResourceId, items);
		this.items = items;
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
			TextView tt = (TextView) v.findViewById(R.id.home_item_title);
			TextView dt = (TextView) v.findViewById(R.id.home_item_desc);
			if (tt != null)
				tt.setText(c.title);
			if(dt != null)
				dt.setText(c.getDesc());
		}
		return v;
	}
}