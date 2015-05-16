package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTable;

import model.Patient;
import model.PatientTableModel;
import model.Procedure;
import model.ProcedureTableModel;
import view.RootView;
import dialog.ProcedureDialog;


/**
 * Controller for Procedures. Handles all Procedure-related functionality.
 * 
 * @author Alan Sheehan - R0011909
 */
public class ProcedureController {
	
	private Callback callback;
	private RootView view;
	private ProcedureTableModel model;
	private JTable procedureTable;
	
	
	/**
	 * Assigns the passed in ArrayList of Procedures to the data of the
	 * {@link #model}.
	 * 
	 * @param data the ArrayList of Procedures
	 * @param view the GUI
	 */
	public ProcedureController(Callback callback, ArrayList<Procedure> data, RootView view){
		
		this.callback = callback;
		this.view = view;
		this.model = new ProcedureTableModel(data);
		procedureTable = view.getProcedureTable();
		
		view.getProcedureTable().setModel(this.model);
		
		initListeners();
		
	}
	
	
	/**
	 * Sets the passed in data to the data of the model.
	 * 
	 * @param data to be set as the model
	 */
	public void setDataModel(ArrayList<Procedure> model){
		this.model.setData(model);
	}
	
	
	/**
	 * Adds a procedure to the model.
	 * 
	 * @param procedure added to the model
	 */
	public void addProcedure(Procedure procedure){
		model.add(procedure);
	}
	
	/**
	 * Removes a procedure from the model.
	 * 
	 * @param index of the procedure to be removed
	 */
	public void removeProcedure(int index){
		model.remove(index);
	}
	
	/**
	 * Returns the patient related to the currently displaying procedure list.
	 * 
	 * @return the patient related to the procedure list
	 */
	private Patient getCurrentPatient(){
		JTable patients = view.getPatientTable();
		int patientRow = patients.convertRowIndexToModel(patients.getSelectedRow());
		Patient patient = ((PatientTableModel) patients.getModel()).get(patientRow);
		return patient;
	}
	
	
	/**
	 * Initializes listeners.
	 */
	private void initListeners(){
		
		
		// setting listener for addProcedure button
		view.getAddProcedureButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ProcedureDialog dialog = new ProcedureDialog(view, "Add Procedure");
				
				Procedure procedure = dialog.showDialog();
				
				if (procedure != null){
					
					Patient patient = getCurrentPatient();
					
					int patientID = patient.getPatientNo();
					int procedureID = procedure.getProcedureNo();
					
					DatabaseController db = DatabaseController.getInstance();
					
					if (db.addProcedureToPatient(patientID, procedureID)){
						callback.refreshPatientProcedureList();
					}
					
				}
				
			}
		});
		
		
		// setting listener for editPorcedure button
		view.getEditProcedureButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ProcedureDialog dialog = new ProcedureDialog(view, "Edit Procedure");
				
				int row = procedureTable.convertRowIndexToModel(procedureTable.getSelectedRow());
				
				Procedure procedure = model.get(row);
				
				int oldProcedureID = procedure.getProcedureNo();
				
				procedure = dialog.showDialog(procedure);
				
				Patient patient = getCurrentPatient();
				
				int patientID = patient.getPatientNo();
				int procedureID = procedure.getProcedureNo();
				
				DatabaseController db = DatabaseController.getInstance();
				
				if (db.updateProcedureOfPatient(patientID, procedureID, oldProcedureID)){
					callback.refreshPatientProcedureList();
				}
				
			}
		});
		
		
		// setting listener for removeProcedure button
		view.getRemoveProcedureButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Patient patient = getCurrentPatient();
				
				int procedureRow = procedureTable.convertRowIndexToModel(procedureTable.getSelectedRow());
				Procedure procedure = model.get(procedureRow);
				
				int patientID = patient.getPatientNo();
				int procedureID = procedure.getProcedureNo();
				
				DatabaseController db = DatabaseController.getInstance();
				db.removeProcedureFromPatient(patientID, procedureID);
				callback.refreshPatientProcedureList();
				
			}
		});
		
	}
	
	
	/**
	 * Allows an instance of the procedure controller class to communicate with
	 * the {@link MasterController}.
	 * 
	 * @author Alan Sheehan - R00111909
	 */
	public interface Callback {
		
		/**
		 * Refreshes a patient's procedure list following any changes.
		 */
		void refreshPatientProcedureList();
		
	}

}
