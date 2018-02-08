package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*BorderPane border = new BorderPane();
        HBox hbox = addApplicationHeader();
        HBox applicationStatus = addApplicationStatusHeader();


        border.setTop(hbox);
        //border.setTop(applicationStatus);

        border.setLeft(addActionPane());
        addStackPane(hbox);         // Add stack to HBox in top region

        Scene scene = new Scene(border);
        primaryStage.setScene(scene);
        primaryStage.setTitle("P-SMART");
        primaryStage.setMaximized(true);
        primaryStage.show();
        */

        Parent root = FXMLLoader.load(getClass().getResource("portal.fxml"));
        primaryStage.setTitle("P-SMART Middleware");
        primaryStage.setMaximized(true);
        Scene loginScene = new Scene(root);
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);






       /*Scene scene = new Scene(border);
       primaryStage.setScene(scene);
       primaryStage.setTitle("P-SMART");
       primaryStage.setMaximized(true);*/
        primaryStage.show();

    }

    public VBox addActionPane() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Text title = new Text("Action");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        Button readButton = new Button();
        readButton.setText("Read Card");
        readButton.setPrefSize(200, 50);

        Button updateButton = new Button();
        updateButton.setText("Update Card");
        updateButton.setPrefSize(200, 50);
        updateButton.setStyle("-fx-background-color: cornflowerblue");

        Button writeButton = new Button();
        writeButton.setText("New Card");
        writeButton.setPrefSize(200, 50);

        /*Hyperlink options[] = new Hyperlink[] {
                new Hyperlink("Sales"),
                new Hyperlink("Marketing"),
                new Hyperlink("Distribution"),
                new Hyperlink("Costs")};

        for (int i=0; i<4; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }*/

        vbox.getChildren().addAll(readButton, updateButton, writeButton);

        return vbox;
    }
    public HBox addApplicationHeader() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        /*Button buttonCurrent = new Button("Read Card");
        buttonCurrent.setPrefSize(100, 20);*/
        Label programTitle = new Label();
        programTitle.setText("P-SMART Middleware | Mbagathi Hospital");
        programTitle.setStyle("-fx-font-family: sans-serif;-fx-font-size: 32px;");
        programTitle.setTextFill(Color.web("#FFFFFF"));


        /*Button buttonProjected = new Button("Write Card");
        buttonProjected.setPrefSize(100, 20);*/

       /* Label readerStatus = new Label();
        readerStatus.setText("Reader Status: Offline");
        readerStatus.setPrefSize(100, 20);*/
        hbox.getChildren().addAll(programTitle);

        return hbox;
    }

    public HBox addApplicationStatusHeader() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336689;");

        /*Button buttonCurrent = new Button("Read Card");
        buttonCurrent.setPrefSize(100, 20);*/
        Label readerStatus = new Label();
        readerStatus.setText("Reader Status: Offline");
        readerStatus.setStyle("-fx-font-family: sans-serif;-fx-font-size: 24px;");
        readerStatus.setTextFill(Color.web("#FFFFFF"));


        /*Button buttonProjected = new Button("Write Card");
        buttonProjected.setPrefSize(100, 20);*/

       /* Label readerStatus = new Label();
        readerStatus.setText("Reader Status: Offline");
        readerStatus.setPrefSize(100, 20);*/
        hbox.getChildren().addAll(readerStatus);

        return hbox;
    }

    public void addStackPane(HBox hb) {
        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE,
                new Stop[]{
                        new Stop(0, Color.web("#4977A3")),
                        new Stop(0.5, Color.web("#B0C6DA")),
                        new Stop(1,Color.web("#9CB6CF")),}));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);

        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));

        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0)); // Center "?"

        hb.getChildren().add(stack);            // Add to HBox from Example 1-2
        HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
    }

    public static void main(String[] args) {
        launch(args);
    }
}
