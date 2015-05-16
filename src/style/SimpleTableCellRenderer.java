package style;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * An implementation of DefaultTableCellRenderer.
 * 
 * @author Alan
 *
 */
public class SimpleTableCellRenderer extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 6987073787682693156L;
	
	
	private static final Color SELECTED_CELL_COLOR = new Color(0/255f, 132/255f, 227/255f, .8f);
	private static final Color ODD_CELL_COLOR = new Color(215, 215, 215);
	private static final Color EVEN_CELL_COLOR = new Color(245, 245, 245);
	
	private DecimalFormat euro = new DecimalFormat("€###,###.00");

	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		JLabel cell = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// sets every second row to a different shade of gray
		if (row % 2 == 0){
			cell.setBackground(EVEN_CELL_COLOR);
			cell.setForeground(Color.BLACK);
		}
		else {
			cell.setBackground(ODD_CELL_COLOR);
			cell.setForeground(Color.BLACK);
		}
		
		// highlights the selected row
		if (row == table.getSelectedRow()){
			cell.setBackground(SELECTED_CELL_COLOR);
			cell.setForeground(Color.WHITE);
		}
		
		if (hasFocus){
			cell.setBorder(null);
		}
		
		cell.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		
		// rendering for strings
		if (value instanceof String){
			
			String text = (String) value;
			
			cell.setText(text);
			
			return cell;
			
		}
		// rendering for integers
		else if (value instanceof Integer){
			
			cell.setText(value.toString());
			
			return cell;
			
		}
		// rendering for doubles
		else if (value instanceof Double){
			
			cell.setText(euro.format((Double) value));
			
			return cell;
			
		}
		// rendering for booleans
		else if (value instanceof Boolean){
			
			JCheckBox tick = new JCheckBox();
			
			Boolean b = (Boolean) value;
			
			tick.setSelected(b);
			tick.setOpaque(false);
			tick.setHorizontalAlignment(SwingConstants.CENTER);
			
			// creating new container for checkbox to avoid issues
			// with painting backgrounds that have an Alpha value
			JPanel container = new JPanel();
			
			if (row % 2 == 0){
				container.setBackground(EVEN_CELL_COLOR);
				container.setForeground(Color.BLACK);
			}
			else {
				container.setBackground(ODD_CELL_COLOR);
				container.setForeground(Color.BLACK);
			}
			
			// highlights the selected row
			if (row == table.getSelectedRow()){
				container.setBackground(SELECTED_CELL_COLOR);
				container.setForeground(Color.WHITE);
			}

			container.setLayout(new BorderLayout());
			container.add(tick);
			
			return container;
			
		}
		// rendering for dates
		else if (value instanceof Date){
			
			Date date = (Date) value;
			
			cell.setText(new SimpleDateFormat("dd/MM/yyyy    @    HH:mm:ss").format(date));
			
			return cell;
			
		}
		// default rendering for all other objects
		else {
			cell.setText(value.toString());
			return cell;
		}
		
	}
	
}
