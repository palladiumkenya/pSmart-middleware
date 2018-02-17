package CardProgramming;
/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              ACOS3Account.java

  Description:       This sample program outlines the steps on how to
                     use the Account File functionalities of ACOS
                     using the PC/SC platform.
                     
  Author:            Donn Johnson A. Fabian

  Date:              October 11, 2012

  Revision Trail:   (Date/Author/Description)
  					 04-15-2010 / M.J.E.Castillo / Bug fixes

======================================================================*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;

import javax.smartcardio.CardException;


public class ACOS3Account extends JFrame implements ReaderEvents.TransmitApduHandler, ActionListener, KeyListener{

	int maxLen;
	private static String algorithm = "DES";
	static String VALIDCHARS = "0123456789";
	
	//GUI Variables
    private JButton buttonReset, buttonQuit, buttonClear, buttonInitialize, buttonConnect, buttonFormat, buttonCredit, buttonDebit, buttonRevoke, buttonBalance;
    private JCheckBox checkBoxDebitCertificate;
    private JComboBox comboBoxReader;
    private JPanel desPanel, accountPanel, messagePanel, readerPanel, securityKeyPanel;
    private JLabel labelReader, labelAmount, labelCertify, labelCredit, labelDebit, labelRevoke;
    private JTextArea textAreaMessage;
    private JRadioButton radioButton3Des, radioButtonDes;
    private JScrollPane scrollPaneMessage;
    private JTextField textFieldAmount, textFieldCertify, textFieldCredit, textFieldDebit, textFieldRevoke;
    private ButtonGroup buttonGroupDes;
    
    private PcscReader pcscReader;
    private Acos3 acos3;
    private ReaderInterface readerInterface;
    ReaderInterface.CHIP_TYPE currentChipType = ReaderInterface.CHIP_TYPE.UNKNOWN;
    
