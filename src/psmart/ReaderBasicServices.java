package psmart;
        /*=====================================================================================================
         *
         *  Author          : s.osewe/A.Ojwang
         *
         *  File            : ReaderBasicServices.java
         *
         *  Copyright (C)   : KeHMIS Palladium .
         *
         *  Description     : This sample program demonstrates the basic device programming of ACR122U reader.
         *
         *  Date            : February 11, 2018
         *
         *  Revision Trailer: [Author] / [Date of modification] / [Details of Modifications done]
         *
         *
         * ====================================================================================================*/

import card.ACOS3.ACOS3ReadWriteFx;
import card.ACOS3.Acos3;
import card.ACOS3.Acos3CardReaderEvents;
import card.ACOS3.Helper;
import card.ACOS3.PcscReader;
import card.ACOS3.ReaderInterface;
import device.Acr122u;
import device.Pcsc.DeviceReaderEvents;
import device.Pcsc.PcscProvider;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import javax.smartcardio.CardException;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReaderBasicServices implements ActionListener, KeyListener, DeviceReaderEvents.TransmitApduHandler {

    private String feedback = null;
    private Acr122u _acr122u;
    PcscReader pcscReader;
    Acos3 acos3;
    private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;
    private boolean _isConnected = false;


    /************** GLOBAL VARIABLES****************/
    private final int BIT_RATE_106 = 0x00;
    private final int BIT_RATE_212 = 0x01;
    private final int BIT_RATE_424 = 0x02;
    private final int MODULATION_TYPE_ISO14443_MIFARE = 0x00;
    private final int MODULATION_TYPE_FELICA = 0x10;
    private final int MODULATION_TYPE_ACTIVE_MODE = 0x01;
    private final int MODULATION_TYPE_INNOVISION_JEWEL_TAG = 0x02;
    private final int VALID_INPUT_LENGTH = 2;

    MaskFormatter _maskFormatterTimeoutParameter = new MaskFormatter("HH");
    MaskFormatter _maskFormatterT1Duration = new MaskFormatter("HH");
    MaskFormatter _maskFormatterT2Duration = new MaskFormatter("HH");
    MaskFormatter _maskFormatterNumberOfRepetition = new MaskFormatter("HH");

    public ReaderBasicServices() throws ParseException {
        _acr122u = new Acr122u();
        pcscReader = new PcscReader();
        readerInterface = new ReaderInterface();
        // Instantiate an event handler object
        readerInterface.setEventHandler(new Acos3CardReaderEvents());

        // Register the event handler implementation of this class

    }
    /**
     * Initializes card reader
     *
     * @return
     */
    public List<String> InitialiseReader() {

        List<String> readerList = new ArrayList<>();


        int index;

        try {
            if (_isConnected) {
                _acr122u.disconnect();
                _isConnected = false;
            }/* else {
                _acr122u = new Acr122u();
            }*/
           //readerList = Arrays.asList(_acr122u.listTerminals());

            readerList.addAll(Arrays.asList("ACOS3","Mifare"));

        } catch (CardException ex) {
            this.feedback = PcscProvider.GetScardErrMsg(ex);
        } catch (Exception ex) {
            this.feedback = ex.getMessage().toString();
        }
        return readerList;

    }

    /**
     * Connects to a selected reader
     * @param cbo
     * @param btnConnectReader
     */
    public void ConnectReader(ComboBox<String> cbo, Button btnConnectReader, TextArea logger) {

        try {
            if (cbo.getSelectionModel().getSelectedIndex() < 0) {
                this.showErrorMessage("Please select smartcard reader.");
                SmartCardUtils.displayOut(logger, "An error occured during card initialization");
                return;
            }

            if (_acr122u.isConnectionActive()) {
                _acr122u.disconnect();
            }
            // Connect directly to the smart card reader
            _acr122u.connectDirect(cbo.getSelectionModel().getSelectedIndex(), true);

            SmartCardUtils.displayOut(logger, "\r\n Successfully connected to " + cbo.getSelectionModel().getSelectedItem());
            _isConnected = true;
            acos3 = new Acos3(readerInterface);
            btnConnectReader.setDisable(true);
        } catch (CardException ex) {
            SmartCardUtils.displayOut(logger, "\r\n Error connecting to card: " + PcscProvider.GetScardErrMsg(ex));
            showErrorMessage(PcscProvider.GetScardErrMsg(ex));
        } catch (Exception ex) {
            SmartCardUtils.displayOut(logger, "\r\n Error connecting to card: " + ex.getMessage());
            showErrorMessage(ex.getMessage());
        }
    }

    private void connectCard(ComboBox<String> comboBoxReaderName,  TextArea logWidget) {

            try
            {
                if(readerInterface.isConnectionActive())
                    readerInterface.disconnect();

                String rdrcon = (String)comboBoxReaderName.getValue();

                readerInterface.connect(rdrcon, "*");
                acos3 = new Acos3(readerInterface);

                SmartCardUtils.displayOut(logWidget, "\nSuccessful connection to "+ rdrcon);

                //Check if card inserted is an ACOS Card
                if(!isAcos3(logWidget))
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
                SmartCardUtils.displayOut(logWidget, card.ACOS3.PcscProvider.GetScardErrMsg(exception));
            }
            catch(Exception exception)
            {
                SmartCardUtils.displayOut(logWidget, exception.getMessage().toString());
            }
    }

    public void readCard(TextArea logWidget) {
        ACOS3ReadWriteFx cardReader = new ACOS3ReadWriteFx(logWidget, acos3);
        cardReader.readCard();
    }

    public void writeCard(String textToWrite, TextArea logWidget)
    {
        ACOS3ReadWriteFx cardReader = new ACOS3ReadWriteFx(logWidget, acos3);
        cardReader.writeCard(textToWrite);
    }

    public boolean isAcos3(TextArea logWidget)
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
    public void onSendCommand(DeviceReaderEvents.TransmitApduEventArg event) {

    }

    public void onReceiveCommand(DeviceReaderEvents.TransmitApduEventArg event) {

    }

    public void showErrorMessage(String message) {
        //JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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
}
