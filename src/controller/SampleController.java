package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SampleController implements Initializable {
    public Label helloWorld;
    Stage prevStage;

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    public void sayHelloWorld(ActionEvent actionEvent) {
        helloWorld.setText("You just clicked me!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
