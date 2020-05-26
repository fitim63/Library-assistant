/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.addBook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listBooks.BookListController;

/**
 * // * FXML Controller class
 *
 * @author LED-COM
 */
public class FXMLDocumentController  implements Initializable {

    @FXML
    private JFXTextField bookTitle;
    @FXML
    private JFXTextField bookID;
    @FXML
    private JFXTextField bookAuthor;
    @FXML
    private JFXTextField bookPublisher;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton cancelBtn;

    /**
     * Initializes the controller class.
     */
//    DatabaseHandler databaseHandler;
    
    @FXML
    private AnchorPane rootPane;
    DatabaseHandler db;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      db = DatabaseHandler.getInstance();
//        checkData();
    }

    @FXML
    private void addBook(ActionEvent event) {
        
            
        String title = bookTitle.getText();
        String id = bookID.getText();
        String author = bookAuthor.getText();
        String publisher = bookPublisher.getText();

        if (title.trim().isEmpty() || id.trim().isEmpty() || author.trim().isEmpty() || publisher.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR); 
            alert.setHeaderText(null);
            alert.setContentText("Please enter all fields!");
            alert.showAndWait();
            return;
        }
        //try(Connection con = db.createConnection()) {
        String query = "insert into `book` (`bookTitle`, `bookID`, `bookAuthor`, `bookPublisher`, `isAvail`)"
                    + " values (?, ?, ?, ?, ?)";
            
             
        try(Connection con = db.createConnection(); PreparedStatement pstmt = con.prepareStatement(query)){  
            pstmt.setString(1, title);
            pstmt.setString(2, id);
            pstmt.setString(3, author);
            pstmt.setString(4, publisher);
            pstmt.setBoolean(5, true);
            System.out.println(query);
            
            
//            if(databaseHandler.execAction(query)){
            if (pstmt.execute()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success");
                alert.showAndWait();
                bookAuthor.setText("");
                bookID.setText("");
                bookPublisher.setText("");
                bookTitle.setText("");
            }
            
        } catch (SQLException e) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, e);
        }   
    }
    
//    public void inflateUI(BookListController.book book){
//        bookTitle.setText(book.getTitle());
//        bookID.setText(book.getId());
//        bookAuthor.setText(book.getAuthor());
//        bookPublisher.setText(book.getPublisher());
//        
//        bookID.editableProperty().set(false);
//    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage)rootPane.getScene().getWindow();
        stage.close();
    }

//    private void checkData() {
//
//         try {
////            String query = "SELECT ? FROM `BOOK`";
////            
////            PreparedStatement pstmt = conn.prepareStatement(query);
////            pstmt.setString(1, bookTitle.getText());
////            ResultSet rs = pstmt.executeQuery();
//            String q = "select bookTitle from book";
//            Statement stmt = db.conn.createStatement();
//            ResultSet rs = stmt.executeQuery(q);
//            while (rs.next()) {
//                String title = rs.getString("bookTitle");
//                System.out.println(title);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
