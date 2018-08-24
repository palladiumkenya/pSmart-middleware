package dbConnection;


import javax.management.Query;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.DriverManager.println;

public  class DBConnection {

    private static String host = "localhost";
    private static String port = "3306";
    private static String dbName = "psmart";
    private static String dburl = "jdbc:mysql://"+host + ":"+ port +"/"+dbName + "?autoReconnect=true&useSSL=false";
    private static String className = "com.mysql.jdbc.Driver";
    private static Connection conn=null;
    private static ResultSet rs=null;
    private static String username = "root"; //get from properties file
    private static String password = "maun"; //get from properties file

    public static Connection connect() throws SQLException{

        try{
            Class.forName(className).newInstance();
        }catch(ClassNotFoundException cnfe){
            System.err.println("Error: "+cnfe.getMessage());
        }catch(InstantiationException ie){
            System.err.println("Error: "+ie.getMessage());
        }catch(IllegalAccessException iae){
            System.err.println("Error: "+iae.getMessage());
        }
        conn = DriverManager.getConnection(dburl,username,password);
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
