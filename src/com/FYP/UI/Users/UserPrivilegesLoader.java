package com.FYP.UI.Users;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class UserPrivilegesLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("userPrivileges.fxml"));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("User Privileges");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
