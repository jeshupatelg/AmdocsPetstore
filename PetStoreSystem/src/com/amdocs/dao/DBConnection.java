package com.amdocs.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static DBConnection dbConnection;
    private static Connection conn;
    private final String URL="jdbc:Oracle:thin:@localhost:1521:orcl";
    private final String USERNAME="scott";
    private final String PASSWORD="tiger";

    private DBConnection(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("Connection established");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBConnection getDBConnection(){
        if(dbConnection == null){
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public Connection getConn(){
        return conn;
    }
    
    public void closeConnection() throws SQLException {
    	conn.commit();
    	conn.close();
    }
}
