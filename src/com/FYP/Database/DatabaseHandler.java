package com.FYP.Database;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

public class DatabaseHandler {

    private static Connection con = null;
    private static Statement stmt = null;
    private PreparedStatement preparedStatement = null;

    public DatabaseHandler() {
        createConnection();

    }

    private void createConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3300/car_parking","root","Mimikhan007");
        } catch (ClassNotFoundException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Database Not Found. Please Check if server's running or not?");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public ResultSet execQuery(String qu){
        ResultSet resultSet;
        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(qu);
        } catch (SQLException e) {
            System.out.println("Exception at execQuery:database " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return null;
        }
        return resultSet;
    }

    public boolean execAction(String query){
        try {
            stmt = con.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException e) {
            System.out.println("Exception at execAction:database " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    boolean insertImage(String query, String filePath) {

        try {
            File image = new File(filePath);
            FileInputStream inputStream = new FileInputStream(image);


            preparedStatement = con.prepareStatement(query);
            preparedStatement.setBinaryStream(1, inputStream, (int)(image.length()));

            preparedStatement.executeUpdate();

            return true;

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: - " + e);
        } catch (SQLException e) {
            System.out.println("SQLException: - " + e);
        } finally {

            try {
                con.close();
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("SQLException Finally: - " + e);
            }
        }
        return false;
    }
}


