package com.FYP.UI.Dashboard;

import com.FYP.Database.DatabaseHandler;
import com.FYP.UI.VehicleEntry.EntryController;
import com.FYP.UI.VehicleExit.ExitController;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class DashboardController implements Initializable {

    public Label usernameLabel;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private JFXButton vehicleEntryBtn;

    @FXML
    private JFXButton vehicleExitBtn;

    @FXML
    private JFXButton showDataBtn;

    @FXML
    private JFXButton UsersBtn;

    private DatabaseHandler databaseHandler;
    private String usernameFromLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = new DatabaseHandler();

    }


    public void getUser(String username) {

        if (username.equals("admin")) {
            vehicleEntryBtn.setDisable(true);
            vehicleExitBtn.setDisable(true);

            usernameLabel.setText("Logged In as : " + "admin");
        } else {
            usernameFromLogin = username;
            showDataBtn.setDisable(true);
            UsersBtn.setDisable(true);
            System.out.println(usernameFromLogin);
            usernameLabel.setText("Logged In as : " + usernameFromLogin);
        }
    }

    @FXML
    void handleLogout() {

        String query = "UPDATE users SET is_online = 0 WHERE is_online = 1;";

        if (databaseHandler.execAction(query)) {
            TrayNotification trayNotification = new TrayNotification();
            trayNotification.setNotificationType(NotificationType.SUCCESS);
//            trayNotification.setTitle("Logout Successful");
            trayNotification.setMessage("Logout Successful");
            trayNotification.showAndDismiss(Duration.millis(2000));
            logoutButton.getScene().getWindow().hide();
            loadStage("/com/FYP/UI/Login/login.fxml","Login");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Logout Failed");
            alert.showAndWait();
        }
    }

    public void handleVehicleEntry() {
        loadStageForEntry();
     }

    public void handleVehicleExit() {
        loadStageForExit();
    }

    public void handleDataDisplay() {
        loadStage("/com/FYP/UI/DisplayData/displayData.fxml","Data of Vehicles parked");
    }

    public void handleUsers() {
        loadStage("/com/FYP/UI/Users/userPrivileges.fxml","Users Window");
    }

    @FXML
    void handleExitButton() {
        String query = "UPDATE users SET is_online = 0 WHERE is_online = 1;";

        if (databaseHandler.execAction(query)) {
            TrayNotification trayNotification = new TrayNotification();
            trayNotification.setNotificationType(NotificationType.SUCCESS);
            trayNotification.setTitle("Logout Successful");
            trayNotification.setMessage("Logout Successful");
            trayNotification.showAndDismiss(Duration.millis(2000));
            System.exit(1);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Software Exit Failed.");
            alert.showAndWait();
        }
    }

    private void loadStage(String url, String title){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = FXMLLoader.load(getClass().getResource(url));
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setResizable(false);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getLocalizedMessage());
            alert.show();
        }
    }

    private void loadStageForEntry(){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/com/FYP/UI/VehicleEntry/vehicleEntry.fxml").openStream());
            EntryController controller = loader.getController();
            controller.getUser(usernameFromLogin);
            stage.setTitle("Vehicle Entry");
//            stage.getIcons().add(new Image("/com/resources/logo.ico"));
            stage.setResizable(false);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private void loadStageForExit(){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/com/FYP/UI/VehicleExit/vehicleExit.fxml").openStream());
            ExitController controller = loader.getController();
            controller.getUser(usernameFromLogin);
            stage.setTitle("Vehicle Exit");
//            stage.getIcons().add(new Image("/com/resources/logo.ico"));
            stage.setResizable(false);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }



}
