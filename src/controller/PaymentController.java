package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTable;

import model.Patient;
import model.PatientTableModel;
import model.Payment;
import model.PaymentTableModel;
import view.RootView;
import dialog.PaymentDialog;


/**
 * Controller for Payments. Handles all Payment-related functionality.
 * 
 * @author Alan Sheehan - R00111909
 */
public class PaymentController {
	
	private Callback callback;
	private RootView view;
	private PaymentTableModel model;
	private JTable paymentTable;
	
	
	/**
	 * Assigns the passed in ArrayList of Payments to the data of the
	 * {@link #model}.
	 * 
	 * @param data the ArrayList of Payments
	 * @param view the GUI
	 */
	public PaymentController(Callback callback, ArrayList<Payment> data, RootView view){
		
		this.callback = callback;
		this.view = view;
		this.model = new PaymentTableModel(data);
		paymentTable = view.getPaymentTable();
		
		view.getPaymentTable().setModel(this.model);
		
		initListeners();
		
	}
	
	
	/**
	 * Sets the passed in data to the data of the model.
	 * 
	 * @param data to be set as the model
	 */
	public void setDataModel(ArrayList<Payment> model){
		this.model.setData(model);
	}
	
	/**
	 * Adds a payment to the model.
	 * 
	 * @param payment added to the data
	 */
	public void addPayment(Payment payment){
		model.add(payment);
	}
	
	/**
	 * Removes a payment from the model.
	 * 
	 * @param index
	 */
	public void removePayment(int index){
		model.remove(index);
	}
	
	/**
	 * Initializes listeners.
	 */
	public void initListeners(){
		
		
		// setting listener for addPayment button
		view.getAddPaymentButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				PaymentDialog dialog = new PaymentDialog(view, "Add Payment");
				
				Payment payment = dialog.showDialog();
				
				if (payment != null){
					
					// resolving current patient id
					JTable patients = view.getPatientTable();
					int patientRow = patients.convertRowIndexToModel(patients.getSelectedRow());
					Patient patient = ((PatientTableModel) patients.getModel()).get(patientRow);
					
					int patientID = patient.getPatientNo();
					
					double amount = payment.getPaymentAmount();
					boolean status = payment.getPaymentStatus();
					
					DatabaseController db = DatabaseController.getInstance();
					
					if (db.insertPayment(patientID, amount, status)){
						callback.refreshPatientPaymentList();
					}
					
				}
				
			}
		});
		
		
		// setting listener for editPayment button
		view.getEditPaymentButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				PaymentDialog dialog = new PaymentDialog(view, "Edit Payment");
				
				// selecting the correct index in the model for the corresponding row selected
				int row = paymentTable.convertRowIndexToModel(paymentTable.getSelectedRow());
				
				Payment payment = model.get(row);
				
				payment = dialog.showDialog(payment);
				
				
				DatabaseController db = DatabaseController.getInstance();
				
				if (db.updatePayment(payment)){
					callback.refreshPatientPaymentList();
				}
				
			}
		});
		
		
		// setting listener for removePayment button
		view.getRemovePaymentButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Payment payment = model.get(paymentTable.convertRowIndexToModel(paymentTable.getSelectedRow()));
				
				
				DatabaseController db = DatabaseController.getInstance();
				
				if (db.deletePayment(payment)){
					callback.refreshPatientPaymentList();
				}
				
			}
		});
		
	}
	
	
	/**
	 * Allows an instance of the payment controller class to communicate with
	 * the {@link MasterController}.
	 * 
	 * @author Alan Sheehan - R00111909
	 */
	public interface Callback {
		
		/**
		 * Refreshes a patient's payment list following any changes.
		 */
		void refreshPatientPaymentList();
		
	}

}
