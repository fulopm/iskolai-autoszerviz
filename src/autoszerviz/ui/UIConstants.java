package autoszerviz.ui;

import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

/**
 *
 * @author fulopmark
 */
public class UIConstants {
    public static class Images {
        public static Image IMAGE_WRENCH;
        static {
            try {
                IMAGE_WRENCH = ImageIO.read(UIConstants.class.getClassLoader().getResourceAsStream("wrench.png"));
            } catch (IOException ex) {
                System.err.println("wrench.png betöltése sikertelen!");
            }
        }
        
    }
    
    public static class General {
    	public static final DateTimeFormatter SIMPLE_DATE_FORMAT = new DateTimeFormatterBuilder().appendPattern("YYYY. MM. dd.").toFormatter();
    	public static final Font BOLD_UI = UIManager.getFont("Label.font").deriveFont(Font.BOLD).deriveFont(14f);
    }
}
