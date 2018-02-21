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
            SmartCardUtils.displayOut(loggerWidget, "\nSubmit Code - IC");
            acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");

            //Select FF 02
            //acos3.selectFile(Acos3.INTERNAL_FILE.PERSONALIZATION_FILE);
            SmartCardUtils.displayOut(loggerWidget, "\nSelect File");
            acos3.selectFile(new byte[] {(byte)0xFF, (byte)0x02});

			/* Write to FF 02
		       This will create 3 User files, no Option registers and
		       Security Option registers defined, Personalization bit is not set */
            SmartCardUtils.displayOut(loggerWidget, "\nWrite Record");
            acos3.writeRecord((byte)0x00, (byte)0x00, new byte[] {(byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00});
            SmartCardUtils.displayOut(loggerWidget, "FF 02 is updated\n");

            // Select FF 04
            SmartCardUtils.displayOut(loggerWidget, "Select File");
            acos3.selectFile(new byte[] { (byte)0xFF, (byte)0x04 });

            //Send IC Code
            SmartCardUtils.displayOut(loggerWidget, "\nSubmit Code - IC");
            acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");

			/* Write to FF 04
		       Write to first record of FF 04 */
            SmartCardUtils.displayOut(loggerWidget, "\nWrite Record");
            acos3.writeRecord((byte)0x00, (byte)0x00, new byte[] { (byte)0x0A, (byte)0x03, (byte)0x00, (byte)0x00, (byte)0xAA, (byte)0x11, (byte)0x00 });
            SmartCardUtils.displayOut(loggerWidget, "User File AA 11 is defined");

            // Write to second record of FF 04
            SmartCardUtils.displayOut(loggerWidget, "\nWrite Record");
            acos3.writeRecord((byte)0x01, (byte)0x00, new byte[] { (byte)0x10, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0xBB, (byte)0x22, (byte)0x00 });
            SmartCardUtils.displayOut(loggerWidget, "User File BB 22 is defined");

            // write to third record of FF 04
            SmartCardUtils.displayOut(loggerWidget, "\nWrite Record");
            acos3.writeRecord((byte)0x02, (byte)0x00, new byte[] { (byte)0x20, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0xCC, (byte)0x33, (byte)0x00 });
            SmartCardUtils.displayOut(loggerWidget, "User File CC 33 is defined");

            /*radioButtonaa11.setSelected(true);
            textFieldValue.setText("");

            radioButtonaa11.setEnabled(true);
            radioButtonbb22.setEnabled(true);
            radioButtoncc33.setEnabled(true);
            buttonRead.setEnabled(true);
            buttonWrite.setEnabled(true);
            textFieldValue.setEnabled(true);*/

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

            // TODO: Dispaly in the grid:
            // grid.setText(Helper.byteArrayToString(data, data.length));
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
                SmartCardUtils.displayOut(loggerWidget, "Please key-in data to write." + "\r\n");
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

            //TODO: displayOut(0, 0, "\nWrite Record");
            acos3.writeRecord((byte)0x00, (byte)0x00, tmpArray);

            SmartCardUtils.displayOut(loggerWidget, "Data read from textbox is written to card" + "\r\n");
            //textToWrite.setText("");

        }
        catch (CardException exception)
        {
            SmartCardUtils.displayOut(loggerWidget, PcscProvider.GetScardErrMsg(exception) + "\r\n");
        }
        catch(Exception exception)
        {
            SmartCardUtils.displayOut(loggerWidget, "An error occured" + "\r\n");
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
