package model;

import java.io.Serializable;

import java.util.ArrayList;

/**
 * Album
 * 
 * An album that contains the name of the album and it's photos.
 * 
 * @author Michael Belmont
 * @author Rohit Mahankali
 */
public class Album implements Serializable 
{
	private String name;
	private ArrayList<Photo> photos;
	
	/**
	 * Object Constructor
	 * 
	 * Creates an Album object.
	 * @param name (String)
	 */
	public Album(String name) 
	{
		this.name = name;
		this.photos = new ArrayList<Photo>();
	}
	
	/**
	 * setName
	 * 
	 * Changes the album name.
	 * @param newName (String)
	 */
	public void setName(String newName) 
	{
		this.name = newName;
	}
	
	/**
	 * getName
	 * 
	 * Returns the name of the album.
	 * @return album name
	 */
	public String getName() 
	{
		return name;
	}
	
	/**
	 * addPhoto
	 * 
	 * Adds a photo to the album.
	 * @param photo (Photo)
	 */
	public void addPhoto(Photo photo) 
	{
		photos.add(photo);
	}
	
	/**
	 * deletePhoto
	 * 
	 * Takes in the index of the photo to be deleted and deletes it from the album.
	 * @param i (int)
	 */
	public void deletePhoto(int i) 
	{
		photos.remove(i);
	}
	
	/**
	 * getPhotoByIndex
	 * 
	 * Takes in the index of a photo and returns the photo at that index.
	 * @param i	(int)
	 * @return the desired photo (Photo)
	 */
	public Photo getPhotoByIndex(int i) 
	{
		return photos.get(i);
	}
	
	/**
	 * getIndexOfPhoto
	 * 
	 * Searches through the photos to find what index the specified photo is at.
	 * @param photo (Photo)
	 * @return the index (int)
	 */
	public int getIndexOfPhoto(Photo photo) 
	{
		for (int i = 0; i < photos.size(); i++)
		{
			if (photos.get(i).equals(photo))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * getPhotos
	 * 
	 * Returns the list of photos in the album.
	 * @return photos (ArrayList)
	 */
	public ArrayList<Photo> getPhotos() 
	{
		return photos;
	}
	
	/**
	 * getSize
	 * 
	 * Returns the number of photos in the album.
	 * @return number of photos (int)
	 */
	public int getSize() 
	{
		return photos.size();
	}
	
	/**
	 * 
	 * 
	 * 
	 * @return oldestPhotoString (String)
	 */
	public String getOldestString()
	{
		if(photos.size()==0)
			return "check.";
		
		Photo oldest = this.getPhotoByIndex(0);
		
		for (Photo photo : this.photos)
		{
			if(photo.getCalendar().compareTo(oldest.getCalendar()) > 0)
			{
				oldest = photo;
			}
		}
		
		return oldest.calendarToString();
	}
	
	/**
	 * 
	 * 
	 * 
	 * @return newestPhotoString (String)
	 */
	public String getNewestString()
	{
		if(photos.size()==0)
			return "No photos";
		
		Photo newest = this.getPhotoByIndex(0);
		
		for (Photo photo : this.photos)
		{
			if(photo.getCalendar().compareTo(newest.getCalendar()) < 0)
			{
				newest = photo;
			}
		}
		
		return newest.calendarToString();
	}
	
	/**
	 * toString
	 * 
	 * Overrides the toString method and returns the album name.
	 * @return name (String)
	 */
	public String toString()
	{
		return name;
	}
	
}