package card.ACOS3;/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              mainApplet.java

  Description:       This program enables the user to browse the 
  					 sample codes for ACOS 3

  Author:            M.J.E.C. Castillo

  Date:              April 20, 2009

  Revision Trail:   (Date/Author/Description)

======================================================================*/

import java.awt.event.*;
import javax.swing.*;
import javax.swing.JApplet;


public class ACOS3MainApplet extends JApplet implements ActionListener
{
	public ACOS3MainApplet() {
	}
	
	//Variables
	boolean openAcct=false, openConfATR=false, openCreateFiles=false, openEncrypt=false, openMutAuth=false;
	boolean openReadWrite=false, openReadWriteBin=false, openSecMsg=false;
	
	//GUI Variables
    private JButton buttonAccount, buttonCreateFiles, buttonEncrypt, buttonMutualAuthentication, buttonReadWrite, buttonReadWriteBinary, buttonSecureMessaging;
	
	static ACOS3Account acct;
	//static ACOS3ConfigureATR confATR;
	static ACOS3CreateFiles createFiles;
	static ACOS3Encrypt encrypt;
	static ACOS3MutualAuthentication mutAuth;
	static ACOS3ReadWrite readWrite;
	static ACOS3ReadWriteBinary readWriteBin;
	static ACOS3SecureMessaging SecMsg;
	
	public void init() 
   	{
		setSize(200,288);
		buttonAccount = new JButton();
		buttonAccount.setBounds(15, 50, 175, 23);
		//buttonConfigureATR = new JButton();
		buttonCreateFiles = new JButton();
		buttonCreateFiles.setBounds(15, 80, 175, 23);
		buttonEncrypt = new JButton();
		buttonEncrypt.setBounds(15, 110, 175, 23);
		buttonMutualAuthentication = new JButton();
		buttonMutualAuthentication.setBounds(15, 140, 175, 23);
		buttonReadWrite = new JButton();
		buttonReadWrite.setBounds(15, 170, 175, 23);
		buttonReadWriteBinary = new JButton();
		buttonReadWriteBinary.setBounds(15, 200, 175, 23);
		buttonSecureMessaging = new JButton();
		buttonSecureMessaging.setBounds(15, 229, 175, 23);
		
		buttonAccount.setText("Account");

		//buttonConfigureATR.setText("Configure ATR");
		
		buttonCreateFiles.setText("Create Files");
		
		buttonEncrypt.setText("Secret Codes Encryption");
		
		buttonMutualAuthentication.setText("Mutual Authentication");
		
		buttonReadWrite.setText("Read Write");
		
		buttonReadWriteBinary.setText("Binary Files");
		
		buttonSecureMessaging.setText("Secure Messaging");
        getContentPane().setLayout(null);
        getContentPane().add(buttonAccount);
        getContentPane().add(buttonCreateFiles);
        getContentPane().add(buttonEncrypt);
        getContentPane().add(buttonMutualAuthentication);
        getContentPane().add(buttonReadWrite);
        getContentPane().add(buttonReadWriteBinary);
        getContentPane().add(buttonSecureMessaging);
        
        JLabel lblAcosSampleCodes = new JLabel("ACOS3 Sample Codes");
        lblAcosSampleCodes.setBounds(46, 25, 131, 14);
        getContentPane().add(lblAcosSampleCodes);
        
        buttonAccount.addActionListener(this);
       // buttonConfigureATR.addActionListener(this);
        buttonCreateFiles.addActionListener(this);
        buttonEncrypt.addActionListener(this);
        buttonMutualAuthentication.addActionListener(this);
        buttonReadWrite.addActionListener(this);
        buttonReadWriteBinary.addActionListener(this);
        buttonSecureMessaging.addActionListener(this);
   		
   	}
	
