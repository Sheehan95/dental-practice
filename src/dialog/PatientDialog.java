package dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.Patient;
import res.ImageResources;

/**
 * A simple dialog with functionality to create and edit Patient objects.
 * 
 * @author Alan Sheehan - R00111909
 */
public class PatientDialog extends JDialog {

	private static final long serialVersionUID = 4464510166069612805L;
	
	
	private Patient patient;
	
	
	private final Dimension labelSize = new Dimension(70, 30);
	
	private JLabel nameLabel, addressLabel, numberLabel;
	
	private JTextField nameField, addressField, numberField;
	
	private JButton confirmButton, cancelButton;
	
	private JPanel nameContainer, addressContainer, numberContainer, buttonContainer;
	
	
	/**
	 * Default constructor.
	 * 
	 * @param parent the parent frame
	 * @param title of the dialog
	 */
	public PatientDialog(JFrame parent, String title){
		
		super(parent, title);
		
		// creating text labels
		nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
		nameLabel.setPreferredSize(labelSize);
		
		addressLabel = new JLabel("Address:", SwingConstants.RIGHT);
		addressLabel.setPreferredSize(labelSize);
		
		numberLabel = new JLabel("Phone:", SwingConstants.RIGHT);
		numberLabel.setPreferredSize(labelSize);
		
		
		// creating text fields
		nameField = new JTextField(20);
		addressField = new JTextField(20);
		numberField = new JTextField(20);

		
		// creating buttons
		confirmButton = new JButton("Confirm");
		confirmButton.setEnabled(false);
		cancelButton = new JButton("Cancel");
		
		
		// creating component containers
		nameContainer = new JPanel(new FlowLayout());
		addressContainer = new JPanel(new FlowLayout());
		numberContainer = new JPanel(new FlowLayout());
		buttonContainer = new JPanel(new FlowLayout());
		
		
		// adding components to containers
		nameContainer.add(nameLabel);
		nameContainer.add(nameField);
		
		addressContainer.add(addressLabel);
		addressContainer.add(addressField);
		
		numberContainer.add(numberLabel);
		numberContainer.add(numberField);
		
		buttonContainer.add(confirmButton);
		buttonContainer.add(cancelButton);
		
		
		// adding containers to frame
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		getContentPane().add(nameContainer);
		getContentPane().add(addressContainer);
		getContentPane().add(numberContainer);
		getContentPane().add(buttonContainer);
		
		pack();
		setModal(true);
		setResizable(false);
		setListeners();
		setLocationRelativeTo(parent);
		
	}
	
	
	/**
	 * Used to display the dialog. Remains open until the user either 
	 * confirms the creation of a new patient or cancels it.
	 * 
	 * @return the patient if creation confirmed, null otherwise
	 */
	public Patient showDialog(){
		
		setIconImage(ImageResources.PATIENT_ADD_ICON);
		
		setVisible(true);
		return patient;
		
	}
	
	
	/**
	 * Used to display the dialog when editing a patient. Fills the 
	 * text fields with that patient's data. Remains open until the
	 * user either confirms the edit or cancels it.
	 * 
	 * @param patient to be edited
	 * @return the patient edited
	 */
	public Patient showDialog(Patient patient){
		
		setIconImage(ImageResources.PATIENT_EDIT_ICON);
		
		this.patient = patient;
		
		nameField.setText(patient.getPatientName());
		addressField.setText(patient.getPatientAddress());
		numberField.setText(patient.getPatientPhone());
		
		setVisible(true);
		return this.patient;
	}
	
	
	/**
	 * Checks each text field to see if they are empty. Enables the 
	 * confirm button if all fields have text, disables it otherwise.
	 */
	private void checkForChange(){
		
		if (nameField.getText().equals("") ||
				addressField.getText().equals("") ||
				numberField.getText().equals("")) {
			
			confirmButton.setEnabled(false);
			
		}
		else {
			confirmButton.setEnabled(true);
		}
		
	}
	
	
	/**
	 * Sets up all necessary listeners.
	 */
	private void setListeners(){
		
		// checks to see if text has changed on the text-fields
		nameField.getDocument().addDocumentListener(new TextChangedListener());
		addressField.getDocument().addDocumentListener(new TextChangedListener());
		numberField.getDocument().addDocumentListener(new TextChangedListener());
		
		
		// if a Patient object was supplied to the dialog, applies the changes to that Patient
		// otherwise, creates a new Patient object with the given details
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (patient == null){
					patient = new Patient(nameField.getText(), addressField.getText(), numberField.getText());
				}
				else {
					patient.setPatientName(nameField.getText());
					patient.setPatientAddress(addressField.getText());
					patient.setPatientPhone(numberField.getText());
				}
				
				setVisible(false);
				dispose();
				
			}
			
		});
		
		
		// closes the dialog
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				setVisible(false);
				dispose();
				
			}
			
		});
		
	}
	
	
	/**
	 * Checks if the field is empty.
	 * 
	 * @author Alan Sheehan - R00111909
	 */
	private class TextChangedListener implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			checkForChange();
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			checkForChange();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			checkForChange();
		}	
		
	}
	
}
