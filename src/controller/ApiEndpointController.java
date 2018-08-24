package controller;

import com.jfoenix.controls.JFXButton;
import dbConnection.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import models.Converter;
import models.EditCell;
import models.Endpoint;
import models.EndpointConfig;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApiEndpointController {

    @FXML
    private TableView<EndpointConfig> GridClientLastEncounter;

    @FXML
    private TableColumn<EndpointConfig, String> endpointUrl;

    @FXML
    private TableColumn<EndpointConfig, String> endpointPurpose;

    @FXML
    void  initialize(){
        assert GridClientLastEncounter != null : "fx:id=\"GridClientLastENcounter1\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert endpointUrl != null : "fx:id=\"endpointUrl\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";
        assert endpointPurpose != null : "fx:id=\"endpointPurpose\" was not injected: check your FXML file 'ApiEndpoints.fxml'.";

        GridClientLastEncounter.getSelectionModel().setCellSelectionEnabled(true);
        endpointUrl.setCellFactory(EditCell.<EndpointConfig, String> forTableColumn(new Converter()));

        endpointUrl.setOnEditCommit(event -> {
        final String value = event.getNewValue() != null ? event.getNewValue() :
                event.getOldValue();
        ((EndpointConfig) event.getTableView().getItems()
                .get(event.getTablePosition().getRow()))
                .setEndpointUrl(value);
            updateEndpoint(event.getRowValue().endpointPurpose.get(), value);
            if(value.equals("")){
                //endpointUrl.
            }
                    //.setStyle("-fx-background-color: rgba(255, 0, 0, 0.2);");
            //GridClientLastEncounter.refresh();
            GridClientLastEncounter.setItems(getEndPointsForDisplay());
        });



        endpointPurpose.setCellValueFactory(new PropertyValueFactory<EndpointConfig,String>("endpointPurpose"));
        GridClientLastEncounter.setItems(getEndPointsForDisplay());
        setTableEditable();
    }


    private void setTableEditable() {
        GridClientLastEncounter.setEditable(true);
        // allows the individual cells to be selected
        GridClientLastEncounter.getSelectionModel().cellSelectionEnabledProperty().set(true);
        // when character or numbers pressed it will start edit in editable
        // fields
        GridClientLastEncounter.setOnKeyPressed(event -> {
        if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
            editFocusedCell();
        } else if (event.getCode() == KeyCode.RIGHT ||
                event.getCode() == KeyCode.TAB) {
            GridClientLastEncounter.getSelectionModel().selectNext();
            event.consume();
        } else if (event.getCode() == KeyCode.LEFT) {
            // work around due to
            // TableView.getSelectionModel().selectPrevious() due to a bug
            // stopping it from working on
            // the first column in the last row of the table
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
        ObservableList<EndpointConfig> endpoints = FXCollections.observableArrayList();

        try{
           Connection dbConn=DBConnection.connect();

            String SQL = "Select * from endpoints WHERE void=0";
            ResultSet rs= dbConn.createStatement().executeQuery(SQL);
            while(rs.next()){
                EndpointConfig endpoint=new EndpointConfig();
                endpoint.endpointPurpose.set(rs.getString("endpointPurpose"));
                endpoint.endpointUrl.set(rs.getString("endpointUrl"));
                endpoints.add(endpoint);
            }
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("PSmart Exception Logger");
            alert.setHeaderText("Exception Handler");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            ex.printStackTrace();
        }
        return endpoints;
    }

    public void updateEndpoint(String purpose, String url) {
        try {
            Connection dbConn = DBConnection.connect();
            String converted = url.startsWith("http") ? url : "http://"+url;
            String sql = "UPDATE endpoints SET endpointUrl = '"+ url +"' WHERE endpointPurpose = '"+ purpose +"';";
            dbConn.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeWindow(JFXButton btn){
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }
}


