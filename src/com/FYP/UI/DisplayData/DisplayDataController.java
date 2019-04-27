package com.FYP.UI.DisplayData;

import com.FYP.Database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DisplayDataController implements Initializable {


    @FXML
    private TableView<VehicleModel> tableView;

    @FXML
    private TableColumn<VehicleModel, String> vehicleNoCol;

    @FXML
    private TableColumn<VehicleModel, String> arrivalCol;

    @FXML
    private TableColumn<VehicleModel, String> departureCol;

    @FXML
    private TableColumn<VehicleModel, Integer> hourCol;

    @FXML
    private TableColumn<VehicleModel, Double> amountCol;

    @FXML
    private TableColumn<VehicleModel, String> addedByUserCol;

    @FXML
    private TableColumn<VehicleModel, String> exitByUserCol;

    @FXML
    private JFXButton closeButton;

    private DatabaseHandler databaseHandler;
    private ObservableList<VehicleModel> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = new DatabaseHandler();
        initCol();
        loadData();
    }

    private void initCol() {
        vehicleNoCol.setCellValueFactory(new PropertyValueFactory<>("vehicleNo"));
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        departureCol.setCellValueFactory(new PropertyValueFactory<>("departure"));
        hourCol.setCellValueFactory(new PropertyValueFactory<>("hours"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        addedByUserCol.setCellValueFactory(new PropertyValueFactory<>("userAdded"));
        exitByUserCol.setCellValueFactory(new PropertyValueFactory<>("userExit"));
    }

    private void loadData() {

        String query = "SELECT V.vehicle_no,V.arrival,V.departure,V.hours_parked,\n" +
                "       V.total_amount,U1.username AS added_by_user,U2.username AS exit_by_user\n" +
                "FROM vehicles AS V\n" +
                "  INNER JOIN users AS U1\n" +
                "    ON U1.user_id = V.added_by_user\n" +
                "INNER JOIN users AS U2\n" +
                "    ON U2.user_id = V.exit_by_user;";

        ResultSet rs = databaseHandler.execQuery(query);

        try{
            while (rs.next()){
                String vehicleNo = rs.getString(1);
                String arrival = rs.getString(2);
                String departure = rs.getString(3);
                int hours = rs.getInt(4);
                double amount = rs.getDouble(5);
                String addedByUser = rs.getString(6);
                String exitByUser = rs.getString(7);

                data.add(new VehicleModel(vehicleNo,arrival,departure,hours,amount,addedByUser,exitByUser));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        tableView.setItems(null);
        tableView.setItems(data);

    }

    @FXML
    void handleClose() {
        closeButton.getScene().getWindow().hide();

    }


}

