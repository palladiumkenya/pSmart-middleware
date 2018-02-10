package device;/*=====================================================================================================
 * 
 *  Author          : Anthony Mark G. Tayabas
 * 
 *  File            : ACR122UBasicDeviceProgramming.java
 * 
 *  Copyright (C)   : Advanced Card System Ltd.
 * 
 *  Description     : This sample program demonstrates the basic device programming of ACR122U reader.
 * 
 *  Date            : January 13, 2017
 * 
 *  Revision Trailer: [Author] / [Date of modification] / [Details of Modifications done]
 * 
 * 
 * ====================================================================================================*/

import device.Pcsc.Helper;
import device.Pcsc.PcscProvider;
import device.Pcsc.ReaderEvents;

import javax.smartcardio.CardException;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JLabel;

import java.text.ParseException;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.JRadioButton;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;



import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@SuppressWarnings("serial")
public class ACR122UBasicDeviceProgramming extends JFrame implements ReaderEvents.TransmitApduHandler
{
	// **************************** Controls ***********************************************************//
	/*********** PANEL ***********/
	private JPanel _ContentPanel;
	private JPanel _PanelFirmware;
	private JPanel _PanelBuzzerOutput;
	private JPanel _PanelTimeoutParameter;
	private JPanel _PanelLedAndBuzzerControl;
	private JPanel _PanelLedStateControl;
	private JPanel _PanelRedLed;
	private JPanel _PanelRedFinal;
	private JPanel _PanelRedStateMask;
	private JPanel _PanelRedInitBlink;
	private JPanel _PanelRedBlinkMask;
	private JPanel _PanelGreenLed;
	private JPanel _PanelGreenFinal;
	private JPanel _PanelGreenStateMask;
	private JPanel _PanelGreenInitBlink;
	private JPanel _PanelGreenBlinkMask;
	private JPanel _PanelBlinkDuration;
	private JPanel _PanelLinkToBuzzer;

	/*********** BUTTON ***********/
	private JButton _ButtonInitialize;
	private JButton _ButtonConnect;
	private JButton _ButtonGetFirmware;
	private JButton _ButtonGetStatus;
	private JButton _ButtonSetTimeout;
	private JButton _ButtonSetBuzzerOutput;
	private JButton _ButtonClear;
	private JButton _ButtonReset;
	private JButton _ButtonQuit;
	private JButton _ButtonSetLedAndBuzzerControl;
	
	/*********** RADIOBUTTON ***********/
	private JRadioButton _RadioButtonBuzzerOutputOn;
	private JRadioButton _RadioButtonBuzzerOutputOff;
	private JRadioButton _RadioButtonRedFinalOn;
	private JRadioButton _RadioButtonRedFinalOff;
	private JRadioButton _RadioButtonRedStateMaskOff;
	private JRadioButton _RadioButtonRedStateMaskOn;
	private JRadioButton _RadioButtonRedInitBlinkOff;
	private JRadioButton _RadioButtonRedInitBlinkOn;
	private JRadioButton _RadioButtonRedBlinkMaskOff;
	private JRadioButton _RadioButtonRedBlinkMaskOn;
	private JRadioButton _RadioButtonGreenFinalOn;
	private JRadioButton _RadioButtonGreenFinalOff;
	private JRadioButton _RadioButtonGreenStateMaskOn;
	private JRadioButton _RadioButtonGreenStateMaskOff;
	private JRadioButton _RadioButtonGreenInitBlinkOn;
	private JRadioButton _RadioButtonGreenInitBlinkOff;
	private JRadioButton _RadioButtonGreenBlinkMaskOn;
	private JRadioButton _RadioButtonGreenBlinkMaskOff;
	private JRadioButton _RadioButtonLinkToBuzzerOff;
	private JRadioButton _RadioButtonT1Duration;
	private JRadioButton _RadioButtonT2Duration;
	private JRadioButton _RadioButtonT1AndT2Duration;

	/*********** FORMATTEDTEXT ***********/
	private JFormattedTextField _FormattedTextTimeoutParameter;
	private JFormattedTextField _FormattedTextT1Duration;
	private JFormattedTextField _FormattedTextT2Duration;
	private JFormattedTextField _FormattedTextNumberOfRepetition;

	/*********** COMOBOBOX ***********/
	private JComboBox<String> _ComboBoxReaderList;

	/*********** TEXTAREA ***********/
	private JTextArea _TextAreaApduLogs;
	
	/*********** SCROLLAREA ***********/
	JScrollPane _ScrollPanelLogs;
	
	/*********** TEXTFIELD ***********/
	private JTextField _TextFieldFirmware;

	/*********** LABEL ***********/
	private JLabel _LabelSelectReader;
	private JLabel _LabelTimeoutParameter;
	private JLabel _LabelT1InitBlink;
	private JLabel _LabelT2ToggleBlink;
	private JLabel _LabelRepetition;
	private JLabel _LabelT1x100;
	private JLabel _LabelT2x100;
	private JLabel _LabelApduLogs;
	
	// ***********************************************************************************************//
	
	/************** GLOBAL VARIABLES****************/
	private final int BIT_RATE_106 = 0x00;
	private final int BIT_RATE_212 = 0x01;
	private final int BIT_RATE_424 = 0x02;
	private final int MODULATION_TYPE_ISO14443_MIFARE = 0x00;
	private final int MODULATION_TYPE_FELICA = 0x10;
	private final int MODULATION_TYPE_ACTIVE_MODE = 0x01;
	private final int MODULATION_TYPE_INNOVISION_JEWEL_TAG = 0x02;
	private final int VALID_INPUT_LENGTH = 2;
	
	private Acr122u _acr122u;
    private boolean _isConnected = false;
    
    MaskFormatter _maskFormatterTimeoutParameter = new MaskFormatter("HH");
    MaskFormatter _maskFormatterT1Duration = new MaskFormatter("HH");
    MaskFormatter _maskFormatterT2Duration = new MaskFormatter("HH");
    MaskFormatter _maskFormatterNumberOfRepetition = new MaskFormatter("HH");
	
