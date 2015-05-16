package model;

import java.util.ArrayList;

/**
 * A table model for Procedure objects.
 * 
 * @author Alan Sheehan - R00111909
 */
public class ProcedureTableModel extends CustomAbstractTableModel <Procedure> {
	
	private static final long serialVersionUID = 2051503359049560500L;
	
	private String[] headers = {"#", "Name", "Price"};
	private ArrayList<Procedure> procedureList;
	
	
	/**
	 * The default constructor which calls {@link #ProcedureTableModel(ArrayList)} with
	 * an empty ArrayList if none is given.
	 */
	public ProcedureTableModel(){
		this(new ArrayList<Procedure>());
	}
	
	/**
	 * Constructor which takes in an ArrayList of Procedures and assigns it as the data.
	 * 
	 * @param data the ArrayList of Procedures
	 */
	public ProcedureTableModel(ArrayList<Procedure> data){
		super();
		setData(data);
	}
	
	
	@Override
	public int getColumnCount() {
		return headers.length;
	}
	
	@Override
	public Class<?> getColumnClass(int column) {
		
		if (column == 0){
			return Integer.class;
		}
		else if (column == 2){
			return Double.class;
		}
		else {
			return String.class;
		}
		
	}

	@Override
	public int getRowCount() {
		return procedureList.size();
	}
	
	@Override
	public String getColumnName(int column) {
		return headers[column];
	}

	@Override
	public Object getValueAt(int row, int column) {
		
		Object value = null;
		Procedure procedure = get(row);
		
		switch(column){
		case 0:
			value = procedure.getProcedureNo();
			break;
		case 1:
			value = procedure.getProcedureName();
			break;
		case 2:
			value = procedure.getProcedureCost();
			break;
		}
		
		return value;
		
	}

	
	@Override
	public void add(Procedure value) {
		procedureList.add(value);
		fireTableDataChanged();
	}
	
	@Override
	public Procedure get(int index) {
		return procedureList.get(index);
	}
	
	@Override
	public void set(int row, Procedure procedure){
		procedureList.set(row, procedure);
		fireTableDataChanged();
	}
	
	@Override
	public int find(Procedure procedure){
		return procedureList.indexOf(procedure);
	}

	@Override
	public void remove(int index) {
		procedureList.remove(index);
		fireTableDataChanged();
	}

	@Override
	public void clear() {
		procedureList.clear();
		fireTableDataChanged();
	}

	@Override
	public void setData(ArrayList<Procedure> data) {
		procedureList = data;
		fireTableDataChanged();
	}
	

	
	public ArrayList<Procedure> getModel(){
		return procedureList;
	}

}