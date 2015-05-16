package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import model.Patient;
import model.Payment;
import model.Procedure;

import org.sqlite.SQLiteConfig;


/**
 * Handles reading and writing Patient Lists & Procedure Lists to an SQLite 
 * database.
 * 
 * @author Alan Sheehan - R00111909
 */
public class DatabaseController {
	
	private static DatabaseController instance = new DatabaseController();
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	private Connection connection;
	private Statement statement;
	
	private static final int QUERY = 0;
	private static final int UPDATE = 1;
	
	private static final String DATABASE_ERROR = "Database Error";
	private static final String CRITICAL_ERROR = "Critical Error";
	private static final String ERROR = "Error";
	
	
	/**
	 * Default constructor which creates the database files & tables if they
	 * don't already exist. Set to private to enforce the singleton.
	 */
	private DatabaseController() {
		
		// establishing connection to the local SQLite database
		try {
			
			Class.forName("org.sqlite.JDBC");
			SQLiteConfig config = new SQLiteConfig();
			config.enforceForeignKeys(true);
			connection = DriverManager.getConnection("jdbc:sqlite:dentalpractice.db", config.toProperties());
			connection.setAutoCommit(false);
			
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to connect to SQLite Database - Stopping application now", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} catch (ClassNotFoundException e){
			JOptionPane.showMessageDialog(null, "SQLite JDBC not found - Stopping application now.", 
					CRITICAL_ERROR, JOptionPane.ERROR_MESSAGE);
			System.exit(2);
		}
		
		// creating tables in the database if they don't already exist
		try {
			
			String ddl = "CREATE TABLE Patients"
					+ "("
					+ "id INTEGER PRIMARY KEY NOT NULL,"
					+ "name TEXT NOT NULL,"
					+ "address TEXT NOT NULL,"
					+ "phone TEXT NOT NULL"
					+ ");"
					+ ""
					+ ""
					+ "CREATE TABLE Procedures"
					+ "("
					+ "id INTEGER PRIMARY KEY NOT NULL,"
					+ "name TEXT UNIQUE NOT NULL,"
					+ "price DOUBLE NOT NULL"
					+ ");"
					+ ""
					+ ""
					+ "CREATE TABLE Payments"
					+ "("
					+ "patientID INTEGER NOT NULL,"
					+ "id INTEGER PRIMARY KEY NOT NULL,"
					+ "amount DOUBLE NOT NULL,"
					+ "date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "paid BOOLEAN NOT NULL DEFAULT FALSE,"
					+ "FOREIGN KEY(patientID) REFERENCES Patients(id) ON DELETE CASCADE ON UPDATE CASCADE"
					+ ");"
					+ ""
					+ ""
					+ "CREATE TABLE ProcedureList"
					+ "("
					+ "patientID INTEGER NOT NULL,"
					+ "procedureID INTEGER NOT NULL,"
					+ "PRIMARY KEY(patientID, procedureID),"
					+ "FOREIGN KEY(patientID) REFERENCES Patients(id) ON DELETE CASCADE ON UPDATE CASCADE,"
					+ "FOREIGN KEY(procedureID) REFERENCES Procedures(id) ON DELETE CASCADE ON UPDATE CASCADE"
					+ ");"
					+ ""
					+ ""
					+ "CREATE UNIQUE INDEX UniquePayments ON Payments(patientID, id);";
			
			statement = connection.createStatement();
			statement.executeUpdate(ddl);
			connection.commit();
			
		} catch (SQLException e){
			// fails if table already exists, which is perfectly fine
		}
		
	}
	
	/**
	 * Returns the single instance of the {@link DatabaseController}.
	 * 
	 * @return an instance of the {@link DatabaseController}
	 */
	public static DatabaseController getInstance(){
		return instance;
	}
	
	
	
