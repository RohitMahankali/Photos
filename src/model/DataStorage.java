package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * DataStorage
 * 
 * Serializes the data and stores all of the users and their data.
 * 
 * @author Michael Belmont
 * @author Rohit Mahankali
 */

public class DataStorage implements Serializable
{
	private static final long serialVersionUID = 8675309L;
	
	public static final String storage = "ds.dat";
	
	private ArrayList<User> userList;
	
	/**
	 * Object Constructor
	 * 
	 * Creates a DataStorage object that will contain all of the program's user data.
	 */
	public DataStorage() 
	{
		userList = new ArrayList<User>();
	}
	
	/**
	 * read
	 * 
	 * Returns a DataStorage object with all of the data contained within the .dat file.
	 * @return ds (DataStorage)
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static DataStorage read() throws ClassNotFoundException, IOException
	{
		ObjectInputStream input = new ObjectInputStream(new FileInputStream("data" + File.separator + storage));
		DataStorage ds = (DataStorage) input.readObject();
		input.close();
		return ds;
	}
	   
	/**
	 * write
	 * 
	 * Writes the data to the .dat file so it can be accessed at a later time.
	 * @param ds (DataStorage)
	 * @throws IOException
	 */
	public static void write (DataStorage ds) throws IOException 
	{
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("data" + File.separator + storage));
		output.writeObject(ds);
		output.close();
	}
	
	/** 
	 * getUserList
	 * 
	 * Returns the list of User objects that have accounts on this instance of the application.
	 * @return userList (ArrayList)
	 */
	public ArrayList<User> getUserList()
	{
		return userList;
	}
	
	/**
	 * addUser
	 * 
	 * Adds a new User to the userList.
	 * @param user (User)   
	 */  
	public void addUser(User user)
	{
		userList.add(user);
	}
	
	/**
	 * 
	 * 
	 * Deletes a specified User from the userList.
	 * @param user (User)	    
	 */
	public void deleteUser(User user)
	{
		userList.remove(user);
	}
	
	/**
	 * isContained
	 * 
	 * Checks to see if a user is contained within the userList, using the username.
	 * @param username (String)
	 * @return if contained in list (boolean)
	 */
	public boolean isContained(String username)
	{
		for(User currentUser : userList)
		{
			if (currentUser.getUsername().equals(username))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * getUser
	 * 
	 * Returns the user specified, using the username.
	 * @param username (String)
	 * @return user (User)
	 */
	public User getUser(String username) 
	{
		for (User user : userList)
		{
			if (user.getUsername().equals(username))
			{
				return user;
			}
		}
		
		return null;
	}
	
}