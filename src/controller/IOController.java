package controller;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import model.Patient;
import model.Procedure;
import model.ProcedureList;

/**
 * Handles reading and writing Patient Lists & Procedure Lists to files in XML.
 * 
 * @author Alan Sheehan - R00111909
 */
public class IOController {

	private JAXBContext patientListContext;
	private JAXBContext procedureListContext;
	
	private File patientListFile = new File("patientList.xml");
	private File procedureListFile = new File("procedureList.xml");
	
	
	/**
	 * Default constructor which creates patientList & procedureList files if
	 * they don't already exist.
	 */
	public IOController () {
		
		// creates a new JAXBContext object
		try {
			
			patientListContext = JAXBContext.newInstance(PatientListWrapper.class);
			procedureListContext = JAXBContext.newInstance(ProcedureListWrapper.class);
			
		} catch (JAXBException e){
			
			JOptionPane.showMessageDialog(null, "Failed to create JAXB context. Saving is disabled for this session:\n" + e.getMessage(), 
					"Critical Error", JOptionPane.ERROR_MESSAGE);
			
		}
		
		
		// creates the patientList file if one doesn't already exist
		if (! patientListFile.exists()){
			writePatientList (new ArrayList<Patient>());
		}
		
		// creates the procedureList file if one doesn't already exist
		if (! procedureListFile.exists()){
			writeProcedureList ();
		}
		
	}
	
	
	/**
	 * Saves the given ArrayList of Patients to the file "patientList.xml".
	 * 
	 * @param toSave the data to be saved
	 */
	public void writePatientList (ArrayList<Patient> toSave){
		
		try {
			
			Marshaller out = patientListContext.createMarshaller();
			out.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			out.marshal(new PatientListWrapper(toSave), patientListFile);
			
		} catch (JAXBException e) {
			
			JOptionPane.showMessageDialog(null, "Failed to write Patient List. Confirm that the file \"patientList.xml\" exists:\n" + e.getMessage(), 
					"Critical Error", JOptionPane.ERROR_MESSAGE);
			
		}
		
	}
	
	
	/**
	 * Reads the contents of the file "patientList.xml" and returns the data
	 * in the form of ArrayList<Patient>.
	 * 
	 * @return the patientList unmarshalled from the file
	 */
	public ArrayList<Patient> readPatientList (){
		
		try {
			
			Unmarshaller in = patientListContext.createUnmarshaller();
			PatientListWrapper wrapper =  (PatientListWrapper) in.unmarshal(patientListFile);
			ArrayList<Patient> patientList = wrapper.patientList;
			
			// sets the uniqueID, used when constructing Patient objects, to the highest Patient ID read
			if (patientList.size() != 0){
				Patient.setUniqueID(patientList.get(patientList.size() - 1).getPatientNo() + 1);
			}
			
			return patientList;
			
		} catch (JAXBException e) {
			
			JOptionPane.showMessageDialog(null, "Failed to read Patient List. Confirm that the file \"patientList.xml\" exists:\n" + e.getMessage(), 
					"Critical Error", JOptionPane.ERROR_MESSAGE);
			
			return new ArrayList<Patient>();
			
		}

	}
	
	
	/**
	 * Saves the given ArrayList<Patient> to the file "patientList.xml".
	 * 
	 * @param toSave the data to be saved
	 */
	public void writeProcedureList (){
		
		ArrayList<Procedure> toSave = ProcedureList.getInstance();
		
		try {
			
			Marshaller out = procedureListContext.createMarshaller();
			out.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			out.marshal(new ProcedureListWrapper(toSave), procedureListFile);
			
		} catch (JAXBException e) {
			
			JOptionPane.showMessageDialog(null, "Failed to save Procedure List. Confirm the file \"procedureList.xml\" exists:\n" + e.getMessage(), 
					"Critical Error", JOptionPane.ERROR_MESSAGE);
			
		}
		
	}
	
	
	/**
	 * Reads the contents of the file "patientList.xml" and returns the data
	 * in the form of an ArrayList of Patients.
	 * 
	 * @return the patientList unmarshalled from the file
	 */
	public void readProcedureList (){
		
		try {
			
			Unmarshaller in = procedureListContext.createUnmarshaller();
			ProcedureListWrapper wrapper =  (ProcedureListWrapper) in.unmarshal(procedureListFile);
			ArrayList<Procedure> procedureList = wrapper.procedureList;
			
			ProcedureList.setProcedureList(procedureList);
			
			// sets the uniqueID, used when constructing Procedure objects, to the highest Procedure ID read
			if (procedureList.size() != 0){
				Procedure.setUniqueID(procedureList.get(procedureList.size() - 1).getProcedureNo() + 1);
			}
			
		} catch (JAXBException e) {
			
			JOptionPane.showMessageDialog(null, "Failed to read Procedure List. Confirm the file \"procedureList.xml\" exists:\n" + e.getMessage(), 
					"Critical Error", JOptionPane.ERROR_MESSAGE);
			
		}

	}
	
	
	/**
	 * Inner class which acts as a wrapper class for an ArrayList<Patient>
	 * class when writing to a file in XML.
	 * 
	 * @author Alan Sheehan - R00111909
	 */
	@XmlRootElement (name = "PatientList")
	private static class PatientListWrapper {
		
		@XmlElement (name = "Patient")
		private ArrayList<Patient> patientList = new ArrayList<Patient>();
		
		/**
		 * Default constructor set to private - only used by JAXB.
		 */
		@SuppressWarnings("unused")
		private PatientListWrapper(){}
		
		
		/**
		 * Wraps the passed in ArrayList<Patient> into a format that can be
		 * saved by XML.
		 * 
		 * @param patientList to be wrapped
		 */
		public PatientListWrapper (ArrayList<Patient> patientList){
			this.patientList = patientList;
		}

	}
	
	
	/**
	 * Inner class which acts as a wrapper class for an ArrayList<Procedure>
	 * class when writing to a file in XML.
	 * 
	 * @author Alan Sheehan - R00111909
	 */
	@XmlRootElement (name = "ProcedureList")
	private static class ProcedureListWrapper {
		
		@XmlElement (name = "Procedure")
		private ArrayList<Procedure> procedureList = new ArrayList<Procedure>();
		
		/**
		 * Default constructor set to private - only used by JAXB.
		 */
		@SuppressWarnings("unused")
		private ProcedureListWrapper (){}
		
		
		/**
		 * Wraps the passed in ArrayList<Procedure> into a format that can be
		 * saved by XML.
		 * 
		 * @param procedureList to be wrapped
		 */
		public ProcedureListWrapper (ArrayList<Procedure> procedureList){
			this.procedureList = procedureList;
		}
		
	}
	

}
