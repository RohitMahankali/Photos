package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.scene.image.Image;

/**
 * Photo
 * 
 * A photo object that contains the serialized photo object and some information about the photo (date/time uploaded, caption, tagList).
 * 
 * @author Michael Belmont
 * @author Rohit Mahankali
 */
public class Photo implements Serializable 
{
	private SerializedPhoto sPhoto;
	private String caption;
	private ArrayList<Tag> tagList;
	private Calendar dateTime;
	private String name;
	
	/**
	 * Object Constructor
	 *
	 * Creates an empty photo object, other than the date and time.
	 */
	public Photo() 
	{
		name = "";
		caption = "";
		tagList = new ArrayList<Tag>();
		dateTime = Calendar.getInstance();
		dateTime.set(Calendar.MILLISECOND, 0);
		sPhoto = new SerializedPhoto();
	}
	
	/**
	 * Object Constructor
	 * 
	 * Creates a photo object with an image.
	 * @param image (Image)
	 */
	public Photo(Image image) 
	{
		this();
		sPhoto.deconstruct(image);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String newName)
	{
		this.name = newName;
	}
	
	/**
	 * getPhoto
	 * 
	 * Returns the reconstructed photo of the sPhoto variable.
	 * @return constructed photo (Image)
	 */
	public Image getPhoto() 
	{
		return sPhoto.construct();
	}
	
	/**
	 * getSerializedPhoto
	 * 
	 * Returns the serialized photo object (sPhoto) with the deconstructed image contained within it.
	 * @return sPhoto (SerializedPhoto)
	 */
	public SerializedPhoto getSerializedPhoto() 
	{
		return sPhoto;
	}
	
	// begin tag operations -----------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * addTag
	 * 
	 * Creates a new tag on the photo with both a name (location, person, etc.) and a value (New Brunswick, Suzie, etc.).
	 * @param name (String)
	 * @param value (String)
	 */
	public void addTag(String name, String value) 
	{
		tagList.add(new Tag(name, value));
	}
	
	/**
	 * deleteTag
	 * 
	 * Deletes a tag from the tag list.
	 * @param i (int)
	 */
	public void deleteTag(int i) 
	{
		tagList.remove(i);
	}
	
	/**
	 * getTag
	 * 
	 * @param index (int)
	 * @return The desired tag (Tag)
	 */
	public Tag getTag(int index) 
	{
		return tagList.get(index);
	}
	
	/**
	 * getTagList
	 * 
	 * Returns the list of tags on the photo.
	 * @return tagList (ArrayList)
	 */
	public ArrayList<Tag> getTagList() 
	{
		return tagList;
	}
	
	/**
	 * isContained
	 * 
	 * Checks to see if the given list of tags is contained within the photo's list of tags (for searching purposes).
	 * @param searchTags (ArrayList)
	 * @return if searchTags is contained within tagList (boolean)
	 */
	public boolean isContained(ArrayList<Tag> searchTags) 
	{
		for (Tag currentTag : searchTags) 
		{
			if (!tagList.contains(currentTag))
			{
				return false;
			}
		}
		
		return true;
	}
	
	// end tag operations ------------------------------------------------------------------------------------------------
	
	// begin caption operations ------------------------------------------------------------------------------------------
	
	/**
	 * changeCaption
	 * 
	 * Changes the current caption of the photo.
	 * @param newCaption (String)
	 */
	public void changeCaption(String newCaption) 
	{
		this.caption = newCaption;
	}
	
	/**
	 * getCaption
	 * 
	 * Returns the current caption of the photo.
	 * @return caption (String)
	 */
	public String getCaption() 
	{
		return caption;
	}
	
	// end caption operations -------------------------------------------------------------------------------------------
	
	// begins date/time operations --------------------------------------------------------------------------------------
	
	/**
	 * getCalendar
	 * 
	 * Returns the Calendar object that contains the photo's date and time.
	 * @return dateTime (Calendar)
	 */
	public Calendar getCalendar() 
	{
		return dateTime;
	}
	
	/**
	 * calendarToString
	 * 
	 * Returns a String containing the date/time of the Calendar object, for displaying purposes.
	 * @return date/time (String)
	 */
	public String calendarToString() 
	{
		String[] str = dateTime.getTime().toString().split("\\s+");
		return str[0] + " " + str[1] + " " + str[2] + ", " + str[5];
	}
	
	/**
	 * checkDateTime
	 * 
	 * Checks to see if the photo was taken between the start date and end date of the given range (inclusive).
	 * @param start (LocalDate) 
	 * @param end (LocalDate)
	 * @return if between start and end (boolean)
	 */
	public boolean checkDateTime(LocalDate start, LocalDate end) 
	{	
		// converts calendar to localdate
		
		LocalDate thisDate  = dateTime.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return (thisDate.equals(start) || thisDate.equals(end) || (thisDate.isAfter(start) && thisDate.isBefore(end)));
	}

}