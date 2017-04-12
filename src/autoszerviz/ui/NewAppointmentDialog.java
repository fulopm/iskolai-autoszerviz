package autoszerviz.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

import autoszerviz.db.DBConstants;
import autoszerviz.db.MySQL;
import net.java.dev.designgridlayout.DesignGridLayout;

public class NewAppointmentDialog extends JDialog implements ActionListener{

	DesignGridLayout layoutHelper;
	
	
	JLabel labelDate;
	DatePicker pickerDate;
	JLabel labelTime;
	TimePicker pickerTime;
	JLabel labelOwner;
	JTextField fieldOwner;
	JLabel labelBrand;
	JTextField fieldBrand;
	
	JButton buttonSave;
	
	public NewAppointmentDialog(JFrame parent) {
		super(parent, ModalityType.APPLICATION_MODAL);
		setTitle("Autószerviz -- Új időpont");
		layoutHelper = new DesignGridLayout(this.getContentPane());
		initUI();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	void initUI() {
		labelDate = new JLabel("Dátum:");
		pickerDate = new DatePicker();
		pickerDate.setDateToToday();
		
		labelTime = new JLabel("Idő:");
		pickerTime = new TimePicker();
		pickerTime.setTimeToNow();
		
		labelOwner = new JLabel("Ügyfél:");
		fieldOwner = new JTextField();
		
		labelBrand = new JLabel("Autómárka:");
		fieldBrand = new JTextField();
		
		buttonSave = new JButton("Rögzít");
		buttonSave.addActionListener(this);
		
		layoutHelper.row().grid(labelDate).add(pickerDate);
		layoutHelper.row().grid(labelTime).add(pickerTime);
		
		layoutHelper.row().grid(labelOwner).add(fieldOwner);
		layoutHelper.row().grid(labelBrand).add(fieldBrand);
		
		layoutHelper.row().bar().right(buttonSave);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (pickerDate.getDate() == null || !pickerDate.isTextFieldValid() || pickerTime.getTime() == null || !pickerTime.isTextFieldValid() || fieldOwner.getText() == null || fieldOwner.getText() == "" || fieldBrand.getText() == null || fieldBrand.getText()=="") {
			JOptionPane.showMessageDialog(NewAppointmentDialog.this, "Hibás adatok! Ellenőrizze a beírt adatokat.", "Autószerviz -- Hiba", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			if (MySQL.createAndRunPreparedUpdate(DBConstants.Query.INSERT_APPOINTMENT, pickerDate.getDate(), pickerTime.getTime(), fieldOwner.getText(), fieldBrand.getText()) > 0) {
				JOptionPane.showMessageDialog(NewAppointmentDialog.this, "Sikeres adatbázisművelet!", "Autószerviz -- Információ", JOptionPane.INFORMATION_MESSAGE);
				NewAppointmentDialog.this.dispose();
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(NewAppointmentDialog.this, "Sikertelen adatbázisművelet!\n"+ex.getErrorCode(), "Autószerviz -- Hiba", JOptionPane.ERROR_MESSAGE);
		}		
	}
	
	
}
