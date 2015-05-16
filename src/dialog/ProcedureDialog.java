package dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import model.CustomComboBoxModel;
import model.Procedure;
import model.ProcedureList;
import res.ImageResources;
import style.SimpleProcedureCellRenderer;

public class ProcedureDialog extends JDialog {

	private static final long serialVersionUID = 4464510166069612805L;
	
	
	private Procedure procedure;
	
	
	private final Dimension labelSize = new Dimension(70, 30);
	
	private JLabel typeLabel, costLabel;
	
	private JComboBox<Procedure> typeComboBox;
	
	private JTextField costField;
	
	private JButton confirmButton, cancelButton;
	
	private JPanel typeContainer, costContainer, buttonContainer;
	
	private CustomComboBoxModel model;
	
	
	/**
	 * Default constructor.
	 * 
	 * @param parent the parent frame
	 * @param title of the dialog
	 */
	public ProcedureDialog(JFrame parent, String title){
		
		super(parent, title);
		
		
		// creating text labels
		typeLabel = new JLabel("Type:", SwingConstants.RIGHT);
		typeLabel.setPreferredSize(labelSize);
		
		costLabel = new JLabel("Cost:", SwingConstants.RIGHT);
		costLabel.setPreferredSize(labelSize);
		
		
		// creating text fields
		costField = new JTextField(20);
		costField.setEnabled(false);
		
		
		// creating combobox
		model = new CustomComboBoxModel();
		model.setData(ProcedureList.getInstance());
		
		typeComboBox = new JComboBox<Procedure>();
		typeComboBox.setRenderer(new SimpleProcedureCellRenderer());
		typeComboBox.setModel(model);
		
		
		// creating buttons
		confirmButton = new JButton("Confirm");
		cancelButton = new JButton("Cancel");
		
		
		// creating component containers
		typeContainer = new JPanel(new FlowLayout());
		costContainer = new JPanel(new FlowLayout());
		buttonContainer = new JPanel(new FlowLayout());
		
		
		// adding components to containers
		typeContainer.add(typeLabel);
		typeContainer.add(typeComboBox);
		
		costContainer.add(costLabel);
		costContainer.add(costField);

		buttonContainer.add(confirmButton);
		buttonContainer.add(cancelButton);
		
		
		// adding containers to frame
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		getContentPane().add(typeContainer);
		getContentPane().add(costContainer);
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
	public Procedure showDialog(){
		
		setIconImage(ImageResources.PROCEDURE_ADD_ICON);
		
		if (ProcedureList.getInstance().isEmpty()){
			JOptionPane.showMessageDialog(null, "There are no Procedures on the system\nCreate a Procedure via the \"Administration\" menu.", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		typeComboBox.setSelectedIndex(0);
		setVisible(true);
		return procedure;
	}
	
	
	/**
	 * Used to display the dialog when editing a patient. Fills the 
	 * text fields with that patient's data. Remains open until the
	 * user either confirms the edit or cancels it.
	 * 
	 * @param procedure to be edited
	 * @return the procedure edited
	 */
	public Procedure showDialog(Procedure procedure){
		
		setIconImage(ImageResources.PROCEDURE_EDIT_ICON);
		
		if (ProcedureList.getInstance().isEmpty()){
			JOptionPane.showMessageDialog(null, "FUCKED UP", "NOPE", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		this.procedure = procedure;
		
		int index = ProcedureList.getInstance().indexOf(procedure);
		
		if (index >= 0){
			typeComboBox.setSelectedIndex(index);
		}
		else {
			typeComboBox.setSelectedIndex(0);
		}
		
		setVisible(true);
		
		return this.procedure;
		
	}
	
	
	/**
	 * Sets up all necessary listeners.
	 */
	private void setListeners(){
		
		
		
		
		// setting the price field to the price of the selected procedure in euro
		typeComboBox.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e){
				
				costField.setText(((Procedure)typeComboBox.getSelectedItem()).getProcedureCostInEuro());
				
			}
			
		});
		
		
		// if a Procedure object was supplie to the dialog, applies the changes
		// to that Proceudre otherwise, creates a new Procedure object with the 
		// given details
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				procedure = ProcedureList.getInstance().get(typeComboBox.getSelectedIndex());
				
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
