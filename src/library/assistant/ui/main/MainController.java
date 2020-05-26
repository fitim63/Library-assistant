/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.main;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;

public class MainController implements Initializable {

    @FXML
    private HBox book_info;
    @FXML
    private Text bookName;
    @FXML
    private Text bookAuthor;
    @FXML
    private JFXTextField bookIDinput;
    @FXML
    private Text bookStatus;
    @FXML
    private HBox member_info;

    DatabaseHandler db;
    @FXML
    private JFXTextField memberIDinput;
    @FXML
    private Text memberName;
    @FXML
    private Text memberContact;
    @FXML
    private JFXTextField bookIDinput2;
    @FXML
    private ListView<String> issueDataList;

    boolean readyForSubmission = false;

    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(book_info, 1);
        JFXDepthManager.setDepth(member_info, 1);

        db = DatabaseHandler.getInstance();
    }

    void loadWindow(String location, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void loadAddMember(ActionEvent event) {
        loadWindow("/library/assistant/ui/addMembers/addMember.fxml", "Add New Member");
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        // loadWindow("/library/assistant/ui/addMembers/addMember.fxml", "Add New Member");  
        loadWindow("/library/assistant/ui/addBook/FXMLDocument.fxml", "Add New Book");
    }

    @FXML
    private void loadMembers(ActionEvent event) {
        loadWindow("/library/assistant/ui/listMembers/memberList.fxml", "Members");
    }

    @FXML
    private void loadBooks(ActionEvent event) {
        loadWindow("/library/assistant/ui/listBooks/bookList.fxml", "Books");
    }

    @FXML
    private void loadBookInfo(ActionEvent event) {
        String id = bookIDinput.getText();
        String q = "select * from book WHERE bookID = ?";

        try (Connection con = db.createConnection(); PreparedStatement pstmt = con.prepareStatement(q)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery();) {

                Boolean flag = false;

                while (rs.next()) {
                    String bName = rs.getString("bookTitle");
                    String bAuthor = rs.getString("bookAuthor");
                    boolean bStatus = rs.getBoolean("isAvail");
                    System.out.println(bStatus);
                    bookName.setText(bName);
                    bookAuthor.setText(bAuthor);
                    String status = (bStatus) ? " Available " : " Not Available!";
                    bookStatus.setText(status);

                    flag = true;
                }
                if (!flag) {
                    bookName.setText("No such book Available!");
                    bookAuthor.setText("");
                    bookStatus.setText("");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadMemberInfo(ActionEvent event) {
        String id = memberIDinput.getText();
        String q = "select * from member WHERE memberID = ?";

        try (Connection con = db.createConnection(); PreparedStatement pstmt = con.prepareStatement(q);) {
            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery();) {

                Boolean flag = false;

                while (rs.next()) {
                    String mName = rs.getString("memberName");
                    String mContact = rs.getString("mobile");

                    memberName.setText(mName);
                    memberContact.setText(mContact);
                    flag = true;
                }
                if (!flag) {
                    memberName.setText("No such member Available!");
                    memberContact.setText("");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadIssue(ActionEvent event) {
        String mID = memberIDinput.getText();
        String bID = bookIDinput.getText();

        if ((mID == null || mID.trim().isEmpty()) || (bID == null || bID.trim().isEmpty())) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("No null values allowed");
            alert1.showAndWait();
            memberIDinput.setText("");
            bookIDinput.setText("");
            return;
        }

        if (bookName.getText().equals("No such book Available!")) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("No such Book exists");
            alert1.showAndWait();
            bookIDinput.setText("");
            bookName.setText("Book Name");
            bookAuthor.setText("Author");
            bookStatus.setText("Book Status");
            return;
        }

        if (memberName.getText().equals("No such member Available!")) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("No such Member exists");
            alert1.showAndWait();
            memberIDinput.setText("");
            memberName.setText("Member Name");
            memberContact.setText("Contact");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm issue operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to issue the book : "
                + bookName.getText() + "\n to " + memberName.getText() + " ?");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.get() == ButtonType.OK) {
            String q2 = "UPDATE book SET isAvail = false WHERE bookID = ?";
            try (Connection con = db.createConnection(); PreparedStatement pstmt2 = con.prepareStatement(q2)) {
                pstmt2.setString(1, bID);

                pstmt2.execute();
            } catch (SQLException e) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
            }

            String q = "insert into issue(memberID ,bookID) values(?,?)";

            try (Connection con = db.createConnection(); PreparedStatement pstmt = con.prepareStatement(q)) {

                pstmt.setString(1, mID);
                pstmt.setString(2, bID);

                //System.out.println(q + " and " + q2);
                if (pstmt.execute()) {

                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Failed");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book issue failed");
                    alert1.showAndWait();
                } else {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Success");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book issue completed");
                    alert1.showAndWait();
                    bookIDinput.setText("");
                    memberIDinput.setText("");
                    bookName.setText("Book Name");
                    bookAuthor.setText("Author");
                    bookStatus.setText("Book Status");
                    memberName.setText("Member Name");
                    memberContact.setText("Contact");
                }
            } catch (SQLException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void loadBookInfo2(ActionEvent event) {
        ObservableList<String> issueData = FXCollections.observableArrayList();
        String bID = bookIDinput2.getText();
        readyForSubmission = false;
        String q = "select * from issue where bookID = ?";
        String q2 = "select * from book where bookID = ?";
        String q3 = "select * from member where memberID = ?";
        try (Connection con = db.createConnection(); PreparedStatement pstmt = con.prepareStatement(q);
                PreparedStatement pstmt2 = con.prepareStatement(q2);) {

            pstmt.setString(1, bID);
            pstmt2.setString(1, bID);

            try (ResultSet rs = pstmt.executeQuery(); ResultSet rs2 = pstmt2.executeQuery();) {
                while (rs.next()) {
                    String mBookID = rs.getString("bookID");
                    String mMemberID = rs.getString("memberID");
                    Timestamp mIssueTime = rs.getTimestamp("issueTime");
                    int mRenewCount = rs.getInt("renew_count");

                    issueData.add("Issue date and time   -   " + mIssueTime.toGMTString());
                    issueData.add("Renew Count   -   " + mRenewCount);

                    issueData.add("");
                    while (rs2.next()) {
                        issueData.add("Book Name   -   " + rs2.getString("bookTitle"));
                        issueData.add("Book ID   -   " + rs2.getString("bookID"));
                        issueData.add("Book Author   -   " + rs2.getString("bookAuthor"));
                        issueData.add("Book Publisher   -   " + rs2.getString("bookPublisher"));

                    }
                    try (PreparedStatement pstmt3 = con.prepareStatement(q3);) {
                        pstmt3.setString(1, mMemberID);
                        try (ResultSet rs3 = pstmt3.executeQuery()) {
                            issueData.add("");
                            while (rs3.next()) {
                                issueData.add("Member Name   -   " + rs3.getString("memberName"));
                                issueData.add("Member ID   -   " + rs3.getString("memberID"));
                                issueData.add("Member Mobile   -   " + rs3.getString("mobile"));
                                issueData.add("Member Email   -   " + rs3.getString("email"));
                            }
                        }
                    }
                    readyForSubmission = true;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }

        issueDataList.getItems().setAll(issueData);
    }

    @FXML
    private void submissionMethod(ActionEvent event) {
        if (!readyForSubmission) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("Please select a book to submit!");
            alert1.showAndWait();
            return;
        }
        String bID = bookIDinput2.getText();
        String q = "delete from issue where bookID = ?";
        String q2 = "update book set isAvail = true where bookID = ?";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Submission operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to return the book ?");
        Optional<ButtonType> response = alert.showAndWait();

        try (Connection con = db.createConnection(); PreparedStatement pstmt = con.prepareStatement(q);
                PreparedStatement pstmt2 = con.prepareStatement(q2)) {

            pstmt.setString(1, bID);
            pstmt2.setString(1, bID);
            pstmt2.execute();
            if (response.get() == ButtonType.OK) {
                if (pstmt.execute()) {

                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Failed");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book Submission Failed");
                    alert1.showAndWait();
                } else {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Success");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book Submission Completed");
                    alert1.showAndWait();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void renewMethod(ActionEvent event) {
        String bID = bookIDinput2.getText();
        if (!readyForSubmission) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("Please select a book to renew!");
            alert1.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Submission operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to renew the book ?");
        Optional<ButtonType> response = alert.showAndWait();

        if (response.get() == ButtonType.OK) {
            String q = "UPDATE issue SET issuetime = CURRENT_TIMESTAMP, renew_count = renew_count+1 WHERE bookID = ?";
            try (Connection con = db.createConnection(); PreparedStatement pstmt = con.prepareStatement(q)) {
                pstmt.setString(1, bID);
                if (pstmt.execute()) {

                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Failed");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book Renew Failed");
                    alert1.showAndWait();
                } else {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Success");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book renew Completed");
                    alert1.showAndWait();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
