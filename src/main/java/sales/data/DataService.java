package sales.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataService {

    private static DataService instance;

    private final Connection connection;

    private DataService() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:northwind.db");
    }

    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new DataService();
        }
        return instance.connection;
    }
}
