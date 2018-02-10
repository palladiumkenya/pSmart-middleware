package card.OtherCards;

import java.awt.Color;
import java.awt.EventQueue;

import javax.smartcardio.CardException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;


@SuppressWarnings("serial")
public class FrameOtherPicc extends JFrame implements ReaderEvents.TransmitApduHandler, ActionListener, KeyListener{
	private JPanel ContentPanel;
	private JTextField TextFieldCla;
	private JTextField TextFieldIns;
	private JTextField TextFieldP1;
	private JTextField TextFieldP2;
	private JTextField TextFieldLc;
	private JTextField TextFieldLe;
	private JButton ButtonInitialize;
	private JButton ButtonConnect;
	private JButton ButtonGetData;
	private JButton ButtonSendCardCommand;
	private JButton ButtonClear;
	private JButton ButtonReset;
	private JButton ButtonQuit;
	private JCheckBox CheckBoxIsoACard;
	private JTextArea TextAreaApduLogs;
	private JComboBox<String> ComboBoxReaderNames;
	
	private PiccClass _piccClass;
	private PcscReader _pcscReader;
	private JTextArea TextAreaData;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameOtherPicc frame = new FrameOtherPicc();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrameOtherPicc() {
		setTitle("Other PICC Card");
		setFont(new Font("Verdana", Font.PLAIN, 10));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 676, 419);
		ContentPanel = new JPanel();
		ContentPanel.setFont(new Font("Verdana", Font.PLAIN, 10));
		ContentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(ContentPanel);
		ContentPanel.setLayout(null);
		
		JLabel LabelSelectReader = new JLabel("Select Reader");
		LabelSelectReader.setFont(new Font("Verdana", Font.PLAIN, 10));
		LabelSelectReader.setBounds(10, 11, 95, 14);
		ContentPanel.add(LabelSelectReader);
		
		ComboBoxReaderNames = new JComboBox<String>();
		ComboBoxReaderNames.setFont(new Font("Verdana", Font.PLAIN, 10));
		ComboBoxReaderNames.setBounds(95, 8, 225, 20);
		ContentPanel.add(ComboBoxReaderNames);
		
