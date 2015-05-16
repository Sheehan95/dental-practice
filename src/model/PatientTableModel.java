package model;

import java.util.ArrayList;

/**
 * A table model for Patient objects.
 * 
 * @author Alan Sheehan - R00111909
 */
public class PatientTableModel extends CustomAbstractTableModel<Patient> {
	
	private static final long serialVersionUID = -6235450858756700465L;
	
	private String[] headers = {"#", "Name", "Address", "Phone #"};
	private ArrayList<Patient> patientList;
	
	
	/**
	 * The default constructor which calls {@link #PatientTableModel(ArrayList)} with
	 * an empty ArrayList if none is given.
	 */
	public PatientTableModel(){
		this(new ArrayList<Patient>());
	}
	
	/**
	 * Constructor which takes in an ArrayList of Patients and assigns it as the data.
	 * 
	 * @param data the ArrayList of Patients
	 */
	public PatientTableModel(ArrayList<Patient> data){
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
		else {
			return String.class;
		}
		
	}

	@Override
	public int getRowCount() {
		return patientList.size();
	}
	
	@Override
	public String getColumnName(int column) {
		return headers[column];
	}

	@Override
	public Object getValueAt(int row, int column) {
		
		Object value = null;
		Patient patient = get(row);
		
		switch(column){
		case 0:
			value = patient.getPatientNo();
			break;
		case 1:
			value = patient.getPatientName();
			break;
		case 2:
			value = patient.getPatientAddress();
			break;
		case 3:
			value = patient.getPatientPhone();
			break;
		}
		
		return value;
		
	}
	
	
	@Override
	public void add(Patient value) {
		patientList.add(value);	
		fireTableDataChanged();
	}

	@Override
	public Patient get(int index) {
		return patientList.get(index);
	}
	
	@Override
	public void set(int index, Patient value) {
		patientList.set(index, value);
		fireTableDataChanged();
	}
	
	@Override
	public int find(Patient patient){
		return patientList.indexOf(patient);
	}

	@Override
	public void remove(int index) {
		patientList.remove(index);
		fireTableDataChanged();
	}

	@Override
	public void clear() {
		patientList.clear();
		fireTableDataChanged();
	}

	@Override
	public void setData(ArrayList<Patient> data) {
		patientList = data;
		fireTableDataChanged();
	}
	
	@Override
	public String toString() {
		return patientList.toString();
	}

}