    // ***********************************************************************************************//
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ACR122UBasicDeviceProgramming frame = new ACR122UBasicDeviceProgramming();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws ParseException
	 */
	public ACR122UBasicDeviceProgramming() throws ParseException 
	{
		ButtonGroup buzzerOutput = new ButtonGroup();
		ButtonGroup redFinal = new ButtonGroup();
		ButtonGroup redStateMask = new ButtonGroup();
		ButtonGroup redInitBlink = new ButtonGroup();
		ButtonGroup redBlinkMask = new ButtonGroup();
		ButtonGroup greenFinal = new ButtonGroup();
		ButtonGroup greenStateMask = new ButtonGroup();
		ButtonGroup greenInitBlink = new ButtonGroup();
		ButtonGroup greenBlinkMask = new ButtonGroup();
		ButtonGroup linkToBuzzer = new ButtonGroup();
		
		// **************************** Controls ***********************************************************//
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setFont(new Font("Verdana", Font.PLAIN, 10));
		setTitle("ACR122U Basic Device Programming");
		setBounds(100, 100, 990, 570);
		setResizable(false);
		getContentPane().setFont(new Font("Verdana", Font.PLAIN, 9));
		getContentPane().setLayout(null);
		
		_ContentPanel =   new JPanel();
		_ContentPanel.setBounds(0, 0, 984, 545);
		getContentPane().add(_ContentPanel);
		_ContentPanel.setLayout(null);

		_LabelSelectReader = new JLabel("Select Reader:");
		_LabelSelectReader.setFont(new Font("Verdana", Font.PLAIN, 10));
		_LabelSelectReader.setBounds(10, 11, 94, 14);
		_ContentPanel.add(_LabelSelectReader);

		_ComboBoxReaderList = new JComboBox<String>();
		_ComboBoxReaderList.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ComboBoxReaderList.setBounds(10, 31, 291, 22);
		_ContentPanel.add(_ComboBoxReaderList);

		_ButtonInitialize = new JButton("Initialize");
		_ButtonInitialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initialize();
			}
		});
		_ButtonInitialize.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ButtonInitialize.setBounds(128, 64, 173, 26);
		_ContentPanel.add(_ButtonInitialize);

		_ButtonConnect = new JButton("Connect");
		_ButtonConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connect();
			}
		});
		_ButtonConnect.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ButtonConnect.setBounds(128, 101, 173, 26);
		_ContentPanel.add(_ButtonConnect);

		_PanelFirmware = new JPanel();
		_PanelFirmware.setBorder(new TitledBorder(null, "Firmware", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelFirmware.setBounds(10, 138, 295, 95);
		((TitledBorder) _PanelFirmware.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_ContentPanel.add(_PanelFirmware);
		_PanelFirmware.setLayout(null);

		_TextFieldFirmware = new JTextField();
		_TextFieldFirmware.setFont(new Font("Verdana", Font.PLAIN, 10));
		_TextFieldFirmware.setHorizontalAlignment(SwingConstants.CENTER);
		_TextFieldFirmware.setEditable(false);
		_TextFieldFirmware.setBounds(10, 23, 275, 24);
		_PanelFirmware.add(_TextFieldFirmware);
		_TextFieldFirmware.setColumns(10);

		_ButtonGetFirmware = new JButton("Get Firmware");
		_ButtonGetFirmware.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getFirmware();
			}
		});
		_ButtonGetFirmware.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ButtonGetFirmware.setBounds(112, 58, 173, 26);
		_PanelFirmware.add(_ButtonGetFirmware);

		_ButtonGetStatus = new JButton("Get Status");
		_ButtonGetStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getStatus();
			}
		});
		_ButtonGetStatus.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ButtonGetStatus.setBounds(128, 244, 173, 26);
		_ContentPanel.add(_ButtonGetStatus);

		_PanelTimeoutParameter = new JPanel();
		_PanelTimeoutParameter.setBorder(new TitledBorder(null, "Timeout Parameter", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelTimeoutParameter.setBounds(10, 281, 295, 122);
		((TitledBorder) _PanelTimeoutParameter.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_ContentPanel.add(_PanelTimeoutParameter);
		_PanelTimeoutParameter.setLayout(null);

		_LabelTimeoutParameter = new JLabel("Timeout Parameter");
		_LabelTimeoutParameter.setFont(new Font("Verdana", Font.PLAIN, 10));
		_LabelTimeoutParameter.setBounds(10, 39, 118, 14);
		_PanelTimeoutParameter.add(_LabelTimeoutParameter);

		_FormattedTextTimeoutParameter = new JFormattedTextField(_maskFormatterTimeoutParameter);
		_FormattedTextTimeoutParameter.setFocusLostBehavior(JFormattedTextField.COMMIT);
		_FormattedTextTimeoutParameter.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if (_FormattedTextTimeoutParameter.getText().trim().length() == 0)
					_FormattedTextTimeoutParameter.setText("");
			}
		});
		_FormattedTextTimeoutParameter.setFont(new Font("Verdana", Font.PLAIN, 10));
		_FormattedTextTimeoutParameter.setBounds(126, 36, 47, 20);
		_PanelTimeoutParameter.add(_FormattedTextTimeoutParameter);

		_ButtonSetTimeout = new JButton("Set Timeout Parameter");
		_ButtonSetTimeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTimeoutParameter();
			}
		});
		_ButtonSetTimeout.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ButtonSetTimeout.setBounds(112, 64, 173, 26);
		_PanelTimeoutParameter.add(_ButtonSetTimeout);

		_PanelBuzzerOutput = new JPanel();
		_PanelBuzzerOutput.setBorder(new TitledBorder(null, "Buzzer Output", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelBuzzerOutput.setBounds(10, 414, 295, 77);
		((TitledBorder) _PanelBuzzerOutput.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_ContentPanel.add(_PanelBuzzerOutput);
		_PanelBuzzerOutput.setLayout(null);

		_RadioButtonBuzzerOutputOn = new JRadioButton("On");
		_RadioButtonBuzzerOutputOn.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonBuzzerOutputOn.setBounds(6, 31, 43, 18);
		_PanelBuzzerOutput.add(_RadioButtonBuzzerOutputOn);

		_RadioButtonBuzzerOutputOff = new JRadioButton("Off");
		_RadioButtonBuzzerOutputOff.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonBuzzerOutputOff.setBounds(51, 31, 43, 18);
		_PanelBuzzerOutput.add(_RadioButtonBuzzerOutputOff);

		_ButtonSetBuzzerOutput = new JButton("Set Buzzer Output");
		_ButtonSetBuzzerOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBuzzerOutput();
			}
		});
		_ButtonSetBuzzerOutput.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ButtonSetBuzzerOutput.setBounds(112, 27, 173, 26);
		_PanelBuzzerOutput.add(_ButtonSetBuzzerOutput);

		_PanelLedAndBuzzerControl = new JPanel();
		_PanelLedAndBuzzerControl.setBorder(new TitledBorder(null, "LED and Buzzer Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelLedAndBuzzerControl.setBounds(312, 11, 328, 528);
		((TitledBorder) _PanelLedAndBuzzerControl.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_ContentPanel.add(_PanelLedAndBuzzerControl);
		_PanelLedAndBuzzerControl.setLayout(null);

		_PanelLedStateControl = new JPanel();
		_PanelLedStateControl.setBorder(new TitledBorder(null, "LED State Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelLedStateControl.setBounds(10, 15, 308, 260);
		((TitledBorder) _PanelLedStateControl.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelLedAndBuzzerControl.add(_PanelLedStateControl);
		_PanelLedStateControl.setLayout(null);

		_PanelRedLed = new JPanel();
		_PanelRedLed.setBorder(new TitledBorder(null, "Red LED", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelRedLed.setBounds(10, 13, 288, 119);
		((TitledBorder) _PanelRedLed.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelLedStateControl.add(_PanelRedLed);
		_PanelRedLed.setLayout(null);

		_PanelRedFinal = new JPanel();
		_PanelRedFinal.setBorder(new TitledBorder(null, "Final LED State", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelRedFinal.setBounds(10, 13, 132, 46);
		((TitledBorder) _PanelRedFinal.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelRedLed.add(_PanelRedFinal);
		_PanelRedFinal.setLayout(null);

		_RadioButtonRedFinalOn = new JRadioButton("On");
		_RadioButtonRedFinalOn.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonRedFinalOn.setBounds(20, 18, 43, 18);
		_PanelRedFinal.add(_RadioButtonRedFinalOn);

		_RadioButtonRedFinalOff = new JRadioButton("Off");
		_RadioButtonRedFinalOff.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonRedFinalOff.setBounds(68, 18, 43, 18);
		_PanelRedFinal.add(_RadioButtonRedFinalOff);

		_PanelRedStateMask = new JPanel();
		_PanelRedStateMask.setBorder(new TitledBorder(null, "LED State Mask", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelRedStateMask.setBounds(146, 13, 132, 46);
		((TitledBorder) _PanelRedStateMask.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelRedLed.add(_PanelRedStateMask);
		_PanelRedStateMask.setLayout(null);

		_RadioButtonRedStateMaskOff = new JRadioButton("Off");
		_RadioButtonRedStateMaskOff.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonRedStateMaskOff.setBounds(69, 21, 43, 18);
		_PanelRedStateMask.add(_RadioButtonRedStateMaskOff);

		_RadioButtonRedStateMaskOn = new JRadioButton("On");
		_RadioButtonRedStateMaskOn.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonRedStateMaskOn.setBounds(20, 18, 43, 18);
		_PanelRedStateMask.add(_RadioButtonRedStateMaskOn);

		_PanelRedInitBlink = new JPanel();
		_PanelRedInitBlink.setBorder(new TitledBorder(null, "Initial Blinking State", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelRedInitBlink.setBounds(10, 62, 132, 46);
		((TitledBorder) _PanelRedInitBlink.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelRedLed.add(_PanelRedInitBlink);
		_PanelRedInitBlink.setLayout(null);

		_RadioButtonRedInitBlinkOff = new JRadioButton("Off");
		_RadioButtonRedInitBlinkOff.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonRedInitBlinkOff.setBounds(68, 18, 43, 18);
		_PanelRedInitBlink.add(_RadioButtonRedInitBlinkOff);

		_RadioButtonRedInitBlinkOn = new JRadioButton("On");
		_RadioButtonRedInitBlinkOn.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonRedInitBlinkOn.setBounds(20, 18, 43, 18);
		_PanelRedInitBlink.add(_RadioButtonRedInitBlinkOn);

		_PanelRedBlinkMask = new JPanel();
		_PanelRedBlinkMask.setBorder(new TitledBorder(null, "LED Blinking Mask", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelRedBlinkMask.setBounds(146, 62, 132, 46);
		((TitledBorder) _PanelRedBlinkMask.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelRedLed.add(_PanelRedBlinkMask);
		_PanelRedBlinkMask.setLayout(null);

		_RadioButtonRedBlinkMaskOff = new JRadioButton("Off");
		_RadioButtonRedBlinkMaskOff.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonRedBlinkMaskOff.setBounds(68, 18, 43, 18);
		_PanelRedBlinkMask.add(_RadioButtonRedBlinkMaskOff);

		_RadioButtonRedBlinkMaskOn = new JRadioButton("On");
		_RadioButtonRedBlinkMaskOn.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonRedBlinkMaskOn.setBounds(20, 18, 43, 18);
		_PanelRedBlinkMask.add(_RadioButtonRedBlinkMaskOn);

		_PanelGreenLed = new JPanel();
		_PanelGreenLed.setBorder(new TitledBorder(null, "Green LED", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelGreenLed.setBounds(10, 133, 288, 119);
		((TitledBorder) _PanelGreenLed.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelLedStateControl.add(_PanelGreenLed);
		_PanelGreenLed.setLayout(null);

		_PanelGreenFinal = new JPanel();
		_PanelGreenFinal.setBorder(new TitledBorder(null, "Final LED State", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelGreenFinal.setBounds(10, 13, 132, 46);
		((TitledBorder) _PanelGreenFinal.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelGreenLed.add(_PanelGreenFinal);
		_PanelGreenFinal.setLayout(null);

		_RadioButtonGreenFinalOn = new JRadioButton("On");
		_RadioButtonGreenFinalOn.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonGreenFinalOn.setBounds(20, 18, 43, 18);
		_PanelGreenFinal.add(_RadioButtonGreenFinalOn);

		_RadioButtonGreenFinalOff = new JRadioButton("Off");
		_RadioButtonGreenFinalOff.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonGreenFinalOff.setBounds(68, 18, 43, 18);
		_PanelGreenFinal.add(_RadioButtonGreenFinalOff);

		_PanelGreenStateMask = new JPanel();
		_PanelGreenStateMask.setBorder(new TitledBorder(null, "LED State Mask", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelGreenStateMask.setBounds(146, 13, 132, 46);
		((TitledBorder) _PanelGreenStateMask.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelGreenLed.add(_PanelGreenStateMask);
		_PanelGreenStateMask.setLayout(null);

		_RadioButtonGreenStateMaskOn = new JRadioButton("On");
		_RadioButtonGreenStateMaskOn.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonGreenStateMaskOn.setBounds(20, 18, 43, 18);
		_PanelGreenStateMask.add(_RadioButtonGreenStateMaskOn);

		_RadioButtonGreenStateMaskOff = new JRadioButton("Off");
		_RadioButtonGreenStateMaskOff.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonGreenStateMaskOff.setBounds(68, 18, 43, 18);
		_PanelGreenStateMask.add(_RadioButtonGreenStateMaskOff);

		_PanelGreenInitBlink = new JPanel();
		_PanelGreenInitBlink.setBorder(new TitledBorder(null, "Initial Blinking State", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelGreenInitBlink.setBounds(10, 62, 132, 46);
		((TitledBorder) _PanelGreenInitBlink.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelGreenLed.add(_PanelGreenInitBlink);
		_PanelGreenInitBlink.setLayout(null);

		_RadioButtonGreenInitBlinkOn = new JRadioButton("On");
		_RadioButtonGreenInitBlinkOn.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonGreenInitBlinkOn.setBounds(20, 18, 43, 18);
		_PanelGreenInitBlink.add(_RadioButtonGreenInitBlinkOn);

		_RadioButtonGreenInitBlinkOff = new JRadioButton("Off");
		_RadioButtonGreenInitBlinkOff.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonGreenInitBlinkOff.setBounds(68, 18, 43, 18);
		_PanelGreenInitBlink.add(_RadioButtonGreenInitBlinkOff);

		_PanelGreenBlinkMask = new JPanel();
		_PanelGreenBlinkMask.setBorder(new TitledBorder(null, "LED Blinking Mask", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelGreenBlinkMask.setBounds(146, 62, 132, 46);
		((TitledBorder) _PanelGreenBlinkMask.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelGreenLed.add(_PanelGreenBlinkMask);
		_PanelGreenBlinkMask.setLayout(null);

		_RadioButtonGreenBlinkMaskOn = new JRadioButton("On");
		_RadioButtonGreenBlinkMaskOn.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonGreenBlinkMaskOn.setBounds(20, 18, 43, 18);
		_PanelGreenBlinkMask.add(_RadioButtonGreenBlinkMaskOn);

		_RadioButtonGreenBlinkMaskOff = new JRadioButton("Off");
		_RadioButtonGreenBlinkMaskOff.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonGreenBlinkMaskOff.setBounds(68, 18, 43, 18);
		_PanelGreenBlinkMask.add(_RadioButtonGreenBlinkMaskOff);

		_PanelBlinkDuration = new JPanel();
		_PanelBlinkDuration.setBorder(new TitledBorder(null, "Blinking Duration Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelBlinkDuration.setBounds(10, 285, 308, 206);
		((TitledBorder) _PanelBlinkDuration.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelLedAndBuzzerControl.add(_PanelBlinkDuration);
		_PanelBlinkDuration.setLayout(null);

		_PanelLinkToBuzzer = new JPanel();
		_PanelLinkToBuzzer.setBorder(new TitledBorder(null, "Link to Buzzer", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_PanelLinkToBuzzer.setBounds(10, 128, 288, 67);
		((TitledBorder) _PanelLinkToBuzzer.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		_PanelBlinkDuration.add(_PanelLinkToBuzzer);
		_PanelLinkToBuzzer.setLayout(null);

		_RadioButtonLinkToBuzzerOff = new JRadioButton("Buzzer Off");
		_RadioButtonLinkToBuzzerOff.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonLinkToBuzzerOff.setBounds(16, 13, 109, 23);
		_PanelLinkToBuzzer.add(_RadioButtonLinkToBuzzerOff);

		_RadioButtonT1Duration = new JRadioButton("T1 Duration");
		_RadioButtonT1Duration.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonT1Duration.setBounds(16, 38, 109, 23);
		_PanelLinkToBuzzer.add(_RadioButtonT1Duration);

		_RadioButtonT2Duration = new JRadioButton("T2 Duration");
		_RadioButtonT2Duration.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonT2Duration.setBounds(127, 13, 109, 23);
		_PanelLinkToBuzzer.add(_RadioButtonT2Duration);

		_RadioButtonT1AndT2Duration = new JRadioButton("T1 and T2 Duration");
		_RadioButtonT1AndT2Duration.setFont(new Font("Verdana", Font.PLAIN, 10));
		_RadioButtonT1AndT2Duration.setBounds(127, 38, 142, 23);
		_PanelLinkToBuzzer.add(_RadioButtonT1AndT2Duration);

		_LabelT1InitBlink = new JLabel("<html>T1 Duration Initial Blinking State</html>");
		_LabelT1InitBlink.setFont(new Font("Verdana", Font.PLAIN, 10));
		_LabelT1InitBlink.setBounds(21, 17, 112, 32);
		_PanelBlinkDuration.add(_LabelT1InitBlink);

		_LabelT2ToggleBlink = new JLabel("<html>T2 Duration Toggle Blinking State</html>");
		_LabelT2ToggleBlink.setFont(new Font("Verdana", Font.PLAIN, 10));
		_LabelT2ToggleBlink.setBounds(21, 56, 112, 32);
		_PanelBlinkDuration.add(_LabelT2ToggleBlink);

		_LabelRepetition = new JLabel("Number of Repetition");
		_LabelRepetition.setFont(new Font("Verdana", Font.PLAIN, 10));
		_LabelRepetition.setBounds(21, 99, 126, 14);
		_PanelBlinkDuration.add(_LabelRepetition);

		_FormattedTextT1Duration = new JFormattedTextField(_maskFormatterT1Duration);
		_FormattedTextT1Duration.setFocusLostBehavior(JFormattedTextField.COMMIT);
		_FormattedTextT1Duration.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (_FormattedTextT1Duration.getText().trim().length() == 0)
					_FormattedTextT1Duration.setText("");
			}
		});
		_FormattedTextT1Duration.setFont(new Font("Verdana", Font.PLAIN, 10));
		_FormattedTextT1Duration.setBounds(151, 21, 44, 22);
		_PanelBlinkDuration.add(_FormattedTextT1Duration);

		_FormattedTextT2Duration = new JFormattedTextField(_maskFormatterT2Duration);
		_FormattedTextT2Duration.setFocusLostBehavior(JFormattedTextField.COMMIT);
		_FormattedTextT2Duration.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (_FormattedTextT2Duration.getText().trim().length() == 0)
					_FormattedTextT2Duration.setText("");
			}
		});
		_FormattedTextT2Duration.setFont(new Font("Verdana", Font.PLAIN, 10));
		_FormattedTextT2Duration.setBounds(151, 58, 44, 22);
		_PanelBlinkDuration.add(_FormattedTextT2Duration);

		_FormattedTextNumberOfRepetition = new JFormattedTextField(_maskFormatterNumberOfRepetition);
		_FormattedTextNumberOfRepetition.setFocusLostBehavior(JFormattedTextField.COMMIT);
		_FormattedTextNumberOfRepetition.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (_FormattedTextNumberOfRepetition.getText().trim().length() == 0)
					_FormattedTextNumberOfRepetition.setText("");
			}
		});
		_FormattedTextNumberOfRepetition.setFont(new Font("Verdana", Font.PLAIN, 10));
		_FormattedTextNumberOfRepetition.setBounds(151, 95, 44, 22);
		_PanelBlinkDuration.add(_FormattedTextNumberOfRepetition);

		_LabelT1x100 = new JLabel("x100 ms");
		_LabelT1x100.setFont(new Font("Verdana", Font.PLAIN, 10));
		_LabelT1x100.setBounds(209, 25, 55, 14);
		_PanelBlinkDuration.add(_LabelT1x100);

		_LabelT2x100 = new JLabel("x100 ms");
		_LabelT2x100.setFont(new Font("Verdana", Font.PLAIN, 10));
		_LabelT2x100.setBounds(209, 62, 55, 14);
		_PanelBlinkDuration.add(_LabelT2x100);

		_ButtonSetLedAndBuzzerControl = new JButton("Set LED and Buzzer Control");
		_ButtonSetLedAndBuzzerControl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getLedAndBuzzerControlParameter();
			}
		});
		_ButtonSetLedAndBuzzerControl.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ButtonSetLedAndBuzzerControl.setBounds(120, 494, 198, 26);
		_PanelLedAndBuzzerControl.add(_ButtonSetLedAndBuzzerControl);

		_LabelApduLogs = new JLabel("APDU Logs:");
		_LabelApduLogs.setFont(new Font("Verdana", Font.PLAIN, 10));
		_LabelApduLogs.setBounds(650, 11, 70, 14);
		_ContentPanel.add(_LabelApduLogs);
		
		_ScrollPanelLogs = new JScrollPane();
		_ScrollPanelLogs.setBounds(650, 31, 324, 472);
		_ContentPanel.add(_ScrollPanelLogs);
		
		_TextAreaApduLogs = new JTextArea();
		_TextAreaApduLogs.setText("");
		_TextAreaApduLogs.setFont(new Font("Verdana", Font.PLAIN, 10));
		_TextAreaApduLogs.setEditable(false);
		_ScrollPanelLogs.setViewportView(_TextAreaApduLogs);

		_ButtonClear = new JButton("Clear");
		_ButtonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_TextAreaApduLogs.setText("");
			}
		});
		_ButtonClear.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ButtonClear.setBounds(650, 510, 104, 26);
		_ContentPanel.add(_ButtonClear);

		_ButtonReset = new JButton("Reset");
		_ButtonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					if (_isConnected)
						_acr122u.disconnect();
					
					resetFields();
				}
				catch (CardException ex)
				{
					addTitleToLog(PcscProvider.GetScardErrMsg(ex));
					showErrorMessage(PcscProvider.GetScardErrMsg(ex));
				}
				catch (Exception ex)
			    {
					addMessageToLog(ex.getMessage());
					showErrorMessage(ex.getMessage());	
			    }
			}
		});
		_ButtonReset.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ButtonReset.setBounds(760, 510, 104, 26);
		_ContentPanel.add(_ButtonReset);

		_ButtonQuit = new JButton("Quit");
		_ButtonQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					if (_isConnected)
						_acr122u.disconnect();
					
					dispose();
				}
				catch (CardException ex)
				{
					addTitleToLog(PcscProvider.GetScardErrMsg(ex));
					showErrorMessage(PcscProvider.GetScardErrMsg(ex));
				}
				catch (Exception ex)
			    {
					addMessageToLog(ex.getMessage());
					showErrorMessage(ex.getMessage());	
			    }
			}
		});
		_ButtonQuit.setFont(new Font("Verdana", Font.PLAIN, 10));
		_ButtonQuit.setBounds(870, 510, 104, 26);
		_ContentPanel.add(_ButtonQuit);
		
		// ***********************************************************************************************//
		
		buzzerOutput.add(_RadioButtonBuzzerOutputOn);
		buzzerOutput.add(_RadioButtonBuzzerOutputOff);
		
		redFinal.add(_RadioButtonRedFinalOn);
		redFinal.add(_RadioButtonRedFinalOff);
		
		redStateMask.add(_RadioButtonRedStateMaskOn);
		redStateMask.add(_RadioButtonRedStateMaskOff);
		
		redInitBlink.add(_RadioButtonRedInitBlinkOn);
		redInitBlink.add(_RadioButtonRedInitBlinkOff);
		
		redBlinkMask.add(_RadioButtonRedBlinkMaskOn);
		redBlinkMask.add(_RadioButtonRedBlinkMaskOff);
		
		greenFinal.add(_RadioButtonGreenFinalOn);
		greenFinal.add(_RadioButtonGreenFinalOff);
		
		greenStateMask.add(_RadioButtonGreenStateMaskOn);
		greenStateMask.add(_RadioButtonGreenStateMaskOff);
		
		greenInitBlink.add(_RadioButtonGreenInitBlinkOn);
		greenInitBlink.add(_RadioButtonGreenInitBlinkOff);
		
		greenBlinkMask.add(_RadioButtonGreenBlinkMaskOn);
		greenBlinkMask.add(_RadioButtonGreenBlinkMaskOff);
		
		linkToBuzzer.add(_RadioButtonLinkToBuzzerOff);
		linkToBuzzer.add(_RadioButtonT1Duration);
		linkToBuzzer.add(_RadioButtonT2Duration);
		linkToBuzzer.add(_RadioButtonT1AndT2Duration);
		
		// Instantiate class
		_acr122u = new Acr122u();
        
		// Instantiate an event handler object 
		_acr122u.setEventHandler(new ReaderEvents());
		
		// Register the event handler implementation of this class
		_acr122u.getEventHandler().addEventListener(this);	
		
		resetFields();
	}
	
	private void initialize()
	{
		String[] readerList;
		int index;
		
		try
		{	
			if (_isConnected)
			{
				_acr122u.disconnect();
				enableControls(false);
				
				_isConnected = false;
			}
			
			_ComboBoxReaderList.removeAllItems();
			
			readerList = _acr122u.listTerminals();
			
			for(index = 0; index < readerList.length; index++)
			{
				if(!readerList.equals(""))
					_ComboBoxReaderList.addItem(readerList[index]);
				else
					break;
			}
			
			if(_ComboBoxReaderList.getItemCount() > 0)
				_ComboBoxReaderList.setSelectedIndex(0);
			
			addMessageToLog("\r\nInitialize success");
			_ButtonConnect.setEnabled(true);
		}
		catch (CardException ex)
		{
			addTitleToLog(PcscProvider.GetScardErrMsg(ex));
			showErrorMessage(PcscProvider.GetScardErrMsg(ex));
			initializeFailed();
		}
		catch (Exception ex)
	    {
			addMessageToLog(ex.getMessage());
			showErrorMessage(ex.getMessage());
			initializeFailed();
	    }
	}
	
	private void connect()
	{
		if (_ComboBoxReaderList.getSelectedIndex() < 0)
		{			
			showErrorMessage("Please select smartcard reader.");
			return; 			
		}
    	
    	try
		{	
			if(_acr122u.isConnectionActive())
				_acr122u.disconnect();
			
    		// Connect directly to the smart card reader
			_acr122u.connectDirect(_ComboBoxReaderList.getSelectedIndex(), true);
			
    		addMessageToLog("\r\nSuccessfully connected to " + _ComboBoxReaderList.getSelectedItem());
    		
			_isConnected = true;
			
			_ButtonConnect.setEnabled(false);
    		enableControls(true);
		}
    	catch (CardException ex)
		{
			addTitleToLog(PcscProvider.GetScardErrMsg(ex));
			showErrorMessage(PcscProvider.GetScardErrMsg(ex));
		}
		catch (Exception ex)
	    {
			addMessageToLog(ex.getMessage());
			showErrorMessage(ex.getMessage());	
	    }
	}
	
	private void getFirmware()
	{
		byte[] firmwareVersion;
		
		try
		{	
			if(_acr122u == null)
			{
				showErrorMessage("Please connect ACR122U reader");
                return;
            }
			
			addTitleToLog("Get Firmware Version");
			firmwareVersion = _acr122u.getFirmwareVersion();
			
			_TextFieldFirmware.setText(Helper.byteArrayToString(firmwareVersion));
		}
		catch (CardException ex)
		{
			addTitleToLog(PcscProvider.GetScardErrMsg(ex));
			showErrorMessage(PcscProvider.GetScardErrMsg(ex));
		}
		catch (Exception ex)
	    {
			addMessageToLog(ex.getMessage());
			showErrorMessage(ex.getMessage());	
	    }
	}
	
	private void getStatus()
	{
		byte[] status;
        String stringStatus = "";
        String bitRateInReception = "";
        String bitRateInTranmission = "";
        String modulationType = "";
        
        try
        {
        	addTitleToLog("Get Status");
        	status = _acr122u.getStatus();
        	
        	if (status.length > 8)
        	{
        		if (status[6] == BIT_RATE_106)
                    bitRateInReception = "106 Kbps";
                else if (status[6] == BIT_RATE_212)
                    bitRateInReception = "212 Kbps";
                else if (status[6] == BIT_RATE_424)
                    bitRateInReception = "424 Kbps";

                if (status[7] == BIT_RATE_106)
                    bitRateInTranmission = "106 Kbps";
                else if (status[7] == BIT_RATE_212)
                    bitRateInTranmission = "212 Kbps";
                else if (status[6] == BIT_RATE_424)
                    bitRateInTranmission = "424 Kbps";

                if (status[8] == MODULATION_TYPE_ISO14443_MIFARE)
                    modulationType = "ISO 14443 or Mifare";
                else if (status[8] == MODULATION_TYPE_FELICA)
                    modulationType = "Felica";
                else if (status[8] == MODULATION_TYPE_ACTIVE_MODE)
                    modulationType = "Active mode";
                else if (status[8] == MODULATION_TYPE_INNOVISION_JEWEL_TAG)
                    modulationType = "Innovation Jewel Tag";
                
                stringStatus = "\r\nError Code: " + String.format("%02x", status[2]) + "\r\n" + "Field: " + String.format("%02x", status[3]) + "\r\n" +
                               "No. of Targets: " + String.format("%02x", status[4]) + "\r\n" + "Logical No. " + String.format("%02x", status[5]) + "\r\n" +
                               "Bit Rate in Reception: " + bitRateInReception + "\r\n" + "Bit Rate in Transmission: " + bitRateInTranmission + "\r\n" +
                               "Modulation Type: " + modulationType;

                addMessageToLog(stringStatus);
        	}
        	else
        	{
        		addMessageToLog("No tag is in the field");
        	}
        }
        catch (CardException ex)
		{
			addTitleToLog(PcscProvider.GetScardErrMsg(ex));
			showErrorMessage(PcscProvider.GetScardErrMsg(ex));
		}
		catch (Exception ex)
	    {
			addMessageToLog(ex.getMessage());
			showErrorMessage(ex.getMessage());	
	    }
	}
	
	private void setTimeoutParameter()
	{
		String tempTimeoutParameter;
		byte timeoutParameter;
		
		try
		{
			tempTimeoutParameter = _FormattedTextTimeoutParameter.getText();
			
			if (tempTimeoutParameter.trim().length() == 0)
			{
				showErrorMessage("Please key-in hex value for Timeout Parameter.");
				_FormattedTextTimeoutParameter.requestFocus();
                return;
			}
			
			if (tempTimeoutParameter.trim().length() != VALID_INPUT_LENGTH)
			{
				showErrorMessage("Invalid input length. Length must be 2.");
				_FormattedTextTimeoutParameter.requestFocus();
                return;
			}
			
			if (!(tempTimeoutParameter.matches("[0-9A-Fa-f]+")))
			{
				showErrorMessage("Please key-in hex value for Timeout Parameter.");
				_FormattedTextTimeoutParameter.requestFocus();
                return;
			}
			
			addTitleToLog("Set Timeout Parameter");
		
			timeoutParameter = (byte) (Integer.parseInt(tempTimeoutParameter, 16) & 0xff);
			
			_acr122u.setTimeoutParameter(timeoutParameter);
		}
		catch (CardException ex)
		{
			addTitleToLog(PcscProvider.GetScardErrMsg(ex));
			showErrorMessage(PcscProvider.GetScardErrMsg(ex));
		}
		catch (Exception ex)
	    {
			addMessageToLog(ex.getMessage());
			showErrorMessage(ex.getMessage());	
	    }
	}
	
	private void setBuzzerOutput()
	{
		Acr122u.BUZZER_OUTPUT buzzerOutput = Acr122u.BUZZER_OUTPUT.OFF;
		
		try
		{
			if (_RadioButtonBuzzerOutputOn.isSelected())
				buzzerOutput = Acr122u.BUZZER_OUTPUT.ON;
			
			addTitleToLog("Set Buzzer Output");
			
			_acr122u.setBuzzerOutput(buzzerOutput);
		}
		catch (CardException ex)
		{
			addTitleToLog(PcscProvider.GetScardErrMsg(ex));
			showErrorMessage(PcscProvider.GetScardErrMsg(ex));
		}
		catch (Exception ex)
	    {
			addMessageToLog(ex.getMessage());
			showErrorMessage(ex.getMessage());	
	    }
	}
	
	private void setLedAndBuzzerControl(String tempT1Duration, String tempT2Duration, String tempNumberOfRepetition)
	{
		Acr122u.LedAndBuzzerControl ledAndBuzzerControl;
		Acr122u.LINK_TO_BUZZER linkToBuzzer = Acr122u.LINK_TO_BUZZER.OFF;
		
		byte t1Duration;
		byte t2Duration;
		byte numberOfRepetition;
		byte tempLinkToBuzzer = 0x00;
		
		try
		{
			t1Duration = (byte) (Integer.parseInt(tempT1Duration, 16) & 0xff);
			t2Duration = (byte) (Integer.parseInt(tempT2Duration, 16) & 0xff);
			numberOfRepetition = (byte) (Integer.parseInt(tempNumberOfRepetition, 16) & 0xff);
			
			ledAndBuzzerControl = new Acr122u.LedAndBuzzerControl();
			
			//Red LED
			if (_RadioButtonRedFinalOn.isSelected())
				ledAndBuzzerControl.setRedLedState(Acr122u.LED_STATE.ON);
			
			if (_RadioButtonRedStateMaskOn.isSelected())
				ledAndBuzzerControl.setRedLedStateMask(Acr122u.LED_STATE_MASK.ON);
			
			if (_RadioButtonRedInitBlinkOn.isSelected())
				ledAndBuzzerControl.setRedLedBlinkingState(Acr122u.INITIAL_LED_BLINKING_STATE.ON);
			
			if (_RadioButtonRedBlinkMaskOn.isSelected())
				ledAndBuzzerControl.setRedLedBlinkingMask(Acr122u.LED_BLINKING_MASK.ON);
			
			//Green LED
			if (_RadioButtonGreenFinalOn.isSelected())
				ledAndBuzzerControl.setGreenLedState(Acr122u.LED_STATE.ON);
			
			if (_RadioButtonGreenStateMaskOn.isSelected())
				ledAndBuzzerControl.setGreenLedStateMask(Acr122u.LED_STATE_MASK.ON);
			
			if (_RadioButtonGreenInitBlinkOn.isSelected())
				ledAndBuzzerControl.setGreenLedBlinkingState(Acr122u.INITIAL_LED_BLINKING_STATE.ON);
			
			if (_RadioButtonGreenBlinkMaskOn.isSelected())
				ledAndBuzzerControl.setGreenLedBlinkingMask(Acr122u.LED_BLINKING_MASK.ON);
			
			//Link to Buzzer
			if (_RadioButtonT1Duration.isSelected())
				linkToBuzzer = Acr122u.LINK_TO_BUZZER.T1_DURATION;
			else if (_RadioButtonT2Duration.isSelected())
				linkToBuzzer = Acr122u.LINK_TO_BUZZER.T2_DURATION;
			else if (_RadioButtonT1AndT2Duration.isSelected())
				linkToBuzzer = Acr122u.LINK_TO_BUZZER.T1_AND_T2_DURATION;
			
			if (linkToBuzzer == Acr122u.LINK_TO_BUZZER.T1_DURATION)
				tempLinkToBuzzer = 0x01;
			else if (linkToBuzzer == Acr122u.LINK_TO_BUZZER.T2_DURATION)
				tempLinkToBuzzer = 0x02;
			else if (linkToBuzzer == Acr122u.LINK_TO_BUZZER.T1_AND_T2_DURATION)
				tempLinkToBuzzer = 0x03;
			
			ledAndBuzzerControl.setBlinkingDuration(new byte[] {t1Duration, t2Duration, numberOfRepetition, tempLinkToBuzzer});
			
			addTitleToLog("Set LED and Buzzer Control");
			
			_acr122u.setLedStatus(ledAndBuzzerControl);
			
		}
		catch (CardException ex)
		{
			addTitleToLog(PcscProvider.GetScardErrMsg(ex));
			showErrorMessage(PcscProvider.GetScardErrMsg(ex));
		}
		catch (Exception ex)
	    {
			addMessageToLog(ex.getMessage());
			showErrorMessage(ex.getMessage());	
	    }
	}
	
	private void getLedAndBuzzerControlParameter()
	{
		String tempT1Duration = _FormattedTextT1Duration.getText();
		String tempT2Duration = _FormattedTextT2Duration.getText();
		String tempNumberOfRepetition = _FormattedTextNumberOfRepetition.getText();
		
		if (tempT1Duration.trim().equals(""))
		{
			showErrorMessage("Please key-in hex value for T1 Duration Initial Blinking State.");
			_FormattedTextT1Duration.requestFocus();
            return;
		}
		
		if (tempT1Duration.trim().length() != VALID_INPUT_LENGTH)
		{
			showErrorMessage("Invalid input length. Length must be 2.");
			_FormattedTextT1Duration.requestFocus();
            return;
		}
		
		if (!(tempT1Duration.matches("[0-9A-Fa-f]+")))
		{
			showErrorMessage("Please key-in hex value for T1 Duration Initial Blinking State.");
			_FormattedTextT1Duration.requestFocus();
            return;
		}
		
		if (tempT2Duration.trim().equals(""))
		{
			showErrorMessage("Please key-in hex value for T2 Duration Toggle Blinking State.");
			_FormattedTextT2Duration.requestFocus();
            return;
		}
		
		if (tempT2Duration.trim().length() != VALID_INPUT_LENGTH)
		{
			showErrorMessage("Invalid input length. Length must be 2.");
			_FormattedTextT2Duration.requestFocus();
            return;
		}
		
		if (!(tempT2Duration.matches("[0-9A-Fa-f]+")))
		{
			showErrorMessage("Please key-in hex value for T2 Duration Toggle Blinking State.");
			_FormattedTextT2Duration.requestFocus();
            return;
		}
		
		if (tempNumberOfRepetition.trim().equals(""))
		{
			showErrorMessage("Please key-in hex value for Number of Repetition.");
			_FormattedTextNumberOfRepetition.requestFocus();
            return;
		}
		
		if (tempNumberOfRepetition.trim().length() != VALID_INPUT_LENGTH)
		{
			showErrorMessage("Invalid input length. Length must be 2.");
			_FormattedTextNumberOfRepetition.requestFocus();
            return;
		}
		
		if (!(tempNumberOfRepetition.matches("[0-9A-Fa-f]+")))
		{
			showErrorMessage("Please key-in hex value for Number of Repetition.");
			_FormattedTextNumberOfRepetition.requestFocus();
            return;
		}
		
		setLedAndBuzzerControl(tempT1Duration, tempT2Duration, tempNumberOfRepetition);
	}
	
	void resetFields()
	{
		enableControls(false);
		
		_TextFieldFirmware.setText("");
		_FormattedTextNumberOfRepetition.setText("");
		_FormattedTextT1Duration.setText("");
		_FormattedTextT2Duration.setText("");
		_FormattedTextTimeoutParameter.setText("");
		
		_RadioButtonBuzzerOutputOff.setSelected(false);
		_RadioButtonBuzzerOutputOn.setSelected(true);
		
		_RadioButtonGreenBlinkMaskOff.setSelected(false);
		_RadioButtonGreenBlinkMaskOn.setSelected(true);
		_RadioButtonGreenFinalOff.setSelected(false);
		_RadioButtonGreenFinalOn.setSelected(true);
		_RadioButtonGreenInitBlinkOff.setSelected(false);
		_RadioButtonGreenInitBlinkOn.setSelected(true);
		_RadioButtonGreenStateMaskOff.setSelected(false);
		_RadioButtonGreenStateMaskOn.setSelected(true);
		
		_RadioButtonRedBlinkMaskOff.setSelected(false);
		_RadioButtonRedBlinkMaskOn.setSelected(true);
		_RadioButtonRedFinalOff.setSelected(false);
		_RadioButtonRedFinalOn.setSelected(true);
		_RadioButtonRedInitBlinkOff.setSelected(false);
		_RadioButtonRedInitBlinkOn.setSelected(true);
		_RadioButtonRedStateMaskOff.setSelected(false);
		_RadioButtonRedStateMaskOn.setSelected(true);
		
		_RadioButtonT1AndT2Duration.setSelected(false);
		_RadioButtonT1Duration.setSelected(false);
		_RadioButtonT2Duration.setSelected(false);
		_RadioButtonLinkToBuzzerOff.setSelected(true);
		
		_ComboBoxReaderList.removeAllItems();
		
		_TextAreaApduLogs.setText("");
		
		addMessageToLog("Program Ready");
		
		_ButtonConnect.setEnabled(false);
		
		_isConnected = false;
	}
	
	void enableControls(boolean isEnable)
	{
		_PanelFirmware.setEnabled(isEnable);
		_PanelTimeoutParameter.setEnabled(isEnable);
		_PanelBuzzerOutput.setEnabled(isEnable);
		_PanelLedAndBuzzerControl.setEnabled(isEnable);
		_PanelLedStateControl.setEnabled(isEnable);
		_ButtonGetStatus.setEnabled(isEnable);
		
		enableRedPanel(isEnable);
		enableGreenPanel(isEnable);
		
		for (Component controls : _PanelFirmware.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelTimeoutParameter.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelBuzzerOutput.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelLedAndBuzzerControl.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelLedStateControl.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelBlinkDuration.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelLinkToBuzzer.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
	}
	
	private void enableRedPanel(boolean isEnable)
	{
		_PanelRedLed.setEnabled(isEnable);
		
		for (Component controls : _PanelRedLed.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelRedFinal.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelRedStateMask.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelRedInitBlink.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelRedStateMask.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelRedInitBlink.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelRedBlinkMask.getComponents())
		{
			controls.setEnabled(isEnable);
		}
	}
	
	private void enableGreenPanel(boolean isEnable)
	{
		_PanelGreenLed.setEnabled(isEnable);
		
		for (Component controls : _PanelGreenLed.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelGreenFinal.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelGreenStateMask.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelGreenInitBlink.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelGreenStateMask.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelGreenInitBlink.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : _PanelGreenBlinkMask.getComponents())
		{
			controls.setEnabled(isEnable);
		}
	}
	
	private void initializeFailed()
	{
		_ButtonConnect.setEnabled(false);
		_ComboBoxReaderList.removeAllItems();
	}
	
	void addTitleToLog(String title)
	{
		_TextAreaApduLogs.setSelectedTextColor(Color.black);
		_TextAreaApduLogs.append("\r\n" + title + "\r\n");
	}
	
	void addMessageToLog(String message)
	{
		_TextAreaApduLogs.append(message + "\r\n");
	}
	
	void showInformationMessage(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	void showWarningMessage(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}
	
	void showErrorMessage(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void onSendCommand(ReaderEvents.TransmitApduEventArg event) {
		addMessageToLog("<< " + event.getAsString(true));
	}

	@Override
	public void onReceiveCommand(ReaderEvents.TransmitApduEventArg event) {
		addMessageToLog(">> " + event.getAsString(true));
	}
}


