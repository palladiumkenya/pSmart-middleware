package card.ACOS3;/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              ACOS3SecureMessaging.java

  Description:       This sample program outlines the steps on how to
                     use the ACOS card for Reading and Writing in Binary
                     process with Secure Messaging using the PC/SC platform.
                    
  Author:            Donn Johnson A. Fabian

  Date:              October 11, 2012

  Revision Trail:   (Date/Author/Description)

======================================================================*/

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.awt.*;
import java.awt.event.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.smartcardio.CardException;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.BadLocationException;


public class ACOS3SecureMessaging extends JFrame implements Acos3CardReaderEvents.TransmitApduHandler, ActionListener, KeyListener{

	//JPCSC Variables
	int retCode;
	private static String algorithm = "DES";
	static String VALIDCHARS = "ABCDEFabcdef0123456789";
	static String VALIDNUM = "0123456789";	
	
	byte [] CRnd = new byte[8];
	byte [] TRnd = new byte[8];
	byte [] tmpResult = new byte[32];
	byte [] CipherKey = new byte[16];
	byte [] SessionKey = new byte[16];
	byte [] SeqNum = new byte[8];
	byte [] tempArray = new byte[32];	
	byte SW1, SW2;
	
	private PcscReader pcscReader;
    private Acos3 acos3;
	
	//GUI Variables
    private JButton buttonInitialize, buttonConnect, buttonFormat, buttonRead, buttonWrite, buttonAuthenticate, buttonClear, buttonReset, buttonQuit;
    private ButtonGroup buttonGroupSecurityOption;
    private JComboBox comboBoxReader;
    private JComboBox comboBoxReader_1;
    private JCheckBox checkBoxSecureMessaging;
    private JLabel labelCardKey, labelData, labelFileID, labelID, labelLen, labelLength, labelOffset, labelReader, labelTerminalKey;
    private JTextArea textAreaData, textAreaMessage;
    private JRadioButton radioButton3DES, radioButtonDES;
    private JPanel messagePanel, cardFormatPanel, binaryPanel, SecurityOptionPanel;
    private JScrollPane scrollPaneData, scrollPaneMessage;
    private JTextField textFieldFID1, textFieldFID2, textFieldFileID1, textFieldFileID2, textFieldFileLen1, textFieldFileLen2, textFieldLen, textFieldOffset1, textFieldOffset2, textFieldCardKey, textFieldTerminalKey;

    private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;
    private JLabel lblApduLogs;
    
