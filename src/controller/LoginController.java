package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import view.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {
    @FXML
    private JFXPasswordField password;

    @FXML
    private Label lblMsg;

    @FXML
    private JFXTextField username;



    public void validateLogin(ActionEvent actionEvent) {


        if (username.getText().isEmpty() == true || password.getText().isEmpty() == true ) {
            lblMsg.setText("Invalid login details! Try again");
            lblMsg.setStyle("-fx-background-color: rgba(6,6,33,0.8); -fx-border-width: 3px;");

        } else {
            goToLandingPage(null);
        }
    }

    private void goToLandingPage (ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(Main.class.getResource("home.fxml"));
            Stage landingPageStage = new Stage();
            landingPageStage.setTitle("P-SMART MIDDLEWARE");
            Scene landingPageScene = new Scene(root);
            landingPageStage.setScene(landingPageScene);
            landingPageStage.setMaximized(true);
            landingPageStage.setResizable(false);
            Stage dashboardStage = (Stage) username.getScene().getWindow();
            dashboardStage.close();
            landingPageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
