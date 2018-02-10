package card.Mifare;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.smartcardio.CardException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.SwingConstants;

public class MifareChangeKey extends JFrame implements
		ReaderEvents.TransmitApduHandler, ActionListener, KeyListener,
		FocusListener {

	int retCode;

	static String VALIDCHARS = "0123456789";
	static String VALIDCHARSHEX = "ABCDEFabcdef0123456789";
	String tempString = "";
	private byte currentSector, currentSectorTrailer;

	PcscReader pcscReader;
	private ReaderFunctions readerFunctions;
	private MifareClassic mifareClassic;

	private ReaderFunctions.CHIP_TYPE currentChipType = ReaderFunctions.CHIP_TYPE.UNKNOWN;

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private JPanel contentPanel;
	private JTextField textFieldKeyStoreNumber;
	private JTextField textFieldKeyValueInput;
	private JTextField textFieldSectorNumber;
	private JTextField textFieldAuthenticationKeyStoreNumber;
	private JTextField textFieldCurrentSectorNumber;
	private JTextField textFieldSectorTrailerBlock;
	private JTextField textFieldKeyA;
	private JTextField textFieldAccessBits;
	private JTextField textFieldKeyB;

	private JRadioButton radioButtonKeyB;
	private JRadioButton radioButtonKeyA;

	private JButton buttonInitialize;
	private JButton buttonConnect;
	private JButton buttonLoadKeys;
	private JButton buttonAuthenticate;
	private JButton buttonRead;
	private JButton buttonUpdate;
	private JButton buttonClear;
	private JButton buttonReset;
	private JButton buttonQuit;

	private JComboBox comboBoxReader;
	private JPanel panelAuthenticate;
	JPanel panelKeyType;
	JPanel panelLoad;
	JPanel panelChangeSector;

	private JLabel label;
	private JLabel label_1;
	JLabel labelKeyType;
	private JTextArea textAreaMessage;

	/** Creates new form MifareChangeKey */
	public MifareChangeKey() {
		setFont(new Font("Verdana", Font.PLAIN, 8));
		this.setTitle("Mifare Classic Change Key");
		// initComponents();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 829, 577);
		setResizable(false);

		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		panelLoad = new JPanel();
		panelLoad.setBorder(new TitledBorder(null,
				"Store Authentication Keys to Device", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelLoad.setBounds(10, 118, 396, 120);
		contentPanel.add(panelLoad);
		panelLoad.setLayout(null);

		JLabel lblKeyStoreNumber = new JLabel("Key Store Number (Dec)");
		lblKeyStoreNumber.setFont(new Font("Verdana", Font.PLAIN, 10));
		lblKeyStoreNumber.setBounds(16, 30, 143, 14);
		panelLoad.add(lblKeyStoreNumber);

		textFieldKeyStoreNumber = new JTextField();
		textFieldKeyStoreNumber.setFont(new Font("Verdana", Font.PLAIN, 10));
		textFieldKeyStoreNumber.setBounds(198, 27, 48, 20);
		panelLoad.add(textFieldKeyStoreNumber);
		textFieldKeyStoreNumber.setColumns(10);

		JLabel lblKeyValueInput = new JLabel("Key Value Input");
		lblKeyValueInput.setFont(new Font("Verdana", Font.PLAIN, 10));
		lblKeyValueInput.setBounds(16, 55, 123, 14);
		panelLoad.add(lblKeyValueInput);

		textFieldKeyValueInput = new JTextField();
		textFieldKeyValueInput.setFont(new Font("Verdana", Font.PLAIN, 10));
		textFieldKeyValueInput.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent arg0) {
				keyValueInputFocusLost();
			}
		});
		textFieldKeyValueInput.setBounds(198, 52, 161, 20);
		panelLoad.add(textFieldKeyValueInput);
		textFieldKeyValueInput.setColumns(10);

		buttonLoadKeys = new JButton("Load Key");
		buttonLoadKeys.setFont(new Font("Verdana", Font.PLAIN, 10));
		buttonLoadKeys.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadKeys();
			}
		});
		buttonLoadKeys.setBounds(268, 80, 118, 23);
		panelLoad.add(buttonLoadKeys);

		panelAuthenticate = new JPanel();
		panelAuthenticate.setBorder(new TitledBorder(null,
				"Authentication Function", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelAuthenticate.setBounds(10, 245, 396, 133);
		contentPanel.add(panelAuthenticate);
		panelAuthenticate.setLayout(null);

		panelKeyType = new JPanel();
		panelKeyType.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelKeyType.setBounds(16, 23, 90, 76);
		panelAuthenticate.add(panelKeyType);
		panelKeyType.setLayout(null);

		labelKeyType = new JLabel("Key Type:");
		labelKeyType.setFont(new Font("Verdana", Font.PLAIN, 10));
		labelKeyType.setBounds(10, 11, 79, 14);
		panelKeyType.add(labelKeyType);

		radioButtonKeyA = new JRadioButton("Key A");
		radioButtonKeyA.setFont(new Font("Verdana", Font.PLAIN, 10));
		radioButtonKeyA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radioButtonKeyA.setSelected(true);
				radioButtonKeyB.setSelected(false);
			}
		});
		radioButtonKeyA.setSelected(true);
		radioButtonKeyA.setBounds(10, 24, 60, 23);
		panelKeyType.add(radioButtonKeyA);

		radioButtonKeyB = new JRadioButton("Key B");
		radioButtonKeyB.setFont(new Font("Verdana", Font.PLAIN, 10));
		radioButtonKeyB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radioButtonKeyA.setSelected(false);
				radioButtonKeyB.setSelected(true);
			}
		});
		radioButtonKeyB.setBounds(10, 46, 60, 23);
		panelKeyType.add(radioButtonKeyB);

		JLabel lblSectorNumberdec = new JLabel("Sector Number (Dec)");
		lblSectorNumberdec.setFont(new Font("Verdana", Font.PLAIN, 10));
		lblSectorNumberdec.setBounds(111, 51, 141, 14);
		panelAuthenticate.add(lblSectorNumberdec);

		textFieldSectorNumber = new JTextField();
		textFieldSectorNumber.setFont(new Font("Verdana", Font.PLAIN, 10));
		textFieldSectorNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		textFieldSectorNumber.setBounds(252, 48, 62, 20);
		textFieldSectorNumber.setDocument(Helper.intFilter());
		panelAuthenticate.add(textFieldSectorNumber);
		textFieldSectorNumber.setColumns(10);

		label = new JLabel("00 - 39");
		label.setFont(new Font("Verdana", Font.PLAIN, 10));
		label.setBounds(324, 51, 46, 14);
		panelAuthenticate.add(label);

		JLabel lblKeyStoreNumber_1 = new JLabel("Key Store Number (Dec)");
		lblKeyStoreNumber_1.setFont(new Font("Verdana", Font.PLAIN, 10));
		lblKeyStoreNumber_1.setBounds(111, 73, 151, 14); // 73
		panelAuthenticate.add(lblKeyStoreNumber_1);

		textFieldAuthenticationKeyStoreNumber = new JTextField();
		textFieldAuthenticationKeyStoreNumber.setFont(new Font("Verdana",
				Font.PLAIN, 10));
		textFieldAuthenticationKeyStoreNumber.setBounds(252, 70, 62, 20);
		panelAuthenticate.add(textFieldAuthenticationKeyStoreNumber);
		textFieldAuthenticationKeyStoreNumber.setColumns(10);
		textFieldAuthenticationKeyStoreNumber.setDocument(Helper.intFilter());

		label_1 = new JLabel("00 - 01");
		label_1.setFont(new Font("Verdana", Font.PLAIN, 10));
		label_1.setBounds(324, 73, 46, 14);
		panelAuthenticate.add(label_1);

		buttonAuthenticate = new JButton("Authenticate");
		buttonAuthenticate.setFont(new Font("Verdana", Font.PLAIN, 10));
		buttonAuthenticate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				authenticate();
			}
		});
		buttonAuthenticate.setBounds(273, 100, 113, 23);
		panelAuthenticate.add(buttonAuthenticate);

		panelChangeSector = new JPanel();
		panelChangeSector.setBorder(new TitledBorder(null,
				"Change Sector Keys", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		panelChangeSector.setBounds(10, panelAuthenticate.getY()
				+ panelAuthenticate.getHeight() + 10, 396, 155);
		contentPanel.add(panelChangeSector);
		panelChangeSector.setLayout(null);

		JLabel lblCurrentSector = new JLabel("Current Sector (Dec)");
		lblCurrentSector.setFont(new Font("Verdana", Font.PLAIN, 10));
		lblCurrentSector.setBounds(10, 28, 121, 14);
		panelChangeSector.add(lblCurrentSector);

		textFieldCurrentSectorNumber = new JTextField();
		textFieldCurrentSectorNumber.setEditable(false);
		textFieldCurrentSectorNumber
				.setFont(new Font("Verdana", Font.PLAIN, 10));
		textFieldCurrentSectorNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		textFieldCurrentSectorNumber.setBounds(128, 25, 47, 20);
		panelChangeSector.add(textFieldCurrentSectorNumber);
		textFieldCurrentSectorNumber.setColumns(10);

		JLabel lblSectorTrainlerBlock = new JLabel("Sector Trailer Block (Dec)");
		lblSectorTrainlerBlock.setFont(new Font("Verdana", Font.PLAIN, 10));
		lblSectorTrainlerBlock.setBounds(185, 28, 142, 14);
		panelChangeSector.add(lblSectorTrainlerBlock);

		textFieldSectorTrailerBlock = new JTextField();
		textFieldSectorTrailerBlock.setEditable(false);
		textFieldSectorTrailerBlock
				.setFont(new Font("Verdana", Font.PLAIN, 10));
		textFieldSectorTrailerBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		textFieldSectorTrailerBlock.setBounds(327, 25, 55, 20);
		panelChangeSector.add(textFieldSectorTrailerBlock);
		textFieldSectorTrailerBlock.setColumns(10);

		textFieldKeyA = new JTextField(16);
		textFieldKeyA.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldKeyA.setFont(new Font("Verdana", Font.PLAIN, 10));
		textFieldKeyA.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				keyAFocusLost();
			}
		});
		textFieldKeyA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		textFieldKeyA.setBounds(10, 71, 121, 20);
		panelChangeSector.add(textFieldKeyA);
		textFieldKeyA.setColumns(10);

		textFieldAccessBits = new JTextField(12);
		textFieldAccessBits.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldAccessBits.setFont(new Font("Verdana", Font.PLAIN, 10));
		textFieldAccessBits.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				accessBitsFocusLost();
			}
		});
		textFieldAccessBits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		textFieldAccessBits.setBounds(141, 71, 110, 20);
		panelChangeSector.add(textFieldAccessBits);
		textFieldAccessBits.setColumns(10);

		textFieldKeyB = new JTextField(16);
		textFieldKeyB.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldKeyB.setFont(new Font("Verdana", Font.PLAIN, 10));
		textFieldKeyB.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				keyBFocusLost();
			}

		});
		textFieldKeyB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		textFieldKeyB.setBounds(261, 71, 121, 20);
		panelChangeSector.add(textFieldKeyB);
		textFieldKeyB.setColumns(10);

		JLabel lblKeyA = new JLabel("Key A");
		lblKeyA.setFont(new Font("Verdana", Font.PLAIN, 10));
		lblKeyA.setBounds(53, 95, 46, 14);
		panelChangeSector.add(lblKeyA);

		JLabel lblAccessBits = new JLabel("Access Bits");
		lblAccessBits.setFont(new Font("Verdana", Font.PLAIN, 10));
		lblAccessBits.setBounds(171, 95, 80, 14);
		panelChangeSector.add(lblAccessBits);

		JLabel lblKeyB = new JLabel("Key B");
		lblKeyB.setFont(new Font("Verdana", Font.PLAIN, 10));
		lblKeyB.setBounds(310, 95, 46, 14);
		panelChangeSector.add(lblKeyB);

		buttonRead = new JButton("Read");
		buttonRead.setFont(new Font("Verdana", Font.PLAIN, 10));
		buttonRead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				read();
			}
		});
		buttonRead.setBounds(63, 120, 128, 23);
		panelChangeSector.add(buttonRead);

		buttonUpdate = new JButton("Update");
		buttonUpdate.setFont(new Font("Verdana", Font.PLAIN, 10));
		buttonUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		buttonUpdate.setBounds(199, 120, 128, 23);
		panelChangeSector.add(buttonUpdate);

		JLabel lblApduLogs = new JLabel("APDU Logs");
		lblApduLogs.setFont(new Font("Verdana", Font.PLAIN, 10));
		lblApduLogs.setBounds(423, 11, 90, 14);
		contentPanel.add(lblApduLogs);

		buttonClear = new JButton("Clear");
		buttonClear.setFont(new Font("Verdana", Font.PLAIN, 10));
		buttonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		buttonClear.setBounds(426, 508, 117, 23);
		contentPanel.add(buttonClear);

		buttonReset = new JButton("Reset");
		buttonReset.setFont(new Font("Verdana", Font.PLAIN, 10));
		buttonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		buttonReset.setBounds(553, 508, 117, 23);
		contentPanel.add(buttonReset);

		buttonQuit = new JButton("Quit");
		buttonQuit.setFont(new Font("Verdana", Font.PLAIN, 10));
		buttonQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
			}
		});
		buttonQuit.setBounds(680, 508, 117, 23);
		contentPanel.add(buttonQuit);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(416, 36, 384, 462);
		contentPanel.add(scrollPane);

		textAreaMessage = new JTextArea();
		textAreaMessage.setLineWrap(true);
		textAreaMessage.setEditable(false);
		textAreaMessage.setFont(new Font("Verdana", Font.PLAIN, 10));
		scrollPane.setViewportView(textAreaMessage);

		JLabel lblSelectReader = new JLabel("Select Reader");
		lblSelectReader.setBounds(10, 33, 85, 14);
		contentPanel.add(lblSelectReader);
		lblSelectReader.setFont(new Font("Verdana", Font.PLAIN, 10));

		comboBoxReader = new JComboBox();
		comboBoxReader.setBounds(105, 29, 301, 22);
		contentPanel.add(comboBoxReader);
		comboBoxReader.setFont(new Font("Verdana", Font.PLAIN, 10));

		buttonInitialize = new JButton("Initialize");
		buttonInitialize.setBounds(288, 62, 118, 23);
		contentPanel.add(buttonInitialize);
		buttonInitialize.setFont(new Font("Verdana", Font.PLAIN, 10));

		buttonConnect = new JButton("Connect");
		buttonConnect.setBounds(288, 94, 118, 23);
		contentPanel.add(buttonConnect);
		buttonConnect.setEnabled(false);
		buttonConnect.setFont(new Font("Verdana", Font.PLAIN, 10));
		buttonConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		buttonInitialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initialize();
			}
		});

		//Add event listeners
		textFieldKeyStoreNumber.addKeyListener(this);
		textFieldKeyValueInput.addKeyListener(this);
		textFieldSectorNumber.addKeyListener(this);
		textFieldAuthenticationKeyStoreNumber.addKeyListener(this);
		textFieldCurrentSectorNumber.addKeyListener(this);
		textFieldSectorTrailerBlock.addKeyListener(this);
		textFieldKeyA.addKeyListener(this);
		textFieldAccessBits.addKeyListener(this);
		textFieldKeyB.addKeyListener(this);
		
		//Disable copy/paste
		textFieldKeyStoreNumber.setTransferHandler(null);
		textFieldKeyValueInput.setTransferHandler(null);
		textFieldSectorNumber.setTransferHandler(null);
		textFieldAuthenticationKeyStoreNumber.setTransferHandler(null);
		textFieldCurrentSectorNumber.setTransferHandler(null);
		textFieldSectorTrailerBlock.setTransferHandler(null);
		textFieldKeyA.setTransferHandler(null);
		textFieldAccessBits.setTransferHandler(null);
		textFieldKeyB.setTransferHandler(null);

		resetFields();

		pcscReader = new PcscReader();
		// Instantiate an event handler object
		pcscReader.setEventHandler(new ReaderEvents());

		// Register the event handler implementation of this class
		pcscReader.getEventHandler().addEventListener(this);

		readerFunctions = new ReaderFunctions(pcscReader);
	}

	private void initialize()
	{
		String[] readerList = null;

		try 
		{
			readerList = readerFunctions.getPcscConnection().listTerminals();
			
			if (readerList.length == 0)
			{
				addMsgToLog("No PC/SC reader detected");
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

			buttonConnect.setEnabled(true);
			comboBoxReader.setEnabled(true);
			buttonReset.setEnabled(true);

			addMsgToLog("Initialize Success\r\n");
		}
		catch (CardException ex)
		{
			addMsgToLog(PcscProvider.GetScardErrMsg(ex));
			showErrorMessage(PcscProvider.GetScardErrMsg(ex));
		}
		catch (Exception ex)
		{
			addMsgToLog(ex.getMessage());
			showErrorMessage(ex.getMessage());
		}
	}

	private void connect()
	{
		String chipType = "";

		try
		{
			if (readerFunctions.getPcscConnection().isConnectionActive())
				readerFunctions.getPcscConnection().disconnect();

			String rdrcon = (String) comboBoxReader.getSelectedItem();

			readerFunctions.getPcscConnection().connect(rdrcon, "*");
			mifareClassic = new MifareClassic(readerFunctions.getPcscConnection());

			addMsgToLog("Successful connection to " + rdrcon);

			currentChipType = readerFunctions.getChipType();

			if (currentChipType != ReaderFunctions.CHIP_TYPE.MIFARE_1K
					&& currentChipType != ReaderFunctions.CHIP_TYPE.MIFARE_4K)
			{
				showErrorMessage("Card is not supported.\r\nPlease present Mifare Classic card.");
				return;
			}

			if (currentChipType == ReaderFunctions.CHIP_TYPE.MIFARE_1K)
				chipType = "Mifare Standard 1K";
			else if (currentChipType == ReaderFunctions.CHIP_TYPE.MIFARE_4K)
				chipType = "Mifare Standard 4K";

			addMsgToLog("Chip Type: " + chipType);
			addMsgToLog("");

			if (currentChipType == ReaderFunctions.CHIP_TYPE.MIFARE_1K)
				label.setText("00 - 15");
			else
				label.setText("00 - 39");

			// Store Authentication Keys Controls
			enableLoadPanel(true);

		}
		catch (CardException ex)
		{
			addTitleToLog(PcscProvider.GetScardErrMsg(ex));
			showErrorMessage(PcscProvider.GetScardErrMsg(ex));
		}
		catch (Exception ex)
		{
			addMsgToLog(ex.getMessage());
			addTitleToLog(ex.getMessage());
		}
	}

	private void loadKeys()
	{
		byte[] key = new byte[6];
		byte keyNumber = 0x20;

		ReaderFunctions.KEY_STRUCTURE keyStructure = ReaderFunctions.KEY_STRUCTURE.VOLATILE;

		String[] strKeys;
		try 
		{

			if (!(textFieldKeyStoreNumber.getText().matches("[0-9A-Fa-f]+"))) 
			{
				showErrorMessage("Please key-in Key Store Number from 00 to 01.");
				textFieldKeyStoreNumber.requestFocus();
				return;
			}

			keyNumber = (byte) ((Integer) Integer.parseInt(
					textFieldKeyStoreNumber.getText(), 16)).byteValue();
			
			if(keyNumber > (byte) 0x01 || keyNumber < (byte) 0x00)
            {
            	showErrorMessage("Please key-in Key Store Number from 00 to 01.");
            	textFieldKeyStoreNumber.requestFocus();
                return;
            }

			if (textFieldKeyValueInput.getText().trim().equals(""))
			{
				showErrorMessage("Please key-in hex value for Key Value.");
				textFieldKeyValueInput.requestFocus();
				return;
			}

			if (textFieldKeyValueInput.getText().replaceAll(" ", "").length() != 12) 
			{
				showErrorMessage("Please key-in hex value for Key Value.");
				textFieldKeyValueInput.requestFocus();
				return;
			}

			// key should be 6 bytes long
			strKeys = textFieldKeyValueInput.getText().trim().split(" ");

			for (int i = 0; i < strKeys.length; i++)
			{
				key[i] = (byte) ((Integer) Integer.parseInt(strKeys[i], 16))
						.byteValue();
			}
			
			addTitleToLog("Load Authentication Key");

			if (readerFunctions.loadAuthKey(keyStructure, keyNumber, key) == false)
				showErrorMessage("Load Key failed");
			else
			{
				addMsgToLog("Load Key success");

				// Authenticate Keys Controls
				enableAuthenticatePanel(true);
			}

		} 
		catch (CardException ex)
		{
			addTitleToLog(PcscProvider.GetScardErrMsg(ex));
			showErrorMessage(PcscProvider.GetScardErrMsg(ex));
		}
		catch (Exception ex) 
		{
			addMsgToLog(ex.getMessage());
			addTitleToLog(ex.getMessage());
		}
	}

	private void authenticate() 
	{
		byte sectorNumber = 0x00;
		byte keyNumber = 0x00;
		ReaderFunctions.KEYTYPES keyType = ReaderFunctions.KEYTYPES.ACR1281_KEYTYPE_A;

		try
		{
			if (textFieldSectorNumber.getText().equals("")) 
			{
				showErrorMessage("Please key-in numeric value for Sector Number.");
				textFieldSectorNumber.requestFocus();
				return;
			}

			String temp = textFieldSectorNumber.getText();
			sectorNumber = Byte.valueOf(temp);

			if (textFieldAuthenticationKeyStoreNumber.getText().equals(""))
			{
				showErrorMessage("Please key-in Key Store Number from 00 to 01.");
				textFieldAuthenticationKeyStoreNumber.requestFocus();
				return;
			} 
			else
			{
				if (Integer.parseInt(textFieldAuthenticationKeyStoreNumber
						.getText()) > 1) 
				{
					showErrorMessage("Please key-in Key Store Number from 00 to 01.");
					textFieldAuthenticationKeyStoreNumber.requestFocus();
					return;
				}
			}

			keyNumber = (byte) ((Integer) Integer.parseInt(
					textFieldAuthenticationKeyStoreNumber.getText(), 16))
					.byteValue();

			if (radioButtonKeyB.isSelected())
				keyType = ReaderFunctions.KEYTYPES.ACR1281_KEYTYPE_B;

			if (currentChipType == ReaderFunctions.CHIP_TYPE.MIFARE_4K)
			{
				if (sectorNumber > 39) 
				{
					showErrorMessage("Invalid Sector Number. Valid Sector Number for Mifare 4K: 0 - 39.");
					return;
				}

				currentSector = sectorNumber;

				// Mifare 4K is organized in 32 sectors with 4 blocks
				// and in 8 sectors with 16 blocks
				if (currentSector <= 31) {
					currentSectorTrailer = (byte) ((currentSector * 4) + 3);
				} 
				else
				{
					// 127 is the physical address of the last block (sector
					// trailer) of the 32nd sector
					currentSectorTrailer = 127;

					// succeeding sector contains 16 blocks
					currentSectorTrailer += (byte) (((currentSector - 32) * 16) + 16);
				}
			} 
			else 
			{
				if (sectorNumber > 15) 
				{
					showErrorMessage("Invalid Sector Number. Valid Sector Number for Mifare 1K: 0 - 15.");
					return;
				}

				currentSector = sectorNumber;
				currentSectorTrailer = (byte) ((currentSector * 4) + 3);
			}

			addTitleToLog("Authenticate Key");
			
			if (readerFunctions.authenticate(currentSectorTrailer, keyType, keyNumber) == false)
				showErrorMessage("Authenticate failed");
			else 
			{
				addMsgToLog("Authenticate success");

				textFieldSectorTrailerBlock.setText(Integer.toString(Byte.toUnsignedInt(currentSectorTrailer)));
				textFieldCurrentSectorNumber.setText(Byte.toString(currentSector));
				
				// Change Sector Keys Controls
				enableChangeKeyPanel(true);
				
			}
		} 
		catch (Exception ex)
		{
			if (ex.getMessage() != null)
				addMsgToLog(ex.getMessage() + "\r\n");
		}
	}

	private void read()
	{
		byte[] sectorTrailer;

		try
		{
			addTitleToLog("Read Binary");
			
			sectorTrailer = mifareClassic.readBinaryBlock(currentSectorTrailer, (byte) 0x10);

			if (sectorTrailer == null || sectorTrailer.length == 0)
				showErrorMessage("Read failed");
			else 
			{
				// Mifare does not allow you to read the actual key A.
				// We will leave this field as blank.
				textFieldKeyA.setText("");
				textFieldAccessBits.setText(byteArrayToString(sectorTrailer, 6, 4, true));
				textFieldKeyB.setText(byteArrayToString(sectorTrailer, 10, 6, true));

				showInformationMessage("NOTE: Mifare does not allow user to read the actual key A. 'Key A' field will be set to empty.");
				addMsgToLog("Read success");
			}
		} 
		catch (Exception ex)
		{
			if (ex.getMessage() != null)
				addMsgToLog(ex.getMessage() + "\r\n");
		}
	}

	private void update() {
		byte[] keyA, accessBits, keyB;
		byte[] newSectorTrailer;

		try
		{
			keyA = Helper.stringToByteArray(textFieldKeyA.getText().trim()
					.replaceAll(" ", ""));
			accessBits = Helper.stringToByteArray(textFieldAccessBits.getText()
					.replaceAll(" ", ""));
			keyB = Helper.stringToByteArray(textFieldKeyB.getText().trim()
					.replaceAll(" ", ""));

			if (keyA == null || keyA.length != 6)
			{
				showErrorMessage("Please key-in 6 bytes key A.");
				textFieldKeyA.requestFocus();
				return;
			}

			if (keyB == null || keyB.length != 6)
			{
				showErrorMessage("Please key-in 6 bytes key B.");
				textFieldKeyB.requestFocus();
				return;
			}

			if (accessBits == null || accessBits.length != 4)
			{
				showErrorMessage("Please key-in 4 bytes access bits.");
				textFieldAccessBits.requestFocus();
				return;
			}

			if (!Helper.byteArrayIsEqual(accessBits, new byte[] { (byte) 0xFF,
					(byte) 0x07, (byte) 0x80, (byte) 0x69 }, 4))
			{
				if (JOptionPane
						.showConfirmDialog(
								this,
								"IMPORTANT: Check Access Bits!\r\n\r\n"
										+ "Please make sure that the access bits are valid you may refer to Mifare Classic manual for more information.\r\n"
										+ "If the access bits are invalid the sector will PERMANENTLY lock it self.\r\n\r\nContinue?", "Warning!", JOptionPane.YES_NO_OPTION) != 0) {
					return;
				}

			}

			showInformationMessage("Please take note of the following: \r\n\r\n"
					+ "Sector: "
					+ Byte.toString(currentSector)
					+ "\r\nKey A : "
					+ Helper.byteAsString(keyA, true)
					+ "\r\nKey B : "
					+ Helper.byteAsString(keyB, true)
					+ "\r\nAccess Bits : "
					+ Helper.byteAsString(accessBits, true));

			newSectorTrailer = new byte[16];

			int i;

			// Append Key A
			for (i = 0; i < 6; i++)
				newSectorTrailer[i] = keyA[i];

			// Append Access Bits
			for (i = 0; i < 4; i++)
				newSectorTrailer[i + 6] = accessBits[i];

			// Append key B
			for (i = 0; i < 6; i++)
				newSectorTrailer[i + 10] = keyB[i];
			
			addTitleToLog("Update Binary");

			mifareClassic.updateBinaryBlock(currentSectorTrailer, newSectorTrailer, (byte) 0x10);

			addMsgToLog("Update success");
			
			addMsgToLog("Sector     : " + Byte.toString(currentSector));
			addMsgToLog("New Key A  : " + Helper.byteAsString(keyA, true));
			addMsgToLog("New Key B  : " + Helper.byteAsString(keyB, true));
			addMsgToLog("Access Bits: " + Helper.byteAsString(accessBits, true));
		} 
		catch (Exception ex)
		{
			if (ex.getMessage() != null)
				addMsgToLog(ex.getMessage() + "\r\n");
		}
	}

	private void reset()
	{
		try 
		{
			// disconnect
			if (readerFunctions.getPcscConnection().isConnectionActive())
				readerFunctions.getPcscConnection().disconnect();

			textAreaMessage.setText("");
			resetFields();
			comboBoxReader.removeAllItems();
		} 
		catch (Exception ex) 
		{
			addMsgToLog(ex.getMessage().toString());
		}
	}

	private void quit() 
	{
		this.dispose();
	}

	private void clear()
	{
		textAreaMessage.setText("");
	}

	public void keyReleased(KeyEvent ke) 
	{

	}

	public void keyPressed(KeyEvent ke)
	{
		// restrict paste actions
		if (ke.getKeyCode() == KeyEvent.VK_V)
			ke.setKeyCode(KeyEvent.VK_UNDO);
	}

	public void keyTyped(KeyEvent ke)
	{
		Character x = (Character) ke.getKeyChar();
		char empty = '\r';

		// Check valid characters
		if (textFieldKeyStoreNumber.isFocusOwner() || textFieldSectorNumber.isFocusOwner()
				|| textFieldAuthenticationKeyStoreNumber.isFocusOwner())
		{
			if (VALIDCHARS.indexOf(x) == -1)
				ke.setKeyChar(empty);
		}
		else
		{
			if (VALIDCHARSHEX.indexOf(x) == -1)
				ke.setKeyChar(empty);
		}
		
		//Limit character length
  		if(textFieldKeyStoreNumber.isFocusOwner() || textFieldSectorNumber.isFocusOwner() ||
  				textFieldAuthenticationKeyStoreNumber.isFocusOwner())
  		{	
  			if (((JTextField)ke.getSource()).getText().length() >= 2 ) 
  			{
  				ke.setKeyChar(empty);	
  				return;
  			}  			
  		}
  		else if(textFieldKeyValueInput.isFocusOwner() || textFieldKeyA.isFocusOwner() ||
  				textFieldKeyB.isFocusOwner())
  		{	
  			if (((JTextField)ke.getSource()).getText().length() >= 12 ) 
  			{		
  				ke.setKeyChar(empty);	
  				return;  				
  			}  			
  		}
  		else if (textFieldAccessBits.isFocusOwner())
  		{
  			if (((JTextField)ke.getSource()).getText().length() >= 8 ) 
  			{		
  				ke.setKeyChar(empty);	
  				return;  				
  			}
  		}
	}

	public void focusGained(FocusEvent e)
	{

	}

	private void accessBitsFocusLost() 
	{
		String tmpStr = "", tmpStr2 = "";

		tmpStr = "";
		tmpStr2 = "";

		tmpStr = textFieldAccessBits.getText().replaceAll(" ", "");

		for (int i = 0; i < tmpStr.length() / 2; i++) 
		{
			tmpStr2 = tmpStr2 + " " + tmpStr.substring(i + i, i + i + 2);
		}

		textFieldAccessBits.setText(tmpStr2);
	}

	private void keyAFocusLost() 
	{
		String tmpStr = "", tmpStr2 = "";

		tmpStr = "";
		tmpStr2 = "";

		tmpStr = textFieldKeyA.getText().replaceAll(" ", "");

		for (int i = 0; i < tmpStr.length() / 2; i++) 
		{
			tmpStr2 = tmpStr2 + " " + tmpStr.substring(i + i, i + i + 2);
		}

		textFieldKeyA.setText(tmpStr2);
	}

	private void keyBFocusLost()
	{
		String tmpStr = "", tmpStr2 = "";

		tmpStr = "";
		tmpStr2 = "";

		tmpStr = textFieldKeyB.getText().replaceAll(" ", "");

		for (int i = 0; i < tmpStr.length() / 2; i++)
		{
			tmpStr2 = tmpStr2 + " " + tmpStr.substring(i + i, i + i + 2);
		}

		textFieldKeyB.setText(tmpStr2);
	}

	private void keyValueInputFocusLost() 
	{
		String tmpStr = "", tmpStr2 = "";

		tmpStr = "";
		tmpStr2 = "";

		tmpStr = textFieldKeyValueInput.getText().replaceAll(" ", "");

		for (int i = 0; i < tmpStr.length() / 2; i++) 
		{
			tmpStr2 = tmpStr2 + " " + tmpStr.substring(i + i, i + i + 2);
		}

		textFieldKeyValueInput.setText(tmpStr2);
	}

	String byteArrayToString(byte[] b, int startIndx, int len,
			boolean spaceInBetween) 
	{
		byte[] newByte;

		if (b.length < startIndx + len)
			b = new byte[startIndx + len];

		newByte = new byte[len];

		for (int i = 0; i < len; i++, startIndx++)
			newByte[i] = b[startIndx];

		return byteArrayToString(newByte, spaceInBetween);
	}

	String byteArrayToString(byte[] tmpBytes, boolean spaceInBetween) 
	{
		String tmpStr = "", tmpStr2 = "";

		if (tmpBytes == null)
			return "";

		for (int i = 0; i < tmpBytes.length; i++) 
		{
			tmpStr = Integer
					.toHexString(((Byte) tmpBytes[i]).intValue() & 0xFF)
					.toUpperCase();

			// For single character hex
			if (tmpStr.length() == 1)
				tmpStr = "0" + tmpStr;

			tmpStr2 += " " + tmpStr;
		}

		return tmpStr2;
	}

	private void resetFields() 
	{
		enablePanels(false);
		
		textFieldKeyStoreNumber.setText("");
		textFieldKeyValueInput.setText("");
		
		radioButtonKeyA.setSelected(true);
		radioButtonKeyB.setSelected(false);
		
		textFieldCurrentSectorNumber.setText("");
		textFieldSectorTrailerBlock.setText("");
		textFieldKeyA.setText("");
		textFieldAccessBits.setText("");
		textFieldKeyB.setText("");

		addMsgToLog("Program Ready\n");
	}

	private void enablePanels(boolean isEnable)
	{
		enableLoadPanel(isEnable);
		enableAuthenticatePanel(isEnable);
		enableChangeKeyPanel(isEnable);
	}

	private void enableLoadPanel(boolean isEnable)
	{
		panelLoad.setEnabled(isEnable);

		for (Component controls : panelLoad.getComponents()) 
		{
			controls.setEnabled(isEnable);
		}
	}

	private void enableAuthenticatePanel(boolean isEnable)
	{
		panelAuthenticate.setEnabled(isEnable);

		for (Component controls : panelAuthenticate.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		radioButtonKeyA.setEnabled(isEnable);
		radioButtonKeyB.setEnabled(isEnable);
		labelKeyType.setEnabled(isEnable);

		textFieldSectorNumber.setText("");
		textFieldAuthenticationKeyStoreNumber.setText("");
	}

	private void enableChangeKeyPanel(boolean isEnable)
	{
		panelChangeSector.setEnabled(isEnable);

		for (Component controls : panelChangeSector.getComponents())
		{
			controls.setEnabled(isEnable);
		}
	}

	public void addMsgToLog(String prefixStr, byte[] buff, String postfixStr,
			int buffLen) 
	{
		String tmpStr = "";

		if (buff.length < buffLen)
			return;

		tmpStr = null;

		// Convert each byte from buff to its string representation.
		for (int i = 0; i < buffLen; i++)
			tmpStr += String.format("{0:X2}", buff[i]) + " ";

		addMsgToLog(prefixStr + tmpStr + postfixStr);
	}

	void addTitleToLog(String title)
	{
		textAreaMessage.setSelectedTextColor(Color.black);
		textAreaMessage.append("\r\n" + title + "\r\n");
	}

	void addMsgToLog(String msg)
	{
		textAreaMessage.append(msg + "\r\n");
	}

	void showInformationMessage(String message) 
	{
		JOptionPane.showMessageDialog(this, message, "Information",
				JOptionPane.INFORMATION_MESSAGE);
	}

	void showErrorMessage(String message) 
	{
		JOptionPane.showMessageDialog(this, message, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	public void onSendCommand(ReaderEvents.TransmitApduEventArg event)
	{
		addMsgToLog("<< " + event.getAsString(true));
	}

	public void onReceiveCommand(ReaderEvents.TransmitApduEventArg event) 
	{
		addMsgToLog(">> " + event.getAsString(true));
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[])
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run() {
				new MifareChangeKey().setVisible(true);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent arg0) 
	{
		// TODO Auto-generated method stub

	}
}
