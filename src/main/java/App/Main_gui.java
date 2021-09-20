package App;

import database.DatabaseController;
import threads.ClientThread;
import threads.ServerThread;
import beans.ClientServerInfo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Main_gui extends JFrame {
    private final ClientServer cs;
    private ClientThread clientThread;
    private ServerThread serverThread;
    public TextArea logovi;

    public Main_gui() throws SQLException, ClassNotFoundException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(300,250);
        setSize(600,300);
        setTitle("unamed - P2P");
        cs  = new ClientServer();
        initGUI();
    }

    private void initGUI(){
        Container cp = this.getContentPane();
        cp.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        TextField tfID = new TextField("", 30);

        cp.add(tfID, gbc);

        Button btnUmrezi = new Button("Ok");
        gbc.gridx = 1;
        gbc.gridy = 0;
        btnUmrezi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tmp = tfID.getText();

                if(tmp != null && !tmp.isEmpty() && !tmp.isBlank() && !tmp.equals("-1")){
                    try {
                        cs.setInfo(Integer.parseInt(tmp.trim()));
                        clientThread = new ClientThread(cs.getInfo());
                        serverThread = new ServerThread(cs.getInfo());
                        serverThread.setGui(Main_gui.this);
                        serverThread.start();
                        clientThread.setGUI(Main_gui.this);
                        String tmp2 = logovi.getText();
                        //tmp2 += "\n";
                        tmp2 += "Spojeno na mrezu\n";
                        logovi.setText(tmp2);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }else if(tmp != null && !tmp.isEmpty() && !tmp.isBlank() && tmp.equals("-1")){
                    DatabaseController controller = null;
                    int id = -1;
                    try {
                        controller = DatabaseController.getDatabaseController();
                        id = controller.howManyInMaster();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    id+=1;
                    try {
                        controller.insertIntoMasterTable(id, id+7001, 2, 1, 1);
                        controller.createServerTable("server"+id);
                        cs.setInfo(id);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    clientThread = new ClientThread(cs.getInfo());
                    serverThread = new ServerThread(cs.getInfo());
                    serverThread.setGui(Main_gui.this);
                    serverThread.start();
                    clientThread.setGUI(Main_gui.this);
                    String tmp2 = logovi.getText();
                    //tmp2 += "\n";
                    tmp2 += "Spojeno na mrezu\n";
                    logovi.setText(tmp2);

                }
            }
        });
        cp.add(btnUmrezi, gbc);

        //odspoji se iz mreze
        Button btnOdspoji = new Button("Odspoji se");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        btnOdspoji.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseController controller = null;
                try {
                    controller = DatabaseController.getDatabaseController();
                    controller.deleteFromMaster(cs.getInfo().getId(), "server"+cs.getInfo().getId());

                    String tmp2 = logovi.getText();
                    //tmp2 += "\n";
                    tmp2 += "Odspojeno s mreze\n";
                    logovi.setText(tmp2);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
        cp.add(btnOdspoji, gbc);


        gbc.gridwidth = 1;
        //gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        TextField tfQuery = new TextField("", 15);
        cp.add(tfQuery, gbc);

        Button btnTrazi = new Button("Trazi");
        gbc.gridx = 1;
        gbc.gridy = 2;

        btnTrazi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = tfQuery.getText();
                clientThread.setQuery(query.trim());
                try{clientThread.start();System.out.println("Unutar try");}catch(IllegalThreadStateException ex){System.out.println("Unutar bloka");}
                System.out.println("Ovdje");

            }
        });
        cp.add(btnTrazi, gbc);


        logovi = new TextArea();
        logovi.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 3;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        cp.add(logovi, gbc);


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            try {
                new Main_gui().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}
