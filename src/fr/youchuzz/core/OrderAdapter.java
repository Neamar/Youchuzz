package fr.youchuzz.core;

import java.util.ArrayList;

import fr.youchuzz.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrderAdapter extends ArrayAdapter<Order> {

        private ArrayList<Order> items;

        public OrderAdapter(Context context, int textViewResourceId, ArrayList<Order> items) {
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
                Order o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.home_item_title);
                        TextView bt = (TextView) v.findViewById(R.id.home_item_desc);
                        if (tt != null) {
                              tt.setText("Name: "+o.getOrderName());                            }
                        if(bt != null){
                              bt.setText("Status: "+ o.getOrderStatus());
                        }
                }
                return v;
        }
}