package controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jsonvalidator.apiclient.APIClient;
import jsonvalidator.utils.SHRUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import view.Main;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {
    @FXML
    private JFXPasswordField password;

    @FXML
    private Label lblMsg;

    @FXML
    private JFXTextField username;

    private String displayName;

    private String facility;

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void validateLogin(ActionEvent actionEvent) {
        Boolean isAuthenticated = false;

        if (username.getText().isEmpty() == true || password.getText().isEmpty() == true ) {
            lblMsg.setText("Please supply both the username and the password!");
            lblMsg.setStyle("-fx-background-color: rgba(6,6,33,0.8); -fx-border-width: 3px;");

        } else {
            String purpose = "HTTP POST - Push Authentication credentials to EMR, get back an auth token";
            JSONObject authRequest = new JSONObject();
            authRequest.put("USERNAME", username.getText());
            authRequest.put("PASSWORD", password.getText());

            String response = APIClient.postObject(authRequest, purpose);

            JSONParser parser = new JSONParser();

            try{
                Object obj = parser.parse(response);
                JSONObject obj2 = (JSONObject)obj;
                displayName = obj2.get("DISPLAYNAME").toString();
                facility = obj2.get("FACILITY").toString();
                isAuthenticated = Boolean.valueOf(obj2.get("STATUS").toString());
            }catch(ParseException pe){
                System.out.println("position: " + pe.getPosition());
                System.out.println(pe);
            }
            if(isAuthenticated) {
                goToLandingPage(null);
            } else {
                lblMsg.setText("Login details supplied do not match any in the EMR!");
                lblMsg.setStyle("-fx-background-color: rgba(6,6,33,0.8); -fx-border-width: 3px;");
            }
        }
    }


    private void goToLandingPage (ActionEvent event) {
        try {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            Parent sceneMain = root.load();
            Stage landingPageStage = new Stage();
            landingPageStage.setTitle("P-SMART MIDDLEWARE");
            Scene landingPageScene = new Scene(sceneMain);
            landingPageStage.setScene(landingPageScene);
            landingPageStage.setResizable(false);
            HomeController controller = root.<HomeController>getController();
            controller.initVariable(this.displayName, this.facility);
            //landingPageStage.setMaximized(true);

            // Get current screen of the stage
            Screen screen = Screen.getPrimary();
            // Change stage properties
            Rectangle2D bounds = screen.getVisualBounds();
            landingPageStage.setX(bounds.getMinX());
            landingPageStage.setY(bounds.getMinY());
            landingPageStage.setWidth(bounds.getWidth());
            landingPageStage.setHeight(bounds.getHeight());
            Stage loginStage = (Stage) username.getScene().getWindow();
            loginStage.close();
            landingPageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
