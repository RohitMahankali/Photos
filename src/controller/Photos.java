package controller;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import model.DataStorage;
import model.User;

/**
 * @author Michael Belmont
 * @author Rohit Mahankali
 * 
 * Primary controller, also the main driver for the application.
 *
 **/
public class Photos extends Application 
{
	/**
	 * start
	 * 
	 * Goes to login window upon application boot.
	 * @param stage
	 */
	@Override
	public void start(Stage stage) throws ClassNotFoundException, IOException 
	{
		DataStorage ds = new DataStorage();
		
		if(!(new File("data" + File.separator + "ds.dat").exists()))
		{
			ds.addUser(new User("admin"));
			DataStorage.write(ds);
		}
		
		ds = DataStorage.read();
		
		try 
		{
			Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
			Scene scene = new Scene(root,500,500);
			stage.setTitle("Photos");
			stage.setScene(scene);
			stage.show();
		} 
		
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) 
	{
		launch(args);
	}
}
