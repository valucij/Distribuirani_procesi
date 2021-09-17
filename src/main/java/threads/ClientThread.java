package threads;

import beans.ClientServerInfo;
import database.DatabaseController;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Dretva koja predstavlja rad kad objekt uzme ulogu klijenta
 */
public class ClientThread extends Thread {
    private final ClientServerInfo info;

    public ClientThread(ClientServerInfo info){
        this.info = info;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        //Cijelo vrijeme (do prekida aplikacije) se može tražiti u mreži ključna riječ
        while(true){
            System.out.println("Enter 'start' if you want to start search, enter 'end' if you want to end search");
            String tmp = sc.nextLine();
            if(tmp.equals("end")){
                break;
            }
            //Ako se želi nešto tražiti, treba se naznačiti prvo sa ključnom riječi search
            if(tmp.equals("search")){
                try {
                     // Pošalji na pretraga
                    String[] s = search();
                    System.out.println("Id server: " + s[0] + ", word: " + s[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        sc.close();
    }

    /**
     * Metoda u kojoj se obavlja pretraga
     * Napomena: treba dodati da se i kod sebe prvo potraži, prije nego se pošalji na traženje djeci
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public String[] search() throws SQLException, IOException, ClassNotFoundException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter query");
        String query = sc.nextLine();

        int id_left_child = info.getId_left_child();
        int id_right_child = info.getId_right_child();

        String result = "";

        if(id_left_child != -1){
            //pošalji upit svom lijevom dijetetu
            result = sendQueryToChild(id_left_child, query);
        }
        if(result.equals("Not found") && id_right_child != -1){
            //pošalji upit svom desnom dijetetu
            result = sendQueryToChild(id_right_child, query);
        }
        sc.close();
        if(result.equals("Not found")){
            String[] r = new String[2];
            r[0] = "-1";
            r[1] = result;
            return r;
        }

        String[] r = result.split(",");//prvo je id servera na kojem se nalazi file, drugo je taj file; u ovom slucaju file je rijec
        return r;
    }

    /**
     * Metoda u kojoj dolazi do konekcije sa zadanim čvorom, i čvoru se šalje upit
     *
     * @param id_child
     * @param query
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private String sendQueryToChild(int id_child, String query) throws SQLException, ClassNotFoundException, IOException {
        DatabaseController controller = DatabaseController.getDatabaseController();

        ClientServerInfo info = controller.getMyClientSeverInfo(id_child);
        int port = info.getPort();

        Socket socket = new Socket("localhost", port);

        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.print(query);

        InputStream input = socket.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        return reader.readLine();

    }
}
