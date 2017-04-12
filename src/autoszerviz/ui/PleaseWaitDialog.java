package autoszerviz.ui;


        
              
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import net.java.dev.designgridlayout.DesignGridLayout;

/**
 *
 * @author fulopm
 */
public class PleaseWaitDialog extends JDialog {

    private JLabel labelPleaseWait;
    JProgressBar progressBar;

    JPanel panel;

    DesignGridLayout layoutHelper;

    public PleaseWaitDialog(JFrame parent, String title) {
        super(parent, title, ModalityType.DOCUMENT_MODAL);
        setUndecorated(true);

        initComponents();

        layoutHelper = new DesignGridLayout(panel);

        layoutHelper.row().center().add(labelPleaseWait);
        layoutHelper.row().center().add(progressBar);

        add(panel);
        pack();
        setLocationRelativeTo(parent);
        
    }

    void initComponents() {
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

        labelPleaseWait = new JLabel("Kérem, várjon...");
        
        labelPleaseWait.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        

    }

}