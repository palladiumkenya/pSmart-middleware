package pSmart;

import pSmart.userFiles.UserFile;
import com.jfoenix.controls.JFXComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javax.smartcardio.CardException;


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
                SmartCardUtils.displayOut(loggerWidget,"Card Present on reader");
            } else {
                SmartCardUtils.displayOut(loggerWidget,"No Card on reader");
            }
            SmartCardUtils.displayOut(loggerWidget, "\nSuccessful connection to " + rdrcon);

            //Check if card inserted is an ACOS Card
            if(!isAcos3()) {
                return;
            }
            if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI) {
                SmartCardUtils.displayOut(loggerWidget, "Chip Type: ACOS3 Combi");
            } else {
                SmartCardUtils.displayOut(loggerWidget, "Chip Type: ACOS3");
            }

        }
        catch (CardException exception)
        {
            SmartCardUtils.displayOut(loggerWidget, PcscProvider.GetScardErrMsg(exception) + "\r\n");
        }
        catch(Exception exception)
        {
            SmartCardUtils.displayOut(loggerWidget, "There was an error connecting to card" + "\r\n");
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

            SmartCardUtils.displayOut(loggerWidget, "\nClear Card");
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
            acos3.writeRecord((byte)0x00, (byte)0x00, new byte[] {(byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00});
            SmartCardUtils.displayOut(loggerWidget, "FF 02 is updated\n");

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
            SmartCardUtils.displayOut(loggerWidget, "Creating 4 user files");

            acos3.configurePersonalizationFile(optionRegister, securityOptionRegister, (byte)0x05);
            SmartCardUtils.displayOut(loggerWidget, "Successfully created 4 user files");

            acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
            acos3.selectFile(new byte[] { (byte)0xFF, (byte)0x04 });

			// Write to FF 04
		    //   Write to first record of FF 04 (AA 11)
            acos3.writeRecord((byte)0x00, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x0A, (byte)0x00, (byte)0x00, (byte)0xAA, (byte)0x11, (byte)0x00 });
            SmartCardUtils.displayOut(loggerWidget, "Demographics User File defined");

            // Write to second record of FF 04 (BB 22)
            acos3.writeRecord((byte)0x01, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x20, (byte)0x00, (byte)0x00, (byte)0xBB, (byte)0x22, (byte)0x00 });
            SmartCardUtils.displayOut(loggerWidget, "Patient Identifier User File defined");

            // write to third record of FF 04 (CC 33)
            acos3.writeRecord((byte)0x02, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x0A, (byte)0x00, (byte)0x00, (byte)0xCC, (byte)0x33, (byte)0x00 });
            SmartCardUtils.displayOut(loggerWidget, "HIV Test Data User File defined");

            // write to fourth record of FF 04 (DD 44)
            acos3.writeRecord((byte)0x03, (byte)0x00, new byte[] { (byte)0xFF, (byte)0x40, (byte)0x00, (byte)0x00, (byte)0xDD, (byte)0x44, (byte)0x00 });
            SmartCardUtils.displayOut(loggerWidget, "Card Details User File defined");



        }
        catch (CardException exception)
        {
            SmartCardUtils.displayOut(loggerWidget, PcscProvider.GetScardErrMsg(exception) + "\r\n");
        }
        catch(Exception exception)
        {
            SmartCardUtils.displayOut(loggerWidget, exception.getMessage().toString() + "\r\n");
        }
    }
    /**
     * Reads card content
     */
    public void readCard (UserFile userFile) {
        byte[] fileId = new byte[2];
        byte dataLen = 0x00;
        byte[] data;

        try {

            fileId = userFile.getFileDescriptor().getFileId();
            dataLen = (byte)userFile.getFileDescriptor().getExpLength();

            // Select user file
            acos3.selectFile(fileId);

            // read first record of user file selected
            //TODO: displayOut(0, 0, "\nRead Record");
            data = acos3.readRecord((byte)0x00, (byte)0x00, dataLen);
            String readMsg = Helper.byteArrayToString(data, data.length);
            SmartCardUtils.displayOut(loggerWidget, ">>Data from Smart Card: \n " + readMsg);

            /**
             * binary file still has a cap of 255 characters and thus may not work as expected
             * byte [] binarFileId = new byte[] { (byte)0xEE, (byte)0x55 } ;
            acos3.selectFile(binarFileId);
            byte [] readData = acos3.readBinary((byte) 0xFF, (byte) 0x00, (byte) 0xFF);

            SmartCardUtils.displayOut(loggerWidget, ">>Data from binary file: \n " +Helper.byteArrayToString(readData, 50));
*/
        }
        catch (CardException exception)
        {
            //SmartCardUtils.displayOut(loggerWidget, PcscProvider.GetScardErrMsg(exception) + "\r\n");
            exception.printStackTrace();

        }
        catch(Exception exception)
        {
            //SmartCardUtils.displayOut(loggerWidget, exception.getMessage().toString() + "\r\n");
            exception.printStackTrace();
        }
    }

    /**
     * Writes data to card
     * @param textToWrite
     */
    public void writeCard (UserFile userFile, String textToWrite) {
        byte[] fileId = new byte[2];
        int expLength = 0;
        String tmpStr = "";
        byte[] tmpArray = new byte[56];

        try
        {
            //Validate input
            if (textToWrite.equals("") || textToWrite.isEmpty())
            {
                SmartCardUtils.displayOut(loggerWidget, "Data to be written not found." + "\r\n");
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

            acos3.writeRecord((byte)0x00, (byte)0x00, tmpArray);

            SmartCardUtils.displayOut(loggerWidget, "Patient data successfully written to card" + "\r\n");

            // disabling this for now. binary file also has a cap of 255 characters

           /* SmartCardUtils.displayOut(loggerWidget, "\nWriting EE 55 Binary File");

            String dataToWrite = "Verify command is a command sent by a reader-side application" +
                    " to the security system on the card to allow it to check for a match to password type" +
                    " information stored on the card. It is used to allow the reader-side application" +
                    " to convince the card that it";
            byte [] binarFileId = new byte[] { (byte)0xEE, (byte)0x55 } ;
            acos3.selectFile(binarFileId);


            byte[] tmpBinaryArray = new byte[dataToWrite.length()];
            int i = 0;
            while (i < dataToWrite.length())
            {
                tmpBinaryArray[i] = dataToWrite.getBytes()[i];
                i++;
            }

            acos3.writeBinary((byte) 0, (byte) 0, tmpBinaryArray);

            SmartCardUtils.displayOut(loggerWidget, " Data successfully written to Binary file");
            */
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
        SmartCardUtils.displayOut(loggerWidget, event.getAsString(true));
    }

    @Override
    public void onReceiveCommand(ReaderEvents.TransmitApduEventArg event) {
        SmartCardUtils.displayOut(loggerWidget, event.getAsString(true) + "\r\n");
    }
}
