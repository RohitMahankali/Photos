package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.DataStorage;
import model.Photo;
import model.Tag;
import model.User;

/**
 * @author Michael Belmont
 * @author Rohit Mahankali
 * 
 * Controls the searching functions of the application.
 * 
 */
public class SearchController 
{
	@FXML 
	Button btnSearch;
	
	@FXML
	Button btnCreateFromSearch;
	
	@FXML
	DatePicker dteStart;
	
	@FXML
	DatePicker dteEnd;
	
	@FXML
	TextField txtTag;
	
	@FXML
	ListView<Photo> lstPhotos;
	
	private ObservableList<Photo> oblPhotos;
	
	private User currentUser;
	
	private ArrayList<Photo> photoList;
	
	private DataStorage ds;
	
	/**
	 * start
	 * 
	 * Creates a window with all the photos listed that can be searched through.
	 * @param stage (Stage)
	 */
	public void start(Stage stage) 
	{	
		photoList = getPhotos();
		System.out.println(photoList.size());
		oblPhotos = FXCollections.observableArrayList(photoList);
		
		btnCreateFromSearch.setDisable(photoList.isEmpty());
		
		lstPhotos.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>()
		{
			
			@Override
			public ListCell<Photo> call(ListView<Photo> p) 
			{
				return new PreviewCell();
			}
		});	
		
