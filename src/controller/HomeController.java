package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import jsonvalidator.utils.DateFormater;
import jsonvalidator.utils.Encryption;
import jsonvalidator.utils.SHRUtils;
import models.CardDetail;
import models.HIVTest;
import models.Identifier;
import models.*;
import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static jsonvalidator.utils.EndpointUtils.getURL;


public class HomeController  {

    private String message=null;
    MainSmartCardReadWrite readerWriter;

    private String displayName;

    private String facility;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @FXML
    private Label lblpsmartTitle;

    @FXML
    private Label lblCurrentPatient;

    @FXML
    private TextField txtSearch;

    @FXML
    private TabPane tpMainTabPane;

    @FXML
    private Button btnWriteToCard;

    @FXML
    private Label lblFacilityName;

    @FXML
    private Label lblSex;

    @FXML
    private Button btnEjectCard;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblMotherNames;

    @FXML
    private Label lblLastUpdated;

    @FXML
    private Label lblLastUpdatedFacility;

    @FXML
    private Label lblAge;

    @FXML
    private TextArea txtProcessLogger;

    @FXML
    private TableColumn<Immunization, String> colImmunizationName;

    @FXML
    private TableColumn<CardDetail, String> colReason;

    @FXML
    private TableColumn<CardDetail, String> colLastUpdate;

    @FXML
    private TableColumn<Immunization, String> colDateAdministered;

    @FXML
    private TableView<Immunization> GridCardSummary;

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
    private TableView<Identifier> GridMotherIdentifiers;

    @FXML
    private TableColumn<Identifier, String> colMotherIdentifierId;

    @FXML
    private TableColumn<Identifier, String> colMotherIdentifierType;

    @FXML
    private TableColumn<Identifier, String> colMotherAssigningAuthority;

    @FXML
    private TableColumn<Identifier, String> colMotherAssigningFacility;

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

    private SHR shr = null;

    private SHR shrFromEMR;

    private EncryptedSHR encryptedSHR;

    //Load Card Details
    private final void loadImmunizations(SHR shr){
        colImmunizationName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDateAdministered.setCellValueFactory(new PropertyValueFactory<>("dateAdministered"));
        GridCardSummary.setItems(FXCollections.observableArrayList(getImmunizations(shr)));

    }

    private final void showCardDetails(SHR shr) {
        lblLastUpdatedFacility.setVisible(true);
        lblLastUpdatedFacility.setText("Last Updated @: " + shr.cARD_DETAILS.lAST_UPDATED_FACILITY);

        lblStatus.setVisible(true);
        lblStatus.setText("Card Status: " + shr.cARD_DETAILS.sTATUS);

        lblLastUpdated.setVisible(true);
        lblLastUpdated.setText("Last Updated On: "+ DateFormater.formatDate(shr.cARD_DETAILS.lAST_UPDATED));
    }

    private final ObservableList<Immunization> getImmunizations(SHR shr){
        ObservableList<Immunization> immunizations = FXCollections.observableArrayList();
        for(int i=0; i < shr.iMMUNIZATION.length;i++ ){
            Immunization immunization = new Immunization(
                    shr.iMMUNIZATION[i].nAME,
                    DateFormater.formatDate(shr.iMMUNIZATION[i].dATE_ADMINISTERED)
            );
            immunizations.add(immunization);
        }
        return immunizations;
    }

    private static final int getMonthsDifference(Date date1, Date date2) {
        int m1 = date1.getYear() * 12 + date1.getMonth();
        int m2 = date2.getYear() * 12 + date2.getMonth();
        return m2 - m1 + 1;
    }

