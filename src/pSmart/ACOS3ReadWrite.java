package pSmart;
/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              ACOS3ReadWrite.java

  Description:       This sample program outlines the steps on how to
                     format the ACOS card and how to  read or write
                     data into it using the PC/SC platform.                 

  Author:            Donn Johnson A. Fabian

  Date:              October 11, 2012

  Revision Trail:   (Date/Author/Description)
  					 04-10-2010 / M.J.E.Castillo / Bug Fixes

======================================================================*/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

import javax.smartcardio.CardException;

public class ACOS3ReadWrite extends JFrame implements ActionListener, KeyListener, ReaderEvents.TransmitApduHandler{

	PcscReader pcscReader;
	Acos3 acos3;
	
	//GUI Variables
	private JTextArea textAreaMessage;
    private JButton buttonConnect, buttonFormat, buttonInitialize, buttonRead, buttonReset, buttonWrite, buttonQuit, buttonClear;
    private ButtonGroup buttonGroupRecord;
    private JPanel midPanel;
    private JLabel labelReader, labelValue;
    private JScrollPane messageScrollPane;
    private JRadioButton radioButtonaa11, radioButtonbb22, radioButtoncc33;
    private JComboBox comboBoxReaderName;
    private JTextField textFieldValue;
    
    private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;

