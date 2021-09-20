package threads;

import App.Main_gui;

import java.io.*;
import java.net.Socket;

/**
 * Klasa koja predstavlja trenutak kada neki objekt koji je do tad bio server mora postati klijent i mora se spojiti
 * na neki drugi objekt koji će tada uzeti ulogu servera
 */
public class Runnable_ implements Runnable{

    private String value;
    private int id;
    private int port;
    private String query;
    private Main_gui gui;

    public Runnable_(int id,int port, String query){
        this.id = id;
        this.port = port;
        this.query = query;
    }

    @Override
    public void run() {

        try {
            Socket socket = new Socket("localhost", port);
            //System.out.println("Spojio se na port " + port);
            String tmp = gui.logovi.getText();
            tmp += "\n";
            tmp += "Spojio se na port " + port + "\n";
            gui.logovi.setText(tmp);

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.print(query);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            value =  reader.readLine();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getValue(){
        return this.value;
    }
    public void setGUI(Main_gui gui){this.gui = gui;}
}
