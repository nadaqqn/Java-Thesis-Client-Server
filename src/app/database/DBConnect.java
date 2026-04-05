package app.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    private static final String URL = "jdbc:mysql://localhost:3306/db_bimbinganskripsi";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // default XAMPP

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