		lstPhotos.setItems(oblPhotos);
		
	}
	
	/**
	 * getPhotos
	 * 
	 * Returns the list of all photos in a user's account.
	 * @return all of the photos (ArrayList)
	 */
	public ArrayList<Photo> getPhotos() 
	{
		ArrayList<Photo> all = new ArrayList<Photo>();
		ArrayList<Album> albumList = currentUser.getAlbumList();
		
		for(int i = 0; i < albumList.size(); i++)
		{
			for (Photo photo : albumList.get(i).getPhotos())
			{
				if (!all.contains(photo))
				{
					all.add(photo);
				}
			}
		}
		
		return all;
	}
	
	/**
	 * createFromSearch
	 * 
	 * Creates a photo album from the search results.
	 * @param event (ActionEvent)
	 * @throws IOException
	 */
	@FXML
	protected void createFromSearch(ActionEvent event) throws IOException 
	{
		Dialog<Album> d = new Dialog<>();
		d.setTitle("Create an Album");
		d.setHeaderText("Add the searched photos into an album.");
		d.setResizable(true);
		   
		Label albumLabel = new Label("Enter a name: ");
		TextField albumTextField = new TextField();
		albumTextField.setPromptText("New Name");
		   
		GridPane grid = new GridPane();
		grid.add(albumLabel, 1, 1);
		grid.add(albumTextField, 2, 1);
		   
		d.getDialogPane().setContent(grid);
		   
		ButtonType btnApprove = new ButtonType("Add", ButtonData.OK_DONE);
		d.getDialogPane().getButtonTypes().add(btnApprove);
		   
		d.setResultConverter(new Callback<ButtonType, Album>() 
		{
			@Override
			public Album call(ButtonType b) 
			{
				if (b == btnApprove) 
				{
					   
					String error = exists(albumTextField.getText());
					   
					if (error != null)
					{
						return null;
					}
											   
					return new Album(albumTextField.getText().trim());
				}
				return null;
			}

			
		});
		   
		Album newAlbum = null;
		
		Optional<Album> confirm = d.showAndWait();
		   
		if (confirm.isPresent()) 
		{
			newAlbum = (Album) confirm.get();
			currentUser.getAlbumList().add(newAlbum);
			
			for (Photo photo : photoList) 
			{
				newAlbum.addPhoto(photo);
			}
			
			DataStorage.write(ds);
		}
	}
	
	/**
	 * exists
	 * 
	 * Checks to see if an album name already exists.
	 * @param newName (String)
	 * @return error message (String)
	 */
	private String exists(String newName) 
	{
		if (newName.trim().isEmpty())
		{
			return "You must enter an album name.";
		}
		
		if (currentUser.nameCheck(newName))
		{
			return newName + " is already in use, please try again.";
		}
		
		else
			return null;
	}
	
	/**
	 * search
	 * 
	 * Searches through the photos on button press.
	 * @param event (ActionEvent)
	 */
	@FXML
	protected void search(ActionEvent event) 
	{
		ArrayList<Photo> library = getPhotos();
		LocalDate start, end;
		String tagSearch = txtTag.getText();
		
		if ((tagSearch.isEmpty()) && dteStart.getValue() == null && dteEnd.getValue() == null) 
		{
			return;
		}
			
		if (dteStart.getValue() == null)
		{
			start = LocalDate.MIN;
		}
		
		else
		{
			start = dteStart.getValue();
		}
		
		if (dteEnd.getValue() == null)
		{
			end = LocalDate.MAX;
		}
		else
		{
			end = dteEnd.getValue();
		}
		
		photoList.clear();
		oblPhotos.clear();
		
		ArrayList<Tag> tagList = new ArrayList<Tag>();
		
		boolean and = false;
		boolean or = false;
		
		if(!tagSearch.isEmpty())
		{
		
			String[] tagsToSearch = tagSearch.trim().split("AND");
			if (tagsToSearch.length == 2)
				and = true;
			else 
			{
				tagsToSearch = tagSearch.trim().split("OR");
				if (tagsToSearch.length == 2)
					or = true;
			}
		
			String[] searchable;
		
			if(tagsToSearch.length == 1)
			{
				searchable = tagsToSearch[0].split("=");
			}
			else
			{
				searchable = new String[4];
				
				String[] condition1 = tagsToSearch[0].trim().split("=");
				String[] condition2 = tagsToSearch[1].trim().split("=");
			
				searchable[0] = condition1[0];
				searchable[1] = condition1[1];
				searchable[2] = condition2[0];
				searchable[3] = condition2[1];
			}
			
			tagList.add(new Tag(searchable[0], searchable[1]));
			
			if(and || or)
			{
				tagList.add(new Tag(searchable[2], searchable[3]));
			}
		}
		
		for (Photo photo : library) 
		{
			if (tagList.isEmpty()) 
			{
				if (photo.checkDateTime(start, end)) 
				{
					photoList.add(photo);
					oblPhotos.add(photo);
				}
			}
			
			else 
			{
				if (and == true && photo.checkDateTime(start, end)) 
				{
					ArrayList<Tag> photoTags = photo.getTagList();
					
					
					for(Tag currentPhotoTag : photoTags)
					{
						if(tagList.get(0).equals(currentPhotoTag))
						{
							for(Tag secondTag : photoTags)
							{
								if(tagList.get(1).equals(secondTag))
								{
									photoList.add(photo);
									oblPhotos.add(photo);
									break;
								}
							}
						}
					}
				}
			
				
				else if (photo.checkDateTime(start, end)) 
				{
					ArrayList<Tag> photoTags = photo.getTagList();
					
					for(Tag currentTag : tagList)
					{
						for(Tag currentPhotoTag : photoTags)
						{
							if(currentTag.equals(currentPhotoTag))
							{
								photoList.add(photo);
								oblPhotos.add(photo);
								break;
							}
						}
					}
				}
			}
		}
		
		lstPhotos.setItems(oblPhotos);
		
		btnCreateFromSearch.setDisable(photoList.isEmpty());
		
	}
	
	/**
	 * PreviewCell
	 * 
	 * For showing a thumbnail in the photo list.
	 */
	private class PreviewCell extends ListCell<Photo> 
	{
			
		AnchorPane pneAnchor = new AnchorPane();
		StackPane pneStack = new StackPane();
		
		ImageView imgImage = new ImageView();
		Label lblCaption = new Label();
			
		public PreviewCell() 
		{
			super();
		
			imgImage.setFitWidth(25.0);
			imgImage.setFitHeight(25.0);
			imgImage.setPreserveRatio(true);
			
			StackPane.setAlignment(imgImage, Pos.CENTER);
			
			pneStack.getChildren().add(imgImage);
			
			pneStack.setPrefHeight(25);
			pneStack.setPrefWidth(25.0);
			
			AnchorPane.setLeftAnchor(pneStack, 0.0);
			
			AnchorPane.setLeftAnchor(lblCaption, 60.0);
			AnchorPane.setTopAnchor(lblCaption, 0.0);
			pneAnchor.getChildren().addAll(pneStack, lblCaption);
			
			pneAnchor.setPrefHeight(25.0);
			
			lblCaption.setMaxWidth(300.0);
			
			setGraphic(pneAnchor);
		}
		
		@Override
		public void updateItem(Photo photo, boolean empty) 
		{
			super.updateItem(photo, empty);
			setText(null);
			if(photo == null)
			{
				imgImage.setImage(null);
				lblCaption.setText("");
			}
			if (photo != null) 
			{
				imgImage.setImage(photo.getPhoto());
				lblCaption.setText(photo.getCaption());
			}	
		}

	}
	
	public void initialize(DataStorage ds, User user)
	{
		this.ds = ds;
		this.currentUser = user;
	}
	
	/**
	 * setUser
	 * 
	 * Sets the current user for the class
	 * @param user (User)
	 */
	public void setUser(User user) 
	{
		this.currentUser = user;
	}
	
	/**
	 * setDS
	 * 
	 * Sets the DataStorage for the class.
	 * @param ds (DataStorage)
	 */
	public void setDS(DataStorage ds)
	{
		this.ds = ds;
	}
	
}