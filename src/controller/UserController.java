package controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Album;
import model.DataStorage;
import model.User;

/**
 * @author Michael Belmont
 * @author Rohit Mahankali
 *
 * This class is the primary controller for the user objects.
 * The window created by this will be for displaying any albums for users.
 * Each user is able to:
 * 	Open albums
 * 	Create albums
 * 	Rename albums
 * 	Delete albums
 * 	Search for photos
 */
public class UserController 
{

	@FXML
	Button btnOpenAlbum;

	@FXML
	Button btnRenameAlbum;

	@FXML
	Button btnDeleteAlbum;

	@FXML
	Button btnCreateAlbum;

	@FXML
	Button btnSearch;

	@FXML
	Label lblName;
	
	@FXML
	Label lblNumber;
	
	@FXML
	Label lblDateRange;
	
	@FXML
	MenuItem mnuLogoutUser;
	
	@FXML
	MenuItem mnuQuitUser;
	
	@FXML
	MenuBar mnbBar;

	@FXML
	ListView<Album> lstAlbums = new ListView<Album>();

	@FXML
	TextField txtCreateAlbum;

	ObservableList<Album> oblAlbums = FXCollections.observableArrayList();
	
	ArrayList<Album> albumList = new ArrayList<Album>();

	private User currentUser;
	
	private DataStorage ds;

	/**
	 * start
	 * 
	 * Initializes the list of albums on application startup, if there are available users.
	 * @param stage (Stage)
	 */
	public void start(Stage stage) 
	{
		albumList = currentUser.getAlbumList();
		
		oblAlbums = FXCollections.observableArrayList(albumList);
		
		lstAlbums.setItems(oblAlbums);
	}
	
	/**
	 * 
	 */
	@FXML
	public void onPress(MouseEvent m)
	{
		if(lstAlbums.getSelectionModel().getSelectedItem() == null)
			return;
		
		Album albumSelected = lstAlbums.getSelectionModel().getSelectedItem();
		
		lblName.setText("Album Name: " + albumSelected.getName());
		lblNumber.setText("Number of Photos: " + albumSelected.getSize());
		lblDateRange.setText("Date Range: " + albumSelected.getNewestString() + " to " + albumSelected.getOldestString());
	}
	
	/**
	 * search
	 * 
	 * Opens the search window.
	 * @param event
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@FXML
	public void search(ActionEvent event) throws IOException, ClassNotFoundException 
	{
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Search.fxml"));
		
		Parent root = (Parent) loader.load();
		
		SearchController sc = loader.<SearchController>getController();
		
		sc.setDS(ds);
		sc.setUser(currentUser);

		Scene scene = new Scene(root, 640, 400);
		stage.setScene(scene);
		sc.start(stage);
		stage.show();
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
		{
	          public void handle(WindowEvent we) 
	          {
	              refresh();
	          }
	      });
	}
	
	/**
	 * openAlbum
	 * 
	 * Opens up an album window with available photos within the album.
	 * @param event (ActionEvent)
	 * @throws IOException
	 */
	public void openAlbum(ActionEvent event) throws IOException 
	{
		Album albumToOpen = lstAlbums.getSelectionModel().getSelectedItem();

		if(albumToOpen == null)
			return;
		
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/view/Album.fxml").openStream());

		AlbumController ac = (AlbumController) loader.getController();

		ac.initializeAlbum(currentUser, albumToOpen);
		
		ac.setDS(ds);
		ac.setUser(currentUser);

		Scene scene = new Scene(root, 640, 640);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * deleteAlbum
	 * 
	 * Deletes an album.
	 * @param event (ActionEvent)
	 * @throws IOException 
	 */
	public void deleteAlbum(ActionEvent event) throws IOException 
	{
		Album albumSelected = lstAlbums.getSelectionModel().getSelectedItem();
		albumList.remove(albumSelected);
		oblAlbums.remove(albumSelected);
		lstAlbums.setItems(oblAlbums);

		DataStorage.write(ds);
	}

	

	/**
	 * Creates new album for a user, functionally creating a new folder.
	 * 
	 * @param event (ActionEvent)
	 * @throws IOException 
	 */
	public void createAlbum(ActionEvent event) throws IOException 
	{
		String newName = txtCreateAlbum.getText().trim();
		
		if(newName.equals("") || newName == null)
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Album Name Cannot Be Blank");
			alert.setHeaderText("Enter an album name.");
			alert.showAndWait();
			return;
		}
		
		for(Album album : albumList)
		{
			if(album.getName().equals(newName))
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Duplicate Album Name");
				alert.setHeaderText("Use a different name.");
				alert.showAndWait();
				return;
			}
		}
		
		Album newAlbum = new Album(newName);
		
		oblAlbums.add(newAlbum);
		albumList.add(newAlbum);
		
		DataStorage.write(ds);
		

	}

	/**
	 * renameAlbum
	 * 
	 * Rename an album. Album list is then updated accordingly.
	 * @param event (ActionEvent)
	 * @throws IOException
	 */
	public void renameAlbum(ActionEvent event) throws IOException 
	{
		String newAlbumName = txtCreateAlbum.getText().trim();
		
		if(newAlbumName.isEmpty())
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Album name cannot be blank.");
			alert.setHeaderText("Select a new name for your album.");
			alert.showAndWait();
			return;
		}
		
		for(Album album : albumList)
		{
			if(album.getName().equals(newAlbumName))
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Album already exists, please try again.");
				alert.setHeaderText("Select a new name for your album.");
				alert.showAndWait();
				return;
			}
		}
		
		Album albumToRename = lstAlbums.getSelectionModel().getSelectedItem();
		
		albumToRename.setName(newAlbumName);
		lstAlbums.refresh();
		txtCreateAlbum.setText("");
		DataStorage.write(ds);
	}
	
	/**
	 * setDS
	 * 
	 * Sets the DataStorage variable upon opening the usercontroller.
	 * @param ds (DataStorage)
	 */
	public void setDS(DataStorage ds)
	{
		this.ds = ds;
	}
	
	/**
	 * setUser
	 * 
	 * Sets the current user when opening the usercontroller.
	 * @param user (User)
	 */
	public void setUser(User user)
	{
		this.currentUser = user;
	}
	
	/**
	 * refresh
	 * 
	 * Refreshes the displayed albums (for use when creating a new album in a search).
	 */
	public void refresh()
	{
		albumList = currentUser.getAlbumList();
		
		oblAlbums = FXCollections.observableArrayList(albumList);
		
		lstAlbums.setItems(oblAlbums);
	}
	
	/**
	 * switchTabs
	 * 
	 * Saves changes to photo data when switching to the tags tab.
	 * @param event (ActionEvent)
	 * @throws IOException
	 */
	@FXML
	public void switchTabs(ActionEvent event) throws IOException
	{
		DataStorage.write(ds);
	}
	
	/**
	 * logout
	 * 
	 * Graceful logout of the application.
	 * @param event (ActionEvent)
	 */
	public void logout(ActionEvent event) throws IOException 
	{
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
	 * Graceful exit of the application.
	 * @param event (ActionEvent)
	 */
	public void quit(ActionEvent event) 
	{
		Platform.exit();
		System.exit(0);
	}

}
