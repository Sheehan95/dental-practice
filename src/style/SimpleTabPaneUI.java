package style;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 * An implementation of the BasicTabbedPaneUI.
 * 
 * @author Alan Sheehan - R00111909
 */
public class SimpleTabPaneUI extends BasicTabbedPaneUI {
	
	private JTabbedPane tabbedPane;
	
	private final Color BACKGROUND_TAB_COLOUR = new Color(209, 209, 209);
	
	
	public SimpleTabPaneUI(JTabbedPane tabbedPane){
		super();
		this.tabbedPane = tabbedPane;
	}
	
	
	@Override
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
			int x, int y, int w, int h, boolean isSelected) {
		
		g.setColor(Color.GRAY);
		
		// drawing left side of tab, if it's the first tab make the line 5 pixels
		if (tabIndex == 0){
			g.drawLine(x, y, x, y + h + 5);
		}
		else {
			g.drawLine(x, y, x, y + h);
		}
		
		// drawing right side of tab
		g.drawLine(x + w, y, x + w, y + h);
		
		// drawing top side of tab
		g.drawLine(x, y, x + w, y);
		
		// drawing bottom side of tab, only if had is unselected
		if (!isSelected){
			g.drawLine(x, y + h, x + w, y + h);
		}
		
		// draws a hroizontal line accross the entire tabbed pane if it's the last tab
		if (tabbedPane.getTabCount() - 1 == tabIndex){
			g.drawLine(x + w, y + h, x + tabbedPane.getSize().width, y + h);
		}
		
	}
	
	
	
	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement,
			int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		
		if (!isSelected) {
			g.setColor(BACKGROUND_TAB_COLOUR);
			g.fillRect(x, y, w, h);
		}
		
	}
	
	
	@Override
	protected void paintFocusIndicator(Graphics g, int tabPlacement,
			Rectangle[] rects, int tabIndex, Rectangle iconRect,
			Rectangle textRect, boolean isSelected) {
		// leave empty - no focus indicator
	}
	
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		
		tabAreaInsets = new Insets(0, 0, 5, 0);
		selectedTabPadInsets = new Insets(0, 0, 0, 0);
		contentBorderInsets = new Insets(0, 0, 0, 0);
	}
	
	
	@Override
	protected int getTabLabelShiftY(int tabPlacement, int tabIndex,
			boolean isSelected) {
		return 0;
	}
	
	
	@Override
	protected int calculateTabHeight(int tabPlacement, int tabIndex,
			int fontHeight) {
		return fontHeight + 14;
	}

}
