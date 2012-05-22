package fr.youchuzz;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.View;

import com.androidquery.AQuery;

public class CreateContentActivity extends BaseActivity {
	private int CONTENT_1 = 1;
	private int CONTENT_2 = 2;
	
	private int PICKING = 0;
	private int UPLOADING = 1;
	private int PREVIEWING = 2;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_create_content);
		
		aq = new AQuery(this);
		
		//Initialize ui, allow user to pick content
		updateUi(CONTENT_1, PICKING);
		updateUi(CONTENT_2, PICKING);
		
		aq.id(R.id.create_content_add_1).clicked(this, "onPickContent");
		aq.id(R.id.create_content_add_2).clicked(this, "onPickContent");
		
		
		registerForContextMenu(aq.id(R.id.create_content_add_1).getView());
		registerForContextMenu(aq.id(R.id.create_content_add_2).getView());
		
		//http://developer.android.com/reference/android/content/Intent.html#ACTION_GET_CONTENT
	}
	
	/**
	 * Called when user press pick button
	 * @param v
	 */
	public void onPickContent(View v)
	{
		Log.i("yc", "Picking content");
		v.showContextMenu();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.content_source_picker, menu);
	}
	
	/**
	 * Update the UI to match current state
	 * @param step
	 */
	private void updateUi(int content, int step)
	{
		int[] ids = null;
		
		if(content == 1)
		{
			ids = new int[]{R.id.create_content_add_1, R.id.create_content_uploading_1, R.id.create_content_image_1};
		}
		else if(content == 2)
		{
			ids = new int[]{R.id.create_content_add_2, R.id.create_content_uploading_2, R.id.create_content_image_2};
		}
		
		//Hide all
		for(int i = 0; i < ids.length; i++)
		{
			aq.id(ids[i]).gone();
		}
		
		//Display current ui-item
		aq.id(ids[step]).visible();
	}
}