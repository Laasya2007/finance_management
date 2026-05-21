import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/project";
    private static final String USER = "root";       // change if needed
    private static final String PASSWORD = "Vyshu@9849";   // change if needed

    public static Connection getConnection() {

        Connection con = null;

        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create Connection
            con = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("✅ Database Connected!");

        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver Not Found!");
            System.out.println("👉 Add mysql-connector-j jar file");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("❌ Database Connection Failed!");
            e.printStackTrace();
        }

        return con;
    }
}