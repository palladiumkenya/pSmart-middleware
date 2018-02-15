package psmart;

import javafx.scene.control.TextArea;

public class SmartCardUtils {

    public static void displayOut(TextArea logWidget, String printText) {

            logWidget.appendText("<< " + printText + "\n");

    }
}
