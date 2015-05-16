package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;


/**
 * A Patient class with an ID, name, address & phone number.
 * 
 * @author Alan Sheehan - R00111909
 */

@XmlAccessorType (XmlAccessType.FIELD)
@XmlType (propOrder = {"patientNo", "patientName", "patientAddress", "patientPhone", "patientProcedureList", "patientPaymentList"})
public class Patient implements Serializable, Comparable<Patient> {
	
	private static final long serialVersionUID = 8454627178652816442L;
	
	@XmlAttribute (name = "uniqueID")
	private static int uniquePatientID = 1;
	
	@XmlElement (name = "ID")
	private int patientNo;
	
	@XmlElement (name = "name")
	private String patientName;
	
	@XmlElement (name = "address")
	private String patientAddress;
	
	@XmlElement (name = "phone")
	private String patientPhone;
	
	
	@XmlElementWrapper (name = "Payments")
	@XmlElement (name = "payment")
	private ArrayList<Payment> patientPaymentList;
	
	@XmlElementWrapper (name = "Procedures")
	@XmlElement (name = "procedure")
	private ArrayList<Procedure> patientProcedureList;
	
	
	/**
	 * Default constructor set to private - used by JAXB only.
	 */
	@SuppressWarnings("unused")
	private Patient(){}
	
	
	/**
	 * Constructor for patient which assigns an incrementally increasing id.
	 * 
	 * @param name of the patient
	 * @param address of the patient
	 * @param patientNo of the patient
	 */
	public Patient(String name, String address, String patientPhone){
		
		setPatientName(name);
		setPatientAddress(address);
		setPatientPhone(patientPhone);
		setPatientNo(uniquePatientID);
		
		patientPaymentList = new ArrayList<Payment>();
		patientProcedureList = new ArrayList<Procedure>();
		
		uniquePatientID++;
		
	}
	

	public Patient(int id, String name, String address, String patientPhone){
		
		setPatientName(name);
		setPatientAddress(address);
		setPatientPhone(patientPhone);
		setPatientNo(id);
		
		patientPaymentList = new ArrayList<Payment>();
		patientProcedureList = new ArrayList<Procedure>();
		
	}
	

	/**
	 * Adds a procedure to the patient.
	 * 
	 * @param procedure to be added.
	 */
	public void addProcedure(Procedure procedure){
		patientProcedureList.add(procedure);
	}
	
	/**
	 * Removes a procedure from the patient at a specified index.
	 * 
	 * @param index of procedure to be removed.
	 */
	public void removeProcedure(int index){
	 	patientProcedureList.remove(index);
	}
	
	/**
	 * Sets the procedure to the given index in the list of procedures, effectively editing/overwriting
	 * an older entry. Throws an IndexOutOfBoundsException if that index doesn't exist in the list.
	 * 
	 * @param index where the procedure is to be set in the list.
	 * @param procedure to be set in the list.
	 * @throws IndexOutOfBoundsException if the index does not exist in the list.
	 */
	public void setProcedure(int index, Procedure procedure) throws IndexOutOfBoundsException{
		
		if (index >= patientProcedureList.size()){
			throw new IndexOutOfBoundsException();
		}
		
		patientProcedureList.set(index, procedure);
	}
	
	
	/**
	 * Adds a payment to the patient.
	 * 
	 * @param payment to be added.
	 */
	public void addPayment(Payment payment){
		patientPaymentList.add(payment);
	}
	
	/**
	 * Removes a payment from the patient at a specified index.
	 * 
	 * @param index of payment for be removed.
	 */
	public void removePayment(int index){
		patientPaymentList.remove(index);
	}
	
	/**
	 * Sets the payment to the given index in the list of payments, effectively editing/overwriting
	 * an older entry. Throws an IndexOutOfBoundsException if that index doesn't exist in the list.
	 * 
	 * @param index where the payment is to be set in the list.
	 * @param payment to be set in the list.
	 * @throws IndexOutOfBoundsException if the index does not exist in the list.
	 */
	public void setPayment(int index, Payment payment) throws IndexOutOfBoundsException{
		
		if (index >= patientPaymentList.size()){
			throw new IndexOutOfBoundsException();
		}
		
		patientPaymentList.set(index, payment);
	}
	
	
	/**
	 * Gets the amount of money owed by the patient based on the procedures
	 * scheduled and the payments made.
	 * 
	 * @return the amount the patient owes
	 */
	public double getAmountOwed (){
		
		double owed = 0;
		double payed = 0;
		
		for (Procedure p : getPatientProcedureList()){
			owed += p.getProcedureCost();
		}
		
		for (Payment p : getPatientPaymentList()){
			if (p.getPaymentStatus()){
				payed += p.getPaymentAmount();
			}
		}
		
		owed -= payed;
		
		return owed;
		
	}
	
	
	@Override
	public int compareTo(Patient patient) {
		
		return this.getPatientName().compareTo(patient.getPatientName());
		
	}
	
	
	@Override
	public String toString() {
		return patientNo + "\t" + patientName + "\t" + patientAddress + "\t" + patientPhone;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof Patient)){
			return false;
		}
		
		Patient patient = (Patient)obj;
		
		return this.patientNo == patient.patientNo;
		
	}
	
	
	/**
	 * Compares two Patient objects based on the amount they owe.
	 * 
	 * @author Alan Sheehan - R0011909
	 */
	public static class CompareByAmountOwed implements Comparator<Patient> {

		@Override
		public int compare(Patient first, Patient second) {
			
			Double d1 = first.getAmountOwed();
			Double d2 = second.getAmountOwed();
			
			return d1.compareTo(d2);
			
		}
		
	}
	
	
	
	// SETTERS & GETTERS

	public static void setUniqueID(int id){
		uniquePatientID = id;
	}
	
	
	
	public int getPatientNo() {
		return patientNo;
	}

	public void setPatientNo(int patientNo) {
		this.patientNo = patientNo;
	}


	
	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	

	public String getPatientAddress() {
		return patientAddress;
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	
	
	public String getPatientPhone() {
		return patientPhone;
	}

	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}
	
	
	
	public ArrayList<Payment> getPatientPaymentList() {
		return patientPaymentList;
	}

	public void setPatientPaymentList(ArrayList<Payment> patientPaymentList) {
		this.patientPaymentList = patientPaymentList;
	}
	
	
	
	public ArrayList<Procedure> getPatientProcedureList() {
		return patientProcedureList;
	}

	public void setPatientProcedureList(ArrayList<Procedure> patientProcedureList) {
		this.patientProcedureList = patientProcedureList;
	}

}
