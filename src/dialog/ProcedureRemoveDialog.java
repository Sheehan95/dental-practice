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

/**
 * A simple dialog with functionality to remove Procedures from the system.
 * 
 * @author Alan Sheehan - R00111909
 */
public class ProcedureRemoveDialog extends JDialog {

	private static final long serialVersionUID = 4464510166069612805L;
	
	private Procedure procedure;
	
	private final Dimension labelSize = new Dimension(70, 30);
	
	private JLabel typeLabel, costLabel;
	
	private JComboBox<Procedure> typeComboBox;
	
	private JTextField costField;
	
	private JButton removeButton, cancelButton;
	
	private JPanel typeContainer, costContainer, buttonContainer;
	
	private CustomComboBoxModel model;
	
	
	/**
	 * Default constructor.
	 * 
	 * @param parent the parent frame
	 * @param title of the dialog
	 */
	public ProcedureRemoveDialog(JFrame parent, String title){
		
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
		removeButton = new JButton("Remove");
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

		buttonContainer.add(removeButton);
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
		setIconImage(ImageResources.PROCEDURE_REMOVE_ICON);
		
	}
	
	
	/**
	 * Used to display the dialog. Remains open until the user either 
	 * confirms the deletion of the selected Procedure or cancels it.
	 */
	public Procedure showDialog(){
		
		if (ProcedureList.getInstance().isEmpty()){
			JOptionPane.showMessageDialog(null, "There are no Procedures on the system\nCreate a Procedure via the \"Administration\" menu.", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		typeComboBox.setSelectedIndex(0);
		setVisible(true);
		
		return procedure;
		
	}
	
	
	/**
	 * Sets up all necessary listeners.
	 */
	private void setListeners(){

		// setting the price field to the price of the selected procedure in euro
		typeComboBox.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e){
				procedure = (Procedure) typeComboBox.getSelectedItem();
				costField.setText(procedure.getProcedureCostInEuro());
			}
			
		});
		
		
		// removes the selected procedure in the combo-box from the system
		removeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ProcedureList.getInstance().remove(typeComboBox.getSelectedIndex());
				
				setVisible(false);
				dispose();
				
			}
			
		});
		
		
		// closes the dialog
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				procedure = null;
				setVisible(false);
				dispose();
				
			}
			
		});
		
	}
	
}