	/**
	 * Executes the given SQL statement. Returns a result set if the type is 
	 * set to {@link #QUERY} or null if the type is {@link #UPDATE}.
	 * 
	 * @param sql the sql statement
	 * @param type the type the statement is
	 * @return a result set if the type is {@link #QUERY}, null otherwise
	 */
	private ResultSet execute (String sql, int type) throws SQLException {
		
		ResultSet result = null;

		statement = connection.createStatement();
		
		if (type == UPDATE){
			statement.executeUpdate(sql);
		}
		else if (type == QUERY){
			result = statement.executeQuery(sql);
		}
		
		connection.commit();
		
		return result;
		
	}
	
	
	/**
	 * Reads all of the patients from the database.
	 * 
	 * @return the patient list
	 */
	public ArrayList<Patient> readPatientList() {
		
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		
		String sql = "SELECT * FROM Patients;";
		
		try {
			
			ResultSet result = execute(sql, QUERY);
			
			while (result.next()){
				
				int id = result.getInt("id");
				String name = result.getString("name");
				String address = result.getString("address");
				String phone = result.getString("phone");
				
				Patient patient = new Patient(id, name, address, phone);
				patient.setPatientProcedureList(readPatientProcedureList(id));
				patient.setPatientPaymentList(readPatientPaymentList(id));
				
				patientList.add(patient);
				
			}
			
		} catch (Exception e){
			JOptionPane.showMessageDialog(null, "Failed to read patient list.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return new ArrayList<Patient>();
		}
		
		return patientList;
		
	}
	
	
	/**
	 * Reads all of the procedures a given patient has.
	 * 
	 * @param patientID of the patient who's procedures will be retrieved
	 * @return the procedure list of the patient
	 */
	public ArrayList<Procedure> readPatientProcedureList(int patientID) {
		
		ArrayList<Procedure> procedureList = new ArrayList<Procedure>();
		
		String sql = 	"SELECT P.* " + 
						"FROM Procedures P, ProcedureList PL " +
						"WHERE P.id = PL.procedureID AND PL.patientID = %s;";
		
		sql = String.format(sql, patientID);
		
		try {
			
			ResultSet result = execute(sql, QUERY);
			
			while (result.next()){
				
				int id = result.getInt("id");
				String name = result.getString("name");
				double price = result.getDouble("price");
				
				procedureList.add(new Procedure(id, name, price));
				
			}
			
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to read patient's procedure list.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return new ArrayList<Procedure>();
		}
		
		return procedureList;
	}
	
	
	/**
	 * Reads all of the payments a given patient has.
	 * 
	 * @param patientID of the patient who's payments will be retrieved
	 * @return the payment list of the patient
	 */
	public ArrayList<Payment> readPatientPaymentList(int patientID) {
		
		ArrayList<Payment> paymentList = new ArrayList<Payment>();
		
		String sql = 	"SELECT * " + 
						"FROM Payments " +
						"WHERE patientID = %s;";
		
		sql = String.format(sql, patientID);
		
		try {
			
			ResultSet result = execute(sql, QUERY);
			
			while (result.next()){
				
				int id = result.getInt("id");
				double amount = result.getDouble("amount");
				Date date = formatter.parse(result.getString("date"));
				boolean status = result.getBoolean("paid");
				
				paymentList.add(new Payment(id, amount, status, date));
				
			}
			
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to read patient's payment list.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
		} catch (ParseException e){
			JOptionPane.showMessageDialog(null, "Failed to read payment date.", 
					ERROR, JOptionPane.ERROR_MESSAGE);
			return new ArrayList<Payment>();
		}
		
		return paymentList;
		
	}
	
	
	/**
	 * Reads all of the procedures from the database.
	 * 
	 * @return the procedure list
	 */
	public ArrayList<Procedure> readProcedureList(){
		
		ArrayList<Procedure> procedureList = new ArrayList<Procedure>();
		
		String sql = "SELECT * FROM Procedures;";
		
		try {
			
			ResultSet result = execute(sql, QUERY);
			
			while (result.next()){
				
				int id = result.getInt("id");
				String name = result.getString("name");
				double price = result.getDouble("price");
				
				procedureList.add(new Procedure(id, name, price));
				
			}
			
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to read procedure list.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return new ArrayList<Procedure>();
		}
		
		return procedureList;
		
	}
	
	
	
	/*
	 * =====================================
	 * ======== Patient-Related SQL ========
	 * =====================================
	 */
	
	/**
	 * Retrieves the patient with the given id from the database.
	 * 
	 * @param patientID of the patient to be retrieved
	 * @return the patient with the given id, null if no such patient exists
	 */
	public Patient getPatient(int patientID) {
		
		Patient patient = null;
		
		String sql = "SELECT * FROM Patients WHERE id=%s;";
		sql = String.format(sql, patientID);
		
		try {
			
			ResultSet result = execute(sql, QUERY);
			
			// no data - patient doesn't exist
			if (!result.isBeforeFirst()){
				return null;
			}
			
			int id = result.getInt("id");
			String name = result.getString("name");
			String address = result.getString("address");
			String phone = result.getString("phone");
			
			patient = new Patient(id, name, address, phone);
			
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to read patient.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
		}
		
		return patient;
		
	}
	
	/**
	 * Retrieves the most recently added patient entry in the database.
	 * 
	 * @return the most recently added patient
	 */
	public Patient mostRecentPatientEntry(){
		
		Patient patient = null;
		
		String sql =	"SELECT * FROM Patients " +
						"WHERE id IN (SELECT MAX(id) FROM Patients);";
		
		try {
			
			ResultSet result = execute(sql, QUERY);
			
			// no data - patient doesn't exist
			if (!result.isBeforeFirst()){
				return null;
			}
			
			int id = result.getInt("id");
			String name = result.getString("name");
			String address = result.getString("address");
			String phone = result.getString("phone");
			
			patient = new Patient(id, name, address, phone);
			
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "There are no entries in the patient table.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
		}
		
		return patient;
		
	}
	
	/**
	 * Inserts a new patient into the database.
	 * 
	 * @param name of the patient
	 * @param address of the patient
	 * @param phone of the patient
	 * 
	 * @return true if the insertion was successful, false otherwise
	 */
	public boolean insertPatient(String name, String address, String phone){
		
		// Wrapping ' around strings for insertion
		name = String.format("'%s'", name);
		address = String.format("'%s'", address);
		phone = String.format("'%s'", phone);
		
		String sql =	"INSERT INTO Patients (name, address, phone) " +
						"VALUES (%1$s, %2$s, %3$s);";
		sql = String.format(sql, name, address, phone);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to insert patient.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	
	/**
	 * Updates the given patient's details in the table.
	 * 
	 * @param patient to be updated
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updatePatient(Patient patient) {
		
		int id = patient.getPatientNo();
		String name = patient.getPatientName();
		String address = patient.getPatientAddress();
		String phone = patient.getPatientPhone();
		
		name = String.format("'%s'", name);
		address = String.format("'%s'", address);
		phone = String.format("'%s'", phone);
		
		String sql =	"UPDATE Patients SET " + 
						"name=%2$s, "+
						"address=%3$s, " +
						"phone=%4$s " + 
						"WHERE id=%1$s;";
		
		sql = String.format(sql, id, name, address, phone);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to update patient.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	
	/**
	 * Calls {@link #deletePatient(int)} with the given patient's id.
	 * 
	 * @param patient to be deleted
	 * @return true if the deletion was successful, false otherwise
	 */
	public boolean deletePatient(Patient patient) {
		return deletePatient(patient.getPatientNo());
	}
	
	/**
	 * Deletes all records associated with the given patient id.
	 * 
	 * @param id of the patient to be deleted
	 * @return true if the deletion was successful, false otherwise
	 */
	public boolean deletePatient(int id) {
		
		String sql = "DELETE FROM patients WHERE id=%s;";
		sql = String.format(sql, id);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to delete patient.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	
	
	
	/* 
	 * =======================================
	 * ======== Procedure-Related SQL ========
	 * =======================================
	 */
	
	/**
	 * Retrieves the procedure with the given id from the database.
	 * 
	 * @param procedureID of the procedure to be retrieved
	 * @return the procedure with the given id, null if no such procedure exists
	 */
	public Procedure getProcedure(int procedureID) {
		
		Procedure procedure = null;
		
		String sql = "SELECT * FROM Procedures WHERE id=%s;";
		sql = String.format(sql, procedureID);
		
		try {
			
			ResultSet result = execute(sql, QUERY);
			
			// no data - procedure doesn't exist
			if (!result.isBeforeFirst()){
				return null;
			}
			
			int id = result.getInt("id");
			String name = result.getString("name");
			double price = result.getDouble("price");
			
			procedure = new Procedure(id, name, price);
			
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to read procedure.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
		}
		
		return procedure;
		
	}
	
	/**
	 * Inserts a new procedure into the database.
	 * 
	 * @param name of the procedure
	 * @param price of the procedure
	 * @return true if the insertion was successful, false otherwise
	 */
	public boolean insertProcedure(String name, double price){
		
		// Wrapping ' around strings for insertion
		name = String.format("'%s'", name);
		
		String sql =	"INSERT INTO Procedures (name, price) " +
						"VALUES (%1$s, %2$.2f);";
		sql = String.format(sql, name, price);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			
			String message = "";
					
			if (e.getMessage().split(" ")[0].equals("UNIQUE")){
				message = "Two procedures cannot share the same name.";
			}
			else {
				message = "Failed to insert procedure.";
			}
			
			JOptionPane.showMessageDialog(null, message, DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			
			return false;
			
		}
		
	}
	
	/**
	 * Updates the given procedure's details in the table.
	 * 
	 * @param procedure to be updated
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updateProcedure(Procedure procedure) {
		
		int id = procedure.getProcedureNo();
		String name = procedure.getProcedureName();
		double price = procedure.getProcedureCost();
		
		name = String.format("'%s'", name);
		
		String sql =	"UPDATE Procedures SET " + 
						"name=%2$s, "+
						"price=%3$.2f " +
						"WHERE id=%1$s;";
		
		sql = String.format(sql, id, name, price);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to update procedure.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	
	/**
	 * Calls {@link #deleteProcedure(int)} with the given procedure's id.
	 * 
	 * @param procedure to be deleted
	 * @return true if the deletion was successful, false otherwise
	 */
	public boolean deleteProcedure(Procedure procedure) {
		return deleteProcedure(procedure.getProcedureNo());
	}
	
	/**
	 * Deletes all records associated with the given procedure id.
	 * 
	 * @param id of the procedure to be deleted
	 * @return true if the deletion was successful, false otherwise
	 */
	public boolean deleteProcedure(int id) {
		
		String sql = "DELETE FROM Procedures WHERE id=%s;";
		sql = String.format(sql, id);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to delete procedure.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	
	
	
	/* 
	 * ===========================================
	 * ======== ProcedureList-Related SQL ========
	 * ===========================================
	 */
	
	/**
	 * Inserts a new procedure into the database.
	 * 
	 * @param name of the procedure
	 * @param price of the procedure
	 * @return true if the insertion was successful, false otherwise
	 */
	public boolean addProcedureToPatient(int patientID, int procedureID){
		
		String sql =	"INSERT INTO ProcedureList (patientID, procedureID) " +
						"VALUES (%1$s, %2$s);";
		sql = String.format(sql, patientID, procedureID);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			
			String message = "";
					
			if (e.getMessage().split(" ")[0].equals("UNIQUE")){
				message = "A single patient cannot be scheduled for the same procedure.";
			}
			else {
				message = "Failed to insert procedure.";
			}
			
			JOptionPane.showMessageDialog(null, message, ERROR, JOptionPane.ERROR_MESSAGE);
			
			return false;
			
		}
		
	}
	
	/**
	 * Updates the given procedure's details in the table.
	 * 
	 * @param procedure to be updated
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updateProcedureOfPatient(int patientID, int procedureID, int oldProcedureID) {
		
		String sql =	"UPDATE ProcedureList SET " + 
						"procedureID=%2$s "+
						"WHERE patientID=%1$s AND procedureID=%3$s;";
		
		sql = String.format(sql, patientID, procedureID, oldProcedureID);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			
			String message = "";
			
			if (e.getMessage().split(" ")[0].equals("UNIQUE")){
				message = "A single patient cannot be scheduled for the same procedure.";
			}
			else {
				message = "Failed to insert procedure.";
			}
			
			JOptionPane.showMessageDialog(null, message, ERROR, JOptionPane.ERROR_MESSAGE);
			
			return false;
			
		}
		
	}
	
	/**
	 * Deletes all records associated with the given procedure id.
	 * 
	 * @param id of the procedure to be deleted
	 * @return true if the deletion was successful, false otherwise
	 */
	public boolean removeProcedureFromPatient(int patientID, int procedureID) {
		
		String sql = "DELETE FROM ProcedureList WHERE patientID=%1$s AND procedureID=%2$s;";
		sql = String.format(sql, patientID, procedureID);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to delete procedure.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	
	
	/*
	 * =====================================
	 * ======== Payment-Related SQL ========
	 * =====================================
	 */
	
	
	/**
	 * Retrieves the payment with the given id from the database.
	 * 
	 * @param paymentID of the payment to be retrieved
	 * @return the payment with the given id, null if no such payment exists
	 */
	public Payment getPayment(int paymentID) {
		
		Payment payment = null;
		
		String sql = "SELECT * FROM Payments WHERE id=%s;";
		sql = String.format(sql, paymentID);
		
		try {
			
			ResultSet result = execute(sql, QUERY);
			
			// no data - payment doesn't exist
			if (!result.isBeforeFirst()){
				return null;
			}
			
			int id = result.getInt("id");
			double amount = result.getDouble("amount");
			Date date = formatter.parse(result.getString("date"));
			boolean status = result.getBoolean("paid");
			
			payment = new Payment(id, amount, status, date);
			
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to read payment.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
		} catch (ParseException e){
			JOptionPane.showMessageDialog(null, "Failed to read payment date.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
		}
		
		return payment;
		
	}
	
	/**
	 * Inserts a new payment into the database.
	 * 
	 * @param patientID the id of the patient making the payment
	 * @param amount of money being paid
	 * @param status whether the money has been paid or not
	 * 
	 * @return true if the insertion was successful, false otherwise
	 */
	public boolean insertPayment(int patientID, double amount, boolean status){
		
		int paid = status == true ? 1:0;
		
		String sql =	"INSERT INTO Payments (patientID, amount, date, paid) " +
						"VALUES (%1$s, %2$.2f, CURRENT_TIMESTAMP, %3$s);";
		sql = String.format(sql, patientID, amount, paid);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to insert payment.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	
	/**
	 * Updates the given payment's details in the table.
	 * 
	 * @param payment to be updated
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updatePayment(Payment payment) {
		
		int id = payment.getPaymentNo();
		double amount = payment.getPaymentAmount();
		boolean status = payment.getPaymentStatus();
		
		int paid = status == true ? 1:0;
		
		String sql =	"UPDATE Payments SET " + 
						"amount=%2$.2f, " +
						"paid=%3$s " +
						"WHERE id=%1$s;";
		
		sql = String.format(sql, id, amount, paid);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to update payment.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	
	/**
	 * Calls {@link #deletePayment(int)} with the given payment's id.
	 * 
	 * @param payment to be deleted
	 * @return true if the deletion was successful, false otherwise
	 */
	public boolean deletePayment(Payment payment) {
		return deletePayment(payment.getPaymentNo());
	}
	
	/**
	 * Deletes all records associated with the given procedure id.
	 * 
	 * @param id of the payment to be deleted
	 * @return true if the deletion was successful, false otherwise
	 */
	public boolean deletePayment(int id) {
		
		String sql = "DELETE FROM Payments WHERE id=%s;";
		sql = String.format(sql, id);
		
		try {
			execute(sql, UPDATE);
			return true;
		} catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Failed to delete payment.", 
					DATABASE_ERROR, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	
}
