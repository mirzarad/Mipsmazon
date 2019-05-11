package sample;

import java.sql.*;

/**
 * Created by mirza on 5/3/2019.
 */
public class DBConnector {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://" + "localhost:3306" + "/" + "mipsmazon";
            Connection conn = (Connection) DriverManager.getConnection(url, "root","Phantomdatabase1!");
        return conn;
    }
}
