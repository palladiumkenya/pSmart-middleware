package controller;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.*;
import java.util.logging.ErrorManager;

import com.sun.org.apache.xerces.internal.xs.StringList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

public class EndpointConfigurationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> GridClientLastENcounter1;

    @FXML
    private JFXComboBox<String> cboEndpointType;

    @FXML
    private JFXTextField txtEndpointName;

    @FXML
    private JFXTextField txtEndpointUrl;

    @FXML
    private JFXTextField txtEndpointUsername;

    @FXML
    private JFXComboBox<String> cboEndpointAction;

    @FXML
    private JFXComboBox<String> cboEndpointPurpose;

    @FXML
    private JFXPasswordField txtEndpointPassword;

    @FXML
    void initialize() {
        assert GridClientLastENcounter1 != null : "fx:id=\"GridClientLastENcounter1\" was not injected: check your FXML file 'endpointconfig.fxml'.";
        assert cboEndpointType != null : "fx:id=\"cboEndpointType\" was not injected: check your FXML file 'endpointconfig.fxml'.";
        assert txtEndpointName != null : "fx:id=\"txtEndpointName\" was not injected: check your FXML file 'endpointconfig.fxml'.";
        assert txtEndpointUrl != null : "fx:id=\"txtEndpointUrl\" was not injected: check your FXML file 'endpointconfig.fxml'.";
        assert txtEndpointUsername != null : "fx:id=\"txtEndpointUsername\" was not injected: check your FXML file 'endpointconfig.fxml'.";
        assert cboEndpointAction != null : "fx:id=\"cboEndpointAction\" was not injected: check your FXML file 'endpointconfig.fxml'.";
        assert cboEndpointPurpose != null : "fx:id=\"cboEndpointPurpose\" was not injected: check your FXML file 'endpointconfig.fxml'.";
        assert txtEndpointPassword != null : "fx:id=\"txtEndpointPassword\" was not injected: check your FXML file 'endpointconfig.fxml'.";

        cboEndpointType.getItems().clear();
        cboEndpointType.getItems().addAll("EMR Endpoint","Registry Endpoint","DWapi Endpoint"
        );

        cboEndpointAction.getItems().clear();
        cboEndpointAction.getItems().addAll("Get","Post");

        cboEndpointPurpose.getItems().clear();
        cboEndpointPurpose.getItems().addAll("");

    }

    public  EndpointConfigurationController(ActionEvent event){

        this.validateEndpointConfiguration();


    }

    public void validateEndpointConfiguration(){
        int errorCount=0; List<String> errorMessage=new ArrayList<String>();

        if (cboEndpointType.getItems().isEmpty() == true || cboEndpointType.getSelectionModel().getSelectedIndex() == 0 ) {
            errorCount+=1; errorMessage.add("Please select endpoint Type");
           // lblMsg.setText("Invalid login details! Try again");
            //lblMsg.setStyle("-fx-background-color: rgba(6,6,33,0.8); -fx-border-width: 3px;");
        }
        if(txtEndpointName.getText().isEmpty()==true ){
            errorCount+=1;
            errorMessage.add("Endpoint name is required!");
        }
        if(txtEndpointUrl.getText().isEmpty()==true){
            errorCount+=1;errorMessage.add("Endpoint Url is required");
        }

        if(cboEndpointAction.getItems().isEmpty()==true){
            errorCount+=1;errorMessage.add("Endpoint action is required!");
        }
        if(cboEndpointPurpose.getItems().isEmpty()==true){
            errorCount+=1;errorMessage.add("Endpoint Purpose is requried!");
        }
        if (errorCount>0){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Endpoint Configuration Error");
            alert.setHeaderText("P-Smart Middleware");
            for (String names:errorMessage){

                alert.setContentText(names+ "\r\n");
            }
            return;
        }
    }
}





