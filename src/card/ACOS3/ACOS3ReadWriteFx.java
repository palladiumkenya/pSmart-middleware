package card.ACOS3;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javax.smartcardio.CardException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ACOS3ReadWriteFx implements ActionListener, KeyListener, ReaderEvents.TransmitApduHandler {

    PcscReader pcscReader;
    Acos3 acos3;
    JFXButton buttonInitialize, buttonConnect, buttonRead, buttonWrite, buttonReset;
    JFXComboBox comboBoxReaderName;
    String textToWrite;
    private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;

    public ACOS3ReadWriteFx(
          PcscReader pcscReader, Acos3 acos3, JFXButton btnInitialize, JFXButton btnConnect, JFXButton btnRead,
          JFXButton btnWrite, JFXButton btnReset, JFXComboBox cboReaderList
    ) {
        this.pcscReader = pcscReader;
        this.acos3 = acos3;
        this.buttonInitialize = btnInitialize;
        this.buttonConnect = btnConnect;
        this.buttonRead = btnRead;
        this.buttonReset = btnReset;
        this.buttonWrite = btnWrite;
        this.comboBoxReaderName = cboReaderList;
        pcscReader = new PcscReader();
        readerInterface = new ReaderInterface();

        // Instantiate an event handler object
        readerInterface.setEventHandler(new ReaderEvents());

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
    public void onSendCommand(ReaderEvents.TransmitApduEventArg event) {
        // display on log window
        // text to write: event.getAsString(true);
    }

    @Override
    public void onReceiveCommand(ReaderEvents.TransmitApduEventArg event) {
        // display on log window
        // text to write: event.getAsString(true);
    }

    @Override
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

                //TODO: print to log window "Initialize success";

                comboBoxReaderName.getSelectionModel().selectFirst();
                // enable connect button: buttonConnect.setEnabled(true);
            }
            catch (Exception ex)
            {
                // disable buttonConnect.setEnabled(false);
                // log error on log window. displayOut(0, 0, "Cannot find a smart card reader.");
                //JOptionPane.showMessageDialog(null, "Cannot find a smart card reader.", "Error", JOptionPane.ERROR_MESSAGE);
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

                // log: displayOut(0, 0, "\nSuccessful connection to " + rdrcon);

                //Check if card inserted is an ACOS Card
                if(!isAcos3())
                    return;

                if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI) {
                    //TODO: displayOut(0, 0, "Chip Type: ACOS3 Combi");

                } else {
                   // TODO: displayOut(0, 0, "Chip Type: ACOS3");
                }
                // TODO: buttonFormat.setEnabled(true);

            }
            catch (CardException exception)
            {
                //TODO: displayOut(0, 0, PcscProvider.GetScardErrMsg(exception) + "\r\n");
                //TODO: JOptionPane.showMessageDialog(null,PcscProvider.GetScardErrMsg(exception), "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception exception)
            {
                //TODO:displayOut(0, 0, exception.getMessage().toString() + "\r\n");
                //TODO:JOptionPane.showMessageDialog(null,exception.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } // Connect

        // TODO: check if you we really need this
        /*if(buttonFormat == e.getSource())
        {
            formatCard();
        } */// Format

        if (buttonRead == e.getSource())
        {
            readCard();
        } // Read


        if(buttonWrite == e.getSource())
        {
            if(textToWrite.equals("") || textToWrite.isEmpty())
            {
                //TODO: JOptionPane.showMessageDialog(null, "Please key-in the data to write", "Error", JOptionPane.ERROR_MESSAGE);
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
                // TODO: displayOut(0, 0, PcscProvider.GetScardErrMsg(exception) + "\r\n");
                //TODO: JOptionPane.showMessageDialog(null,PcscProvider.GetScardErrMsg(exception), "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception exception)
            {
                //TODO: displayOut(0, 0, exception.getMessage().toString() + "\r\n");
                //TODO: JOptionPane.showMessageDialog(null,exception.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }  // Reset
        /*

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
        }  // rbcc33*/

    }

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
            // TODO: displayOut(0, 0, PcscProvider.GetScardErrMsg(exception) + "\r\n");
            //TODO:JOptionPane.showMessageDialog(null,PcscProvider.GetScardErrMsg(exception), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception exception)
        {
            //TODO: displayOut(0, 0, exception.getMessage().toString() + "\r\n");
            //TODO: JOptionPane.showMessageDialog(null,exception.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeCard()
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
                //TODO:JOptionPane.showMessageDialog(this, "Please key-in data to write.", "Error", JOptionPane.ERROR_MESSAGE);
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

            //TODO: displayOut(0, 0, "Data read from textbox is written to card");
            //textToWrite.setText("");

        }
        catch (CardException exception)
        {
            //TODO: displayOut(0, 0, PcscProvider.GetScardErrMsg(exception) + "\r\n");
            //TODO: JOptionPane.showMessageDialog(null,PcscProvider.GetScardErrMsg(exception), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception exception)
        {
            //TODO: displayOut(0, 0, exception.getMessage().toString() + "\r\n");
            //TODO: JOptionPane.showMessageDialog(null,exception.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    public boolean isAcos3()
    {
        currentChipType = readerInterface.getChipType();

        if(currentChipType == ReaderInterface.CHIP_TYPE.ERROR)
            return false;

        if(currentChipType != ReaderInterface.CHIP_TYPE.ACOS3)
        {
            // Log: JOptionPane.showMessageDialog(null,"Card not supported. Please use ACOS3 Card", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
