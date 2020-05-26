/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.database;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author LED-COM
 */
public class DatabaseHandler {

    private static DatabaseHandler handler = null;
    public static Connection conn;
    private static Statement stmt;

    private DatabaseHandler() {
        createConnection();
//        setupBookTable();
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    public Connection createConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ladatabase", "root", "fm38739");
            //stmt = conn.createStatement();
            System.out.println("Database connection successful");
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            return conn;
        }
    }

//    void setupBookTable() {
//        String TABLE_NAME = "BOOK";
//        try {
//            stmt = conn.createStatement();
//            DatabaseMetaData dm = conn.getMetaData();
//            ResultSet tables = dm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
//            if (tables.next()) {
//                System.out.println("Table " + TABLE_NAME + " already exists!");
//            } else {
//                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
//                        + "  bookID varchar(200) primary key, \n"
//                        + "  bookTitle varchar(200), \n"
//                        + "  bookAuthor varchar(200), \n"
//                        + "  bookPublisher varchar(200), \n"
//                        + "  isAvailable boolean default true"
//                        + ")");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        }
//    }
//z    public ResultSet execQuery(String query) {
//        ResultSet resultset;
//        try {
//            stmt = conn.createStatement();
//            resultset = stmt.executeQuery(query);
//        } catch (SQLException se) {
//            System.out.println("Exception at execQuery: databaseHandler" + se.getLocalizedMessage());
//           return null;
//        }finally{}
//        
//        return resultset;
//    }
//
//    public boolean execAction(String q) {
//        try {
//            PreparedStatement pstmt = conn.prepareStatement(q);
//            pstmt.execute();
//            return true;
//        } catch (SQLException se) {
//            JOptionPane.showMessageDialog(null, "Error  :" + se.getMessage(), "Error occured", JOptionPane.ERROR_MESSAGE);
//            System.out.println("Exception at execQuery: databaseHandler" + se.getLocalizedMessage());
//            return false;
//        }finally{
//            
//        }
//    }
//   Connection conn = dataSource.getConnection();
//   
//   try{
//    Statement smtmt  = conn.createStatement();
//    
//}catch(){
//    
//}finally{
//    if(smtmt != null){
//        smtmt.close();
//    }
//    if(conn != null){
//        conn.close();
//    }
//}
    
    
    
//    try(Connection con =  DriverManager.getConnection(database-url, user, password); Statement st = conn.createStatement()) {
//
// //your stuffs here
//} catch (SQLException e) {
//   e.printStackTrace();
//}    
}
