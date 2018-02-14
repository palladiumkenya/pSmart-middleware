package card.ACOS3;
/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              ACOS3CreateFiles.java

  Description:       This sample program outlines the steps on how to
                     create user-defined files in ACOS smart card
                     using the PC/SC platform.
                     
  Author:            M.J.E.C. Castillo

  Date:              August 26, 2008

  Revision Trail:   (Date/Author/Description)

======================================================================*/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JOptionPane;

import javax.smartcardio.CardException;

public class ACOS3CreateFiles extends JFrame implements ActionListener, Acos3CardReaderEvents.TransmitApduHandler{

	PcscReader pcscReader;	
	Acos3 acos3;
	
	private JButton buttonInitialize, buttonConnect, buttonCreate, buttonQuit, buttonDisconnect, buttonClear;
	private JTextArea textAreaMessage;
	private JComboBox comboBoxReader;
	private JLabel jLabel1;
	private JScrollPane messageScrollPane;
	
	private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;
    
    private OptionRegister optionRegister;
    private SecurityOptionRegister securityOptionRegister;
    private SecurityAttribute readSecurityAttribute;
    private SecurityAttribute writeSecurityAttribute;
    
    {	
    	this.setTitle("ACOS3 Create Files");
        initComponents();
        initMenu();
        
        textAreaMessage.setForeground(Color.black);
        textAreaMessage.setEditable(false);
        
        pcscReader = new PcscReader();
        readerInterface = new ReaderInterface();
        
		// Instantiate an event handler object 
        readerInterface.setEventHandler(new Acos3CardReaderEvents());
		
		// Register the event handler implementation of this class
        readerInterface.getEventHandler().addEventListener(this);
    }

    private void initComponents() {

   	  	setSize(470,305);
   	  	setResizable(false);
   	  	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   	  	
   	  	//sets the location of the form at the center of screen
   		Dimension dim = getToolkit().getScreenSize();
   		Rectangle abounds = getBounds();
   		setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
   		requestFocus();
   		
   		String[] rdrNameDef = {"Please select reader"};	
   		
   	  	comboBoxReader = new JComboBox(rdrNameDef);
   	  	comboBoxReader.setSelectedIndex(0);
   	  	buttonInitialize = new JButton();
   	  	buttonConnect = new JButton();
   	  	buttonCreate = new JButton();
   	  	buttonDisconnect = new JButton();
   	  	buttonClear = new JButton();
   	  	buttonQuit = new JButton();
   	  	textAreaMessage = new JTextArea();
   	  	textAreaMessage.setWrapStyleWord(true);
   	  	messageScrollPane = new JScrollPane();
   	  	jLabel1 = new JLabel();
   	  	jLabel1.setText("Select Reader");
   	  	textAreaMessage.setColumns(20);
   	  	textAreaMessage.setRows(5);
   	  	messageScrollPane.setViewportView(textAreaMessage);   	  	
		
        buttonInitialize.setText("Initialize");
        buttonConnect.setText("Connect");   
        buttonCreate.setText("Create Files");
        buttonDisconnect.setText("Reset"); 
        buttonClear.setText("Clear");
        buttonQuit.setText("Quit");

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(26)
        					.addComponent(jLabel1)
        					.addPreferredGap(ComponentPlacement.RELATED))
        				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        					.addContainerGap()
        					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        						.addComponent(buttonConnect, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
        						.addComponent(buttonCreate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
        						.addComponent(buttonClear, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
        						.addComponent(buttonDisconnect, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
        						.addComponent(buttonInitialize, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
        						.addComponent(buttonQuit, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
        					.addGap(21)))
        			.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(comboBoxReader, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(messageScrollPane, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
        			.addGap(16))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(26)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(comboBoxReader, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel1))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(buttonInitialize)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(buttonConnect)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(buttonCreate, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
        					.addGap(23)
        					.addComponent(buttonDisconnect)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(buttonClear)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(buttonQuit))
        				.addComponent(messageScrollPane, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE))
        			.addGap(38))
        );
        getContentPane().setLayout(layout);
        
        textAreaMessage.setLineWrap(true);
        
        buttonInitialize.addActionListener(this);
        buttonConnect.addActionListener(this);
        buttonDisconnect.addActionListener(this);
        buttonClear.addActionListener(this);
        buttonCreate.addActionListener(this);
        buttonQuit.addActionListener(this);
        
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
		} // Initialize
		
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
				
