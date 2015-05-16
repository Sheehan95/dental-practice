package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType (XmlAccessType.FIELD)
@XmlType (propOrder = {"procedureNo", "procedureName", "procedureCost"})
public class Procedure implements Serializable, Cloneable {
	
	
	public Procedure(int id, String name, double price){
		setProcedureNo(id);
		setProcedureName(name);
		setProcedureCost(price);
	}
	
	
	
	private static final long serialVersionUID = -416991125887918591L;
	
	private static int uniqueProcedureID = 1;
	
	@XmlElement (name = "ID")
	private int procedureNo;
	
	@XmlElement (name = "type")
	private String procedureName;
	
	@XmlElement (name = "cost")
	private double procedureCost;
	
	// not saved in XML
	private transient DecimalFormat euro = new DecimalFormat("€###,###.00");
	
	
	/**
	 * Default constructor set to private - used by JAXB only.
	 */
	@SuppressWarnings("unused")
	private Procedure(){}
	
	
	/**
	 * Single constructor for Procedure which accepts the name
	 * and cost of the procedure.
	 * 
	 * @param procedureName name of the procedure
	 * @param procedureCost cost of the procedure
	 */
	public Procedure(String procedureName, double procedureCost){
		setProcedureName(procedureName);
		setProcedureCost(procedureCost);
		setProcedureNo(uniqueProcedureID);
		
		uniqueProcedureID++;
	}
	
	
	@Override
	public String toString() {
		return procedureNo + "\t" + procedureName + "\t" + procedureCost;
	}
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		Procedure p = (Procedure) obj;
		
		if (this.procedureName.equals(p.procedureName)){
			return true;
		}
		else {
			return false;
		}

	}
	
	
	
	// SETTERS & GETTERS

	public static void setUniqueID(int id){
		uniqueProcedureID = id;
	}
	
	public int getProcedureNo() {
		return procedureNo;
	}

	public void setProcedureNo(int procedureNo) {
		this.procedureNo = procedureNo;
	}


	
	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}


	
	public double getProcedureCost() {
		return procedureCost;
	}

	public void setProcedureCost(double procedureCost) {
		
		// rounding the given value to two decimal places
		BigDecimal bd = new BigDecimal(procedureCost);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		
		this.procedureCost = bd.doubleValue();
		
	}
	
	public String getProcedureCostInEuro(){
		return euro.format(procedureCost);
	}

}
