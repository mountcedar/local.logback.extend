/**
 *
 */
package local.logback.extend;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * @author sugiyama
 * @todo use encoder or layout and print out with the layout set in property
 */
public class UIAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private LogbackTextPane disp = null;
	protected static String lineSep = System.getProperty("line.separator");

	@Override
	public void start() {
		disp = new LogbackTextPane(getName());
		super.start();
	}

    public void append(ILoggingEvent event) {
        try {
            String line = String.format("%d - %s [%s] %s - %s%s",
            		event.getTimeStamp() - event.getLoggerContextVO().getBirthTime(),
            		event.getLevel(),
            		event.getThreadName(),
            		event.getLoggerName(),
            		event.getFormattedMessage(),
            		lineSep);
            this.disp.write(line);
		} catch (Exception e) {
			//System.err.println(e.getMessage());
			for (StackTraceElement elem: e.getStackTrace()) {
				System.err.println(elem);
			}
		}
    }
}
