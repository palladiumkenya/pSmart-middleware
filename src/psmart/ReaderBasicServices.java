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

import device.Acr122u;
import device.Pcsc.PcscProvider;
import device.Pcsc.ReaderEvents;

import javax.smartcardio.CardException;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

import  controller.HomeController;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class ReaderBasicServices implements ReaderEvents.TransmitApduHandler {

    private String feedback=null;
    private Acr122u _acr122u;
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
    }

    // ***********************************************************************************************//

   // @Override
    public String[] InitialiseReader() {

        HomeController _homeController=new HomeController();
        
        String[] readerList=null;

        int index;

        try
        {
            if (_isConnected)
            {
                _acr122u.disconnect();
                _isConnected = false;
            }
          readerList = _acr122u.listTerminals();

        }
        catch (CardException ex)
        {
            this.feedback=PcscProvider.GetScardErrMsg(ex);
        }
        catch (Exception ex)
        {
            this.feedback=ex.getMessage().toString();
        }
        return readerList;

    }

    //@Override
    public void ConnectReader(ComboBox<String> cbo, Button btn) {

      //  if(hc.lblCardStatus.getText()=="offline"){showErrorMessage("Please select smartcard reader."); return; }

        try
        {
            if (cbo.getSelectionModel().getSelectedIndex() < 0)
            {
                this.showErrorMessage("Please select smartcard reader.");
                return;
            }

            if(_acr122u.isConnectionActive())
                _acr122u.disconnect();

            // Connect directly to the smart card reader
            _acr122u.connectDirect(cbo.getSelectionModel().getSelectedIndex(), true);

            this.WriteOperationLog ("\r\n Successfully connected to " + cbo.getSelectionModel().getSelectedItem());

            _isConnected = true;

            btn.setDisable(true);
        }
        catch (CardException ex)
        {
            WriteOperationLog(PcscProvider.GetScardErrMsg(ex));
            showErrorMessage(PcscProvider.GetScardErrMsg(ex));
        }
        catch (Exception ex)
        {
            WriteOperationLog(ex.getMessage());
            showErrorMessage(ex.getMessage());
        }
    }

   // @Override
    public void WriteOperationLog(String message) {

       // ta.appendText(message+ "\r\n");
    }

   // @Override
    public void onSendCommand(ReaderEvents.TransmitApduEventArg event) {

    }

    //@Override
    public void onReceiveCommand(ReaderEvents.TransmitApduEventArg event) {

    }

    //@Override
    public void showErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
