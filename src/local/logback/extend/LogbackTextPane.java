package local.logback.extend;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class LogbackTextPane extends JTextPane {
	private static final long serialVersionUID = -2087232534531132757L;

	public static enum Status {
		STARTED, PAUSED, STOPPED
	};

	public static Map<String, LogbackTextPane> logbackTexts = new HashMap<String, LogbackTextPane>();

	//buffer to hold log statements when the UI is set to PAUSE
	public static final String STYLE_REGULAR = "regular";
	public static final String STYLE_HIGHLIGHTED = "highlighted";
	protected List<String> logBuffer = null;
	protected Status state;
	protected String id = null;

	public LogbackTextPane(String id) {
		this.id = id;
		logBuffer = new ArrayList<String>();
		this.state = Status.STARTED;
		StyledDocument sDoc = this.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		Style s1 = sDoc.addStyle(STYLE_REGULAR, def);
		StyleConstants.setFontFamily(def, "SansSerif");
		Style s2 = sDoc.addStyle(STYLE_HIGHLIGHTED, s1);
		StyleConstants.setBackground(s2, Color.BLUE);
		LogbackTextPane.logbackTexts.put(id, this);
	}

	/**Displays the log in the text area unless dispMsg is set to false in which
	 * case it adds the log to a buffer. When dispMsg becomes true, the buffer
	 * is first flushed and it's contents are displayed in the text area.
	 * @param log The log message to be displayed in the text area
	 */
	public void write(String log) {
		if(state == Status.STARTED) {
			try {
			StyledDocument sDoc = getStyledDocument();
			if(!logBuffer.isEmpty()) {
				System.out.println("flushing buffer");
				Iterator<String> iter = logBuffer.iterator();
				while(iter.hasNext()) {
					sDoc.insertString(0, (String)iter.next(), sDoc.getStyle(STYLE_REGULAR));
					iter.remove();
				}
			}
			sDoc.insertString(0, log, sDoc.getStyle(STYLE_REGULAR));
			} catch(BadLocationException ble) {
				System.out.println("Bad Location Exception : " + ble.getMessage());
			}
		}
		else if(state == Status.PAUSED){
			logBuffer.add(log);
		}
	}

	public Status getState() {
		return state;
	}


	public void setState(Status state) {
		this.state = state;
	}
}
