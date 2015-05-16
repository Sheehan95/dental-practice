package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Patient;
import model.PatientTableModel;
import model.Payment;
import model.Procedure;
import model.ProcedureList;
import view.RootView;
import dialog.ProcedureAddDialog;
import dialog.ProcedureRemoveDialog;

/**
 * A controller class which holds reference to all other controllers. Handles events
 * outside of the scope of other controllers, as well as ones involving multiple
 * controllers.
 * 
 * @author Alan Sheehan - R00111909
 */
public class MasterController implements PatientController.Callback, 
											ProcedureController.Callback,
											PaymentController.Callback {
	
	private RootView view;
	
	private ArrayList<Patient> patientList;
	
	private PatientController patientControl;
	private ProcedureController procedureControl;
	private PaymentController paymentControl;
	
	private DatabaseController dbControl;
	@SuppressWarnings("unused")
	private ReportController reportControl;
	
	public MasterController(RootView view){
		
		this.view = view;
		
		dbControl = DatabaseController.getInstance();
		patientList = dbControl.readPatientList();
	
		ProcedureList.setProcedureList(dbControl.readProcedureList());
		
		patientControl = new PatientController(this, patientList, view);
		reportControl = new ReportController(view);
		
		// adds dummy data to both controllers until a patient is selected
		procedureControl = new ProcedureController(this, new ArrayList<Procedure>(), view);
		paymentControl = new PaymentController(this, new ArrayList<Payment>(), view);
		
		initListeners();
		
	}
	
	
	/**
	 * Initializes listeners.
	 */
	private void initListeners(){

		
		// setting listener for patient table
		view.getPatientTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {

				int index = view.getPatientTable().getSelectedRow();
				
				if (index < 0){
					// no selection made - disabling selection-dependent buttons
					view.getEditPatientButton().setEnabled(false);
					view.getRemovePatientButton().setEnabled(false);
					view.getAddProcedureButton().setEnabled(false);
					view.getAddPaymentButton().setEnabled(false);
				}
				else {
					// selection made - enabling buttons & populating tables with selection-related details
					view.getEditPatientButton().setEnabled(true);
					view.getRemovePatientButton().setEnabled(true);
					view.getAddProcedureButton().setEnabled(true);
					view.getAddPaymentButton().setEnabled(true);
					
					index = view.getPatientTable().convertRowIndexToModel(index);
					Patient patient = ((PatientTableModel) view.getPatientTable().getModel()).get(index);
					
					procedureControl.setDataModel(dbControl.readPatientProcedureList(patient.getPatientNo()));
					paymentControl.setDataModel(dbControl.readPatientPaymentList(patient.getPatientNo()));
				}
				
			}
		});
		
		
		// setting listener for procedure table
		view.getProcedureTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				int index = view.getProcedureTable().getSelectedRow();
				
				if (index < 0){
					// no selection made - disabling selection-dependent buttons
					view.getEditProcedureButton().setEnabled(false);
					view.getRemoveProcedureButton().setEnabled(false);
				}
				else {
					// selection made - enabling buttons
					view.getEditProcedureButton().setEnabled(true);
					view.getRemoveProcedureButton().setEnabled(true);
				}
				
			}
		});
		
		
		// setting listener for payment table
		view.getPaymentTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				int index = view.getPaymentTable().getSelectedRow();
				
				if (index < 0){
					// no selection made - disabling selection-dependent buttons
					view.getEditPaymentButton().setEnabled(false);
					view.getRemovePaymentButton().setEnabled(false);
				}
				else {
					// selection made - enabling buttons
					view.getEditPaymentButton().setEnabled(true);
					view.getRemovePaymentButton().setEnabled(true);
				}
				
			}
		});
		
		
		// setting up window listener for prompting user to save
		view.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				int choice = JOptionPane.showConfirmDialog(view, "Are you sure you want to quit?");
				
				switch (choice){
				
				case JOptionPane.YES_OPTION:
					view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					break;
				
				}

			}
			
		});
		
		
		// setting up procedure add menu item listener
		view.getProcedureAddMenuItem().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ProcedureAddDialog dialog = new ProcedureAddDialog(view, "Add Procedure");
				Procedure procedure = dialog.showDialog();
				
				if (procedure != null){
					
					String name = procedure.getProcedureName();
					double cost = procedure.getProcedureCost();
					
					if (dbControl.insertProcedure(name, cost)){
						ProcedureList.setProcedureList(dbControl.readProcedureList());
					}
					
				}
				
			}
			
		});
		
		
		// setting up procedure remove menu item listener
		view.getProcedureRemoveMenuItem().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ProcedureRemoveDialog dialog = new ProcedureRemoveDialog(view, "Remove Procedure");
				Procedure procedure = dialog.showDialog();
				
				if (procedure != null){
					
					int procedureID = procedure.getProcedureNo();
					
					if (dbControl.deleteProcedure(procedureID)){
						ProcedureList.setProcedureList(dbControl.readProcedureList());
					}
					
				}
				
			}
			
		});
		
	}
	
	
	@Override
	public void refreshPatientList() {
		patientControl.setDataModel(dbControl.readPatientList());
	}
	
	@Override
	public void refreshPatientProcedureList() {
		JTable patients = view.getPatientTable();
		int patientRow = patients.convertRowIndexToModel(patients.getSelectedRow());
		Patient patient = ((PatientTableModel) patients.getModel()).get(patientRow);
		int patientID = patient.getPatientNo();
		
		procedureControl.setDataModel(dbControl.readPatientProcedureList(patientID));
	}
	
	@Override
	public void refreshPatientPaymentList() {
		JTable patients = view.getPatientTable();
		int patientRow = patients.convertRowIndexToModel(patients.getSelectedRow());
		Patient patient = ((PatientTableModel) patients.getModel()).get(patientRow);
		int patientID = patient.getPatientNo();
		
		paymentControl.setDataModel(dbControl.readPatientPaymentList(patientID));
	}
	
}
