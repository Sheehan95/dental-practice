package view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import res.ImageResources;

/**
 * The Frame which holds all other GUI components.
 * 
 * @author Alan Sheehan - R00111909
 */
public class RootView extends JFrame {
	
	private static final long serialVersionUID = -2593711613317285968L;

	
	private PatientDetailsView patientDetails;
	private ActionPaneView actionPane;
	
	private MenuBarView menuBar;
	
	private JPanel contentPane;
	
	private Border frameBorder = BorderFactory.createEmptyBorder(0, 15, 15, 15);
	
	
	/**
	 * Default constructor which creates the frame.
	 */
	public RootView(){
		
		// setting look and feel of buttons to the Windows look and feel
		UIManager.put("ButtonUI", "com.sun.java.swing.plaf.windows.WindowsButtonUI");
		
		
		// creating components to add to the frame
		menuBar = new MenuBarView();
		
		contentPane = (JPanel)getContentPane();
		
		patientDetails = new PatientDetailsView();
		actionPane = new ActionPaneView();
		
		
		// adding components to the frame
		setJMenuBar(menuBar);
		getContentPane().add(actionPane, BorderLayout.NORTH);
		getContentPane().add(patientDetails);
		
		
		// setting up the frame
		setSize(1000, 500);
		setVisible(true);
		setTitle("Dentist App");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		contentPane.setBorder(frameBorder);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setIconImage(ImageResources.DENTIST_APP_ICON);
		
		initListeners();
		
	}
	
	
	/**
	 * Initializes listeners.
	 */
	public void initListeners(){
		
		// setting listener for the tab pane
		patientDetails.getProcedurePaymentTab().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				
				// if the selected tab is the procedure tab, display procedure-related buttons
				if (patientDetails.getProcedurePaymentTab().getSelectedIndex() == PatientDetailsView.PROCEDURE_TAB){
					actionPane.getCardContainerLayout().show(actionPane.getProcedurePaymentContainer(), ActionPaneView.PROCEDURE_CARD);
				}
				// if the selected tab is the payment tab, display payment-related buttons
				else {
					actionPane.getCardContainerLayout().show(actionPane.getProcedurePaymentContainer(), ActionPaneView.PAYMENT_CARD);
				}
				
			}
		});
		
	}
	
	
	// ===========================================================
	// ==================== SETTERS & GETTERS ==================== 
	// ===========================================================
	
	
	// =========================== GETTERS FOR OTHERS ===========================
	public JPanel getCardContainer(){
		return actionPane.getProcedurePaymentContainer();
	}
	
	
	// ======================== GETTERS FOR MENU ITEMS ========================
	public JMenuItem getSaveMenuItem (){
		return menuBar.getSaveMenuItem();
	}
	
	
	public JMenuItem getGenerateCompleteMenuItem (){
		return menuBar.getGenerateCompleteMenuItem();
	}
	
	public JMenuItem getGenerateOverdueMenuItem (){
		return menuBar.getGenerateOverdueMenuItem();
	}
	
	
	public JMenuItem getProcedureAddMenuItem (){
		return menuBar.getProcedureAddMenuItem();
	}
	
	public JMenuItem getProcedureRemoveMenuItem (){
		return menuBar.getProcedureRemoveMenuItem();
	}
	
	
	// =========================== GETTERS FOR TABLES ===========================
	public JTable getPatientTable(){
		return patientDetails.getPatientTable();
	}
	
	public JTable getProcedureTable(){
		return patientDetails.getProcedureTable();
	}
	
	public JTable getPaymentTable(){
		return patientDetails.getPaymentTable();
	}
	
	
	// =========================== GETTERS FOR BUTTONS ===========================
	
	// PATIENTS
	public JButton getAddPatientButton(){
		return actionPane.getAddPatientButton();
	}
	
	public JButton getEditPatientButton(){
		return actionPane.getEditPatientButton();
	}
	
	public JButton getRemovePatientButton(){
		return actionPane.getRemovePatientButton();
	}
	
	
	// PROCEDURES
	public JButton getAddProcedureButton(){
		return actionPane.getAddProcedureButton();
	}
	
	public JButton getEditProcedureButton(){
		return actionPane.getEditProcedureButton();
	}
	
	public JButton getRemoveProcedureButton(){
		return actionPane.getRemoveProcedureButton();
	}
	
	
	// PAYMENTS
	public JButton getAddPaymentButton(){
		return actionPane.getAddPaymentButton();
	}
	
	public JButton getEditPaymentButton(){
		return actionPane.getEditPaymentButton();
	}
	
	public JButton getRemovePaymentButton(){
		return actionPane.getRemovePaymentButton();
	}
	
}
