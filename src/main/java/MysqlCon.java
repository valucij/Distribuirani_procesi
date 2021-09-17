import java.sql.*;

/**
 * Klasa koja služi samo za testiranje. NIJE DIO APLIKACIJE
 */
class MysqlCon{
    public static void main(String args[]){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/projekt","kirlia","kirliapass");
            //here sonoo is database name, root is username and password
         /*   Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("show tables;");
            System.out.println(rs.getString(1));*/
            String query = "SELECT * FROM demo";

            // create the java statement
            Statement st = con.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            // iterate through the java resultset
            while (rs.next())
            {
                //int id = rs.getInt("id");
                String age = rs.getString("age");
                String lastName = rs.getString("name");


                // print the results
                System.out.format(" %s, %s, \n", /*id,*/ age, lastName);
            }
            con.close();
        }catch(Exception e){ System.out.println(e);}
    }
}