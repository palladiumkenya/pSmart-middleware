package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
            lblMsg.setText("Login successful!");
            lblMsg.setStyle("-fx-background-color: #9C2827; -fx-border-width: 3px;");
        }
    }
}
