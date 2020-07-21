package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jsonvalidator.utils.PropertiesManager;
import models.Endpoint;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        List<Endpoint> endpoints = PropertiesManager.readJSONFile();
        List<String> endpointUrls = new ArrayList<>();
        for (Endpoint endpoint : endpoints) {
            if(endpoint.getEndpointUrl().equals("")) {
                endpointUrls.add(endpoint.getEndpointPurpose());
            }
        }
        if(endpointUrls.size() > 0) {
            Parent root = FXMLLoader.load(getClass().getResource("ApiEndpoints.fxml"));
            primaryStage.setTitle("P-SMART");
            Scene loginScene = new Scene(root);
            primaryStage.setScene(loginScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            primaryStage.setTitle("P-SMART");
            Scene loginScene = new Scene(root);
            primaryStage.setScene(loginScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
