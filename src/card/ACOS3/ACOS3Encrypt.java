package card.ACOS3;/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              ACOS3Encrypt.java

  Description:       This sample program outlines the steps on how to
                     use the encryption options in ACOS card using
                     the PC/SC platform.
                 
  Author:            Donn Johonson A. Fabian

  Date:              October 11, 2012

  Revision Trail:   (Date/Author/Description)
  					 04-15-2010 / M.J.E.Castillo / Bug fixes

======================================================================*/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
 



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

public class ACOS3Encrypt extends JFrame implements Acos3CardReaderEvents.TransmitApduHandler, ActionListener, KeyListener{

	int retCode;
	static String VALIDCHARS = "0123456789abcdefABCDEF";
    private static String algorithm = "DES";
		
	byte [] cKey = new byte[16]; 
	byte [] tKey = new byte[16]; 
	byte [] tmpArray = new byte[33];
    byte [] CRnd = new byte[8];    			// Card random number
    byte [] TRnd = new byte[8];    			// Terminal random number
	byte [] ReverseKey = new byte[16];      // Reverse of Terminal Key
    byte [] SessionKey = new byte[16];
	String tmpStr;
	
	private JTextArea textAreaMessage;
	private JButton buttonClear, buttonDisconnect, buttonQuit, buttonInitialize, buttonConnect, buttonFormat, buttonSetValue, buttonSubmit;
	private JComboBox comboBoxCode, comboBoxReader;	
	private ButtonGroup buttonGroupEncrypt, buttonGroupSecure;
	private JScrollPane jScrollPane1;
	private JTextField textFieldCardKey,textFieldTerminalKey, textFieldSetValue;
	private JLabel labelCardKey, labelCodeValue, labelSelect, labelResult, labelSetValue, labelTerminalKey;
	private JRadioButton radioButton3DES, radioButtonDES, radioButtonEncrypted, radioButtonNotEncrypted;	
	
	private PcscReader pcscReader;
    private Acos3 acos3;

    private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;
    private JPanel panelSecurityOption;
    private JPanel panelKeyTemplate;
    private JPanel panelCodeSubmission;
    
    public ACOS3Encrypt() 
    {    	
    	this.setTitle("ACOS3 Secret Codes Encryption");
        initComponents();
        initMenu();
        pcscReader = new PcscReader();
        readerInterface = new ReaderInterface();
        
		// Instantiate an event handler object 
        readerInterface.setEventHandler(new Acos3CardReaderEvents());
		
		// Register the event handler implementation of this class
        readerInterface.getEventHandler().addEventListener(this);
    }

