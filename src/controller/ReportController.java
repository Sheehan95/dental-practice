package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import model.Patient;
import model.Payment;
import model.Procedure;
import view.RootView;
import dialog.ReportDialog;

/**
 * Controller for Reports. Handles all Report-related functionality.
 * 
 * @author Alan Sheehan - R00111909
 */
public class ReportController {
	
	private RootView view;
	
	
	/**
	 * Assigns the passed in ArrayList of Patients to the data of the
	 * {@link #patientList}.
	 * 
	 * @param patientList the ArrayList of Patients
	 * @param view the GUI
	 */
	public ReportController (RootView view){
		
		this.view = view;
		
		initListeners();
		
	}
	
	
	/**
	 * Generates a report containing all Patients in the system.
	 * Displays the report with a {@link dialog.ReportDialog}.
	 */
	public void generateFullReport (){
		
		ReportDialog dialog = new ReportDialog();
		
		dialog.setTitle("Complete Report");
		
		
		ArrayList<Patient> patientList = DatabaseController.getInstance().readPatientList();
		Collections.sort(patientList);
		
		for (Patient p : patientList){
			
			String patient = patientToHTML(p);
			
			try {
				
				dialog.appendTo(patient);
				
			} catch (BadLocationException e){
				
				JOptionPane.showMessageDialog(null, "Error in adding the following patient to the report:\n" + p.toString(), 
						"Critical Error", JOptionPane.ERROR_MESSAGE);
				return;
				
			} catch (IOException e){
				
				JOptionPane.showMessageDialog(null, "Error in adding the following patient to the report:\n" + p.toString(), 
						"Critical Error", JOptionPane.ERROR_MESSAGE);
				return;
				
			}
			
		}
		
		dialog.showDialog();
		
	}
	
	
	/**
	 * Generates a report of all Patients who haven't made a payment in 6 months.
	 * Displays the report with a {@link dialog.ReportDialog}.
	 */
	public void generateOverdueReport (){
		
		ReportDialog dialog = new ReportDialog();
		
		dialog.setTitle("Overdue Report");
		
		Calendar currentDate = new GregorianCalendar();
		
		Calendar paymentDate = new GregorianCalendar();
		
		
		ArrayList<Patient> patientList = DatabaseController.getInstance().readPatientList();
		Collections.sort(patientList, new Patient.CompareByAmountOwed());
		
		
		for (int i = 0 ; i < patientList.size() ; i++){
			
			Patient p = patientList.get(i);
			
			// skips the patient if he doesn't owe money or no payments have been made
			if (p.getAmountOwed() <= 0 || p.getPatientPaymentList().isEmpty()){
				i++;
			}
			else {
				
				// creating deep copy to avoid sorting original data
				ArrayList<Payment> payments = p.getPatientPaymentList();
				payments.sort(new Payment.SortByDate());
				
				
				// getting most recent payment			
				Payment payment = payments.get(payments.size() - 1);
				paymentDate.setTime(payment.getPaymentDate());
				
				
				// calculating difference in months between last payment and current time in months
				int differenceYears = currentDate.get(Calendar.YEAR) - paymentDate.get(Calendar.YEAR);
				int differenceMonths = currentDate.get(Calendar.MONTH) - paymentDate.get(Calendar.MONTH);
				
				int difference = differenceMonths + (12 * differenceYears);
				
				// adds the patient to the report if the patient hasn't made a payment in 6 months
				if (difference > 6){
					
					String patient = patientToHTML(p);
				
					try {
						
						dialog.appendTo(patient);
						
					} catch (BadLocationException e){
						
						JOptionPane.showMessageDialog(null, "Error in adding the following patient to the report:\n" + p.toString(), 
								"Critical Error", JOptionPane.ERROR_MESSAGE);
						return;
						
					} catch (IOException e){
						
						JOptionPane.showMessageDialog(null, "Error in adding the following patient to the report:\n" + p.toString(), 
								"Critical Error", JOptionPane.ERROR_MESSAGE);
						return;
							
					}
						
				}
			}
				
		}
		
		dialog.showDialog();
		
	}
	
	
	/**
	 * Converts an object of type {@link model.Patient} into HTML
	 * text representing the Patient.
	 * 
	 * @param patient to be converted to HTML
	 * @return the HTML representation of the patient
	 */
	private String patientToHTML (Patient patient){
		
		String html = "";
		
		// adding basic patient information
		html += "<h2>" + patient.getPatientName() + "</h2>";
		html += "<ul>";
		html += "<li><b>ID:</b> " + patient.getPatientNo() + "</li>";
		html += "<li><b>Address:</b> " + patient.getPatientAddress() + "</li>";
		html += "<li><b>Phone:</b> " + patient.getPatientPhone() + "</li>";
		html += "</ul>";
		
		
		
		// adding patient procedures in table form
		html += "<h3>Procedures</h3>";
		html += "<table width = \"100%\">";
		html += "<tr>";
		html += "<th>ID</th>";
		html += "<th>Name</th>";
		html += "<th>Cost</th>";
		html += "</tr>";
		
		if (patient.getPatientProcedureList().isEmpty()){
			html += "<tr>";
			html += "<td> N/A </td>";
			html += "<td> N/A </td>";
			html += "<td> N/A </td>";
			html += "</tr>";
		}
		
		for (Procedure pro : patient.getPatientProcedureList()){
			html += "<tr>";
			html += "<td>" + pro.getProcedureNo() +"</td>";
			html += "<td>" + pro.getProcedureName() +"</td>";
			html += "<td>" + pro.getProcedureCostInEuro() +"</td>";
			html += "</tr>";
		}
		
		html += "</table>";
		
		
		
		// adding patient payments in table form
		html += "<h3>Payments</h3>";
		html += "<table width = \"100%\">";
		html += "<tr>";
		html += "<th>ID</th>";
		html += "<th>Amount</th>";
		html += "<th>Date</th>";
		html += "<th>Status</th>";
		html += "</tr>";
		
		if (patient.getPatientPaymentList().isEmpty()){
			html += "<tr>";
			html += "<td> N/A </td>";
			html += "<td> N/A </td>";
			html += "<td> N/A </td>";
			html += "<td> N/A </td>";
			html += "</tr>";
		}
		
		for (Payment pay : patient.getPatientPaymentList()){
			html += "<tr>";
			html += "<td>" + pay.getPaymentNo() + "</td>";
			html += "<td>" + pay.getPaymentAmount() + "</td>";
			html += "<td>" + pay.getPaymentDate() + "</td>";
			html += "<td>" + pay.getPaymentStatus() + "</td>";
			html += "</tr>";
		}
		
		html += "</table>";
		
		
		// adding line break for next patient
		html += "<br><br><br><hr><br><br><br>";
		
		return html;
		
	}
	
	
	/**
	 * Initializes listeners.
	 */
	private void initListeners (){
		
		
		// setting listener for generate complete report menu item
		view.getGenerateCompleteMenuItem().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				generateFullReport();
				
			}
			
		});
		
		
		// setting listener for generate overdue report menu item
		view.getGenerateOverdueMenuItem().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				generateOverdueReport();
				
			}
			
		});
		
	}
	

}