    public ACOS3SecureMessaging() 
    {	
    	this.setTitle("ACOS3 Secure Messaging");
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
		//GUI Variables
		setSize(663,631);
		setResizable(false);
   	  	setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
		
		//sets the location of the form at the center of screen
   		Dimension dim = getToolkit().getScreenSize();
   		Rectangle abounds = getBounds();
   		setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
   		requestFocus();
   		
   		String[] rdrNameDef = {"Please select reader             "};	
		
		buttonGroupSecurityOption = new ButtonGroup();
        comboBoxReader = new JComboBox();
        cardFormatPanel = new JPanel();
        cardFormatPanel.setBounds(10, 147, 299, 210);
        labelFileID = new JLabel();
        labelFileID.setBounds(16, 102, 55, 14);
        labelLength = new JLabel();
        labelLength.setBounds(16, 130, 55, 14);
        textFieldFileID1 = new JTextField();
        textFieldFileID1.setBounds(81, 99, 45, 20);
        textFieldFileID1.setTransferHandler(null);
        textFieldFileID2 = new JTextField();
        textFieldFileID2.setBounds(132, 99, 45, 20);
        textFieldFileID2.setTransferHandler(null);
        textFieldFileLen1 = new JTextField();
        textFieldFileLen1.setBounds(81, 127, 45, 20);
        textFieldFileLen1.setTransferHandler(null);
        textFieldFileLen2 = new JTextField();
        textFieldFileLen2.setBounds(132, 127, 45, 20);
        textFieldFileLen2.setTransferHandler(null);
        buttonFormat = new JButton();
        buttonFormat.setBounds(164, 155, 122, 23);
        labelCardKey = new JLabel();
        labelCardKey.setBounds(16, 19, 85, 14);
        labelTerminalKey = new JLabel();
        labelTerminalKey.setBounds(16, 45, 85, 14);
        textFieldCardKey = new JTextField();
        textFieldCardKey.setBounds(108, 16, 175, 20);
        textFieldCardKey.setTransferHandler(null);
        textFieldTerminalKey = new JTextField();
        textFieldTerminalKey.setBounds(108, 42, 175, 20);
        textFieldTerminalKey.setTransferHandler(null);
        buttonAuthenticate = new JButton();
        buttonAuthenticate.setBounds(164, 180, 122, 23);
        binaryPanel = new JPanel();
        binaryPanel.setBounds(10, 368, 299, 226);
        labelID = new JLabel();
        labelOffset = new JLabel();
        textFieldFID1 = new JTextField();
        textFieldFID1.setTransferHandler(null);
        textFieldFID2 = new JTextField();
        textFieldFID2.setTransferHandler(null);
        textFieldOffset1 = new JTextField();
        textFieldOffset1.setTransferHandler(null);
        textFieldOffset2 = new JTextField();
        textFieldOffset2.setTransferHandler(null);
        labelLen = new JLabel();
        textFieldLen = new JTextField();
        textFieldLen.setEnabled(false);
        textFieldLen.setTransferHandler(null);
        labelData = new JLabel();
        scrollPaneData = new JScrollPane();
        textAreaData = new JTextArea();
        textAreaData.setEnabled(false);
        textAreaData.setTransferHandler(null);
        
        buttonRead = new JButton();
        buttonWrite = new JButton();
        checkBoxSecureMessaging = new JCheckBox();
        checkBoxSecureMessaging.setBounds(150, 69, 133, 23);
        messagePanel = new JPanel();
        messagePanel.setBounds(319, 0, 329, 592);
        scrollPaneMessage = new JScrollPane();
        scrollPaneMessage.setBounds(10, 31, 309, 511);
        textAreaMessage = new JTextArea();
        textAreaMessage.setWrapStyleWord(true);
        textAreaMessage.setTransferHandler(null);
        buttonClear = new JButton();
        buttonClear.setBounds(10, 553, 100, 23);
        buttonReset = new JButton();
        buttonReset.setBounds(118, 553, 100, 23);
        buttonQuit = new JButton();
        buttonQuit.setBounds(224, 553, 100, 23);
        SecurityOptionPanel = new JPanel();
        SecurityOptionPanel.setBounds(10, 62, 148, 74);
        radioButtonDES = new JRadioButton();
        radioButton3DES = new JRadioButton();
		
		buttonGroupSecurityOption.add(radioButtonDES);
        buttonGroupSecurityOption.add(radioButton3DES);

		radioButtonDES.addActionListener(this);
		radioButton3DES.addActionListener(this);		

        cardFormatPanel.setBorder(BorderFactory.createTitledBorder("Card Format Routine"));

        labelFileID.setText("File ID");
        labelLength.setText("Length");
        buttonFormat.setText("Format Card");
        labelCardKey.setText("Card Key");
        labelTerminalKey.setText("Terminal Key");
        buttonAuthenticate.setText("Authenticate");
        checkBoxSecureMessaging.setText("Secure Messaging");

        binaryPanel.setBorder(BorderFactory.createTitledBorder("Read and Write to Binary File"));

        labelID.setText("File ID");
        labelOffset.setText("File Offset");
        labelLen.setText("Length");
        labelData.setText("Data");

        scrollPaneData.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        textAreaData.setColumns(20);
        textAreaData.setRows(5);         
        textAreaData.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyTyped(KeyEvent ke) {
        		       		
        		if(!textFieldLen.getText().equals(""))
        		{
        			int length = Integer.parseInt(textFieldLen.getText(), 16);
					int currentTextLength = textAreaData.getText().length();
					
					if(currentTextLength >= length)
					{					
						try {
							textAreaData.setText(textAreaData.getText(0, length));
							ke.setKeyChar('\r');
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
        			return;
        		}
        	}
        	
        });
        
        scrollPaneData.setViewportView(textAreaData);

        buttonRead.setText("Read Binary");
        buttonWrite.setText("Write Binary");

        GroupLayout gl_binaryPanel = new GroupLayout(binaryPanel);
        binaryPanel.setLayout(gl_binaryPanel);
        gl_binaryPanel.setHorizontalGroup(
            gl_binaryPanel.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_binaryPanel.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_binaryPanel.createParallelGroup(Alignment.LEADING)
                    .addGroup(Alignment.TRAILING, gl_binaryPanel.createParallelGroup(Alignment.LEADING, false)
                        .addComponent(buttonRead, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonWrite, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                    .addGroup(gl_binaryPanel.createSequentialGroup()
                        .addGroup(gl_binaryPanel.createParallelGroup(Alignment.LEADING)
                            .addComponent(labelID)
                            .addComponent(labelOffset))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(gl_binaryPanel.createParallelGroup(Alignment.LEADING)
                            .addGroup(gl_binaryPanel.createSequentialGroup()
                                .addComponent(textFieldFID1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(textFieldFID2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                            .addGroup(gl_binaryPanel.createSequentialGroup()
                                .addComponent(textFieldOffset1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(textFieldOffset2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(labelLen)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(textFieldLen, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))))
                    .addComponent(labelData)
                    .addComponent(scrollPaneData, GroupLayout.PREFERRED_SIZE, 259, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        gl_binaryPanel.setVerticalGroup(
            gl_binaryPanel.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_binaryPanel.createSequentialGroup()
                .addGroup(gl_binaryPanel.createParallelGroup(Alignment.BASELINE)
                    .addComponent(labelID)
                    .addComponent(textFieldFID1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldFID2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(gl_binaryPanel.createParallelGroup(Alignment.BASELINE)
                    .addComponent(labelOffset)
                    .addComponent(textFieldOffset1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldOffset2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelLen)
                    .addComponent(textFieldLen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(labelData)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(scrollPaneData, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(buttonRead)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(buttonWrite)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        textAreaMessage.setColumns(20);
        textAreaMessage.setRows(5);
        scrollPaneMessage.setViewportView(textAreaMessage);

        buttonClear.setText("Clear");
        buttonReset.setText("Reset");
        buttonQuit.setText("Quit");

        SecurityOptionPanel.setBorder(BorderFactory.createTitledBorder("Security Option"));

        radioButtonDES.setText("DES");
        radioButton3DES.setText("3DES");
        labelReader = new JLabel();
        labelReader.setBounds(10, 11, 283, 14);
        
        labelReader.setText("Select Reader");
        comboBoxReader_1 = new JComboBox(rdrNameDef);
        comboBoxReader_1.setBounds(10, 31, 283, 20);
        comboBoxReader_1.setSelectedIndex(0);
		
        
        textAreaMessage.setForeground(Color.black);
        textAreaMessage.setEditable(false);
        getContentPane().setLayout(null);
        getContentPane().add(labelReader);
        getContentPane().add(comboBoxReader_1);
        getContentPane().add(cardFormatPanel);
        getContentPane().add(binaryPanel);
        getContentPane().add(SecurityOptionPanel);
        SecurityOptionPanel.setLayout(new GridLayout(2, 1, 0, 0));
        SecurityOptionPanel.add(radioButtonDES);
        SecurityOptionPanel.add(radioButton3DES);
        getContentPane().add(messagePanel);
        buttonInitialize = new JButton();
        buttonInitialize.setBounds(168, 74, 125, 23);
        getContentPane().add(buttonInitialize);
        
        buttonInitialize.setText("Initalize");
        
        buttonInitialize.setMnemonic(KeyEvent.VK_I);
        buttonConnect = new JButton();
        buttonConnect.setBounds(168, 102, 125, 23);
        getContentPane().add(buttonConnect);
        buttonConnect.setText("Connect");
        buttonConnect.setMnemonic(KeyEvent.VK_C);
        buttonConnect.addActionListener(this);
        
        buttonInitialize.addActionListener(this);
        
        textAreaMessage.setLineWrap(true);
        textAreaData.setLineWrap(true);
        buttonReset.setMnemonic(KeyEvent.VK_R);
        buttonClear.setMnemonic(KeyEvent.VK_L);
        buttonFormat.setMnemonic(KeyEvent.VK_F);
        buttonRead.setMnemonic(KeyEvent.VK_R);
        buttonWrite.setMnemonic(KeyEvent.VK_W);
        buttonQuit.setMnemonic(KeyEvent.VK_Q);
        messagePanel.setLayout(null);
        messagePanel.add(scrollPaneMessage);
        messagePanel.add(buttonClear);
        messagePanel.add(buttonReset);
        messagePanel.add(buttonQuit);
        
        lblApduLogs = new JLabel("APDU Logs");
        lblApduLogs.setBounds(10, 11, 100, 14);
        messagePanel.add(lblApduLogs);
        buttonAuthenticate.setMnemonic(KeyEvent.VK_A);
        cardFormatPanel.setLayout(null);
        cardFormatPanel.add(labelTerminalKey);
        cardFormatPanel.add(labelCardKey);
        cardFormatPanel.add(labelLength);
        cardFormatPanel.add(labelFileID);
        cardFormatPanel.add(textFieldFileLen1);
        cardFormatPanel.add(textFieldFileID1);
        cardFormatPanel.add(textFieldFileID2);
        cardFormatPanel.add(textFieldFileLen2);
        cardFormatPanel.add(buttonAuthenticate);
        cardFormatPanel.add(buttonFormat);
        cardFormatPanel.add(textFieldTerminalKey);
        cardFormatPanel.add(checkBoxSecureMessaging);
        cardFormatPanel.add(textFieldCardKey);
        buttonReset.addActionListener(this);
        buttonClear.addActionListener(this);
        buttonFormat.addActionListener(this);
        buttonRead.addActionListener(this);
        buttonWrite.addActionListener(this);
        buttonQuit.addActionListener(this);
        buttonAuthenticate.addActionListener(this);
        
        textFieldFileID1.addKeyListener(this);
        textFieldFileID2.addKeyListener(this);
        textFieldFileLen1.addKeyListener(this);
        textFieldFileLen2.addKeyListener(this);
        textFieldFID1.addKeyListener(this);
        textFieldFID2.addKeyListener(this);
        textFieldOffset1.addKeyListener(this);
        textFieldOffset2.addKeyListener(this);
        textFieldLen.addKeyListener(this);
        textFieldCardKey.addKeyListener(this);
        textFieldTerminalKey.addKeyListener(this);
        
    }

	public void actionPerformed(ActionEvent e) 
	{
		
		if(buttonInitialize == e.getSource())
		{			
			String[] readerList = null;
			
		    try
		    {
				comboBoxReader_1.removeAllItems();
				
			    readerList = readerInterface.listTerminals();					
				if (readerList.length == 0)
				{
					displayOut(0, 0, "No PC/SC reader detected");
					return;
				}				
					
				for (int i = 0; i < readerList.length; i++)
				{
					if (!readerList.equals(""))	
						comboBoxReader_1.addItem(readerList[i]);
					else
						break;
				}			    
					
				displayOut(0, 0, "Initialize success\n");
				comboBoxReader_1.setSelectedIndex(0);
				buttonConnect.setEnabled(true);
		    }
		    catch (Exception ex)
		    {
		    	buttonConnect.setEnabled(false);
		    	displayOut(0, 0, "Cannot find a smart card reader.");
		    	JOptionPane.showMessageDialog(null, "Cannot find a smart card reader.", "Error", JOptionPane.ERROR_MESSAGE);
		    }
		}  // Init
		
		if(buttonConnect == e.getSource())
		{	
			try
			{
				
				if(readerInterface.isConnectionActive())	
					readerInterface.disconnect();
				
				String rdrcon = (String)comboBoxReader_1.getSelectedItem();
				
				readerInterface.connect(rdrcon, "*");
				acos3 = new Acos3(readerInterface);				
				
				displayOut(0, 0, "Successful connection to " + rdrcon + "\n");
				
				//Check if card inserted is an ACOS Card
				if(!isAcos3())
					return;
				
				if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI)
					displayOut(0, 0, "Chip Type: ACOS3 Combi");
				else if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3CONTACTLESS)
					displayOut(0, 0, "Chip Type: ACOS3 Contactless");
				else
					displayOut(0, 0, "Chip Type: ACOS3");
				
				textFieldFileID1.setEnabled(true);
				textFieldFileID2.setEnabled(true);
				textFieldFileLen1.setEnabled(true);
				textFieldFileLen2.setEnabled(true);
				buttonFormat.setEnabled(true);
				textFieldCardKey.setEnabled(true);
				textFieldTerminalKey.setEnabled(true);
				checkBoxSecureMessaging.setEnabled(true);
				radioButtonDES.setSelected(true);
				radioButtonDES.setEnabled(true);
				radioButton3DES.setEnabled(true);
				
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
		}  // Connect
		
		
		if(buttonClear == e.getSource())
		{	
			textAreaMessage.setText("");
		}  // Clear
		
		
		if(buttonQuit == e.getSource())
		{	
			this.dispose();
		}  // Quit
		
		
		if(buttonReset == e.getSource())
		{	
			try			
			{
				//disconnect
				if (readerInterface.isConnectionActive())
					readerInterface.disconnect();
				
				textAreaMessage.setText("");
				initMenu();
				comboBoxReader_1.removeAllItems();
				comboBoxReader_1.addItem("Please select reader                   ");				
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
		}  // Reset
		
		if (radioButton3DES == e.getSource())
		{	
			textFieldCardKey.setText("");
			textFieldTerminalKey.setText("");	
		}
		
		if (radioButtonDES == e.getSource())
		{
			textFieldCardKey.setText("");
			textFieldTerminalKey.setText("");	
		}
		
		if(buttonFormat == e.getSource())
		{			
			try
			{
				byte[] fileLength = new byte[2];
	            byte[] fileId = new byte[2];            
	            
	            String tmpStr = "";			
				byte[] buff = new byte[8];				
				
				if(!validateCardFormatRoutine())
					return;
				
				fileId[0] = (byte)((Integer)Integer.parseInt(textFieldFileID1.getText(), 16)).byteValue();
				fileId[1] = (byte)((Integer)Integer.parseInt(textFieldFileID2.getText(), 16)).byteValue();				
				
				fileLength[0] = (byte)((Integer)Integer.parseInt(textFieldFileLen1.getText(), 16)).byteValue();				
				fileLength[1] = (byte)((Integer)Integer.parseInt(textFieldFileLen2.getText(), 16)).byteValue();
				
				//Format Card
				displayOut(0, 0, "\nSubmit Code - IC");
				acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
				
				displayOut(0, 0, "\nClear Card");
				acos3.clearCard();
				
				readerInterface.disconnect();
				readerInterface.connect(comboBoxReader_1.getSelectedItem().toString(), "*");
				
				displayOut(0, 0, "\nSubmit Code - IC");
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
	            displayOut(0, 0, "\nSelect File - FF 02");
	            displayOut(0, 0, "Write Record");
	            acos3.configurePersonalizationFile(optionRegister, securityOptionRegister, (byte)0x03);
	            displayOut(0, 0, "FF 02 is updated");
	            
	            //Select Security File
	            displayOut(0, 0, "\nSelect File - FF 03");
	            acos3.selectFile(Acos3.INTERNAL_FILE.SECURITY_FILE);
	            
	            displayOut(0, 0, "\nSubmit Code - IC");
	            acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
	            
	            int tmpInt = 0;
	            
	            if(radioButtonDES.isSelected())
				{				
					//record 02 for card key
					tmpStr = textFieldCardKey.getText();
					for (int i = 0; i < 8; i++)
					{
					    tmpInt = (int)tmpStr.charAt(i);
					    buff[i] = (byte) tmpInt;
					}
					
					displayOut(0, 0, "\nWrite Record");
					acos3.writeRecord((byte)0x02, (byte)0x00, buff);	
					
					//record 03 for terminal key
					tmpStr = textFieldTerminalKey.getText();
					for (int i = 0; i < 8; i++)
					{
					    tmpInt = (int)tmpStr.charAt(i);
					    buff[i] = (byte) tmpInt;
					}		
					displayOut(0, 0, "\nWrite Record");
					acos3.writeRecord((byte)0x03, (byte)0x00, buff);
				}
				else
				{
					//record 02 of cardkey
					tmpStr = textFieldCardKey.getText();
					for (int i = 0; i < 8; i++)
					{
					    tmpInt = (int)tmpStr.charAt(i);
					    buff[i] = (byte) tmpInt;
					}	
					displayOut(0, 0, "\nWrite Record");
					acos3.writeRecord((byte)0x02, (byte)0x00, buff);				
					
					//record 12 for cardkey
					tmpStr = textFieldCardKey.getText();
					for (int i = 0; i < 8; i++)
					{
					    tmpInt = (int)tmpStr.charAt(i+8);
					    buff[i] = (byte) tmpInt;
					}
					displayOut(0, 0, "\nWrite Record");
					acos3.writeRecord((byte)0x0C, (byte)0x00, buff);
					
					//record 03 for terminal key
					tmpStr = textFieldTerminalKey.getText();
					for (int i = 0; i < 8; i++)
					{
					    tmpInt = (int)tmpStr.charAt(i);
					    buff[i] = (byte) tmpInt;
					}
					displayOut(0, 0, "\nWrite Record");
					acos3.writeRecord((byte)0x03, (byte)0x00, buff);				
					
					//record 13 for terminal key
					tmpStr = textFieldTerminalKey.getText();
					for (int i = 0; i < 8; i++)
					{
					    tmpInt = (int)tmpStr.charAt(i+8);
					    buff[i] = (byte) tmpInt;
					}				

					displayOut(0, 0, "\nWrite Record");
					acos3.writeRecord((byte)0x0D, (byte)0x00, buff);
				}
	            
	            displayOut(0,0,"FF 03 is updated");
	            
	            //Select User File Management File
	            displayOut(0, 0, "\nSelect File - FF 04");
	            acos3.selectFile(Acos3.INTERNAL_FILE.USER_FILE_MGMT_FILE);
	            
	            displayOut(0, 0, "\nSubmit Code - IC");
	            acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
	            
	            //Set the read and write security attributes
	            //See Data File Access Control of the ACOS3 Reference Manual for more information
	            SecurityAttribute readSecurityAttribute = new SecurityAttribute();
	            
	            //Set read to free access
	            readSecurityAttribute.setAccessCondition1(false);
	            readSecurityAttribute.setAccessCondition2(false);
	            readSecurityAttribute.setAccessCondition3(false);
	            readSecurityAttribute.setAccessCondition4(false);
	            readSecurityAttribute.setAccessCondition5(false);
	            readSecurityAttribute.setIssuerCode(false);
	            readSecurityAttribute.setPin(false);
	            
	            SecurityAttribute writeSecurityAttribute = new SecurityAttribute();
	            
	            //Set write to free access
	            writeSecurityAttribute.setAccessCondition1(false);
	            writeSecurityAttribute.setAccessCondition2(false);
	            writeSecurityAttribute.setAccessCondition3(false);
	            writeSecurityAttribute.setAccessCondition4(false);
	            writeSecurityAttribute.setAccessCondition5(false);
	            writeSecurityAttribute.setIssuerCode(false);
	            writeSecurityAttribute.setPin(false);
	            
	            displayOut(0, 0, "\nCreate Binary File");
	            
				if(checkBoxSecureMessaging.isSelected())
					acos3.createBinaryFile(fileId, Helper.byteToInt(fileLength), writeSecurityAttribute, readSecurityAttribute, true, true);
				else
					acos3.createBinaryFile(fileId, Helper.byteToInt(fileLength), writeSecurityAttribute, readSecurityAttribute, false, false);
				
				displayOut(0, 0, "Format Card successful");
				
				buttonAuthenticate.setEnabled(true);
				
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
		}  // Format
		
		
		if(buttonRead == e.getSource())
		{	
			try
			{
				byte fileID1, fileID2, hiByte, loByte, tmpLen;
				
				if(!validateRead())
					return;
				
				fileID1 = (byte)((Integer)Integer.parseInt(textFieldFID1.getText(), 16)).byteValue();
				fileID2 = (byte)((Integer)Integer.parseInt(textFieldFID2.getText(), 16)).byteValue();
				
				if(textFieldOffset1.getText().equals(""))
					hiByte = (byte) 0x00;
				else
					hiByte = ((Integer)Integer.parseInt(textFieldOffset1.getText(), 16)).byteValue();
				
				loByte = (byte)((Integer)Integer.parseInt(textFieldOffset2.getText(), 16)).byteValue();
				tmpLen = (byte)((Integer)Integer.parseInt(textFieldLen.getText(), 16)).byteValue();
				
				//select user file
				displayOut(0, 0, "\nSelect File");
				acos3.selectFile(new byte[] { fileID1, fileID2 });			
				
				//read binary
				displayOut(0, 0, "\nRead Binary");
				if(checkBoxSecureMessaging.isSelected())
				{	
					retCode = readBinarySM(hiByte, loByte, Integer.parseInt(textFieldLen.getText(), 16));
				}
				else
				{				
					byte[] readData = new byte[Integer.parseInt(textFieldLen.getText(), 16)];
					readData = acos3.readBinary(hiByte, loByte, (byte)tmpLen);
					String data = Helper.byteArrayToString(readData, Integer.parseInt(textFieldLen.getText(), 16));
					textAreaData.setText(data);
				}
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
		}  // Read
		
		
		if(buttonWrite == e.getSource())
		{			
			try
			{
				String tmpStr = "";
				byte hiByte, loByte, fileID1, fileID2;
				int tmpLen;
				byte[] tmpArray = new byte[255];

				if(!validateWrite())
					return;
				
				fileID1 = (byte)((Integer)Integer.parseInt(textFieldFID1.getText(), 16)).byteValue();
				fileID2 = (byte)((Integer)Integer.parseInt(textFieldFID2.getText(), 16)).byteValue();
				
				if(textFieldOffset1.getText().equals(""))
					hiByte = (byte)0x00;
				else
					hiByte = (byte)((Integer)Integer.parseInt(textFieldOffset1.getText(), 16)).byteValue();
				
				loByte = (byte)((Integer)Integer.parseInt(textFieldOffset2.getText(), 16)).byteValue();
				tmpLen = (Integer)Integer.parseInt(textFieldLen.getText(), 16);
								
				//select user file
				displayOut(0, 0, "\nSelect File");
				acos3.selectFile(new byte[] { fileID1, fileID2 });			
				
				//write input data to card			
				tmpStr = textAreaData.getText();
				int tmpStrLength = tmpStr.length();
				int tmpInt;			
				tmpArray = new byte[tmpLen];
				
				for(int i = 0 ; i < tmpLen; i++)
				{				
					if(i > (tmpStrLength - 1))
					{
						tmpArray[i] = 0x00;
					} else
					{
						tmpInt = (int)tmpStr.charAt(i);
						tmpArray[i] = (byte) tmpInt;
					}
				}
				
				displayOut(0, 0, "\nWrite Binary");
				if(checkBoxSecureMessaging.isSelected())
					writeBinarySM((byte)hiByte, (byte)loByte, tmpLen, tmpArray);
				else
				{
					acos3.writeBinary((byte)hiByte, (byte)loByte, tmpArray);
					displayOut(0, 0, "Write Binary successful\r\n");
				}
				
				textAreaData.setText("");
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
			
		}  // Write
		
		
		if(buttonAuthenticate == e.getSource())
		{
			try
			{
				byte [] cKey = new byte[16];
				byte [] tKey = new byte[16];			
				byte [] ReverseKey = new byte[16];
				byte[] tripleDesResult = new byte[8];
				String tmpStr = "";
							
				if(!validateKeys())
					return;
				
				displayOut(0,0,"\nStart Session");
				CRnd = acos3.startSession();
				
				for(int i = 0; i < 6; i++)
					SeqNum[i] = (byte)0x00;
					
				SeqNum[6] = CRnd[6];
				SeqNum[7] = CRnd[7];
				
				// Retrieve Terminal Key from Input Template
				int indx, tmpInt;
				tempArray = new byte[32];
				
				tmpStr = textFieldTerminalKey.getText();			
				for(int i = 0; i < textFieldTerminalKey.getText().length(); i++)
					tKey[i] = (byte)((int)tmpStr.charAt(i));		
				
				//  Encrypt Random No (CRnd) with Terminal Key (tKey)
				//    tmpArray will hold the 8-byte Enrypted number
				for (indx = 0; indx < 8; indx++)
					tempArray[indx] = CRnd[indx];
				
				byte[] responseData = new byte[8];
				byte[] response = new byte[10];
				
				if (radioButtonDES.isSelected()) 				
				{
					DES(tempArray, tKey);
					
					//  Issue Authenticate command using 8-byte Encrypted No (tmpArray)
					//    and Random Terminal number (TRnd)
					for (indx = 0; indx < 8; indx++)
						responseData[indx] = tempArray[indx];
				}
				else
				{
					tripleDesResult = tripleDES(tempArray,tKey);
					
					//  Issue Authenticate command using 8-byte Encrypted No (tmpArray)
					//    and Random Terminal number (TRnd)
					for (indx = 0; indx < 8; indx++)
						responseData[indx] = tripleDesResult[indx];
				}

				displayOut(0,0,"\nAuthenticate");
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
						tempArray[indx] = CRnd[indx];
	
					DES(tempArray, cKey);
					
					// XOR the result with tRnd
					for (indx = 0; indx < 8; indx++)
						tempArray[indx] = (byte)(tempArray[indx] ^ TRnd[indx]);
	            
					// DES the result with tKey
					DES(tempArray,tKey);
	
					// tmpArray now holds the SessionKey
					for (indx = 0; indx < 8; indx++)
						SessionKey[indx] = tempArray[indx];
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
						tempArray[indx] = CRnd[indx];
	
					tripleDesResult = tripleDES(tempArray, cKey);
					
					// 3DES the result with tKey
					tripleDesResult = tripleDES(tripleDesResult,tKey);
					
					// tmpArray holds the left half of SessionKey
					for (indx=0; indx<8;indx++)
						SessionKey[indx] = tripleDesResult[indx];
	
					/* compute ReverseKey of tKey
					' just swap its left side with right side
					' ReverseKey = right half of tKey + left half of tKey */
					for (indx = 0;indx < 8; indx++)
						ReverseKey[indx] = tKey[8 + indx];
	           
					for (indx = 0;indx < 8;indx++)
						ReverseKey[8 + indx] = tKey[indx];
	
					// compute tmpArray = 3DES (TRnd, ReverseKey)
					for (indx = 0; indx < 8; indx++)
						tempArray[indx] = TRnd[indx];
	
					tripleDesResult = tripleDES(tempArray, ReverseKey);
	
					// tmpArray holds the right half of SessionKey
					for (indx = 0; indx < 8; indx++)
						SessionKey[indx + 8] = tripleDesResult[indx];	
				}
				
				// compute DES (TRnd, SessionKey)
				for (indx = 0; indx < 8;indx++)
					tempArray[indx] = TRnd[indx];
				
				if (radioButtonDES.isSelected()) 
				{
					DES(tempArray,SessionKey);
					
					for (indx = 0; indx < 8;indx++)
					{
						if (tmpResult[indx] != tempArray[indx]) 
						{				
							displayOut(0, 0, "Card Response and Terminal Response do not match.");
							displayOut(0, 0, "Mutual Authentication failed.");
							return;
						}
					}
				}
				else 
				{
					tripleDesResult = tripleDES(tempArray,SessionKey);
					
					for (indx = 0; indx < 8;indx++)
					{
						if (tmpResult[indx] != tripleDesResult[indx]) 
						{				
							displayOut(0, 0, "Card Response and Terminal Response do not match.");
							displayOut(0, 0, "Mutual Authentication failed.");
							return;
						}
					}
				}

				textFieldFID1.setEnabled(true);
				textFieldFID2.setEnabled(true);
				textFieldOffset1.setEnabled(true);
				textFieldOffset2.setEnabled(true);
				textFieldLen.setEnabled(true);
				textAreaData.setEnabled(true);
				buttonRead.setEnabled(true);
				buttonWrite.setEnabled(true);
					
				displayOut(0,0,"Mutual Authentication is successful.");
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
		}  // Authenticate
				
	}
	
	
	public int readBinarySM(byte hiByte, byte loByte, int dataLen)
	{
		try
		{
			byte [] buff = new byte[256];
			int lastblk;
			int L, Pi;		
		
			//build TLV < 89 04 CLA INS P1 P2><97 01 P3>
			buff[0] = (byte) 0x89;
			buff[1] = (byte) 0x04;
			buff[2] = (byte) (0x80 | 0x0C);
			buff[3] = (byte) 0xB0;
			buff[4] =  hiByte;
			buff[5] =  loByte;
			buff[6] = (byte) 0x97;
			buff[7] = (byte) 0x01;
			buff[8] = (byte) (dataLen & 0xFF);
			
			//increment SeqNum
			if((SeqNum[7] +1)==256)
				SeqNum[6] = (byte)(SeqNum[6] + 1);
			else
				SeqNum[7] = (byte)(SeqNum[7] + 1);
			
			//last block of buff will have the MAC
			lastblk = ENC_CBC(SeqNum, buff, 9, SessionKey);
			
			//increment SeqNum
			if((SeqNum[7] +1)==256)
				SeqNum[6] = (byte)(SeqNum[6] + 1);
			else
				SeqNum[7] = (byte)(SeqNum[7] + 1);
			
			Apdu apdu;
			
			apdu = new Apdu();
			apdu.setCommand(new byte[] {(byte)(0x80 | 0x0C), (byte)0xB0, (byte)hiByte,
							(byte)loByte, (byte)0x09});
			
			byte[] sendData = new byte[9];
			sendData[0] = (byte)0x97;
			sendData[1] = (byte)0x01;
			sendData[2] = (byte)(dataLen & 0xFF);			
			sendData[3] = (byte)0x8E;
			sendData[4] = (byte)0x04;
			
			int tmp = lastblk;
			for(int i = 5; i < 9; i++ )
			{
				sendData[i] = buff[tmp];
				tmp+=1;
			}
			
			apdu.setSendData(sendData);
			
			readerInterface.sendApduCommand(apdu);
			
			if (!((apdu.getSw()[0] == (byte)0x61) || (apdu.getSw()[0] == (byte)0x90))) 			
				throw new Exception (acos3.getErrorMessage(apdu.getSw()));
						
			byte[] receiveData;
			if (apdu.getSw()[0] == (byte)0x61)
			{
				//get card's SM Response
				receiveData = new byte[apdu.getSw()[1] & 0xFF];
				receiveData = acos3.getResponse(apdu.getSw()[1]);		
			}
			else
			{
				//get card's SM Response
				receiveData = new byte[apdu.getReceiveData().length];
				for (int x = 0; x < apdu.getReceiveData().length; x++)
					receiveData[x] = apdu.getReceiveData()[x];
			}
			
			byte[] statusWord = new byte[2];
			
			if(receiveData[2] == (byte) 0x67 && receiveData[3] == (byte)0x00)
			{
				statusWord[0] = receiveData[2];
				statusWord[1] = receiveData[3];
				throw new Exception(acos3.getErrorMessage(statusWord));
			}
			if(receiveData[2] == (byte) 0x6A && receiveData[3] == (byte)0x83)
			{
				statusWord[0] = receiveData[2];
				statusWord[1] = receiveData[3];
				throw new Exception(acos3.getErrorMessage(statusWord));
			}
			if (receiveData[0] != (byte)0x87)	
			{
				statusWord[0] = receiveData[2];
				statusWord[1] = receiveData[3];
				throw new Exception(acos3.getErrorMessage(statusWord));
			}
			
			if ((receiveData[(receiveData[1] & 0xFF) + 2] != (byte)0x99) || (receiveData[(receiveData[1]  & 0xFF) + 3] != (byte)0x02))			
			{
				statusWord[0] = receiveData[(receiveData[1] & 0xFF) + 4];
				statusWord[1] = receiveData[(receiveData[1] & 0xFF) + 5];
				throw new Exception(acos3.getErrorMessage(statusWord));	
			}
			// check Status Words of Read Binary command
			if ((receiveData[(receiveData[1] & 0xFF) + 4] != (byte)0x90) || (receiveData[(receiveData[1] & 0xFF) + 5] != (byte)0x00))
			{
				statusWord[0] = receiveData[(receiveData[1] & 0xFF) + 4];
				statusWord[1] = receiveData[(receiveData[1] & 0xFF) + 5];
				throw new Exception(acos3.getErrorMessage(statusWord));	
			}
			
			if ((receiveData[(receiveData[1] & 0xFF) + 6] != (byte)0x8E) || (receiveData[(receiveData[1] & 0xFF) + 7] != (byte)0x04))			
			{
				statusWord[0] = receiveData[(receiveData[1] & 0xFF) + 4];
				statusWord[1] = receiveData[(receiveData[1] & 0xFF) + 5];
				throw new Exception(acos3.getErrorMessage(statusWord));	
			}
			
			//get ENC_Data and decrypt it
			L = (receiveData[1] & 0xFF);
			Pi = (receiveData[2] & 0xFF);
			
			for(int i = 0; i < L; i++)
				buff[i] = (byte) (receiveData[3+i] & 0xFF);
			
			DEC_CBC(SeqNum, buff, L-1, SessionKey);
			
			String tmpStr = "";
			int i = 0;
			int len = Integer.parseInt(textFieldLen.getText(), 16);
	
			while((buff[i] & 0xFF) != 0x00)
			{	
				if(i < len)
					tmpStr = tmpStr + (char)(buff[i] & 0xFF);
				i++;
				
				if (i == (Integer.parseInt(textFieldLen.getText(), 16)))
					break;
			}			
			
			textAreaData.setText(tmpStr);
			
			SW1 = (byte) (receiveData[L + 2 + 1 + 1] & 0xFF);
			SW2 = (byte) (receiveData[L + 2 + 1 + 1 + 1] & 0xFF);
			
			//get MAC, MAC length = 4
			buff[0] = (byte) 0x89;
			buff[1] = (byte) 0x04;
			buff[2] = (byte) 0x8C;
			buff[3] = (byte) 0xB0;
			buff[4] = (byte) hiByte;
			buff[5] = (byte) loByte;
			buff[6] = (byte) 0x87;
			buff[7] = (byte) L;
			buff[8] = (byte) Pi;
			
			i = 9;
			for(int j = 3; j < L+3; j++)
			{	
				buff[i] = receiveData[j];
				i = i + 1;
			}
			
			buff[9+L-1] = (byte)0x99;
			buff[9+L-1 + 1] = (byte)0x02;
			buff[9+L-1 + 2] = (byte)SW1;
			buff[9+L-1 + 3] = (byte)SW2;
			
			lastblk = ENC_CBC(SeqNum, buff, L + 6 + 2 + 4, SessionKey);
			
			for(int j = lastblk; j < lastblk + 4; j++)
			{			
				if(buff[j] != receiveData[j+1])
				{
					displayOut(0, 0, "MAC is Incorrect");
					return lastblk;
				}
			}
			
			displayOut(0, 0, "MAC is Correct");
			displayOut(0, 0, "Secure Messaging Success");
			
			displayOut(0, 0, "Read success");
			
			return 0;
		}
		catch (CardException exception)
		{
			displayOut(0, 0, PcscProvider.GetScardErrMsg(exception) + "\r\n");
			JOptionPane.showMessageDialog(null,PcscProvider.GetScardErrMsg(exception), "Error", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
		catch(Exception exception)
		{
			displayOut(0, 0, exception.getMessage().toString() + "\r\n");
			JOptionPane.showMessageDialog(null,exception.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
	}
	
	
	public int DEC_CBC(byte [] SeqNumber, byte [] buff, int length, byte [] key)
	{
		byte [] xorBlk = new byte[256];
		byte [] lastBlk = new byte[256];
		byte [] temp = new byte[256];
		byte [] tripleDesResult = new byte[8];
		int blockLen;
		
		if((length % 8) != 0)
			return -1;
			
		for(int i = 0; i < 8; i++)
			xorBlk[i] = SeqNum[i];
		
		blockLen = length /8;		
		
		for(int i = 0; i < blockLen; i++)
		{	
			for(int x = 0; x < 8; x++)
			{
				 temp[x] = lastBlk[x] = buff[x + (8*i)];
			}
			
			if(radioButtonDES.isSelected())
			{
				DES2(lastBlk, key);
				
				for(int x = 0; x < 8; x++)
					buff[x + (8*i)] = (byte)(lastBlk[x] ^ xorBlk[x]);
			}
			else
			{
				tripleDesResult = tripleDES2(lastBlk, SessionKey);
				
				for(int x = 0; x < 8; x++)
					buff[x + (8*i)] = (byte)(tripleDesResult[x] ^ xorBlk[x]);
			}
			
			for(int x = 0; x < 8; x++)
				xorBlk[x] = temp[x];
		}
		
		return (length -8);
	}
	
	
	public void writeBinarySM(byte hiByte, byte loByte, int dataLen, byte[] dataIn)
	{
		try
		{
			byte [] ENCData = new byte[256];
			byte [] buff = new byte[256];
			byte [] tmpArray = new byte[256];
			int lastblk = 0, pi;
			
			for (int i = 0; i < dataIn.length; i++)
				tmpArray[i] = dataIn[i];
		
			//prepare SeqNum
			if((SeqNum[7] + 1) == 256)
				SeqNum[6] = (byte)(SeqNum[6] + 1);
			else
				SeqNum[7] = (byte)(SeqNum[7] + 1);
				
			pi = (((dataLen % 8) - 8) % 8) * -1;
			
			ENC_CBC(SeqNum, tmpArray, dataLen, SessionKey);
							
			ENCData = new byte[dataLen + pi];
				
			for(int i = 0; i < dataLen + pi; i++)
				ENCData[i] = tmpArray[i];					
	
			//Build TLV
			buff[0] = (byte) 0x89;
			buff[1] = (byte) 0x04;
			buff[2] = (byte) (0x80 | 0x0C);
			buff[3] = (byte) 0xD0;
			buff[4] = hiByte;
			buff[5] = loByte;
			buff[6] = (byte) 0x87;
			buff[7] = (byte) (dataLen + pi + 1);
			buff[8] = (byte) pi;	
			
			// Append the encrypted data to the TLV
			for(int i = 0; i < dataLen + pi; i++)
				buff[i+9] = ENCData[i];
				
			lastblk = ENC_CBC(SeqNum, buff, 9 + dataLen + pi, SessionKey);
			
			Apdu apdu;			
			apdu = new Apdu();
			
			//prepare SM APDU
			apdu.setCommand(new byte[] {(byte)(0x80 | 0x0C), (byte)0xD0, (byte)hiByte,
							(byte)loByte, (byte)(9 + dataLen + pi)});
			
			byte[] sendData = new byte[9 + dataLen + pi];
			sendData[0] = (byte)0x87;
			sendData[1] = (byte)(dataLen + pi + 1);
			sendData[2] = (byte)pi;	
			
			//Append the Encrypted Data to the APDU
			for(int i = 0; i < dataLen + pi; i++)
				sendData[i+3] = ENCData[i];
			
			// Add the MAC Tags 0x8E 0x04 to the APDU
			sendData[3 + dataLen + pi] = (byte)0x8E;		// MAC Tag
			sendData[3 + dataLen + pi + 1] = (byte)0x04;	// MAC Length
			
			int j = 3 + dataLen + pi + 2;
			for(int i = lastblk; i < lastblk + 4; i++ )
			{
				sendData[j] = buff[i];
				j++;
			}
			
			apdu.setSendData(sendData);
			
			readerInterface.sendApduCommand(apdu);
			byte[] statusWord = new byte[2];
			
			if (!((apdu.getSw()[0] == (byte)0x61) || (apdu.getSw()[0] == (byte)0x90))) 		
				throw new Exception (acos3.getErrorMessage(apdu.getSw()));	
			
			byte[] responseData;
			if (apdu.getSw()[0] == (byte)0x61)
			{
				responseData = new byte[apdu.getSw()[1] & 0xFF];
				responseData = acos3.getResponse(apdu.getSw()[1]);
			}
			else				
			{
				responseData = new byte[apdu.getReceiveData().length];
				for (int x = 0; x < apdu.getReceiveData().length; x++)
					responseData[x] = apdu.getReceiveData()[x];
			}
			
			if(responseData[2] == (byte) 0x67 && responseData[3] == (byte)0x00)
			{
				statusWord[0] = responseData[2];
				statusWord[1] = responseData[3];
				throw new Exception(acos3.getErrorMessage(statusWord));
			}
			
			if(responseData[2] == (byte) 0x6A && responseData[3] == (byte)0x83)
			{
				statusWord[0] = responseData[2];
				statusWord[1] = responseData[3];
				throw new Exception(acos3.getErrorMessage(statusWord));
			}
			
			//increment SeqNum
			if((SeqNum[7] + 1) == 256)
				SeqNum[6] = (byte)(SeqNum[6] +1);
			else
				SeqNum[7] = (byte)(SeqNum[7] +1);
			
			if (responseData[0] != (byte)0x99)
			{
				statusWord[0] = responseData[2];
				statusWord[1] = responseData[3];
				throw new Exception(acos3.getErrorMessage(statusWord));
			}
			
			if (responseData[1] != (byte)0x02)
			{
				statusWord[0] = responseData[2];
				statusWord[1] = responseData[3];
				throw new Exception(acos3.getErrorMessage(statusWord));
			}
			
			// check Status Words of Write Binary command
			if (responseData[2] != (byte)0x90 || responseData[3] != (byte)0x00)
			{
				statusWord[0] = responseData[2];
				statusWord[1] = responseData[3];
				throw new Exception(acos3.getErrorMessage(statusWord));
			}			
			
			if (responseData[4] != (byte)0x8E)
			{
				statusWord[0] = responseData[2];
				statusWord[1] = responseData[3];
				throw new Exception(acos3.getErrorMessage(statusWord));
			}
			
			if (responseData[5] != (byte)0x04)
			{
				statusWord[0] = responseData[2];
				statusWord[1] = responseData[3];
				throw new Exception(acos3.getErrorMessage(statusWord));
			}
	
			SW1 = responseData[2];
			SW2 = responseData[3];
				
			buff[0] = (byte) 0x89;
			buff[1] = (byte) 0x04;
			buff[2] = (byte) 0x8C;
			buff[3] = (byte) 0xD2;
			buff[4] = (byte) 0x00;
			buff[5] = (byte) 0x00;
			buff[6] = (byte) 0x99;
			buff[7] = (byte) 0x02;
			buff[8] = (byte) SW1;
			buff[9] = (byte) SW2;
				
			lastblk = ENC_CBC(SeqNum, buff, 10, SessionKey);

			displayOut(0, 0, "Write Binary successful\r\n");
		}
		catch(Exception ex)
		{
			displayOut(0, 0, ex.getMessage().toString());
			JOptionPane.showMessageDialog(null,ex.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	public int ENC_CBC(byte [] SeqNumber, byte [] buff, int length, byte [] key)
	{
		int blocklen;		
		byte [] xorBlk = new byte[8];
		byte [] lstBlk = new byte[8];
		byte[] tripleDesResult = new byte[8];
		
		if((length % 8) != 0)
		{	
			buff[length] = (byte)0x80;
			length = length +1;
			
			while((length % 8) != 0)
			{	
				buff[length] = (byte) 0x00;
				length = length + 1;
			}	
		}
		
		for(int i = 0; i < key.length; i++)
			CipherKey[i] = key[i];
		
		blocklen = length /8;
		
		//initial vector
		for(int i = 0; i < 8; i++)
			xorBlk[i] = SeqNum[i];
					
		for(int i = 0; i < blocklen; i++)
		{
			//Get the block to be processed. This is the 8 bytes of data.
			for (int j = 0; j < 8; j++)
				lstBlk[j] = buff[j + (8 * i)];
			
			//Xor the current block with the xor block.
            for (int j = 0; j < 8; j++)
                lstBlk[j] = (byte)(lstBlk[j] ^ xorBlk[j]);
			
			if(radioButtonDES.isSelected())
			{
				DES(lstBlk, CipherKey);
				
				for(int j = 0; j < 8; j++)
				{	
	                //Make the latest encrypted block as the next xor block.
	                xorBlk[j] = lstBlk[j];

	                //Copy the encrypted data to the output
	                buff[j + (8 * i)] = lstBlk[j];				
				}
			}
			else
			{
				tripleDesResult = tripleDES(lstBlk, CipherKey);
				
				for(int j = 0; j < 8; j++)
				{	
	                //Make the latest encrypted block as the next xor block.
	                xorBlk[j] = tripleDesResult[j];

	                //Copy the encrypted data to the output
	                buff[j + (8 * i)] = tripleDesResult[j];				
				}
			}
			
			
		}
				
		return (length - 8);
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
	
	
	public static void DES2(byte Data[], byte key[])
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
           
            Cipher encryptCipher = Cipher.getInstance("DES/ECB/NoPadding");
            encryptCipher.init(Cipher.DECRYPT_MODE, secretKey);           
            
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
	
	public static byte[] tripleDES2(byte[] data, byte[] key) 
	{
        byte[] result = null;
        byte[] tmpKey = new byte[24];
        
        try 
        {
        	System.arraycopy(key, 0, tmpKey, 0, 16);
        	System.arraycopy(key, 0, tmpKey, 16, 8);
        	
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            SecretKeySpec myKey = new SecretKeySpec(tmpKey, "DESede");

            cipher.init(Cipher.DECRYPT_MODE, myKey);

            try 
            {
                result = cipher.doFinal(data);
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
        
        return result;
    }
		
	public static byte[] tripleDES(byte[] data, byte[] key) 
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
                result = cipher.doFinal(data);
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
        
        return result;
    }
		
	public void displayOut(int mType, int msgCode, String printText)
	{
		switch(mType)
		{	
			case 2: textAreaMessage.append("<< " + printText + "\n"); break;
			case 3: textAreaMessage.append(">> " + printText); break;
			default: textAreaMessage.append(printText + "\n");
		}	
	}
	
	
	public void initMenu()
	{			
		buttonConnect.setEnabled(false);
		buttonInitialize.setEnabled(true);
		textAreaMessage.setText("");
		textFieldFileID1.setText("");
		textFieldFileID1.setEnabled(false);
		textFieldFileID2.setText("");
		textFieldFileID2.setEnabled(false);
		textFieldFileLen1.setText("");
		textFieldFileLen1.setEnabled(false);
		textFieldFileLen2.setText("");
		textFieldFileLen2.setEnabled(false);
		buttonFormat.setEnabled(false);
		textFieldFID1.setText("");
		textFieldFID1.setEnabled(false);
		textFieldFID2.setText("");
		textFieldFID2.setEnabled(false);
		textFieldOffset1.setText("");
		textFieldOffset1.setEnabled(false);
		textFieldOffset2.setText("");
		textFieldOffset2.setEnabled(false);
		textFieldLen.setText("");
		textAreaData.setText("");
		buttonRead.setEnabled(false);
		buttonWrite.setEnabled(false);
		buttonAuthenticate.setEnabled(false);
		textFieldCardKey.setEnabled(false);
		textFieldTerminalKey.setEnabled(false);
		checkBoxSecureMessaging.setEnabled(false);
		checkBoxSecureMessaging.setSelected(false);
		radioButtonDES.setEnabled(false);
		radioButton3DES.setEnabled(false);
		radioButtonDES.setSelected(true);
		textFieldCardKey.setText("");
		textFieldTerminalKey.setText("");
		displayOut(0, 0, "Program Ready");
	}
	
	public void keyReleased(KeyEvent ke) {	}	
	
	public void keyPressed(KeyEvent ke) 
	{
  		//restrict paste actions
		if (ke.getKeyCode() == KeyEvent.VK_V ) 
			ke.setKeyCode(KeyEvent.VK_UNDO );						
  	}
	
	public void keyTyped(KeyEvent ke) 
	{	
  		Character x = (Character)ke.getKeyChar();
  		char empty = '\r';

  		//Check valid characters
  		if (VALIDCHARS.indexOf(x) == -1 ) 
  			ke.setKeyChar(empty);
  		
  		if(textFieldCardKey.isFocusOwner() || textFieldTerminalKey.isFocusOwner())
  		{
  			if (VALIDNUM.indexOf(x) == -1 ) 
  	  			ke.setKeyChar(empty);
  			
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
  		else
  		{ 			
			//Limit character length
	  		if (((JTextField)ke.getSource()).getText().length() >= 2 ) 
	  		{
	  			ke.setKeyChar(empty);
	  			return;
	  		}	  		
	  		
	  		if(textFieldLen.isFocusOwner())
	  		{
	  			textAreaData.setText("");		
	  		}
	  		
  		}		
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
                new ACOS3SecureMessaging().setVisible(true);
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
    		JOptionPane.showMessageDialog(null, "Card not supported. Please use ACOS3 Card.", "Error", JOptionPane.ERROR_MESSAGE);
    		return false;
    	}
    	return true;
    }
    
    public boolean validateKeys()
    {
    	if(textFieldCardKey.getText().equals(""))
    	{
    		JOptionPane.showMessageDialog(null, "Please key-in numeric value for Card Key.", "Error", JOptionPane.ERROR_MESSAGE);
    		textFieldCardKey.requestFocus();
    		return false;
    	}
    	
    	if(textFieldTerminalKey.getText().equals(""))
    	{
    		JOptionPane.showMessageDialog(null, "Please key-in numeric value for Terminal Key.", "Error", JOptionPane.ERROR_MESSAGE);
    		textFieldTerminalKey.requestFocus();
    		return false;
    	}    	

    	if(!(textFieldCardKey.getText().chars().allMatch(Character:: isDigit)))
    	{
    		JOptionPane.showMessageDialog(null, "Please key-in numeric value for Card Key.", "Error", JOptionPane.ERROR_MESSAGE);
    		textFieldCardKey.requestFocus();
    		return false;
    	}
    	
    	if(!(textFieldTerminalKey.getText().chars().allMatch(Character:: isDigit)))
    	{
    		JOptionPane.showMessageDialog(null, "Please key-in numeric value for Terminal Key.", "Error", JOptionPane.ERROR_MESSAGE);
    		textFieldTerminalKey.requestFocus();
    		return false;
    	}
    	
    	int length = 8;
    	if(radioButton3DES.isSelected())
    		length = 16;
    	
    	if(textFieldCardKey.getText().length() < length)
    	{
    		JOptionPane.showMessageDialog(null, "Invalid input length. Length must be " + length + ".", "Error", JOptionPane.ERROR_MESSAGE);
    		textFieldCardKey.requestFocus();
    		return false;    		
    	}
    	
    	if(textFieldTerminalKey.getText().length() < length)
    	{
    		JOptionPane.showMessageDialog(null, "Invalid input length. Length must be " + length + ".", "Error", JOptionPane.ERROR_MESSAGE);
    		textFieldTerminalKey.requestFocus();
    		return false;    		
    	} 	
    	
    	return true;
    }
    
    public boolean validateCardFormatRoutine()
    {   
    	if(!validateKeys())
    		return false;
    	
    	if(textFieldFileID1.getText().equals(""))
		{	
			JOptionPane.showMessageDialog(null, "Please key-in hex value for File ID.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldFileID1.requestFocus();
			return false;
		}
		if(textFieldFileID2.getText().equals(""))
		{	
			JOptionPane.showMessageDialog(null, "Please key-in hex value for File ID.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldFileID2.requestFocus();
			return false;
		}
		
		if(textFieldFileLen1.getText().equals(""))
		{	
			JOptionPane.showMessageDialog(null, "Please key-in hex value for Length.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldFileLen1.requestFocus();
			return false;
		}
		
		if(textFieldFileLen2.getText().equals(""))
		{	
			JOptionPane.showMessageDialog(null, "Please key-in hex value for Length.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldFileLen2.requestFocus();
			return false;
		}	
    	
		byte[] fileLength = new byte[2];
		fileLength[0] = (byte)((Integer)Integer.parseInt(textFieldFileLen1.getText(), 16)).byteValue();				
		fileLength[1] = (byte)((Integer)Integer.parseInt(textFieldFileLen2.getText(), 16)).byteValue();
		
		if(Helper.byteToInt(fileLength) <= 0)
		{
			JOptionPane.showMessageDialog(null, "Please key-in valid length. Valid value: 01h - FFFFh.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldLen.requestFocus();
			return false;
		}
		
    	return true;
    }
    
    public boolean validateRead()
    {
    	if(textFieldFID1.getText().equals(""))
		{	
			JOptionPane.showMessageDialog(null, "Please key-in hex value for File ID.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldFID1.requestFocus();
			return false;
		}
		if(textFieldFID2.getText().equals(""))
		{	
			JOptionPane.showMessageDialog(null, "Please key-in hex value for File ID.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldFID2.requestFocus();
			return false;
		}
		
		if(textFieldOffset1.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Please key-in hex value for Offset.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldOffset1.requestFocus();
			return false;
		}
		
		if(textFieldOffset2.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Please key-in hex value for Offset.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldOffset2.requestFocus();
			return false;
		}			
		if(textFieldLen.getText().equals(""))
		{	
			JOptionPane.showMessageDialog(null, "Please key-in hex value for Length.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldLen.requestFocus();
			return false;
		}		
		
		int tmpLen = Integer.parseInt(textFieldLen.getText(), 16);
		
		if(tmpLen <= 0)
		{
			JOptionPane.showMessageDialog(null, "Please key-in valid length. Valid value: 01h - FFh.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldLen.requestFocus();
			return false;
		}
    	
		return true;
    }
    
    public boolean validateWrite()
    {    	
    	if(!validateRead())
    		return false;
    	
		if(textAreaData.getText().length() <= 0)
		{
			JOptionPane.showMessageDialog(null, "Please key-in data to write.", "Error", JOptionPane.ERROR_MESSAGE);
			textAreaData.requestFocus();
			return false;				
		}
		
    	return true;
    }
}