    public ACOS3Account() 
    {    	
    	this.setTitle("ACOS3 Account");
        initComponents();
        initMenu();
        
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
		setSize(690,535);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//sets the location of the form at the center of screen
   		Dimension dim = getToolkit().getScreenSize();
   		Rectangle abounds = getBounds();
   		setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
   		requestFocus();
   		
   		String[] rdrNameDef = {"Please select reader  "};
   		
		buttonGroupDes = new ButtonGroup();
	    readerPanel = new JPanel();
        labelReader = new JLabel();
        buttonInitialize = new JButton();
        buttonConnect = new JButton();
        buttonFormat = new JButton();
        desPanel = new JPanel();
        radioButtonDes = new JRadioButton();
        radioButton3Des = new JRadioButton();
        securityKeyPanel = new JPanel();
        labelCredit = new JLabel();
        labelDebit = new JLabel();
        labelCertify = new JLabel();
        labelRevoke = new JLabel();
        textFieldCredit = new JTextField();
        textFieldDebit = new JTextField();
        textFieldCertify = new JTextField();
        textFieldRevoke = new JTextField();
        accountPanel = new JPanel();
        labelAmount = new JLabel();
        textFieldAmount = new JTextField();
        checkBoxDebitCertificate = new JCheckBox();
        buttonCredit = new JButton();
        buttonDebit = new JButton();
        buttonBalance = new JButton();
        buttonRevoke = new JButton();
        messagePanel = new JPanel();
        scrollPaneMessage = new JScrollPane();
        textAreaMessage = new JTextArea();
        textAreaMessage.setWrapStyleWord(true);
        textAreaMessage.setLineWrap(true);
        buttonClear = new JButton();
        buttonReset = new JButton();
        buttonQuit = new JButton();
		comboBoxReader = new JComboBox(rdrNameDef);
		comboBoxReader.setSelectedIndex(0);
		
        labelReader.setText("Select Reader");
		
        buttonInitialize.setText("Initialize");
        buttonConnect.setText("Connect");
        buttonFormat.setText("Format Card");
        
        desPanel.setBorder(BorderFactory.createTitledBorder("Security Option"));
        
        radioButtonDes.setText("DES");
        radioButton3Des.setText("3DES");
        buttonGroupDes.add(radioButtonDes);
        buttonGroupDes.add(radioButton3Des);

        GroupLayout gl_desPanel = new GroupLayout(desPanel);
        desPanel.setLayout(gl_desPanel);
        gl_desPanel.setHorizontalGroup(
            gl_desPanel.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_desPanel.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(gl_desPanel.createParallelGroup(Alignment.LEADING)
                    .addComponent(radioButtonDes, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioButton3Des)))
        );
        gl_desPanel.setVerticalGroup(
            gl_desPanel.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_desPanel.createSequentialGroup()
                .addComponent(radioButtonDes)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(radioButton3Des)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout gl_readerPanel = new GroupLayout(readerPanel);
        gl_readerPanel.setHorizontalGroup(
        	gl_readerPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_readerPanel.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_readerPanel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_readerPanel.createSequentialGroup()
        					.addGroup(gl_readerPanel.createParallelGroup(Alignment.LEADING)
        						.addGroup(gl_readerPanel.createSequentialGroup()
        							.addGap(8)
        							.addComponent(desPanel, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(gl_readerPanel.createParallelGroup(Alignment.LEADING)
        								.addComponent(buttonInitialize, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
        								.addComponent(buttonConnect, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
        								.addComponent(buttonFormat, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)))
        						.addComponent(comboBoxReader, 0, 224, Short.MAX_VALUE))
        					.addGap(15))
        				.addGroup(gl_readerPanel.createSequentialGroup()
        					.addComponent(labelReader)
        					.addContainerGap(172, Short.MAX_VALUE))))
        );
        gl_readerPanel.setVerticalGroup(
        	gl_readerPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_readerPanel.createSequentialGroup()
        			.addGap(5)
        			.addComponent(labelReader)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(comboBoxReader, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addGroup(gl_readerPanel.createParallelGroup(Alignment.TRAILING)
        				.addGroup(gl_readerPanel.createSequentialGroup()
        					.addComponent(buttonConnect)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(buttonFormat))
        				.addGroup(gl_readerPanel.createParallelGroup(Alignment.BASELINE)
        					.addComponent(desPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addComponent(buttonInitialize)))
        			.addContainerGap())
        );
        
        readerPanel.setLayout(gl_readerPanel);

        accountPanel.setBorder(BorderFactory.createTitledBorder("Account Functions"));

        labelAmount.setText("Amount");
        checkBoxDebitCertificate.setText("Require Debit Certificate");
        buttonCredit.setText("Credit");
        buttonDebit.setText("Debit");
        buttonBalance.setText("Inquire Balance");
        buttonRevoke.setText("Revoke Debit");

        GroupLayout gl_acctPanel = new GroupLayout(accountPanel);
        accountPanel.setLayout(gl_acctPanel);
        gl_acctPanel.setHorizontalGroup(
            gl_acctPanel.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_acctPanel.createSequentialGroup()
                .addGroup(gl_acctPanel.createParallelGroup(Alignment.LEADING)
                    .addGroup(gl_acctPanel.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(labelAmount)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(textFieldAmount, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
                    .addGroup(gl_acctPanel.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(gl_acctPanel.createParallelGroup(Alignment.LEADING)
                            .addGroup(gl_acctPanel.createSequentialGroup()
                                .addGroup(gl_acctPanel.createParallelGroup(Alignment.LEADING, false)
                                    .addComponent(buttonDebit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(buttonCredit, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_acctPanel.createParallelGroup(Alignment.LEADING, false)
                                    .addComponent(buttonRevoke, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(buttonBalance, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(checkBoxDebitCertificate))))
                .addContainerGap())
        );
        gl_acctPanel.setVerticalGroup(
            gl_acctPanel.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_acctPanel.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_acctPanel.createParallelGroup(Alignment.BASELINE)
                    .addComponent(labelAmount)
                    .addComponent(textFieldAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(checkBoxDebitCertificate)
                .addGap(12, 12, 12)
                .addGroup(gl_acctPanel.createParallelGroup(Alignment.BASELINE)
                    .addComponent(buttonCredit)
                    .addComponent(buttonBalance))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(gl_acctPanel.createParallelGroup(Alignment.BASELINE)
                    .addComponent(buttonDebit)
                    .addComponent(buttonRevoke))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        scrollPaneMessage.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        textAreaMessage.setColumns(20);
        textAreaMessage.setRows(5);
        scrollPaneMessage.setViewportView(textAreaMessage);

        buttonClear.setText("Clear");
        buttonReset.setText("Reset");
        buttonQuit.setText("Quit");

        GroupLayout gl_msgPanel = new GroupLayout(messagePanel);
        gl_msgPanel.setHorizontalGroup(
        	gl_msgPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_msgPanel.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_msgPanel.createParallelGroup(Alignment.LEADING)
        				.addComponent(scrollPaneMessage, GroupLayout.PREFERRED_SIZE, 368, GroupLayout.PREFERRED_SIZE)
        				.addGroup(gl_msgPanel.createSequentialGroup()
        					.addComponent(buttonClear, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(buttonReset, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(buttonQuit, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(17, Short.MAX_VALUE))
        );
        gl_msgPanel.setVerticalGroup(
        	gl_msgPanel.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_msgPanel.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPaneMessage, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_msgPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(buttonClear)
        				.addComponent(buttonReset)
        				.addComponent(buttonQuit))
        			.addContainerGap())
        );
        messagePanel.setLayout(gl_msgPanel);

        securityKeyPanel.setBorder(BorderFactory.createTitledBorder("Security Keys"));

        labelCredit.setText("Credit");
        labelDebit.setText("Debit");
        labelCertify.setText("Certify");
        labelRevoke.setText("Revoke Debit");

        GroupLayout gl_secKeyPanel = new GroupLayout(securityKeyPanel);
        gl_secKeyPanel.setHorizontalGroup(
        	gl_secKeyPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_secKeyPanel.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_secKeyPanel.createParallelGroup(Alignment.LEADING)
        				.addComponent(labelCredit)
        				.addComponent(labelDebit)
        				.addComponent(labelCertify)
        				.addComponent(labelRevoke))
        			.addGap(9)
        			.addGroup(gl_secKeyPanel.createParallelGroup(Alignment.LEADING)
        				.addComponent(textFieldDebit, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
        				.addComponent(textFieldCertify, Alignment.TRAILING, 144, 144, Short.MAX_VALUE)
        				.addComponent(textFieldRevoke, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
        				.addComponent(textFieldCredit, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
        			.addContainerGap())
        );
        gl_secKeyPanel.setVerticalGroup(
        	gl_secKeyPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_secKeyPanel.createSequentialGroup()
        			.addGroup(gl_secKeyPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelCredit)
        				.addComponent(textFieldCredit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_secKeyPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelDebit)
        				.addComponent(textFieldDebit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_secKeyPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelCertify)
        				.addComponent(textFieldCertify, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_secKeyPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(labelRevoke)
        				.addComponent(textFieldRevoke, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        securityKeyPanel.setLayout(gl_secKeyPanel);

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(accountPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(securityKeyPanel, 0, 0, Short.MAX_VALUE)
        				.addComponent(readerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(messagePanel, GroupLayout.PREFERRED_SIZE, 395, Short.MAX_VALUE)
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(11)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(messagePanel, GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(readerPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(securityKeyPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(accountPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap())
        );
        
        getContentPane().setLayout(layout);
        
        buttonInitialize.setMnemonic(KeyEvent.VK_I);
        buttonConnect.setMnemonic(KeyEvent.VK_C);
        buttonReset.setMnemonic(KeyEvent.VK_R);
        buttonClear.setMnemonic(KeyEvent.VK_L);
        buttonFormat.setMnemonic(KeyEvent.VK_F);
        buttonCredit.setMnemonic(KeyEvent.VK_R);
        buttonDebit.setMnemonic(KeyEvent.VK_D);
        buttonBalance.setMnemonic(KeyEvent.VK_B);
        buttonRevoke.setMnemonic(KeyEvent.VK_V);
        buttonQuit.setMnemonic(KeyEvent.VK_Q);
        
        buttonInitialize.addActionListener(this);
        buttonConnect.addActionListener(this);
        buttonReset.addActionListener(this);
        buttonClear.addActionListener(this);
        buttonFormat.addActionListener(this);
        buttonCredit.addActionListener(this);
        buttonDebit.addActionListener(this);
        buttonBalance.addActionListener(this);
        buttonQuit.addActionListener(this);
        buttonRevoke.addActionListener(this);
        radioButtonDes.addActionListener(this);
        radioButton3Des.addActionListener(this);
        textFieldCredit.addKeyListener(this);
        textFieldCredit.setTransferHandler(null);
        textFieldDebit.addKeyListener(this);
        textFieldDebit.setTransferHandler(null);
        textFieldRevoke.addKeyListener(this);
        textFieldRevoke.setTransferHandler(null);
        textFieldCertify.addKeyListener(this);
        textFieldCertify.setTransferHandler(null);
        textFieldAmount.addKeyListener(this);    
        textFieldAmount.setTransferHandler(null);
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
				buttonReset.setEnabled(true);
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
				
				displayOut(0, 0, "Successfully connected to " + rdrcon);
				
				//Check if card inserted is an ACOS Card
				if(!isAcos3())
					return;				
				
				if(currentChipType == ReaderInterface.CHIP_TYPE.ACOS3COMBI)
					displayOut(0, 0, "Chip Type: ACOS3 Combi");
				else
					displayOut(0, 0, "Chip Type: ACOS3");
			    
				buttonFormat.setEnabled(true);
				textFieldCredit.setEnabled(true);
				textFieldDebit.setEnabled(true);
				textFieldCertify.setEnabled(true);
				textFieldRevoke.setEnabled(true);
				radioButtonDes.setEnabled(true);
				radioButton3Des.setEnabled(true);				
				maxLen = 8;
				
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
		
		if(buttonClear == e.getSource())
		{	
			textAreaMessage.setText("");
		}  // Clear
		
		if(buttonQuit == e.getSource())
		{
			this.dispose();			
		} // Quit
		
		if(buttonReset == e.getSource())
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
				displayOut(1, 0, "Program Ready\n");
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
		
		if(buttonFormat == e.getSource())
		{
			try
			{
				String tmpStr = "";
				byte[] tmpArray = new byte[4];
				
				if(!validTemplate())
				{
					return;
				}
				
				//Check if card inserted is an ACOS3 Card
				if(!isAcos3())
					return;
				
				//submit issuer code
				acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");				
				
				//select FF 02
				acos3.selectFile(Acos3.INTERNAL_FILE.PERSONALIZATION_FILE);
			    
			    //write to FF 02
			    //    This step will define the Option registers,
			    //    Security Option registers and Personalization bit
			    //    are not set
			    if(radioButtonDes.isSelected())
			    	tmpArray[0] = (byte)0x29;
			    else
			    	tmpArray[0] = (byte)0x2B;
			    
			    tmpArray[1] = (byte)0x00;
			    tmpArray[2] = (byte)0x03;
			    tmpArray[3] = (byte)0x00;
			    
			    acos3.writeRecord((byte)0x00, (byte)0x00, tmpArray);
			    
			    displayOut(0, 0, "FF 02 is updated");
			    
			    //submit issuer code to write into FF 05 and FF 06
			    acos3.submitCode(Acos3.CODE_TYPE.IC, "ACOSTEST");
			    
			    //select FF 05
				acos3.selectFile(Acos3.INTERNAL_FILE.ACCOUNT_FILE);			    
			    
			    //write to FF 05
			    //   Record 00
			    tmpArray[0] = (byte)0x00;
			    tmpArray[1] = (byte)0x00;
			    tmpArray[2] = (byte)0x00;
			    tmpArray[3] = (byte)0x00;			    
			    acos3.writeRecord((byte)0x00, (byte)0x00, tmpArray);			    
			    displayOut(0, 0, "Record 00 is updated");
			    
			    //   Record 01
			    tmpArray[0] = (byte)0x00;
			    tmpArray[1] = (byte)0x00;
			    tmpArray[2] = (byte)0x01;
			    tmpArray[3] = (byte)0x00;
			    acos3.writeRecord((byte)0x01, (byte)0x00, tmpArray);
			    displayOut(0, 0, "Record 01 is updated");
			    
			    //   Record 02
			    tmpArray[0] = (byte)0x00;
			    tmpArray[1] = (byte)0x00;
			    tmpArray[2] = (byte)0x00;
			    tmpArray[3] = (byte)0x00;
			    acos3.writeRecord((byte)0x02, (byte)0x00, tmpArray);
			    displayOut(0, 0, "Record 02 is updated");
			    
			    //   Record 03
			    tmpArray[0] = (byte)0x00;
			    tmpArray[1] = (byte)0x00;
			    tmpArray[2] = (byte)0x01;
			    tmpArray[3] = (byte)0x00;
			    acos3.writeRecord((byte)0x03, (byte)0x00, tmpArray);
			    displayOut(0, 0, "Record 03 is updated");
			    
			    //   Record 04
			    tmpArray[0] = (byte)0xFF;
			    tmpArray[1] = (byte)0xFF;
			    tmpArray[2] = (byte)0xFF;
			    tmpArray[3] = (byte)0x00;
			    acos3.writeRecord((byte)0x04, (byte)0x00, tmpArray);
			    displayOut(0, 0, "Record 04 is updated");
			    
			    //   Record 05
			    tmpArray[0] = (byte)0x00;
			    tmpArray[1] = (byte)0x00;
			    tmpArray[2] = (byte)0x00;
			    tmpArray[3] = (byte)0x00;
			    acos3.writeRecord((byte)0x05, (byte)0x00, tmpArray);
			    displayOut(0, 0, "Record 05 is updated");
			    
			    //   Record 06
			    tmpArray[0] = (byte)0x00;
			    tmpArray[1] = (byte)0x00;
			    tmpArray[2] = (byte)0x00;
			    tmpArray[3] = (byte)0x00;
			    acos3.writeRecord((byte)0x06, (byte)0x00, tmpArray);
			    displayOut(0, 0, "Record 06 is updated");
			    
			    //   Record 07
			    tmpArray[0] = (byte)0x00;
			    tmpArray[1] = (byte)0x00;
			    tmpArray[2] = (byte)0x00;
			    tmpArray[3] = (byte)0x00;
			    acos3.writeRecord((byte)0x07, (byte)0x00, tmpArray);
			    displayOut(0, 0, "Record 07 is updated");			    
			    displayOut(0, 0, "FF 05 is updated");
			    
			    //select FF 06
			    acos3.selectFile(Acos3.INTERNAL_FILE.ACCOUNT_SECURITY_FILE);			    
			    
			    int tmpInt;
			    //write to FF 06
			    if(radioButtonDes.isSelected())
			    {		    	
			    	tmpArray = new byte[8];
			    	// Record 00 for Debit key
			    	tmpStr = textFieldDebit.getText();		    	
			    	for(int i = 0; i < 8; i++)
			    	{
			    		tmpInt=(int) tmpStr.charAt(i);
			    		tmpArray[i] = (byte)tmpInt; 
			    	}
			    	acos3.writeRecord((byte)0x00, (byte)0x00, tmpArray);		
			    	displayOut(0, 0, "Debit key is updated");
			    	
					// Record 01 for Credit key
					tmpStr = textFieldCredit.getText();
					for(int i = 0; i < 8; i++)
				    {
				    	tmpInt=(int) tmpStr.charAt(i);
				    	tmpArray[i] = (byte)tmpInt; 
				    }
					acos3.writeRecord((byte)0x01, (byte)0x00, tmpArray);	
					displayOut(0, 0, "Credit key is updated");
					
					// Record 02 for Certify key
					tmpStr = textFieldCertify.getText();
					for(int i = 0; i < 8; i++)
			    	{
						tmpInt=(int) tmpStr.charAt(i);
			    		tmpArray[i] = (byte)tmpInt; 
			    	}
					acos3.writeRecord((byte)0x02, (byte)0x00, tmpArray);
					displayOut(0, 0, "Certify key is updated");
					
					// Record 03 for Revoke Debit key
					tmpStr = textFieldRevoke.getText();
					for(int i = 0; i < 8; i++)
				    {
						tmpInt=(int) tmpStr.charAt(i);
				    	tmpArray[i] = (byte)tmpInt; 
				    }
					acos3.writeRecord((byte)0x03, (byte)0x00, tmpArray);	
					displayOut(0, 0, "Revoke key is updated");
					displayOut(0, 0, "FF 06 is updated");					
			    }
			    else
			    {	
			    	tmpArray = new byte[8];
			    	// Record 04 for Left half of Debit key
			    	tmpStr = textFieldDebit.getText();
					for(int i = 0; i < 8; i++)
			    	{
						tmpInt=(int) tmpStr.charAt(i);
			    		tmpArray[i] = (byte)tmpInt; 
			    	}
					acos3.writeRecord((byte)0x04, (byte)0x00, tmpArray);					
					displayOut(0, 0, "Left half of Debit key is updated");
					
					//  Record 00 for Right half of Debit key
					for(int i = 8; i < 16; i++)
			    	{
						tmpInt =(int) tmpStr.charAt(i);
			    		tmpArray[i-8] = (byte)tmpInt; 
			    	}
					acos3.writeRecord((byte)0x00, (byte)0x00, tmpArray);					
					displayOut(0, 0, "Right half of Debit key is updated");
					
					// Record 05 for Left half of Credit key
			    	tmpStr = textFieldCredit.getText();
					for(int i = 0; i < 8; i++)
			    	{
						tmpInt=(int) tmpStr.charAt(i);
			    		tmpArray[i] = (byte)tmpInt; 
			    	}
					acos3.writeRecord((byte)0x05, (byte)0x00, tmpArray);
					displayOut(0, 0, "Left half of Credit key is updated");
					
					// Record 01 for Right half of Credit key
					for(int i = 8; i < 16; i++)
			    	{
						tmpInt=(int) tmpStr.charAt(i);
			    		tmpArray[i-8] = (byte)tmpInt; 
			    	}
					acos3.writeRecord((byte)0x01, (byte)0x00, tmpArray);
					displayOut(0, 0, "Right half of Credit key is updated");
					
					// Record 06 for Left half of Certify key
			    	tmpStr = textFieldCertify.getText();
					for(int i = 0; i < 8; i++)
			    	{
				   		tmpInt=(int) tmpStr.charAt(i);
			    		tmpArray[i] = (byte)tmpInt; 
			    	}
					acos3.writeRecord((byte)0x06, (byte)0x00, tmpArray);
					displayOut(0, 0, "Left half of Certify key is updated");
					
					// Record 02 for Right half of Certify key
					for(int i = 8; i < 16; i++)
			    	{
				   		tmpInt=(int) tmpStr.charAt(i);
			    		tmpArray[i-8] = (byte)tmpInt; 
			    	}
					acos3.writeRecord((byte)0x02, (byte)0x00, tmpArray);					
					displayOut(0, 0, "Right half of Certify key is updated");
					
					// Record 07 for Left half of Revoke Debit key
			    	tmpStr = textFieldRevoke.getText();
					for(int i = 0; i < 8; i++)
			    	{
				   		tmpInt=(int) tmpStr.charAt(i);
			    		tmpArray[i] = (byte)tmpInt; 
			    	}
					acos3.writeRecord((byte)0x07, (byte)0x00, tmpArray);
					displayOut(0, 0, "Left half of Revoke key is updated");
					
					// Record 03 for Right half of Revoke Debit key
					for(int i = 8; i < 16; i++)
			    	{					 
				   		tmpInt=(int) tmpStr.charAt(i);
			    		tmpArray[i-8] = (byte)tmpInt;		    		
			    	}
					acos3.writeRecord((byte)0x03, (byte)0x00, tmpArray);	
					displayOut(0, 0, "Right half of Revoke key is updated");
					displayOut(0, 0, "FF 06 is updated");
			    }			    
			    
			    buttonCredit.setEnabled(true);
				buttonDebit.setEnabled(true);
				buttonRevoke.setEnabled(true);
				buttonBalance.setEnabled(true);
				textFieldAmount.setEnabled(true);
				checkBoxDebitCertificate.setEnabled(true);
				
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
		
		if(buttonCredit == e.getSource())
		{
			try
			{
				String tmpStr = "";
				byte[] tmpArray = new byte[4];
				byte[] tripleMac = new byte[4];
				byte[] TTREFc = new byte[4];
				byte[] ATREF = new byte[6];
				byte[] tmpKey = new byte[16];
				byte[] tempAmount = new byte[3];
				int amount, tmpVal;
				
				//Check if Credit key and valid Transaction value are provided
				if(textFieldCredit.getText().length() < maxLen)
				{	
					textFieldCredit.selectAll();
					textFieldCredit.requestFocus();
					return;
				}			
				if(textFieldAmount.getText().equals(""))
				{	
					textFieldAmount.requestFocus();
					JOptionPane.showMessageDialog(this, "Please key-in numeric value for Amount.", "Error", JOptionPane.ERROR_MESSAGE);
					return;				
				}
				
				if(Integer.parseInt(textFieldAmount.getText()) > 16777215)
				{	
					textFieldAmount.requestFocus();
					JOptionPane.showMessageDialog(this, "Please key-in valid Amount. Valid value: 1 - 16,777,215.", "Error", JOptionPane.ERROR_MESSAGE);
					return;				
				}
				
				if(Integer.parseInt(textFieldAmount.getText()) < 1)
				{	
					textFieldAmount.requestFocus();
					JOptionPane.showMessageDialog(this, "Please key-in valid Amount. Valid value: 1 - 16,777,215.", "Error", JOptionPane.ERROR_MESSAGE);
					return;				
				}
				
				//Check if card inserted is an ACOS Card
				if(!isAcos3())
					return;
				
				//Issue INQUIRE ACCOUNT command using any arbitrary data and Credit key
				//     Arbitrary data is 1111h
				//Issue GET RESPONSE command with Le = 19h or 25 bytes
				for(int i = 0; i < 4; i++)
					tmpArray[i] = (byte)0x01;
				
				InquireAccountResponse inquireAccountResponse = acos3.inquireAccount((byte)0x02, tmpArray);				
				
				//Store ACOS card values for TTREFc and ATREF				
				TTREFc = inquireAccountResponse.getTtrefc();				
				ATREF = inquireAccountResponse.getAtref();
				
				tmpArray = new byte[32];
				
				//Prepare MAC data block: E2 + AMT + TTREFc + ATREF + 00 + 00
				//   use tmpArray as the data block
				tmpArray[0] = (byte)0xE2;
				
				amount = Integer.parseInt(textFieldAmount.getText());				
				tmpVal = (int)amount/256;
				tmpVal = (int)tmpVal/256;
				tmpArray[1] = tempAmount[0] = (byte)(tmpVal % 256);
				tmpVal = (int)amount/256;
				tmpArray[2] = tempAmount[1] = (byte)(tmpVal % 256);
				tmpArray[3] = tempAmount[2] = (byte)(amount % 256);
				
				for(int i = 0; i < 4; i++)
					tmpArray[i+4] = TTREFc[i];			
	                        
	            //Increment the last 2 Bytes of the ATREF. ATREF is composed of 4-byte AID and 2-byte ATC.
				tmpVal = (int)((ATREF[4]*256) + ATREF[5])+1;
	            ATREF[4] = (byte)(tmpVal/256);
	            ATREF[5] = (byte)(tmpVal%256);
	                        
				for(int i = 0; i < 6; i++)
					tmpArray[i+8] = ATREF[i];                        
				
				tmpArray[14] = (byte)0x00;
				tmpArray[15] = (byte)0x00;
				
				//Generate applicable MAC values, MAC result will be stored in tmpArray
				tmpStr = textFieldCredit.getText();
				int tmpInt;
				for(int i = 0; i < tmpStr.length(); i++)
				{
					tmpInt = (int)tmpStr.charAt(i);
					tmpKey[i] = (byte)tmpInt;
				}
				
				byte[] tempMac = new byte[4];
				
				if(radioButtonDes.isSelected())
				{
					mac(tmpArray, tmpKey);
					
					//Format Credit command data and execute credit command
					//   Using tmpArray, the first four bytes are carried over
					for(int i = 0; i < 4; i++)					
						tempMac[i] = tmpArray[i];
				
				}
				else
				{
					tripleMac = TripleMac(tmpArray, tmpKey);
				
					//Format Credit command data and execute credit command
					//   Using tmpArray, the first four bytes are carried over
					for(int i = 0; i < 4; i++)					
						tempMac[i] = tripleMac[i];
				}		
				
				acos3.credit(tempMac, tempAmount, TTREFc);
				
				displayOut(0, 0, "Credit transaction completed");
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
		}  // Credit		
		
		if(buttonDebit == e.getSource())
		{			
			try
			{				
				byte[] tmpArray = new byte[4]; 
				byte[] tripleMac = new byte[4]; 
				byte[] tmpKey = new byte[16];
				byte[] TTREFd = new byte[4];
				byte[] ATREF = new byte[6];			
				byte[] tempAmount = new byte[3];
				int tmpBalance, newBalance, amount, tmpVal;
				String tmpStr = "";
				
				//Check if Debit key and valid Transaction value are provided
				if(textFieldDebit.getText().length() != maxLen)
				{	
					textFieldDebit.selectAll();
					textFieldDebit.requestFocus();
					return;
				}			
				if(textFieldAmount.getText().equals(""))
				{	
					textFieldAmount.requestFocus();
					JOptionPane.showMessageDialog(this, "Please key-in numeric value for Amount.", "Error", JOptionPane.ERROR_MESSAGE);
					return;				
				}
				
				if(Integer.parseInt(textFieldAmount.getText()) > 16777215)
				{	
					textFieldAmount.requestFocus();
					JOptionPane.showMessageDialog(this, "Please key-in valid Amount. Valid value: 1 - 16,777,215.", "Error", JOptionPane.ERROR_MESSAGE);
					return;				
				}
				
				if(Integer.parseInt(textFieldAmount.getText()) < 1)
				{	
					textFieldAmount.requestFocus();
					JOptionPane.showMessageDialog(this, "Please key-in valid Amount. Valid value: 1 - 16,777,215.", "Error", JOptionPane.ERROR_MESSAGE);
					return;				
				}
				
				//Check if card inserted is an ACOS Card
				if(!isAcos3())
					return;
				
				//Issue INQUIRE ACCOUNT command using any arbitrary data and Credit key
				//    Arbitrary data is 1111h
				//Issue GET RESPONSE command with Le = 19h or 25 bytes
				for(int i = 0; i < 4; i++)
					tmpArray[i] = (byte)0x01;
				
				InquireAccountResponse inquireAccountResponse = acos3.inquireAccount((byte)0x02, tmpArray);			
							
				tmpBalance = inquireAccountResponse.getBalanceInt();
				
				//Store ACOS card values for TTREFd and ATREF			
				TTREFd = inquireAccountResponse.getTtrefd();
				ATREF = inquireAccountResponse.getAtref();
				
				tmpArray = new byte[32];
				
				//Prepare MAC data block: E6 + AMT + TTREFd + ATREF + 00 + 00
				//    use tmpArray as the data block
				amount = Integer.parseInt(textFieldAmount.getText());
				tmpArray[0] = (byte)0xE6;
				tmpVal = (int)(amount / 256);
				tmpVal = (int)(tmpVal / 256);
				tmpArray[1] = tempAmount[0] = (byte)(tmpVal % 256);
				tmpVal = (int)(amount/256);
				tmpArray[2] = tempAmount[1] = (byte)(tmpVal % 256);
				tmpArray[3] = tempAmount[2] = (byte)(amount % 256);
				
				for(int i = 0; i < 4; i++)
					tmpArray[i + 4] = TTREFd[i];			
	                        
	            //Increment the last 2 Bytes of the ATREF. ATREF is composed of 4-byte AID and 2-byte ATC.
				tmpVal = (int)((ATREF[4]*256) + ATREF[5])+1;
	            ATREF[4] = (byte) (tmpVal/256);
	            ATREF[5] = (byte)(tmpVal%256);
	                        
				for(int i = 0; i < 6; i++)
					tmpArray[i + 8] = ATREF[i];			
	                        
				tmpArray[14] = (byte)0x00;
				tmpArray[15] = (byte)0x00;
				
				//Generate applicable MAC values, MAC result will be stored in tmpArray
				tmpStr = textFieldDebit.getText();
				for(int i = 0; i < tmpStr.length(); i++)
					tmpKey[i] = (byte)((int)tmpStr.charAt(i));
				
				byte[] tempMac = new byte[4];
				
				if(radioButtonDes.isSelected())
				{
					mac(tmpArray, tmpKey);
					
					for(int i = 0; i < 4; i++)					
						tempMac[i] = tmpArray[i];
				}
				else
				{
					tripleMac = TripleMac(tmpArray, tmpKey);
					
					for(int i = 0; i < 4; i++)					
						tempMac[i] = tripleMac[i];
				}				
				
				if(!checkBoxDebitCertificate.isSelected())
				{	
					acos3.debit(tempMac, tempAmount, TTREFd);				
				}
				else
				{
					byte[] response = new byte[4];					
					response = acos3.debitWithDbc(tempMac, tempAmount, TTREFd);					
					
					//Prepare MAC data block: 01 + New Balance + ATC + TTREFD + 00 + 00 + 00
				    //   use tmpArray as the data block					
					newBalance = tmpBalance - amount;
					
					tmpArray = new byte[32];
					tmpArray[0] = (byte)0x01;
					
					tmpVal = (int)(newBalance / 256);
					tmpVal = (int)(tmpVal / 256);
					tmpArray[1] = (byte)(tmpVal % 256);
					tmpVal = (int)(newBalance / 256);
					tmpArray[2] = (byte)(tmpVal % 256);
					tmpArray[3] = (byte)(newBalance % 256);
					
					tmpVal = (int)(amount / 256);
					tmpVal = (int)(tmpVal / 256);
					tmpArray[4] = (byte)(tmpVal % 256);
					tmpVal = (int)(amount / 256);
					tmpArray[5] = (byte)(tmpVal % 256);
					tmpArray[6] = (byte)(amount % 256);
					tmpArray[7] = ATREF[4];
					tmpArray[8] = ATREF[5];    
					
					for(int i = 0; i < 4; i++)
						tmpArray[i+9] = TTREFd[i];
					
					tmpArray[13] = (byte)0x00;
					tmpArray[14] = (byte)0x00;
					tmpArray[15] = (byte)0x00;
					
					//Generate applicable MAC values, MAC result will be stored in tmpArray
					tmpStr = textFieldDebit.getText();
					for(int i = 0; i < tmpStr.length(); i++)
						tmpKey[i] = (byte)((int)tmpStr.charAt(i));
					
					if(radioButtonDes.isSelected())
					{
						mac(tmpArray, tmpKey);
						
						for (int i = 0; i < 4; i++)
						{
							if ((byte)response[i] != (byte)tmpArray[i])
							{
								displayOut(0, 0, "Debit Certificate Failed");
								checkBoxDebitCertificate.setSelected(false);
								return;
							}
						}
					}
					else
					{
						tripleMac = TripleMac(tmpArray, tmpKey);
						
						for (int i = 0; i < 4; i++)
						{
							if ((byte)response[i] != (byte)tripleMac[i])
							{
								displayOut(0, 0, "Debit Certificate Failed");
								checkBoxDebitCertificate.setSelected(false);
								return;
							}
						}
					}
					
					displayOut(0, 0, "DEBIT CERTIFICATE: " + Helper.byteAsString(response, 0, response.length, true));				
				}
				
				displayOut(0, 0, "Debit transaction completed");
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
		} // Debit
		
		if(buttonBalance == e.getSource())
		{			
			try
			{
				String tmpStr = "";
				byte[] tmpArray = new byte[4];		
				byte[] tripleMac = new byte[4];	
				byte[] tmpKey = new byte[16];				
				byte[] TTREFc = new byte[4];
				byte[] TTREFd = new byte[4];
				byte[] ATREF = new byte[6];				
				int tmpBalance;
				byte lastTran;				
				
				//Check if Certify key is provided
				if(textFieldCertify.getText().length() != maxLen)
				{				
					textFieldCertify.selectAll();
					textFieldCertify.requestFocus();
					return;				
				}
								
				//Issue INQUIRE ACCOUNT command using any arbitrary data and Certify key
				//Issue GET RESPONSE command with Le = 19h or 25 bytes
				//    Arbitrary data is 1111h
				for(int i = 0; i < 4; i++)
					tmpArray[i] = (byte)0x01;
				
				InquireAccountResponse inquireAccountResponse = acos3.inquireAccount((byte)0x02, tmpArray);
				
				//Check integrity of data returned by card
				// Build MAC input data
				// Extract the info from ACOS card in Dataout
				lastTran = inquireAccountResponse.getTransactionType();
				tmpBalance = inquireAccountResponse.getBalanceInt();
			    
			    TTREFc = inquireAccountResponse.getTtrefc();			    
			    TTREFd = inquireAccountResponse.getTtrefd();
			    ATREF = inquireAccountResponse.getAtref();
			    
			    tmpArray = new byte[32];
			    for(int i = 0; i < 4; i++)
					tmpArray[i] = (byte)0x01;
			    
			    // Move data from ACOS card as input to MAC calculations
			    tmpArray[4] = inquireAccountResponse.getTransactionType();
			    for(int i = 0; i < 3; i++)
			    	tmpArray[i+5] = inquireAccountResponse.getBalance()[i];
			    
			    for(int i = 0; i < 6; i++)
			    	tmpArray[i+8] = ATREF[i];
			 
			    tmpArray[14] = (byte)0x00;
			    tmpArray[15] = (byte)0x00;
			    
			    for(int i = 0; i < 4; i++)
			    	tmpArray[i+16] = TTREFc[i];
			    
			    for(int i = 0; i < 4; i++)
			    	tmpArray[i+20] = TTREFd[i];
			    
			    //Generate applicable MAC values
			    tmpStr = textFieldCertify.getText();
			    for(int i = 0; i < tmpStr.length(); i++)
			    	tmpKey[i] = (byte)((int)tmpStr.charAt(i));
			    
			    if(radioButtonDes.isSelected())
			    {
			    	mac(tmpArray, tmpKey);
			    	
			    	//Compare MAC values
				    for(int i = 0; i < 4; i++)
				    {	
				    	if(tmpArray[i] != inquireAccountResponse.getMac4()[i])
				    	{
				    		displayOut(0, 0, "MAC is incorrect, data integrity is jeopardized.");
				    		return;		    		
				    	}		    	
				    }
			    }
			    else
		    	{
			    	tripleMac = TripleMac(tmpArray, tmpKey);
			    	
			    	//Compare MAC values
				    for(int i = 0; i < 4; i++)
				    {	
				    	if(tripleMac[i] != inquireAccountResponse.getMac4()[i])
				    	{
				    		displayOut(0, 0, "MAC is incorrect, data integrity is jeopardized.");
				    		return;		    		
				    	}		    	
				    }
		    	}

			    //Display relevant data from ACOS card
			    switch(lastTran)
			    {		    
				    case 1: tmpStr = "DEBIT"; break;
				    case 2: tmpStr = "REVOKE DEBIT"; break;
				    case 3: tmpStr = "CREDIT"; break;
				    default: tmpStr = "NOT DEFINED";		    
			    }
			    
			    displayOut(0, 0, "Last transaction is " + tmpStr + ".");
			    displayOut(0, 0, "Amount is " + tmpBalance);
			    textFieldAmount.setText(Integer.toString(tmpBalance));		
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
		}  // Inquire Balance
		
		if(buttonRevoke == e.getSource())
		{			
			try
			{
				String tmpStr = "";
				int amount, tmpVal;
				byte[] tmpArray = new byte[4];	
				byte[] tripleMac = new byte[4];
				byte[] tmpKey = new byte[16];
				byte[] TTREFd = new byte[4];
				byte[] ATREF = new byte[6];
				
				//1. Check if Debit key and valid Transaction value are provided
				if(textFieldRevoke.getText().length() != maxLen)
				{	
					textFieldRevoke.selectAll();
					textFieldRevoke.requestFocus();
					return;
				}								
				if(textFieldAmount.getText().equals(""))
				{	
					textFieldAmount.requestFocus();
					JOptionPane.showMessageDialog(this, "Please key-in numeric value for Amount.", "Error", JOptionPane.ERROR_MESSAGE);
					return;				
				}
				
				if(Integer.parseInt(textFieldAmount.getText()) > 16777215)
				{	
					textFieldAmount.requestFocus();
					JOptionPane.showMessageDialog(this, "Please key-in valid Amount. Valid value: 1 - 16,777,215.", "Error", JOptionPane.ERROR_MESSAGE);
					return;				
				}
				
				if(Integer.parseInt(textFieldAmount.getText()) < 1)
				{	
					textFieldAmount.requestFocus();
					JOptionPane.showMessageDialog(this, "Please key-in valid Amount. Valid value: 1 - 16,777,215.", "Error", JOptionPane.ERROR_MESSAGE);
					return;				
				}
				
				//Check if card inserted is an ACOS Card
				if(!isAcos3())
					return;
				
				//Issue INQUIRE ACCOUNT command using any arbitrary data and Credit key
				//    Arbitrary data is 1111h
				//Issue GET RESPONSE command with Le = 19h or 25 bytes
				for(int i = 0; i < 4; i++)
					tmpArray[i] = (byte)0x01;
				
				InquireAccountResponse inquireAccountResponse = acos3.inquireAccount((byte)0x02, tmpArray);			
				
				//Store ACOS card values for TTREFd and ATREF
				TTREFd = inquireAccountResponse.getTtrefd();
				ATREF = inquireAccountResponse.getAtref();
				
				tmpArray = new byte[32];
				
				//Prepare MAC data block: E8 + AMT + TTREFd + ATREF + 00 + 00
				//    use tmpArray as the data block
				tmpArray[0] = (byte)0xE8;
				
				amount = Integer.parseInt(textFieldAmount.getText());				
				tmpVal = (int)(amount / 256);
				tmpVal = (int)(tmpVal / 256);
				tmpArray[1] = (byte)(tmpVal % 256);
				tmpVal = (int)(amount/256);
				tmpArray[2] = (byte)(tmpVal % 256);
				tmpArray[3] = (byte)(amount % 256);
				
				for(int i = 0; i < 4; i++)
					tmpArray[i + 4] = TTREFd[i];
				
				for(int i = 0; i < 6; i++)
					tmpArray[i + 8] = ATREF[i];
				
	            //Increment the last 2 Bytes of the ATREF. ATREF is composed of 4-byte AID and 2-byte ATC.
				tmpVal = (int)((ATREF[4]*256) + ATREF[5])+1;
	            ATREF[4] = (byte)(tmpVal/256);
	            ATREF[5] = (byte)(tmpVal%256);
	                        
	            for(int i = 0; i < 6; i++)
					tmpArray[i + 8] = ATREF[i];
	                        
				tmpArray[14] = (byte)0x00;
				tmpArray[15] = (byte)0x00;
				
				//Generate applicable MAC values, MAC result will be stored in tmpArray
				tmpStr = textFieldRevoke.getText();
				for(int i = 0; i < tmpStr.length(); i++)
					tmpKey[i] = (byte)((int)tmpStr.charAt(i));
				
				byte[] tempMac = new byte[4];
				
				if(radioButtonDes.isSelected())
				{
					mac(tmpArray, tmpKey);
					
					for(int i = 0; i < 4; i++)					
						tempMac[i] = tmpArray[i];
				}
				else
				{
					tripleMac = TripleMac(tmpArray, tmpKey);
					
					for(int i = 0; i < 4; i++)					
						tempMac[i] = tripleMac[i];
				}
				
				//Execute Revoke Debit command data and execute credit command
				//    Using tmpArray, the first four bytes storing the MAC value are carried over
				acos3.revokeDebit(tempMac);
				displayOut(0, 0, "Amount is " + inquireAccountResponse.getBalanceInt());
				displayOut(0, 0, "Revoke Debit transaction completed");		
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
		} // Revoke
		
		if(radioButtonDes == e.getSource())
		{ 			
			textFieldCredit.setText("");
			textFieldDebit.setText("");
			textFieldCertify.setText("");
			textFieldRevoke.setText("");
			textFieldAmount.setText("");
			maxLen = 8;
		} // rbDes

		if(radioButton3Des == e.getSource())
		{	
			textFieldCredit.setText("");
			textFieldDebit.setText("");
			textFieldCertify.setText("");
			textFieldRevoke.setText("");
			textFieldAmount.setText("");
			maxLen = 16;
		}  // rb3Des
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

	// MAC as defined in ACOS manual
	// receives 8-byte Key and 16-byte Data
	// result is stored in Data
	public void mac(byte[] Data, byte[]  key)
	{
		int i;   

		DES(Data,key);
		for (i=0; i<8; i++)
			Data[i] = Data[i] ^= Data [i + 8];
		
		DES(Data, key);
	}
	
	// Triple MAC as defined in ACOS manual
	// receives 16-byte Key and 16-byte Data
	// result is stored in Data	
	public static byte[] TripleMac(byte[] Data, byte[] key) {
        int i;

        byte[] testByte = new byte[Data.length];

        System.arraycopy(Data, 0, testByte, 0, 16);

        Data = tripleDES(Data, key);

        System.arraycopy(testByte, 8, Data, 8, Data.length - 8);

        for (i = 0; i < 8; i++)
            Data[i] = Data[i] ^= Data[i + 8];

        Data = tripleDES(Data, key);
        System.arraycopy(testByte, 8, Data, 8, Data.length - 8);
        return Data;
    }

	public boolean validTemplate()
	{
		if(textFieldCredit.getText().length() == 0)
		{
			JOptionPane.showMessageDialog(this, "Please key-in numeric value for Credit Key.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldCredit.selectAll();
			textFieldCredit.requestFocus();
			
			return false;
		}
		
		if(textFieldDebit.getText().length() == 0)
		{
			JOptionPane.showMessageDialog(this, "Please key-in numeric value for Debit Key.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldDebit.selectAll();
			textFieldDebit.requestFocus();
			
			return false;
		}
		
		if(textFieldCertify.getText().length() == 0)
		{
			JOptionPane.showMessageDialog(this, "Please key-in numeric value for Certify Key.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldCertify.selectAll();
			textFieldCertify.requestFocus();
			
			return false;
		}
		
		if(textFieldRevoke.getText().length() == 0)
		{
			JOptionPane.showMessageDialog(this, "Please key-in numeric value for Revoke Debit Key.", "Error", JOptionPane.ERROR_MESSAGE);
			textFieldRevoke.selectAll();
			textFieldRevoke.requestFocus();
			
			return false;
		}
		
		if(textFieldCredit.getText().length() < maxLen)
		{
			JOptionPane.showMessageDialog(this, "Invalid input length. Length must be " + maxLen, "Error", JOptionPane.ERROR_MESSAGE);
			textFieldCredit.selectAll();
			textFieldCredit.requestFocus();
			
			return false;
		}

		if(textFieldDebit.getText().length() < maxLen)
		{
			JOptionPane.showMessageDialog(this, "Invalid input length. Length must be " + maxLen, "Error", JOptionPane.ERROR_MESSAGE);
			textFieldDebit.selectAll();
			textFieldDebit.requestFocus();
			
			return false;
		}
		
		if(textFieldCertify.getText().length() < maxLen)
		{
			JOptionPane.showMessageDialog(this, "Invalid input length. Length must be " + maxLen, "Error", JOptionPane.ERROR_MESSAGE);
			textFieldCertify.selectAll();
			textFieldCertify.requestFocus();
			
			return false;
		}	
		
		if(textFieldRevoke.getText().length() < maxLen)
		{
			JOptionPane.showMessageDialog(this, "Invalid input length. Length must be " + maxLen, "Error", JOptionPane.ERROR_MESSAGE);
			textFieldRevoke.selectAll();
			textFieldRevoke.requestFocus();
			
			return false;
		}
		
		return true;
	}
		
	public void displayOut(int mType, int msgCode, String printText)
	{
		switch(mType)
		{
			case 2: textAreaMessage.append("\n<< " + printText + "\n"); break;
			case 3: textAreaMessage.append(">> " + printText); break;
			default: textAreaMessage.append(printText + "\n");
		}
	}

	public void initMenu()
	{	
		buttonConnect.setEnabled(false);
		buttonInitialize.setEnabled(true);
		buttonReset.setEnabled(false);
		buttonFormat.setEnabled(false);
		buttonCredit.setEnabled(false);
		buttonDebit.setEnabled(false);
		buttonRevoke.setEnabled(false);
		buttonBalance.setEnabled(false);
		textFieldCredit.setEnabled(false);
		textFieldDebit.setEnabled(false);
		textFieldCertify.setEnabled(false);
		textFieldRevoke.setEnabled(false);
		textFieldAmount.setEnabled(false);
		checkBoxDebitCertificate.setEnabled(false);
		radioButtonDes.setEnabled(false);
		radioButton3Des.setEnabled(false);
		radioButtonDes.setSelected(true);
		textFieldCredit.setText("");
		textFieldDebit.setText("");
		textFieldRevoke.setText("");
		textFieldCertify.setText("");
		textFieldAmount.setText("");
		displayOut(0, 0, "Program Ready\n");
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

  		//Check valid characters
  		if(textFieldAmount.isFocusOwner())
  		{	
  			if (VALIDCHARS.indexOf(x) == -1 ) 
  				ke.setKeyChar(empty);
  		}
  					  
		//Limit character length
  		if (textFieldAmount.isFocusOwner())
  		{	
  			if (((JTextField)ke.getSource()).getText().length() >= 8 ) 
  			{		
  				ke.setKeyChar(empty);  				
  				return;
  			}	
  		}
  		else
  		{  	  	
	  		if(radioButtonDes.isSelected())
	  		{
	  			if (((JTextField)ke.getSource()).getText().length() >= maxLen ) 
	  			{		
	  				ke.setKeyChar(empty);  				
	  				return;
	  			}			
	  		}  		
	  		if(radioButton3Des.isSelected())
	  		{
	  			if (((JTextField)ke.getSource()).getText().length() >= maxLen ) 
	  			{
	  				ke.setKeyChar(empty);
	  				return;
	  			}			
	  		}  		    	
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
                new ACOS3Account().setVisible(true);
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

    
	