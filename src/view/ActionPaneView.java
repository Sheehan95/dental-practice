package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import res.ImageResources;

/**
 * The action pane holds most of the functionality of the program. Buttons for adding, editing
 * & removing patients, procedures & payments are contained within this panel.
 * 
 * @author Alan Sheehan - R00111909
 */
public class ActionPaneView extends JPanel {
	
	private static final long serialVersionUID = -4556823102029124599L;
	
	
	private JButton addPatientButton, editPatientButton, removePatientButton;
	private JButton addProcedureButton, editProcedureButton, removeProcedureButton;
	private JButton addPaymentButton, editPaymentButton, removePaymentButton;
	
	private JPanel patientPanel, procedurePanel, paymentPanel, procedurePaymentContainer;
	
	private CardLayout cardContainerLayout;
	
	private Dimension buttonSize = new Dimension(175, 30);
	
	private Border containerSpacing = BorderFactory.createEmptyBorder(10, 0, 15, 0);
	
	public final static String PROCEDURE_CARD = "Procedure";
	public final static String PAYMENT_CARD = "Payment";
	
	
	/**
	 * Default constructor.
	 */
	public ActionPaneView() {

		// creating Patient related buttons
		addPatientButton = new JButton("Add Patient");
		addPatientButton.setPreferredSize(buttonSize);
		addPatientButton.setIcon(new ImageIcon(ImageResources.PATIENT_ADD_ICON));
		
		editPatientButton = new JButton("Edit Patient");
		editPatientButton.setPreferredSize(buttonSize);
		editPatientButton.setEnabled(false);
		editPatientButton.setIcon(new ImageIcon(ImageResources.PATIENT_EDIT_ICON));
		
		removePatientButton = new JButton("Remove Patient");
		removePatientButton.setPreferredSize(buttonSize);
		removePatientButton.setEnabled(false);
		removePatientButton.setIcon(new ImageIcon(ImageResources.PATIENT_REMOVE_ICON));
		
		
		// creating Procedure related buttons
		addProcedureButton = new JButton("Add Procedure");
		addProcedureButton.setPreferredSize(buttonSize);
		addProcedureButton.setEnabled(false);
		addProcedureButton.setIcon(new ImageIcon(ImageResources.PROCEDURE_ADD_ICON));
		
		editProcedureButton = new JButton("Edit Procedure");
		editProcedureButton.setPreferredSize(buttonSize);
		editProcedureButton.setEnabled(false);
		editProcedureButton.setIcon(new ImageIcon(ImageResources.PROCEDURE_EDIT_ICON));
		
		removeProcedureButton = new JButton("Remove Procedure");
		removeProcedureButton.setPreferredSize(buttonSize);
		removeProcedureButton.setEnabled(false);
		removeProcedureButton.setIcon(new ImageIcon(ImageResources.PROCEDURE_REMOVE_ICON));
		
		
		// creating Payment related buttons
		addPaymentButton = new JButton("Add Payment");
		addPaymentButton.setPreferredSize(buttonSize);
		addPaymentButton.setEnabled(false);
		addPaymentButton.setIcon(new ImageIcon(ImageResources.PAYMENT_ADD_ICON));
		
		editPaymentButton = new JButton("Edit Payment");
		editPaymentButton.setPreferredSize(buttonSize);
		editPaymentButton.setEnabled(false);
		editPaymentButton.setIcon(new ImageIcon(ImageResources.PAYMENT_EDIT_ICON));
		
		removePaymentButton = new JButton("Remove Payment");
		removePaymentButton.setPreferredSize(buttonSize);
		removePaymentButton.setEnabled(false);
		removePaymentButton.setIcon(new ImageIcon(ImageResources.PAYMENT_REMOVE_ICON));
		
		
		// creating containers
		patientPanel = new JPanel(new FlowLayout());
		patientPanel.setBorder(containerSpacing);
		
		procedurePanel = new JPanel(new FlowLayout());
		
		paymentPanel = new JPanel(new FlowLayout());
		
		
		// adding components to containers
		patientPanel.add(addPatientButton);
		patientPanel.add(editPatientButton);
		patientPanel.add(removePatientButton);
		
		procedurePanel.add(addProcedureButton);
		procedurePanel.add(editProcedureButton);
		procedurePanel.add(removeProcedureButton);
		
		paymentPanel.add(addPaymentButton);
		paymentPanel.add(editPaymentButton);
		paymentPanel.add(removePaymentButton);
		
		
		// creating card layout
		cardContainerLayout = new CardLayout();
		procedurePaymentContainer = new JPanel(cardContainerLayout);
		
		procedurePaymentContainer.add(procedurePanel, PROCEDURE_CARD);
		procedurePaymentContainer.add(paymentPanel, PAYMENT_CARD);
		procedurePaymentContainer.setBorder(containerSpacing);
		
		cardContainerLayout.show(procedurePaymentContainer, PROCEDURE_CARD);
		
		
		// adding components to panel
		setLayout(new BorderLayout());
		
		add(patientPanel, BorderLayout.WEST);
		add(procedurePaymentContainer, BorderLayout.EAST);

	}
	


	// ===========================================================
	// ==================== SETTERS & GETTERS ==================== 
	// ===========================================================
	
	
	// PATIENT RELATED BUTTONS
	public JButton getAddPatientButton() {
		return addPatientButton;
	}

	public JButton getEditPatientButton() {
		return editPatientButton;
	}

	public JButton getRemovePatientButton() {
		return removePatientButton;
	}


	// PROCEDURE
	public JButton getAddProcedureButton() {
		return addProcedureButton;
	}

	public JButton getEditProcedureButton() {
		return editProcedureButton;
	}

	public JButton getRemoveProcedureButton() {
		return removeProcedureButton;
	}


	// PAYMENT
	public JButton getAddPaymentButton() {
		return addPaymentButton;
	}

	public JButton getEditPaymentButton() {
		return editPaymentButton;
	}

	public JButton getRemovePaymentButton() {
		return removePaymentButton;
	}

	
	// CARD-CONTAINER
	public CardLayout getCardContainerLayout(){
		return cardContainerLayout;
	}
	
	public JPanel getProcedurePaymentContainer(){
		return procedurePaymentContainer;
	}
	
}
