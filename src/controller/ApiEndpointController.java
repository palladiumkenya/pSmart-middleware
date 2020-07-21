package controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jsonvalidator.utils.PropertiesManager;
import jsonvalidator.utils.SHRUtils;
import models.Converter;
import models.EditCell;
import models.Endpoint;
import models.EndpointConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jsonvalidator.utils.PropertiesManager.writeFile;

public class ApiEndpointController {

    @FXML
    private TableView<EndpointConfig> GridClientLastEncounter;

    @FXML
    private TableColumn<EndpointConfig, String> endpointUrl;

    @FXML
    private TableColumn<EndpointConfig, String> endpointPurpose;

    @FXML
    private Button btnSaveEndpoints;

    @FXML
    void  initialize(){
        assert GridClientLastEncounter != null : "fx:id=\"GridClientLastENcounter1\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert endpointUrl != null : "fx:id=\"endpointUrl\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert endpointPurpose != null : "fx:id=\"endpointPurpose\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";

        GridClientLastEncounter.getSelectionModel().setCellSelectionEnabled(true);
        endpointUrl.setCellFactory(EditCell.<EndpointConfig, String> forTableColumn(new Converter(endpointPurpose.getText())));

        endpointUrl.setOnEditCommit(event -> {
        final String value = event.getNewValue() != null ? event.getNewValue() :
                event.getOldValue();
        ((EndpointConfig) event.getTableView().getItems()
                .get(event.getTablePosition().getRow()))
                .setEndpointUrl(value);
            updateEndpoint(event.getRowValue().endpointPurpose.get(), value);
            if(value.equals("")){
            }
            GridClientLastEncounter.setItems(getEndPointsForDisplay());
        });



        endpointPurpose.setCellValueFactory(new PropertyValueFactory<EndpointConfig,String>("endpointPurpose"));
        GridClientLastEncounter.setItems(getEndPointsForDisplay());
        setTableEditable();
    }

    @FXML
    void loadLogin(ActionEvent ae) {
        List<Endpoint> endpoints = PropertiesManager.readJSONFile();
        List<String> endpointUrls = new ArrayList<>();
        for (Endpoint endpoint : endpoints) {
            if(endpoint.getEndpointUrl().equals("")) {
                endpointUrls.add(endpoint.getEndpointPurpose());
            }
        }

        if(endpointUrls.size() == 0) {
            createStage("/view/login.fxml");
            Stage endPointsStage = (Stage) btnSaveEndpoints.getScene().getWindow();
            endPointsStage.close();
        } else {
            createStage("/view/ApiEndpoints.fxml");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Endpoints:");
            alert.setHeaderText("P-Smart Middleware");
            alert.setContentText("Please input all the endpoints!");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){

            } else {

            }
        }
    }

    private void createStage(String uiFxml) {
        try{
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(uiFxml));
            primaryStage.setTitle("P-SMART");
            Scene loginScene = new Scene(root);
            primaryStage.setScene(loginScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setTableEditable() {
        GridClientLastEncounter.setEditable(true);
        GridClientLastEncounter.getSelectionModel().cellSelectionEnabledProperty().set(true);
        GridClientLastEncounter.setOnKeyPressed(event -> {
        if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
            editFocusedCell();
        } else if (event.getCode() == KeyCode.RIGHT ||
                event.getCode() == KeyCode.TAB) {
            GridClientLastEncounter.getSelectionModel().selectNext();
            event.consume();
        } else if (event.getCode() == KeyCode.LEFT) {
            selectPrevious();
            event.consume();
        }
        });
    }

    @SuppressWarnings("unchecked")
    private void selectPrevious() {
        if (GridClientLastEncounter.getSelectionModel().isCellSelectionEnabled()) {
            // in cell selection mode, we have to wrap around, going from
            // right-to-left, and then wrapping to the end of the previous line
            TablePosition<EndpointConfig, ?> pos = GridClientLastEncounter.getFocusModel()
                    .getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {
                // go to previous row
                GridClientLastEncounter.getSelectionModel().select(pos.getRow(),
                        getTableColumn(pos.getTableColumn(), -1));
            } else if (pos.getRow() < GridClientLastEncounter.getItems().size()) {
                // wrap to end of previous row
                GridClientLastEncounter.getSelectionModel().select(pos.getRow() - 1,
                        GridClientLastEncounter.getVisibleLeafColumn(
                                GridClientLastEncounter.getVisibleLeafColumns().size() - 1));
            }
        } else {
            int focusIndex = GridClientLastEncounter.getFocusModel().getFocusedIndex();
            if (focusIndex == -1) {
                GridClientLastEncounter.getSelectionModel().select(GridClientLastEncounter.getItems().size() - 1);
            } else if (focusIndex > 0) {
                GridClientLastEncounter.getSelectionModel().select(focusIndex - 1);
            }
        }
    }

    private TableColumn<EndpointConfig, ?> getTableColumn(
            final TableColumn<EndpointConfig, ?> column, int offset) {
        int columnIndex = GridClientLastEncounter.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return GridClientLastEncounter.getVisibleLeafColumn(newColumnIndex);
    }

    @SuppressWarnings("unchecked")
    private void editFocusedCell() {
        final TablePosition<EndpointConfig, ?> focusedCell = GridClientLastEncounter
                .focusModelProperty().get().focusedCellProperty().get();
        GridClientLastEncounter.edit(focusedCell.getRow(), focusedCell.getTableColumn());
    }

    public ObservableList<EndpointConfig> getEndPointsForDisplay(){
        List<Endpoint> endpoints = PropertiesManager.readJSONFile();
        ObservableList<EndpointConfig> endpointConfigs = FXCollections.observableArrayList();
        for (Endpoint endpoint : endpoints) {
            EndpointConfig endpointConfig =new EndpointConfig();
            endpointConfig.endpointPurpose.set(endpoint.getEndpointPurpose());
            endpointConfig.endpointUrl.set(endpoint.getEndpointUrl());
            endpointConfigs.add(endpointConfig);
        }
        return endpointConfigs;
    }

    public void updateEndpoint(String purpose, String url) {
        List<Endpoint> endpoints = PropertiesManager.readJSONFile();
        List<Endpoint> newEndpoints = new ArrayList<>();
        for (Endpoint endpoint : endpoints) {
            Endpoint newEndpoint = new Endpoint();
            if(endpoint.getEndpointPurpose().equals(purpose)) {
                newEndpoint.setEndpointPurpose(purpose);
                newEndpoint.setEndpointUrl(url);
            } else {
                newEndpoint.setEndpointUrl(endpoint.getEndpointUrl());
                newEndpoint.setEndpointPurpose(endpoint.getEndpointPurpose());
            }
            newEndpoints.add(newEndpoint);
        }
        writeFile(SHRUtils.getJSON(newEndpoints));
    }

    public void closeWindow(JFXButton btn){
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }
}