    private void initComponents() 
    {
		setSize(650, 488);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// sets the location of the form at the center of screen
		Dimension dim = getToolkit().getScreenSize();
		Rectangle abounds = getBounds();
		setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
		requestFocus();

		String[] rdrNameDef = { "Please select reader                   " };

		buttonGroupEncrypt = new ButtonGroup();
		buttonGroupSecure = new ButtonGroup();
		buttonInitialize = new JButton();
		buttonInitialize.setBounds(212, 60, 100, 23);
		buttonConnect = new JButton();
		buttonConnect.setBounds(212, 86, 100, 23);
		buttonQuit = new JButton();
		buttonQuit.setBounds(539, 420, 95, 23);
		labelSelect = new JLabel();
		labelSelect.setBounds(10, 11, 80, 14);
		labelResult = new JLabel();
		labelResult.setBounds(323, 11, 67, 14);
		jScrollPane1 = new JScrollPane();
		jScrollPane1.setBounds(322, 31, 312, 378);
		textAreaMessage = new JTextArea();
		buttonClear = new JButton();
		buttonClear.setBounds(322, 420, 95, 23);
		buttonDisconnect = new JButton();
		buttonDisconnect.setBounds(430, 420, 95, 23);
		comboBoxReader = new JComboBox(rdrNameDef);
		comboBoxReader.setBounds(10, 31, 301, 20);
		comboBoxReader.setSelectedIndex(0);

		labelSelect.setText("Select Reader");

		buttonInitialize.setText("Initialize");
		buttonConnect.setText("Connect");
		buttonClear.setText("Clear");
		buttonDisconnect.setText("Reset");
		labelSelect.setText("Select Reader");
		labelResult.setText("APDU Logs");

		textAreaMessage.setColumns(20);
		textAreaMessage.setRows(5);
		jScrollPane1.setViewportView(textAreaMessage);

		JPanel panelEncryptionOption = new JPanel();
		panelEncryptionOption.setBounds(10, 120, 151, 88);
		panelEncryptionOption.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Encryption Option",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));

		panelEncryptionOption.setLayout(new GridLayout(0, 1, 0, 0));
		radioButtonNotEncrypted = new JRadioButton();
		buttonGroupEncrypt.add(radioButtonNotEncrypted);
		radioButtonNotEncrypted.setText("Not Encrypted");
		radioButtonNotEncrypted.addActionListener(this);
		
		panelEncryptionOption.add(radioButtonNotEncrypted);
		
		radioButtonEncrypted = new JRadioButton();
		buttonGroupEncrypt.add(radioButtonEncrypted);
		radioButtonEncrypted.setText("Encrypted");
		radioButtonEncrypted.addActionListener(this);
		panelEncryptionOption.add(radioButtonEncrypted);
		
		panelSecurityOption = new JPanel();
		panelSecurityOption.setBounds(160, 120, 151, 88);
		panelSecurityOption.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Security Option",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		panelSecurityOption.setLayout(new GridLayout(0, 1, 0, 0));
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

		panelKeyTemplate = new JPanel();
		panelKeyTemplate.setBounds(10, 219, 302, 107);
		panelKeyTemplate.setBorder(new TitledBorder(null, "Key Template",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panelKeyTemplate.setLayout(null);
		labelCardKey = new JLabel();
		labelCardKey.setHorizontalAlignment(SwingConstants.RIGHT);
		labelCardKey.setBounds(10, 24, 89, 14);
		panelKeyTemplate.add(labelCardKey);

		labelCardKey.setText("Card Key");
		textFieldCardKey = new JTextField();
		textFieldCardKey.setBounds(109, 21, 183, 20);
		panelKeyTemplate.add(textFieldCardKey);
		textFieldCardKey.setTransferHandler(null);
		textFieldCardKey.addKeyListener(this);
		labelTerminalKey = new JLabel();
		labelTerminalKey.setHorizontalAlignment(SwingConstants.RIGHT);
		labelTerminalKey.setBounds(10, 49, 89, 14);
		panelKeyTemplate.add(labelTerminalKey);
		labelTerminalKey.setText("Terminal Key");
		textFieldTerminalKey = new JTextField();
		textFieldTerminalKey.setBounds(109, 46, 183, 20);
		textFieldTerminalKey.setTransferHandler(null);
		panelKeyTemplate.add(textFieldTerminalKey);
		buttonFormat = new JButton();
		buttonFormat.setBounds(192, 73, 100, 23);
		panelKeyTemplate.add(buttonFormat);
		buttonFormat.setText("Format");
		buttonFormat.addActionListener(this);
		textFieldTerminalKey.addKeyListener(this);

		panelCodeSubmission = new JPanel();
		panelCodeSubmission.setBorder(new TitledBorder(null, "Code Submission",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCodeSubmission.setBounds(10, 337, 302, 116);

		panelCodeSubmission.setLayout(null);
		labelCodeValue = new JLabel();
		labelCodeValue.setHorizontalAlignment(SwingConstants.RIGHT);
		labelCodeValue.setBounds(10, 25, 89, 14);
		panelCodeSubmission.add(labelCodeValue);
		labelCodeValue.setText("Code");
		comboBoxCode = new JComboBox();
		comboBoxCode.setBounds(109, 22, 183, 20);
		panelCodeSubmission.add(comboBoxCode);

		comboBoxCode.setModel(new DefaultComboBoxModel(new String[] {"Application Code 1", "Application Code 2", "Application Code 3", "Application Code 4", "Application Code 5", "PIN"}));
		comboBoxCode.setSelectedIndex(5);
		textFieldSetValue = new JTextField();
		textFieldSetValue.setBounds(109, 51, 183, 20);
		panelCodeSubmission.add(textFieldSetValue);
		labelSetValue = new JLabel();
		labelSetValue.setHorizontalAlignment(SwingConstants.RIGHT);
		labelSetValue.setBounds(10, 54, 89, 14);
		panelCodeSubmission.add(labelSetValue);
		labelSetValue.setText("Value");
		buttonSetValue = new JButton();
		buttonSetValue.setBounds(60, 82, 111, 23);
		panelCodeSubmission.add(buttonSetValue);
		buttonSetValue.setText("Set Value");
		buttonSubmit = new JButton();
		buttonSubmit.setBounds(181, 82, 111, 23);
		panelCodeSubmission.add(buttonSubmit);
		buttonSubmit.setText("Submit");
		buttonSubmit.addActionListener(this);
		buttonSetValue.addActionListener(this);
		textFieldSetValue.addKeyListener(this);
		textFieldSetValue.setTransferHandler(null);

		buttonQuit.setText("Quit");

		textAreaMessage.setLineWrap(true);

		buttonInitialize.addActionListener(this);
		buttonConnect.addActionListener(this);
		buttonDisconnect.addActionListener(this);
		buttonClear.addActionListener(this);
		buttonQuit.addActionListener(this);

		textAreaMessage.setForeground(Color.black);
		textAreaMessage.setEditable(false);

		getContentPane().setLayout(null);
		getContentPane().add(panelEncryptionOption);
		getContentPane().add(panelSecurityOption);
		getContentPane().add(panelKeyTemplate);
		getContentPane().add(panelCodeSubmission);
		getContentPane().add(buttonConnect);
		getContentPane().add(buttonInitialize);
		getContentPane().add(comboBoxReader);
		getContentPane().add(labelSelect);
		getContentPane().add(buttonClear);
		getContentPane().add(buttonDisconnect);
		getContentPane().add(buttonQuit);
		getContentPane().add(jScrollPane1);
		getContentPane().add(labelResult);
        
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
							    
				comboBoxReader.setSelectedIndex(0);
				buttonConnect.setEnabled(true);
				
				displayOut(0, 0, "Initialize success\n");
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
				
				displayOut(0, 0, "Successful connection to " + rdrcon + "\n");	

				//Check if card inserted is an ACOS Card
				if(!isAcos3())
					return;
				
				if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI)
					displayOut(0, 0, "Chip Type: ACOS3 Combi");
				else
					displayOut(0, 0, "Chip Type: ACOS3");
			    	
				buttonFormat.setEnabled(true);
				radioButtonEncrypted.setEnabled(true);
				radioButtonNotEncrypted.setEnabled(true);
				radioButtonNotEncrypted.setSelected(true);			
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
		}  //Clear
		
		if (buttonDisconnect == e.getSource())
		{
			try			
			{
				//disconnect
				if (readerInterface.isConnectionActive())
					readerInterface.disconnect();
				
				textAreaMessage.setText("");
				textFieldCardKey.setText("");
				textFieldTerminalKey.setText("");
				textFieldSetValue.setText("");			
				buttonInitialize.setEnabled(true);		
				initMenu();	
				comboBoxReader.removeAllItems();
				comboBoxReader.addItem("Please select reader                   ");
				comboBoxCode.setSelectedIndex(5);
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
		
		if(buttonQuit == e.getSource())
		{	
			this.dispose();
		}  // Quit
		
		if (radioButtonNotEncrypted == e.getSource())
		{
			radioButtonDES.setEnabled(false);
			radioButton3DES.setEnabled(false);		
			textFieldCardKey.requestFocus();	
			textFieldCardKey.setText("");
			textFieldTerminalKey.setText("");
			textFieldSetValue.setText("");		
		} // rbNotEnc			
		
		if (radioButtonEncrypted == e.getSource())
		{
			radioButtonDES.setEnabled(true);
			radioButton3DES.setEnabled(true);
			radioButtonDES.setSelected(true);
			textFieldCardKey.setText("");
			textFieldTerminalKey.setText("");
			textFieldCardKey.requestFocus();
			textFieldSetValue.setText("");
		}  // rbEnc
		
		if (radioButtonDES == e.getSource())
		{
			radioButtonEncrypted.setSelected(true);			
			textFieldCardKey.setText("");
			textFieldTerminalKey.setText("");
			textFieldCardKey.requestFocus();				
			textFieldSetValue.setText("");			
		} // rbDES
		
		if (radioButton3DES == e.getSource())
		{
			radioButtonEncrypted.setSelected(true);
			textFieldCardKey.setText("");
			textFieldTerminalKey.setText("");
			textFieldCardKey.requestFocus();		
			textFieldSetValue.setText("");
		} // rb3DES
		
		if (buttonFormat == e.getSource())
		{
			try
			{				
				// check values
				if (!validTemplate())
					return;
				
				displayOut(1, 0, "Submit Code - IC");
				acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
			
				tmpArray = new byte[4];
				
				// Select FF 02
				displayOut(1, 0, "Select File");
				acos3.selectFile(Acos3.INTERNAL_FILE.PERSONALIZATION_FILE);
	        	
	        	if (radioButtonDES.isSelected())
	        		tmpArray[0] = (byte)0x00;
	        	else
	        		tmpArray[0] = (byte)0x02;
	        	
	        	if (radioButtonNotEncrypted.isSelected())
	        		tmpArray[1] = (byte)0x00;   
	        	else
					tmpArray[1] = (byte)0x7E;        	// Encryption on all codes, except IC, enabled
	        	
	        	tmpArray[2] = (byte)0x03;			  	// 00    No of user files
				tmpArray[3] = (byte)0x00;           	// 00    Personalization bit
				

				displayOut(1, 0, "Write Record");
				acos3.writeRecord((byte)0x00, (byte)0x00, tmpArray);			
				
				displayOut(0, 0, "FF 02 is updated");
							
				displayOut(1, 0, "Submit Code - IC");
				acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
				
				//  Select FF 03
				displayOut(1, 0, "Select File");
				acos3.selectFile(Acos3.INTERNAL_FILE.SECURITY_FILE);
				
				if (radioButtonDES.isSelected() || radioButtonNotEncrypted.isSelected())
				{								
					int indx, tmpInt;
					tmpArray = new byte[8];
					
					tmpStr = textFieldCardKey.getText();					
					for (indx = 0; indx < 8; indx++)
					{
					    tmpInt = (int)tmpStr.charAt(indx);
					    tmpArray[indx] = (byte)tmpInt;
					}
					
					displayOut(1, 0, "Write Record");
					acos3.writeRecord((byte)0x02, (byte)0x00, tmpArray);				
					
					//  Record 03 for Terminal key
					tmpStr = textFieldTerminalKey.getText();
					for (indx = 0; indx < 8; indx++)
					{
					    tmpInt = (int)tmpStr.charAt(indx);
					    tmpArray[indx] = (byte)tmpInt;
					}
					
					displayOut(1, 0, "Write Record");
					acos3.writeRecord((byte)0x03, (byte)0x00, tmpArray);
					
					displayOut(0, 0, "FF 03 is updated");
				}
				else if (radioButton3DES.isSelected())
				{
					int indx, tmpInt;				
					tmpArray = new byte[8];
					
					tmpStr = textFieldCardKey.getText();				
					for (indx = 0; indx < 8; indx++)
					{
					    tmpInt = (int)tmpStr.charAt(indx);
					    tmpArray[indx] = (byte)tmpInt;
					}		
					
					displayOut(1, 0, "Write Record");
					acos3.writeRecord((byte)0x02, (byte)0x00, tmpArray);				
	
					// Record 12 for Right half of Card key
					for (indx = 8; indx < 16; indx++)
					{
					    tmpInt = (int)tmpStr.charAt(indx);
					    tmpArray[indx-8] = (byte)tmpInt;
					}
					
					displayOut(1, 0, "Write Record");
					acos3.writeRecord((byte)0x0C, (byte)0x00, tmpArray);				
					
					//  Record 03 for Terminal key
					tmpStr = textFieldTerminalKey.getText();
					for (indx = 0; indx < 8; indx++)
					{
						tmpInt = (int)tmpStr.charAt(indx);
						tmpArray[indx] = (byte)tmpInt;
					}
					
					displayOut(1, 0, "Write Record");
					acos3.writeRecord((byte)0x03, (byte)0x00, tmpArray);					
	
					//  Record 13 for Right half of Terminal key					
					for (indx = 8; indx < 16; indx++)
					{
						tmpInt = (int)tmpStr.charAt(indx);
						tmpArray[indx-8] = (byte) tmpInt;					
					}
					
					displayOut(1, 0, "Write Record");
					acos3.writeRecord((byte)0x0D, (byte)0x00, tmpArray);
					
					displayOut(0, 0, "FF 03 is updated");					
				}
				
				displayOut(1, 0, "Format card successful");
				buttonSetValue.setEnabled(true);							
				textFieldSetValue.setEnabled(true);
				comboBoxCode.setEnabled(true);	
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
		
		if (buttonSetValue == e.getSource())
		{
			try
			{
				// check values				
				if(comboBoxCode.getSelectedIndex() == -1)
				{
					JOptionPane.showMessageDialog(this,"Please select Code.", "Error", JOptionPane.ERROR_MESSAGE);
					return;				
				}
				
				if(textFieldSetValue.getText().length() == 0)
				{	
					JOptionPane.showMessageDialog(this,"Please key-in numeric value for " + 
												  (String)comboBoxCode.getSelectedItem() + 
												  ".", "Error", JOptionPane.ERROR_MESSAGE);
					textFieldSetValue.requestFocus();
					return;
				}
				
		    	if(!(textFieldSetValue.getText().chars().allMatch(Character:: isDigit)))
		    	{
		    		JOptionPane.showMessageDialog(this, 
		    									  "Please key-in numeric value for " + 
		    		                              (String)comboBoxCode.getSelectedItem() + 
		    		                              ".", "Error", JOptionPane.ERROR_MESSAGE);
		    		textFieldSetValue.requestFocus();
		    		return;
		    	}
				
				if(textFieldSetValue.getText().length()  < 8)
				{	
					JOptionPane.showMessageDialog(this,"Invalid input length. Length must be 8.", "Error", JOptionPane.ERROR_MESSAGE);
					textFieldSetValue.requestFocus();
					return;
				}
				
				displayOut(1, 0, "Submit Code - IC");
				acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
				
				displayOut(1, 0, "Select File");
				acos3.selectFile(Acos3.INTERNAL_FILE.SECURITY_FILE);
				
				int indx, tmpInt;
				byte tmpByte = (byte)0x01;
				
				tmpArray = new byte[8];
				tmpStr = "";				
				tmpStr = textFieldSetValue.getText();
				for (indx = 0; indx < 8; indx++)
				{
					tmpInt = (int)tmpStr.charAt(indx);
					tmpArray[indx] = (byte) tmpInt;
				}
				 
				switch (comboBoxCode.getSelectedIndex())
				{			 
					case 0:
						tmpByte = (byte)0x05;
						tmpStr = "Application Code 1";
						break;				 
					case 1:
						tmpByte = (byte)0x06;
						tmpStr = "Application Code 2";
						break;				 
					case 2:
						tmpByte = (byte)0x07;
						tmpStr = "Application Code 3";
						break;				 
					case 3:
						tmpByte = (byte)0x08;
						tmpStr = "Application Code 4";
						break;				 
					case 4:
						tmpByte = (byte)0x09;
						tmpStr = "Application Code 5";
						break;
					case 5:
						tmpByte = (byte)0x01;
						tmpStr = "PIN";
						break;	
				}
				 
				// Write to corresponding record in FF 03
				displayOut(1, 0, "Write Record");
				acos3.writeRecord(tmpByte, (byte)0x00, tmpArray);
				 	
				displayOut(0, 0, tmpStr + " set successfully\n");	

				buttonSubmit.setEnabled(true);	
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
		} // Set Value
		
		if (buttonSubmit == e.getSource())
		{
			try
			{
				byte[] tmpCode = new byte[8];
				
				// check values
				if(comboBoxCode.getSelectedIndex() == -1)
				{
					JOptionPane.showMessageDialog(this,"Please select Code.", "Error", JOptionPane.ERROR_MESSAGE);
					return;				
				}
				
				if(textFieldSetValue.getText().length() == 0)
				{	
					JOptionPane.showMessageDialog(this,"Please key-in numeric value for " + 
												  (String)comboBoxCode.getSelectedItem() + 
												  ".", "Error", JOptionPane.ERROR_MESSAGE);
					textFieldSetValue.requestFocus();
					return;
				}
				
		    	if(!(textFieldSetValue.getText().chars().allMatch(Character:: isDigit)))
		    	{
		    		JOptionPane.showMessageDialog(this, 
		    									  "Please key-in numeric value for " + 
		    		                              (String)comboBoxCode.getSelectedItem() + 
		    		                              ".", "Error", JOptionPane.ERROR_MESSAGE);
		    		textFieldSetValue.requestFocus();
		    		return;
		    	}
				
				if(textFieldSetValue.getText().length()  < 8)
				{	
					JOptionPane.showMessageDialog(this,"Invalid input length. Length must be 8.", "Error", JOptionPane.ERROR_MESSAGE);
					textFieldSetValue.requestFocus();
					return;
				}		
				
				if (radioButtonEncrypted.isSelected())
				{		
					if (!validTemplate())
						return;
							
					GetSessionKey();
					if (retCode != 0)
						return;				
					 
					int indx, tmpInt;					
					tmpStr = "";
					tmpStr = textFieldSetValue.getText();
					for (indx = 0; indx < 8; indx++)
					{
						tmpInt = (int)tmpStr.charAt(indx);
						tmpArray[indx] = (byte)tmpInt;
					}
					 
					if (radioButtonDES.isSelected())
						DES(tmpArray, SessionKey);
					else
						TripleDES(tmpArray, SessionKey);
									
					tmpCode = Arrays.copyOfRange(tmpArray, 0, 8);
					displayOut(1, 0, "Submit Code");
				}
				else
				{					
					int indx, tmpInt;
					tmpStr = "";
					tmpStr = textFieldSetValue.getText();
					for (indx = 0; indx < 8; indx++)
					{
						tmpInt = (int)tmpStr.charAt(indx);
						tmpCode[indx] = (byte) tmpInt;
					}
					displayOut(0, 0, "Submit Code");
				}
				
				
				
				switch (comboBoxCode.getSelectedIndex())
				{
					
					case 0:
						acos3.submitCode(Acos3.CODE_TYPE.AC1, tmpCode);						
						tmpStr = "Application Code 1";
						break;
					case 1:
						acos3.submitCode(Acos3.CODE_TYPE.AC2, tmpCode);
						tmpStr = "Application Code 2";
						break;
					case 2:						
						acos3.submitCode(Acos3.CODE_TYPE.AC3, tmpCode);
						tmpStr = "Application Code 3";
						break;
					case 3:						
						acos3.submitCode(Acos3.CODE_TYPE.AC4, tmpCode);
						tmpStr = "Application Code 4";
						break;
					case 4:						
						acos3.submitCode(Acos3.CODE_TYPE.AC5, tmpCode);
						tmpStr = "Application Code 5";
						break;
					case 5:
						acos3.submitCode(Acos3.CODE_TYPE.PIN, tmpCode);						
						tmpStr = "PIN";
						break;
				}
				
				displayOut(0, 0, tmpStr + " submitted successfully");	
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
		} // Submit
				
	}
		
	public void DES(byte Data[], byte key[])
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

	public static void TripleDES(byte Data[], byte key[])
	{
		byte[] result = null;
        byte[] tmpKey = new byte[24];
        
        try 
        {        	
        	System.arraycopy(key, 0, tmpKey, 0, 16);
        	System.arraycopy(key, 0, tmpKey, 16, 8);
        	
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            SecretKeySpec myKey = new SecretKeySpec(tmpKey, "DESede");

            cipher.init(Cipher.ENCRYPT_MODE, myKey);

            try 
            {
                result = cipher.doFinal(Data);
                
                for(int i = 0; i < 8; i++)
                {
                	Data[i] = result[i];                	
                }
            } 
            catch (IllegalBlockSizeException | BadPaddingException e) 
            {
                e.printStackTrace();
            }

        } 
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) 
        {
            e.printStackTrace();
        }
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
	
	private void GetSessionKey()
	{
		int indx, tmpInt;
		
		try
		{
			retCode = 0;
			if (radioButtonDES.isSelected())
			{
				tmpStr = textFieldCardKey.getText();
				for (indx = 0; indx < 8; indx++)
				{
				    tmpInt = (int)tmpStr.charAt(indx);
				    cKey[indx] = (byte) tmpInt;
				}
				
				tmpStr = textFieldTerminalKey.getText();
				for (indx = 0; indx < 8; indx++)
				{
				    tmpInt = (int)tmpStr.charAt(indx);
				    tKey[indx] = (byte) tmpInt;
				}
			}
			else
			{
				tmpStr = textFieldCardKey.getText();
				for (indx = 0; indx < 16; indx++)
				{
				    tmpInt = (int)tmpStr.charAt(indx);
				    cKey[indx] = (byte) tmpInt;
				}
				
				tmpStr = textFieldTerminalKey.getText();
				for (indx = 0; indx < 16; indx++)
				{
				    tmpInt = (int)tmpStr.charAt(indx);
				    tKey[indx] = (byte) tmpInt;
				}
			}			
			
			tmpArray = new byte[32];
			
			displayOut(0, 0, "Start Session");
			CRnd = acos3.startSession();
			
			for (indx = 0; indx < 8; indx++)
				tmpArray[indx] = CRnd[indx];
			
			if (radioButtonDES.isSelected())		
				DES(tmpArray, tKey);
			else			
				TripleDES(tmpArray, tKey);
			
			byte[] responseData = new byte[8];			
	
			//  Issue Authenticate command using 8-byte Encrypted No (tmpArray)
			//    and Random Terminal number (TRnd)
			for (indx = 0; indx < 8; indx++)
				responseData[indx] = tmpArray[indx];
			
			displayOut(1, 0, "Authenticate");
			byte[] response = acos3.authenticate(responseData, TRnd);
			if (response[0] == (byte)0x61)
			{
				responseData = new byte[(response[1] & 0xFF)];
			
				// Get 8-byte result of card-side authentication
				responseData = acos3.getResponse(response[1]);
			}
			else
			{
				for (indx = 0; indx < 8; indx++)
					responseData[indx] = response[indx];
			}
			
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
					tmpArray[indx] = tmpArray[indx] ^= TRnd[indx];
	        
				// DES the result with tKey
				DES(tmpArray, tKey);
	
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
	
				for (indx = 0; indx < 8; indx++)
					tmpArray[indx] = CRnd[indx];
	
				TripleDES(tmpArray, cKey);
				
				// 3DES the result with tKey			
				TripleDES(tmpArray, tKey);
	
				// tmpArray holds the left half of SessionKey
				for (indx = 0; indx < 8; indx++)
					SessionKey[indx] = tmpArray[indx];			
	
				/* compute ReverseKey of tKey
				' just swap its left side with right side
				' ReverseKey = right half of tKey + left half of tKey */
				for (indx = 0; indx < 8; indx++)
					ReverseKey[indx] = tKey[8 + indx];
	       			
				for (indx = 8; indx < 16; indx++)
					ReverseKey[indx] = tKey[indx - 8];
				
				// compute tmpArray = 3DES (TRnd, ReverseKey)
				for (indx = 0; indx < 8; indx++)
					tmpArray[indx] = TRnd[indx];
	
				TripleDES(tmpArray, ReverseKey);
	
				// tmpArray holds the right half of SessionKey
				for (indx = 0; indx < 8; indx++)
					SessionKey[indx + 8] = tmpArray[indx];	
			}			
		}
		catch (Exception ex)
		{
			displayOut(0, 0, ex.getMessage().toString());
			retCode = -1;
		}
	}
	
	private boolean validTemplate()
	{
		int length = 8;
		
		if(radioButton3DES.isSelected() && radioButtonEncrypted.isSelected())
			length = 16;
		
		if(textFieldCardKey.getText().length() == 0)
		{
			JOptionPane.showMessageDialog(this, "Please key-in numeric value for Card Key.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldCardKey.requestFocus(); 
			return false;
		}
		
		if(textFieldTerminalKey.getText().length() == 0)
		{
			JOptionPane.showMessageDialog(this, "Please key-in numeric value for Terminal Key.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldTerminalKey.requestFocus(); 
			return false;
		}
		
		if (textFieldCardKey.getText().length() < length)
		{	
			JOptionPane.showMessageDialog(this, "Invalid input length. Length must be " + length + ".", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldCardKey.requestFocus(); 
			return false;
		}
		
		if (textFieldTerminalKey.getText().length() < length)
		{
			JOptionPane.showMessageDialog(this, "Invalid input length. Length must be " + length + ".", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldTerminalKey.requestFocus(); 
			return false;
		}
		
		return true;
	}
		
	public void keyReleased(KeyEvent ke) { }	
	
	public void keyPressed(KeyEvent ke) {  	}
	
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

  		//Check valid characters
  		if (VALIDCHARS.indexOf(x) == -1 ) 
  			ke.setKeyChar(empty);
  					  
		//Limit character length  		
  		if (radioButtonEncrypted.isSelected())
  		{
  			if(radioButtonDES.isSelected())
  			{
  				if (((JTextField)ke.getSource()).getText().length() >= 8 ) 
  				{		
  					ke.setKeyChar(empty);  				
  					return;
  				}			
  			}
  			else if(radioButton3DES.isSelected())
  			{ 
  				if(textFieldSetValue.isFocusOwner())
  				{
  					if (((JTextField)ke.getSource()).getText().length() >= 8 ) 
  	  				{
  	  					ke.setKeyChar(empty);
  	  					return;
  	  				}
  				}
  				else
  				{
	  				if (((JTextField)ke.getSource()).getText().length() >= 16 ) 
	  				{
	  					ke.setKeyChar(empty);
	  					return;
	  				}
  				}
  			}
  			else
  			{
  				if (((JTextField)ke.getSource()).getText().length() >= 8 ) 
  				{	
  					ke.setKeyChar(empty);	
  					return;
  				}	
  			}
  		}  		
  		else if (radioButtonNotEncrypted.isSelected())
  		{	
  			if  (((JTextField)ke.getSource()).getText().length() >= 8 ) 
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
			case 1: textAreaMessage.append("\n" + printText + "\n"); break;
			case 2: textAreaMessage.append("<< " + printText + "\n"); break;
			case 3: textAreaMessage.append(">> " + printText); break;
			default: textAreaMessage.append(printText + "\n");
		}
	}
	
	public void initMenu()
	{	
		textAreaMessage.setText("");
		displayOut(0, 0, "Program Ready");
		buttonInitialize.setEnabled(true);
		buttonConnect.setEnabled(false);
		buttonSetValue.setEnabled(false);
		buttonSubmit.setEnabled(false);
		buttonFormat.setEnabled(false);
		radioButtonEncrypted.setEnabled(false);
		radioButtonNotEncrypted.setEnabled(false);
		radioButtonDES.setEnabled(false);
		radioButton3DES.setEnabled(false);
		textFieldCardKey.setEnabled(false);
		textFieldTerminalKey.setEnabled(false);
		textFieldTerminalKey.setText("");
		textFieldCardKey.setText("");
		textFieldSetValue.setEnabled(false);
		textFieldSetValue.setText("");
		comboBoxCode.setEnabled(false);
		comboBoxCode.setSelectedIndex(5);
		radioButtonNotEncrypted.setSelected(true);
		radioButtonDES.setSelected(true);		
	}
	
	public void onSendCommand(Acos3CardReaderEvents.TransmitApduEventArg event)
	{
		displayOut(2, 0, event.getAsString(true));
	}

	public void onReceiveCommand(Acos3CardReaderEvents.TransmitApduEventArg event)
	{
		displayOut(3, 0, event.getAsString(true) + "\r\n");
	}
	
    
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ACOS3Encrypt().setVisible(true);
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
    		JOptionPane.showMessageDialog(null,"Card not supported. Please use ACOS3 Card", "Error", JOptionPane.ERROR_MESSAGE);
    		return false;
    	}
    	return true;
    }
}
