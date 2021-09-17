package database;

import beans.ClientServerInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//ovjde je server==client, ali svugje pise server jer je lakse tako pisati

/***
 * Klasa koja daje rad sa bazama. Ukoliko se unutar neke druge klase želi raditi s bazama,
 * prvo se pozove samo ova klasa, i onda preko instance ove klase se već obavlja želejni posao s bazama
 */
public class DatabaseController {

    private java.sql.Connection con;
    private static DatabaseController controller;

    private DatabaseController() throws SQLException, ClassNotFoundException {
        if(con == null){
            con = Connection.getConnection();
        }
    }

    public static DatabaseController getDatabaseController() throws SQLException, ClassNotFoundException {
        if(controller == null){
            controller = new DatabaseController();
        }
        return controller;
    }

    public void createServerTable(String name) throws SQLException {
        String createQuery1 = "CREATE TABLE IF NOT EXISTS " + name
                +" (first_word VARCHAR(20), second_word VARCHAR(20))";

        PreparedStatement stmt = con.prepareStatement(createQuery1);
        stmt.execute();
    }

    public void createMasterTable() throws SQLException {

        String query1 = "CREATE TABLE IF NOT EXISTS master"
                + "(id_server INTEGER, port INTEGER, id_parent INTEGER,"
                + "id_left_child INTEGER, id_right_child INTEGER) ";

        PreparedStatement stmt = con.prepareStatement(query1);
        stmt.execute();
    }

    public void insertIntoServerTable(String tableName, String first, String second) throws SQLException {
        String insertQuery1 = "INSERT INTO "+tableName+" (first_word, second_word)"
                + "values (?, ?)";
        PreparedStatement stmt = con.prepareStatement(insertQuery1);
        stmt.setString(1, first);
        stmt.setString(2, second);
        stmt.execute();
    }

    public void insertIntoMasterTable(int id_server, int port, int id_parent,int id_left_child, int id_right_child) throws SQLException {

        if(doesServerExists(id_server)){
            return;
        }

        String insertQuery = "INSERT INTO master (id_server, port, id_parent, id_left_child, id_right_child)"
                + "values (?, ?, ?, ?, ?)";

        PreparedStatement stmt = con.prepareStatement(insertQuery);
        stmt.setInt(1, id_server);
        stmt.setInt(2, port);
        stmt.setInt(3, id_parent);
        stmt.setInt(4, id_left_child);
        stmt.setInt(5, id_right_child);
        stmt.execute();
    }

    public String getStringFromServer(String s, String name) throws SQLException {
        String selectQuery = "SELECT second_word FROM "+name+" WHERE first_word = ?";

        PreparedStatement stmt = con.prepareStatement(selectQuery);
        stmt.setString(1, s);

        ResultSet result = stmt.executeQuery();

        if(!result.next()){
            return null;
        }
        return result.getString("second_word");
    }

    public  ClientServerInfo getMyClientSeverInfo(int id) throws SQLException {
        String selectQuery = "SELECT * FROM master WHERE id_server = ?";

        PreparedStatement stmt = con.prepareStatement(selectQuery);
        stmt.setInt(1, id);

        ResultSet result = stmt.executeQuery();

        ClientServerInfo info = new ClientServerInfo();

        if(!result.next()){
            return null;
        }
        info.setId(id);
        info.setPort(result.getInt("port"));
        info.setId_parent(result.getInt("id_parent"));
        info.setId_left_child(result.getInt("id_left_child"));
        info.setId_right_child(result.getInt("id_right_child"));

        return info;
    }

    public void updateMasterTableParent(int id_server, int id_parent) throws SQLException {
        String updateQuery = "UPDATE master set id_parent = ? where id_server = ? ";
        updateHelp(updateQuery, id_server, id_parent);
    }

    public void updateMasterTableLeft(int id_server, int id_left_child) throws SQLException {
        String updateQuery = "UPDATE master set id_left_child = ? where id_server = ? ";
        updateHelp(updateQuery, id_server, id_left_child);
    }

    public void updateMasterTableRight(int id_server, int id_right_child) throws SQLException {
        String updateQuery = "UPDATE master set id_right_child = ? where id_server = ? ";
        updateHelp(updateQuery, id_server, id_right_child);
    }

    private void updateHelp(String updateQuery, int id_server, int id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(updateQuery);
        stmt.setInt(1, id);
        stmt.setInt(2, id_server);
        stmt.executeUpdate();
    }

    public void deleteFromMaster(int id_server, String name) throws SQLException {
        String query = "DELETE FROM master WHERE id_server = ?";
        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setInt(1, id_server);

        // execute the preparedstatement
        preparedStmt.execute();

        query = "DROP TABLE "+name;
        preparedStmt.executeUpdate(query);
    }

    public boolean doesServerExists(int id_server) throws SQLException {

        String query = "SELECT * FROM master WHERE id_server = ?";

        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, id_server);

        ResultSet result = stmt.executeQuery();

        if(result.next()){
            return true;
        }else{
            return false;
        }
    }
}
