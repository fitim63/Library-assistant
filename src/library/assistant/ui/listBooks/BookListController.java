/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.listBooks;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addBook.FXMLDocumentController;
import library.assistant.ui.listBooks.BookListController.book;

/**
 * FXML Controller class
 *
 * @author LED-COM
 */
public class BookListController implements Initializable {

    ObservableList<book> list = FXCollections.observableArrayList();
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<book> tableView;
    @FXML
    private TableColumn<book, String> titleCol;
    @FXML
    private TableColumn<book, String> idCol;
    @FXML
    private TableColumn<book, String> authorCol;
    @FXML
    private TableColumn<book, String> publisherCol;
    @FXML
    private TableColumn<book, Boolean> availableCol;

    /**
     * Initializes the controller class.
     */
    DatabaseHandler db;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = DatabaseHandler.getInstance();
        initCol();
        load();
        
    }

    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
    }

    private void load() {
//        DatabaseHandler handler = new DatabaseHandler();
        String q = "select * from book";
        try (Connection con = db.createConnection(); Statement stmt = con.createStatement();) {
//            String query = "SELECT ? FROM book";

//            PreparedStatement pstmt = db.conn.prepareStatement(query);
//            ResultSet rs = pstmt.executeQuery();
            try (ResultSet rs = stmt.executeQuery(q);) {
                while (rs.next()) {
                    String titlex = rs.getString("bookTitle");
                    String idx = rs.getString("bookID");
                    String authorx = rs.getString("bookAuthor");
                    String publisherx = rs.getString("bookPublisher");
                    Boolean availablex = rs.getBoolean("isAvail");

                    list.add(new book(titlex, idx, authorx, publisherx, availablex));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableView.setItems(list);
    }

    @FXML
    private void editBook(ActionEvent event) {
        
        
    }

    @FXML
    private void deleteBook(ActionEvent event) {
        
        book selected = tableView.getSelectionModel().getSelectedItem();
          
        
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setTitle("Deleting Book");
            alert1.setHeaderText(null);
            alert1.setContentText("Are you sure you want to delete the book : " +selected.getTitle()+" ?");
            Optional<ButtonType> answer = alert1.showAndWait();
            
            String id = selected.getId();
            if(answer.get() == ButtonType.OK){
                String sql = "Delete from book where bookID = ?";
                try(Connection con = db.createConnection(); PreparedStatement pstmt = con.prepareStatement(sql)){
                    pstmt.setString(1, id);
                    
                    pstmt.execute();
                    list.remove(selected);
                    
                } catch (SQLException ex) {
                Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
    }
    
//    public boolean isBookIssued(){
//        
//        String sql = "SELECT COUNT(*) FROM issue where bookID = ?";
//        
//        try(Connection con = db.createConnection(); PreparedStatement p = con.prepareStatement(sql)){
//            p.setString(1, book.getId());
//            ResultSet rs = p.executeQuery();
//            while(rs.next()){
//                int count = rs.getInt(1);
//                if(count > 0){
//                    return true;
//                }else{
//                    return false;
//                }
//            }
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
//    

    public static class book {

        private final SimpleStringProperty title;
        private final SimpleStringProperty id;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleBooleanProperty available;

        book(String title, String id, String author, String publisher, boolean available) {
            this.title = new SimpleStringProperty(title);
            this.id = new SimpleStringProperty(id);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.available = new SimpleBooleanProperty(available);
        }

        public String getTitle() {
            return title.get();
        }

        public String getId() {
            return id.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public Boolean getAvailable() {
            return available.get();
        }

    }
}
