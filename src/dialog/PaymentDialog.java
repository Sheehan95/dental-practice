package dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import model.Payment;
import res.ImageResources;

/**
 * A simple dialog with functionality to create and edit Payment objects.
 * 
 * @author Alan Sheehan - R00111909
 */
public class PaymentDialog extends JDialog {

	private static final long serialVersionUID = 4464510166069612805L;
	
	
	private Payment payment;
	private Date date;
	
	
	private final Dimension labelSize = new Dimension(70, 30);
	
	private JLabel amountLabel, statusLabel, dateLabel;
	
	private JTextField amountField, dateField;
	
	private JPanel statusField;
	
	private JCheckBox isPaid;
	
	private JButton confirmButton, cancelButton;
	
	private JPanel amountContainer, isPaidContainer, dateContainer, buttonContainer;

	
	/**
	 * Default constructor.
	 * 
	 * @param parent the parent frame
	 * @param title of the dialog
	 */
	public PaymentDialog(JFrame parent, String title){
		
		super(parent, title);
		
		
		// creating text labels
		amountLabel = new JLabel("Amount:", SwingConstants.RIGHT);
		amountLabel.setPreferredSize(labelSize);
		
		statusLabel = new JLabel("Status:", SwingConstants.RIGHT);
		statusLabel.setPreferredSize(labelSize);
		
		dateLabel = new JLabel("Date:", SwingConstants.RIGHT);
		dateLabel.setPreferredSize(labelSize);
		
		
		// creating text fields
		amountField = new JTextField(20);
		
		dateField = new JTextField(20);
		dateField.setEnabled(false);
		
		// creating checkbox
		statusField = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		isPaid = new JCheckBox("UnPaid");
		isPaid.setHorizontalTextPosition(SwingConstants.LEFT);
		statusField.setPreferredSize(new Dimension(224, 20));
		statusField.add(isPaid);

		
		// creating buttons
		confirmButton = new JButton("Confirm");
		confirmButton.setEnabled(false);
		cancelButton = new JButton("Cancel");
		
		
		// creating component containers
		amountContainer = new JPanel(new FlowLayout());
		isPaidContainer = new JPanel(new FlowLayout());
		dateContainer = new JPanel(new FlowLayout());
		buttonContainer = new JPanel(new FlowLayout());
		
		
		// adding components to containers
		amountContainer.add(amountLabel);
		amountContainer.add(amountField);
		
		isPaidContainer.add(statusLabel);		
		isPaidContainer.add(statusField);
		
		dateContainer.add(dateLabel);
		dateContainer.add(dateField);
		
		buttonContainer.add(confirmButton);
		buttonContainer.add(cancelButton);
		
		
		// adding containers to frame
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		getContentPane().add(amountContainer);
		getContentPane().add(isPaidContainer);
		getContentPane().add(dateContainer);
		getContentPane().add(buttonContainer);
		
		pack();
		setModal(true);
		setResizable(false);
		setListeners();
		setLocationRelativeTo(parent);
		
	}
	
	
	/**
	 * Used to display the dialog. Remains open until the user either 
	 * confirms the creation of a new  or cancels it.
	 * 
	 * @return the Payment if creation confirmed, null otherwise
	 */
	public Payment showDialog(){
		
		setIconImage(ImageResources.PAYMENT_ADD_ICON);
		
		date = new Date();
		dateField.setText(date.toString());
		isPaid.setSelected(false);
		
		setVisible(true);
		return payment;
		
	}
	
	
	/**
	 * Used to display the dialog when editing a payment. Fills the 
	 * text fields with that payment's data. Remains open until the
	 * user either confirms the edit or cancels it.
	 * 
	 * @param payment to be edited
	 * @return the payment edited
	 */
	public Payment showDialog(Payment payment){
		
		setIconImage(ImageResources.PAYMENT_EDIT_ICON);
		
		this.payment = payment;
		
		date = payment.getPaymentDate();
		
		amountField.setText(Double.toString(payment.getPaymentAmount()));
		isPaid.setSelected(payment.getPaymentStatus());
		dateField.setText(date.toString());
		
		setVisible(true);
		return this.payment;
		
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
	 * Worker method which ensures the entry is a valid double and
	 * is greater than 0.
	 */
	private void validEntry(){
		
		String amount = amountField.getText();
		
		if (! amount.isEmpty()){
			
			if (Double.parseDouble(amount) > 0){
				confirmButton.setEnabled(true);
			}
			else {
				confirmButton.setEnabled(false);
			}

		}
		else {
			confirmButton.setEnabled(false);
		}
		
	}
	
	
	/**
	 * Sets up all necessary listeners.
	 */
	private void setListeners(){
		
		// stops the user from entering anything into the amountField that
		// isn't a number or decimal place, calls validEntry after any change
		// is made
		((AbstractDocument) amountField.getDocument()).setDocumentFilter(new DocumentFilter(){
			
			@Override
			public void replace(FilterBypass fb, int offset, int length,
					String text, AttributeSet attrs)
					throws BadLocationException {
				
				// if the character being inserted is a '.' and there's already
				// a decimal point in the amountField, do nothing
				if (text.charAt(0) == '.' && existsDecimal(amountField.getText())){
					
				}
				else {
					// regex which removes all characters that aren't a digit or a decimal
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
		
		
		// changes the text displayed next to the checkbox depending on the state
		// of the checkbox
		isPaid.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				if (isPaid.isSelected()){
					isPaid.setText("Paid");
				}
				else {
					isPaid.setText("UnPaid");
				}
				
			}
			
		});

		
		// if a Payment object was supplied to the dialog, applies the changes to that Payment
		// otherwise, creates a new Payment object with the given details
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				double amount = Double.parseDouble(amountField.getText());
				
				if (payment == null){
					payment = new Payment(amount, isPaid.isSelected());
					payment.setPaymentDate(date);
				}
				else {
					payment.setPaymentAmount(amount);
					payment.setPaymentDate(date);
					payment.setPaymentStatus(isPaid.isSelected());
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
	
}
