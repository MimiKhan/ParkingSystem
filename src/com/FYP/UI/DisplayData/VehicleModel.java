package com.FYP.UI.DisplayData;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class VehicleModel {
    private SimpleStringProperty vehicleNo;
    private SimpleStringProperty arrival;
    private SimpleStringProperty departure;
    private SimpleIntegerProperty hours;
    private SimpleDoubleProperty amount;
    private SimpleStringProperty userAdded;
    private SimpleStringProperty userExit;

    public VehicleModel(String vehicleNo, String arrival, String departure, int hours, double amount, String userAdded, String userExit) {
        this.vehicleNo = new SimpleStringProperty(vehicleNo);
        this.arrival = new SimpleStringProperty(arrival);
        this.departure = new SimpleStringProperty(departure);
        this.hours = new SimpleIntegerProperty(hours);
        this.amount = new SimpleDoubleProperty(amount);
        this.userAdded = new SimpleStringProperty(userAdded);
        this.userExit = new SimpleStringProperty(userExit);
    }

    public String getVehicleNo() {
        return vehicleNo.get();
    }

    public SimpleStringProperty vehicleNoProperty() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo.set(vehicleNo);
    }

    public String getArrival() {
        return arrival.get();
    }

    public SimpleStringProperty arrivalProperty() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival.set(arrival);
    }

    public String getDeparture() {
        return departure.get();
    }

    public SimpleStringProperty departureProperty() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure.set(departure);
    }

    public int getHours() {
        return hours.get();
    }

    public SimpleIntegerProperty hoursProperty() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours.set(hours);
    }

    public double getAmount() {
        return amount.get();
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public String getUserAdded() {
        return userAdded.get();
    }

    public SimpleStringProperty userAddedProperty() {
        return userAdded;
    }

    public void setUserAdded(String userAdded) {
        this.userAdded.set(userAdded);
    }

    public String getUserExit() {
        return userExit.get();
    }

    public SimpleStringProperty userExitProperty() {
        return userExit;
    }

    public void setUserExit(String userExit) {
        this.userExit.set(userExit);
    }
}
