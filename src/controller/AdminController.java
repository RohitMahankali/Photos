package controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import model.DataStorage;
import model.User;

/**
 * @author Michael Belmont
 * @author Rohit Mahankali
 * 
 * The primary controller for the Admin window.
 * 
 * The Admin account can perform the following actions: 
 * 	View the list of users
 * 	Create users
 * 	Delete users
 *
 */
public class AdminController 
{
	
	@FXML
	Button btnAddUser;
	
	@FXML
	Button btnDeleteUser;
	
	@FXML
	Button btnLogoutAdmin;
	
	@FXML
	Button btnQuitAdmin;
	
	@FXML
	MenuItem mnuLogoutUser;
	
	@FXML
	MenuItem mnuQuitUser;
	
	@FXML
	MenuBar mnbBar;
	
	@FXML
	ListView<User> lstUsers = new ListView<User>();
	
	ObservableList<User> oblUsers = FXCollections.observableArrayList();
	
	ArrayList<User> userList = new ArrayList<User>();
	
	@FXML
	Label lblStatus;
	
	@FXML
	TextField txtManage;
	
	private DataStorage ds;
	
	/**
	 * start
	 * 
	 * Starts up the admin controller window by populating the list.
	 * @param stage (Stage)
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void start(Stage stage) throws ClassNotFoundException, IOException 
	{
		ds = DataStorage.read();
		userList = ds.getUserList();
		oblUsers = FXCollections.observableArrayList(userList);
		lstUsers.setItems(oblUsers);
		
		txtManage.setEditable(true);
	}
	
	/**
	 * addUser
	 * 
	 * Primary method to create a user.
	 * @param event (ActionEvent)
	 * @throws IOException 
	 */
	@FXML
	protected void addUser(ActionEvent event) throws IOException 
	{
		if(txtManage.getText().equals(""))
			return;
		
		if(ds.isContained(txtManage.getText().trim()))
		{
			lblStatus.setText("Duplicate username, please try again");
			return;
		}
		
		User user = new User(txtManage.getText().trim());
		userList.add(user);
		oblUsers.add(user);
		lstUsers.setItems(oblUsers);
		txtManage.clear();
		DataStorage.write(ds);
		lblStatus.setText("New user added: " + user.getUsername());
	}
	
	/**
	 * deleteUser
	 * 
	 * Deletes a user and anything contained within that user's folder
	 * @param event (ActionEvent)
	 * @throws IOException 
	 */
	@FXML
	protected void deleteUser(ActionEvent event) throws IOException 
	{
		User user = lstUsers.getSelectionModel().getSelectedItem();
		
		if(user == null)
			return;
		
		if(user.getUsername().equals("admin"))
			return;
		
		oblUsers.remove(user);
		lstUsers.setItems(oblUsers);
		userList.remove(user);
		DataStorage.write(ds);
		lblStatus.setText("User deleted: " + user.getUsername());
	}
	
	/**
	 * logout
	 * 
	 * Allows graceful logout of the admin account
	 * @param event (ActionEvent)
	 * @throws IOException
	 */
	public void logout(ActionEvent event) throws IOException 
	{
		DataStorage.write(ds);
		Stage stageToClose = (Stage) mnbBar.getScene().getWindow();
		stageToClose.hide();
		
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
		Scene scene = new Scene(root, 500, 500);
		
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * quit
	 * 
	 * Allows graceful exit of the application
	 * @param event (ActionEvent)
	 * @throws IOException 
	 */
	public void quit(ActionEvent event) throws IOException 
	{
		DataStorage.write(ds);
		Platform.exit();
		System.exit(0);
	}

}
