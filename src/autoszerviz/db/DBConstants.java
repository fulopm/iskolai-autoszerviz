package autoszerviz.db;

/**
 *
 * @author fulopmark
 */
public class DBConstants {
    
    public static class Auth {
        public static final String DOCKER_CONN_STRING = "jdbc:mysql://192.168.99.100:32768/autoszerviz";
        public static final String DOCKER_USERNAME = "root";
        public static final String DOCKER_PASSWORD = "root";
    }
    
    public static class Query {
    	public static final String SELECT_ALL_USERS = "SELECT * FROM users";
        public static final String SELECT_USERS = "SELECT * FROM users WHERE username=? AND password=?";
        public static final String SELECT_ALL_APPOINTMENTS = "SELECT * FROM appointments";
        public static final String SELECT_APPOINTMENTS = "SELECT * FROM appointments WHERE date=? AND time=? AND owner=? AND brand=?";
        public static final String SELECT_APPOINTMENTS_BY_DATE = "SELECT * FROM appointments WHERE date=?";
        public static final String INSERT_APPOINTMENT = "INSERT INTO appointments (date, time, owner, brand) VALUES (?, ?, ?, ?)";
    }
}
