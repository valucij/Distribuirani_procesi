import database.DatabaseController;
import database.Initialization;

import java.sql.SQLException;

/**
 * Klasa koja sluzi samo za testiranje. NIJE DIO APLIKACIJE
 */
class Demo{

    public static void main(String[]args) throws SQLException, ClassNotFoundException {

        Initialization init = Initialization.getInitialization_instance();
        init.initializeDemo();

        /*DatabaseController controller = DatabaseController.getDatabaseController();
        String query = "kuca";
        String result = controller.getStringFromServer(query, "server1");
        System.out.println(result);
*/
    }

}
