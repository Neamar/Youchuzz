package fr.youchuzz;

import java.util.ArrayList;

import com.androidquery.AQuery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import fr.youchuzz.core.Chuzz;
import fr.youchuzz.core.ChuzzAdapter;

public class HomeActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		
		ArrayList<Chuzz> m_chuzzs = new ArrayList<Chuzz>();
		Chuzz c1 = new Chuzz();
		c1.title = "Rouge à lèvres";
		c1.nbVoters = 12;
		c1.creationDate = "12/03/2012";
		Chuzz c2 = new Chuzz();
		c2.title = "Montre Rolex";
		c2.nbVoters = 158;
		c2.creationDate = "10/03/2012";
		m_chuzzs.add(c1);
		m_chuzzs.add(c2);

		ListView lv = (ListView) findViewById(R.id.home_chuzzs);
		ChuzzAdapter m_adapter = new ChuzzAdapter(this, R.layout.item_home_chuzz, m_chuzzs);
		lv.setAdapter(m_adapter);
		
		AQuery aq = new AQuery(this);
		
		aq.id(R.id.home_create).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), CreateContentActivity.class);
				startActivity(myIntent);
			}
		});
	}
}