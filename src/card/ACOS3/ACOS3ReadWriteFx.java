package card.ACOS3;

import javafx.scene.control.TextArea;
import psmart.SmartCardUtils;

import javax.smartcardio.CardException;

public class ACOS3ReadWriteFx implements Acos3CardReaderEvents.TransmitApduHandler {
   TextArea logWidget;
   Acos3 acos3;


    private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;

    public ACOS3ReadWriteFx(TextArea logWidget, Acos3 acos3) {

        this.logWidget = logWidget;
        this.acos3 = acos3;
        readerInterface = new ReaderInterface();

        // Instantiate an event handler object
        readerInterface.setEventHandler(new Acos3CardReaderEvents());

        // Register the event handler implementation of this class
        readerInterface.getEventHandler().addEventListener(this);

    }



    @Override
    public void onSendCommand(Acos3CardReaderEvents.TransmitApduEventArg event) {
        SmartCardUtils.displayOut(logWidget, event.getAsString(true));
    }

    @Override
    public void onReceiveCommand(Acos3CardReaderEvents.TransmitApduEventArg event) {
        SmartCardUtils.displayOut(logWidget, event.getAsString(true));
    }


    public void readCard()
    {
        byte[] fileId = new byte[2];
        byte dataLen = 0x00;
        byte[] data;

        try {

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

    public void writeCard(String textToWrite)
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
