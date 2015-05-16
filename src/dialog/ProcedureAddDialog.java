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
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import model.Procedure;
import res.ImageResources;

/**
 * A simple dialog with functionality to add new Procedures to the system.
 * 
 * @author Alan Sheehan - R00111909
 */
public class ProcedureAddDialog extends JDialog {

	private static final long serialVersionUID = 4464510166069612805L;
	
	
	private Procedure procedure;
	
	
	private final Dimension labelSize = new Dimension(70, 30);
	
	private JLabel nameLabel, costLabel;
	
	private JTextField nameField, costField;
	
	private JButton addButton, cancelButton;
	
	private JPanel nameContainer, costContainer, buttonContainer;
	
	
	/**
	 * Default constructor.
	 * 
	 * @param parent the parent frame
	 * @param title of the dialog
	 */
	public ProcedureAddDialog(JFrame parent, String title){
		
		super(parent, title);
		
		
		// creating text labels
		nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
		nameLabel.setPreferredSize(labelSize);
		
		costLabel = new JLabel("Cost:", SwingConstants.RIGHT);
		costLabel.setPreferredSize(labelSize);
		
		
		// creating text fields
		nameField = new JTextField(20);
		costField = new JTextField(20);

		
		// creating buttons
		addButton = new JButton("Add");
		addButton.setEnabled(false);
		cancelButton = new JButton("Cancel");
		
		
		// creating component containers
		nameContainer = new JPanel(new FlowLayout());
		costContainer = new JPanel(new FlowLayout());
		buttonContainer = new JPanel(new FlowLayout());
		
		
		// adding components to containers
		nameContainer.add(nameLabel);
		nameContainer.add(nameField);
		
		costContainer.add(costLabel);
		costContainer.add(costField);
		
		buttonContainer.add(addButton);
		buttonContainer.add(cancelButton);
		
		
		// adding containers to frame
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		getContentPane().add(nameContainer);
		getContentPane().add(costContainer);
		getContentPane().add(buttonContainer);
		
		pack();
		setModal(true);
		setResizable(false);
		setListeners();
		setLocationRelativeTo(parent);
		setIconImage(ImageResources.PROCEDURE_ADD_ICON);
		
	}
	
	
	/**
	 * Used to display the dialog. Remains open until the user either 
	 * confirms the creation of a new Procedure or cancels it.
	 * 
	 * @return the procedures if creation confirmed, null otherwise
	 */
	public Procedure showDialog(){
		
		setVisible(true);
		return procedure;
		
	}
	
	
	/**
	 * Checks each text field to see if they are empty. Enables the 
	 * confirm button if each field has text, disables it otherwise.
	 */
	private void validEntry(){
		
		String amount = costField.getText();
		
		if (nameField.getText().isEmpty() ||
				costField.getText().isEmpty()) {
			
			addButton.setEnabled(false);
			
		}
		
		else if (! amount.isEmpty()){
			
			if (Double.parseDouble(amount) > 0){
				addButton.setEnabled(true);
			}
			else {
				addButton.setEnabled(false);
			}

		}
		
		else {
			addButton.setEnabled(true);
		}
		
	}
	
	
	/**
	 * Worker method which checks to see if a decimal point exists
	 * in the given string.
	 * 
	 * @param text to check decimals for
	 * @return true if decimal point exists, false otherwise
	 */
	private boolean existsDecimal(String text){
		
		for (int i = 0 ; i < text.length() ; i++){
			if (text.charAt(i) == '.'){
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Sets up all necessary listeners.
	 */
	private void setListeners(){
		
		// checks to see if text has changed on the text-fields
		nameField.getDocument().addDocumentListener(new TextChangedListener());
		
		
		// stops the user from entering anything into the costField that
		// isn't a number or decimal place, calls validEntry after any change
		// is made
		((AbstractDocument) costField.getDocument()).setDocumentFilter(new DocumentFilter(){
			
			@Override
			public void replace(FilterBypass fb, int offset, int length,
					String text, AttributeSet attrs)
					throws BadLocationException {
				
				// if the character being inserted is a '.' and there's already
				// a decimal point in the amountField, do nothing
				if (text.charAt(0) == '.' && existsDecimal(costField.getText())){
					
				}
				else {
					// regex which removes all characters that aren't a digit of a decimal
					fb.insertString(offset, text.replaceAll("[^\\d.]", ""), attrs);
					validEntry();
				}
			}
			
			@Override
			public void remove(FilterBypass fb, int offset, int length)
					throws BadLocationException {
				
				super.remove(fb, offset, length);
				validEntry();
				
			}
			
		});
		
		
		// returns the newly created Procedure
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				double amount = Double.parseDouble((costField.getText()));
				
				if (procedure == null){
					procedure = new Procedure(nameField.getText(), amount);
				}
				else {
					procedure.setProcedureName(nameField.getText());
					procedure.setProcedureCost(amount);
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
			validEntry();
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			validEntry();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			validEntry();
		}	
		
	}
	
}
