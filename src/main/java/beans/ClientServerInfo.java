package beans;

/***
 * Klasa koja sadrži informacije o klijentu/serveru.
 */
public class ClientServerInfo {
    /***
     * id se odnosi na id objekta, id kojim je ozacen u tablici. id je jedinstveni broj
     */
    private int id;
    private int id_parent;
    /***
     * Port na kojem objekt sluša
     */
    private int port;
    private int id_left_child;
    private int id_right_child;

    public int getId() {
        return id;
    }

    public int getId_parent() {
        return id_parent;
    }

    public int getPort() {
        return port;
    }

    public int getId_left_child() {
        return id_left_child;
    }

    public int getId_right_child() {
        return id_right_child;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_parent(int id_parent) {
        this.id_parent = id_parent;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setId_left_child(int id_left_child) {
        this.id_left_child = id_left_child;
    }

    public void setId_right_child(int id_right_child) {
        this.id_right_child = id_right_child;
    }
}
