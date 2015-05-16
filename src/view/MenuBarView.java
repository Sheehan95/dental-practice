package view;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBarView extends JMenuBar {

	private static final long serialVersionUID = -1067207478703037329L;

	private JMenu fileMenu, reportMenu, generateMenu, adminMenu;
	
	private JMenuItem fileSave;
	
	private JMenuItem reportOpen;
	
	private JMenuItem generateComplete, generateOverdue;
	
	private JMenuItem procedureAdd, procedureRemove;

	
	/**
	 * Default constructor.
	 */
	public MenuBarView(){
		
		setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
		
		// creating menus
		fileMenu = new JMenu("File");
		reportMenu = new JMenu("Report");
		generateMenu = new JMenu("Generate");
		adminMenu = new JMenu("Administration");
		
		
		// creating "File" menu items
		fileSave = new JMenuItem("Save");
		
		// creating "Generate" menu items
		generateComplete = new JMenuItem("Complete Report");
		generateOverdue = new JMenuItem("Overdue Report");
		
		// creating "Report" menu items
		reportOpen = new JMenuItem("Open");
		
		// creating "Administration" menu items
		procedureAdd = new JMenuItem("Add Procedure");
		procedureRemove = new JMenuItem("Remove Procedure");
		
		
		// populating "File" menu
		fileMenu.add(fileSave);

		
		// populating "Generate" menu
		generateMenu.add(generateComplete);
		generateMenu.add(generateOverdue);
		
		
		// populating "Report" menu
		reportMenu.add(reportOpen);
		reportMenu.add(generateMenu);
		
		// populating "Administration" menu
		adminMenu.add(procedureAdd);
		adminMenu.add(procedureRemove);
		
		
		// adding menus to MenuBar
		add(fileMenu);
		add(reportMenu);
		add(adminMenu);
		
		fileMenu.setVisible(false);
		reportOpen.setVisible(false);
		
	}
	
	
	
	// ===========================================================
	// ==================== SETTERS & GETTERS ==================== 
	// ===========================================================
	
	public JMenuItem getSaveMenuItem (){
		return fileSave;
	}
	
	
	public JMenuItem getGenerateCompleteMenuItem (){
		return generateComplete;
	}
	
	public JMenuItem getGenerateOverdueMenuItem (){
		return generateOverdue;
	}
	
	
	public JMenuItem getProcedureAddMenuItem (){
		return procedureAdd;
	}
	
	public JMenuItem getProcedureRemoveMenuItem (){
		return procedureRemove;
	}

}
