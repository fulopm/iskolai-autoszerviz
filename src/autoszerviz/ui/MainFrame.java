package autoszerviz.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;

import autoszerviz.db.DBConstants;
import autoszerviz.db.MySQL;

/**
 *
 * @author fulopmark
 */

public class MainFrame extends JFrame implements ApplicationListener {

	// private final DesignGridLayout layoutHelper;
	private final JPanel contentPanel;

	MenuBar menuBar;
	Menu menuAppointments;
	MenuItem menuAppointmentsAdd;
	Menu menuItemHelp;
	MenuItem menuAbout;

	JPanel panelDate;
	LocalDate dateSelectedDate;
	JLabel labelSelectedDate;
	JButton buttonPlusOneDay;
	JButton buttonMinusOneDay;
	JButton buttonMinusOneMonth;
	JButton buttonPlusOneMonth;

	JPanel panelAppointments;
	JTable tableAppointments;

	public MainFrame() {

		Application app = new DefaultApplication();
		if (app.isMac()) {
			app.addAboutMenuItem();
			app.addApplicationListener(this);
			app.setApplicationIconImage((BufferedImage)UIConstants.Images.IMAGE_WRENCH);
		}
		
		setIconImage(UIConstants.Images.IMAGE_WRENCH);

		contentPanel = new JPanel(new BorderLayout());
		panelDate = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelAppointments = new JPanel(new FlowLayout(FlowLayout.CENTER));
		// layoutHelper = new DesignGridLayout(contentPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setTitle("Autószerviz -- Főoldal");

		initUI();
		setMenuBar(menuBar);

		this.setContentPane(contentPanel);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	void initUI() {
		menuBar = new MenuBar();

		menuAppointments = new Menu("Időpontok");
		menuAppointmentsAdd = new MenuItem("Új időpont");
		menuAppointmentsAdd.setShortcut(new MenuShortcut(KeyEvent.VK_N));
		menuAppointmentsAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				NewAppointmentDialog nap = new NewAppointmentDialog(MainFrame.this);

			}
		});

		menuAppointments.add(menuAppointmentsAdd);
		menuItemHelp = new Menu("Segítség");
		menuAbout = new MenuItem("About");
		menuAbout.setShortcut(new MenuShortcut(KeyEvent.VK_A));
		menuAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog aboutDialog = new AboutDialog(MainFrame.this);

			}
		});

		menuItemHelp.add(menuAbout);

		menuBar.add(menuAppointments);
		menuBar.add(menuItemHelp);

		dateSelectedDate = LocalDate.now();
		labelSelectedDate = new JLabel(UIConstants.General.SIMPLE_DATE_FORMAT.format(dateSelectedDate));
		labelSelectedDate.setFont(UIConstants.General.BOLD_UI);

		buttonMinusOneDay = new JButton("<");
		buttonMinusOneDay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				dateSelectedDate = dateSelectedDate.minusDays(1);
				labelSelectedDate.setText(UIConstants.General.SIMPLE_DATE_FORMAT.format(dateSelectedDate));
				((AppointmentsTableModel) tableAppointments.getModel()).update(dateSelectedDate);
			}
		});

		buttonPlusOneDay = new JButton(">");
		buttonPlusOneDay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				dateSelectedDate = dateSelectedDate.plusDays(1);
				labelSelectedDate.setText(UIConstants.General.SIMPLE_DATE_FORMAT.format(dateSelectedDate));
				((AppointmentsTableModel) tableAppointments.getModel()).update(dateSelectedDate);

			}
		});

		buttonMinusOneMonth = new JButton("<<");
		buttonMinusOneMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				dateSelectedDate = dateSelectedDate.minusMonths(1);
				labelSelectedDate.setText(UIConstants.General.SIMPLE_DATE_FORMAT.format(dateSelectedDate));
				((AppointmentsTableModel) tableAppointments.getModel()).update(dateSelectedDate);
			}
		});

		buttonPlusOneMonth = new JButton(">>");
		buttonPlusOneMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				dateSelectedDate = dateSelectedDate.plusMonths(1);
				labelSelectedDate.setText(UIConstants.General.SIMPLE_DATE_FORMAT.format(dateSelectedDate));
				((AppointmentsTableModel) tableAppointments.getModel()).update(dateSelectedDate);

			}
		});

		panelDate.add(buttonMinusOneMonth);
		panelDate.add(buttonMinusOneDay);
		panelDate.add(labelSelectedDate);
		panelDate.add(buttonPlusOneDay);
		panelDate.add(buttonPlusOneMonth);

		contentPanel.add(panelDate, BorderLayout.NORTH);

		tableAppointments = new JTable(new AppointmentsTableModel(dateSelectedDate));

		panelAppointments.add(new JScrollPane(tableAppointments));
		contentPanel.add(panelAppointments, BorderLayout.CENTER);
		
		

	}

	public static class AppointmentsTableModel extends AbstractTableModel {

		List<Appointment> appointments;
		Set<Integer> hours;

		public AppointmentsTableModel(LocalDate date) {
			appointments = new ArrayList<>();
			fillList(date);
		}

		public void update(LocalDate dateFilter) {
			fillList(dateFilter);
			fireTableDataChanged();
		}

		private void fillList(LocalDate dateFilter) {
			appointments.clear();
			try (ResultSet rs = MySQL.createAndRunPreparedQuery(DBConstants.Query.SELECT_APPOINTMENTS_BY_DATE,
					dateFilter)) {
				while (rs.next()) {
					int appId = rs.getInt(1);
					Date appDate = rs.getDate(2);
					Time appTime = rs.getTime(3);
					String appOwner = rs.getString(4);
					String appBrand = rs.getString(5);
					appointments.add(new Appointment(appId, appDate, appTime, appOwner, appBrand));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			hours = new HashSet<>();
			for (Appointment app : appointments) {

				hours.add(app.time.toLocalTime().getHour());
			}

		}

		@Override
		public int getRowCount() {
			return appointments.size();
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return "Idő";
			case 1:
				return "Ügyfél";
			case 2:
				return "Márka";
			}

			return super.getColumnName(column);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return appointments.get(rowIndex).time;
			case 1:
				return appointments.get(rowIndex).owner;
			case 2:
				return appointments.get(rowIndex).brand;
			}
			return "";
		}

		public List<Appointment> getAppointments() {
			return appointments;
		}

	}

	@Override
	public void handleAbout(ApplicationEvent arg0) {
		AboutDialog dialog = new AboutDialog(null);
		arg0.setHandled(true);

	}

	@Override
	public void handleOpenApplication(ApplicationEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleOpenFile(ApplicationEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlePreferences(ApplicationEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlePrintFile(ApplicationEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleQuit(ApplicationEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleReOpenApplication(ApplicationEvent arg0) {
		// TODO Auto-generated method stub

	}

}
