package threads;

import App.Main_gui;
import beans.ClientServerInfo;
import database.DatabaseController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Klasa koja predstavlja rad objekta u trenutku kad je taj objekt server, tj u trenutku u kojem
 * preuzme ulogu servera
 */
public class ServerThread extends Thread{
    private final ClientServerInfo originalServer;
    private ClientServerInfo currentServer;
    public Main_gui gui;

    public void setGui(Main_gui gui){
        this.gui = gui;
    }

    public ServerThread(ClientServerInfo info){
        this.originalServer = info;
        this.currentServer = info;
    }

    public void run(){

        while(true){
            int port = originalServer.getPort();
            ServerSocket serverSocket = null;
            Socket clientSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("Spojio se klijent.");
               /* String tmp = gui.logovi.getText();
                tmp += "\n";
                tmp += "Spojio se klijent\n";
                gui.logovi.setText(tmp);*/
                //ako se netko spoji na njega, treba upit potražiti
                //System.out.println(in.read());
                String outString = search(in.readLine());
                //System.out.println("ovjdej je " + outString);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
               // System.out.print("outString: " + outString);
                out.println(outString);
                serverSocket.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metoda u kojoj se traži zadani upit
     * @param query
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private String search(String query) throws SQLException, ClassNotFoundException {
        int id = currentServer.getId();
        DatabaseController controller = DatabaseController.getDatabaseController();
        //prvo pogledaj postoji li kod tebe upit
        String result = controller.getStringFromServer(query, "server"+id);
        int id_left_child = currentServer.getId_left_child();
        int id_right_child = currentServer.getId_right_child();
        
       // System.out.print("test ovdje je");
        //ako postoji, vrati ga
        if(result != null){
            return currentServer.getId()+","+result;
        }
        // pogledaj postoji li kod lijevog dijeteta, ako postoji, upit. U ovom trenutku server treba uzeti ulogu klijenta, i to
        //se delegira u novu dretvu
        if(id_left_child != -1){
            ClientServerInfo info_left = controller.getMyClientSeverInfo(id_left_child);
            int port_left = info_left.getPort();
            Runnable_ left = new Runnable_(id, port_left, query);
            Thread thread = new Thread(left);
            thread.start();
            return left.getValue();

        }
        // pogledaj postoji li kod desnog dijeteta, ako postoji, upit. U ovom trenutku server treba uzeti ulogu klijenta, i to
        //se delegira u novu dretvu
        if(id_right_child != -1){
            ClientServerInfo info_right = controller.getMyClientSeverInfo(id_right_child);
            int port_right = info_right.getPort();
            Runnable_ right = new Runnable_(id, port_right, query);
            Thread threadR = new Thread(right);
            threadR.start();
            return right.getValue();
        }
        //ako do ovog trenutka ništa nije bilo nađeno, onda treba vratiti da upit nije uspio
        return "Not found";
    }
}
