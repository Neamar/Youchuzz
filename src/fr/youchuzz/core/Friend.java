package fr.youchuzz.core;

/**
 * Class handling data for a chuzz
 * @author neamar
 *
 */
public class Friend {
	public int id;
	public String name;
	public String firstName;
	public String email;
	public String imageUrl;
	
	public boolean selected = false;
	
	public String getName()
	{
		if(!name.equals(""))
			return firstName + " " + name;
		else
			return email;
	}
}
