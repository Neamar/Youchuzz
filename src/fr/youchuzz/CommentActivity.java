package fr.youchuzz;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.androidquery.AQuery;

import fr.youchuzz.core.API;

/**
 * Display comments for a chuzz.
 * Uses an intent with two keys, "title" (string) and "id" (int).
 * 
 * @author neamar
 *
 */
public class CommentActivity extends BaseActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		API.updateActivity(this);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_comment);
		
		aq = new AQuery(this);
		
		if(getIntent() == null)
		{
			error("Displaying comments without picking any chuzz.");
			finish();
		}
		else
		{
			setTitle(getIntent().getStringExtra("title"));
			aq.id(R.id.comment_detail).text(getIntent().getStringExtra("details"));
			aq.id(R.id.comment_chuzz).clicked(this, "onChuzzClicked");
			
			loadComments();
		}
	}
	
	public void loadComments()
	{
		//Load HTML from template file assets/comment.html
		String html = "";
		try
		{
			InputStream fin = getAssets().open("comments.html");
			byte[] buffer = new byte[fin.available()];
			fin.read(buffer);
			fin.close();

			html = new String(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		WebView webView = ((WebView) findViewById(R.id.comment_webview));


		//Replace placeholder
		html = html.replace("{{id}}", Integer.toString(getIntent().getIntExtra("id", -1)));

		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		// Load datas
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		
		webView.loadDataWithBaseURL("http://youchuzz.com", html, "text/html", "UTF-8", null);
	}
	
	public void onChuzzClicked(View v)
	{
		finish();
	}
}