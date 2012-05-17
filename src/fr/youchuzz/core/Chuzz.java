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
	
	public String getDesc()
	{
		return nbVoters + " votant(s), créé le " + creationDate;
	}
}
