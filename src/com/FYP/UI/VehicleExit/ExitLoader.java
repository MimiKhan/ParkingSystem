package com.FYP.UI.VehicleExit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ExitLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/FYP/UI/VehicleExit/vehicleExit.fxml"));
        primaryStage.setTitle("Vehicle Exit");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
