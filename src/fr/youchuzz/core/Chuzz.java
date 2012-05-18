package fr.youchuzz.core;

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
	public String imageUrl;
	
	public String getDesc()
	{
		return nbVoters + " vote" + (nbVoters>1?"s":"") + ", créé le " + creationDate;
	}
}