    private final void showCurrentClient(SHR shr) {
        String patientName = shr.pATIENT_IDENTIFICATION.pATIENT_NAME.fIRST_NAME + " " + shr.pATIENT_IDENTIFICATION.pATIENT_NAME.mIDDLE_NAME + " " + shr.pATIENT_IDENTIFICATION.pATIENT_NAME.lAST_NAME;
        String patientDob = shr.pATIENT_IDENTIFICATION.dATE_OF_BIRTH;
        String patientSex = shr.pATIENT_IDENTIFICATION.sEX;
        String patientMotherName = shr.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_NAME.fIRST_NAME + " " + shr.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_NAME.lAST_NAME;
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
        lblMotherNames.setVisible(true);
        lblMotherNames.setText("Mother Name: "+ patientMotherName);
    }
    //Load Identifiers
    private final void loadIdentifiers(SHR shr){
        colIdentifierId.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        colIdentifierType.setCellValueFactory(new PropertyValueFactory<>("identifierType"));
        colAssigningAuthority.setCellValueFactory(new PropertyValueFactory<>("assigningAuthority"));
        colAssigningFacility.setCellValueFactory(new PropertyValueFactory<>("assigningFacility"));
        GridClientIdentifiers.setItems(FXCollections.observableArrayList(getIdentifiers(shr)));
    }

    private final void loadMotherIdentifiers(SHR shr){
        colMotherIdentifierId.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        colMotherIdentifierType.setCellValueFactory(new PropertyValueFactory<>("identifierType"));
        colMotherAssigningAuthority.setCellValueFactory(new PropertyValueFactory<>("assigningAuthority"));
        colMotherAssigningFacility.setCellValueFactory(new PropertyValueFactory<>("assigningFacility"));
        GridMotherIdentifiers.setItems(FXCollections.observableArrayList(getMotherIdentifiers(shr)));
    }

    @FXML
    private void nextCard(ActionEvent ae) {
        ObservableList<EligiblePerson> eligiblePersons = FXCollections.observableArrayList();
        GridEligibleList.setItems(eligiblePersons);
        GridMotherIdentifiers.getItems().clear();
        GridClientLastENcounter.getItems().clear();
        GridClientIdentifiers.getItems().clear();
        GridCardSummary.getItems().clear();
        txtProcessLogger.clear();

        lblLastUpdatedFacility.setVisible(false);
        lblLastUpdatedFacility.setText("");

        lblStatus.setVisible(false);
        lblStatus.setText("");

        lblLastUpdated.setVisible(false);
        lblLastUpdated.setText("");

        lblAge.setVisible(false);
        lblAge.setText("");

        lblAge.setVisible(false);
        lblAge.setText("");

        lblCurrentPatient.setText("Insert a card to read it's contents.");
        lblSex.setVisible(false);
        lblSex.setText("");
        lblMotherNames.setVisible(false);
        lblMotherNames.setText("");

        cboDeviceReaderList.getItems().clear();

        btnWriteToCard.setDisable(true);
        btnReadCard.setDisable(true);

        readerWriter = new MainSmartCardReadWrite(txtProcessLogger, cboDeviceReaderList);
        btnConnectReader.setDisable(true);
        btnLoadEligibleList.setDisable(true);
        lblSex.setVisible(false);
        lblAge.setVisible(false);
        lblStatus.setVisible(false);
        lblLastUpdated.setVisible(false);
        lblLastUpdatedFacility.setVisible(false);
        lblMotherNames.setVisible(false);
        btnLoadFromEMR.setDisable(true);
        btnEjectCard.setDisable(true);
        btnPushToEMR.setDisable(true);
        tpMainTabPane.getSelectionModel().select(0);
        //txtProcessLogger.setText("");

    }

