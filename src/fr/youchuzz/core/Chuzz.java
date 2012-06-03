package fr.youchuzz.core;

import org.json.JSONObject;

import android.content.Context;
import fr.youchuzz.R;

/**
 * Class handling data for a chuzz
 * @author neamar
 *
 */
public class Chuzz {
	public int id;
	public String title;
	public int nbVoters;
	public String creationDate;
	
	/**
	 * Image representation for the chuzz -- most probably the image
	 * with the most votes.
	 */
	public String imageUrl;
	
	/**
	 * Json representation for the content, holding additional information
	 */
	public JSONObject json;
	
	public String getDesc(Context c)
	{
		String template = c.getString(R.string.home_ui_item_desc);
		template = template
				.replace("{{vs}}", nbVoters>1?"s":"")
				.replace("{{v}}", Integer.toString(nbVoters))
				.replace("{{d}}", creationDate);
		return template;
	}
}
