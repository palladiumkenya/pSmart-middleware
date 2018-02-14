package controller;

import card.ACOS3.ACOS3ReadWriteFx;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import psmart.ReaderBasicServices;
import psmart.SmartCardUtils;

import java.text.ParseException;
import java.util.List;

public class HomeController  {

    private String message=null;

    @FXML
    private Label lblpsmartTitle;

    @FXML
    private Button btnWriteToCard;

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
        btnWriteToCard.setDisable(false);
        btnReadCard.setDisable(false);

    }

    @FXML
    public void initialiseCardReader(ActionEvent event) {

        int index;

        try {
            ReaderBasicServices reader=new ReaderBasicServices();
            List<String> readerList=reader.InitialiseReader();
            cboDeviceReaderList.getItems().clear(); // clear the combobox control

            if (readerList.size()>0) {
                for(String readerName: readerList) {
                    cboDeviceReaderList.getItems().add(readerName);
                }
                cboDeviceReaderList.getSelectionModel().select(0);
                btnConnectReader.setDisable(false);
            }else {
                cboDeviceReaderList.getItems().add("No Reader Selected");
                cboDeviceReaderList.getSelectionModel().select(0);
            }
        }catch(Exception e){
            cboDeviceReaderList.getItems().add("No Reader Selected");
            cboDeviceReaderList.getSelectionModel().select(0);//.setValue("No Reader Selected");
            btnConnectReader.setDisable(true);
           SmartCardUtils.displayOut(txtProcessLogger, "Reader initialization error");
        }
    }

    @FXML
    public void connectReader(ActionEvent event){

        try{
            ReaderBasicServices reader = new ReaderBasicServices();

            reader.ConnectReader(cboDeviceReaderList,btnConnectReader, txtProcessLogger);

        }catch(ParseException e){
            SmartCardUtils.displayOut(txtProcessLogger, "Reader parse error. Cannot connect");

        } catch (Exception e) {
            SmartCardUtils.displayOut(txtProcessLogger, "An error occured during card initialization");
            e.printStackTrace();
        }
    }

    @FXML
    void sendDataToEmr(ActionEvent event) {

    }


    public void writeToCard(ActionEvent event) {
        SmartCardUtils.displayOut(txtProcessLogger, "\nWrite to card initiated. ");

        ACOS3ReadWriteFx readWrite = new ACOS3ReadWriteFx(
                btnInitialiseReader, btnConnectReader, btnConnectReader, btnWriteToCard, null, cboDeviceReaderList
        );
        readWrite.setTextToWrite("This is Felix");
        readWrite.writeCard();
    }
    /**
     * should ensure card reader is initialized and connected
     *
     */
    public void readCardContent(ActionEvent event) {

        initialiseCardReader(null);
        connectReader(null);

    }


}
