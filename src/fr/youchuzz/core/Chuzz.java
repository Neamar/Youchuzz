package fr.youchuzz.core;

import java.util.ArrayList;

import org.json.JSONObject;

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
	
	public String getDesc()
	{
		return nbVoters + " vote" + (nbVoters>1?"s":"") + ", créé le " + creationDate;
	}
}
