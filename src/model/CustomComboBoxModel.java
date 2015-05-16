package model;

import javax.swing.DefaultComboBoxModel;

/**
 * An implementation of the ComboBoxModel for Procedures.
 * 
 * @author Alan Sheehan - R00111909
 */
public class CustomComboBoxModel extends DefaultComboBoxModel<Procedure> {

	private static final long serialVersionUID = -8389686249224234929L;
	
	private ProcedureList procedureList;
	
	@Override
	public Procedure getElementAt(int index) {
		return procedureList.get(index);
	}

	@Override
	public int getSize() {
		return procedureList.size();
	}


	/**
	 * Adds a new procedure to the list
	 * 
	 * @param procedure to be added
	 */
	public void addProcedure(Procedure procedure){
		procedureList.add(procedure);
		fireContentsChanged(this, 0, procedureList.size());
	}
	
	/**
	 * Removes a procedure from the list
	 * 
	 * @param index of procedure to be removed
	 */
	public void removeProcedure(int index){
		procedureList.remove(index);
		fireContentsChanged(this, 0, procedureList.size());
	}
	
	/**
	 * Clears all procedures from the list
	 */
	public void clear(){
		procedureList.clear();
		fireContentsChanged(this, 0, procedureList.size());
	}

	/**
	 * Sets the list
	 * 
	 * @param procedureList the list to be set
	 */
	public void setData(ProcedureList procedureList){
		this.procedureList = procedureList;
		fireContentsChanged(this, 0, procedureList.size());
	}
	
}
