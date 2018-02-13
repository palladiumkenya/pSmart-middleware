package dbConnection;


import javax.management.Query;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.DriverManager.println;

public class DBConnection {

    private String username;
    private String password;
    private String host;
    private int port;
    private String database;
    private String dburl;
    private Connection connectionString=null;
    private ResultSet rs=null;

    public DBConnection(String database){
        this.database = database;
        this.username="root";
        this.password="root";
        this.host="localhost";
        this.port=3306;
        this.database="psmart";
        this.dburl="jdbc:mysql://"+this.host+"/"+ this.database;//LOGIN is the database.
    }

    //@Override
    public Connection GetConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connectionString = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);

            if (!connectionString.isClosed()) {
                println("Connection Successful");

            } else {

            }
        }
        catch(Exception  ex )
        {
            ex.printStackTrace();
        }
        return connectionString;
    }

    //@Override
    public void CloseConnection() {

        try{
            if(this.connectionString!=null){ connectionString.close();}
        }catch(Exception ex){
            println(""+ex.getMessage());
        }
    }

   // @Override
    public ResultSet ExecuteQuery(String query) {

        try{
           rs=(this.connectionString.prepareStatement(query)).executeQuery();
        }
        catch(SQLException ex){
            println(ex.getMessage());
        }
        return rs;
    }
}
