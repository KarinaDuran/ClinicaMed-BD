import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", "MaquinandoCaos693");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
