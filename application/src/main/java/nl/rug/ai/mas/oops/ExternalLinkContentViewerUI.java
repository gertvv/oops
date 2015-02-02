package nl.rug.ai.mas.oops;

import java.awt.Desktop;
import java.net.URL;

import javax.help.JHelpContentViewer;
import javax.help.plaf.basic.BasicContentViewerUI;
import javax.swing.JComponent;
import javax.swing.event.HyperlinkEvent;

public class ExternalLinkContentViewerUI extends BasicContentViewerUI {
	private static final long serialVersionUID = 5684045375900687224L;

	public ExternalLinkContentViewerUI(JHelpContentViewer b) {
		super(b);
	}
     
	public static javax.swing.plaf.ComponentUI createUI(JComponent x){
          return new ExternalLinkContentViewerUI((JHelpContentViewer) x);
     }
	
	public void hyperlinkUpdate(HyperlinkEvent he){
		if (he.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
			try {
				URL url = he.getURL();
				if (url.getProtocol().equalsIgnoreCase("mailto") || 
						url.getProtocol().equalsIgnoreCase("http") ||
						url.getProtocol().equalsIgnoreCase("https") ||
						url.getProtocol().equalsIgnoreCase("ftp")) {
					Desktop.getDesktop().browse(url.toURI());
					return;
				}
			}
			catch(Throwable t) {
			}
		}
		super.hyperlinkUpdate(he);
	}
}