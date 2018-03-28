package dbConnection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.sql.DriverManager.println;
import static jsonvalidator.utils.PropertiesManager.*;

public  class DBConnection {
    private static String dburl = "jdbc:mysql://"+ DB_HOST + ":"+ DB_PORT +"/"+ DB_NAME + "?autoReconnect=true&useSSL=false";
    private static String className = "com.mysql.jdbc.Driver";
    private static Connection conn=null;

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
        conn = DriverManager.getConnection(dburl, DB_USERNAME, DB_PASSWORD);
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
