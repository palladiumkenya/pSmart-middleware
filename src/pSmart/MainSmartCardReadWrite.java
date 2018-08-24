package pSmart;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.Nullable;
import jsonvalidator.mapper.SHR;
import pSmart.userFiles.UserFile;
import com.jfoenix.controls.JFXComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javax.smartcardio.CardException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.toHexString;


public class MainSmartCardReadWrite implements ReaderEvents.TransmitApduHandler {
    PcscReader pcscReader;
    Acos3 acos3;
    TextArea loggerWidget;
    JFXComboBox cboReaderList;
    private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;
    private boolean _isConnected = false;

    public MainSmartCardReadWrite(TextArea loggerWidget, JFXComboBox readerList) {
        this.loggerWidget = loggerWidget;
        this.cboReaderList = readerList;
        readerInterface = new ReaderInterface();
        pcscReader = new PcscReader();
        //_acr122u = new Acr122u();
        // Instantiate an event handler object
        readerInterface.setEventHandler(new ReaderEvents());

        // Register the event handler implementation of this class
        readerInterface.getEventHandler().addEventListener(this);

    }

    /**
     * Initializes drop down for reader with list of scanned/attached card readers
     * @param btnConnect button to be enabled for successful initialization
     */
    public void initializeReader(Button btnConnect) {
        String[] readerList = null;

        try {
            cboReaderList.getItems().clear();

            readerList = readerInterface.listTerminals();
            if (readerList.length == 0)
            {
                SmartCardUtils.displayOut(loggerWidget, "No PC/SC reader detected");
                cboReaderList.getItems().add("No PC/SC reader detected");
                return;
            }


            for (int i = 0; i < readerList.length; i++)
            {
                if (!readerList.equals("")) {
                    cboReaderList.getItems().add(readerList[i]);
                } else {
                    break;
                }
            }


            SmartCardUtils.displayOut(loggerWidget, "Reader Initialization successful");

            cboReaderList.getSelectionModel().selectFirst();
            btnConnect.setDisable(false);

        }
        catch (Exception ex)
        {
            btnConnect.setDisable(true);
            SmartCardUtils.displayOut(loggerWidget, "Error: Cannot find a smart card reader.");
        }
    }

    public void writeArray(List<String> elements, UserFile userFile) {
        for(int i=0; i < elements.size();i++) {
            String data = elements.get(i);
            Byte aByte = getByte(i);
            writeCard(userFile, data, aByte);
        }
    }

    //TODO: Add more bytes to the map
    public Byte getByte (Integer key) {
        Map<Integer, Byte> recordUserFiles = new HashMap<>();
        recordUserFiles.put(0, (byte)0x00);
        recordUserFiles.put(1, (byte)0x01);
        recordUserFiles.put(2, (byte)0x02);
        recordUserFiles.put(3, (byte)0x03);
        recordUserFiles.put(4, (byte)0x04);
        recordUserFiles.put(5, (byte)0x05);
        recordUserFiles.put(6, (byte)0x06);
        recordUserFiles.put(7, (byte)0x07);
        recordUserFiles.put(8, (byte)0x08);
        recordUserFiles.put(9, (byte)0x09);
        recordUserFiles.put(10, (byte)0x0A);
        return recordUserFiles.get(key);
    }

