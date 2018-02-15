package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import psmart.GenericPopupController;
import psmart.ReaderBasicServices;
import view.Main;

import java.net.URL;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private String message=null;

    @FXML
    private Menu mnuEndpointConfig;

    @FXML
    private Label lblpsmartTitle;

    @FXML
    private Label lblFacilityName;

    @FXML
    private TextArea txtProcessLogger;

    @FXML
    private TableColumn<?, ?> gridCardSummary;

    @FXML
    private TableColumn<?, ?> gridCardSummary1;

    @FXML
    private TableColumn<?, ?> gridCardSummary2;

    @FXML
    private TableColumn<?, ?> gridCardSummary21;

    @FXML
    private TableView<?> GridClientIdentifiers;

    @FXML
    private TableView<?> GridClientLastENcounter;

    @FXML
    private TableView<?> GridClientLastENcounter1;

    @FXML
    private Label lblCardStatus;

    @FXML
    private Button btnInitialiseReader;

    @FXML
    private Button btnConnectReader;

    @FXML
    private Button btnUpdateCard;

    @FXML
    private Button btnNewCard;

    @FXML
    private Button btnReadCard;

    @FXML
    private Button btnWriteToCard;

    @FXML
    private Label lblUserId;

    @FXML
    private Button btnPushToEMR;

    @FXML
    private JFXComboBox<String> cboDeviceReaderList;

    @FXML
    void initialize() {
        assert lblpsmartTitle != null : "fx:id=\"lblpsmartTitle\" was not injected: check your FXML file 'home.fxml'.";
        assert lblFacilityName != null : "fx:id=\"lblFacilityName\" was not injected: check your FXML file 'home.fxml'.";
        assert gridCardSummary != null : "fx:id=\"gridCardSummary\" was not injected: check your FXML file 'home.fxml'.";
        assert gridCardSummary1 != null : "fx:id=\"gridCardSummary1\" was not injected: check your FXML file 'home.fxml'.";
        assert gridCardSummary2 != null : "fx:id=\"gridCardSummary2\" was not injected: check your FXML file 'home.fxml'.";
        assert gridCardSummary21 != null : "fx:id=\"gridCardSummary21\" was not injected: check your FXML file 'home.fxml'.";
        assert GridClientIdentifiers != null : "fx:id=\"GridClientIdentifiers\" was not injected: check your FXML file 'home.fxml'.";
        assert GridClientLastENcounter != null : "fx:id=\"GridClientLastENcounter\" was not injected: check your FXML file 'home.fxml'.";
        assert GridClientLastENcounter1 != null : "fx:id=\"GridClientLastENcounter1\" was not injected: check your FXML file 'home.fxml'.";
        assert cboDeviceReaderList != null : "fx:id=\"cboDeviceReaderList\" was not injected: check your FXML file 'home.fxml'.";
        assert btnInitialiseReader != null : "fx:id=\"btnInitialiseReader\" was not injected: check your FXML file 'home.fxml'.";
        assert btnConnectReader != null : "fx:id=\"btnConnectReader\" was not injected: check your FXML file 'home.fxml'.";
        assert btnUpdateCard != null : "fx:id=\"btnUpdateCard\" was not injected: check your FXML file 'home.fxml'.";
        assert btnNewCard != null : "fx:id=\"btnNewCard\" was not injected: check your FXML file 'home.fxml'.";
        assert btnReadCard != null : "fx:id=\"btnReadCard\" was not injected: check your FXML file 'home.fxml'.";
        assert txtProcessLogger != null : "fx:id=\"txtProcessLogger\" was not injected: check your FXML file 'home.fxml'.";
        assert lblUserId != null : "fx:id=\"lblUserId\" was not injected: check your FXML file 'home.fxml'.";
        assert btnPushToEMR != null : "fx:id=\"btnPushToEMR\" was not injected: check your FXML file 'home.fxml'.";

    }

    @FXML
    public void InitialiseCardReader(ActionEvent event) {

        int index;

        try {
            ReaderBasicServices reader=new ReaderBasicServices();
            String[] readerList=reader.InitialiseReader();
            cboDeviceReaderList.getItems().clear(); // clear the combobox control

            if (readerList.length>0)
            {
                for(index = 0; index <readerList.length; index++)
                {
                    if(!readerList.equals("0"))
                        cboDeviceReaderList.getItems().addAll(readerList[index]);
                    else
                        break;
                }
                cboDeviceReaderList.getSelectionModel().select(0);
                btnConnectReader.setDisable(false);
            }else {
                cboDeviceReaderList.getItems().add("No Reader Selected");
            }
        }catch(Exception e){
           // txtProcessLogger.appendText(message);
            cboDeviceReaderList.getItems().add("No Reader Selected");
            cboDeviceReaderList.getSelectionModel().select(0);//.setValue("No Reader Selected");
            btnConnectReader.setDisable(true);
           // lblCardStatus.setText("reader Initialization Error");
        }
    }

    @FXML
    public void ConnectReader(ActionEvent event){

        try{
            ReaderBasicServices reader = new ReaderBasicServices();

            reader.ConnectReader(cboDeviceReaderList,btnConnectReader);

        }catch(ParseException e){

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void LoadEndpointConfig(ActionEvent event) {

        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(
                    Main.class.getResource("ApiEndpoints.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("EndPoint Configuration");
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
           // stage.initOwner(
             //       ((Node)event.getSource()).getScene().getWindow() );
            stage.show();

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("P-Smart Middleware");
            alert.setContentText(e.getMessage());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chose OK e.

            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }


    @FXML
    void sendDataToEmr(ActionEvent event) {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
