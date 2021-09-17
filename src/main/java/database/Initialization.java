package database;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klasa koja inicira rad s ovom aplikacijom... Puni bazu podataka s početnim podacima tako da se odmah može krenuti
 * nešto raditi. Sve se može pokrenuti i bez da se ovo prvo pokrene
 */
public class Initialization {

    private DatabaseController controller = null;
    private static Initialization initialization_instance = null;
    //private static java.sql.Connection con;
    private Initialization() throws SQLException, ClassNotFoundException {
        controller = DatabaseController.getDatabaseController();
    }

    public static Initialization getInitialization_instance() throws SQLException, ClassNotFoundException {
        if(initialization_instance == null){
            initialization_instance = new Initialization();
        }
        return initialization_instance;
    }

    /**
     * Metoda koja stvara tablicu koja drži podatke za servere
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void initialize() throws SQLException, ClassNotFoundException {
        controller.createMasterTable();
    }

    /**
     * Metoda koja inicira baš demo
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void initializeDemo() throws SQLException, ClassNotFoundException {

        initialize();

        //prvi server-klijent
        controller.insertIntoMasterTable(1, 7000, -1, 2, 3);
        insertIntoServerTable("server1", "kuca", "drvo");

        //drugi server-klijent
        controller.insertIntoMasterTable(2, 7001, 1, -1, -1);
        insertIntoServerTable("server2", "sunce", "cvijet");

        //treci server-klijent
        controller.insertIntoMasterTable(3, 7002, 1, -1, -1);
        insertIntoServerTable("server3", "potok", "voda");

    }

    private void insertIntoServerTable(String name, String first, String second) throws SQLException {
        controller.createServerTable(name);
        controller.insertIntoServerTable(name, first, second);
    }

}
