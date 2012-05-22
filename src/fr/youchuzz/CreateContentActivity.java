package fr.youchuzz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.AQuery;

public class CreateContentActivity extends BaseActivity {
	
	private int CONTENT_1 = 1;
	private int CONTENT_2 = 2;
	
	private int PICKING = 0;
	private int UPLOADING = 1;
	private int PREVIEWING = 2;
	
	private int REQUEST_IMAGE = 100;
	private int REQUEST_PHOTO = 101;
	
	private int currentlyPicking = 0;
	
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
	}
	
	/**
	 * Called when user press pick button
	 * @param v
	 */
	public void onPickContent(View v)
	{
		if(v.getId() == R.id.create_content_add_1)
			currentlyPicking = CONTENT_1;
		else if(v.getId() == R.id.create_content_add_2)
			currentlyPicking = CONTENT_2;
		
		v.showContextMenu();
	}
	
	/**
	 * Display "select pick menu"
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_content_source_picker, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_content_source_pick_gallery:
				//Pick from gallery (or other file browser)
				Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("image/*");
				startActivityForResult(Intent.createChooser(galleryIntent, getString(R.string.create_content_pick_image)), REQUEST_IMAGE);

				return true;
			case R.id.menu_content_source_pick_photo:
				//Pick by taking new picture
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
	

	public void onActivityResult (int requestCode, int resultCode, Intent data)
	{
		Log.i("yc", "Results for " + Integer.toString(requestCode));
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_IMAGE) {
				Uri selectedImageUri = data.getData();
				updateUi(currentlyPicking, UPLOADING);
			}
		}
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