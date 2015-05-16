package style;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.Procedure;

/**
 * An implementation of ListCellRenderer which renders Procedure objects for a
 * combo-box.
 * 
 * @author Alan Sheehan - R00111909
 */
public class SimpleProcedureCellRenderer extends JLabel implements ListCellRenderer<Procedure> {

	private static final long serialVersionUID = 6802341850069188331L;

	
	/**
	 * Default constructor.
	 */
	public SimpleProcedureCellRenderer(){
		
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
		setPreferredSize(new Dimension(200, 15));
		
	}
	
	
	@Override
	public Component getListCellRendererComponent(
			JList<? extends Procedure> list, Procedure procedure, int index,
			boolean isSelected, boolean cellHasFocus) {
		
		if (procedure == null){
			return null;
		}
		
		if (isSelected){
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		
		setText(procedure.getProcedureName());
		
		return this;
		
	}



}
