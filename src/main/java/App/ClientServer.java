package App;

import beans.ClientServerInfo;
import database.DatabaseController;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Klasa koja baš predstavlja jedan čvor, jednog server/klijenta; ona je tu samo radi lakše organizacije
 * zapravo, može se i bez nje, jer se svugdje koristi samo ServerClientInfo koji isto tako predtavlja baš jednog servera klijenta.
 * Eventualno ova klasa ima svog kontrolera kako bi mogla komunicirati s bazom, ali to se ne koristi na taj način pa nije bitno
 */
public class ClientServer {
    private ClientServerInfo info;
    private DatabaseController controller;

    public ClientServer() throws SQLException, ClassNotFoundException {
        controller = DatabaseController.getDatabaseController();
    }

    public ClientServerInfo getInfo() {
        return info;
    }

    public void setInfo(int id) throws SQLException {
        this.info = controller.getMyClientSeverInfo(id);
    }



}
