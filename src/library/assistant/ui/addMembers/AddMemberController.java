/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.addMembers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author LED-COM
 */
public class AddMemberController implements Initializable {

    @FXML
    private JFXTextField memberName;
    @FXML
    private JFXTextField memberID;
    @FXML
    private JFXTextField mobile;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton cancelBtn;

    
    @FXML
    private AnchorPane rootPane;
    /**
     * Initializes the controller class.
     */
    
    DatabaseHandler db;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = DatabaseHandler.getInstance();
    }    


    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage)rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addMember(ActionEvent event) {
        String name = memberName.getText();
        String id = memberID.getText();
        String mMobile = mobile.getText();
        String mEmail = email.getText();
        
        if (name.trim().isEmpty() || id.trim().isEmpty() || mMobile.trim().isEmpty() || mEmail.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter all fields!");
            alert.showAndWait();
            return;
        }
        String query = "insert into `member` (`memberName`, `memberID`, `mobile`, `email`)"
                    + " values (?, ?, ?, ?)";

         try(Connection con = db.createConnection(); PreparedStatement pstmt = con.prepareStatement(query)){ 
            //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ladatabase", "root", "fm38739");
//            String query = "insert into `book` values (?,?,?,?,?)";
            
            
            pstmt.setString(1, name);
            pstmt.setString(2, id);
            pstmt.setString(3, mMobile);
            pstmt.setString(4, mEmail);
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
                memberID.setText("");
                memberName.setText("");
                mobile.setText("");
                email.setText("");
                
            }
            
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
}