package com.FYP.UI.Users;


import com.FYP.Database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserPrivilegesController implements Initializable {


    @FXML
    private JFXTextField usernameTF;

    @FXML
    private JFXPasswordField passwordPF;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> usernameCol;

    @FXML
    private TableColumn<User, String> passwordCol;

    private DatabaseHandler databaseHandler;
    private ObservableList<User> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = new DatabaseHandler();
        initCol();
        loadData();
    }

    private void initCol() {
        usernameCol.setCellValueFactory(e -> e.getValue().usernameProperty());
        passwordCol.setCellValueFactory(e -> e.getValue().passwordProperty());
    }

    private void loadData() {
        String query = "SELECT username,password FROM users;";

        ResultSet rs = databaseHandler.execQuery(query);

        try {
            while (rs.next()){
                String user = rs.getString(1);
                String pass = rs.getString(2);

                data.add(new User(user, pass));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(null);
        tableView.setItems(data);
    }

    private Boolean handleTextFields(){
        String user = usernameTF.getText();
        String pass = passwordPF.getText();
        return (!user.isEmpty() && !user.trim().isEmpty()) || (!pass.isEmpty() && !pass.trim().isEmpty());

    }

    @FXML
    void handleAddButton() {

        String user = usernameTF.getText();
        String pass = passwordPF.getText();

        String query = "INSERT INTO users(username, password) VALUES ('" + user + "','" + pass + "');";

        if (!handleTextFields()) {
//            System.out.println("Please Fill Textfields");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all TextFields and then Click on Add Button");
            alert.showAndWait();
        } else {
            databaseHandler.execAction(query);
            TrayNotification trayNotification = new TrayNotification();
            trayNotification.setNotificationType(NotificationType.SUCCESS);
            trayNotification.setTitle("User Added Successfully");
            trayNotification.setMessage("User: " + user + " added Successful");
            trayNotification.showAndDismiss(Duration.millis(2000));
            clearFields();
            data.clear();
            loadData();
        }
    }

    private void clearFields() {
        usernameTF.clear();
        passwordPF.clear();
    }

    @FXML
    void handleDeleteButton() {
        User user = tableView.getSelectionModel().getSelectedItem();
        String username = user.getUsername();


        String query = "DELETE FROM users WHERE username = '" + username + "';";

        if (username == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a user from table and then Click on Add Button");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure want to delete user?");
            Optional<ButtonType> answer = alert.showAndWait();

            if (answer.get().equals(ButtonType.OK)) {
                databaseHandler.execAction(query);
                TrayNotification trayNotification = new TrayNotification();
                trayNotification.setNotificationType(NotificationType.SUCCESS);
                trayNotification.setTitle("User Deleted Successfully");
                trayNotification.setMessage("User: " + username + " deleted Successful");
                trayNotification.showAndDismiss(Duration.millis(2000));
                data.clear();
                loadData();
            } else {
                TrayNotification trayNotification = new TrayNotification();
                trayNotification.setNotificationType(NotificationType.SUCCESS);
                trayNotification.setTitle("User Deletion Failed");
                trayNotification.setMessage("User: " + username + " not deleted Successfully");
                trayNotification.showAndDismiss(Duration.millis(2000));
            }

        }

    }


    public void handleEnterKey(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            handleAddButton();
        }
    }


}
