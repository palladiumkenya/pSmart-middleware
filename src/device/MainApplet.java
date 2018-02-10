package device;
/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              MainApplet.java

  Description:       This program enables the user to browse the sample codes for ACR122UBasicDeviceProgramming

  Author:            Anthony Mark G. Tayabas

  Date:              January 30, 2017

  Revision Trail:   (Date/Author/Description)

======================================================================*/

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class MainApplet extends JApplet implements ActionListener
{
	public MainApplet() {}
	
	// Variables
	boolean _isMifareNdefOpen = false;

	//GUI Variables
    private JButton _ButtonMifareNdef;
	
	static ACR122UBasicDeviceProgramming _acr122uBasicDeviceProgramming;

	public void init() 
   	{
		setSize(275,120);
		_ButtonMifareNdef = new JButton();
		_ButtonMifareNdef.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ButtonMifareNdef.setBounds(10, 44, 250, 23);

		_ButtonMifareNdef.setText("ACR122U Basic Device Programming");
        
        JLabel LabelMifareNdefSampleCode = new JLabel("ACR122U Basic Device Programming");
        LabelMifareNdefSampleCode.setHorizontalAlignment(SwingConstants.CENTER);
        LabelMifareNdefSampleCode.setFont(new Font("Verdana", Font.PLAIN, 10));
        LabelMifareNdefSampleCode.setBounds(10, 19, 250, 14);
        
        getContentPane().setLayout(null);
        getContentPane().add(LabelMifareNdefSampleCode);
        getContentPane().add(_ButtonMifareNdef);
        
        _ButtonMifareNdef.addActionListener(this);
   		
   	}
	
	public void actionPerformed(ActionEvent e) 
	{		
		if(_ButtonMifareNdef == e.getSource())
		{
			closeFrames();
			
			if(_isMifareNdefOpen == false)
			{
				try {
					_acr122uBasicDeviceProgramming = new ACR122UBasicDeviceProgramming();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				_acr122uBasicDeviceProgramming.setVisible(true);
				_isMifareNdefOpen = true;
			}
			else
			{			
				_acr122uBasicDeviceProgramming.dispose();
				
				try {
					_acr122uBasicDeviceProgramming = new ACR122UBasicDeviceProgramming();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				_acr122uBasicDeviceProgramming.setVisible(true);
				_isMifareNdefOpen = true;
			}			
		}
	}
	
	public void closeFrames()
	{		
		if(_isMifareNdefOpen == true)
		{
			_acr122uBasicDeviceProgramming.dispose();
			_isMifareNdefOpen = false;
		}		
	}
	
	public void start()
	{
	
	}
}