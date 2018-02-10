package card.OtherCards;/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              mainApplet.java

  Description:       This program enables the user to browse the sample codes for ACR122

  Author:            M.J.E.C. Castillo

  Date:              August 4, 2008

  Revision Trail:   (Date/Author/Description)

======================================================================*/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class mainApplet extends JApplet implements ActionListener
{
	public mainApplet() {
	}
	
	//Variables
	boolean openOtherPicc = false;

	//GUI Variables
    private JButton buttonOtherPicc;
	
	static FrameOtherPicc otherPicc;

	
	public void init() 
   	{
		setSize(220,90);
	    buttonOtherPicc = new JButton();
	    buttonOtherPicc.setFont(new Font("Verdana", Font.PLAIN, 10));
	    buttonOtherPicc.setBounds(22, 51, 176, 23);

        buttonOtherPicc.setText("Other PICC");
        
        JLabel lblMifareCardProgramming = new JLabel("Other PICC Card Programming");
        lblMifareCardProgramming.setHorizontalAlignment(SwingConstants.CENTER);
        lblMifareCardProgramming.setFont(new Font("Verdana", Font.PLAIN, 10));
        lblMifareCardProgramming.setBounds(22, 26, 176, 14);
        
        getContentPane().setLayout(null);
        getContentPane().add(lblMifareCardProgramming);
        getContentPane().add(buttonOtherPicc);
        
        buttonOtherPicc.addActionListener(this);
   		
   	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(buttonOtherPicc == e.getSource())
		{
			closeFrames();
			
			if(openOtherPicc == false)
			{
				otherPicc = new FrameOtherPicc();
				otherPicc.setVisible(true);
				openOtherPicc = true;
			}
			else
			{			
				otherPicc.dispose();
				otherPicc = new FrameOtherPicc();
				otherPicc.setVisible(true);
				openOtherPicc = true;
			}
		}
	}
	
	public void closeFrames()
	{		
		if(openOtherPicc == true)
		{
			otherPicc.dispose();
			openOtherPicc = false;
		}
	}
	
	public void start()
	{
	
	}
}