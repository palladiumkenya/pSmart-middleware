package dbConnection;


import javax.management.Query;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.DriverManager.println;

public  class DBConnection {

    private static String username;
    private static String password;
    private static String host;
    private static int port;
    private static String dbName;
    private static String dburl;
    private static Connection conn=null;
    private static ResultSet rs=null;

    public DBConnection(String database){
        this.dbName = database;
        this.username="root";
        this.password="root";
        this.host="localhost";
        this.port=3306;
        this.dbName="psmart";
        this.dburl="jdbc:mysql://"+this.host+"/"+ this.dbName;//LOGIN is the database.
    }

    public static Connection connect() throws SQLException{

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(ClassNotFoundException cnfe){
            System.err.println("Error: "+cnfe.getMessage());
        }catch(InstantiationException ie){
            System.err.println("Error: "+ie.getMessage());
        }catch(IllegalAccessException iae){
            System.err.println("Error: "+iae.getMessage());
        }
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/psmart","root","");
        return conn;
    }

    public static Connection getConnection() throws SQLException{
        if(conn !=null && !conn.isClosed())
            return conn;
        connect();
        return conn;
    }

    public static void closeConnection() throws SQLException {

        try{
            connect();
            if(conn!=null){ conn.close();}
        }catch(Exception ex){
            println(""+ex.getMessage());
        }
    }


}
