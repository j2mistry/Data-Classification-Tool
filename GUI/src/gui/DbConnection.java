/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.spi.DirStateFactory;
import oracle.sql.*;



public class DbConnection {

    int db;
    Statement st;
    String  JDBC_DRIVER = "com.mysql.jdbc.Driver";
    String DB_URL = "jdbc:mysql://localhost";
    String ODB_URL = "jdbc:oracle:thin:@localhost:1521";
    
    Connection conn;
    String tableName;
    String className;
    public DbConnection() {
        
    }
    
    

    DbConnection(int selectedIndex) throws ClassNotFoundException, SQLException {
        db=selectedIndex;
        if(db==1)
        {
            Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL,"root","root");
         
        }
        else
        {
            	Class.forName("oracle.jdbc.driver.OracleDriver");
 	conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:XE", "system",
					"root");
 
        }
    }
    
    public void selectDb(String dbName) throws SQLException
    {
        System.out.println(dbName  +"      LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL  ");
        if(db==1)
        conn = DriverManager.getConnection(DB_URL+ "/"+dbName,"root","root");
        else
            conn = DriverManager.getConnection(ODB_URL+ ":"+dbName,"system","root");
         
    }

    public Statement getStatement() throws SQLException {
        return conn.createStatement();
   
    }
    
}
