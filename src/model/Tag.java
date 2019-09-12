package model;

import java.io.Serializable;

/**
 * Tag
 * 
 * A tag object that contains the name (location, person, etc.) and value (New Brunswick, Suzie, etc.) of each tag.
 * 
 * @author Michael Belmont
 * @author Rohit Mahankali
 */
public class Tag implements Serializable 
{
	private String name, value;
	
	/**
	 * Object Constructor
	 * 
	 * Creates a Tag object that has both a name and a value.
	 * @param name (String)
	 * @param value (String)
	 */
	public Tag(String name, String value) 
	{
		this.name = name;
		this.value = value;
	}
	
	/**
	 * getName
	 * 
	 * Returns the name of the tag.
	 * @return name (String)
	 */
	public String getName() 
	{
		return name;
	}
	
	/**
	 * getValue
	 * 
	 * Returns the value of the tag.
	 * @return value (String)
	 */
	public String getValue() 
	{
		return value;
	}
	
	/**
	 * setName
	 * 
	 * Changes the name of the tag.
	 * @param name (String)
	 */
	public void setName(String name) 
	{
		this.name = name;
	}
	
	/**
	 * setValue
	 * 
	 * Changes the value of the tag.
	 * @param value (String)
	 */
	public void setValue(String value) 
	{
		this.value = value;
	}
	
	/**
	 * equals
	 * 
	 * Overrides the default equals method, instead comparing a tag's name and value with another to determine if they are identical.
	 * @return if equals (boolean)
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (obj==null)
			if (!(obj instanceof Tag))
			   return false;

		Tag otherTag = (Tag) obj;

        return name.equals(otherTag.getName()) && value.equals(otherTag.getValue());
	}
	
}