package App;

import threads.ClientThread;
import threads.ServerThread;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Main program, glavni dio aplikacije. Aplikacije se pokreće iz ove klase. Ako se
 * aplikacije pokerene iz ove klase, onda će aplikacije biti samo konzolska
 */
public class Main_console {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        System.out.println("Are you connected to network? Enter your id:");
        Scanner sc = new Scanner(System.in);
        int id = -1;

        while(true){
            String tmp = sc.nextLine();
            try{
                id = Integer.parseInt(tmp);
                break;
            }catch(NumberFormatException ex){
                System.out.println("Write -1 if you aren't part of the network, and integer if you are.");
            }
        }

        ClientServer cs = new ClientServer();
        cs.setInfo(id);

        /*controller = DatabaseController.getDatabaseController();
        info = controller.getMyClientSeverInfo(id);*/
        //pokreni dvije dretve, serversku i klijentsku, tako da ova aplikacije bude u isto vrijeme server i klijent
        Thread serverThread = new ServerThread(cs.getInfo());
        Thread clientThread = new ClientThread(cs.getInfo());

        serverThread.start();
        clientThread.start();

        //potreban join? jer ono... tako i tako ce se aplikacije prekinuti > za demo dovoljno?
    }


}