		ButtonInitialize = new JButton("Initialize");
		ButtonInitialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				initialize();
			}
		});
		ButtonInitialize.setFont(new Font("Verdana", Font.PLAIN, 10));
		ButtonInitialize.setBounds(165, 39, 155, 23);
		ContentPanel.add(ButtonInitialize);
		
		ButtonConnect = new JButton("Connect");
		ButtonConnect.setEnabled(false);
		ButtonConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connect();
			}
		});
		ButtonConnect.setFont(new Font("Verdana", Font.PLAIN, 10));
		ButtonConnect.setBounds(165, 65, 155, 23);
		ContentPanel.add(ButtonConnect);
		
		JPanel GroupBoxGetDataFunction = new JPanel();
		GroupBoxGetDataFunction.setBorder(new TitledBorder(null, "Get Data Function", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupBoxGetDataFunction.setBounds(10, 99, 323, 58);
		((TitledBorder) GroupBoxGetDataFunction.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		ContentPanel.add(GroupBoxGetDataFunction);
		GroupBoxGetDataFunction.setLayout(null);
		
		CheckBoxIsoACard = new JCheckBox("ISO 14443-A Card");
		CheckBoxIsoACard.setFont(new Font("Verdana", Font.PLAIN, 10));
		CheckBoxIsoACard.setBounds(6, 20, 145, 23);
		GroupBoxGetDataFunction.add(CheckBoxIsoACard);
		
		ButtonGetData = new JButton("Get Data");
		ButtonGetData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getData();
			}
		});
		ButtonGetData.setFont(new Font("Verdana", Font.PLAIN, 10));
		ButtonGetData.setBounds(157, 20, 155, 23);
		GroupBoxGetDataFunction.add(ButtonGetData);
		
		JPanel GroupBoxSendCardCommand = new JPanel();
		GroupBoxSendCardCommand.setBorder(new TitledBorder(null, "Send Card Command", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupBoxSendCardCommand.setBounds(10, 168, 323, 213);
		((TitledBorder) GroupBoxSendCardCommand.getBorder()).setTitleFont(new Font("Verdana", Font.PLAIN, 10));
		ContentPanel.add(GroupBoxSendCardCommand);
		GroupBoxSendCardCommand.setLayout(null);
		
		ButtonSendCardCommand = new JButton("Send Card Command");
		ButtonSendCardCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendCommandData();
			}
		});
		ButtonSendCardCommand.setBounds(158, 180, 155, 23);
		ButtonSendCardCommand.setFont(new Font("Verdana", Font.PLAIN, 10));
		GroupBoxSendCardCommand.add(ButtonSendCardCommand);
		
		JLabel LabelCla = new JLabel("CLA");
		LabelCla.setFont(new Font("Verdana", Font.PLAIN, 10));
		LabelCla.setBounds(20, 25, 29, 14);
		GroupBoxSendCardCommand.add(LabelCla);
		
		JLabel LabelIns = new JLabel("INS");
		LabelIns.setFont(new Font("Verdana", Font.PLAIN, 10));
		LabelIns.setBounds(67, 25, 29, 14);
		GroupBoxSendCardCommand.add(LabelIns);
		
		JLabel LabelP1 = new JLabel("P1");
		LabelP1.setFont(new Font("Verdana", Font.PLAIN, 10));
		LabelP1.setBounds(115, 25, 29, 14);
		GroupBoxSendCardCommand.add(LabelP1);
		
		JLabel LabelP2 = new JLabel("P2");
		LabelP2.setFont(new Font("Verdana", Font.PLAIN, 10));
		LabelP2.setBounds(158, 25, 29, 14);
		GroupBoxSendCardCommand.add(LabelP2);
		
		JLabel LabelLc = new JLabel("Lc");
		LabelLc.setFont(new Font("Verdana", Font.PLAIN, 10));
		LabelLc.setBounds(199, 25, 29, 14);
		GroupBoxSendCardCommand.add(LabelLc);
		
		JLabel LabelLe = new JLabel("Le");
		LabelLe.setFont(new Font("Verdana", Font.PLAIN, 10));
		LabelLe.setBounds(243, 25, 29, 14);
		GroupBoxSendCardCommand.add(LabelLe);
		
		TextFieldCla = new JTextField();
		TextFieldCla.setHorizontalAlignment(SwingConstants.CENTER);
		TextFieldCla.setFont(new Font("Verdana", Font.PLAIN, 10));
		TextFieldCla.setBounds(15, 45, 37, 20);
		GroupBoxSendCardCommand.add(TextFieldCla);
		
		TextFieldIns = new JTextField();
		TextFieldIns.setHorizontalAlignment(SwingConstants.CENTER);
		TextFieldIns.setFont(new Font("Verdana", Font.PLAIN, 10));
		TextFieldIns.setColumns(2);
		TextFieldIns.setBounds(59, 45, 37, 20);
		GroupBoxSendCardCommand.add(TextFieldIns);
		
		TextFieldP1 = new JTextField();
		TextFieldP1.setHorizontalAlignment(SwingConstants.CENTER);
		TextFieldP1.setFont(new Font("Verdana", Font.PLAIN, 10));
		TextFieldP1.setColumns(2);
		TextFieldP1.setBounds(103, 45, 37, 20);
		GroupBoxSendCardCommand.add(TextFieldP1);
		
		TextFieldP2 = new JTextField();
		TextFieldP2.setHorizontalAlignment(SwingConstants.CENTER);
		TextFieldP2.setFont(new Font("Verdana", Font.PLAIN, 10));
		TextFieldP2.setColumns(2);
		TextFieldP2.setBounds(147, 45, 37, 20);
		GroupBoxSendCardCommand.add(TextFieldP2);
		
		TextFieldLc = new JTextField();
		TextFieldLc.setHorizontalAlignment(SwingConstants.CENTER);
		TextFieldLc.setFont(new Font("Verdana", Font.PLAIN, 10));
		TextFieldLc.setColumns(2);
		TextFieldLc.setBounds(191, 45, 37, 20);
		GroupBoxSendCardCommand.add(TextFieldLc);
		
		TextFieldLe = new JTextField();
		TextFieldLe.setHorizontalAlignment(SwingConstants.CENTER);
		TextFieldLe.setFont(new Font("Verdana", Font.PLAIN, 10));
		TextFieldLe.setColumns(2);
		TextFieldLe.setBounds(235, 45, 37, 20);
		GroupBoxSendCardCommand.add(TextFieldLe);
		
		JLabel LabelDataIn = new JLabel("Data In");
		LabelDataIn.setFont(new Font("Verdana", Font.PLAIN, 10));
		LabelDataIn.setBounds(15, 76, 59, 14);
		GroupBoxSendCardCommand.add(LabelDataIn);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(18, 100, 293, 68);
		GroupBoxSendCardCommand.add(scrollPane_1);
		
		TextAreaData = new JTextArea();
		TextAreaData.setFont(new Font("Verdana", Font.PLAIN, 10));
		TextAreaData.setWrapStyleWord(true);
		TextAreaData.setLineWrap(true);
		scrollPane_1.setViewportView(TextAreaData);
		
		JLabel LabelApduLogs = new JLabel("APDU Logs");
		LabelApduLogs.setFont(new Font("Verdana", Font.PLAIN, 10));
		LabelApduLogs.setBounds(352, 11, 68, 14);
		ContentPanel.add(LabelApduLogs);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(349, 30, 308, 306);
		ContentPanel.add(scrollPane);
		
		TextAreaApduLogs = new JTextArea();
		TextAreaApduLogs.setLineWrap(true);
		TextAreaApduLogs.setFont(new Font("Verdana", Font.PLAIN, 10));
		TextAreaApduLogs.setEditable(false);
		TextAreaApduLogs.setWrapStyleWord(true);
		scrollPane.setViewportView(TextAreaApduLogs);
		
		ButtonClear = new JButton("Clear");
		ButtonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clear();
			}
		});
		ButtonClear.setFont(new Font("Verdana", Font.PLAIN, 10));
		ButtonClear.setBounds(350, 347, 100, 23);
		ContentPanel.add(ButtonClear);
		
		ButtonReset = new JButton("Reset");
		ButtonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				disconnect();
			}
		});
		ButtonReset.setFont(new Font("Verdana", Font.PLAIN, 10));
		ButtonReset.setBounds(455, 347, 100, 23);
		ContentPanel.add(ButtonReset);
		
		ButtonQuit = new JButton("Quit");
		ButtonQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				quit();
			}
		});
		ButtonQuit.setFont(new Font("Verdana", Font.PLAIN, 10));
		ButtonQuit.setBounds(559, 347, 100, 23);
		ContentPanel.add(ButtonQuit);
		
		TextFieldCla.addKeyListener(this);
		TextFieldIns.addKeyListener(this);
		TextFieldLc.addKeyListener(this);
		TextFieldLe.addKeyListener(this);
		TextFieldP1.addKeyListener(this);
		TextFieldP2.addKeyListener(this);
		
		_pcscReader = new PcscReader();
		 
		// Instantiate an event handler object 
		_pcscReader.setEventHandler(new ReaderEvents());
		
		// Register the event handler implementation of this class
		_pcscReader.getEventHandler().addEventListener(this);
        
        _piccClass = new PiccClass(_pcscReader);
        
        resetEnableControls(false);
        TextAreaApduLogs.setText("Program Ready\r\n");
	}
	
	private void resetEnableControls(boolean isEnabled)
	{
		ButtonConnect.setEnabled(isEnabled);
		ButtonGetData.setEnabled(isEnabled);
		ButtonSendCardCommand.setEnabled(isEnabled);
		CheckBoxIsoACard.setEnabled(isEnabled);
		
		TextFieldCla.setEnabled(isEnabled);
		TextFieldIns.setEnabled(isEnabled);
		TextFieldLc.setEnabled(isEnabled);
		TextFieldLe.setEnabled(isEnabled);
		TextFieldP1.setEnabled(isEnabled);
		TextFieldP2.setEnabled(isEnabled);
		TextAreaData.setEnabled(isEnabled);
		
		TextFieldCla.setText("");
		TextFieldIns.setText("");
		TextFieldLc.setText("");
		TextFieldLe.setText("");
		TextFieldP1.setText("");
		TextFieldP2.setText("");
		TextAreaData.setText("");
		

	}
	
	private void initialize()
	{
		String[] readerList = null;
		
	    try
	    {
	    	readerList = _pcscReader.listTerminals();
			if (readerList.length == 0)
			{
				JOptionPane.showMessageDialog(this, "No PC/SC reader detected");
				return;
			}
			
			ComboBoxReaderNames.removeAllItems();
				
			for (int i = 0; i < readerList.length; i++)
			{
				if (!readerList.equals(""))	
					ComboBoxReaderNames.addItem(readerList[i]);
				else
					break;
			}
			
			ComboBoxReaderNames.setSelectedIndex(0);
			ButtonConnect.setEnabled(true);

			addMessageToLog("\r\nInitialize success");
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
	
	private void connect()
	{
		try
		{
			String readerName = (String)ComboBoxReaderNames.getSelectedItem();
			
			_pcscReader.connect(readerName, "*");
			
			addMessageToLog("\r\nSuccessfully connected to " + readerName);
			
			resetEnableControls(true);
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
	
	private void getData()
	{
		try
		{
			byte uIso14443A;
			byte[] uCardVersion;
			
			if(CheckBoxIsoACard.isSelected())
				uIso14443A = 0x01;
			else
				uIso14443A = 0x00;
			
			addTitleToLog("Get Data");
			uCardVersion = _piccClass.getData(uIso14443A);
			
			if(CheckBoxIsoACard.isSelected())
				addMessageToLog("\r\nUID: " + Helper.byteAsString(uCardVersion, true));
			else
				addMessageToLog("\r\nATS: " + Helper.byteAsString(uCardVersion, true));
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
	
	private void sendCommandData()
	{
		 byte   uCla, uIns, uP1, uP2;
         byte   uLc = 0x00;
         byte   uLe = 0x00;
         byte[] uData = null;
         int    iDataLen = 0;
         int    iCaseType = 0;
         
		try
		{
			// Validate inputs
			if(TextFieldCla.getText().length() < 2)
			{
				showErrorMessage("Please key-in hex value for CLA.");
				TextFieldCla.requestFocus();
				return;
			}
			
			if(TextFieldIns.getText().length() < 2)
			{
				showErrorMessage("Please key-in hex value for INS.");
				TextFieldIns.requestFocus();
				return;			
			}
			
			if(TextFieldP1.getText().length() < 2)
			{	
				showErrorMessage("Please key-in hex value for P1.");
				TextFieldP1.requestFocus();
				return;
			}
			
			if(TextFieldP2.getText().length() < 2)
			{
				showErrorMessage("Please key-in hex value for P2.");
				TextFieldP2.requestFocus();
				return;
			}
			
			uCla = (byte) ((Integer)Integer.parseInt(TextFieldCla.getText(), 16)).byteValue();
			uIns = (byte) ((Integer)Integer.parseInt(TextFieldIns.getText(), 16)).byteValue();
			uP1 = (byte) ((Integer)Integer.parseInt(TextFieldP1.getText(), 16)).byteValue();
			uP2 = (byte) ((Integer)Integer.parseInt(TextFieldP2.getText(), 16)).byteValue();
			
			if(Integer.parseInt(TextFieldLc.getText()) > 0 && TextFieldLc.getText() != "00")
			{
				if(TextAreaData.getText().length() == 0)
				{
					showErrorMessage("Please key-in hex value for Data In.");
					TextAreaData.requestFocus();
					return;
				}				
			}
			
			if(Integer.parseInt(TextFieldLc.getText()) > 0)
			{
				uLc = (byte) ((Integer)Integer.parseInt(TextFieldLc.getText(), 16)).byteValue();
				
				if(uLc > 0x00)
				{
					uData = Helper.getBytes(TextAreaData.getText(), " ");
					if(uData == null)
					{
						showErrorMessage("\r\nData In field has invalid format");
						TextAreaData.requestFocus();
						return;
					}
					
					iDataLen = uData.length;
					
					if((int)uLc != iDataLen)
					{
						showErrorMessage("Length of Data does not match Lc");
						TextAreaData.requestFocus();
						return;
					}
				}
			}
			
			if(TextFieldLe.getText().length() > 0)
			{
				uLe = (byte) ((Integer)Integer.parseInt(TextFieldLe.getText(), 16)).byteValue();
			}
			
			if(Integer.parseInt(TextFieldLc.getText()) == 0)
			{
				if(uLe == 0x00)
					iCaseType = 0;
				else
					iCaseType = 1;
			}
			else
			{
				if(uLe == 0x00)
					iCaseType = 2;
				else
					iCaseType = 3;
			}
			
			addTitleToLog("Send Command");
			_piccClass.sendCommand(iCaseType, uCla, uIns, uP1, uP2, uLc, uLe, uData);
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
	
	public void disconnect()
	{
		try			
		{
			//disconnect
			if (_pcscReader.isConnectionActive())
				_pcscReader.disconnect();
			
				addMessageToLog("SCardDisconnect Success");
				TextAreaApduLogs.setText("Program Ready\r\n");
				resetEnableControls(false);
				CheckBoxIsoACard.setSelected(false);
				ComboBoxReaderNames.removeAllItems();
				ButtonInitialize.setEnabled(true);
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
	
	public void quit()
	{
		
		this.dispose();	
	}
	
	public void clear()
	{
		
		TextAreaApduLogs.setText("");
	}
	
	public void addMessageToLog(String prefixStr, byte[] buff, String postfixStr, int buffLen)
	{
		String tmpStr = "";

        if (buff.length < buffLen)
            return;

        tmpStr = null;

        //Convert each byte from buff to its string representation.
        for (int i = 0; i < buffLen; i++)
            tmpStr += String.format("{0:X2}", buff[i]) + " ";

        addMessageToLog(prefixStr + tmpStr + postfixStr);
	}
	
	void addTitleToLog(String title)
	{
		TextAreaApduLogs.setSelectedTextColor(Color.black);
		TextAreaApduLogs.append("\r\n" + title + "\r\n");
	}
	
	void addMessageToLog(String msg)
	{
		TextAreaApduLogs.append(msg + "\r\n");		
	}	
	
	void showErrorMessage(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void onSendCommand(ReaderEvents.TransmitApduEventArg event) 
	{
		addMessageToLog("<< " + event.getAsString(true));		
	}

	public void onReceiveCommand(ReaderEvents.TransmitApduEventArg event) 
	{
		addMessageToLog(">> " + event.getAsString(true));		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent ke) {
		//Character x = (Character)ke.getKeyChar();
  		char empty = '\r';

  		//Check valid characters
  		/*if(textFieldAmount.isFocusOwner())
  		{	
  			if (VALIDCHARS.indexOf(x) == -1 ) 
  				ke.setKeyChar(empty);
  		}*/
  					  
		//Limit character length  	  	

		if (((JTextField)ke.getSource()).getText().length() >= 2 ) 
		{		
			ke.setKeyChar(empty);  				
			return;
		}			

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
