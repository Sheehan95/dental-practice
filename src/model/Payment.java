package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A Payment class with an ID, amount, date & status.
 * 
 * @author Alan Sheehan - R00111909
 */
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType (propOrder = {"paymentNo", "paymentAmount", "paymentDate", "paymentStatus"})
public class Payment implements Serializable {
	
	
	
	public Payment(int id, double amount, boolean status, Date date){
		setPaymentNo(id);
		setPaymentAmount(amount);
		setPaymentStatus(status);
		setPaymentDate(date);
	}
	
	
	

	private static final long serialVersionUID = 6144304199176705234L;
	
	private static int uniquePaymentID = 1;
	
	@XmlElement (name = "ID")
	private int paymentNo;
	
	@XmlElement (name = "amount")
	private double paymentAmount;
	
	@XmlElement (name = "date")
	private Date paymentDate;
	
	@XmlElement (name = "status")
	private boolean paymentStatus;
	
	
	/**
	 * Default constructor set to private - used by JAXB only.
	 */
	@SuppressWarnings("unused")
	private Payment(){}
	
	
	/**
	 * Constructor which calls {@link #Payment(double, boolean)}
	 * with a default value of unpaid for the status.
	 * 
	 * @param amount of money
	 */
	public Payment(double amount){
		
		this(amount, false);
		
	}
	
	/**
	 * Constructor which takes both the amount and status of
	 * the payment, paid or unpaid.
	 * 
	 * @param amount of money
	 * @param status paid or unpaid
	 */
	public Payment(double amount, boolean status){
		setPaymentNo(uniquePaymentID);
		setPaymentAmount(amount);
		setPaymentDate(new Date());
		setPaymentStatus(status);
		
		uniquePaymentID++;
	}
	
	
	@Override
	public String toString() {
		return paymentNo + "\t" + paymentAmount + "\t" + paymentDate + "\t" + paymentStatus;
	}
	
	
	/**
	 * Compares two Payment objects based on their date.
	 * 
	 * @author Alan Sheehan - R00111909
	 */
	public static class SortByDate implements Comparator<Payment> {

		@Override
		public int compare(Payment firstPayment, Payment secondPayment) {
			
			Date first = firstPayment.getPaymentDate();
			Date second = secondPayment.getPaymentDate();
			
			return first.compareTo(second);
			
		}
		
	}
	
	
	
	// SETTERS & GETTERS

	public int getPaymentNo() {
		return paymentNo;
	}


	public void setPaymentNo(int paymentNo) {
		this.paymentNo = paymentNo;
	}


	
	public double getPaymentAmount() {
		return paymentAmount;
	}


	public void setPaymentAmount(double paymentAmount) {
		
		// rounding the given value to two decimal places
		BigDecimal bd = new BigDecimal(paymentAmount);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		
		this.paymentAmount = bd.doubleValue();
		
	}


	
	public Date getPaymentDate() {
		return paymentDate;
	}


	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}


	
	public boolean getPaymentStatus() {
		return paymentStatus;
	}


	public void setPaymentStatus(boolean isPaid) {
		this.paymentStatus = isPaid;
	}
	
}
