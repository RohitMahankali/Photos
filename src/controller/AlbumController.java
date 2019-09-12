package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
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
 * This is the primary controller for functions inside of an album.
 * Used when an album is selected.
 * 
 * The currentUser is able to:
 * 	Add photos to the album.
 * 	Delete photos from the album.
 * 	Add or change captions on the photos.
 * 	Add or delete tags on photos.
 * 	Copy or move photos into other albums.
 * 
 */
public class AlbumController 
{
	@FXML
	Button btnPhotoDelete;
	
	@FXML
	Button btnPhotoAdd;
	
	@FXML
	Button btnTagAdd;
	
	@FXML
	Button btnTagDelete;

	@FXML
	Button btnPrevious;
	
	@FXML
	Button btnNext;
	
	@FXML
	ImageView imgPhotoDisplay;

	@FXML
	ListView<Photo> lstPhotos;
	
	ObservableList<Photo> oblPhotos = FXCollections.observableArrayList();

	ArrayList<Photo> photos;
	
	@FXML
	TableView<Tag> tblTags;
	
	@FXML
	TableColumn<Tag,String> tbcTagNames;
	
	@FXML
	TableColumn<Tag,String> tbcTagValues;
	
	ObservableList<Tag> oblTags = FXCollections.observableArrayList();
	
	@FXML
	TextField txtPhotoName;
	
	@FXML
	TextField txtPhotoDateTime;
	
	@FXML
	TextField txtPhotoCaption;

	@FXML
	TextField txtAlbumName;
	
	@FXML
	TextField txtTagName;
	
	@FXML
	TextField txtTagValue;
	
	@FXML
	Tab tabTags;
	
	@FXML
	TabPane tbpTabs;

	private User currentUser;
	private Album currentAlbum;
	private int currentIndex;
	
	DataStorage ds = new DataStorage();

	/**
	 * Gets albums and photos from the current user.
	 * 
	 * @param currentUser
	 * @param currentAlbum
	 */
	public void initializeAlbum(User currentUser, Album currentAlbum) 
	{
		this.currentUser = currentUser;
		this.currentAlbum = currentAlbum;
		
		txtAlbumName.setText(currentAlbum.getName());
		
		photos = currentAlbum.getPhotos();
		oblPhotos = FXCollections.observableArrayList(photos);

		lstPhotos.setCellFactory(
				new Callback<ListView<Photo>, ListCell<Photo>>()
				{
					@Override
					public ListCell<Photo> call(ListView<Photo> photo)
					{
						return new PreviewCell();
					}
				}
		);
		
		lstPhotos.setItems(oblPhotos);
		
		tbpTabs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() 
		{

		    @Override
		    public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab)
		    {
		        try {
					DataStorage.write(ds);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
		
		currentIndex = 0;
		
		createImageView();
	}

	/**
	 * Makes an ImageView from a photo file.
	 * 
	 * @param photoFile
	 * @return 
	 */
	private void createImageView() 
	{
		if(photos.size() == 0)
			return;
		
		txtPhotoCaption.setText(photos.get(currentIndex).getCaption());
		txtPhotoDateTime.setText(photos.get(currentIndex).calendarToString());
		txtPhotoName.setText(currentAlbum.getPhotoByIndex(currentIndex).getName());
		txtPhotoDateTime.setEditable(false);
		txtAlbumName.setEditable(false);
		
		oblTags = FXCollections.observableArrayList(photos.get(currentIndex).getTagList());
		
		tbcTagNames.setCellValueFactory(new Callback<CellDataFeatures<Tag, String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Tag, String> tag) 
			{
				
				return new SimpleStringProperty(tag.getValue().getName());
			}
		});	
		
		tbcTagValues.setCellValueFactory(new Callback<CellDataFeatures<Tag, String>, ObservableValue<String>>()
		{
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Tag, String> tag) 
			{
				
				return new SimpleStringProperty(tag.getValue().getValue());
			}
		});	
		
		tblTags.setItems(oblTags);
		
		btnPrevious.setDisable(currentIndex == 0);
		btnNext.setDisable(currentIndex == photos.size()-1);
		
		photos = currentAlbum.getPhotos();
		oblPhotos = FXCollections.observableArrayList(photos);
		
		lstPhotos.setCellFactory(
				new Callback<ListView<Photo>, ListCell<Photo>>()
				{
					@Override
					public ListCell<Photo> call(ListView<Photo> photo)
					{
						return new PreviewCell();
					}
				}
		);
		
		
		
