package controller;


import dbConnection.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jsonvalidator.apiclient.APIClient;
import jsonvalidator.mapper.CardAssignment;
import jsonvalidator.mapper.EligibleList;
import jsonvalidator.mapper.EncryptedSHR;
import jsonvalidator.mapper.SHR;
import jsonvalidator.utils.SHRUtils;
import models.CardDetail;
import models.HIVTest;
import models.Identifier;
import models.*;
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
import pSmart.userFiles.UserFile;
import view.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class HomeController  {

    private String message=null;
    MainSmartCardReadWrite readerWriter;

    @FXML
    private Label lblpsmartTitle;

    @FXML
    private Label lblCurrentPatient;

    @FXML
    private TabPane tpMainTabPane;

    @FXML
    private Button btnWriteToCard;

    @FXML
    private Label lblFacilityName;

    @FXML
    private Label lblSex;

    @FXML
    private Label lblAge;

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
    private TableView<EligiblePerson> GridEligibleList;

    @FXML
    private TableColumn<EligiblePerson, String> colPatientId;

    @FXML
    private TableColumn<EligiblePerson, String> colFirstName;

    @FXML
    private TableColumn<EligiblePerson, String> colMiddleName;

    @FXML
    private TableColumn<EligiblePerson, String> colLastName;

    @FXML
    private TableColumn<EligiblePerson, String> colGender;

    @FXML
    private TableColumn<EligiblePerson, String> colAge;

    @FXML
    private Label lblCardStatus;

    @FXML
    private Button btnInitialiseReader;

    @FXML
    private Button btnConnectReader;

    @FXML
    private Button btnLoadFromEMR;

    @FXML
    private Button btnReadCard;

    @FXML
    private Button btnLoadEligibleList;

    @FXML
    private Label lblUserId;

    @FXML
    private Button btnPushToEMR;

    @FXML
    private JFXComboBox<String> cboDeviceReaderList;

    private SHR shr;

    private EncryptedSHR encryptedSHR;

    //Load Card Details
    private final void loadCardDetails(){
        colCardStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colFacilityLastUpdated.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedFacility"));
        colLastUpdate.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        colReason.setCellValueFactory(new PropertyValueFactory<CardDetail, String>("reason"));
        GridCardSummary.setItems(FXCollections.observableArrayList(getCardDetails()));
    }

    private final ObservableList<CardDetail> getCardDetails(){
        CardDetail cardDetail = new CardDetail(
                shr.cARD_DETAILS.sTATUS,
                shr.cARD_DETAILS.rEASON,
                shr.cARD_DETAILS.lAST_UPDATED,
                shr.cARD_DETAILS.lAST_UPDATED_FACILITY
        );
        ObservableList<CardDetail> cardsDetails = FXCollections.observableArrayList(cardDetail);
        return cardsDetails;
    }

    private static final int getMonthsDifference(Date date1, Date date2) {
        int m1 = date1.getYear() * 12 + date1.getMonth();
        int m2 = date2.getYear() * 12 + date2.getMonth();
        return m2 - m1 + 1;
    }

    private final void showCurrentClient() {
        String patientName = shr.pATIENT_IDENTIFICATION.pATIENT_NAME.fIRST_NAME + " " + shr.pATIENT_IDENTIFICATION.pATIENT_NAME.mIDDLE_NAME + " " + shr.pATIENT_IDENTIFICATION.pATIENT_NAME.lAST_NAME;
        String patientDob = shr.pATIENT_IDENTIFICATION.dATE_OF_BIRTH;
        String patientSex = shr.pATIENT_IDENTIFICATION.sEX;
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        if(!patientDob.equals("") && !patientDob.isEmpty()) {
            try {
                Date dob = formatter.parse(patientDob);
                Date now = new Date();
                long timeBetween = now.getTime() - dob.getTime();
                double yearsBetween = timeBetween / 3.156e+10;
                if(yearsBetween < 1) {
                    int months = getMonthsDifference(dob, now);
                    lblAge.setVisible(true);
                    lblAge.setText("Age: " + Integer.toString(months) + " months old");
                } else {
                    int age = (int) Math.floor(yearsBetween);
                    lblAge.setVisible(true);
                    lblAge.setText("Age: " + Integer.toString(age) + " years old");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        lblCurrentPatient.setText(patientName);
        lblSex.setVisible(true);
        lblSex.setText("Sex: " + patientSex);
    }
    //Load Identifiers
    private final void loadIdentifiers(){
        colIdentifierId.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        colIdentifierType.setCellValueFactory(new PropertyValueFactory<>("identifierType"));
        colAssigningAuthority.setCellValueFactory(new PropertyValueFactory<>("assigningAuthority"));
        colAssigningFacility.setCellValueFactory(new PropertyValueFactory<>("assigningFacility"));
        GridClientIdentifiers.setItems(FXCollections.observableArrayList(getIdentifiers()));
    }

    //Load Identifiers
    private final void loadElligibleList(){
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        GridEligibleList.setItems(FXCollections.observableArrayList(getEligibilityList()));

		GridEligibleList.setRowFactory( tv -> {
			TableRow<EligiblePerson> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					EligiblePerson rowData = row.getItem();
					System.out.println(rowData.getFirstName());

					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("Card Assignment");
					alert.setContentText("Are you sure you want to assign the current card to "+ rowData + "?");

					ButtonType buttonTypeYes = new ButtonType("Yes");
					ButtonType buttonTypeNo = new ButtonType("No");

					alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == buttonTypeYes){
					    String url = getURL("HTTP POST - Push the card assignment details to EMR");
					    CardAssignment cardAssignment = new CardAssignment(rowData.getPatientId(), "123456-hjkuyi-lo9087");
					    String cardAssignmentStr = SHRUtils.getJSON(cardAssignment);
                        String response = APIClient.postData(url, cardAssignmentStr);
                        shr = SHRUtils.getSHRObj(response);
                        loadCardDetails();
						loadIdentifiers();
						loadHIVTests();
                        showCurrentClient();
						btnWriteToCard.setDisable(false);
						tpMainTabPane.getSelectionModel().select(0);
                        SmartCardUtils.displayOut(txtProcessLogger, "\n"+ response +"\n");
					} else if (result.get() == buttonTypeNo) {

					}
				}
			});
			return row ;
		});
    }

    //private displayAlert

    private final ObservableList<Identifier> getIdentifiers(){
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

    private final ObservableList<EligiblePerson> getEligibilityList(){
        ObservableList<EligiblePerson> eligiblePersons = FXCollections.observableArrayList();
        String eligibleListUrl = getURL("HTTP GET - Fetch eligible list from EMR");
        String eligibleListStr = APIClient.fetchData(eligibleListUrl);
        List<EligibleList> eligibleList = SHRUtils.getEligibleList(eligibleListStr);
        for(int i=0; i < eligibleList.size(); i++) {
            EligiblePerson eligiblePerson = new EligiblePerson(
                    eligibleList.get(i).getPatientId(),
                    eligibleList.get(i).getFirstName(),
                    eligibleList.get(i).getMiddleName(),
                    eligibleList.get(i).getLastName(),
                    eligibleList.get(i).getGender(),
                    eligibleList.get(i).getAge()
            );
            eligiblePersons.add(eligiblePerson);
        }
        return eligiblePersons;
    }

    //Load Identifiers
    private final void loadHIVTests(){
        colTestDate.setCellValueFactory(new PropertyValueFactory<HIVTest, String>("testDate"));
        colResult.setCellValueFactory(new PropertyValueFactory<HIVTest, String>("result"));
        colFacility.setCellValueFactory(new PropertyValueFactory<HIVTest, String>("facility"));
        colStrategy.setCellValueFactory(new PropertyValueFactory<HIVTest, String>("strategy"));
        colType.setCellValueFactory(new PropertyValueFactory<HIVTest, String>("type"));
        GridClientLastENcounter.setItems(FXCollections.observableArrayList(getHIVTests()));
    }

    private final ObservableList<HIVTest> getHIVTests(){
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
        btnLoadEligibleList.setDisable(true);
        lblSex.setVisible(false);
        lblAge.setVisible(false);
    }

    @FXML
    public void initialiseCardReader(ActionEvent event) {
        try {
            MainSmartCardReadWrite reader=new MainSmartCardReadWrite(txtProcessLogger, cboDeviceReaderList);
            readerWriter.initializeReader(btnConnectReader);
            btnConnectReader.setDisable(false);

        } catch(Exception e){
            btnConnectReader.setDisable(true);
           SmartCardUtils.displayOut(txtProcessLogger, "Reader initialization error");
        }
    }

    @FXML
    public void connectReader(ActionEvent event){
            readerWriter.connectReader(btnConnectReader);
            btnReadCard.setDisable(false);
            btnLoadEligibleList.setDisable(false);
            btnLoadFromEMR.setDisable(false);
    }

    private String getURL(String purpose) {
        String url = "";
        for (Endpoint endpoint : getEndPoints()) {
            if(endpoint.getEndpointPurpose().equals(purpose)){
                if(!(endpoint.getEndpointUrl().isEmpty() || endpoint.getEndpointUrl() == null)) {
                    url = endpoint.getEndpointUrl();
                }
            }
        }
        return url;
    }

    @FXML
    void sendDataToEmr(ActionEvent event) {
        String purpose = "HTTP POST - Push SHR to EMR";
        String url = getURL(purpose);
        if(!url.isEmpty()){
            String shrStr = SHRUtils.getJSON(shr);
            System.out.println(shrStr);
            String response = APIClient.postData(url, shrStr);
            SmartCardUtils.displayOut(txtProcessLogger, "\nResponse from EMR\n "+response);
            btnLoadFromEMR.setDisable(true);
        } else {
            SmartCardUtils.displayOut(txtProcessLogger, "\nPlease specify the `"+purpose+"` endpoint url!");
        }
    }

    @FXML
    void getEligibleList(ActionEvent actionEvent) {
        tpMainTabPane.getSelectionModel().select(3);
        loadElligibleList();
        btnLoadEligibleList.setDisable(true);
        btnReadCard.setDisable(true);
        btnPushToEMR.setDisable(true);
    }

    public List<Endpoint> getEndPoints() {
        Endpoint endpoint;
        List<Endpoint> endpoints = new ArrayList<Endpoint>();
        try {
            Connection dbConn = DBConnection.connect();
            String sql = "Select * from endpoints WHERE void=0";
            ResultSet rs = dbConn.createStatement().executeQuery(sql);
            while(rs.next()){
                endpoint = new Endpoint();
                endpoint.setEndpointPurpose(rs.getString("endpointPurpose"));
                endpoint.setEndpointUrl(rs.getString("endpointUrl"));
                endpoint.setEndpointUsername(rs.getString("username"));
                endpoint.setEndpointPassword(rs.getString("password"));
                endpoints.add(endpoint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return endpoints;
    }


    public List<String> getStringArr(SHR shr, String context){
        List<String> stringArr = new ArrayList<>();
        switch (context) {
            case "INTERNAL_PATIENT_ID":
                for(int i = 0; i < shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID.length ; i++){
                    stringArr.add(SHRUtils.getJSON(shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i]));
                }
                break;

            case "HIV_TEST":
                for(int i = 0; i < shr.hIV_TEST.length; i++){
                    stringArr.add(SHRUtils.getJSON(shr.hIV_TEST[i]));
                }
                break;
            case "IMMUNIZATION":
                for(int i = 0; i < shr.iMMUNIZATION.length; i++){
                    stringArr.add(SHRUtils.getJSON(shr.iMMUNIZATION[i]));
                }
                break;
            case "MOTHER_IDENTIFIER":
                for(int i = 0; i < shr.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_IDENTIFIER.length; i++){
                    stringArr.add(SHRUtils.getJSON(shr.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_IDENTIFIER[i]));
                }
                break;
            default:
                break;
        }
        return stringArr;
    }

    public void writeToCard(ActionEvent event) throws ParseException {
        SmartCardUtils.displayOut(txtProcessLogger, "\nWrite to card initiated. ");
        readerWriter.formatCard();
        List<String> demographics = new ArrayList<>();

        StringBuilder otherDemographics = new StringBuilder();
        otherDemographics
                .append("\"DATE_OF_BIRTH\": \"").append(shr.pATIENT_IDENTIFICATION.dATE_OF_BIRTH).append("\"")
                .append(", \"DATE_OF_BIRTH_PRECISION\": \"").append(shr.pATIENT_IDENTIFICATION.dATE_OF_BIRTH_PRECISION).append("\"")
                .append(", \"SEX\": \"").append(shr.pATIENT_IDENTIFICATION.sEX).append("\"")
                .append(", \"DEATH_DATE\": \"").append(shr.pATIENT_IDENTIFICATION.dEATH_DATE).append("\"")
                .append(", \"DEATH_INDICATOR\": \"").append(shr.pATIENT_IDENTIFICATION.dEATH_INDICATOR).append("\"")
                .append(", \"PHONE_NUMBER\": \"").append(shr.pATIENT_IDENTIFICATION.pHONE_NUMBER).append("\"")
                .append(", \"MARITAL_STATUS\": \"").append(shr.pATIENT_IDENTIFICATION.mARITAL_STATUS).append("\"");
        String patientName = SHRUtils.getJSON(shr.pATIENT_IDENTIFICATION.pATIENT_NAME);
        demographics.add(patientName);
        demographics.add(otherDemographics.toString());
        String patientExternalIdentifiers = SHRUtils.getJSON(shr.pATIENT_IDENTIFICATION.eXTERNAL_PATIENT_ID);
        String cardDetails = SHRUtils.getJSON(shr.cARD_DETAILS);
        String motherDetails = SHRUtils.getJSON(shr.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_NAME);
        List<String> immunizationDetails = getStringArr(shr, "IMMUNIZATION");
        List<String> hivTests = getStringArr(shr, "HIV_TEST");
        List<String> internalIdentifiers = getStringArr(shr, "INTERNAL_PATIENT_ID");
        List<String> motherIdentifiers = getStringArr(shr, "MOTHER_IDENTIFIER");
        String addressDetails=SHRUtils.getJSON(shr.pATIENT_IDENTIFICATION.pATIENT_ADDRESS);

        //write arrays
        readerWriter.writeArray(immunizationDetails, SmartCardUtils.getUserFile(SmartCardUtils.IMMUNIZATION_USER_FILE_NAME));
        readerWriter.writeArray(hivTests, SmartCardUtils.getUserFile(SmartCardUtils.HIV_TEST_USER_FILE_NAME));
        readerWriter.writeArray(internalIdentifiers, SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_INTERNAL_NAME));
        readerWriter.writeArray(demographics, SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_DEMOGRAPHICS_NAME));
        readerWriter.writeArray(motherIdentifiers, SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_IDENTIFIER_NAME));

        readerWriter.writeCard(SmartCardUtils.getUserFile(SmartCardUtils.CARD_DETAILS_USER_FILE_NAME), cardDetails, (byte)0x00 );
        readerWriter.writeCard(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_EXTERNAL_NAME), patientExternalIdentifiers, (byte)0x00);
        readerWriter.writeCard(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_ADDRESS_NAME), addressDetails, (byte)0x00);
        readerWriter.writeCard(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_DETAIL_NAME), motherDetails, (byte)0x00);
        //TODO: encrypt and compress SHR and send to EMR
        //byte[] compressedMessage = Compression.Compress(encryptedSHR);

        //prepare UI for next card
        shr = null;
        btnWriteToCard.setDisable(true);
        btnReadCard.setDisable(true);
        readerWriter = new MainSmartCardReadWrite(txtProcessLogger, cboDeviceReaderList);
        btnConnectReader.setDisable(true);
        btnLoadEligibleList.setDisable(true);

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
        String shrStr = "{\n";
        shrStr += "\t\"CARD_DETAILS\": " +  readerWriter.readCard(SmartCardUtils.getUserFile(SmartCardUtils.CARD_DETAILS_USER_FILE_NAME), (byte)0x00 );
        shrStr += ", \t\"IMMUNIZATION\": [" + readerWriter.readArray(SmartCardUtils.getUserFile(SmartCardUtils.IMMUNIZATION_USER_FILE_NAME)) + "]";
        shrStr += ",\t\"HIV_TEST\": [" + readerWriter.readArray(SmartCardUtils.getUserFile(SmartCardUtils.HIV_TEST_USER_FILE_NAME))+ "]";

        shrStr += ", \t\"PATIENT_IDENTIFICATION\": {\n";
        shrStr += "  \t\"PATIENT_NAME\": " + readerWriter.readArray(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_DEMOGRAPHICS_NAME));

        shrStr += ", \t\"EXTERNAL_PATIENT_ID\": " + readerWriter.readCard(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_EXTERNAL_NAME), (byte)0x00);
        shrStr += ", \t\"INTERNAL_PATIENT_ID\": [" + readerWriter.readArray(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_INTERNAL_NAME)) + "]";
        shrStr += ", \t\"PATIENT_ADDRESS\": " + readerWriter.readCard(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_ADDRESS_NAME), (byte)0x00);
        shrStr += ", \t\"MOTHER_DETAILS\": { \n\t\"MOTHER_NAME\": " + readerWriter.readCard(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_DETAIL_NAME), (byte)0x00);
        shrStr += ", \t\"MOTHER_IDENTIFIER\": [" + readerWriter.readArray(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_IDENTIFIER_NAME)) + "]";
        shrStr += "}\n}";
        shrStr += ", \t\"NEXT_OF_KIN\": []";
        shrStr += ", \t\"VERSION\": \"1.0.0\"";
        shrStr += "\n}";
        shr = SHRUtils.getSHRObj(shrStr);
        loadCardDetails();
        loadIdentifiers();
        loadHIVTests();
        showCurrentClient();
        btnWriteToCard.setDisable(false);
        btnLoadFromEMR.setDisable(false);
        btnPushToEMR.setDisable(false);
        SmartCardUtils.displayOut(txtProcessLogger, "\nSuccessfully read the Shared Health Record from card\n");
    }


    public void formatCard(ActionEvent event) {
        readerWriter.connectReader(btnConnectReader);
        btnReadCard.setDisable(false);
        //btnFormatCard.setDisable(false);
    }

    public void getFromEMR(ActionEvent actionEvent){

        String purpose = "HTTP GET - Fetch SHR from EMR. Takes Card serial as parameter";
        String url = getURL(purpose);

        if(!url.isEmpty()){
            //String cardSerialNo = SHRUtils.getCardSerialNo(shr);
            //url += (url.endsWith("/")) ? cardSerialNo : "/" + cardSerialNo;
            url += (url.endsWith("/")) ? "12345678-ADFGHJY-0987654-QWERTY" : "/" + "12345678-ADFGHJY-0987654-QWERTY";
            String SHRStr = APIClient.fetchData(url);
            shr = SHRUtils.getSHRObj(SHRStr);
            loadCardDetails();
            loadIdentifiers();
            loadHIVTests();
            showCurrentClient();
            btnWriteToCard.setDisable(false);
            btnLoadFromEMR.setDisable(true);
            btnPushToEMR.setDisable(true);
            SmartCardUtils.displayOut(txtProcessLogger, "\nSuccessfully retrieved SHR from the EMR\n");
        } else {
            SmartCardUtils.displayOut(txtProcessLogger, "\nPlease specify the `"+purpose+"` endpoint url!");
        }
    }
}