    public String readArray(UserFile userFile) {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<255;i++) {
            String readData = readCard(userFile, getByte(i));
            if(readData.startsWith("ÿÿÿ")){
                break;
            }
            builder.append(readData).append(",");
        }
        if(builder.length() > 0) {
            builder.toString().substring(0, builder.toString().length() - 1);
            builder.deleteCharAt(builder.length()-1);
        } else {
            builder.append("");
        }
        return builder.toString();
    }

    /**
     * Uses selected reader and establishes connection
     */
    public void connectReader(Button btnConnectReader) {
        try
        {
            if(readerInterface.isConnectionActive())
                readerInterface.disconnect();

            int rdrcon = cboReaderList.getSelectionModel().getSelectedIndex();

            readerInterface.connect(rdrcon, "*");
            acos3 = new Acos3(readerInterface);

            if (readerInterface._activeTerminal.isCardPresent()) {
                SmartCardUtils.displayOut(loggerWidget,"Card detected on reader");
            } else {
                SmartCardUtils.displayOut(loggerWidget,"Please insert a card in reader!");
            }

            //Check if card inserted is an ACOS Card
            if(!isAcos3()) {
                return;
            }
            if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI) {
                //SmartCardUtils.displayOut(loggerWidget, "Chip Type: ACOS3 Combi");
            } else {
                //SmartCardUtils.displayOut(loggerWidget, "Chip Type: ACOS3");
            }

        }
        catch (CardException exception)
        {
            SmartCardUtils.displayOut(loggerWidget, PcscProvider.GetScardErrMsg(exception));
        }
        catch(Exception exception)
        {
            SmartCardUtils.displayOut(loggerWidget, "There was an error connecting to card");
            exception.printStackTrace();
        }
    }

    /**
     * format card
     */
    public void formatCard()
    {
        try
        {
            //Send IC Code
            //SmartCardUtils.displayOut(loggerWidget, "\nSubmit Code - IC");
            acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
            acos3.clearCard();
            acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
            //Select FF 02
            //acos3.selectFile(Acos3.INTERNAL_FILE.PERSONALIZATION_FILE);
            //SmartCardUtils.displayOut(loggerWidget, "\nSelect File");
            acos3.selectFile(new byte[] {(byte)0xFF, (byte)0x02});

			/* Write to FF 02
		       This will create 6 User files, no Option registers and
		       Security Option registers defined, Personalization bit is not set */
            //SmartCardUtils.displayOut(loggerWidget, "\nWrite Record");
            acos3.writeRecord((byte)0x00, (byte)0x00, new byte[] {(byte)0x00, (byte)0x00, (byte)0x09, (byte)0x00});
            //SmartCardUtils.displayOut(loggerWidget, "FF 02 is updated\n");

            // Select FF 04
            //SmartCardUtils.displayOut(loggerWidget, "Select File");
            acos3.selectFile(new byte[] { (byte)0xFF, (byte)0x04 });

            //Send IC Code
            //SmartCardUtils.displayOut(loggerWidget, "\nSubmit Code - IC");
            acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");

            //Set Option Registers and Security Option Registers
            //See Personalization File of ACOS3 Reference Manual for more information
            OptionRegister optionRegister = new OptionRegister();

            optionRegister.setRequireMutualAuthenticationOnInquireAccount(false);
            optionRegister.setRequireMutualAuthenticationOnAccountTransaction(false);
            optionRegister.setEnableRevokeDebitCommand(false);
            optionRegister.setEnableChangePinCommand(false);
            optionRegister.setEnableDebitMac(false);
            optionRegister.setRequirePinDuringDebit(false);
            optionRegister.setEnableAccount(false);


            SecurityOptionRegister securityOptionRegister = new SecurityOptionRegister();

            securityOptionRegister.setIssuerCode(false);
            securityOptionRegister.setPin(false);
            securityOptionRegister.setAccessCondition5(false);
            securityOptionRegister.setAccessCondition4(false);
            securityOptionRegister.setAccessCondition3(false);
            securityOptionRegister.setAccessCondition2(false);
            securityOptionRegister.setAccessCondition1(false);

            //Write record to Personalization File
            //Number of File = 3
            //Select Personalization File
            //SmartCardUtils.displayOut(loggerWidget, "Initializing 9 user files");

            acos3.configurePersonalizationFile(optionRegister, securityOptionRegister, (byte)0x09);
            //SmartCardUtils.displayOut(loggerWidget, "Successfully initialized 7 user files");

            acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
            acos3.selectFile(new byte[] { (byte)0xFF, (byte)0x04 });

			// Write to FF 04
		    //   Write to first record of FF 04 (AA 00)
            acos3.writeRecord((byte)0x00, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x0A, (byte)0x00, (byte)0x00, (byte)0xAA, (byte)0x00, (byte)0x00 });
            //SmartCardUtils.displayOut(loggerWidget, "User file for Card details defined");

            // Write to second record of FF 04 (BB 22)
            acos3.writeRecord((byte)0x01, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x20, (byte)0x00, (byte)0x00, (byte)0xBB, (byte)0x00, (byte)0x00 });
            //SmartCardUtils.displayOut(loggerWidget, "User file for Immunization defined");

            // write to third record of FF 04 (CC 33)
            acos3.writeRecord((byte)0x02, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x0A, (byte)0x00, (byte)0x00, (byte)0xCC, (byte)0x00, (byte)0x00 });
            //SmartCardUtils.displayOut(loggerWidget, "User file for hiv tests defined");

            // write to fourth record of FF 04 (DD 44)
            acos3.writeRecord((byte)0x03, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x40, (byte)0x00, (byte)0x00, (byte)0xDD, (byte)0x00, (byte)0x00 });
            //SmartCardUtils.displayOut(loggerWidget, "User file for external identifiers defined");

            // write to fifth record of FF 04 (DD 44)
            acos3.writeRecord((byte)0x04, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x40, (byte)0x00, (byte)0x00, (byte)0xDD, (byte)0x11, (byte)0x00 });
            //SmartCardUtils.displayOut(loggerWidget, "User file for internal identifiers defined");

            // write to sixth record of FF 04 (DD 44)
            acos3.writeRecord((byte)0x05, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x40, (byte)0x00, (byte)0x00, (byte)0xDD, (byte)0x22, (byte)0x00 });
            //SmartCardUtils.displayOut(loggerWidget, "User file for patient names defined");

            // write to seventh record of FF 04 (DD 44)
            acos3.writeRecord((byte)0x06, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x40, (byte)0x00, (byte)0x00, (byte)0xDD, (byte)0x33, (byte)0x00 });
            //SmartCardUtils.displayOut(loggerWidget, "User file for address defined");

            // write to eighth record of FF 04 (EE 00)
            acos3.writeRecord((byte)0x07, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x40, (byte)0x00, (byte)0x00, (byte)0xEE, (byte)0x00, (byte)0x00 });
            //SmartCardUtils.displayOut(loggerWidget, "User file for mother details defined");

            // write to Nineth record of FF 04 (EE 11)
            acos3.writeRecord((byte)0x08, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x40, (byte)0x00, (byte)0x00, (byte)0xEE, (byte)0x11, (byte)0x00 });
            //SmartCardUtils.displayOut(loggerWidget, "User file for mother identifiers defined");

            SmartCardUtils.displayOut(loggerWidget, "Card formatted successfully");
        }
        catch (CardException exception)
        {
            SmartCardUtils.displayOut(loggerWidget, PcscProvider.GetScardErrMsg(exception));
        }
        catch(Exception exception)
        {
            SmartCardUtils.displayOut(loggerWidget, exception.getMessage().toString());
        }
    }
    /**
     * Reads card content
     */
    public String readCard(UserFile userFile, byte recordNumber) {
        byte[] fileId = new byte[2];
        byte dataLen = 0x00;
        byte[] data;
        String readMsg = "";

        try {

            fileId = userFile.getFileDescriptor().getFileId();
            dataLen = (byte)userFile.getFileDescriptor().getExpLength();

            // Select user file
            acos3.selectFile(fileId);

            // read first record of user file selected
            //TODO: displayOut(0, 0, "\nRead Record");
            data = acos3.readRecord(recordNumber, (byte)0x00, dataLen);
            if(data.length > 0) readMsg = Helper.byteArrayToString(data, data.length);
        }
        catch(Exception exception)
        {
            //SmartCardUtils.displayOut(loggerWidget, exception.getMessage().toString());
            System.out.println("here it is...");
            exception.printStackTrace();
        }
        return readMsg;
    }

    public String getCardSerial() {
        String cardSerialStr = "";
        try {
            byte[]  cardSerialBytes = acos3.getCardInfo(Acos3.CARD_INFO_TYPE.CARD_SERIAL);
            cardSerialStr = bytearray2intarray(cardSerialBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cardSerialStr;
    }

    public static String toHexString(byte[] array) {
        String bufferString = "";

        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                String hexChar = Integer.toHexString(array[i] & 0xFF);
                if (hexChar.length() == 1) {
                    hexChar = "0" + hexChar;
                }
                bufferString += hexChar.toUpperCase(Locale.US) + " ";
            }
        }
        return bufferString;
    }

    public String bytearray2intarray(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte byt : bytes) {
            //builder.append(iterative(toHexString(new byte[]{byt})));
            builder.append((Integer.parseInt(toHexString(new byte[]{byt}).trim(), 16)));
        }
        return builder.toString();
    }

    /**
     * Writes data to card
     * @param textToWrite
     */
    public void writeBinaryDataToCard (String textToWrite) {
        byte[] fileId = new byte[2];
        byte[] dataToWrite;
        byte hiByte, loByte;



        try
        {
            //Validate input
            if (textToWrite.equals("") || textToWrite.isEmpty())
            {
                SmartCardUtils.displayOut(loggerWidget, "Data to be written not found.");
                return;
            }

            fileId = new byte[] { (byte)0xDD, (byte)0x55 } ;//userFile.getFileDescriptor().getFileId();
            hiByte = (byte)0x00;
            loByte = (byte)0x7D0;
            String strLength = String.valueOf(textToWrite.length());
            dataToWrite = new byte[(Integer)Integer.parseInt(strLength, 16)];

            for(int i = 0; i < textToWrite.length(); i++) {
                dataToWrite[i] = (byte) textToWrite.charAt(i);
            }
            // Select user file
            //TODO:displayOut(0, 0, "\nSelect File");
            acos3.selectFile(fileId);
            SmartCardUtils.displayOut(loggerWidget, "Binary file (DD 55) selected");
            acos3.writeBinary((byte) hiByte, (byte) loByte, dataToWrite);

            SmartCardUtils.displayOut(loggerWidget, "Patient data successfully written to binary card");

        }
        catch (CardException exception)
        {
            SmartCardUtils.displayOut(loggerWidget, PcscProvider.GetScardErrMsg(exception));
        }
        catch(Exception exception)
        {
            SmartCardUtils.displayOut(loggerWidget, "An error occured");
            exception.printStackTrace();
        }
    }
    /**
     * Writes data to card
     * @param textToWrite
     * @param recordNumber
     */
    public void writeCard(UserFile userFile, String textToWrite, byte recordNumber) {
        byte[] fileId = new byte[2];
        int expLength = 0;
        String tmpStr = "";
        byte[] tmpArray = new byte[56];



        try
        {
            //Validate input
            if (textToWrite.equals("") || textToWrite.isEmpty())
            {
                SmartCardUtils.displayOut(loggerWidget, "Data to be written not found.");
                return;
            }

            fileId = userFile.getFileDescriptor().getFileId();
            expLength = userFile.getFileDescriptor().getExpLength();

            // Select user file
            //TODO:displayOut(0, 0, "\nSelect File");
            acos3.selectFile(fileId);

            tmpStr = textToWrite;
            tmpArray = new byte[expLength];
            int indx = 0;
            while (indx < textToWrite.length())
            {
                tmpArray[indx] = tmpStr.getBytes()[indx];
                indx++;
            }
            while (indx < expLength)
            {
                tmpArray[indx] = (byte)0x00;
                indx++;
            }

            acos3.writeRecord(recordNumber, (byte)0x00, tmpArray);

            //SmartCardUtils.displayOut(loggerWidget, userFile.getDescription() + " successfully written to card");
        }
        catch (CardException exception)
        {
            SmartCardUtils.displayOut(loggerWidget, PcscProvider.GetScardErrMsg(exception) + "\r\n");
        }
        catch(Exception exception)
        {
            SmartCardUtils.displayOut(loggerWidget, "An error occured" + "\r\n");
            exception.printStackTrace();
        }
    }

    public boolean isAcos3() {
        currentChipType = readerInterface.getChipType();

        if(currentChipType == ReaderInterface.CHIP_TYPE.ERROR)
            return false;

        if(currentChipType != ReaderInterface.CHIP_TYPE.ACOS3)
        {
            SmartCardUtils.displayOut(loggerWidget, "Card not supported. Please use ACOS3 Card" + "\r\n");
            return false;
        }
        return true;
    }

    @Override
    public void onSendCommand(ReaderEvents.TransmitApduEventArg event) {
        //SmartCardUtils.displayOut(loggerWidget, event.getAsString(true));
    }

    @Override
    public void onReceiveCommand(ReaderEvents.TransmitApduEventArg event) {
        //SmartCardUtils.displayOut(loggerWidget, event.getAsString(true) + "\r\n");
    }


}
