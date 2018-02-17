package CardProgramming;
/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              ACOS3ReadWriteBinary.java

  Description:       This sample program outlines the steps on how to
                     use the ACOS card for Reading and Writing in Binary
                     process using the PC/SC platform.
                    
  Author:            Donn Johnson A. Fabian

  Date:              October 11, 2012

  Revision Trail:   (Date/Author/Description)
  					 04-15-2010 / M.J.E.Castillo / Bug Fixes

======================================================================*/

import java.awt.*;
import java.awt.event.*;

import javax.smartcardio.CardException;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ACOS3ReadWriteBinary extends JFrame implements ActionListener, KeyListener, ReaderEvents.TransmitApduHandler{
	
	static String VALIDCHARS = "ABCDEFabcdef0123456789";
	
	PcscReader pcscReader;
	Acos3 acos3;
	
	//GUI Variables
    private JButton buttonInitialize, buttonConnect, buttonFormat, buttonRead, buttonWrite, buttonClear, buttonReset, buttonQuit;
    private JComboBox comboBoxReader;
    private JLabel labelData, labelFileID, labelID, labelLen, labelLength, labelOffset, labelReader;
    private JTextArea textAreaData, textAreaMessage;
    private JPanel messagePanel, readerPanel, cardFormatPanel, binaryPanel;
    private JScrollPane scrollPaneData, scrollPaneMessage;
    private JTextField textFieldFID1, textFieldFID2, textFieldFileID1, textFieldFileID2, textFieldFileLen1, textFieldFileLen2, textFieldLen, textFieldOffset1, textFieldOffset2;
	
    private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;
    private OptionRegister optionRegister;
    private SecurityOptionRegister securityOptionRegister;
    private SecurityAttribute readSecurityAttribute;
    private SecurityAttribute writeSecurityAttribute;
    
    public ACOS3ReadWriteBinary() 
    {	
    	this.setTitle("ACOS3 Binary Files");
        initComponents();
        initMenu();
        
        textAreaMessage.setForeground(Color.black);
        textAreaMessage.setEditable(false);
        getContentPane().setLayout(null);
        getContentPane().add(readerPanel);
        getContentPane().add(binaryPanel);
        binaryPanel.setLayout(null);
        binaryPanel.add(labelID);
        binaryPanel.add(labelOffset);
        binaryPanel.add(textFieldOffset1);
        binaryPanel.add(textFieldOffset2);
        binaryPanel.add(labelLen);
        binaryPanel.add(textFieldLen);
        binaryPanel.add(textFieldFID1);
        binaryPanel.add(textFieldFID2);
        binaryPanel.add(labelData);
        binaryPanel.add(scrollPaneData);
        binaryPanel.add(buttonWrite);
        binaryPanel.add(buttonRead);
        getContentPane().add(cardFormatPanel);
        cardFormatPanel.setLayout(null);
        cardFormatPanel.add(labelLength);
        cardFormatPanel.add(labelFileID);
        cardFormatPanel.add(textFieldFileLen1);
        cardFormatPanel.add(textFieldFileID1);
        cardFormatPanel.add(textFieldFileLen2);
        cardFormatPanel.add(textFieldFileID2);
        cardFormatPanel.add(buttonFormat);
        getContentPane().add(messagePanel);
        
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
		setSize(657,485);
		setResizable(false);
   	  	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//sets the location of the form at the center of screen
   		Dimension dim = getToolkit().getScreenSize();
   		Rectangle abounds = getBounds();
   		setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
   		requestFocus();
   		
   		String[] rdrNameDef = {"Please select reader             "};	
		
        readerPanel = new JPanel();
        readerPanel.setBounds(10, 11, 289, 114);
        labelReader = new JLabel();
        buttonInitialize = new JButton();
        buttonConnect = new JButton();
        cardFormatPanel = new JPanel();
        cardFormatPanel.setBounds(10, 130, 289, 80);
        labelFileID = new JLabel();
        labelFileID.setBounds(10, 23, 46, 14);
        labelLength = new JLabel();
        labelLength.setBounds(10, 52, 46, 14);
        textFieldFileID1 = new JTextField();
        textFieldFileID1.setBounds(66, 20, 34, 20);
        textFieldFileID1.setTransferHandler(null);
        textFieldFileID2 = new JTextField();
        textFieldFileID2.setBounds(105, 20, 34, 20);
        textFieldFileID2.setTransferHandler(null);
        textFieldFileLen1 = new JTextField();
        textFieldFileLen1.setBounds(66, 49, 34, 20);
        textFieldFileLen1.setTransferHandler(null);
        textFieldFileLen2 = new JTextField();
        textFieldFileLen2.setBounds(105, 49, 34, 20);
        textFieldFileLen2.setTransferHandler(null);
        buttonFormat = new JButton();
        buttonFormat.setBounds(159, 33, 114, 23);
        binaryPanel = new JPanel();
        binaryPanel.setBounds(10, 214, 289, 232);
        labelID = new JLabel();
        labelID.setBounds(16, 27, 62, 14);
        labelOffset = new JLabel();
        labelOffset.setBounds(16, 53, 62, 14);
        textFieldFID1 = new JTextField();
        textFieldFID1.setBounds(88, 24, 35, 20);
        textFieldFID1.setTransferHandler(null);
        textFieldFID2 = new JTextField();
        textFieldFID2.setBounds(129, 24, 35, 20);
        textFieldFID2.setTransferHandler(null);
        textFieldOffset1 = new JTextField();
        textFieldOffset1.setBounds(88, 50, 35, 20);
        textFieldOffset1.setTransferHandler(null);
        textFieldOffset2 = new JTextField();
        textFieldOffset2.setBounds(129, 50, 35, 20);
        textFieldOffset2.setTransferHandler(null);
        labelLen = new JLabel();
        labelLen.setHorizontalAlignment(SwingConstants.RIGHT);
        labelLen.setBounds(174, 53, 56, 14);
        textFieldLen = new JTextField();
        textFieldLen.setBounds(240, 50, 35, 20);
        textFieldLen.setTransferHandler(null);
        labelData = new JLabel();
        labelData.setBounds(16, 78, 62, 14);
        scrollPaneData = new JScrollPane();
        scrollPaneData.setBounds(16, 98, 259, 57);
        textAreaData = new JTextArea();
        textAreaData.setTransferHandler(null);
        buttonRead = new JButton();
        buttonRead.setBounds(148, 167, 125, 23);
        buttonWrite = new JButton();
        buttonWrite.setBounds(148, 198, 125, 23);
        messagePanel = new JPanel();
        messagePanel.setBounds(305, 11, 338, 435);
        scrollPaneMessage = new JScrollPane();
        textAreaMessage = new JTextArea();
        textAreaMessage.setWrapStyleWord(true);
        textAreaMessage.setTransferHandler(null);
        buttonClear = new JButton();
        buttonReset = new JButton();
        buttonQuit = new JButton();
        comboBoxReader = new JComboBox(rdrNameDef);
		comboBoxReader.setSelectedIndex(0);
		
        labelReader.setText("Select Reader");

		buttonInitialize.setText("Initalize");
        buttonConnect.setText("Connect");

        GroupLayout gl_rdrPanel = new GroupLayout(readerPanel);
        gl_rdrPanel.setHorizontalGroup(
        	gl_rdrPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_rdrPanel.createSequentialGroup()
        			.addGroup(gl_rdrPanel.createParallelGroup(Alignment.LEADING)
        				.addGroup(Alignment.TRAILING, gl_rdrPanel.createSequentialGroup()
        					.addGap(154)
        					.addComponent(buttonConnect, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
        				.addGroup(Alignment.TRAILING, gl_rdrPanel.createSequentialGroup()
        					.addGap(154)
        					.addComponent(buttonInitialize, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
        				.addGroup(Alignment.TRAILING, gl_rdrPanel.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(comboBoxReader, 0, 269, Short.MAX_VALUE))
        				.addGroup(gl_rdrPanel.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(labelReader)))
        			.addContainerGap())
        );
        gl_rdrPanel.setVerticalGroup(
        	gl_rdrPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_rdrPanel.createSequentialGroup()
        			.addComponent(labelReader)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(comboBoxReader, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(9)
        			.addComponent(buttonInitialize)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(buttonConnect)
        			.addGap(0, 0, Short.MAX_VALUE))
        );
        
        readerPanel.setLayout(gl_rdrPanel);

        cardFormatPanel.setBorder(BorderFactory.createTitledBorder("Card Format Routine"));
        
        labelFileID.setText("File ID");
        labelLength.setText("Length");
        buttonFormat.setText("Format Card");

        binaryPanel.setBorder(BorderFactory.createTitledBorder("Read and Write to Binary File"));

        labelID.setText("File ID");
        labelOffset.setText("File Offset");
        labelLen.setText("Length");
        labelData.setText("Data");

        scrollPaneData.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        textAreaData.setColumns(20);
        textAreaData.setRows(5);
        scrollPaneData.setViewportView(textAreaData);

        buttonRead.setText("Read Binary");
        buttonWrite.setText("Write Binary");

        textAreaMessage.setColumns(20);
        textAreaMessage.setRows(5);
        scrollPaneMessage.setViewportView(textAreaMessage);

        buttonClear.setText("Clear");
        buttonReset.setText("Reset");
        buttonQuit.setText("Quit");
        
        JLabel lblNewLabel = new JLabel("APDU Logs");

        GroupLayout gl_msgPanel = new GroupLayout(messagePanel);
        gl_msgPanel.setHorizontalGroup(
        	gl_msgPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_msgPanel.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_msgPanel.createParallelGroup(Alignment.LEADING)
        				.addComponent(scrollPaneMessage, GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
        				.addComponent(lblNewLabel)
        				.addGroup(gl_msgPanel.createSequentialGroup()
        					.addComponent(buttonClear, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
        					.addComponent(buttonReset, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(buttonQuit, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap())
        );
        gl_msgPanel.setVerticalGroup(
        	gl_msgPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_msgPanel.createSequentialGroup()
        			.addComponent(lblNewLabel)
        			.addGap(8)
        			.addComponent(scrollPaneMessage, GroupLayout.PREFERRED_SIZE, 362, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(gl_msgPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(buttonClear)
        				.addComponent(buttonQuit)
        				.addComponent(buttonReset))
        			.addContainerGap(26, Short.MAX_VALUE))
        );
        messagePanel.setLayout(gl_msgPanel);
		
        textAreaMessage.setLineWrap(true);
        textAreaData.setLineWrap(true);
        
        buttonInitialize.setMnemonic(KeyEvent.VK_I);
        buttonConnect.setMnemonic(KeyEvent.VK_C);
        buttonReset.setMnemonic(KeyEvent.VK_R);
        buttonClear.setMnemonic(KeyEvent.VK_L);
        buttonFormat.setMnemonic(KeyEvent.VK_F);
        buttonRead.setMnemonic(KeyEvent.VK_R);
        buttonWrite.setMnemonic(KeyEvent.VK_W);
        buttonQuit.setMnemonic(KeyEvent.VK_Q);

        buttonInitialize.addActionListener(this);
        buttonConnect.addActionListener(this);
        buttonReset.addActionListener(this);
        buttonClear.addActionListener(this);
        buttonFormat.addActionListener(this);
        buttonRead.addActionListener(this);
        buttonWrite.addActionListener(this);
        buttonQuit.addActionListener(this);
        
        textFieldFileID1.addKeyListener(this);
        textFieldFileID2.addKeyListener(this);
        textFieldFileLen1.addKeyListener(this);
        textFieldFileLen2.addKeyListener(this);
        textFieldFID1.addKeyListener(this);
        textFieldFID2.addKeyListener(this);
        textFieldOffset1.addKeyListener(this);
        textFieldOffset2.addKeyListener(this);
        textFieldLen.addKeyListener(this);
        
        //Key events
        textFieldLen.addKeyListener(this);
        textFieldLen.addKeyListener(new KeyListener(){
   			
   			@Override
   		    public void keyPressed(KeyEvent e){
   			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				textAreaData.setText("");
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
   		});
        
        textAreaData.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				char empty = '\r';
				int len = (Integer)Integer.parseInt(textFieldLen.getText(), 16);
				if(!textAreaData.getText().equals(""))
				{	
					if (((JTextArea)e.getSource()).getText().length() >= len) 
	  				{	
	  					e.setKeyChar(empty);
	  					return;
	  				}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
   		});
    }

	public void actionPerformed(ActionEvent e) 
	{
		if(buttonInitialize == e.getSource())
		{	
			String[] readerList = null;
			
		    try
		    {
		    	comboBoxReader.removeAllItems();
		    	
			    readerList = readerInterface.listTerminals();					
				if (readerList.length == 0)
				{
					textAreaMessage.append("No PC/SC reader detected");
					return;
				}
				
				comboBoxReader.removeAllItems();
					
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
		}  // Initialize
		
		if(buttonConnect == e.getSource())
		{		
			try
			{
				if(readerInterface.isConnectionActive())	
					readerInterface.disconnect();
				
				String rdrcon = (String)comboBoxReader.getSelectedItem();
				
				readerInterface.connect(rdrcon, "*");
				acos3 = new Acos3(readerInterface);				
				
				displayOut(0, 0, "Successfully connected to " + rdrcon + "\n");	
				
				//Check if card inserted is an ACOS Card
				if(!isAcos3())
					return;
				
				if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI)
					displayOut(0, 0, "Chip Type: ACOS3 Combi");
				else
					displayOut(0, 0, "Chip Type: ACOS3");
			    
				textFieldFileID1.setEnabled(true);
				textFieldFileID2.setEnabled(true);
				textFieldFileLen1.setEnabled(true);
				textFieldFileLen2.setEnabled(true);
				buttonFormat.setEnabled(true);
								
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
		
		if(buttonReset==e.getSource())
		{	
			try			
			{
				//disconnect
				if (readerInterface.isConnectionActive())
					readerInterface.disconnect();
				
				initMenu();
				textAreaMessage.setText("");
				comboBoxReader.removeAllItems();
				comboBoxReader.addItem("Please select reader                   ");
				displayOut(0, 0, "Program Ready");
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
		} // Reset
		
		if(buttonFormat == e.getSource())
		{	
			formatCard();			
		}  // Format
		
		if(buttonRead == e.getSource())
		{	
			readCard();			
		}  // Read
		
		if(buttonWrite == e.getSource())
		{		
			writeCard();						
		}  // Write
				
	}
	
	private void formatCard()
	{	
		byte fileId[] = new byte[2];
		byte fileLength[] = new byte[2];
	
		try
		{
			//validate input
			if(textFieldFileID1.getText().equals("") || !isHexString(textFieldFileID1.getText()))
			{
				JOptionPane.showMessageDialog(null, "Please key-in hex value for File ID.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldFileID1.requestFocus();
				return;
			}
			
			fileId[0] = (byte)((Integer)Integer.parseInt(textFieldFileID1.getText(), 16)).byteValue();
			
			if(textFieldFileID2.getText().equals("") || !isHexString(textFieldFileID2.getText()))
			{	
				JOptionPane.showMessageDialog(null, "Please key-in hex value for File ID.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldFileID2.requestFocus();
				return;
			}
			fileId[1] = (byte)((Integer)Integer.parseInt(textFieldFileID2.getText(), 16)).byteValue();
			
			if(textFieldFileLen1.getText().equals("") || !isHexString(textFieldFileLen1.getText()))
			{
				JOptionPane.showMessageDialog(null, "Please key-in hex value for Length.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldFileLen1.requestFocus();
				return;
			}
			fileLength[0] = (byte)((Integer)Integer.parseInt(textFieldFileLen1.getText(), 16)).byteValue();
			
			if(textFieldFileLen2.getText().equals("") || !isHexString(textFieldFileLen2.getText()))
			{
				JOptionPane.showMessageDialog(null, "Please key-in hex value for Length.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldFileLen2.requestFocus();
				return;
			}
			
			fileLength[1] = (byte)((Integer)Integer.parseInt(textFieldFileLen2.getText(), 16)).byteValue();
			
			if(Helper.byteToInt(fileLength) <= 0)
			{
				JOptionPane.showMessageDialog(null, "Please key-in valid Length. Valid value: 01h - FFFFh.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldFileLen2.requestFocus();
				return;
			}
		
			//Format Card
			displayOut(0, 0, "\nSubmit Code - IC");
			acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
			
			displayOut(0, 0, "\nClear Card");
			acos3.clearCard();
			
			//perform a reset for changes in ACOS3 to take effect			
			readerInterface.disconnect();
			readerInterface.connect(comboBoxReader.getSelectedItem().toString(), "*");
			
			displayOut(0, 0, "\nSubmit Code - IC");
			acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");

			OptionRegister optionRegister = new OptionRegister();
			
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

			//Select FF 02
			//Write record to Personalization File
            //Number of File = 1
			displayOut(0, 0, "\nSelect File - FF 02");
			displayOut(0, 0, "Write Record");
			acos3.configurePersonalizationFile(optionRegister, securityOptionRegister, (byte)0x01);
			
			displayOut(0, 0, "\nCard reset is successful");
			
			//perform a reset for changes in ACOS3 to take effect			
			readerInterface.disconnect();
			readerInterface.connect(comboBoxReader.getSelectedItem().toString(), "*");
			
			//Select User File Management File
			displayOut(0, 0, "\nSubmit Code - IC");
			acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
			
			displayOut(0, 0, "\nSelect File - FF 04");
			acos3.selectFile(Acos3.INTERNAL_FILE.USER_FILE_MGMT_FILE);
			
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
			acos3.createBinaryFile(fileId, Helper.byteToInt(fileLength), writeSecurityAttribute, readSecurityAttribute, false, false);
			displayOut(0,0, "Binary User file " + textFieldFileID1.getText() + textFieldFileID2.getText() + " is defined. Size: " + String.format("0x%05X", Helper.byteToInt(fileLength) & 0x0FFFFF) + " bytes");
			textFieldFID1.setEnabled(true);
			textFieldFID2.setEnabled(true);
			textFieldOffset1.setEnabled(true);
			textFieldOffset2.setEnabled(true);
			textFieldLen.setEnabled(true);
			textAreaData.setEnabled(true);
			buttonRead.setEnabled(true);
			buttonWrite.setEnabled(true);
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
	}
	
	private void readCard()
	{		
		try
		{
			byte fileID1, fileID2, hiByte, loByte, tmpLen;
			String tmpStr = "";
			
			//validate input
			if(textFieldFID1.getText().equals("") || !isHexString(textFieldFID1.getText()))
			{	
				JOptionPane.showMessageDialog(null, "Please key-in hex value for File ID.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldFID1.requestFocus();
				return;
			}
			if(textFieldFID2.getText().equals("") || !isHexString(textFieldFID2.getText()))
			{	
				JOptionPane.showMessageDialog(null, "Please key-in hex value for File ID.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldFID2.requestFocus();
				return;
			}
			
			if(textFieldOffset1.getText().equals("") || !isHexString(textFieldOffset1.getText()))
			{
				JOptionPane.showMessageDialog(null, "Please key-in hex value for Offset.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldOffset1.requestFocus();
				return;
			}
			
			if(textFieldOffset2.getText().equals("") || !isHexString(textFieldOffset2.getText()))
			{
				JOptionPane.showMessageDialog(null, "Please key-in hex value for Offset.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldOffset2.requestFocus();
				return;
			}			
			if(textFieldLen.getText().equals("") || !isHexString(textFieldLen.getText()))
			{	
				JOptionPane.showMessageDialog(null, "Please key-in hex value for Length.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldLen.requestFocus();
				return;
			}		
			
			tmpLen = (byte)((Integer)Integer.parseInt(textFieldLen.getText(), 16)).byteValue();
			
			if(tmpLen <= 0)
			{
				JOptionPane.showMessageDialog(null, "Please key-in valid length. Valid value: 01h - FFh.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldLen.requestFocus();
				return;
			}
			
			fileID1 = (byte)((Integer)Integer.parseInt(textFieldFID1.getText(), 16)).byteValue();
			fileID2 = (byte)((Integer)Integer.parseInt(textFieldFID2.getText(), 16)).byteValue();
			
			if(textFieldOffset1.getText().equals(""))
				hiByte = (byte) 0x00;
			else
				hiByte = ((Integer)Integer.parseInt(textFieldOffset1.getText(), 16)).byteValue();
			
			loByte = (byte)((Integer)Integer.parseInt(textFieldOffset2.getText(), 16)).byteValue();
			
			//select user file
			displayOut(0, 0, "\nSelect File");
			acos3.selectFile(new byte[] { fileID1, fileID2 });
			
			//read binary			
			byte[] readData = new byte[Integer.parseInt(textFieldLen.getText(), 16)];
			displayOut(0, 0, "\nRead Binary");
			readData = acos3.readBinary(hiByte, loByte, (byte)tmpLen);			
			
			textAreaData.setText(Helper.byteArrayToString(readData, readData.length));	
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
	}
	
	private void writeCard()
	{
		try
		{
			byte hiByte, loByte, fileID1, fileID2;
			int tmpLen;
			byte[] tmpArray = new byte[255];
			byte[] dataToWrite;
			String tmpStr = "";
			
			//validate input
			if(textFieldFID1.getText().equals("") || !isHexString(textFieldFID1.getText()))
			{	
				JOptionPane.showMessageDialog(null, "Please key-in hex value for File ID.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldFID1.requestFocus();
				return;
			}
			if(textFieldFID2.getText().equals("") || !isHexString(textFieldFID2.getText()))
			{	
				JOptionPane.showMessageDialog(null, "Please key-in hex value for File ID.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldFID2.requestFocus();
				return;
			}
			
			if(textFieldOffset1.getText().equals("") || !isHexString(textFieldOffset1.getText()))
			{
				JOptionPane.showMessageDialog(null, "Please key-in hex value for Offset.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldOffset1.requestFocus();
				return;
			}
			
			if(textFieldOffset2.getText().equals("") || !isHexString(textFieldOffset2.getText()))
			{
				JOptionPane.showMessageDialog(null, "Please key-in hex value for Offset.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldOffset2.requestFocus();
				return;
			}			
			if(textFieldLen.getText().equals("") || !isHexString(textFieldLen.getText()))
			{	
				JOptionPane.showMessageDialog(null, "Please key-in hex value for Length.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldLen.requestFocus();
				return;
			}		
			
			tmpLen = (Integer)Integer.parseInt("00" + textFieldLen.getText(), 16);
			//tmpLen = (byte)((Integer)Integer.parseInt("00" + textFieldLen.getText(), 16)).byteValue();
			
			if(tmpLen <= 0)
			{
				JOptionPane.showMessageDialog(null, "Please key-in valid length. Valid value: 01h - FFh.", "Error", JOptionPane.ERROR_MESSAGE);
				textFieldLen.requestFocus();
				return;
			}
			
			if(textAreaData.getText().length() <= 0)
			{
				JOptionPane.showMessageDialog(null, "Please key-in data to write.", "Error", JOptionPane.ERROR_MESSAGE);
				textAreaData.requestFocus();
				return;				
			}
			
			fileID1 = (byte)((Integer)Integer.parseInt(textFieldFID1.getText(), 16)).byteValue();
			fileID2 = (byte)((Integer)Integer.parseInt(textFieldFID2.getText(), 16)).byteValue();
			
			if(textFieldOffset1.getText().equals(""))
				hiByte = (byte)0x00;
			else
				hiByte = (byte)((Integer)Integer.parseInt(textFieldOffset1.getText(), 16)).byteValue();
			
			loByte = (byte)((Integer)Integer.parseInt(textFieldOffset2.getText(), 16)).byteValue();
			
			dataToWrite = new byte[(Integer)Integer.parseInt(textFieldLen.getText(), 16)];
			
			for(int i = 0; i < textAreaData.getText().length(); i++)
				dataToWrite[i] = (byte)textAreaData.getText().charAt(i);
			
			//select user file
			displayOut(0, 0, "\nSelect File");
			acos3.selectFile(new byte[] { fileID1, fileID2 });

			displayOut(0, 0, "\nWrite Binary");
			acos3.writeBinary((byte) hiByte, (byte) loByte, dataToWrite);
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
	}

	public void displayOut(int mType, int msgCode, String printText)
	{
		switch(mType)
		{
			case 2: textAreaMessage.append("<< " + printText + "\n");break;
			case 3: textAreaMessage.append(">> " + printText);break;
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
		textFieldLen.setEnabled(false);
		textAreaData.setText("");
		textAreaData.setEnabled(false);
		buttonRead.setEnabled(false);
		buttonWrite.setEnabled(false);
		displayOut(0, 0, "Program Ready");
	}
	
	public void keyReleased(KeyEvent ke) { }
	
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
  					  
		//Limit character length
  		if(((JTextField)ke.getSource()).getText().length() >= 2 ) 
  		{
  			ke.setKeyChar(empty);
  			return;
  		}				
	}
	
	public void onSendCommand(ReaderEvents.TransmitApduEventArg event)
	{
		displayOut(2, 0, event.getAsString(true));
	}
	public void onReceiveCommand(ReaderEvents.TransmitApduEventArg event)
	{
		displayOut(3, 0, event.getAsString(true) + "\r\n");
	}
 
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ACOS3ReadWriteBinary().setVisible(true);
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
    		JOptionPane.showMessageDialog(null,"Card not supported. Please use ACOS3 Card.", "Error", JOptionPane.ERROR_MESSAGE);
    		return false;
    	}
    	return true;
    }
    
    public static boolean isHexString(String str)
    {
    	int value;
        try
        {
        	value = Integer.parseInt(str,16);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
}
