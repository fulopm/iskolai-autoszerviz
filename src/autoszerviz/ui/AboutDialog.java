package autoszerviz.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class AboutDialog extends JDialog {

	JPanel contentPanel;
	BoxLayout layout;

	JLabel labelIcon;
	JLabel labelName;
	JLabel labelAuthor;

	public AboutDialog(JFrame parent) {
		super(parent, ModalityType.DOCUMENT_MODAL);
		
		contentPanel = new JPanel();
		layout = new BoxLayout(contentPanel, BoxLayout.Y_AXIS);
		
		contentPanel.setLayout(layout);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		initUI();
		
		setUndecorated(true);
		setContentPane(contentPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	void initUI() {
		labelIcon = new JLabel(new ImageIcon(UIConstants.Images.IMAGE_WRENCH));
		labelIcon.setAlignmentX(CENTER_ALIGNMENT);
		
		labelName = new JLabel("Autószervíz");
		labelName.setFont(labelName.getFont().deriveFont(Font.BOLD));
		labelName.setAlignmentX(CENTER_ALIGNMENT);
		labelName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		labelAuthor = new JLabel("© Fülöp Márk 2017");
		labelAuthor.setAlignmentX(CENTER_ALIGNMENT);
		labelAuthor.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

		contentPanel.add(labelIcon);
		contentPanel.add(labelName);
		contentPanel.add(labelAuthor);

		getRootPane().registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog.this.dispose();

			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

}
