package res;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * A static class containing all images used in the application.
 * 
 * @author Alan Sheehan - R00111909
 */
public class ImageResources {
	
	private static Class<ImageResources> images = ImageResources.class;
	
	// empty image fallback to avoid NullPointerExceptions when images are used
	private static Image fallback = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
	
	public static Image DENTIST_APP_ICON;
	
	public static Image PATIENT_ADD_ICON, PATIENT_EDIT_ICON, PATIENT_REMOVE_ICON;
	
	public static Image PROCEDURE_ADD_ICON, PROCEDURE_EDIT_ICON, PROCEDURE_REMOVE_ICON;
	
	public static Image PAYMENT_ADD_ICON, PAYMENT_EDIT_ICON, PAYMENT_REMOVE_ICON;
	
	
	/**
	 * Loads all of the images when the class is loaded.
	 */
	static {
		
		try {
			
			DENTIST_APP_ICON = ImageIO.read(images.getResource("/res/dentistIcon.png"));
			
			PATIENT_ADD_ICON = ImageIO.read(images.getResource("/res/patientAdd.png"));
			PATIENT_EDIT_ICON = ImageIO.read(images.getResource("/res/patientEdit.png"));
			PATIENT_REMOVE_ICON = ImageIO.read(images.getResource("/res/patientRemove.png"));
			
			PROCEDURE_ADD_ICON = ImageIO.read(images.getResource("/res/procedureAdd.png"));
			PROCEDURE_EDIT_ICON = ImageIO.read(images.getResource("/res/procedureEdit.png"));
			PROCEDURE_REMOVE_ICON = ImageIO.read(images.getResource("/res/procedureRemove.png"));
			
			PAYMENT_ADD_ICON = ImageIO.read(images.getResource("/res/paymentAdd.png"));
			PAYMENT_EDIT_ICON = ImageIO.read(images.getResource("/res/paymentEdit.png"));
			PAYMENT_REMOVE_ICON = ImageIO.read(images.getResource("/res/paymentRemove.png"));
			
		} catch (IOException e){
			
			JOptionPane.showMessageDialog(null, "Failed to load images. \nSystem may not have permission to read the images.", "Error", JOptionPane.ERROR_MESSAGE);
			
			setFallbackImage();
			
		} catch (IllegalArgumentException e){
		
			JOptionPane.showMessageDialog(null, "Failed to load images. \nOne or more images may not be present on system.", "Error", JOptionPane.ERROR_MESSAGE);
			
			setFallbackImage();

		} catch (Exception e){
			
			JOptionPane.showMessageDialog(null, "Failed to load images. \nUnknown error occured.", "Error", JOptionPane.ERROR_MESSAGE);
			
			setFallbackImage();
			
		}
		
	}
	
	
	/**
	 * Sets all of the images in the class to a fallback, translucent image.
	 */
	private static void setFallbackImage (){
		
		PATIENT_ADD_ICON = fallback;
		PATIENT_EDIT_ICON = fallback;
		PATIENT_REMOVE_ICON = fallback;
		
		PROCEDURE_ADD_ICON = fallback;
		PROCEDURE_EDIT_ICON = fallback;
		PROCEDURE_REMOVE_ICON = fallback;
		
		PAYMENT_ADD_ICON = fallback;
		PAYMENT_EDIT_ICON = fallback;
		PAYMENT_REMOVE_ICON = fallback;
		
	}

}
