package com.FYP.UI.Users;

import javafx.beans.property.SimpleStringProperty;

public class User {

    private SimpleStringProperty username;
    private SimpleStringProperty password;


    public User(String username, String password) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }

    String getUsername() {
        return username.get();
    }

    SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
}
