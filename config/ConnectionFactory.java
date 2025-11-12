package config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionFactory {
    private static final String URL  = "jdbc:postgresql://localhost:5432/feifood";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";
    static {
        try { Class.forName("org.postgresql.Driver"); }
        catch (ClassNotFoundException e) { throw new RuntimeException("Driver PostgreSQL não encontrado", e); }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
