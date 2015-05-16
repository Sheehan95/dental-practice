package dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class ReportDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	
	private JEditorPane reportDisplay;
	
	private HTMLEditorKit html;
	private HTMLDocument doc;
	private StyleSheet css;
	
	
	/**
	 * Default constructor.
	 */
	public ReportDialog(){
		
		reportDisplay = new JEditorPane();
		reportDisplay.setBackground(new Color(248, 248, 248));
		reportDisplay.setEditable(false);
		
		
		JScrollPane scrollpane = new JScrollPane(reportDisplay);
		getContentPane().add(scrollpane);
		
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setSize(600, screenSize.height - 150);
		setLocationRelativeTo(null);
		setModal(true);
		setResizable(false);
		setIconImage(((ImageIcon)UIManager.getIcon("FileView.fileIcon")).getImage());
		
	}
	
	
	/**
	 * Used to display the dialog. Remains open until the user closes it.
	 */
	public void showDialog (){
		
		if (reportDisplay.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Report contains to entries", "Empty Report", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			setVisible(true);
		}
		
	}
	
	
	/**
	 * Appends html to the end of the dialog's display.
	 * 
	 * @param content html to be added to the dialog
	 * @throws BadLocationException
	 * @throws IOException
	 */
	public void appendTo (String content) throws BadLocationException, IOException {
		
		if (reportDisplay.getText().isEmpty()){
			html = new HTMLEditorKit();
			reportDisplay.setEditorKit(html);
			
			doc = (HTMLDocument) reportDisplay.getDocument();
			css = html.getStyleSheet();
			initStyleSheet();
		}
		
		html.insertHTML(doc, doc.getLength(), content, 0, 0, null);
		
	}
	
	
	/**
	 * Adds the styling rules to the CSS Stylesheet.
	 */
	private void initStyleSheet (){
		
		css.addRule("body {color: #535353; "
				+ "padding: 10px;}");
		
		css.addRule("li {list-style-type: none;}");
		
		// styling headers
		css.addRule("h1, h2, h3, th "
				+ "{color: #1B1B1B;}");
		
		css.addRule("#title "
				+ "{text-align: center;}");
		
		
		// styling tables
		css.addRule("table "
				+ "{border-spacing: 0px; "
				+ "margin: 0px; "
				+ "padding: 0px;}");
		
		css.addRule("td, th "
				+ "{border: 1px solid black; "
				+ "margin: 0px; padding: 0px; "
				+ "text-align: center;}");
		
	}
	
}
