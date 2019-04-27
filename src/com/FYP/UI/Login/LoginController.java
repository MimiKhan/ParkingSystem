package com.FYP.UI.Login;

import com.FYP.Database.DatabaseHandler;
import com.FYP.Database.DatabaseHandler;
import com.FYP.UI.Dashboard.DashboardController;
import com.FYP.UI.VehicleEntry.EntryController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton closeButton;

    private DatabaseHandler databaseHandler;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        databaseHandler = new DatabaseHandler();
    }

    public void handleLoginButton(){

        String user = usernameField.getText();
        String pw = passwordField.getText();

        String dbuser = null;
        String dbpw = null;
        int id = 0;
        boolean isOnline = false;

        String query = "SELECT * from users WHERE username = '" + user + "';";

        ResultSet rs = databaseHandler.execQuery(query);
        try {
            while (rs.next()) {
                id = rs.getInt("user_id");
                dbuser = rs.getString("username");
                dbpw = rs.getString("password");
                isOnline = rs.getBoolean("is_online");
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        if (user.equals("admin") && pw.equals("admin")) {
            TrayNotification trayNotification = new TrayNotification();
            trayNotification.setNotificationType(NotificationType.SUCCESS);
            trayNotification.setTitle("Login Successful");
            trayNotification.setMessage("Login as Admin Successful");
            trayNotification.showAndDismiss(Duration.millis(2000));
            loadWindow();
            clearFields();
            loginButton.getScene().getWindow().hide();
        } else {
            if (user.equals(dbuser) && pw.equals(dbpw)) {
                if (!isOnline) {
                    String query1 = "UPDATE users SET is_online = 1 WHERE user_id = '" + id + "';";
                    databaseHandler.execAction(query1);
                    TrayNotification trayNotification = new TrayNotification();
                    trayNotification.setNotificationType(NotificationType.SUCCESS);
                    trayNotification.setTitle("Login Successful");
                    trayNotification.setMessage("Login as " + user + " Successful");
                    trayNotification.showAndDismiss(Duration.millis(2000));
                    loginButton.getScene().getWindow().hide();
                    loadWindow();
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("User is already online. Try with another user.");
                    alert.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username or password incorrect.");
                alert.showAndWait();
            }
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    private void loadWindow(){
        String user = usernameField.getText();
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/com/FYP/UI/Dashboard/dashboard.fxml").openStream());
            DashboardController controller = loader.getController();
            controller.getUser(user);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Dashboard");
//            stage.getIcons().add(new Image("/com/resources/logo.ico"));
            stage.setResizable(false);
            stage.setOnCloseRequest(event -> System.exit(1));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public void handleCloseButton(){
        closeButton.getScene().getWindow().hide();

    }

    public void handleEnterKey() {
        handleLoginButton();
    }


}