    private final void loadElligibleList(){
        lblLastUpdatedFacility.setVisible(false);
        lblLastUpdatedFacility.setText("");

        lblStatus.setVisible(false);
        lblStatus.setText("");

        lblLastUpdated.setVisible(false);
        lblLastUpdated.setText("");

        lblAge.setVisible(false);
        lblAge.setText("");

        lblAge.setVisible(false);
        lblAge.setText("");

        lblCurrentPatient.setText("Insert a card to read it's contents.");
        lblSex.setVisible(false);
        lblSex.setText("");
        lblMotherNames.setVisible(false);
        lblMotherNames.setText("");
        lblSex.setVisible(false);
        lblAge.setVisible(false);
        lblStatus.setVisible(false);
        lblLastUpdated.setVisible(false);
        lblLastUpdatedFacility.setVisible(false);
        lblMotherNames.setVisible(false);


        colPatientId.setCellValueFactory(cellData -> cellData.getValue().patientIdProperty());
        colFirstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        colMiddleName.setCellValueFactory(cellData -> cellData.getValue().middleNameProperty());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        colGender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        colAge.setCellValueFactory(cellData -> cellData.getValue().ageProperty());

        FilteredList<EligiblePerson> filteredData = new FilteredList<>(getEligibilityList(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<EligiblePerson> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(GridEligibleList.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        GridEligibleList.setItems(sortedData);

        // GridEligibleList.setItems(FXCollections.observableArrayList(getEligibilityList()));
        SmartCardUtils.displayOut(txtProcessLogger,  "Successfully fetched eligible clients");

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
					    CardAssignment cardAssignment = new CardAssignment(rowData.getPatientId(), readerWriter.getCardSerial());
					    String cardAssignmentStr = SHRUtils.getJSON(cardAssignment);
                        String response = APIClient.postData(url, cardAssignmentStr);
                        shrFromEMR = SHRUtils.getSHRObj(response);
                        //add card serial and card details to SHR
                        String pattern = "yyyyMMddhhmmss";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        String date = simpleDateFormat.format(new Date());

                        shrFromEMR.cARD_DETAILS.lAST_UPDATED_FACILITY = this.facility;
                        shrFromEMR.cARD_DETAILS.lAST_UPDATED = date;
                        System.out.println(SHRUtils.getJSON(shrFromEMR));
                        loadImmunizations(shrFromEMR);
                        loadMotherIdentifiers(shrFromEMR);
						loadIdentifiers(shrFromEMR);
						loadHIVTests(shrFromEMR);
                        showCurrentClient(shrFromEMR);
                        showCardDetails(shrFromEMR);
						btnWriteToCard.setDisable(false);
						tpMainTabPane.getSelectionModel().select(0);
                        SmartCardUtils.displayOut(txtProcessLogger,  "Successfully fetched client's Shared Health Record");
					} else if (result.get() == buttonTypeNo) {

					}
				}
			});
			return row ;
		});
    }

    //private displayAlert

    private final ObservableList<Identifier> getIdentifiers(SHR shr){
        ObservableList<Identifier> identifiers = FXCollections.observableArrayList();
        for(int i=0; i < shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID.length;i++ ){
            Identifier identifier;
            if(shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].iDENTIFIER_TYPE.equals("CARD_SERIAL_NUMBER")) {
                identifier = new Identifier(
                        readerWriter.getCardSerial(),
                        shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].iDENTIFIER_TYPE,
                        shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].aSSIGNING_AUTHORITY,
                        shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].aSSIGNING_FACILITY
                );
            } else {
                identifier = new Identifier(
                        shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].iD,
                        shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].iDENTIFIER_TYPE,
                        shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].aSSIGNING_AUTHORITY,
                        shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID[i].aSSIGNING_FACILITY
                );
            }
            identifiers.add(identifier);
        }
        return identifiers;
    }

    private final ObservableList<Identifier> getMotherIdentifiers(SHR shr){
        ObservableList<Identifier> identifiers = FXCollections.observableArrayList();
        for(int i=0; i < shr.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_IDENTIFIER.length;i++ ){
            Identifier identifier = new Identifier(
                    shr.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_IDENTIFIER[i].iD,
                    shr.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_IDENTIFIER[i].iDENTIFIER_TYPE,
                    shr.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_IDENTIFIER[i].aSSIGNING_AUTHORITY,
                    shr.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_IDENTIFIER[i].aSSIGNING_FACILITY
            );
            identifiers.add(identifier);
        }
        return identifiers;
    }

    private final ObservableList<EligiblePerson> getEligibilityList(){
        ObservableList<EligiblePerson> eligiblePersons = FXCollections.observableArrayList();
        String eligibleListUrl = getURL("HTTP GET - Fetch eligible list from EMR");
        Map<String, String> responseMap = APIClient.fetchData(eligibleListUrl);
        String response = responseMap.get("response");
        String success = responseMap.get("success");
        if(success.equals("true")) {
            List<EligibleList> eligibleList = SHRUtils.getEligibleList(response);
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
        } else {
            SmartCardUtils.displayOut(txtProcessLogger, response);
        }

        return eligiblePersons;
    }

    //Load Identifiers
    private final void loadHIVTests(SHR shr){
        colTestDate.setCellValueFactory(new PropertyValueFactory<>("testDate"));
        colResult.setCellValueFactory(new PropertyValueFactory<>("result"));
        colFacility.setCellValueFactory(new PropertyValueFactory<>("facility"));
        colStrategy.setCellValueFactory(new PropertyValueFactory<>("strategy"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        GridClientLastENcounter.setItems(FXCollections.observableArrayList(getHIVTests(shr)));
    }

    private final ObservableList<HIVTest> getHIVTests(SHR shr){
        ObservableList<HIVTest> hivTests = FXCollections.observableArrayList();
        for(int i=0; i < shr.hIV_TEST.length;i++ ){
            HIVTest hivTest = new HIVTest(
                    DateFormater.formatDate(shr.hIV_TEST[i].dATE),
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
        lblStatus.setVisible(false);
        lblLastUpdated.setVisible(false);
        lblLastUpdatedFacility.setVisible(false);
        lblMotherNames.setVisible(false);

        String facilityName = getURL("Facility Name");
        lblFacilityName.setText(facilityName);
    }

    public void initVariable(String displayName, String facility){
        this.displayName = displayName;
        this.facility = facility;
        lblUserId.setText(displayName);
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

    @FXML
    void sendDataToEmr(ActionEvent event) {
        String purpose = "HTTP POST - Push SHR to EMR";
        String response = APIClient.postObject(shr, purpose);
        SmartCardUtils.displayOut(txtProcessLogger, response);
    }

    @FXML
    void getEligibleList(ActionEvent actionEvent) {
        tpMainTabPane.getSelectionModel().select(4);
        loadElligibleList();
        btnLoadEligibleList.setDisable(true);
        btnReadCard.setDisable(true);
        btnPushToEMR.setDisable(true);
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
        String cardSerial = SHRUtils.getCardSerialNo(shrFromEMR);
        System.out.println(cardSerial);

        if(!cardSerial.equals(readerWriter.getCardSerial())){
            SmartCardUtils.displayOut(txtProcessLogger, "This card does not belong to this client!");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Wrong Card Assignment");
            alert.setHeaderText("P-Smart Middleware");
            alert.setContentText("This card does not belong to this client!");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){

            } else {

            }
        } else {
            System.out.println("From EMR: "+SHRUtils.getJSON(shrFromEMR));
            SmartCardUtils.displayOut(txtProcessLogger, "Writing to card ... ");
            readerWriter.formatCard();
            List<String> demographics = new ArrayList<>();
            //shrFromEMR = shr;
            StringBuilder otherDemographics = new StringBuilder();
            otherDemographics
                    .append("\"DATE_OF_BIRTH\": \"").append(shrFromEMR.pATIENT_IDENTIFICATION.dATE_OF_BIRTH).append("\"")
                    .append(", \"DATE_OF_BIRTH_PRECISION\": \"").append(shrFromEMR.pATIENT_IDENTIFICATION.dATE_OF_BIRTH_PRECISION).append("\"")
                    .append(", \"SEX\": \"").append(shrFromEMR.pATIENT_IDENTIFICATION.sEX).append("\"")
                    .append(", \"DEATH_DATE\": \"").append(shrFromEMR.pATIENT_IDENTIFICATION.dEATH_DATE).append("\"")
                    .append(", \"DEATH_INDICATOR\": \"").append(shrFromEMR.pATIENT_IDENTIFICATION.dEATH_INDICATOR).append("\"")
                    .append(", \"PHONE_NUMBER\": \"").append(shrFromEMR.pATIENT_IDENTIFICATION.pHONE_NUMBER).append("\"")
                    .append(", \"MARITAL_STATUS\": \"").append(shrFromEMR.pATIENT_IDENTIFICATION.mARITAL_STATUS).append("\"");
            String patientName = SHRUtils.getJSON(shrFromEMR.pATIENT_IDENTIFICATION.pATIENT_NAME);
            demographics.add(patientName);
            demographics.add(otherDemographics.toString());
            String patientExternalIdentifiers = SHRUtils.getJSON(shrFromEMR.pATIENT_IDENTIFICATION.eXTERNAL_PATIENT_ID);
            String cardDetails = SHRUtils.getJSON(shrFromEMR.cARD_DETAILS);
            String motherDetails = SHRUtils.getJSON(shrFromEMR.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_NAME);
            List<String> immunizationDetails = getStringArr(shrFromEMR, "IMMUNIZATION");
            List<String> hivTests = getStringArr(shrFromEMR, "HIV_TEST");
            List<String> internalIdentifiers = getStringArr(shrFromEMR, "INTERNAL_PATIENT_ID");
            List<String> motherIdentifiers = getStringArr(shrFromEMR, "MOTHER_IDENTIFIER");
            String addressDetails=SHRUtils.getJSON(shrFromEMR.pATIENT_IDENTIFICATION.pATIENT_ADDRESS);

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

            SmartCardUtils.displayOut(txtProcessLogger, "Successfully written to card");
            System.out.println("After writing: "+SHRUtils.getJSON(shrFromEMR));
            btnEjectCard.setDisable(false);
            //TODO: encrypt and compress SHR and send to EMR
            //byte[] compressedMessage = Compression.Compress(encryptedSHR);

            SmartCardUtils.displayOut(txtProcessLogger, "Sending encrypted SHR to PSMART Store");
            String purpose = "HTTP POST - Push encrypted SHR to EMR";
            EncryptedSHR encSHR  = getEncryptedSHR(shrFromEMR);
            String response = APIClient.postObject(encSHR, purpose);
            SmartCardUtils.displayOut(txtProcessLogger, response);
            readerWriter.getCardSerial();

            btnWriteToCard.setDisable(true);
            btnReadCard.setDisable(true);
            readerWriter = new MainSmartCardReadWrite(txtProcessLogger, cboDeviceReaderList);
            btnConnectReader.setDisable(true);
            btnLoadEligibleList.setDisable(true);
        }
    }

    public EncryptedSHR getEncryptedSHR(SHR shr) {
        EncryptedSHR.ADDENDUM addendum = new EncryptedSHR.ADDENDUM(shr.cARD_DETAILS, shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID);
        String strShr = SHRUtils.getJSON(shr);
        String encSHRStr = Encryption.encrypt(strShr);
        EncryptedSHR encSHR = new EncryptedSHR(encSHRStr, addendum);
        return encSHR;
    }

    public void LoadEndpointConfig(ActionEvent event) throws ParseException{
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(
                    Main.class.getResource("ApiEndpoints.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Configuration");
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
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
        String readTest = readerWriter.readCard(SmartCardUtils.getUserFile(SmartCardUtils.CARD_DETAILS_USER_FILE_NAME), (byte)0x00 );

        if(readTest.startsWith("ÿÿÿÿÿÿÿÿÿÿ") || readTest.equals("") || readTest.isEmpty() || readTest == null) {
            SmartCardUtils.displayOut(txtProcessLogger, "This card is empty!");
            return;
        }
        String shrStr = "{\n";
        shrStr += "\t\"PATIENT_IDENTIFICATION\": {\n";
        shrStr += "  \t\"PATIENT_NAME\": " + readerWriter.readArray(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_DEMOGRAPHICS_NAME));

        shrStr += ", \t\"EXTERNAL_PATIENT_ID\": " + readerWriter.readCard(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_EXTERNAL_NAME), (byte)0x00);
        shrStr += ", \t\"INTERNAL_PATIENT_ID\": [" + readerWriter.readArray(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_INTERNAL_NAME)) + "]";
        shrStr += ", \t\"PATIENT_ADDRESS\": " + readerWriter.readCard(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_ADDRESS_NAME), (byte)0x00);
        shrStr += ", \t\"MOTHER_DETAILS\": { \n\t\"MOTHER_NAME\": " + readerWriter.readCard(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_DETAIL_NAME), (byte)0x00);
        shrStr += ", \t\"MOTHER_IDENTIFIER\": [" + readerWriter.readArray(SmartCardUtils.getUserFile(SmartCardUtils.IDENTIFIERS_USER_FILE_MOTHER_IDENTIFIER_NAME)) + "]";
        shrStr += "}\n}";
        shrStr += ", \t\"NEXT_OF_KIN\": []";
        shrStr += ",\t\"HIV_TEST\": [" + readerWriter.readArray(SmartCardUtils.getUserFile(SmartCardUtils.HIV_TEST_USER_FILE_NAME))+ "]";
        shrStr += ", \t\"IMMUNIZATION\": [" + readerWriter.readArray(SmartCardUtils.getUserFile(SmartCardUtils.IMMUNIZATION_USER_FILE_NAME)) + "]";
        shrStr += ", \t\"CARD_DETAILS\": " +  readerWriter.readCard(SmartCardUtils.getUserFile(SmartCardUtils.CARD_DETAILS_USER_FILE_NAME), (byte)0x00 );
        shrStr += ", \t\"VERSION\": \"1.0.0\"";
        shrStr += "\n}";

        System.out.println(shrStr);
        shr = SHRUtils.getSHRObj(shrStr);
        loadImmunizations(shr);
        loadMotherIdentifiers(shr);
        loadIdentifiers(shr);
        loadHIVTests(shr);
        showCurrentClient(shr);
        showCardDetails(shr);
        tpMainTabPane.getSelectionModel().select(0);
        btnWriteToCard.setDisable(false);
        btnLoadFromEMR.setDisable(false);
        btnPushToEMR.setDisable(false);
        btnEjectCard.setDisable(false);
        SmartCardUtils.displayOut(txtProcessLogger, "Successfully read the Shared Health Record from card");
    }


    public void formatCard(ActionEvent event) {
        readerWriter.connectReader(btnConnectReader);
        btnReadCard.setDisable(true);
        readerWriter.formatCard();
    }

    public void getFromEMR(ActionEvent actionEvent){

        String purpose = "HTTP GET - Fetch SHR from EMR. Takes Card serial as parameter";
        String url = getURL(purpose);

        if(!url.isEmpty()){
            String cardSerialNo = readerWriter.getCardSerial();
            if(url.endsWith("=")){
                url = (url.endsWith("=")) ? url.concat(cardSerialNo) : url;
            } else {
                url += (url.endsWith("/")) ? cardSerialNo : "/" + cardSerialNo;
            }
            System.out.println(url);
            Map<String, String> responseMap = APIClient.fetchData(url);
            String response = responseMap.get("response");
            String success = responseMap.get("success");
            if(success.equals("true")) {
                shrFromEMR = SHRUtils.getSHRObj(response);
                loadImmunizations(shrFromEMR);
                loadIdentifiers(shrFromEMR);
                loadHIVTests(shrFromEMR);
                loadMotherIdentifiers(shrFromEMR);
                showCurrentClient(shrFromEMR);
                showCardDetails(shrFromEMR);

                if(shr != null) {
                    DiffResult diffPID = shrFromEMR.pATIENT_IDENTIFICATION.diff(shr.pATIENT_IDENTIFICATION);
                    DiffResult diffCardDetails = shrFromEMR.cARD_DETAILS.diff(shr.cARD_DETAILS);

                    List<String> diffs = new ArrayList<>();
                    for(Diff<?> d: diffPID.getDiffs()) {
                        diffs.add(d.getFieldName() + " FROM [" + d.getRight() + "] TO [" + d.getLeft() + "]");
                    }
                    for(Diff<?> d: diffCardDetails.getDiffs()) {
                        diffs.add(d.getFieldName() + " FROM [" + d.getRight() + "] TO [" + d.getLeft() + "]");
                    }
                    String formattedDiffs = "";
                    if(diffs.size() > 0) {
                        formattedDiffs.concat("Changes from the EMR:\n");
                        for (String diff : diffs) {
                            formattedDiffs.concat("- ").concat(diff).concat("\n");
                        }
                        SmartCardUtils.displayOut(txtProcessLogger, formattedDiffs);
                    }
                    shrFromEMR.hIV_TEST = getFinalHIVTests(shrFromEMR.hIV_TEST, shr.hIV_TEST).toArray(new SHR.HIV_TEST[0]);
                    shrFromEMR.iMMUNIZATION = getFinalImmunizations(shrFromEMR.iMMUNIZATION, shr.iMMUNIZATION).toArray(new SHR.IMMUNIZATION[0]);

                    shrFromEMR.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID = getFinalIdentifiers(
                            shrFromEMR.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID,
                            shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID).toArray(new SHR.PATIENT_IDENTIFICATION.INTERNAL_PATIENT_ID[0]);

                    shrFromEMR.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_IDENTIFIER = getFinalMotherIdentifiers(
                            shrFromEMR.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_IDENTIFIER,
                            shr.pATIENT_IDENTIFICATION.mOTHER_DETAILS.mOTHER_IDENTIFIER).toArray(new SHR.PATIENT_IDENTIFICATION.MOTHER_DETAILS.MOTHER_IDENTIFIER[0]);
                }
                tpMainTabPane.getSelectionModel().select(0);
                btnWriteToCard.setDisable(false);
                btnLoadFromEMR.setDisable(true);
                btnPushToEMR.setDisable(true);
                SmartCardUtils.displayOut(txtProcessLogger, "Successfully retrieved SHR from the EMR");
            } else {
                SmartCardUtils.displayOut(txtProcessLogger, responseMap.get("response"));
            }
        } else {
            SmartCardUtils.displayOut(txtProcessLogger, "Please specify the `"+purpose+"` endpoint url!");
        }
    }

    private List<SHR.HIV_TEST> getFinalHIVTests(SHR.HIV_TEST []EMRTests, SHR.HIV_TEST []cardTests) {
        List<SHR.HIV_TEST> finalHIVTests = new ArrayList<>();
        finalHIVTests.addAll(Arrays.asList(EMRTests));
        finalHIVTests.addAll(Arrays.asList(cardTests));
        java.util.List<SHR.HIV_TEST> uniqueTests = finalHIVTests.stream().distinct().collect(Collectors.toList());
        return uniqueTests;
    }

    private List<SHR.PATIENT_IDENTIFICATION.INTERNAL_PATIENT_ID> getFinalIdentifiers(SHR.PATIENT_IDENTIFICATION.INTERNAL_PATIENT_ID []ids1, SHR.PATIENT_IDENTIFICATION.INTERNAL_PATIENT_ID []ids2) {
        List<SHR.PATIENT_IDENTIFICATION.INTERNAL_PATIENT_ID> finalIdentifiers = new ArrayList<>();
        finalIdentifiers.addAll(Arrays.asList(ids1));
        finalIdentifiers.addAll(Arrays.asList(ids2));
        java.util.List<SHR.PATIENT_IDENTIFICATION.INTERNAL_PATIENT_ID> uniqueIdentifiers = finalIdentifiers.stream().distinct().collect(Collectors.toList());
        return uniqueIdentifiers;
    }

    private List<SHR.PATIENT_IDENTIFICATION.MOTHER_DETAILS.MOTHER_IDENTIFIER> getFinalMotherIdentifiers(
            SHR.PATIENT_IDENTIFICATION.MOTHER_DETAILS.MOTHER_IDENTIFIER []ids1, SHR.PATIENT_IDENTIFICATION.MOTHER_DETAILS.MOTHER_IDENTIFIER []ids2) {
        List<SHR.PATIENT_IDENTIFICATION.MOTHER_DETAILS.MOTHER_IDENTIFIER> finalIdentifiers = new ArrayList<>();
        finalIdentifiers.addAll(Arrays.asList(ids1));
        finalIdentifiers.addAll(Arrays.asList(ids2));
        java.util.List<SHR.PATIENT_IDENTIFICATION.MOTHER_DETAILS.MOTHER_IDENTIFIER> uniqueIdentifiers = finalIdentifiers.stream().distinct().collect(Collectors.toList());
        return uniqueIdentifiers;
    }

    private List<SHR.IMMUNIZATION> getFinalImmunizations(SHR.IMMUNIZATION []immunizations, SHR.IMMUNIZATION []immunizations1) {
        List<SHR.IMMUNIZATION> finalImmunization = new ArrayList<>();
        finalImmunization.addAll(Arrays.asList(immunizations));
        finalImmunization.addAll(Arrays.asList(immunizations1));
        java.util.List<SHR.IMMUNIZATION> uniqueImmunizations = finalImmunization.stream().distinct().collect(Collectors.toList());
        return uniqueImmunizations;
    }
}