    public ACOS3ReadWrite() 
    {	
    	this.setTitle("ACOS3 Read Write");
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
   		setSize(654,331);
   		setResizable(false);
   	  	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   		
   		//sets the location of the form at the center of screen
   		Dimension dim = getToolkit().getScreenSize();
   		Rectangle abounds = getBounds();
   		setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
   		requestFocus();
   		
        String[] rdrNameDef = {"Please select reader"};	

   		buttonGroupRecord = new ButtonGroup();
        midPanel = new JPanel();
        midPanel.setBorder(new TitledBorder(null, "User File", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        midPanel.setBounds(10, 118, 274, 163);
        labelReader = new JLabel();
        labelReader.setBounds(10, 11, 82, 14);

		labelReader.setText("Select Reader");
		getContentPane().setLayout(null);
		getContentPane().add(labelReader);
		getContentPane().add(midPanel);
		midPanel.setLayout(null);
		buttonRead = new JButton();
		buttonRead.setBounds(139, 27, 125, 23);
		midPanel.add(buttonRead);

		buttonRead.setText("Read");
		buttonWrite = new JButton();
		buttonWrite.setBounds(139, 61, 125, 23);
		midPanel.add(buttonWrite);

		buttonWrite.setText("Write");
		radioButtonaa11 = new JRadioButton();
		radioButtonaa11.setBounds(28, 20, 101, 15);
		midPanel.add(radioButtonaa11);

		buttonGroupRecord.add(radioButtonaa11);
		radioButtonaa11.setText("AA 11");
		radioButtonaa11.setBorder(null);
		radioButtonbb22 = new JRadioButton();
		radioButtonbb22.setBounds(28, 45, 101, 15);
		midPanel.add(radioButtonbb22);

		buttonGroupRecord.add(radioButtonbb22);
		radioButtonbb22.setText("BB 22");
		radioButtonbb22.setBorder(null);
		radioButtoncc33 = new JRadioButton();
		radioButtoncc33.setBounds(28, 70, 101, 15);
		midPanel.add(radioButtoncc33);

		buttonGroupRecord.add(radioButtoncc33);
		radioButtoncc33.setText("CC 33");
		radioButtoncc33.setBorder(null);
		labelValue = new JLabel();
		labelValue.setBounds(10, 107, 46, 14);
		midPanel.add(labelValue);

		labelValue.setText("Data");
		textFieldValue = new JTextField();
		textFieldValue.setBounds(10, 125, 254, 20);
		textFieldValue.setTransferHandler(null);
		
		midPanel.add(textFieldValue);

		textFieldValue.addKeyListener(this);
		radioButtoncc33.addActionListener(this);
		radioButtonbb22.addActionListener(this);
		radioButtonaa11.addActionListener(this);
		buttonWrite.addActionListener(this);
		buttonRead.addActionListener(this);
		comboBoxReaderName = new JComboBox(rdrNameDef);
		comboBoxReaderName.setBounds(10, 28, 267, 20);
		getContentPane().add(comboBoxReaderName);
		comboBoxReaderName.setSelectedIndex(0);
		buttonInitialize = new JButton();
		buttonInitialize.setBounds(10, 59, 125, 23);
		getContentPane().add(buttonInitialize);

		buttonInitialize.setText("Initialize");
		buttonConnect = new JButton();
		buttonConnect.setBounds(152, 59, 125, 23);
		getContentPane().add(buttonConnect);
		buttonConnect.setText("Connect");
		buttonFormat = new JButton();
		buttonFormat.setBounds(152, 90, 125, 23);
		getContentPane().add(buttonFormat);
		buttonFormat.setText("Format Card");
		buttonFormat.addActionListener(this);
		buttonConnect.addActionListener(this);

		buttonInitialize.addActionListener(this);

		JLabel labelApduLogs = new JLabel("APDU Logs");
		labelApduLogs.setBounds(294, 11, 101, 14);
		getContentPane().add(labelApduLogs);
		messageScrollPane = new JScrollPane();
		messageScrollPane.setBounds(294, 28, 340, 222);
		getContentPane().add(messageScrollPane);
		textAreaMessage = new JTextArea();
		textAreaMessage.setWrapStyleWord(true);

		textAreaMessage.setColumns(20);
		textAreaMessage.setRows(5);
		messageScrollPane.setViewportView(textAreaMessage);

		textAreaMessage.setForeground(Color.black);
		textAreaMessage.setEditable(false);

		textAreaMessage.setLineWrap(true);
		buttonQuit = new JButton();
		buttonQuit.setBounds(534, 258, 100, 23);
		getContentPane().add(buttonQuit);
		buttonQuit.setText("Quit");
		buttonReset = new JButton();
		buttonReset.setBounds(415, 258, 100, 23);
		getContentPane().add(buttonReset);
		buttonReset.setText("Reset");
		buttonClear = new JButton();
		buttonClear.setBounds(294, 258, 100, 23);
		getContentPane().add(buttonClear);
		buttonClear.setText("Clear");
		buttonClear.addActionListener(this);
		buttonReset.addActionListener(this);
		buttonQuit.addActionListener(this);
    }

    
	public void actionPerformed(ActionEvent e) 
	{		
		
		if(buttonInitialize == e.getSource())
		{
			String[] readerList = null;
			
		    try
		    {
				comboBoxReaderName.removeAllItems();
				
			    readerList = readerInterface.listTerminals();					
				if (readerList.length == 0)
				{
					displayOut(0, 0, "No PC/SC reader detected");
					return;
				}
				
					
				for (int i = 0; i < readerList.length; i++)
				{
					if (!readerList.equals(""))	
						comboBoxReaderName.addItem(readerList[i]);
					else
						break;
				}
							
				displayOut(0, 0, "Initialize success");
				
				comboBoxReaderName.setSelectedIndex(0);
				buttonConnect.setEnabled(true);
		    }
		    catch (Exception ex)
		    {
		    	buttonConnect.setEnabled(false);
		    	displayOut(0, 0, "Cannot find a smart card reader.");
		    	JOptionPane.showMessageDialog(null, "Cannot find a smart card reader.", "Error", JOptionPane.ERROR_MESSAGE);
		    }
		} // Init

		if(buttonConnect == e.getSource())
		{
			try
			{
				if(readerInterface.isConnectionActive())	
					readerInterface.disconnect();
				
				String rdrcon = (String)comboBoxReaderName.getSelectedItem();
				
				readerInterface.connect(rdrcon, "*");
				acos3 = new Acos3(readerInterface);				
				
				displayOut(0, 0, "\nSuccessful connection to " + rdrcon);

				//Check if card inserted is an ACOS Card
				if(!isAcos3())
					return;
				
				if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI)
					displayOut(0, 0, "Chip Type: ACOS3 Combi");
				else
					displayOut(0, 0, "Chip Type: ACOS3");
			    
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
		} // Connect
		
		if(buttonQuit == e.getSource())
		{	
			this.dispose();
		} // Quit
		
		if(buttonFormat == e.getSource())
		{
			formatCard();
		} // Format
		
		if (buttonRead == e.getSource())
		{	
			readCard();			
		} // Read
		
		
		if(buttonWrite == e.getSource())
		{	
			if(textFieldValue.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Please key-in the data to write", "Error", JOptionPane.ERROR_MESSAGE);
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
				
				initMenu();				
				comboBoxReaderName.removeAllItems();
				comboBoxReaderName.addItem("Please select reader");
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
		
		
		if(buttonClear == e.getSource())
		{
			textAreaMessage.setText("");
		}  // Clear
			
		
		if(radioButtonaa11 == e.getSource())
		{
			textFieldValue.setText("");			
		}  // rbaa11 
		
		
		if(radioButtonbb22 == e.getSource())
		{
			textFieldValue.setText("");			
		}  // rbbb2
		
		
		if(radioButtoncc33 == e.getSource())
		{
			textFieldValue.setText("");			
		}  // rbcc33
	}
	
	
	private void formatCard()
	{		
		try
		{
			//Send IC Code
			displayOut(0, 0, "\nSubmit Code - IC");
			acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
						
			//Select FF 02
			//acos3.selectFile(Acos3.INTERNAL_FILE.PERSONALIZATION_FILE);	
			displayOut(0, 0, "\nSelect File");
			acos3.selectFile(new byte[] {(byte)0xFF, (byte)0x02});
			
			/* Write to FF 02
		       This will create 3 User files, no Option registers and
		       Security Option registers defined, Personalization bit is not set */
			displayOut(0, 0, "\nWrite Record");
			acos3.writeRecord((byte)0x00, (byte)0x00, new byte[] {(byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00});
			displayOut(0, 0, "FF 02 is updated\n");
			
			// Select FF 04
			displayOut(0, 0, "Select File");
            acos3.selectFile(new byte[] { (byte)0xFF, (byte)0x04 });
			
            //Send IC Code
            displayOut(0, 0, "\nSubmit Code - IC");
			acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
			
			/* Write to FF 04
		       Write to first record of FF 04 */
			displayOut(0, 0, "\nWrite Record");
			acos3.writeRecord((byte)0x00, (byte)0x00, new byte[] { (byte)0x0A, (byte)0x03, (byte)0x00, (byte)0x00, (byte)0xAA, (byte)0x11, (byte)0x00 });
			displayOut(0, 0, "User File AA 11 is defined");
			
			// Write to second record of FF 04
			displayOut(0, 0, "\nWrite Record");
            acos3.writeRecord((byte)0x01, (byte)0x00, new byte[] { (byte)0x10, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0xBB, (byte)0x22, (byte)0x00 });
            displayOut(0, 0, "User File BB 22 is defined");
			
            // write to third record of FF 04
            displayOut(0, 0, "\nWrite Record");
            acos3.writeRecord((byte)0x02, (byte)0x00, new byte[] { (byte)0x20, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0xCC, (byte)0x33, (byte)0x00 });
            displayOut(0, 0, "User File CC 33 is defined");
			
            radioButtonaa11.setSelected(true);
			textFieldValue.setText("");
			
			radioButtonaa11.setEnabled(true);			    
		    radioButtonbb22.setEnabled(true);
		    radioButtoncc33.setEnabled(true);			    
		    buttonRead.setEnabled(true);
		    buttonWrite.setEnabled(true);
			textFieldValue.setEnabled(true);
			
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
		byte[] fileId = new byte[2];
        byte dataLen = 0x00;
        byte[] data;
		
		try
		{	
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
			}
			
			// Select user file
			displayOut(0, 0, "\nSelect File");
            acos3.selectFile(fileId);
            
            // read first record of user file selected
			displayOut(0, 0, "\nRead Record");
            data = acos3.readRecord((byte)0x00, (byte)0x00, dataLen);
            
            textFieldValue.setText(Helper.byteArrayToString(data, data.length));    
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
		byte[] fileId = new byte[2];        
        int expLength = 0;
        String tmpStr = "";
        byte[] tmpArray = new byte[56];
        
		try
		{				
			//Validate input
			if (textFieldValue.getText().equals(""))
			{
			    JOptionPane.showMessageDialog(this, "Please key-in data to write.", "Error", JOptionPane.ERROR_MESSAGE);
			    textFieldValue.requestFocus();
			    return;
			}
			
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
			
			 // Select user file
			displayOut(0, 0, "\nSelect File");
            acos3.selectFile(fileId);
            
            tmpStr = textFieldValue.getText();
			tmpArray = new byte[expLength];
			int indx = 0;
			while (indx < textFieldValue.getText().length())
			{
				tmpArray[indx] = tmpStr.getBytes()[indx];
				indx++;
			}
			while (indx < expLength)
			{
				tmpArray[indx] = (byte)0x00;
				indx++;
			}	
			
			displayOut(0, 0, "\nWrite Record");
            acos3.writeRecord((byte)0x00, (byte)0x00, tmpArray);
            
            displayOut(0, 0, "Data read from textbox is written to card");
            textFieldValue.setText("");
			
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
	
	public void keyReleased(KeyEvent ke) { }
	
	public void keyPressed(KeyEvent ke) 
	{
  		//restrict paste actions
		if (ke.getKeyCode() == KeyEvent.VK_V ) 
			ke.setKeyCode(KeyEvent.VK_UNDO );						
  	}
	
	public void keyTyped(KeyEvent ke) 
	{
  		char empty = '\r';
  		
  		if(textFieldValue.isFocusOwner())
  		{
			//Limit character length  	  	
	  		if(radioButtonaa11.isSelected())
	  		{ 
	  			if (((JTextField)ke.getSource()).getText().length() >= 10 ) 
	  			{	
	  				ke.setKeyChar(empty);
	  				return;
	  			}			
	  		}  		
	  		if(radioButtonbb22.isSelected())
	  		{ 
	  			if (((JTextField)ke.getSource()).getText().length() >= 16 ) 
	  			{	
	  				ke.setKeyChar(empty);
	  				return;
	  			}			
	  		}  	
	  		if(radioButtoncc33.isSelected())
	  		{ 
	  			if (((JTextField)ke.getSource()).getText().length() >= 32 ) 
	  			{	
	  				ke.setKeyChar(empty);
	  				return;
	  			}			
	  		}
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
		buttonInitialize.setEnabled(true);
		buttonConnect.setEnabled(false);
		buttonFormat.setEnabled(false);
		buttonRead.setEnabled(false);
		buttonWrite.setEnabled(false);
		radioButtonaa11.setEnabled(false);
		radioButtonbb22.setEnabled(false);
		radioButtoncc33.setEnabled(false);
		radioButtonaa11.setSelected(true);
		textFieldValue.setEnabled(false);
		textAreaMessage.setText("");
		textFieldValue.setText("");
		displayOut(0, 0, "Program Ready");
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
                new ACOS3ReadWrite().setVisible(true);
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
