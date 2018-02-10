package card.Mifare;/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              mainApplet.java

  Description:       This program enables the user to browse the sample codes for ACR122

  Author:            M.J.E.C. Castillo

  Date:              August 4, 2008

  Revision Trail:   (Date/Author/Description)

======================================================================*/

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class mainApplet extends JApplet implements ActionListener
{
	public mainApplet() {
	}
	
	//Variables
	boolean openMifare = false, openchangeKey = false, openPicc = false;

	//GUI Variables
    private JButton bMifare, bChangeKey;
	
	static MifareCardProgramming mifare;
	static MifareChangeKey changeKey;

	
	public void init() 
   	{
		setSize(230,120);
	    bMifare = new JButton();
	    bMifare.setFont(new Font("Verdana", Font.PLAIN, 10));
	    bMifare.setBounds(10, 44, 210, 23);
	    bChangeKey = new JButton();
	    bChangeKey.setFont(new Font("Verdana", Font.PLAIN, 10));
	    bChangeKey.setBounds(10, 73, 210, 23);

        bMifare.setText("Mifare Card Programming");
        bChangeKey.setText("Mifare Change Key");
        
        JLabel lblMifareCardProgramming = new JLabel("Mifare Sample Codes");
        lblMifareCardProgramming.setHorizontalAlignment(SwingConstants.CENTER);
        lblMifareCardProgramming.setFont(new Font("Verdana", Font.PLAIN, 10));
        lblMifareCardProgramming.setBounds(10, 19, 210, 14);
        
        getContentPane().setLayout(null);
        getContentPane().add(lblMifareCardProgramming);
        getContentPane().add(bMifare);
        getContentPane().add(bChangeKey);
        
        bMifare.addActionListener(this);
        bChangeKey.addActionListener(this);
   		
   	}
	
	public void actionPerformed(ActionEvent e) 
	{
		
		if(bMifare == e.getSource())
		{
			closeFrames();
			
			if(openMifare == false)
			{
				mifare = new MifareCardProgramming();
				mifare.setVisible(true);
				openMifare = true;
			}
			else
			{
			
				mifare.dispose();
				mifare = new MifareCardProgramming();
				mifare.setVisible(true);
				openMifare = true;
			}
			
		}
		
		if(bChangeKey == e.getSource())
		{
			
			closeFrames();
			
			if(openchangeKey == false)
			{
				changeKey = new MifareChangeKey();
				changeKey.setVisible(true);
				openchangeKey = true;
			}
			else
			{
				changeKey.dispose();
				changeKey = new MifareChangeKey();
				changeKey.setVisible(true);
				openchangeKey = true;
			}
		}
		
	}
	
	public void closeFrames()
	{
		
		if(openMifare == true)
		{
			mifare.dispose();
			openMifare = false;
		}
		
		if(openchangeKey == true)
		{
			changeKey.dispose();
			openchangeKey = false;
		}
		
	}
	
	public void start()
	{
	
	}
}