package model;

import java.util.ArrayList;

/**
 * A table model for Payment objects.
 * 
 * @author Alan Sheehan - R00111909
 */
public class PaymentTableModel extends CustomAbstractTableModel<Payment> {
	
	private static final long serialVersionUID = -8495420559534666122L;
	
	private String[] headers = {"#", "Amount Due", "Date", "Paid"};
	private ArrayList<Payment> paymentList;
	
	
	/**
	 * The default constructor which calls {@link #PaymentTableModel(ArrayList)} with
	 * an empty ArrayList if none is given.
	 */
	public PaymentTableModel(){
		this(new ArrayList<Payment>());
	}
	
	/**
	 * Constructor which takes in an ArrayList of Payments and assigns it as the data.
	 * 
	 * @param data the ArrayList of Payments
	 */
	public PaymentTableModel(ArrayList<Payment> data){
		super();
		setData(data);
	}
	
	
	
	@Override
	public int getColumnCount() {
		return headers.length;
	}
	
	@Override
	public Class<?> getColumnClass(int column) {
		
//		if (column == 0){
//			return Integer.class;
//		}
//		else if (column == 1){
//			return String.class;
//		}
//		else if (column == 2){
//			return Date.class;
//		}
//		else {
//			return Boolean.class;
//		}
		
		if (column == 3){
			return Boolean.class;
		}
		
		return Object.class;
		
	}

	@Override
	public int getRowCount() {
		return paymentList.size();
	}
	
	@Override
	public String getColumnName(int column) {
		return headers[column];
	}

	@Override
	public Object getValueAt(int row, int column) {
		
		Object value = null;
		Payment payment = get(row);
		
		switch(column){
		case 0:
			value = payment.getPaymentNo();
			break;
		case 1:
			value = payment.getPaymentAmount();
			break;
		case 2:
			value = payment.getPaymentDate();
			break;
		case 3:
			value = payment.getPaymentStatus();
			break;
		}
		
		return value;
		
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		
		return true;

	}

	
	@Override
	public void add(Payment value) {
		paymentList.add(value);
		fireTableDataChanged();
	}

	@Override
	public Payment get(int index) {
		return paymentList.get(index);
	}
	
	@Override
	public void set(int index, Payment payment) {
		paymentList.set(index, payment);
		fireTableDataChanged();
	}
	
	@Override
	public int find(Payment payment){
		return paymentList.indexOf(payment);
	}

	@Override
	public void remove(int index) {
		paymentList.remove(index);
		fireTableDataChanged();
	}

	@Override
	public void clear() {
		paymentList.clear();
		fireTableDataChanged();
	}

	@Override
	public void setData(ArrayList<Payment> data) {
		paymentList = data;
		fireTableDataChanged();
	}
	
	public ArrayList<Payment> getModel(){
		return paymentList;
	}
	
}