	public void actionPerformed(ActionEvent e) 
	{
		
		if(buttonAccount == e.getSource())
		{
			
			closeFrames();
			
			if(openAcct == false)
			{
				acct = new ACOS3Account();
				acct.setVisible(true);
				openAcct = true;
			}
			else
			{
				acct.dispose();
				acct = new ACOS3Account();
				acct.setVisible(true);
				openAcct = true;
			}
			
		}
		
		/*if(buttonConfigureATR == e.getSource())
		{			
			closeFrames();
			
			if(openConfATR == false)
			{
				confATR = new ACOS3ConfigureATR();
				confATR.setVisible(true);
				openConfATR = true;
			}
			else
			{
				confATR.dispose();
				confATR = new ACOS3ConfigureATR();
				confATR.setVisible(true);
				openConfATR = true;
			}		
		}*/
		
		if(buttonCreateFiles == e.getSource())
		{
			
			closeFrames();
			
			if(openCreateFiles == false)
			{
				createFiles = new ACOS3CreateFiles();
				createFiles.setVisible(true);
				openCreateFiles = true;
			}
			else
			{
				createFiles.dispose();
				createFiles = new ACOS3CreateFiles();
				createFiles.setVisible(true);
				openCreateFiles = true;
			}
			
		}
		
		if(buttonEncrypt == e.getSource())
		{
			
			closeFrames();
			
			if(openEncrypt == false)
			{
				encrypt = new ACOS3Encrypt();
				encrypt.setVisible(true);
				openEncrypt = true;
			}
			else
			{
				encrypt.dispose();
				encrypt = new ACOS3Encrypt();
				encrypt.setVisible(true);
				openEncrypt = true;
			}
			
		}
		
		if(buttonMutualAuthentication == e.getSource())
		{
			
			closeFrames();
			
			if(openMutAuth == false)
			{
				mutAuth = new ACOS3MutualAuthentication();
				mutAuth.setVisible(true);
				openMutAuth = true;
			}
			else
			{
				mutAuth.dispose();
				mutAuth = new ACOS3MutualAuthentication();
				mutAuth.setVisible(true);
				openMutAuth = true;
			}
			
		}
		
		if(buttonReadWrite == e.getSource())
		{
			
			closeFrames();
			
			if(openReadWrite == false)
			{
				readWrite = new ACOS3ReadWrite();
				readWrite.setVisible(true);
				openReadWrite = true;
			}
			else
			{
				readWrite.dispose();
				readWrite = new ACOS3ReadWrite();
				readWrite.setVisible(true);
				openReadWrite = true;
			}
			
		}
		
		if(buttonReadWriteBinary == e.getSource())
		{
			
			closeFrames();
			
			if(openReadWriteBin == false)
			{
				readWriteBin = new ACOS3ReadWriteBinary();
				readWriteBin.setVisible(true);
				openReadWriteBin = true;
			}
			else
			{
				readWriteBin.dispose();
				readWriteBin = new ACOS3ReadWriteBinary();
				readWriteBin.setVisible(true);
				openReadWriteBin = true;
			}
			
		}
		
		if(buttonSecureMessaging == e.getSource())
		{
			
			closeFrames();
			
			if(openSecMsg == false)
			{
				SecMsg = new ACOS3SecureMessaging();
				SecMsg.setVisible(true);
				openSecMsg = true;
			}
			else
			{
				SecMsg.dispose();
				SecMsg = new ACOS3SecureMessaging();
				SecMsg.setVisible(true);
				openSecMsg = true;
			}
		}
	}
	
	public void closeFrames()
	{
		
		if(openAcct==true)
		{
			acct.dispose();
			openAcct = false;
		}
		
		/*if(openConfATR==true)
		{
			confATR.dispose();
			openConfATR = false;
		}*/
		
		if(openCreateFiles==true)
		{
			createFiles.dispose();
			openCreateFiles = false;
		}
		
		if(openEncrypt==true)
		{
			encrypt.dispose();
			openEncrypt = false;
		}
		
		if(openMutAuth==true)
		{
			mutAuth.dispose();
			openMutAuth = false;
		}
		
		if(openReadWrite==true)
		{
			readWrite.dispose();
			openReadWrite = false;
		}
		
		if(openReadWriteBin==true)
		{
			readWriteBin.dispose();
			openReadWriteBin = false;
		}
		
		if(openSecMsg==true)
		{
			SecMsg.dispose();
			openSecMsg = false;
		}
		
	}
	
	public void start()
	{
	
	}
}