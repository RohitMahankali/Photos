package model;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * SerializedPhoto
 * 
 * A serialized photo that takes a photo and converts it into serialized data, preserving the image.
 * 
 * @author Michael Belmont
 * @author Rohit Mahankali
 */
public class SerializedPhoto implements Serializable 
{
	private int hei, wid;
	private int[][] photoLayout;
	
	/**
	 * Object Constructor
	 * 
	 * Creates a SerializedPhoto object.
	 */
	public SerializedPhoto() 
	{
	}
	
	/**
	 * deconstruct
	 * 
	 * Takes an Image object and uses Java PixelReader to get each individual pixel so the image can be recreated.
	 * @param image (Image)
	 */
	public void deconstruct(Image image) 
	{
		wid = ((int) image.getWidth());
		hei = ((int) image.getHeight());
		
		photoLayout = new int[wid][hei];
		
		PixelReader deconstructor = image.getPixelReader();
		
		for (int w = 0; w < wid; w++)
			for (int h = 0; h < hei; h++)
				photoLayout[w][h] = deconstructor.getArgb(w, h);
	}
	
	/**
	 * construct
	 * 
	 * Takes the deconstructed image stored in this object and builds it into a constructed image using PixelWriter.
	 * @return constructed image (Image)
	 */
	public Image construct() 
	{
		WritableImage constructedImage = new WritableImage(wid, hei);
		
		PixelWriter constructor = constructedImage.getPixelWriter();
		
		for (int i = 0; i < wid; i++)
		{
			for (int j = 0; j < hei; j++)
			{
				constructor.setArgb(i, j, photoLayout[i][j]);
			}
		}
		
		return constructedImage;
	}
	
	/**
	 * getWidth
	 * 
	 * Returns the width of the photo.
	 * @return wid (int)
	 */
	public int getWidth() 
	{
		return wid;
	}
	
	/**
	 * getHeight
	 * 
	 * Returns the height of the photo.
	 * @return hei (int)
	 */
	public int getHeight() 
	{
		return hei;
	}
	
	/**
	 * getPhotoLayout
	 * 
	 * Returns the photoLayout variable, an array containing the deconstructed image in the form of individual pixels.
	 * @return photoLayout (int[][])
	 */
	public int[][] getPhotoLayout() 
	{
		return photoLayout;
	}
	
	/**
	 * equals
	 * 
	 * Overrides the default Object equals method by checking each individual pixel against each other.
	 * @param otherPhoto (SerializedPhoto)
	 * @return equal (true), not equal (false)
	 */
	public boolean equals(SerializedPhoto otherPhoto) 
	{
		if (wid != otherPhoto.getWidth())
			return false;
		if (hei != otherPhoto.getHeight())
			return false;
		for (int i = 0; i < wid; i++)
			for (int j = 0; j < hei; j++)
				if (photoLayout[i][j] != otherPhoto.getPhotoLayout()[i][j])
					return false;
		return true;
	}
	
}