package autoszerviz;

import autoszerviz.ui.AboutDialog;
import autoszerviz.ui.LoginFrame;
import autoszerviz.ui.UIConstants;

import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;

/**
 *
 * @author fulopmark
 */
public class Main {
    public static void main(String[] args) {
    	
    	if (System.getProperty("os.name").startsWith("Mac")) {
    		System.setProperty("apple.laf.useScreenMenuBar", "true");
    		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Autószerviz");
    	}
    	
    	
        LoginFrame frame = new LoginFrame();
    }

	
}
