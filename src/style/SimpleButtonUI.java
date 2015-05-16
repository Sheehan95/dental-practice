package style;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;


public class SimpleButtonUI extends BasicButtonUI implements Serializable, MouseListener {
	
	
	private static final long serialVersionUID = -390206798808309097L;

	private Color BUTTON_ENABLED_TEXT = new Color(68, 68, 68);
	private Color BUTTON_DISABLED_TEXT = new Color(153, 153, 153);
	

	Color START = new Color(230, 230, 230);
	Color END = new Color(220, 220, 220);
	
	
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		c.addMouseListener(this);
	}
	
	
	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
		c.removeMouseListener(this);
	}
	
	
	@Override
	public void paint(Graphics g, JComponent c) {
		
		AbstractButton b = (AbstractButton) c;
		Dimension size = b.getSize();
		
		
		//GradientPaint gp = new GradientPaint(0, 0, START, 0, 25, END);
		
		g.setColor(START);
		g.fillRect(0, 0, size.width, size.height / 2);
		
		g.setColor(END);
		g.fillRect(0, size.height / 2, size.width, size.height);
		
		
		if (b.isEnabled()){
			g.setColor(BUTTON_ENABLED_TEXT);
		}
		else {
			b.setBackground(null);
			g.setColor(null);
			g.fillRect(0, 0, size.width, size.height);
			g.setColor(BUTTON_DISABLED_TEXT);
		}
		
		String text = b.getText();
		
		g.setFont(c.getFont());
		FontMetrics metrics = g.getFontMetrics();
		
		int x = (size.width - metrics.stringWidth(text)) / 2;
		int y = (size.height + metrics.getAscent()) / 2;
		
		g.drawString(text, x, y);
		
	}
	
	@Override
	protected void paintText(Graphics g, AbstractButton b, Rectangle textRect,
			String text) {
		
		g.setColor(Color.CYAN);
		
		
		
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		
		JComponent c = (JComponent) e.getComponent();
		AbstractButton b = (AbstractButton) c;
		
		
		
		b.repaint();
		
	}
	

	@Override
	public void mouseEntered(MouseEvent e) {
		
		JComponent c = (JComponent) e.getComponent();
		
		c.setBackground(Color.RED);
		
		c.repaint();
		
	}
	

	@Override
	public void mouseExited(MouseEvent e) {
		
		JComponent c = (JComponent) e.getComponent();
		
		if (c.isEnabled()){
			
		}
		
	}
	

	@Override
	public void mousePressed(MouseEvent e) {

		
	}
	

	@Override
	public void mouseReleased(MouseEvent e) {

		
	}
	
}
