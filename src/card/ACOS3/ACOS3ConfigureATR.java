package card.ACOS3;
/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              ACOS3ConfigureATR.java

  Description:       This sample program outlines the steps on how to
                     change the ATR of a smart card using the PC/SC platform.
                     You can also change the Card Baud Rate and the Historical Bytes of the card.

  NOTE:            Historical Bytes valid value must be 0 to 9 and A,B,C,D,E,F only. e.g.(11,99,AE,AA,FF etc)
                   if historical byte is leave blank the profram will assume it as 00.
                   After you update the ATR you have to initialize and connect to the device
                   before you can see the updated ATR.
                     
  Author:            Donn Johnson A. Fabian

  Date:              October 11, 2012

  Revision Trail:   (Date/Author/Description)
					04-15-2010 / M.J.E.Castillo / Modified "Update" to fix bug
======================================================================*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ACOS3ConfigureATR extends JFrame implements ReaderEvents.TransmitApduHandler, ActionListener, ItemListener, KeyListener{

	int retCode;
	boolean connActive; 
	public static final int INVALID_SW1SW2 = -450;
	static String VALIDCHARS = "0123456789"; 

	//All variables that requires pass-by-reference calls to functions are
	//declared as 'Array of int' with length 1
	//Java does not process pass-by-ref to int-type variables, thus Array of int was used.
	byte [] historicalBytes = new byte[15];

	private JTextArea textAreaMessage;
	private JButton buttonInitialize, buttonConnect, buttonClear, buttonDisconnect, buttonQuit, buttonGetATR, buttonUpdate;
	private JComboBox comboBoxNoBytes, comboBoxRate, comboBoxReader;
	private JLabel jLabel1, jLabel3, jLabel5, labelSelect;
	private JScrollPane jScrollPane1;
	private JTextField textField1, textField10, textField11, textField12, textField13, textField14, textField15, textField2, textField3, textField4, textField5, textField6, textField7, textField8, textField9, textFieldT0, textFieldTA;
    
	private PcscReader pcscReader;
    private Acos3 acos3;
    private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;
	
    public ACOS3ConfigureATR() 
    {	
    	this.setTitle("ACOS3 Configure ATR");
        initComponents();
        initMenu();
        
        buttonDisconnect.setEnabled(true);
        textFieldT0.setEnabled(false);
        textFieldTA.setEnabled(false);
        textAreaMessage.setForeground(Color.black);
        textAreaMessage.setEditable(false);
        
        pcscReader = new PcscReader();
        readerInterface = new ReaderInterface();
        
		// Instantiate an event handler object 
        readerInterface.setEventHandler(new ReaderEvents());
		
		// Register the event handler implementation of this class
        readerInterface.getEventHandler().addEventListener(this);
    }

    private void initComponents() 
    {
   	  	setSize(584,482);
   	  	setResizable(false);
   	  	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   	  	
   	  	//sets the location of the form at the center of screen
   		Dimension dim = getToolkit().getScreenSize();
   		Rectangle abounds = getBounds();
   		setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
   		requestFocus();
   		
   		String[] rdrNameDef = {"Please select reader"};	

		labelSelect = new JLabel();
        buttonInitialize = new JButton();
        buttonConnect = new JButton();
        buttonGetATR = new JButton();
        comboBoxRate = new JComboBox();
        jLabel1 = new JLabel();
        textFieldTA = new JTextField();
        comboBoxNoBytes = new JComboBox();
        jLabel3 = new JLabel();
        textFieldT0 = new JTextField();
        jLabel5 = new JLabel();
        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();
        textField4 = new JTextField();
        textField5 = new JTextField();
        textField6 = new JTextField();
        textField7 = new JTextField();
        textField8 = new JTextField();
        textField9 = new JTextField();
        textField10 = new JTextField();
        textField11 = new JTextField();
        textField12 = new JTextField();
        textField13 = new JTextField();
        textField14 = new JTextField();
        textField15 = new JTextField();
        buttonUpdate = new JButton();
        jScrollPane1 = new JScrollPane();
        textAreaMessage = new JTextArea();
        buttonClear = new JButton();
        buttonQuit = new JButton();
        buttonDisconnect = new JButton();
	    comboBoxReader = new JComboBox(rdrNameDef);
	    comboBoxReader.setSelectedIndex(0);
	    
        labelSelect.setText("Select Reader");		

        buttonInitialize.setText("Initialize");
        buttonConnect.setText("Connect");
        buttonGetATR.setText("Get ATR");
        comboBoxRate.setModel(new DefaultComboBoxModel(new String[] {"9600", "14400", "28800", "57600" , "115200"}));      
        jLabel1.setText("Card Baud Rate");
        textFieldTA.setText("");
        comboBoxNoBytes.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F"}));
        jLabel3.setText("No. of Historical Bytes");
        textFieldT0.setText("");
        jLabel5.setText("Historical Bytes");
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
        textField8.setText("");
        textField9.setText("");
        textField10.setText("");
        textField11.setText("");
        textField12.setText("");
        textField13.setText("");
        textField14.setText("");
        textField15.setText("");
        buttonUpdate.setText("Update ATR");

        textAreaMessage.setColumns(20);
        textAreaMessage.setRows(5);
        jScrollPane1.setViewportView(textAreaMessage);

        buttonClear.setText("Clear");
        buttonDisconnect.setText("Reset");
        buttonQuit.setText("Quit");

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(Alignment.LEADING, layout.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(labelSelect, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
        				.addGroup(Alignment.LEADING, layout.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(comboBoxReader, GroupLayout.PREFERRED_SIZE, 260, 260))
        				.addGroup(layout.createSequentialGroup()
        					.addContainerGap(48, Short.MAX_VALUE)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        						.addGroup(layout.createSequentialGroup()
        							.addGap(17)
        							.addComponent(buttonInitialize, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(textField6, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(textField7, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(textField8, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(textField9, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(textField10, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
        						.addComponent(jLabel1)
        						.addGroup(layout.createSequentialGroup()
        							.addGroup(layout.createParallelGroup(Alignment.LEADING)
        								.addGroup(layout.createSequentialGroup()
        									.addComponent(textField1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(textField2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(textField3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(textField4, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
        								.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        									.addComponent(jLabel3)
        									.addComponent(jLabel5)
        									.addComponent(comboBoxNoBytes, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(comboBoxRate, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        								.addComponent(textFieldTA)
        								.addComponent(textFieldT0)
        								.addComponent(textField5, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)))
        						.addGroup(layout.createSequentialGroup()
        							.addGap(17)
        							.addGroup(layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(buttonGetATR, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
        								.addComponent(buttonConnect, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(textField11, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
        								.addComponent(buttonUpdate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        								.addGroup(Alignment.LEADING, layout.createSequentialGroup()
        									.addComponent(textField12, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(textField13, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(textField14, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(textField15, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)))
        					.addGap(36)))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(buttonClear, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(buttonDisconnect, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(buttonQuit, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
        				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(labelSelect)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(comboBoxReader, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
        					.addComponent(buttonInitialize)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(buttonConnect)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(buttonGetATR)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(jLabel1)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(comboBoxRate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textFieldTA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addGap(10)
        					.addComponent(jLabel3)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(comboBoxNoBytes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textFieldT0, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addGap(26)
        					.addComponent(jLabel5)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(textField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField9, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(textField11, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField12, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField13, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField14, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textField15, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(buttonUpdate)
        				.addComponent(buttonClear)
        				.addComponent(buttonDisconnect)
        				.addComponent(buttonQuit))
        			.addGap(49))
        );
        getContentPane().setLayout(layout);
        
        textAreaMessage.setLineWrap(true);
        
        buttonInitialize.addActionListener(this);
        buttonConnect.addActionListener(this);
        buttonDisconnect.addActionListener(this);
        buttonClear.addActionListener(this);
        buttonGetATR.addActionListener(this);
        buttonQuit.addActionListener(this);
        buttonUpdate.addActionListener(this);        
        comboBoxRate.addItemListener(this);        
        comboBoxNoBytes.addItemListener(this);
        textField1.addKeyListener(this);
        textField2.addKeyListener(this);
        textField3.addKeyListener(this);
        textField4.addKeyListener(this);
        textField5.addKeyListener(this);
        textField6.addKeyListener(this);
        textField7.addKeyListener(this);
        textField8.addKeyListener(this);
        textField9.addKeyListener(this);
        textField10.addKeyListener(this);
        textField11.addKeyListener(this);
        textField12.addKeyListener(this);
        textField13.addKeyListener(this);
        textField14.addKeyListener(this);
        textField15.addKeyListener(this);
        
    }

	public void actionPerformed(ActionEvent e) 
	{
		if(buttonInitialize == e.getSource())
		{			
			 String[] readerList = null;
				
			    try
			    {
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
								   
					displayOut(0,  0, "Initialize success");
					buttonConnect.setEnabled(true);
					
			    }
			    catch (Exception ex)
			    {
			    	displayOut(0, 0, ex.getMessage().toString());
			    	JOptionPane.showMessageDialog(null,"Cannot Find Smart Card Reader");
			    }		    
			}

		if(buttonConnect == e.getSource())
		{	
			try
			{
				if(readerInterface.isConnectionActive())	
					readerInterface.disconnect();
				
				String rdrcon = (String)comboBoxReader.getSelectedItem();
				
				readerInterface.connect(rdrcon, "*");
				acos3 = new Acos3(readerInterface);
				
				//Check if card inserted is an ACOS Card
				if(!isAcos3())
					return;
				
				displayOut(0, 0, "Successfuly connected to " + rdrcon);
				
				if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI)
					displayOut(0, 0, "Chip Type: ACOS3 Combi\n");
				else
					displayOut(0, 0, "Chip Type: ACOS3\n");
			    
				 connActive = true;
				 buttonDisconnect.setEnabled(true);
				 buttonGetATR.setEnabled(true);
				 buttonUpdate.setEnabled(true);	
				 textField1.setEnabled(true);
				 textField2.setEnabled(true);
				 textField3.setEnabled(true);
				 textField4.setEnabled(true);
				 textField5.setEnabled(true);
				 textField6.setEnabled(true);
				 textField7.setEnabled(true);
				 textField8.setEnabled(true);
				 textField9.setEnabled(true);
				 textField10.setEnabled(true);
				 textField11.setEnabled(true);
				 textField12.setEnabled(true);
				 textField13.setEnabled(true);
				 textField14.setEnabled(true);
				 textField15.setEnabled(true);
			}
			catch (Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
			}
	
		} // Connect
		
		if (buttonClear == e.getSource())
		{
			textAreaMessage.setText("");	
		} // Clear

		if (buttonDisconnect == e.getSource())
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
			catch (Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
			}              
		} // Disconnect
		
		if (buttonGetATR == e.getSource())
		{			
			byte[] atr;
			String protocol;
			try
			{
				//Perform the Card Status
				atr = readerInterface.getAtr();
				protocol = readerInterface.getCardProtocol();
				
				displayOut(0, 0, "Active Protocol: " + protocol);
				displayOut(0, 0, "ATR: " + Helper.byteAsString(atr, true));
				
				textFieldT0.setText(Integer.toHexString(((Byte)atr[1]).intValue() & 0xFF).toUpperCase());
				textFieldTA.setText(Integer.toHexString(((Byte)atr[2]).intValue() & 0xFF).toUpperCase());
				
				comboBoxRate.setEnabled(true);
				comboBoxNoBytes.setEnabled(true);

			}
			catch (Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
			}  
		} // Get ATR
		
		if (buttonUpdate == e.getSource())
		{	
			byte[] dataIn = new byte[35];
            int historicalByte = 0; 
            int j;
            try
            {
            	 //Select FF07 file
                acos3.selectFile(new byte[] {(byte)0xFF, (byte)0x07});
                
                // Submit Issuer Code
                acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
                
                // Updating the ATR value
                historicalByte = comboBoxNoBytes.getSelectedIndex();
                
                //for(int i = 0; i < 35; i++)
                //	dataIn[i] = (byte)0x00;
   			
                dataIn[0] = (byte)((Integer)Integer.parseInt(textFieldTA.getText(), 16)).byteValue();
                
                if (historicalByte == 16) 
                	dataIn[1] = (byte)0xFF; //restoring to it original historical bytes
    			else
    				dataIn[1] = (byte)(historicalByte);
                
                textBoxUpdateATR();
         
                if(historicalByte < 16)
                {
                	System.arraycopy(historicalBytes, 0, dataIn, 2, historicalBytes.length);
                }

                for (int indx = (historicalByte + 3); indx < 35; indx++)
                    dataIn[indx] = 0x00;
                
                acos3.writeRecord((byte)0x00, (byte)0x00, dataIn);  
            }
            catch (Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
			}  
		} // Update ATR
		
		if(buttonQuit == e.getSource())
		{	
			this.dispose();
		} // Quit			
	}
	
	public void textBoxUpdateATR()
	{
		if(!textField1.getText().equals(""))
		{
			historicalBytes[0] = (byte)((Integer)Integer.parseInt(textField1.getText(), 16)).byteValue();
		}
		
		if(!textField2.getText().equals(""))
		{
			historicalBytes[1] = (byte)((Integer)Integer.parseInt(textField2.getText(), 16)).byteValue();
		}
		
		if(!textField3.getText().equals(""))
		{
			historicalBytes[2] = (byte)((Integer)Integer.parseInt(textField3.getText(), 16)).byteValue();
		}
		
		if(!textField4.getText().equals(""))
		{
			historicalBytes[3] = (byte)((Integer)Integer.parseInt(textField4.getText(), 16)).byteValue();
		}
		
		if(!textField5.getText().equals(""))
		{
			historicalBytes[4] = (byte)((Integer)Integer.parseInt(textField5.getText(), 16)).byteValue();
		}
		
		if(!textField6.getText().equals(""))
		{
			historicalBytes[5] = (byte)((Integer)Integer.parseInt(textField6.getText(), 16)).byteValue();
		}
		
		if(!textField7.getText().equals(""))
		{
			historicalBytes[6] = (byte)((Integer)Integer.parseInt(textField7.getText(), 16)).byteValue();
		}
		
		if(!textField8.getText().equals(""))
		{
			historicalBytes[7] = (byte)((Integer)Integer.parseInt(textField8.getText(), 16)).byteValue();
		}
		
		if(!textField9.getText().equals(""))
		{
			historicalBytes[8] = (byte)((Integer)Integer.parseInt(textField9.getText(), 16)).byteValue();
		}
		
		if(!textField10.getText().equals(""))
		{
			historicalBytes[9] = (byte)((Integer)Integer.parseInt(textField10.getText(), 16)).byteValue();
		}
		
		if(!textField11.getText().equals(""))
		{
			historicalBytes[10] = (byte)((Integer)Integer.parseInt(textField11.getText(), 16)).byteValue();
		}
		
		if(!textField12.getText().equals(""))
		{
			historicalBytes[11] = (byte)((Integer)Integer.parseInt(textField12.getText(), 16)).byteValue();
		}
		
		if(!textField13.getText().equals(""))
		{
			historicalBytes[12] = (byte)((Integer)Integer.parseInt(textField13.getText(), 16)).byteValue();
		}
		
		if(!textField14.getText().equals(""))
		{
			historicalBytes[13] = (byte)((Integer)Integer.parseInt(textField14.getText(), 16)).byteValue();
		}
		
		if(!textField15.getText().equals(""))
		{
			historicalBytes[14] = (byte)((Integer)Integer.parseInt(textField15.getText(), 16)).byteValue();
		}
	}
    
	public void itemStateChanged(ItemEvent e) 
	{   
		if (e.getStateChange() == ItemEvent.SELECTED)
		{	     
			switch (comboBoxRate.getSelectedIndex())
	    	{
		    	case 0: textFieldTA.setText("11"); break;
		    	case 1: textFieldTA.setText("92"); break;
		    	case 2: textFieldTA.setText("93"); break;
		    	case 3: textFieldTA.setText("94"); break;
		    	case 4: textFieldTA.setText("95"); break;
	    	}
		} 

		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			switch (comboBoxNoBytes.getSelectedIndex())
			{ 
				case 0 : textFieldT0.setText("B0"); break;
				case 1 : textFieldT0.setText("B1"); break;
				case 2 : textFieldT0.setText("B2"); break;
				case 3 : textFieldT0.setText("B3"); break;
				case 4 : textFieldT0.setText("B4"); break;
				case 5 : textFieldT0.setText("B5"); break;
				case 6 : textFieldT0.setText("B6"); break;
				case 7 : textFieldT0.setText("B7"); break;
				case 8 : textFieldT0.setText("B8"); break;
				case 9 : textFieldT0.setText("B9"); break;
				case 10 : textFieldT0.setText("BA"); break;
				case 11 : textFieldT0.setText("BB"); break;
				case 12 : textFieldT0.setText("BC"); break;
				case 13 : textFieldT0.setText("BD"); break;
				case 14 : textFieldT0.setText("BE"); break;
				case 15 : textFieldT0.setText("BF"); break;
			}
			
			switch (comboBoxNoBytes.getSelectedIndex())
			{
				case 16:
				case 0:
				{
					textField1.setEnabled(false);
					textField2.setEnabled(false);
					textField3.setEnabled(false);
					textField4.setEnabled(false);
					textField5.setEnabled(false);
					textField6.setEnabled(false);
					textField7.setEnabled(false);
					textField8.setEnabled(false);
					textField9.setEnabled(false);
					textField10.setEnabled(false);
					textField11.setEnabled(false);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);		
					break;
				}
				case 1:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(false);
					textField3.setEnabled(false);
					textField4.setEnabled(false);
					textField5.setEnabled(false);
					textField6.setEnabled(false);
					textField7.setEnabled(false);
					textField8.setEnabled(false);
					textField9.setEnabled(false);
					textField10.setEnabled(false);
					textField11.setEnabled(false);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);		
					break;
				}
				case 2:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(false);
					textField4.setEnabled(false);
					textField5.setEnabled(false);
					textField6.setEnabled(false);
					textField7.setEnabled(false);
					textField8.setEnabled(false);
					textField9.setEnabled(false);
					textField10.setEnabled(false);
					textField11.setEnabled(false);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);		
					break;
				}
				case 3:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(false);
					textField5.setEnabled(false);
					textField6.setEnabled(false);
					textField7.setEnabled(false);
					textField8.setEnabled(false);
					textField9.setEnabled(false);
					textField10.setEnabled(false);
					textField11.setEnabled(false);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);		
					break;
				}
				case 4:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(false);
					textField6.setEnabled(false);
					textField7.setEnabled(false);
					textField8.setEnabled(false);
					textField9.setEnabled(false);
					textField10.setEnabled(false);
					textField11.setEnabled(false);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);		
					break;
				}
				case 5:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(true);
					textField6.setEnabled(false);
					textField7.setEnabled(false);
					textField8.setEnabled(false);
					textField9.setEnabled(false);
					textField10.setEnabled(false);
					textField11.setEnabled(false);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);		
					break;
				}
				case 6:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(true);
					textField6.setEnabled(true);
					textField7.setEnabled(false);
					textField8.setEnabled(false);
					textField9.setEnabled(false);
					textField10.setEnabled(false);
					textField11.setEnabled(false);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);		
					break;
				}
				case 7:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(true);
					textField6.setEnabled(true);
					textField7.setEnabled(true);
					textField8.setEnabled(false);
					textField9.setEnabled(false);
					textField10.setEnabled(false);
					textField11.setEnabled(false);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);		
					break;
				}
				case 8:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(true);
					textField6.setEnabled(true);
					textField7.setEnabled(true);
					textField8.setEnabled(true);
					textField9.setEnabled(false);
					textField10.setEnabled(false);
					textField11.setEnabled(false);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);		
					break;
				}
				case 9:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(true);
					textField6.setEnabled(true);
					textField7.setEnabled(true);
					textField8.setEnabled(true);
					textField9.setEnabled(true);
					textField10.setEnabled(false);
					textField11.setEnabled(false);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);		
					break;
				}
				case 10:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(true);
					textField6.setEnabled(true);
					textField7.setEnabled(true);
					textField8.setEnabled(true);
					textField9.setEnabled(true);
					textField10.setEnabled(true);
					textField11.setEnabled(false);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);	
					break;				
				}
				case 11:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(true);
					textField6.setEnabled(true);
					textField7.setEnabled(true);
					textField8.setEnabled(true);
					textField9.setEnabled(true);
					textField10.setEnabled(true);
					textField11.setEnabled(true);
					textField12.setEnabled(false);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);		
					break;		
				}
				case 12:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(true);
					textField6.setEnabled(true);
					textField7.setEnabled(true);
					textField8.setEnabled(true);
					textField9.setEnabled(true);
					textField10.setEnabled(true);
					textField11.setEnabled(true);
					textField12.setEnabled(true);
					textField13.setEnabled(false);
					textField14.setEnabled(false);
					textField15.setEnabled(false);	
					break;		
				}
				case 13:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(true);
					textField6.setEnabled(true);
					textField7.setEnabled(true);
					textField8.setEnabled(true);
					textField9.setEnabled(true);
					textField10.setEnabled(true);
					textField11.setEnabled(true);
					textField12.setEnabled(true);
					textField13.setEnabled(true);
					textField14.setEnabled(false);
					textField15.setEnabled(false);			
					break;		
				}
				case 14:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(true);
					textField6.setEnabled(true);
					textField7.setEnabled(true);
					textField8.setEnabled(true);
					textField9.setEnabled(true);
					textField10.setEnabled(true);
					textField11.setEnabled(true);
					textField12.setEnabled(true);
					textField13.setEnabled(true);
					textField14.setEnabled(true);
					textField15.setEnabled(false);			
					break;		
				}
				case 15:
				{
					textField1.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
					textField5.setEnabled(true);
					textField6.setEnabled(true);
					textField7.setEnabled(true);
					textField8.setEnabled(true);
					textField9.setEnabled(true);
					textField10.setEnabled(true);
					textField11.setEnabled(true);
					textField12.setEnabled(true);
					textField13.setEnabled(true);
					textField14.setEnabled(true);
					textField15.setEnabled(true);				
					break;		
				}
			} // switch
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
  		Character x = (Character)ke.getKeyChar();
  		char empty = '\r';  		

  		//Check valid characters
  		if (VALIDCHARS.indexOf(x) == -1 ) 
  			ke.setKeyChar(empty);
  					  
		//Limit character length to 2
  		if(((JTextField)ke.getSource()).getText().length() >= 2) 
		{
			ke.setKeyChar(empty);					
		
			if (textFieldTA == ke.getSource())
			{			 
				if (textFieldTA.getText().equals("11"))
					comboBoxRate.setSelectedIndex(0);	
		    	
				if (textFieldTA.getText().equals("92"))
					comboBoxRate.setSelectedIndex(1);	
		    	
				if (textFieldTA.getText().equals("93"))
					comboBoxRate.setSelectedIndex(2);	
		    	
				if (textFieldTA.getText().equals("94"))
					comboBoxRate.setSelectedIndex(3);	
		    	
				if (textFieldTA.getText().equals("95"))
					comboBoxRate.setSelectedIndex(4);	
			} // txtTA
		 
			if (textFieldT0 == ke.getSource())
			{			
				if (textFieldT0.getText().equals("B0"))
				    comboBoxNoBytes.setSelectedIndex(0);
				    	
				if (textFieldT0.getText().equals("B1"))
				    comboBoxNoBytes.setSelectedIndex(1);
							
				if (textFieldT0.getText().equals("B2"))
				    comboBoxNoBytes.setSelectedIndex(2);
	
				if (textFieldT0.getText().equals("B3"))
				    comboBoxNoBytes.setSelectedIndex(3);
				
				if (textFieldT0.getText().equals("B4"))
				    comboBoxNoBytes.setSelectedIndex(4);
				
				if (textFieldT0.getText().equals("B5"))
				    comboBoxNoBytes.setSelectedIndex(5);
			
				if (textFieldT0.getText().equals("B6"))
				    comboBoxNoBytes.setSelectedIndex(6);
				
				if (textFieldT0.getText().equals("B7"))
				    comboBoxNoBytes.setSelectedIndex(7);
			
				if (textFieldT0.getText().equals("B8"))
				    comboBoxNoBytes.setSelectedIndex(8);
			
				if (textFieldT0.getText().equals("B9"))
				    comboBoxNoBytes.setSelectedIndex(9);
				
				if (textFieldT0.getText().equals("BA"))
				    comboBoxNoBytes.setSelectedIndex(10);
			
				if (textFieldT0.getText().equals("BB"))
				    comboBoxNoBytes.setSelectedIndex(11);
				
				if (textFieldT0.getText().equals("BC"))
				    comboBoxNoBytes.setSelectedIndex(12);
			
				if (textFieldT0.getText().equals("BD"))
				    comboBoxNoBytes.setSelectedIndex(13);
			
				if (textFieldT0.getText().equals("BF"))
				    comboBoxNoBytes.setSelectedIndex(15);
			 }
			 else 
			 {
				 switch (comboBoxNoBytes.getSelectedIndex())
				 { 
				 	case 16:
				 		comboBoxNoBytes.setSelectedIndex(16);
					case 14:
						comboBoxNoBytes.setSelectedIndex(14);
				 }
				
				 textField1.setText("");
				 textField2.setText("");
				 textField3.setText("");
				 textField4.setText("");
				 textField5.setText("");
				 textField6.setText("");
				 textField7.setText("");
				 textField8.setText("");
				 textField9.setText("");
				 textField10.setText("");
				 textField11.setText("");
				 textField12.setText("");
				 textField13.setText("");
				 textField14.setText("");
				 textField15.setText("");
			 }		
		}		
  	}
	
	public void initMenu()
	{		
		buttonInitialize.setEnabled(true);
		buttonConnect.setEnabled(false);
		buttonGetATR.setEnabled(false);
		buttonUpdate.setEnabled(false);
		textField1.setEnabled(false);
		textField2.setEnabled(false);
		textField3.setEnabled(false);
		textField4.setEnabled(false);
		textField5.setEnabled(false);
		textField6.setEnabled(false);
		textField7.setEnabled(false);
		textField8.setEnabled(false);
		textField9.setEnabled(false);
		textField10.setEnabled(false);
		textField11.setEnabled(false);
		textField12.setEnabled(false);
		textField13.setEnabled(false);
		textField14.setEnabled(false);
		textField15.setEnabled(false);
		textFieldT0.setText("");
		textFieldTA.setText("");
		comboBoxNoBytes.setEnabled(false);		
		comboBoxRate.setEnabled(false);		
		comboBoxNoBytes.setSelectedIndex(0);
		comboBoxRate.setSelectedIndex(0);
		textAreaMessage.setText("");
		displayOut(0, 0, "Program Ready");
	}
	
	public void displayOut(int mType, int msgCode, String printText)
	{
		switch(mType)
		{			
			case 2: textAreaMessage.append("< " + printText + "\n"); break;
			case 3: textAreaMessage.append("> " + printText + "\n"); break;
			default: textAreaMessage.append(printText + "\n");
		}
	}
    
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ACOS3ConfigureATR().setVisible(true);
            }
        });
    }
    
	public void onSendCommand(ReaderEvents.TransmitApduEventArg event) 
	{
		displayOut(2, 0, event.getAsString(true));
	}

	public void onReceiveCommand(ReaderEvents.TransmitApduEventArg event) 
	{
		displayOut(3, 0, event.getAsString(true) + "\r\n");
	}
    
    public boolean isAcos3()
    {
    	currentChipType = readerInterface.getChipType();
    	
    	if(currentChipType == ReaderInterface.CHIP_TYPE.ERROR)
    		return false;
    	
    	if(currentChipType != ReaderInterface.CHIP_TYPE.ACOS3 && currentChipType != ReaderInterface.CHIP_TYPE.ACOS3COMBI)
    	{
    		JOptionPane.showMessageDialog(null,"Card is not supported. Please present ACOS3 or ACOS3 Combi Card");
    		return false;
    	}
    	return true;
    }

}
