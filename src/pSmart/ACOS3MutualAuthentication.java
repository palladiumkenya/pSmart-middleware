package pSmart;
/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              ACOS3MutualAuthentication.java

  Description:       This sample program outlines the steps on how to
                     use the ACOS card for the Mutual Authentication
                     process using the PC/SC platform.
                    
  Author:            Donn Johnson A. Fabian

  Date:              October 11, 2012

  Revision Trail:   (Date/Author/Description)
  					 04-15-2010 / M.J.E.Castillo / Bug Fixes

======================================================================*/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
 
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

import javax.smartcardio.CardException;

public class ACOS3MutualAuthentication extends JFrame implements ReaderEvents.TransmitApduHandler, ActionListener, KeyListener{

	//int retCode;
	static String VALIDCHARS = "0123456789abcdefABCDEF";
    private static String algorithm = "DES";
 
	byte [] cKey = new byte[16]; 
	byte [] tKey = new byte[16]; 
	byte [] tmpArray = new byte[32];
    byte [] CRnd = new byte[8];    			// Card random number
    byte [] TRnd = new byte[8];    			// Terminal random number
	byte [] ReverseKey = new byte[16];      	// Reverse of Terminal Key
    byte [] SessionKey = new byte[16];
	byte [] tmpResult = new byte[16];
	String tmpStr;
	
	private PcscReader pcscReader;
    private Acos3 acos3;
		
	private ButtonGroup buttonGroupSecure;
	private JTextArea textAreaMessage;
	private JButton buttonClear, buttonDisconnect, buttonQuit, buttonExecuteMutualAuthentication, buttonInitialize, buttonConnect, buttonFormat;
	private JComboBox comboBoxReader;
	private JScrollPane jScrollPane1;
	private JLabel labelCardKey, labelSelect, labelResult, labelTerminalKey;
	private JRadioButton radioButton3DES, radioButtonDES;
	private JTextField textFieldCardKey, textFieldTerminalKey;
	
	private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;
    private OptionRegister optionRegister;
    private SecurityOptionRegister securityOptionRegister;
    private JPanel panelSecurityOption;
    private JPanel panelKeyTemplate;

    public ACOS3MutualAuthentication() 
    {    	
    	this.setTitle("ACOS3 Mutual Authentication");
        initComponents();
        initMenu();
        
        pcscReader = new PcscReader();
        readerInterface = new ReaderInterface();
        
		// Instantiate an event handler object 
        readerInterface.setEventHandler(new ReaderEvents());
		
		// Register the event handler implementation of this class
        readerInterface.getEventHandler().addEventListener(this);
    }

