package autoszerviz.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.swing.JOptionPane;

/**
 *
 * @author fulopmark
 */
public class MySQL {

	static Connection con;

	static {
		try {
			con = DriverManager.getConnection(DBConstants.Auth.DOCKER_CONN_STRING, DBConstants.Auth.DOCKER_USERNAME,
					DBConstants.Auth.DOCKER_PASSWORD);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Sikertelen adatbáziskapcsolat!", "", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	static ResultSet createAndRunQuery(String query) throws SQLException {
		Statement stmt = con.createStatement();
		
		return stmt.executeQuery(query);
		
	}

	public static ResultSet createAndRunPreparedQuery(String query, Object... param) throws SQLException {
		int qmarkCount = 0;
		for (char c : query.toCharArray()) {
			if (c == '?') {
				qmarkCount++;
			}
		}

		if (qmarkCount == 0) {
			return createAndRunQuery(query);
		}
		
		if (qmarkCount != param.length) {
			return null;
		}

		PreparedStatement stmt = con.prepareStatement(query);
		for (int i = 1; i < param.length + 1; i++) {
			System.out.println(i - 1 + ", " + param[i - 1]);
			if (param[i - 1] instanceof String) {
				stmt.setString(i, (String) param[i - 1]);
			} else if (param[i - 1] instanceof LocalDate) {
				stmt.setDate(i, java.sql.Date.valueOf((LocalDate) param[i - 1]));
			} else if (param[i - 1] instanceof LocalTime) {
				stmt.setTime(i, java.sql.Time.valueOf((LocalTime) param[i - 1]));
			}
		}

		return stmt.executeQuery();
	}
	
	public static int createAndRunPreparedUpdate(String query, Object... param) throws SQLException {
		int qmarkCount = 0;
		for (char c : query.toCharArray()) {
			if (c == '?') {
				qmarkCount++;
			}
		}

		if (param.length == 0 || qmarkCount == 0) return -1;
		
		
		if (qmarkCount != param.length) {
			return -1;
		}

		PreparedStatement stmt = con.prepareStatement(query);
		for (int i = 1; i < param.length + 1; i++) {
			System.out.println(i - 1 + ", " + param[i - 1]);
			if (param[i - 1] instanceof String) {
				stmt.setString(i, (String) param[i - 1]);
			} else if (param[i - 1] instanceof LocalDate) {
				stmt.setDate(i, java.sql.Date.valueOf((LocalDate) param[i - 1]));
			} else if (param[i - 1] instanceof LocalTime) {
				stmt.setTime(i, java.sql.Time.valueOf((LocalTime) param[i - 1]));
			}
		}

		return stmt.executeUpdate();
	}

	public static void close() throws SQLException {
		if (con == null) {
			return;
		}
		con.close();
	}
}
