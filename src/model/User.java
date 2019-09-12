package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User
 * 
 * A user class that contains information about a user account including username and albums.
 * 
 * @author Michael Belmont
 * @author Rohit Mahankali
 */
public class User implements Serializable 
{
	private String username;
	private ArrayList<Album> albumList;
	
	/**
	 * Object Constructor
	 * 
	 * Creates a user object, taking in the username and creating an empty album list.
	 * @param username	The username
	 */
	public User(String username) 
	{
		this.username = username;
		this.albumList = new ArrayList<Album>();
	}
	
	/**
	 * getUsername
	 * 
	 * Returns the username of the user object.
	 * @return username (String)
	 */
	public String getUsername() 
	{
		return username;
	}
	
	/**
	 * getAlbumList
	 * 
	 * Returns the ArrayList of albums.
	 * @return albumList (ArrayList)
	 */
	public ArrayList<Album> getAlbumList() 
	{
		return albumList;
	}
	
	/**
	 * addEmptyAlbum
	 * 
	 * Adds a new album to the album list, using the name passed to the method.
	 * @param name (String)
	 */
	public void addEmptyAlbum(String name) 
	{
		albumList.add(new Album(name));
	}
	
	/**
	 * addAlbum
	 * 
	 * Adds an already created album to this user's albumList.
	 * @param album (Album)
	 */
	public void addAlbum(Album album) 
	{
		albumList.add(album);
	}
	
	/**
	 * insertPhoto
	 * 
	 * Used to insert a photo into an album object.
	 * @param photo (Photo)
	 * @param index (int)
	 */
	public void insertPhoto(Photo photo, int index) 
	{
		albumList.get(index).addPhoto(photo);
	}
	
	/**
	 * nameCheck
	 * 
	 * Checks to see if the entered album name is unique.
	 * @param name (String)
	 * @return if exists (boolean)
	 */
	public boolean nameCheck(String name) 
	{
		for (Album currentAlbum : albumList)
		{
			if (currentAlbum.getName().toLowerCase().equals(name.trim().toLowerCase()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * getIndexOfAlbum
	 * 
	 * @param album (Album)
	 * @return index (int)
	 */
	public int getIndex(Album album) 
	{
		for (int index = 0; index < albumList.size(); index++)
		{
			if (albumList.get(index).getName().equals(album.getName()))
			{
				return index;
			}
		}
		
		return -1;
	}
	
	
	/**
	 * getAlbum
	 * 
	 * Returns the album requested.
	 * @param name (String)
	 * @return desired album (Album)
	 */
	public Album getAlbum(String name) 
	{
		for(Album currentAlbum : albumList)
		{
			if(currentAlbum.getName().equals(name))
			{
				return currentAlbum;
			}
		}
		return null;
	}
	
	/**
	 * deleteAlbum
	 * 
	 * Deletes the desired album from the user's album list.
	 * @param album (Album)
	 * 
	 */
	
	public void deleteAlbum(Album album)
	{
		albumList.remove(album);
	}
	
	/**
	 * toString
	 * 
	 * Overrides the default toString method.
	 * @return username (String)
	 */
	@Override
	public String toString()
	{
		return username;
	}
	
}