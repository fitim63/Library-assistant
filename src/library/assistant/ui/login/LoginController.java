/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.main.MainController;

/**
 * FXML Controller class
 *
 * @author LED-COM
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField LoginUsername;
    @FXML
    private JFXPasswordField LoginPassword;

    /**
     * Initializes the controller class.
     */
    DatabaseHandler db;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = DatabaseHandler.getInstance();
    }

    @FXML
    private void cancelMethod(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void loginMethod(ActionEvent event) {
        String username = LoginUsername.getText();
        String password = LoginPassword.getText();

        String q = "select * from login where username=? and password=?";

        try (Connection con = db.createConnection(); PreparedStatement pstmt = con.prepareStatement(q)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    closeStage();
                    loadWindow();
                } else {
                    JOptionPane.showMessageDialog(null, "Username and password NOT  matched");
                    LoginUsername.setText("");
                    LoginPassword.setText("");
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void closeStage() {
        ((Stage) LoginUsername.getScene().getWindow()).close();

    }

    void loadWindow() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/Main.fxml"));

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Libray Assistant");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
