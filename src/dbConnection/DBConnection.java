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
    public Connection connectionString=null;
    public ResultSet rs=null;

    public DBConnection(){
        this.database = database;
        this.username="root";
        this.password="root";
        this.host="localhost";
        this.port=3306;
        this.database="psmart";
        this.dburl="jdbc:mysql://"+this.host+"/"+ this.database;//LOGIN is the database.
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+","+username+","+password);
    }

    public void OpenConnection(){

    }

    public void CloseConnection() {

        try{
            if(this.connectionString!=null){ connectionString.close();}
        }catch(Exception ex){
            println(""+ex.getMessage());
        }
    }

    public  ResultSet ExecuteQuery(String query) {

        try{

           rs=(connectionString.prepareStatement(query)).executeQuery();
        }
        catch(SQLException ex){
            println(ex.getMessage());
        }
        return rs;
    }
}
