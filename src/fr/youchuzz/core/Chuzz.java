package fr.youchuzz.core;

/**
 * Class handling data for a chuzz
 * @author neamar
 *
 */
public class Chuzz {
	public String title;
	public int nbVoters;
	public String creationDate;
	public String imageUrl = "http://imalbum.aufeminin.com/album/D20100222/644270_RQXTV5N6MQVPM83FVKXKOQ2JXQ1XTT_rouge-a-levre-dr-pierre-ricaud_H160438_S.jpg";
	
	public String getDesc()
	{
		return nbVoters + " votant(s), créé le " + creationDate;
	}
}
