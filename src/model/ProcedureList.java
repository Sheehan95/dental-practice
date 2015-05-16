package model;

import java.util.ArrayList;

/**
 * A singleton class providing access to all Procedures on the system.
 * 
 * @author Alan Sheehan - R00111909
 */
public class ProcedureList extends ArrayList<Procedure>{

	private static final long serialVersionUID = -584074620516313870L;
	
	private static ProcedureList list = new ProcedureList();

	
	/**
	 * Default constructor set to private to create singleton.
	 */
	private ProcedureList(){}
	
	
	/**
	 * Retrieves the instance.
	 * 
	 * @return the list of Procedures
	 */
	public static ProcedureList getInstance(){
		
		return list;
		
	}
	
	/**
	 * Sets the list of Procedures to a new list.
	 * 
	 * @param procedureList to be set
	 */
	public static void setProcedureList (ArrayList<Procedure> procedureList){
		
		list.clear();
		
		for (Procedure p : procedureList){
			list.add(p);
		}
		
	}
	
	
	@Override
	public String toString(){
		String details = "";
		
		for (Procedure p : list){
			details += p + "\n";
		}
		
		return details;
	}
	
}
