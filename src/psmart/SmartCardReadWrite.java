package psmart;

import com.jfoenix.controls.JFXComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import psmart.pcscReader.Acr122u;
import psmart.pcscReader.PcscProvider;
import psmart.pcscReader.PcscReader;
import psmart.pcscReader.ReaderInterface;
import psmart.userFiles.UserFile;

import javax.smartcardio.CardException;


public class SmartCardReadWrite {
    Acos3 acos3;
    Acr122u _acr122u;
    ReaderInterface readerInterface;
    PcscReader pcscReader;
    TextArea loggerWidget;
    JFXComboBox readerList;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;
    private boolean _isConnected = false;

    public SmartCardReadWrite(TextArea loggerWidget, JFXComboBox readerList) {
        this.loggerWidget = loggerWidget;
        this.readerList = readerList;
        readerInterface = new ReaderInterface();
        pcscReader = new PcscReader();
        _acr122u = new Acr122u();

    }


    /**
     * Uses selected reader and establishes connection
     */
    public void connectReader(Button btnConnectReader) {
        try {
            if (readerList.getSelectionModel().getSelectedIndex() < 0) {
                SmartCardUtils.displayOut(loggerWidget, "An error occured during card initialization");
                return;
            }

            if (_acr122u.isConnectionActive()) {
                _acr122u.disconnect();
            }
            // Connect directly to the smart card reader
          //  _acr122u.connectDirect(readerList.getSelectionModel().getSelectedIndex(), true);

            SmartCardUtils.displayOut(loggerWidget, "\r\n Successfully connected to " + readerList.getSelectionModel().getSelectedItem());
            _isConnected = true;
            acos3 = new Acos3(readerInterface);
            btnConnectReader.setDisable(true);
        } catch (CardException ex) {
            SmartCardUtils.displayOut(loggerWidget, "\r\n Error connecting to card: " + PcscProvider.GetScardErrMsg(ex));

        } catch (Exception ex) {
            SmartCardUtils.displayOut(loggerWidget, "\r\n Error connecting to card: " + ex.getMessage());

        }
    }

    private void connectCard() {

        try
        {
            if(readerInterface.isConnectionActive())
                readerInterface.disconnect();

            String rdrcon = (String)readerList.getValue();

            readerInterface.connect(rdrcon, "*");
            acos3 = new Acos3(readerInterface);

            SmartCardUtils.displayOut(loggerWidget, "\nSuccessful connection to "+ rdrcon);

            //Check if card inserted is an ACOS Card
            if(!isAcos3(loggerWidget))
                return;

            if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI) {
                SmartCardUtils.displayOut(loggerWidget, "Chip Type: ACOS3 Combi");

            } else {
                SmartCardUtils.displayOut(loggerWidget, "Chip Type: ACOS3");
            }
            // TODO: buttonFormat.setEnabled(true);

        }
        catch (CardException exception)
        {
            SmartCardUtils.displayOut(loggerWidget, card.ACOS3.PcscProvider.GetScardErrMsg(exception));
        }
        catch(Exception exception)
        {
            SmartCardUtils.displayOut(loggerWidget, exception.getMessage().toString());
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

    public boolean isAcos3(TextArea logWidget) {
        currentChipType = readerInterface.getChipType();

        if(currentChipType == ReaderInterface.CHIP_TYPE.ERROR)
            return false;

        if(currentChipType != ReaderInterface.CHIP_TYPE.ACOS3)
        {
            SmartCardUtils.displayOut(logWidget, "Card not supported. Please use ACOS3 Card" + "\r\n");
            return false;
        }
        return true;
    }
}