    private void initComponents() 
    {
		//GUI Variables
    	setSize(640,377);  
    	setResizable(false);
   	  	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	
    	//sets the location of the form at the center of screen
   		Dimension dim = getToolkit().getScreenSize();
   		Rectangle abounds = getBounds();
   		setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
   		requestFocus();
   		
	    String[] rdrNameDef = {"Please select reader                   "};
	    
		buttonGroupSecure = new ButtonGroup();
	    labelSelect = new JLabel();
	    labelSelect.setBounds(10, 11, 80, 14);
	    buttonInitialize = new JButton();
	    buttonInitialize.setBounds(172, 70, 125, 23);
	    buttonConnect = new JButton();
	    buttonConnect.setBounds(172, 104, 125, 23);
	    buttonExecuteMutualAuthentication = new JButton();
	    buttonExecuteMutualAuthentication.setBounds(172, 276, 125, 46);
	    jScrollPane1 = new JScrollPane();
	    jScrollPane1.setBounds(325, 31, 299, 256);
	    textAreaMessage = new JTextArea();
	    textAreaMessage.setWrapStyleWord(true);
	    textAreaMessage.setLineWrap(true);
	    buttonClear = new JButton();
	    buttonClear.setBounds(325, 298, 90, 23);
	    buttonDisconnect = new JButton();
	    buttonDisconnect.setBounds(430, 298, 90, 23);
	    buttonQuit = new JButton();
	    buttonQuit.setBounds(534, 298, 90, 23);
	    labelResult = new JLabel();
	    labelResult.setBounds(325, 11, 67, 14);
	    comboBoxReader = new JComboBox(rdrNameDef);
	    comboBoxReader.setBounds(10, 31, 287, 20);
		comboBoxReader.setSelectedIndex(0);
	    
	    labelSelect.setText("Select Reader");

	    buttonInitialize.setText("Initialize");
	    buttonConnect.setText("Connect"); 
	    buttonConnect.setText("Connect");
        buttonExecuteMutualAuthentication.setText("<html>" + "Execute Mutual Authentication".replaceAll("\\n", "<br>") + "</html>");

        textAreaMessage.setColumns(20);
        textAreaMessage.setRows(5);
        jScrollPane1.setViewportView(textAreaMessage);

        buttonClear.setText("Clear");
        buttonDisconnect.setText("Reset");
        labelResult.setText("APDU Logs");   
        buttonQuit.setText("Quit");
        
        panelSecurityOption = new JPanel();
        panelSecurityOption.setBounds(10, 62, 145, 69);
        panelSecurityOption.setBorder(new TitledBorder(null, "Security Option", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelSecurityOption.setLayout(new GridLayout(2, 1, 0, 0));
        radioButtonDES = new JRadioButton();
        panelSecurityOption.add(radioButtonDES);
        
                buttonGroupSecure.add(radioButtonDES);
                radioButtonDES.setText("DES");
                radioButtonDES.addActionListener(this);
        radioButton3DES = new JRadioButton();
        panelSecurityOption.add(radioButton3DES);
        
                buttonGroupSecure.add(radioButton3DES);
                radioButton3DES.setText("3DES");
                radioButton3DES.addActionListener(this);
        
                
                
                textAreaMessage.setForeground(Color.black);
                textAreaMessage.setEditable(false);
                getContentPane().setLayout(null);
                getContentPane().add(buttonExecuteMutualAuthentication);
                getContentPane().add(buttonInitialize);
                getContentPane().add(buttonConnect);
                getContentPane().add(buttonClear);
                getContentPane().add(buttonDisconnect);
                getContentPane().add(buttonQuit);
                getContentPane().add(jScrollPane1);
                getContentPane().add(labelResult);
                getContentPane().add(labelSelect);
                getContentPane().add(comboBoxReader);
                getContentPane().add(panelSecurityOption);
                
                panelKeyTemplate = new JPanel();
                panelKeyTemplate.setBorder(new TitledBorder(null, "Key Template", TitledBorder.LEADING, TitledBorder.TOP, null, null));
                panelKeyTemplate.setBounds(10, 142, 299, 123);
                getContentPane().add(panelKeyTemplate);
                panelKeyTemplate.setLayout(null);
                labelCardKey = new JLabel();
                labelCardKey.setHorizontalAlignment(SwingConstants.RIGHT);
                labelCardKey.setBounds(10, 30, 98, 14);
                panelKeyTemplate.add(labelCardKey);
                labelCardKey.setText("Card Key");
                labelTerminalKey = new JLabel();
                labelTerminalKey.setHorizontalAlignment(SwingConstants.RIGHT);
                labelTerminalKey.setBounds(10, 55, 98, 14);
                panelKeyTemplate.add(labelTerminalKey);
                labelTerminalKey.setText("Terminal Key");
                textFieldCardKey = new JTextField();
                textFieldCardKey.setBounds(118, 27, 171, 20);
                panelKeyTemplate.add(textFieldCardKey);
                textFieldTerminalKey = new JTextField();
                textFieldTerminalKey.setBounds(118, 52, 171, 20);
                panelKeyTemplate.add(textFieldTerminalKey);
                buttonFormat = new JButton();
                buttonFormat.setBounds(164, 83, 125, 23);
                panelKeyTemplate.add(buttonFormat);
                
                buttonFormat.setText("Format");
                buttonFormat.addActionListener(this);
                textFieldTerminalKey.addKeyListener(this);
                textFieldCardKey.addKeyListener(this);
                textFieldTerminalKey.setTransferHandler(null);
                textFieldCardKey.setTransferHandler(null);
        
        buttonInitialize.addActionListener(this);
        buttonConnect.addActionListener(this);
        buttonDisconnect.addActionListener(this);
        buttonClear.addActionListener(this);
        buttonQuit.addActionListener(this);
        buttonExecuteMutualAuthentication.addActionListener(this);
        
    }

	public void actionPerformed(ActionEvent e) 
	{
		if (buttonInitialize == e.getSource())
		{
			String[] readerList = null;
			
		    try
		    {
				comboBoxReader.removeAllItems();
				
			    readerList = readerInterface.listTerminals();					
				if (readerList.length == 0)
				{
					displayOut(0, 0, "No PC/SC reader detected");
					return;
				}				
					
				for (int i = 0; i < readerList.length; i++)
				{
					if (!readerList.equals(""))	
						comboBoxReader.addItem(readerList[i]);
					else
						break;
				}
							 
				displayOut(0, 0, "Initialize success\n");
				
				comboBoxReader.setSelectedIndex(0);
				buttonConnect.setEnabled(true);				
		    }
		    catch (Exception ex)
		    {
		    	buttonConnect.setEnabled(false);
		    	displayOut(0, 0, "Cannot find a smart card reader.");
		    	JOptionPane.showMessageDialog(null,"Cannot find a smart card reader.", "Error", JOptionPane.ERROR_MESSAGE);
		    }
		} // Init
		
		if(buttonConnect == e.getSource())
		{	
			try
			{
				if(readerInterface.isConnectionActive())	
					readerInterface.disconnect();
				
				String rdrcon = (String)comboBoxReader.getSelectedItem();
				
				readerInterface.connect(rdrcon, "*");
				acos3 = new Acos3(readerInterface);
				
				displayOut(0, 0, "Successful connection to " + rdrcon);		
				
				//Check if card inserted is an ACOS Card
				if(!isAcos3())
					return;
				
				if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI)
					displayOut(0, 0, "Chip Type: ACOS3 Combi");
				else if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3CONTACTLESS)
					displayOut(0, 0, "Chip Type: ACOS3 Contactless");
				else
					displayOut(0, 0, "Chip Type: ACOS3");
			    
			    buttonFormat.setEnabled(true);			    
				radioButtonDES.setEnabled(true);
				radioButton3DES.setEnabled(true);
				textFieldCardKey.setEnabled(true);
				textFieldTerminalKey.setEnabled(true);
			}
			catch (CardException exception)
			{
				displayOut(0, 0, PcscProvider.GetScardErrMsg(exception) + "\r\n");
				JOptionPane.showMessageDialog(null,PcscProvider.GetScardErrMsg(exception), "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch(Exception exception)
			{
				displayOut(0, 0, exception.getMessage().toString() + "\r\n");
				JOptionPane.showMessageDialog(null,exception.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}		
		} // Connect
		
		if (buttonClear == e.getSource())
		{
			textAreaMessage.setText("");	
		} // Clear
			
		if (buttonQuit == e.getSource())
		{	
			this.dispose();
		} // Quit
		
		if (buttonDisconnect == e.getSource())
		{
			try			
			{
				//disconnect
				if (readerInterface.isConnectionActive())
					readerInterface.disconnect();
				
				textAreaMessage.setText("");
				buttonInitialize.setEnabled(true);		
				textFieldCardKey.setText("");
				textFieldTerminalKey.setText("");
				initMenu();
				comboBoxReader.removeAllItems();
				comboBoxReader.addItem("Please select reader                   ");				
			}
			catch (CardException exception)
			{
				displayOut(0, 0, PcscProvider.GetScardErrMsg(exception) + "\r\n");
				JOptionPane.showMessageDialog(null,PcscProvider.GetScardErrMsg(exception), "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch(Exception exception)
			{
				displayOut(0, 0, exception.getMessage().toString() + "\r\n");
				JOptionPane.showMessageDialog(null,exception.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}			
		} // Disconnect
		
		if (radioButtonDES == e.getSource())
		{
			textFieldCardKey.setText("");
			textFieldTerminalKey.setText("");
			textFieldCardKey.requestFocus();
		} // rbDES
		
		if (radioButton3DES == e.getSource())
		{
			textFieldCardKey.setText("");
			textFieldTerminalKey.setText("");
			textFieldCardKey.requestFocus();
		} // rb3DES
		
		if (buttonFormat == e.getSource())
		{
			byte[] cardKey, terminalKey;
			try
			{				
				if(!validTemplate())
					return;

				acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");				
				acos3.clearCard();
				
				//Format Card
				displayOut(0, 0, "Submit Code - IC");
				acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");

                //Set Option Registers and Security Option Registers
                //See Personalization File of ACOS3 Reference Manual for more information
				OptionRegister optionRegister = new OptionRegister();
				
				if(radioButtonDES.isSelected())
					optionRegister.setEnableTripleDes(false);
				else
					optionRegister.setEnableTripleDes(true);
				
				optionRegister.setRequireMutualAuthenticationOnInquireAccount(false);
				optionRegister.setRequireMutualAuthenticationOnAccountTransaction(false);
				optionRegister.setEnableRevokeDebitCommand(false);
				optionRegister.setRequirePinDuringDebit(false);
				optionRegister.setEnableDebitMac(false);
				optionRegister.setEnableChangePinCommand(false);
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
				displayOut(0, 0, "Select File - FF 02");
				displayOut(0, 0, "Format Card");
				acos3.configurePersonalizationFile(optionRegister, securityOptionRegister, (byte)0x03);
				//displayOut(0, 0, "Update FF 02 OK");
				
				//Select Security File
				displayOut(0, 0, "Select File - FF 03");
				acos3.selectFile(Acos3.INTERNAL_FILE.SECURITY_FILE);
				acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
				
				if(radioButtonDES.isSelected())
				{
					int indx, tmpInt;
					tmpArray = new byte[8];
						
					 //Write the card key in Record 02
					tmpStr = textFieldCardKey.getText();
					for (indx = 0; indx < 8; indx++)
					{
						tmpInt = (int)tmpStr.charAt(indx);
						tmpArray[indx] = (byte) tmpInt;
					}
					
					displayOut(0, 0, "Write Record");
					acos3.writeRecord((byte)0x02, (byte)0x00, tmpArray);				
					//displayOut(0,0,"Write card key OK");	
					
					//Write the terminal key in Record 03
					tmpStr = textFieldTerminalKey.getText();
					for (indx = 0; indx < 8; indx++)
					{
						tmpInt = (int)tmpStr.charAt(indx);
						tmpArray[indx] = (byte) tmpInt;
					}
					
					displayOut(0, 0, "Write Record");
					acos3.writeRecord((byte)0x03, (byte)0x00, tmpArray);
					//displayOut(0,0,"Write terminal key OK");
				}
				else
				{
					//Write the left half of the card key in Record 02
                    //Write the right half of the card key in Record 12 (0x0C)
					int indx, tmpInt;
					tmpArray = new byte[8];
					
					// Record 02 for Left half of Card Key
					tmpStr = textFieldCardKey.getText();					
					for (indx = 0; indx < 8; indx++)
					{
						tmpInt = (int)tmpStr.charAt(indx);
						tmpArray[indx] = (byte) tmpInt;
					}	
					
					displayOut(0, 0, "Write Record");
					acos3.writeRecord((byte)0x02, (byte)0x00, tmpArray);				
					//displayOut(0,0,"Write left half of card key OK");
					
					// Record 12 for Right half of Card key
					for (indx = 8; indx < 16; indx++)
					{
						tmpInt = (int)tmpStr.charAt(indx);
						tmpArray[indx-8] = (byte) tmpInt;
					}

					displayOut(0, 0, "Write Record");
					acos3.writeRecord((byte)0x0C, (byte)0x00, tmpArray);				
					//displayOut(0,0,"Write right half of card key OK");
					
					 //Write the left half of the terminal key in Record 03
                    //Write the right half of the terminal key in Record 13 (0x0D)
					tmpStr = textFieldTerminalKey.getText();
					for (indx = 0; indx < 8; indx++)
					{
						tmpInt = (int)tmpStr.charAt(indx);
						tmpArray[indx] = (byte) tmpInt;
					}		

					displayOut(0, 0, "Write Record");
					acos3.writeRecord((byte)0x03, (byte)0x00, tmpArray);
					//displayOut(0,0,"Write left half of terminal key OK");
					
					//  Record 13 for Right half of Terminal key					
					for (indx = 8; indx < 16; indx++)
					{
						tmpInt = (int)tmpStr.charAt(indx);
						tmpArray[indx-8] = (byte) tmpInt;
					}

					displayOut(0, 0, "Write Record");
					acos3.writeRecord((byte)0x0D, (byte)0x00, tmpArray);
					//displayOut(0,0,"Write right half of terminal key OK");
				}
				
				displayOut(0,0, "Format Card successful");
				
				buttonExecuteMutualAuthentication.setEnabled(true);
			}
			catch (CardException exception)
			{
				displayOut(0, 0, PcscProvider.GetScardErrMsg(exception) + "\r\n");
				JOptionPane.showMessageDialog(null,PcscProvider.GetScardErrMsg(exception), "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch(Exception exception)
			{
				displayOut(0, 0, exception.getMessage().toString() + "\r\n");
				JOptionPane.showMessageDialog(null,exception.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} // Format
		
		if (buttonExecuteMutualAuthentication == e.getSource())
		{
			String tmpStr = "";
			byte[] tripleDesResult = new byte[8];
			
			try
			{
				if(!validTemplate())
					return;
				
				displayOut(0, 0, "Start Session");
				CRnd = acos3.startSession();
				//displayOut(0,0, "Start session OK");
				
				// Retrieve Terminal Key from Input Template
				int indx, tmpInt;
				tmpArray = new byte[32];
				
				tmpStr = textFieldTerminalKey.getText();			
				for(int i = 0; i < textFieldTerminalKey.getText().length(); i++)
					tKey[i] = (byte)((int)tmpStr.charAt(i));
			
				//  Encrypt Random No (CRnd) with Terminal Key (tKey)
				//    tmpArray will hold the 8-byte Enrypted number
				for (indx = 0; indx < 8; indx++)
					tmpArray[indx] = CRnd[indx];
				
				if (radioButtonDES.isSelected())	
				{
					DES(tmpArray, tKey);				
				}
				else	
				{
					tripleDesResult = tripleDES(tmpArray,tKey);
					
					for (indx = 0; indx < 8; indx++)
						tmpArray[indx] = tripleDesResult[indx];
				}	
				
				byte[] responseData = new byte[8];
				byte[] response = new byte[8];
				
				//  Issue Authenticate command using 8-byte Encrypted No (tmpArray)
				//    and Random Terminal number (TRnd)
				for (indx = 0; indx < 8; indx++)
					responseData[indx] = tmpArray[indx];
	
				displayOut(0, 0, "Authenticate");
				response = acos3.authenticate(responseData, TRnd);
				
				if (response[0] == (byte)0x61)
				{
					responseData = new byte[(response[1] & 0xFF)];
				
					// Get 8-byte result of card-side authentication
					// and save to tmpResult
					responseData = acos3.getResponse(response[1]);            
		            
		        	for (indx = 0; indx < 8; indx++)
		                tmpResult[indx] = responseData[indx];
				}
				else
				{
					for (indx = 0; indx < 8; indx++)
		                tmpResult[indx] = response[indx];
				}
	        		
	        	/*  Terminal-side authentication process
	            '  Retrieve Card Key from Input Template */		
				tmpStr = textFieldCardKey.getText();
				for (indx = 0; indx < textFieldCardKey.getText().length(); indx++)
				{
				    tmpInt = (int)tmpStr.charAt(indx);
				    cKey[indx] = (byte) tmpInt;
				}
				
				//  Compute for Session Key
				if (radioButtonDES.isSelected()) 
				{
					/*  for single DES
					' prepare SessionKey
					' SessionKey = DES (DES(RNDc, KC) XOR RNDt, KT) */
	
					// calculate DES(cRnd,cKey)
					for (indx = 0; indx < 8; indx++)
						tmpArray[indx] = CRnd[indx];
	
					DES(tmpArray, cKey);
					
					// XOR the result with tRnd
					for (indx = 0; indx < 8; indx++)
						tmpArray[indx] = (byte)(tmpArray[indx] ^ TRnd[indx]);
	            
					// DES the result with tKey
					DES(tmpArray,tKey);
	
					// temp now holds the SessionKey
					for (indx = 0; indx < 8; indx++)
						SessionKey[indx] = tmpArray[indx];
				}
				else
				{
					/*  for triple DES
					' prepare SessionKey
					' Left half SessionKey =  3DES (3DES (CRnd, cKey), tKey)
					' Right half SessionKey = 3DES (TRnd, REV (tKey))
					' tmpArray = 3DES (CRnd, cKey) */
					
					// calculate DES(cRnd,cKey)
					for (indx=0; indx<8; indx++)
						tmpArray[indx] = CRnd[indx];
	
					tripleDesResult = tripleDES(tmpArray, cKey);					
					
					// 3DES the result with tKey
					tripleDesResult = tripleDES(tripleDesResult,tKey);
					
					// tmpArray holds the left half of SessionKey
					for (indx = 0; indx < 8; indx++)
						SessionKey[indx] = tripleDesResult[indx];
	
					/* compute ReverseKey of tKey
					' just swap its left side with right side
					' ReverseKey = right half of tKey + left half of tKey */
					for (indx = 0; indx < 8; indx++)
						ReverseKey[indx] = tKey[8 + indx];
	           
					for (indx = 0; indx < 8; indx++)
						ReverseKey[8 + indx] = tKey[indx];
	
					// compute tmpArray = 3DES (TRnd, ReverseKey)
					for (indx = 0; indx < 8; indx++)
						tripleDesResult[indx] = TRnd[indx];
	
					tripleDesResult = tripleDES(tripleDesResult, ReverseKey);
	
					// tmpArray holds the right half of SessionKey
					for (indx = 0; indx < 8; indx++)
						SessionKey[indx + 8] = tripleDesResult[indx];	
				}
				
				// compute DES (TRnd, SessionKey)
				for (indx = 0; indx < 8; indx++)
					tmpArray[indx] = TRnd[indx];	
				
				if (radioButtonDES.isSelected()) 
				{
					DES(tmpArray,SessionKey);
					
					for (indx = 0; indx < 8;indx++)
					{
						if (tmpResult[indx] != tmpArray[indx]) 
						{				
							displayOut(0, 0, "Card Response and Terminal Response do not match.");
							displayOut(0, 0, "Mutual Authentication failed.");
							return;
						}
					}
					
					displayOut(0,0,"Card Response: " + Helper.byteAsString(tmpResult, 0, 8, true));
					displayOut(0,0,"\nTerminal Response: " + Helper.byteAsString(tmpArray, 0, 8, true));				
				}
				else 
				{
					tripleDesResult = tripleDES(tmpArray,SessionKey);
					
					for (indx = 0; indx < 8;indx++)
					{
						if (tmpResult[indx] != tripleDesResult[indx]) 
						{				
							displayOut(0, 0, "Card Response and Terminal Response do not match.");
							displayOut(0, 0, "Mutual Authentication failed.");
							return;
						}
					}

					displayOut(0,0,"Card Response: " + Helper.byteAsString(tmpResult, 0, 8, true));
					displayOut(0,0,"\nTerminal Response: " + Helper.byteAsString(tripleDesResult, 0, 8, true));
				}				

				displayOut(0,0,"\nMutual Authentication successful");			
			}
			catch (CardException exception)
			{
				displayOut(0, 0, PcscProvider.GetScardErrMsg(exception) + "\r\n");
				JOptionPane.showMessageDialog(null,PcscProvider.GetScardErrMsg(exception), "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch(Exception exception)
			{
				displayOut(0, 0, exception.getMessage().toString() + "\r\n");
				JOptionPane.showMessageDialog(null,exception.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} // Mutual Authenticate
	}
	
	public static void DES(byte Data[], byte key[])
	{
		byte[] keyTemp = new byte[8];
		
		for(int i = 0; i < 8; i++)
		{
			keyTemp[i] = key[i];
		}
        try 
        {   
            DESKeySpec desKeySpec = new DESKeySpec(keyTemp);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);                  
           
            Cipher encryptCipher = Cipher.getInstance(algorithm);
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
           
            byte encryptedContents[] = process(Data, encryptCipher);
    
            for(int i = 0; i < 8; i++)
            {
            	Data[i] = encryptedContents[i];
            }
           
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
	}
	
	/*Triple DES Algo
	 * Data: 8 bytes
	 * Key:  16 bytes
	 *ENCRYPT:
	 * 1. DES(Data,Key,ENCRYPT)
	 * 2. DES(Data,Key[8],DECRYPT)
	 * 3. DES(Data,Key,ENCRYPT)
	 * 
	 * DECRYPT:
	 * 1. DES(Data,Key,DECRYPT)
	 * 2. DES(Data,Key[8],ENCRYPT)
	 * 3. DES(Data,Key,DECRYPT)
	 * */
	public static byte[] tripleDES(byte[] data, byte[] key) {
        byte[] result = null;
        byte[] tmpKey = new byte[24];
        try {
         System.arraycopy(key, 0, tmpKey, 0, 16);
         System.arraycopy(key, 0, tmpKey, 16, 8);
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            SecretKeySpec myKey = new SecretKeySpec(tmpKey, "DESede");

            cipher.init(Cipher.ENCRYPT_MODE, myKey);

            try {

                result = cipher.doFinal(data);

            } catch (IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return result;
    }
	 
	private static byte[] process(byte processMe[], Cipher cipher) throws Exception 
	{
		// Create the input stream to be used for encryption
		ByteArrayInputStream in = new ByteArrayInputStream(processMe);
	       
		// Now actually encrypt the data and put it into a
		// ByteArrayOutputStream so we can pull it out easily.
		CipherInputStream processStream = new CipherInputStream(in, cipher);
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
		int whatWasRead = 0;
		while ((whatWasRead = processStream.read()) != -1) 
		{
			resultStream.write(whatWasRead);
		}
	       
		return resultStream.toByteArray();
	}	

	public void keyReleased(KeyEvent ke) { }

	public void keyPressed(KeyEvent ke) { }

	public void keyTyped(KeyEvent ke) 
	{  		
  		Character x = (Character)ke.getKeyChar();
  		char empty = '\r';
  		
	      if (!((x >= '0') && (x <= '9') ||
	         (x == KeyEvent.VK_BACK_SPACE) ||
	         (x == KeyEvent.VK_DELETE))) {
	        getToolkit().beep();
	        ke.consume();
	      }
  		
		//Limit character length  	  	
  		if(radioButtonDES.isSelected())
  		{ 
  			if (((JTextField)ke.getSource()).getText().length() >= 8 ) 
  			{	
  				ke.setKeyChar(empty);
  				return;
  			}			
  		}  		
  		if(radioButton3DES.isSelected())
  		{ 
  			if (((JTextField)ke.getSource()).getText().length() >= 16 ) 
  			{	
  				ke.setKeyChar(empty);
  				return;
  			}			
  		}	
	}

	public void displayOut(int mType, int msgCode, String printText)
	{
		switch(mType)
		{	
			case 2: textAreaMessage.append("<< " + printText + "\n");break;
			case 3: textAreaMessage.append(">> " + printText + "\n\n");break;			
			default: textAreaMessage.append(printText + "\n");
		}
	}
		
	public void initMenu()
	{		
		textAreaMessage.setText("");
		displayOut(0, 0, "Program Ready\n");
		buttonInitialize.setEnabled(true);
		buttonConnect.setEnabled(false);		
		buttonFormat.setEnabled(false);
		buttonExecuteMutualAuthentication.setEnabled(false);
		radioButtonDES.setEnabled(false);
		radioButton3DES.setEnabled(false);
		textFieldCardKey.setEnabled(false);
		textFieldTerminalKey.setEnabled(false);
		radioButtonDES.setSelected(true);
	}
	
	public void onSendCommand(ReaderEvents.TransmitApduEventArg event) 
	{
		displayOut(2, 0, event.getAsString(true));
	}

	public void onReceiveCommand(ReaderEvents.TransmitApduEventArg event) 
	{
		displayOut(3, 0, event.getAsString(true) + "\r");
	}

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ACOS3MutualAuthentication().setVisible(true);
            }
        });
    }

    public boolean isAcos3()
    {
    	currentChipType = readerInterface.getChipType();
    	
    	if(currentChipType == ReaderInterface.CHIP_TYPE.ERROR)
   		   return false;
    	
    	if(currentChipType != ReaderInterface.CHIP_TYPE.ACOS3)
    	{
    		JOptionPane.showMessageDialog(null, "Card not supported. Please use ACOS3 Card", "Error", JOptionPane.ERROR_MESSAGE);
    		return false;
    	}
    	return true;
    }
    
    public boolean validTemplate()
	{
    	int length = 8;
    	
    	if(radioButton3DES.isSelected())
    		length = 16;
    	
		if(textFieldCardKey.getText().length() == 0)
		{
			JOptionPane.showMessageDialog(this, "Please key-in numeric value for Card Key.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldCardKey.selectAll();
			textFieldCardKey.requestFocus();
			
			return false;
		}
		
		if(textFieldTerminalKey.getText().length() == 0)
		{
			JOptionPane.showMessageDialog(this, "Please key-in numeric value for Terminal Key.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldTerminalKey.selectAll();
			textFieldTerminalKey.requestFocus();
			
			return false;
		}
		
		if(textFieldCardKey.getText().length() < length)
		{
			JOptionPane.showMessageDialog(this, "Invalid input length. Length must be " + length + ".", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldCardKey.selectAll();
			textFieldCardKey.requestFocus();
			
			return false;
		}

		if(textFieldTerminalKey.getText().length() < length)
		{
			JOptionPane.showMessageDialog(this, "Invalid input length. Length must be " + length + ".", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldTerminalKey.selectAll();
			textFieldTerminalKey.requestFocus();
			
			return false;
		}
		
		
		return true;
	}
}
