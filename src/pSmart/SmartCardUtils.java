package pSmart;

import pSmart.userFiles.UserFile;
import pSmart.userFiles.UserFileDescriptor;
import javafx.scene.control.TextArea;


import java.util.HashMap;
import java.util.Map;

public class SmartCardUtils {
    public static final String PATIENT_DEMOGRAPHICS_USER_FILE = "User file for patient demographics data";
    public static final String PATIENT_HIV_TEST_USER_FILE = "User file for hiv test details";
    public static final String PATIENT_IDENTIFIER_USER_FILE = "User file for patient identifiers";
    public static final String PATIENT_CARD_DETAILS_USER_FILE = "User file for smart card details";
    public static  final String PATIENT_DEMOGRAPHICS_USER_FILE_NAME = "AA 11";
    public static final String PATIENT_IDENTIFIER_USER_FILE_NAME = "BB 22";
    public static final String PATIENT_HIV_TEST_USER_FILE_NAME = "CC 33";
    public static final String PATIENT_CARD_DETAILS_USER_FILE_NAME = "DD 44";

    public static void displayOut(TextArea logWidget, String printText) {

            logWidget.appendText("<< " + printText + "\n");

    }

    public static UserFile getUserFile (String name) {
        if (name != null || !name.isEmpty()) {
            return getAllUserFiles().get(name);
        }
        return null ;
    }

    public static Map<String, UserFile> getAllUserFiles() {
        Map<String, UserFile> allUserFiles = new HashMap<>();
        allUserFiles.put(SmartCardUtils.PATIENT_DEMOGRAPHICS_USER_FILE_NAME,
                new UserFile(SmartCardUtils.PATIENT_DEMOGRAPHICS_USER_FILE_NAME, PATIENT_DEMOGRAPHICS_USER_FILE, new UserFileDescriptor(new byte[] { (byte)0xAA, (byte)0x11 },0x0A )));
        allUserFiles.put(SmartCardUtils.PATIENT_IDENTIFIER_USER_FILE_NAME,
                new UserFile(SmartCardUtils.PATIENT_IDENTIFIER_USER_FILE_NAME, PATIENT_IDENTIFIER_USER_FILE,  new UserFileDescriptor(new byte[] { (byte)0xBB, (byte)0x22 }, 0x10 )));

        allUserFiles.put(SmartCardUtils.PATIENT_HIV_TEST_USER_FILE_NAME,
                new UserFile(SmartCardUtils.PATIENT_HIV_TEST_USER_FILE_NAME, PATIENT_HIV_TEST_USER_FILE,  new UserFileDescriptor(new byte[] { (byte)0xCC, (byte)0x33 }, 0x20 )));

        allUserFiles.put(SmartCardUtils.PATIENT_CARD_DETAILS_USER_FILE_NAME,
                new UserFile(SmartCardUtils.PATIENT_CARD_DETAILS_USER_FILE_NAME, PATIENT_CARD_DETAILS_USER_FILE,  new UserFileDescriptor(new byte[] { (byte)0xDD, (byte)0x44 }, 0x30 )));

        return allUserFiles;


    }
}
