package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;	
	 
/**
 * @author Michael Belmont
 * @author Rohit Mahankali
 * 
 * This is the primary controller for the login screen.
 * 
 */
public class LoginController 
{
	@FXML private Button btnLogin;
	
	@FXML private TextField txtUser;
	    
	/**
	 * login
	 * 
	 * Is executed when the login button is clicked, login a user in, whether it is the admin or a normal user.
	 * @param event (ActionEvent)
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 */
	@FXML protected void login(ActionEvent event) throws ClassNotFoundException, IOException 
	{
		String username = txtUser.getText();
	    	
	    Parent parent;
		DataStorage ds = DataStorage.read();
					
		if(ds.isContained(username)) 
		{
			if(username.equals("admin"))
			{	 
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
				parent = (Parent) loader.load();
				        
				AdminController ac = loader.getController();
				
				Scene scene = new Scene(parent);
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();	
				               
				ac.start(stage);
				            
				stage.setScene(scene);
				stage.show();
			}
			
			else
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/User.fxml"));
				parent = (Parent) loader.load();
			
				UserController uc = loader.getController();
				
				uc.setDS(ds);
				uc.setUser(ds.getUser(username.toLowerCase()));
				
				Scene scene = new Scene(parent);
				
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();	
				
				uc.start(stage);
				
				stage.setScene(scene);
				stage.show();  
			}
					
		}
	         
	}
	    
	   
}