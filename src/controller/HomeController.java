package controller;

import CardProgramming.MainSmartCardReadWrite;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import psmart.ReaderBasicServices;
import psmart.SmartCardReadWrite;
import psmart.SmartCardUtils;

import java.text.ParseException;
import java.util.List;

public class HomeController  {

    private String message=null;
    MainSmartCardReadWrite readerWriter;

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
        btnWriteToCard.setDisable(false);
        btnReadCard.setDisable(false);
        readerWriter = new MainSmartCardReadWrite(txtProcessLogger, cboDeviceReaderList);
    }

    @FXML
    public void initialiseCardReader(ActionEvent event) {

        try {
            MainSmartCardReadWrite reader=new MainSmartCardReadWrite(txtProcessLogger, cboDeviceReaderList);
            readerWriter.initializeReader(btnConnectReader);

        }catch(Exception e){
            btnConnectReader.setDisable(true);
           SmartCardUtils.displayOut(txtProcessLogger, "Reader initialization error");
        }
    }

    @FXML
    public void connectReader(ActionEvent event){

            readerWriter.connectReader(btnConnectReader);
    }

    @FXML
    void sendDataToEmr(ActionEvent event) {

    }


    public void writeToCard(ActionEvent event) throws ParseException {
        SmartCardUtils.displayOut(txtProcessLogger, "\nWrite to card initiated. ");

        SmartCardReadWrite writer = new SmartCardReadWrite(txtProcessLogger, cboDeviceReaderList);
        writer.writeCard(SmartCardUtils.getUserFile(SmartCardUtils.PATIENT_CARD_DETAILS_USER_FILE_NAME), "This is test");
    }
    /**
     * should ensure card reader is initialized and connected
     *
     */
    public void readCardContent(ActionEvent event) throws ParseException {

        try{
            SmartCardReadWrite reader = new SmartCardReadWrite(txtProcessLogger, cboDeviceReaderList);

            reader.readCard(SmartCardUtils.getUserFile(SmartCardUtils.PATIENT_CARD_DETAILS_USER_FILE_NAME));

        }catch(Exception e){
            SmartCardUtils.displayOut(txtProcessLogger, "Reader parse error. Cannot connect");
            e.printStackTrace();

        }

    }


}
