package card.ACOS3;

import com.jfoenix.controls.JFXComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import psmart.SmartCardUtils;

import javax.smartcardio.CardException;

public class ACOS3ReadWriteFx implements Acos3CardReaderEvents.TransmitApduHandler {

    PcscReader pcscReader;
    Acos3 acos3;
    Button buttonInitialize, buttonConnect, buttonRead, buttonWrite, buttonReset;
    JFXComboBox comboBoxReaderName;
    String textToWrite;
    TextArea logWidget;
    private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;

    public ACOS3ReadWriteFx(
          Button btnInitialize, Button btnConnect, Button btnRead,
          Button btnWrite, Button btnReset, JFXComboBox cboReaderList
    ) {
        this.pcscReader = pcscReader;
        //this.acos3 = acos3;
        this.buttonInitialize = btnInitialize;
        this.buttonConnect = btnConnect;
        this.buttonRead = btnRead;
        this.buttonReset = btnReset;
        this.buttonWrite = btnWrite;
        this.comboBoxReaderName = cboReaderList;
        pcscReader = new PcscReader();
        readerInterface = new ReaderInterface();

        // Instantiate an event handler object
        readerInterface.setEventHandler(new Acos3CardReaderEvents());

        // Register the event handler implementation of this class
        readerInterface.getEventHandler().addEventListener(this);

    }

    public String getTextToWrite() {
        return textToWrite;
    }

    public void setTextToWrite(String textToWrite) {
        this.textToWrite = textToWrite;
    }

    @Override
    public void onSendCommand(Acos3CardReaderEvents.TransmitApduEventArg event) {
        SmartCardUtils.displayOut(logWidget, event.getAsString(true));
    }

    @Override
    public void onReceiveCommand(Acos3CardReaderEvents.TransmitApduEventArg event) {
        SmartCardUtils.displayOut(logWidget, event.getAsString(true));
    }

    /*@Override
    public void actionPerformed(ActionEvent e) {
        if(buttonInitialize == e.getSource())
        {
            String[] readerList = null;

            try
            {
                comboBoxReaderName.getItems().clear();

                readerList = readerInterface.listTerminals();
                if (readerList.length == 0)
                {
                    // write to log window "No PC/SC reader detected";
                    return;
                }



                for (int i = 0; i < readerList.length; i++)
                {
                    if (!readerList.equals(""))
                        comboBoxReaderName.getItems().addAll(readerList[i]);
                    else
                        break;
                }

                SmartCardUtils.displayOut(logWidget, "Initialize Success");

                comboBoxReaderName.getSelectionModel().selectFirst();
                // enable connect button: buttonConnect.setEnabled(true);
            }
            catch (Exception ex)
            {
                SmartCardUtils.displayOut(logWidget, "\nCannot find a smart card reader. ");
                // disable buttonConnect.setEnabled(false);
            }
        } // Init

        if(buttonConnect == e.getSource())
        {
            try
            {
                if(readerInterface.isConnectionActive())
                    readerInterface.disconnect();

                String rdrcon = (String)comboBoxReaderName.getValue();

                readerInterface.connect(rdrcon, "*");
                acos3 = new Acos3(readerInterface);

                SmartCardUtils.displayOut(logWidget, "\nSuccessful connection to "+ rdrcon);

                //Check if card inserted is an ACOS Card
                if(!isAcos3())
                    return;

                if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI) {
                    SmartCardUtils.displayOut(logWidget, "Chip Type: ACOS3 Combi");

                } else {
                    SmartCardUtils.displayOut(logWidget, "Chip Type: ACOS3");
                }
                // TODO: buttonFormat.setEnabled(true);

            }
            catch (CardException exception)
            {
                SmartCardUtils.displayOut(logWidget, PcscProvider.GetScardErrMsg(exception));
            }
            catch(Exception exception)
            {
                SmartCardUtils.displayOut(logWidget, exception.getMessage().toString());
            }
        } // Connect

        // TODO: check if you we really need this
        *//*if(buttonFormat == e.getSource())
        {
            formatCard();
        } *//*// Format

        if (buttonRead == e.getSource())
        {
            readCard();
        } // Read


        if(buttonWrite == e.getSource())
        {
            if(textToWrite.equals("") || textToWrite.isEmpty())
            {
                SmartCardUtils.displayOut(logWidget, "No data provided for writing");
                return;
            }

            writeCard();
        } // Write


        if(buttonReset == e.getSource())
        {
            try
            {
                //disconnect
                if (readerInterface.isConnectionActive())
                    readerInterface.disconnect();

                //TODO: initMenu();
                comboBoxReaderName.getItems().clear();
                comboBoxReaderName.getItems().add("Please select reader");
            }
            catch (CardException exception)
            {
                SmartCardUtils.displayOut(logWidget, PcscProvider.GetScardErrMsg(exception));
            }
            catch(Exception exception)
            {
                SmartCardUtils.displayOut(logWidget, exception.getMessage().toString() + "\r\n");
            }
        }  // Reset
        *//*

        if(radioButtonaa11 == e.getSource())
        {
            textToWrite.setText("");
        }  // rbaa11


        if(radioButtonbb22 == e.getSource())
        {
            textToWrite.setText("");
        }  // rbbb2


        if(radioButtoncc33 == e.getSource())
        {
            textToWrite.setText("");
        }  // rbcc33*//*

    }*/

