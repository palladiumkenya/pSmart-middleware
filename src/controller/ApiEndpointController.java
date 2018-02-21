package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.sun.prism.impl.Disposer;
import dbConnection.DBConnection;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.EndpointConfig;

import javax.print.DocFlavor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiEndpointController {

    @FXML
    private TableView<EndpointConfig> GridClientLastEncounter;

    @FXML
    private TableColumn<EndpointConfig, String> endpointType;

    @FXML
    private TableColumn<EndpointConfig, String> endpointName;

    @FXML
    private TableColumn<EndpointConfig, String> endpointUrl;

    @FXML
    private TableColumn<EndpointConfig, String> endpointAction;

    @FXML
    private TableColumn<EndpointConfig, String> endpointPurpose;


    @FXML
    private JFXComboBox cboEndpointType;

    @FXML
    private JFXTextField txtEndpointName;

    @FXML
    private JFXTextField txtEndpointUsername;

    @FXML
    private JFXComboBox cboEndpointAction;

    @FXML
    private JFXComboBox cboEndpointPurpose;

    @FXML
    private JFXPasswordField txtEndpointPassword;

    @FXML
    private JFXTextField txtEndpointUrl;

    @FXML
    void  initialize(){
        assert GridClientLastEncounter != null : "fx:id=\"GridClientLastENcounter1\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert endpointType != null : "fx:id=\"endpointType\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert endpointName != null : "fx:id=\"endpointName\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert endpointUrl != null : "fx:id=\"endpointUrl\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert endpointAction != null : "fx:id=\"endpointAction\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert endpointPurpose != null : "fx:id=\"enpointPurpose\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert cboEndpointType != null : "fx:id=\"cboEndpointType\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert txtEndpointName != null : "fx:id=\"txtEndpointName\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert txtEndpointUsername != null : "fx:id=\"txtEndpointUsername\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert cboEndpointAction != null : "fx:id=\"cboEndpointAction\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert cboEndpointPurpose != null : "fx:id=\"cboEndpointPurpose\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert txtEndpointPassword != null : "fx:id=\"txtEndpointPassword\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert txtEndpointUrl != null : "fx:id=\"txtEndpointUrl\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";


        cboEndpointType.getItems().clear();
        cboEndpointAction.getItems().clear();
        cboEndpointPurpose.getItems().clear();
        cboEndpointType.getItems().addAll(Arrays.asList("EMR", "Registry","Diwapi"));
        cboEndpointAction.getItems().addAll(Arrays.asList("Get", "Post","Put"));
        cboEndpointPurpose.getItems().addAll(Arrays.asList("Push to EMR", "Get from EMR ","Get from Registry"));

        endpointType.setCellValueFactory(new PropertyValueFactory<EndpointConfig,String>("endpointType"));
        endpointUrl.setCellValueFactory(new PropertyValueFactory<EndpointConfig,String>("endpointUrl"));
        endpointAction.setCellValueFactory(new PropertyValueFactory<EndpointConfig,String>("endpointAction"));
        endpointPurpose.setCellValueFactory(new PropertyValueFactory<EndpointConfig,String>("endpointPurpose"));
        /*endpointUsername.setCellValueFactory(new PropertyValueFactory<EndpointConfig,String>("endpointUsername"));
        endpointPassword.setCellValueFactory(new PropertyValueFactory<EndpointConfig,String>("endpointPassword"));*/

        viewEndpointConfiguration();
    }

    public String validateEndpointConfiguration(){
        int errorCount=0; List<String> errorMessage=new ArrayList<String>();
        String msg=null;

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

        if(cboEndpointAction.getItems().isEmpty()==true || cboEndpointAction.getSelectionModel().getSelectedIndex() == 0){
            errorCount+=1;errorMessage.add("Endpoint action is required!");
        }
        if(cboEndpointPurpose.getItems().isEmpty()==true || cboEndpointPurpose.getSelectionModel().getSelectedIndex() == 0){
            errorCount+=1;errorMessage.add("Endpoint Purpose is required!");
        }
        if (errorCount>0){

            for (String names:errorMessage){

                //alert.setContentText(names+ "\r\n");
                msg+=names+"\r\n";
            }
            return msg;
        }else{return msg;}
    }

    public void viewEndpointConfiguration(){
        ObservableList<EndpointConfig> endpoints;

        endpoints= FXCollections.observableArrayList();

        //Connection dbConn;
        try{
           Connection dbConn=DBConnection.connect();

            String SQL = "Select * from endpoints WHERE void=0 Order By endpointType";
            ResultSet rs= dbConn.createStatement().executeQuery(SQL);
           //= executeQuery(SQL);

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                EndpointConfig endpoint=new EndpointConfig();
                endpoint.endpointType.set(rs.getString("endpointType"));
                endpoint.endpointUrl.set(rs.getString("endpointUrl"));
                endpoint.endpointAction.set(rs.getString("endpointAction"));
                endpoint.endpointPurpose.set(rs.getString("endpointPurpose"));
               endpoint.endpointUsername.set(rs.getString("username"));
                endpoints.add(endpoint);
            }
            //FINALLY ADDED TO TableView
            GridClientLastEncounter.setItems(endpoints);

            DBConnection.closeConnection();
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("PSmart Exception Logger");
            alert.setHeaderText("Exception Handler");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    public void addNewEndpoint() {
        try {

            Connection dbConn=DBConnection.connect();

            if (cboEndpointType.getItems().isEmpty() == true) {

                cboEndpointType.setStyle("-fx-background-color: rgba(6,6,33,0.8); -fx-border-width: 3px;");
            }

            String sql="INSERT INTO `psmart`.`endpoints` (`endpointType`,`endpointUrl`,`endpointAction`,`endpointPurpose`,`username`,`password`,`user`)" +
                    "VALUES(" +
                    "'"+ cboEndpointType.getSelectionModel().getSelectedItem()+"','"+txtEndpointUrl.getText().toString()+"','"+cboEndpointAction.getSelectionModel().getSelectedItem()+"','"+cboEndpointPurpose.getSelectionModel().getSelectedItem()+"','"+
                        txtEndpointUsername.getText().toString() +"','"+txtEndpointPassword.getText().toString()+"','Admin')";

            int rs= dbConn.createStatement().executeUpdate(sql);
            if(rs>0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PSmart Exception Logger");
                alert.setHeaderText("Exception Handler");
                alert.setContentText("Endpoint saved successfully");
                alert.showAndWait();
            }
        }
        catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("PSmart Exception Logger");
            alert.setHeaderText("Exception Handler");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    public void closeWindow(JFXButton btn){
        Stage stage = (Stage) btn.getScene().getWindow();
        // do what you have to do
        stage.close();
    }



}


