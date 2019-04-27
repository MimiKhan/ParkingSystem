package com.FYP.UI.VehicleEntry;

import com.FYP.Database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntryController implements Initializable {

    @FXML
    private Pane imageViewPane;

    @FXML
    private ImageView imageView;

    @FXML
    private JFXButton browseButton;

    @FXML
    private JFXTextField vehicleNoTF;

    @FXML
    private JFXTimePicker timePicker;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXButton closeButton;
    private Boolean is_paid = false;


    private String imageFile,usernameFromLogin;
    private File file;

    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = new DatabaseHandler();
        getTime();
        getDate();
    }

    public void getUser(String username) {

        this.usernameFromLogin = username;
        System.out.println(this.usernameFromLogin);
    }

    private void getDate() {
        LocalDate date = LocalDate.now();

        datePicker.setValue(date);
        datePicker.setDisable(true);
    }

    private void getTime() {
        LocalTime time = LocalTime.now();

        timePicker.setValue(time);
        timePicker.setDisable(true);
    }

    private int getUserId(String username){
        int id = 0;

        String query = "SELECT user_id FROM users WHERE username = '" + username + "';";

        ResultSet rs = databaseHandler.execQuery(query);

        try{
            while (rs.next()){
                id = rs.getInt(1);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return id;

    }

    // For browsing image
    @FXML
    void handleBrowse() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Select Image", "*.jpg", "*.png", "*.jpeg"));


        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                imageFile = file.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                System.out.println(e.getMessage());
            }
            Image image = new Image(imageFile);
            imageView.setSmooth(true);
            imageView.setPreserveRatio(true);
            imageView.setCache(true);
            imageView.setImage(image);
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File not found...");
            alert.show();
        }

        handleData();
    }
    // For getting Data
    private void handleData() {
        // getting data from API
        try {
            // Read image file to byte array
            Path path = Paths.get(file.getAbsolutePath());
            System.out.println(path.toString());

            byte[] data = Files.readAllBytes(path);

            // Encode file bytes to base64
            byte[] encoded = Base64.getEncoder().encode(data);

            String secret_key = "sk_2a11ed1b4d82ee14c8eba5cc";
            URL url = new URL("https://api.openalpr.com/v2/recognize_bytes?recognize_vehicle=1&country=us&secret_key=" + secret_key);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setFixedLengthStreamingMode(encoded.length);
            http.setDoOutput(true);

            // Send our Base64 content over the stream
            try(OutputStream os = http.getOutputStream()) {
                os.write(encoded);
            }

            int status_code = http.getResponseCode();
            if (status_code == 200)
            {
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        http.getInputStream()));
                StringBuilder json_content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    json_content.append(inputLine);
                in.close();
                System.out.println(json_content);

                String newString = json_content.substring(225,250);

                int startInd = 0, endInd = 0;

                Pattern pattern = Pattern.compile("[A-Z]{3}[0-9]{3}");
                Matcher matcher = pattern.matcher(newString);

                while(matcher.find()){
//                    System.out.println("Found match at: "  + matcher.start() + " to " + matcher.end());
                    startInd = matcher.start();
                    endInd = matcher.end();
                }
                String combinedString = newString.substring(startInd,endInd);
                System.out.println(combinedString);
                String final1 = newString.substring(startInd,startInd+3);
                String final2 = newString.substring(endInd-3,endInd);

                String finalno = final1 + "-" + final2;

                vehicleNoTF.setText(finalno);
            }
            else
            {
                System.out.println("Got non-200 response: " + status_code);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void handleClose() {
        closeButton.getScene().getWindow().hide();
    }

    private void clearFields(){
        vehicleNoTF.clear();
        timePicker.setValue(null);
        datePicker.setValue(null);

        getTime();
        getDate();

    }

    @FXML
    void handleSave() {

        int id = getUserId(usernameFromLogin);

        String vehicleNo = vehicleNoTF.getText();

        LocalDateTime localDateTime = LocalDateTime.of(datePicker.getValue(),timePicker.getValue());
        localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM:SS"));


        String query = "INSERT INTO vehicles(vehicle_no, arrival, added_by_user)\n" +
                "values('" + vehicleNo + "','" + localDateTime + "','" + id + "');";
        if (databaseHandler.execAction(query)) {
            TrayNotification tray = new TrayNotification();
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.setAnimationType(AnimationType.POPUP);
            tray.setTitle("vehicle entered Successfully");
            tray.setMessage("vehicle has been entered successfully.");
            tray.showAndDismiss(Duration.millis(2000));
            System.out.println("Success in insert");
            clearFields();
        }else {
            TrayNotification trayNotification = new TrayNotification();
            trayNotification.setNotificationType(NotificationType.ERROR);
            trayNotification.setTitle("Insertion Failed..!");
            trayNotification.setMessage("Entry has not been inserted. Try Again");
            trayNotification.showAndDismiss(Duration.millis(2000));
            System.out.println("Failed");
            clearFields();
        }


    }


}