    private void readCard()
    {
        byte[] fileId = new byte[2];
        byte dataLen = 0x00;
        byte[] data;

        try
        {

            fileId = new byte[] { (byte)0xAA, (byte)0x11 };
            dataLen = (byte) 0x0A;
            /*
            TODO: Get to understand what these mean
            if( radioButtonaa11.isSelected())
            {
                fileId = new byte[] { (byte)0xAA, (byte)0x11 };
                dataLen = (byte) 0x0A;
            }

            if( radioButtonbb22.isSelected())
            {
                fileId = new byte[] { (byte)0xBB, (byte)0x22 };
                dataLen = (byte) 0x10;
            }

            if( radioButtoncc33.isSelected())
            {
                fileId = new byte[] { (byte)0xCC, (byte)0x33 };
                dataLen = (byte) 0x20;
            }*/

            // Select user file
           //TODO: displayOut(0, 0, "\nSelect File");
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
            SmartCardUtils.displayOut(logWidget, PcscProvider.GetScardErrMsg(exception) + "\r\n");

        }
        catch(Exception exception)
        {
            SmartCardUtils.displayOut(logWidget, exception.getMessage().toString() + "\r\n");
        }
    }

    public void writeCard()
    {
        byte[] fileId = new byte[2];
        int expLength = 0;
        String tmpStr = "";
        byte[] tmpArray = new byte[56];

        try
        {
            //Validate input
            if (textToWrite.equals("") || textToWrite.isEmpty())
            {
                SmartCardUtils.displayOut(logWidget, "Please key-in data to write." + "\r\n");
                //TODO:textToWrite.requestFocus();
                return;
            }

            fileId = new byte[] { (byte)0xAA, (byte)0x11 };
            expLength = 10;
            /*
            TODO: get what these mean
            if( radioButtonaa11.isSelected())
            {
                fileId = new byte[] { (byte)0xAA, (byte)0x11 };
                expLength = 10;
            }

            if( radioButtonbb22.isSelected())
            {
                fileId = new byte[] { (byte)0xBB, (byte)0x22 };
                expLength = 16;
            }

            if( radioButtoncc33.isSelected())
            {
                fileId = new byte[] { (byte)0xCC, (byte)0x33 };
                expLength = 32;
            }
*/
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

            SmartCardUtils.displayOut(logWidget, "Data read from textbox is written to card" + "\r\n");
            //textToWrite.setText("");

        }
        catch (CardException exception)
        {
            SmartCardUtils.displayOut(logWidget, PcscProvider.GetScardErrMsg(exception) + "\r\n");
        }
        catch(Exception exception)
        {
            SmartCardUtils.displayOut(logWidget, "An error occured" + "\r\n");
        }
    }

    public boolean isAcos3()
    {
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
