package database;

import java.sql.DriverManager;
import java.sql.SQLException;

/***
 * Klasa koja daje vezu prema bazi podataka. Već su upisani podaci: ime databaze, korisničko ime i lozinka
 */
public class Connection {

    private static java.sql.Connection conn;
    private Connection(){}

    public static java.sql.Connection getConnection() throws SQLException, ClassNotFoundException {
        if(conn == null){
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/projekt","kirlia","kirliapass");
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        conn.close();
    }

    public static void destroyConnection() throws SQLException {
        closeConnection();
        conn = null;
    }


}
