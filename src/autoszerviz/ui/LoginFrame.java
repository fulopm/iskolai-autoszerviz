package autoszerviz.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.DefaultApplication;

import autoszerviz.db.DBConstants;
import net.java.dev.designgridlayout.DesignGridLayout;

/**
 *
 * @author fulopmark
 */
public class LoginFrame extends JFrame implements ActionListener {

    DesignGridLayout layoutHelper;
    JPanel contentPane;

    JLabel labelWrenchIcon;

    JLabel labelUsername;
    JLabel labelPassword;

    JTextField fieldUsername;
    JPasswordField fieldPassword;

    JButton btnLogin;

    public LoginFrame() {

		Application app = new DefaultApplication();
		if (app.isMac()) {
			app.setApplicationIconImage((BufferedImage)UIConstants.Images.IMAGE_WRENCH);
			
		}
		
		setIconImage(UIConstants.Images.IMAGE_WRENCH);
        contentPane = new JPanel();
        layoutHelper = new DesignGridLayout(contentPane);

        this.setContentPane(contentPane);
        initUI();
        
        JRootPane rp = SwingUtilities.getRootPane(btnLogin);
        rp.setDefaultButton(btnLogin);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Autószerviz -- Bejelentkezés");
        setResizable(false);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void initUI() {
        labelWrenchIcon = new JLabel(new ImageIcon(UIConstants.Images.IMAGE_WRENCH));

        labelUsername = new JLabel("Felhasználónév:");
        labelPassword = new JLabel("Jelszó:");

        fieldUsername = new JTextField(10);
        fieldPassword = new JPasswordField(10);

        btnLogin = new JButton("Bejelentkezés");
      
        btnLogin.addActionListener(this);

        layoutHelper.row().center().add(labelWrenchIcon);
        layoutHelper.row().grid(labelUsername).add(fieldUsername);
        layoutHelper.row().grid(labelPassword).add(fieldPassword);
        layoutHelper.row().right().add(btnLogin);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnLogin) {

            SwingWorker<Boolean, Void> dbLoginWorker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        ResultSet rs = autoszerviz.db.MySQL.createAndRunPreparedQuery(DBConstants.Query.SELECT_USERS, fieldUsername.getText(), new String(fieldPassword.getPassword()));
                        return rs.next();
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return false;

                }

            };

            final PleaseWaitDialog dialog = new PleaseWaitDialog(this, "");

            dbLoginWorker.addPropertyChangeListener(new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("state")) {
                        if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                            dialog.dispose();
                        }
                    }
                }
            });

            dbLoginWorker.execute();
            dialog.setVisible(true);

            boolean loginSuccessful = false;
            try {
                loginSuccessful = dbLoginWorker.get();
            } catch (InterruptedException | ExecutionException ex) {
                System.err.println(ex);
            }

            if (loginSuccessful) {

                JOptionPane.showMessageDialog(LoginFrame.this, "Sikeres bejelentkezés!", "Autószerviz -- Információ", JOptionPane.INFORMATION_MESSAGE);
                LoginFrame.this.dispose();

                MainFrame frame = new MainFrame();

            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Sikertelen bejelentkezés!", "Autószerviz -- Hiba", JOptionPane.ERROR_MESSAGE);
            }

        };

    }
}