		lstPhotos.setItems(oblPhotos);
		
		imgPhotoDisplay.setImage(photos.get(currentIndex).getPhoto());
	}

	/**
	 * addPhoto
	 * 
	 * Adds a photo to the album.
	 * @param event (ActionEvent)
	 * @throws IOException
	 */
	@FXML
	private void addPhoto(ActionEvent event) throws IOException 
	{
		FileChooser selector = new FileChooser();
		
		FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPGs", "*.JPG");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNGs", "*.PNG");
       	selector.getExtensionFilters().addAll(jpgFilter, pngFilter);
       	
		selector.setTitle("Choose a photo to upload: ");
		
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		
		File file = selector.showOpenDialog(stage);
		
		if (file == null)
			return;
		
        BufferedImage bI = ImageIO.read(file);
        Image image = SwingFXUtils.toFXImage(bI, null);
        Photo temp = new Photo(image);
        
        for (Photo photo : currentAlbum.getPhotos()) 
        {
        	if (temp.getSerializedPhoto().equals(photo.getSerializedPhoto())) 
        	{
        		Alert alert = new Alert(AlertType.WARNING);
    			alert.setTitle("DUPLICATE");
    			alert.setContentText("Photo already exists");
    			alert.showAndWait();
        		return;
        	}
        }
        
        Photo newPhoto = null;
        boolean existing = false;
        
        for (Album album : currentUser.getAlbumList()) 
        {
        	for (Photo photo : album.getPhotos()) 
        	{
        		if (image.equals(photo.getPhoto())) 
        		{
        			newPhoto = photo;
        			existing = true;
        			break;
        		}
        		if (existing)
        			break;
        	}
        }
        
        if (!existing)
        	newPhoto = new Photo(image);
        
        currentAlbum.addPhoto(newPhoto);
        oblPhotos.add(newPhoto);
        
        currentIndex = currentAlbum.getSize()-1;
        
        createImageView();
        
        DataStorage.write(ds);
	}
	
	/**
	 * movePhoto
	 * 
	 * Moves a photo to another album.
	 * @param event (ActionEvent)
	 * @throws IOException
	 */
	@FXML
	public void movePhoto(ActionEvent event) throws IOException
	{
		Photo currentPhoto = lstPhotos.getSelectionModel().getSelectedItem();

		Dialog<Album> dialog = new Dialog<>();
		dialog.setTitle("Move Photo");
		dialog.setHeaderText("Move this photo to another album?");
		dialog.setResizable(true);
		   
		Label lblMove = new Label("Select the album:");
		
		List<String> albums = new ArrayList<String>();
		
		for(Album album : currentUser.getAlbumList())
		{
			String name = album.getName();
			
			if(album.getName() != currentAlbum.getName())
			{
				albums.add(name);
			}
		}
		
		ObservableList<String> oblSelectableAlbums = FXCollections.observableArrayList(albums);
		
		ComboBox<String> cmbSelection = new ComboBox<String>(oblSelectableAlbums);
		   
		GridPane grid = new GridPane();
		grid.add(lblMove, 1, 1);
		grid.add(cmbSelection, 1, 2);
		   
		dialog.getDialogPane().setContent(grid);
		   
		ButtonType buttonTypeOk = new ButtonType("Move", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		
		Optional<Album> result = dialog.showAndWait();
		 
		if (result.isPresent()) 
		{
			String newName = cmbSelection.getSelectionModel().getSelectedItem();
			Album newAlbum = currentUser.getAlbum(newName);
			
			newAlbum.addPhoto(currentPhoto);
			
			oblPhotos.remove(currentPhoto);
			currentAlbum.deletePhoto(currentIndex);
			
			if (currentAlbum.getSize() == 0)
			{
				Platform.exit();
			}
			
			currentIndex=0;
			createImageView();

			DataStorage.write(ds);
		}
	}
	
	/**
	 * deletePhoto
	 * 
	 * Deletes the currently selected photo.
	 * @param event (ActionEvent)
	 * @throws IOException
	 */
	@FXML
	public void deletePhoto(ActionEvent event) throws IOException
	{
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Delete this photo?");
		
		Optional<ButtonType> confirm = alert.showAndWait();
		
		if(confirm.isPresent())
		{
			currentAlbum.deletePhoto(currentIndex);
			
			System.out.println(currentAlbum.getSize());
			
			oblPhotos.remove(currentIndex);
		}
		
		currentIndex = 0;
		
		if(currentAlbum.getSize() == 0)
		{
			Stage stageToClose = (Stage) btnPhotoDelete.getScene().getWindow();
			stageToClose.hide();
			
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/User.fxml"));
			
			Parent root = (Parent) loader.load();
			
			UserController uc = loader.<UserController>getController();
			
			uc.setDS(ds);
			uc.setUser(currentUser);

			Scene scene = new Scene(root, 640, 640);
			stage.setScene(scene);
			uc.start(stage);
			stage.show();
		}
		
		createImageView();
		
		DataStorage.write(ds);
	}
	
	/**
	 * addTag
	 * 
	 * Adds a tag to a photo.
	 * @param event (ActionEvent)
	 * @throws IOException
	 */
	@FXML
	public void addTag(ActionEvent event) throws IOException
	{
		if(txtTagName.getText().isEmpty() || txtTagValue.getText().isEmpty())
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Empty");
			alert.setContentText("Need both a tag name AND value");
			alert.showAndWait();
    		return;
		}
		
		currentAlbum.getPhotoByIndex(currentIndex).addTag(txtTagName.getText(), txtTagValue.getText());
		
		createImageView();
		
		DataStorage.write(ds);
	}
	
	/**
	 * deleteTag
	 * 
	 * Deletes a tag from a photo.
	 * @param event (ActionEvent)
	 * @throws IOException
	 */
	@FXML
	public void deleteTag(ActionEvent event) throws IOException
	{
		
		Photo currentPhoto = photos.get(currentIndex);
		if(!(currentPhoto.getTagList().size() == 0))
		{	
			Tag tagToDelete = tblTags.getSelectionModel().getSelectedItem();
		
			for(int i = 0; i < currentPhoto.getTagList().size(); i++)
			{
				if(tagToDelete.equals(currentPhoto.getTag(i)))
				{
					currentPhoto.deleteTag(i);
					createImageView();
				
					DataStorage.write(ds);
					break;
				}
			}
		}
	}
	
	/**
	 * selectPhoto
	 * 
	 * Selects the photo selected in the listview.
	 */
	@FXML
	public void selectPhoto()
	{
		currentIndex = currentAlbum.getIndexOfPhoto(lstPhotos.getSelectionModel().getSelectedItem());
		createImageView();
	}
	
	/**
	 * nextPhoto
	 * 
	 * Goes to the next photo in the list.
	 */
	@FXML
	public void nextPhoto()
	{
		refresh();
		currentIndex++;
		createImageView();
	}
	
	/**
	 * previousPhoto
	 * 
	 * Goes to the previous photo in the list.
	 */
	@FXML
	public void previousPhoto()
	{
		refresh();
		currentIndex--;
		createImageView();
	}
	
	/**
	 * refresh
	 * 
	 * Applies any updates to fields changed by the user on a photo.
	 */
	public void refresh()
	{
		Photo currentPhoto = currentAlbum.getPhotoByIndex(currentIndex);
		
		currentPhoto.changeCaption(txtPhotoCaption.getText());
		currentPhoto.setName(txtPhotoName.getText());
	}
	
	/**
	 * setDS
	 * 
	 * Sets the DataStorage variable when opening the AlbumController.
	 * @param ds (DataStorage)
	 */
	public void setDS(DataStorage ds)
	{
		this.ds = ds;
	}
	
	/**
	 * setUser
	 * 
	 * Sets the current user when opening the AlbumController.
	 * @param user (User)
	 */
	public void setUser(User user)
	{
		this.currentUser = user;
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
			
			pneStack.setPrefHeight(25.0);
			pneStack.setPrefWidth(25.0);
			
			AnchorPane.setLeftAnchor(pneStack, 0.0);
			
			AnchorPane.setLeftAnchor(lblCaption, 30.0);
			AnchorPane.setTopAnchor(lblCaption, 0.0);
			pneAnchor.getChildren().addAll(pneStack, lblCaption);
			
			pneAnchor.setPrefHeight(25.0);
			
			lblCaption.setMaxWidth(300.0);
			
			setGraphic(pneAnchor);
		}
		
		@Override
		public void updateItem(Photo photo, boolean empty) {
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
}
