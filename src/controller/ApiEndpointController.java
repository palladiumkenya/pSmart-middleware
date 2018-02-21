package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class ApiEndpointController {

    @FXML
    private TableView<?> GridClientLastENcounter1;

    @FXML
    private JFXComboBox<?> cboEndpointType;

    @FXML
    private JFXTextField txtEndpointName;

    @FXML
    private JFXTextField txtEndpointUsername;

    @FXML
    private JFXComboBox<?> cboEndpointAction;

    @FXML
    private JFXComboBox<?> cboEndpointPurpose;

    @FXML
    private JFXPasswordField txtEndpointPassword;

    @FXML
    private JFXTextField txtEndpointUrl;

}


