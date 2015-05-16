package view;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import model.PatientTableModel;
import model.PaymentTableModel;
import model.ProcedureTableModel;
import style.SimpleTabPaneUI;
import style.SimpleTableCellRenderer;

/**
 * Displays the details of the patients in patient, procedure & payment tables.
 * 
 * @author Alan Sheehan - R00111909
 */
public class PatientDetailsView extends JSplitPane {
	
	private static final long serialVersionUID = 3381241529672624220L;

	
	private JScrollPane patientScroll, procedureScroll, paymentScroll;
	private JTable patientTable, procedureTable, paymentTable;
	
	private JTabbedPane patientTab, procedurePaymentTab;
	
	private Dimension minTableSize = new Dimension(400, 200);
	
	public final static int PROCEDURE_TAB = 0;
	public final static int PAYMENT_TAB = 1;
	
	
	/**
	 * Default constructor.
	 */
	public PatientDetailsView(){
		
		SimpleTableCellRenderer renderer = new SimpleTableCellRenderer();
		
		// creating and setting up tables
		patientTable = new JTable();
		patientTable.setFillsViewportHeight(true);
		patientTable.setAutoCreateRowSorter(true);
		patientTable.getTableHeader().setReorderingAllowed(false);
		patientTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		patientTable.setShowGrid(false);
		patientTable.setIntercellSpacing(new Dimension(0, 0));
		
		patientTable.setDefaultRenderer(Object.class, renderer);
		patientTable.setDefaultRenderer(Integer.class, renderer);
		
		
		procedureTable = new JTable();
		procedureTable.setFillsViewportHeight(true);
		procedureTable.setAutoCreateRowSorter(true);
		procedureTable.getTableHeader().setReorderingAllowed(false);
		procedureTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		procedureTable.setShowGrid(false);
		procedureTable.setIntercellSpacing(new Dimension(0, 0));
		
		procedureTable.setDefaultRenderer(Object.class, renderer);
		procedureTable.setDefaultRenderer(Double.class, renderer);
		procedureTable.setDefaultRenderer(Integer.class, renderer);
		
		
		paymentTable = new JTable();
		paymentTable.setFillsViewportHeight(true);
		paymentTable.setAutoCreateRowSorter(true);
		paymentTable.getTableHeader().setReorderingAllowed(false);
		paymentTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		paymentTable.setShowGrid(false);
		paymentTable.setIntercellSpacing(new Dimension(0, 0));
		
		paymentTable.setDefaultRenderer(Object.class, renderer);
		paymentTable.setDefaultRenderer(Boolean.class, renderer);
		
		
		
		
		// creating and setting up scroll panes for tables
		patientScroll = new JScrollPane(patientTable);
		patientScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		patientScroll.setMinimumSize(minTableSize);
		
		procedureScroll = new JScrollPane(procedureTable);
		procedureScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		procedureScroll.setMinimumSize(minTableSize);
		
		paymentScroll = new JScrollPane(paymentTable);
		paymentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paymentScroll.setMinimumSize(minTableSize);
		
		
		// creating and adding components to the tab panes
		procedurePaymentTab = new JTabbedPane();
		procedurePaymentTab.add("Procedures", procedureScroll);
		procedurePaymentTab.add("Payments", paymentScroll);
		
		patientTab = new JTabbedPane();
		patientTab.add("Patients", patientScroll);
		
		
		// setting custom UI to tab panes
		patientTab.setUI(new SimpleTabPaneUI(patientTab));
		procedurePaymentTab.setUI(new SimpleTabPaneUI(procedurePaymentTab));
		
		
		// setting the components to the split pane
		setLeftComponent(patientTab);
		setRightComponent(procedurePaymentTab);
		
		
		// setting custom UI to the split pane
		setResizeWeight(0.5);
		setDividerSize(7);
		setBorder(null);
		setContinuousLayout(true);
		
	}
	
	
	
	// ===========================================================
	// ==================== SETTERS & GETTERS ==================== 
	// ===========================================================
	
	public JTable getPatientTable(){
		return patientTable;
	}
	
	public JTable getProcedureTable(){
		return procedureTable;
	}
	
	public JTable getPaymentTable(){
		return paymentTable;
	}
	
	public void setPatientTableModel(PatientTableModel model){
		patientTable.setModel(model);
	}
	
	public void setProcedureTableModel(ProcedureTableModel model){
		procedureTable.setModel(model);
	}
	
	public void setPaymentTableModel(PaymentTableModel model){
		paymentTable.setModel(model);
	}
	
	public JTabbedPane getProcedurePaymentTab(){
		return procedurePaymentTab;
	}

}
