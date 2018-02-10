package controller;

import externalApi.JsonRetrievalService;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javax.json.JsonArray;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ExternalApiController implements Initializable {
    public static final String BASE_URL = "http://biking.michael-simons.eu/api";

    private static final Logger logger = Logger.getLogger(ExternalApiController.class.getName());
    private ResourceBundle resources;

    public void sendDataToEmr(ActionEvent event) {
        JsonArray data = null;
        try {
            data = JsonRetrievalService.readDataFromEmr ("/bikes.json?all=true");

        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            e.getCause();
        }
    }


    //private final Preferences preferences = Preferences.userNodeForPackage(this.getClass());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;


    }



}