				//Check if card inserted is an ACOS3 Card
				if(!isAcos3())
					return;
				
				if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI)
					displayOut(0, 0, "Chip Type: ACOS3 Combi\n");
				else
					displayOut(0, 0, "Chip Type: ACOS3\n");
			    
				buttonCreate.setEnabled(true);
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
		}  // Clear

		if (buttonDisconnect == e.getSource())
		{
			try			
			{
				//disconnect
				if (readerInterface.isConnectionActive())
					readerInterface.disconnect();
				
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
		
		if (buttonCreate == e.getSource())
		{
			createFiles();
		} // Create Files
		
		if(buttonQuit == e.getSource())
		{				
			this.dispose();
		} // Quit
				
	}
	
	private void createFiles()
	{
		try
		{
			displayOut(0, 0, "Submit Code - IC");
			acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
			
			displayOut(0, 0, "Clear Card");
			acos3.clearCard();
			
			displayOut(0, 0, "Submit Code - IC");
			acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");				
			
			//Set Option Registers and Security Option Registers
            //See Personalization File of ACOS3 Reference Manual for more information
			OptionRegister optionRegister = new OptionRegister();
		
			optionRegister.setRequireMutualAuthenticationOnInquireAccount(false);
			optionRegister.setRequireMutualAuthenticationOnAccountTransaction(false);
			optionRegister.setEnableRevokeDebitCommand(false);
			optionRegister.setEnableChangePinCommand(false);
			optionRegister.setEnableDebitMac(false);
			optionRegister.setRequirePinDuringDebit(false);
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
			//Select Personalization File
			displayOut(0, 0, "Select File - FF 02");
			displayOut(0, 0, "Write Record");
			acos3.configurePersonalizationFile(optionRegister, securityOptionRegister, (byte)0x03);
			
			displayOut(0, 0, "Submit Code - IC");
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
			
			//Select User File Management File
			displayOut(0, 0, "Select File - FF 04");
			
			//Create Files
            //Record File ID:       AA 11
            //Record Number:        0x00
            //Record Length:        0x05
            //Number of Records:    0x03
			displayOut(0, 0, "Write Record");
			acos3.createRecordFile((byte)0x00, new byte[] { (byte)0xAA, (byte)0x11}, (byte)0x03, (byte)0x05, writeSecurityAttribute, readSecurityAttribute, false, false);
			
			displayOut(0, 0, "Select File - FF 04");
			
			//Record File ID:       BB 22
            //Record Number:        0x01
            //Record Length:        0x0A (10)
            //Number of Records:    0x02
			displayOut(0, 0, "Write Record");
			acos3.createRecordFile((byte)0x01, new byte[] { (byte)0xBB, (byte)0x22}, (byte)0x02, (byte)0x0A, writeSecurityAttribute, readSecurityAttribute, false, false);
			
			displayOut(0, 0, "Select File - FF 04");
			
			//Record File ID:       CC 33
            //Record Number:        0x02
            //Record Length:        0x06
            //Number of Records:    0x04
			displayOut(0, 0, "Write Record");
			acos3.createRecordFile((byte)0x02, new byte[] { (byte)0xCC, (byte)0x33}, (byte)0x04, (byte)0x06, writeSecurityAttribute, readSecurityAttribute, false, false);
			
			//Select files to verify that they are created
            //The response is 90 [RecordNumber] if the file is selected successfully
			displayOut(0, 0, "Select File - AA 11");
			acos3.selectFile(new byte[] {(byte)0xAA, (byte)0x11});
			displayOut(0, 0, "Select File - BB 22");
			acos3.selectFile(new byte[] {(byte)0xBB, (byte)0x22});
			displayOut(0, 0, "Select File - CC 33");
			acos3.selectFile(new byte[] {(byte)0xCC, (byte)0x33});

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
			case 3: textAreaMessage.append(">> " + printText + "\n");break;
			default: textAreaMessage.append(printText + "\n");
		}	
	}
		
	public void initMenu()
	{
		buttonInitialize.setEnabled(true);
		buttonConnect.setEnabled(false);
		buttonCreate.setEnabled(false);		
		textAreaMessage.setText("");
		displayOut(0, 0, "Program Ready\n");
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
                new ACOS3CreateFiles().setVisible(true);
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
