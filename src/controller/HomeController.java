package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jsonvalidator.apiclient.APIClient;
import jsonvalidator.mapper.SHR;
import jsonvalidator.utils.SHRUtils;
import models.CardDetail;
import models.HIVTest;
import models.Identifier;
import jsonvalidator.utils.SHRUtils;
import pSmart.MainSmartCardReadWrite;
import pSmart.SmartCardUtils;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import view.Main;

import javax.swing.*;
import java.text.ParseException;
import java.util.Optional;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class HomeController  {

    private static final String SHRURL = "https://my-json-server.typicode.com/tedb19/SHR/SHR";

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
    private TableColumn<CardDetail, String> colCardStatus;

    @FXML
    private TableColumn<CardDetail, String> colReason;

    @FXML
    private TableColumn<CardDetail, String> colLastUpdate;

    @FXML
    private TableColumn<CardDetail, String> colFacilityLastUpdated;

    @FXML
    private TableView<CardDetail> GridCardSummary;

    @FXML
    private TableView<HIVTest> GridClientLastENcounter;

    @FXML
    private TableColumn<HIVTest, String> colTestDate;

    @FXML
    private TableColumn<HIVTest, String> colResult;

    @FXML
    private TableColumn<HIVTest, String> colType;

    @FXML
    private TableColumn<HIVTest, String> colFacility;

    @FXML
    private TableColumn<HIVTest, String> colStrategy;

    @FXML
    private TableView<Identifier> GridClientIdentifiers;

    @FXML
    private TableColumn<Identifier, String> colIdentifierId;

    @FXML
    private TableColumn<Identifier, String> colIdentifierType;

    @FXML
    private TableColumn<Identifier, String> colAssigningAuthority;

    @FXML
    private TableColumn<Identifier, String> colAssigningFacility;

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
    private Button btnFormatCard;

    @FXML
    private Button btnReadCard;

    @FXML
    private Label lblUserId;

    @FXML
    private Button btnPushToEMR;

    @FXML
    private JFXComboBox<String> cboDeviceReaderList;

    //Load Card Details
    private final void loadCardDetails(SHR shr){
        colCardStatus.setCellValueFactory(new PropertyValueFactory<CardDetail, String>("status"));
        colFacilityLastUpdated.setCellValueFactory(new PropertyValueFactory<CardDetail, String>("lastUpdatedFacility"));
        colLastUpdate.setCellValueFactory(new PropertyValueFactory<CardDetail, String>("lastUpdated"));
        colReason.setCellValueFactory(new PropertyValueFactory<CardDetail, String>("reason"));
        GridCardSummary.setItems(FXCollections.observableArrayList(getCardDetails(shr)));
    }

    private final ObservableList<CardDetail> getCardDetails(SHR shr){
        CardDetail cardDetail = new CardDetail(
                shr.cARD_DETAILS.sTATUS,
                shr.cARD_DETAILS.rEASON,
                shr.cARD_DETAILS.lAST_UPDATED,
                shr.cARD_DETAILS.lAST_UPDATED_FACILITY
        );
        ObservableList<CardDetail> cardsDetails = FXCollections.observableArrayList(cardDetail);
        return cardsDetails;
    }

    //Load Identifiers
    private final void loadIdentifiers(SHR shr){
        colIdentifierId.setCellValueFactory(new PropertyValueFactory<Identifier, String>("identifier"));
        colIdentifierType.setCellValueFactory(new PropertyValueFactory<Identifier, String>("identifierType"));
        colAssigningAuthority.setCellValueFactory(new PropertyValueFactory<Identifier, String>("assigningAuthority"));
        colAssigningFacility.setCellValueFactory(new PropertyValueFactory<Identifier, String>("assigningFacility"));
        GridClientIdentifiers.setItems(FXCollections.observableArrayList(getIdentifiers(shr)));
    }

    private final ObservableList<Identifier> getIdentifiers(SHR shr){
        ObservableList<Identifier> identifiers = FXCollections.observableArrayList();
        for(int i=0; i < shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID.length;i++ ){
            Identifier identifier = new Identifier(
                    shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].iD,
                    shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].iDENTIFIER_TYPE,
                    shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].aSSIGNING_AUTHORITY,
                    shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].aSSIGNING_FACILITY
            );
            identifiers.add(identifier);
        }
        return identifiers;
    }

    //Load Identifiers
    private final void loadHIVTests(SHR shr){
        colTestDate.setCellValueFactory(new PropertyValueFactory<HIVTest, String>("testDate"));
        colResult.setCellValueFactory(new PropertyValueFactory<HIVTest, String>("result"));
        colFacility.setCellValueFactory(new PropertyValueFactory<HIVTest, String>("facility"));
        colStrategy.setCellValueFactory(new PropertyValueFactory<HIVTest, String>("strategy"));
        colType.setCellValueFactory(new PropertyValueFactory<HIVTest, String>("type"));
        GridClientLastENcounter.setItems(FXCollections.observableArrayList(getHIVTests(shr)));
    }

    private final ObservableList<HIVTest> getHIVTests(SHR shr){
        ObservableList<HIVTest> hivTests = FXCollections.observableArrayList();
        for(int i=0; i < shr.hIV_TEST.length;i++ ){
            HIVTest hivTest = new HIVTest(
                    shr.hIV_TEST[i].dATE,
                    shr.hIV_TEST[i].rESULT,
                    shr.hIV_TEST[i].tYPE,
                    shr.hIV_TEST[i].fACILITY,
                    shr.hIV_TEST[i].sTRATEGY
            );
            hivTests.add(hivTest);
        }
        return hivTests;
    }

    @FXML
    void initialize() {
        btnWriteToCard.setDisable(true);
        btnReadCard.setDisable(true);
        readerWriter = new MainSmartCardReadWrite(txtProcessLogger, cboDeviceReaderList);
        btnConnectReader.setDisable(true);
    }

    @FXML
    public void initialiseCardReader(ActionEvent event) {

        try {
            MainSmartCardReadWrite reader=new MainSmartCardReadWrite(txtProcessLogger, cboDeviceReaderList);
            readerWriter.initializeReader(btnConnectReader);
            btnConnectReader.setDisable(false);

        }catch(Exception e){
            btnConnectReader.setDisable(true);
           SmartCardUtils.displayOut(txtProcessLogger, "Reader initialization error");
        }
    }

    @FXML
    public void connectReader(ActionEvent event){

            readerWriter.connectReader(btnConnectReader);
            btnReadCard.setDisable(false);
            btnFormatCard.setDisable(false);

    }

    @FXML
    void sendDataToEmr(ActionEvent event) {

    }


    public void writeToCard(ActionEvent event) throws ParseException {
        SmartCardUtils.displayOut(txtProcessLogger, "\nWrite to card initiated. ");

        MainSmartCardReadWrite writer = new MainSmartCardReadWrite(txtProcessLogger, cboDeviceReaderList);
        //writer.writeCard(SmartCardUtils.getUserFile(SmartCardUtils.PATIENT_CARD_DETAILS_USER_FILE_NAME), "This is test");
    }

    public void LoadEndpointConfig(ActionEvent event) throws ParseException{

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

    /**
     * should ensure card reader is initialized and connected
     *
     */
    public void readCardContent(ActionEvent event) throws ParseException {

        readerWriter.readCard(SmartCardUtils.getUserFile(SmartCardUtils.PATIENT_DEMOGRAPHICS_USER_FILE_NAME));
        readerWriter.readCard(SmartCardUtils.getUserFile(SmartCardUtils.PATIENT_IDENTIFIER_USER_FILE_NAME));

    }


    public void formatCard(ActionEvent event) {
        readerWriter.formatCard();
        btnUpdateCard.setDisable(false);
        btnNewCard.setDisable(false);
    }

    public void updateCard(ActionEvent event) {

        String patientDemographics = SHRUtils.getPatientDemographicsSampleData();
        String patientIdentifiers = SHRUtils.getPatientIdentifiersSampleData();
        String htsData = SHRUtils.getHivTestSampleData();
        String cardDetails = SHRUtils.getCardDetails();

        readerWriter.writeBinaryDataToCard(SHRUtils.getSlicedSHR());
        //readerWriter.writeCard(SmartCardUtils.getUserFile(SmartCardUtils.PATIENT_DEMOGRAPHICS_USER_FILE_NAME), patientDemographics);
        //readerWriter.writeCard(SmartCardUtils.getUserFile(SmartCardUtils.PATIENT_IDENTIFIER_USER_FILE_NAME), patientIdentifiers);
        //readerWriter.writeCard(SmartCardUtils.getUserFile(SmartCardUtils.PATIENT_CARD_DETAILS_USER_FILE_NAME), cardDetails);
    }
    public void getFromEMR(ActionEvent actionEvent){
        String SHRStr = APIClient.getSHRStr(SHRURL, "");
        SHR shr = SHRUtils.getSHR(SHRStr);
        loadCardDetails(shr);
        loadIdentifiers(shr);
        loadHIVTests(shr);
    }
}
