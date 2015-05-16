package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTable;

import model.CustomAbstractTableModel;
import model.Patient;
import model.PatientTableModel;
import model.Payment;
import model.Procedure;
import view.RootView;
import dialog.PatientDialog;


/**
 * Controller for Patients. Handles all Patient-related functionality.
 * 
 * @author Alan Sheehan - R00111909
 */
public class PatientController {
	
	private Callback callback;
	private RootView view;
	private PatientTableModel model;
	private JTable patientTable;
	
	
	/**
	 * Assigns the passed in ArrayList of Patients to the data of the
	 * {@link #model}.
	 * 
	 * @param data the ArrayList of Patients
	 * @param view the GUI
	 */
	public PatientController(Callback callback, ArrayList<Patient> data, RootView view){
		
		this.callback = callback;
		this.view = view;
		this.model = new PatientTableModel(data);
		patientTable = view.getPatientTable();
		
		view.getPatientTable().setModel(this.model);
		
		initListeners();
		
	}
	
	
	/**
	 * Sets the passed in data to the data of the model.
	 * 
	 * @param data to be set as the model
	 */
	public void setDataModel(ArrayList<Patient> data){
		this.model.setData(data);
	}
	
	/**
	 * Adds a patient to the model.
	 * 
	 * @param patient added to the model
	 */
	public void addPatient(Patient patient){
		model.add(patient);
	}
	
	/**
	 * Removes a patient from the model.
	 * 
	 * @param index of the patient to be removed
	 */
	public void removePatient(int index){
		model.remove(index);
	}

	
	/**
	 * Initializes listeners.
	 */
	private void initListeners(){
		
		
		// setting listener for addPatient button
		view.getAddPatientButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				PatientDialog dialog = new PatientDialog(view, "Add Patient");
				
				Patient patient = dialog.showDialog();
				
				if (patient != null){
					
					String name = patient.getPatientName();
					String address = patient.getPatientAddress();
					String phone = patient.getPatientPhone();
					
					DatabaseController db = DatabaseController.getInstance();
					
					if (db.insertPatient(name, address, phone)){
						callback.refreshPatientList();
						patient = db.mostRecentPatientEntry();
						// selecting row of patient added
						if (patient != null){
							int row = patientTable.convertRowIndexToView(model.find(patient));
							patientTable.setRowSelectionInterval(row, row);
						}
					}
					
				}
				
			}
			
		});
		
		
		// setting listener for editPatient button
		view.getEditPatientButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				PatientDialog dialog = new PatientDialog(view, "Edit Patient");
				
				// selecting the correct index in the model for the corresponding row selected
				int row = patientTable.convertRowIndexToModel(patientTable.getSelectedRow());
				
				Patient patient = model.get(row);
				
				patient = dialog.showDialog(patient);
				
				DatabaseController db = DatabaseController.getInstance();
				
				if (db.updatePatient(patient)){
					callback.refreshPatientList();
					// selecting the row of patient edited
					row = patientTable.convertRowIndexToView(model.find(patient));
					patientTable.setRowSelectionInterval(row, row);
				}
				
			}
		});
		
		
		// setting listener for removePatient button
		view.getRemovePatientButton().addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Patient patient = model.get(patientTable.convertRowIndexToModel(patientTable.getSelectedRow()));
				
				DatabaseController db = DatabaseController.getInstance();
				db.deletePatient(patient);
				callback.refreshPatientList();
				
				// clearing procedure & payment tables
				((CustomAbstractTableModel<Procedure>)view.getProcedureTable().getModel()).clear();
				((CustomAbstractTableModel<Payment>)view.getPaymentTable().getModel()).clear();
				
				if (model.getRowCount() > 0){
					patientTable.setRowSelectionInterval(0, 0);
				}
		
			}
		});
		
	}
	
	
	/**
	 * Allows an instance of the patient controller class to communicate with
	 * the {@link MasterController}.
	 * 
	 * @author Alan Sheehan - R00111909
	 */
	public interface Callback {
		
		/**
		 * Refreshes the patient list following any changes.
		 */
		void refreshPatientList();
		
	}
	
	
}
