package card.Mifare;/*
  Copyright(C):      Advanced Card Systems Ltd

  File:              mifareProg.java

  Description:       This sample program outlines the steps on how to
                     transact with Mifare 1K/4K cards using ACR128

  Author:            M.J.E.C. Castillo

  Date:              July 7, 2008

  Revision Trail:   (Date/Author/Description)

======================================================================*/

import static javax.swing.JOptionPane.YES_NO_OPTION;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.smartcardio.CardException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;

public class MifareCardProgramming extends JFrame implements ReaderEvents.TransmitApduHandler,ActionListener, KeyListener{

	//JPCSC Variables
	int retCode;
	boolean connActive; 
	static String VALIDCHARS = "0123456789";
	static String VALIDCHARSHEX = "ABCDEFabcdef0123456789";
        private ReaderFunctions.CHIP_TYPE currentChipType = ReaderFunctions.CHIP_TYPE.UNKNOWN;
	
	//All variables that requires pass-by-reference calls to functions are
	//declared as 'Array of int' with length 1
	//Java does not process pass-by-ref to int-type variables, thus Array of int was used.
	int [] ATRLen = new int[1]; 
	int [] hContext = new int[1]; 
	int [] cchReaders = new int[1];
	int [] hCard = new int[1];
	int [] PrefProtocols = new int[1]; 		
	int [] RecvLen = new int[1];
	int SendLen = 0;
	int [] nBytesRet =new int[1];
	int maxBlocks = 63;
	byte [] SendBuff = new byte[262];
	byte [] RecvBuff = new byte[262];
	byte [] szReaders = new byte[1024];
	
	//GUI Variables
    private JPanel authPanel, binBlkPanel, msgPanel, storeAuthPanel, valBlkPanel;
    private JButton buttonBinRead, buttonClear, buttonConnect, buttonInitialize, buttonLoadKey, buttonReset, buttonValDec, buttonValInc;
    private JButton buttonValRead, buttonValRes, buttonValStore, buttonAuthenticate, buttonBinUpdate, buttonQuit;
    private ButtonGroup bdKeyType, bgKeySource, bgStoreKeys;
    private JComboBox comboboxReader;
    private JLabel jLabel1, jLabel10, jLabel11, jLabel12, jLabel2, jLabel3, jLabel4;
    private JLabel jLabel6, jLabel7, jLabel8, jLabel9, lblReader;
    private JPanel keyTypePanel, keyValPanel;
    private JTextArea mMsg;
    private JRadioButton radiobuttonKeyA, radiobuttonKeyB;
    private JScrollPane scrlPaneMsg;
    private JTextField textboxBinBlk, textboxBinData, textboxBinLen, textboxBlockNumber, textboxKey1, textboxKey2, textboxKey3, textboxKey4, textboxKey5;
    private JTextField textboxKey6, textboxKeyAdd;
    private JTextField textboxMemAdd, textboxValAmount, textboxValBlk, textboxValSrc, textboxValTar;
 
    private JLabel lblApduLogs;
    private PcscReader pcscReader;
    private MifareClassic mifareClassic;
    private ReaderFunctions readerFunctions;
    

    Dimension dim =Toolkit.getDefaultToolkit().getScreenSize();
    
    public MifareCardProgramming() {
    	getContentPane().setFont(new Font("Verdana", Font.PLAIN, 10));
    	setFont(new Font("Verdana", Font.PLAIN, 8));
    	setResizable(false);
    	//this.setTitle("Mifare Card Programming");
        //initComponents();
        //initMenu();
    	
    	this.setTitle("Mifare Classic Card Programming");
    	initComponents();
    	resetFields();
    	
    	pcscReader=new PcscReader(); //instantiate an event handler object
    	pcscReader.setEventHandler(new ReaderEvents());
    	pcscReader.getEventHandler().addEventListener(this);
    	readerFunctions= new ReaderFunctions(pcscReader);
    }


    @SuppressWarnings("unchecked")

    private void initComponents() {

    	setSize(800,560);
        bgStoreKeys = new ButtonGroup();
        bgKeySource = new ButtonGroup();
        bdKeyType = new ButtonGroup();
        storeAuthPanel = new JPanel();
        storeAuthPanel.setBounds(10, 113, 362, 120);
        jLabel1 = new JLabel();
        jLabel1.setFont(new Font("Verdana", Font.PLAIN, 10));
        jLabel2 = new JLabel();
        jLabel2.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonLoadKey = new JButton();
        buttonLoadKey.setFont(new Font("Verdana", Font.PLAIN, 10));
        keyValPanel = new JPanel();
        textboxKey1 = new JTextField();
        textboxKey1.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxKey2 = new JTextField();
        textboxKey2.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxKey3 = new JTextField();
        textboxKey3.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxKey4 = new JTextField();
        textboxKey4.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxKey5 = new JTextField();
        textboxKey5.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxKey6 = new JTextField();
        textboxKey6.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxMemAdd = new JTextField();
        textboxMemAdd.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxMemAdd.setEnabled(false);        
        authPanel = new JPanel();
        authPanel.setBounds(10, storeAuthPanel.getY() + storeAuthPanel.
                getHeight() + 5, 362, 137);
        keyTypePanel = new JPanel();
        keyTypePanel.setBounds(16, 22, 92, 75);
        radiobuttonKeyA = new JRadioButton();
        radiobuttonKeyA.setFont(new Font("Verdana", Font.PLAIN, 10));
        radiobuttonKeyA.setSelected(true);
        radiobuttonKeyB = new JRadioButton();
        radiobuttonKeyB.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonAuthenticate = new JButton();
        buttonAuthenticate.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonAuthenticate.setBounds(232, 99, 114, 23);
        
        jLabel4 = new JLabel();
        jLabel4.setFont(new Font("Verdana", Font.PLAIN, 10));
        jLabel4.setBounds(148, 70, 112, 14);
        textboxKeyAdd = new JTextField();
        textboxKeyAdd.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxKeyAdd.setEnabled(false);
        textboxKeyAdd.setBounds(253, 67, 29, 20);
        
        jLabel3 = new JLabel();
        jLabel3.setFont(new Font("Verdana", Font.PLAIN, 10));
        jLabel3.setBounds(148, 45, 91, 14);        
        textboxBlockNumber = new JTextField();
        textboxBlockNumber.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxBlockNumber.setBounds(253, 42, 29, 20); //(138, 127, 29, 20);
        
        binBlkPanel = new JPanel();
        binBlkPanel.setBounds(10, authPanel.getY() + authPanel.getHeight() + 5, 362, 137);
        jLabel6 = new JLabel();
        jLabel6.setFont(new Font("Verdana", Font.PLAIN, 10));
        jLabel7 = new JLabel();
        jLabel7.setFont(new Font("Verdana", Font.PLAIN, 10));
        jLabel8 = new JLabel();
        jLabel8.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxBinBlk = new JTextField();
        textboxBinBlk.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxBinLen = new JTextField();
        textboxBinLen.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxBinData = new JTextField();
        textboxBinData.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyTyped(KeyEvent e) {
        		if (textboxBinData.getText().length() >= Integer.parseInt(textboxBinLen.getText()))
        		{
        			e.consume();
        		}
        	}
        });
        textboxBinData.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonBinRead = new JButton();
        buttonBinRead.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonBinUpdate = new JButton();
        buttonBinUpdate.setFont(new Font("Verdana", Font.PLAIN, 10));
        valBlkPanel = new JPanel();
        valBlkPanel.setBounds(388, 11, 383, 187);
        jLabel9 = new JLabel();
        jLabel9.setFont(new Font("Verdana", Font.PLAIN, 10));
        jLabel9.setBounds(16, 24, 117, 14);
        textboxValAmount = new JTextField();
        textboxValAmount.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxValAmount.setBounds(143, 21, 70, 20);
        jLabel10 = new JLabel();
        jLabel10.setFont(new Font("Verdana", Font.PLAIN, 10));
        jLabel10.setBounds(16, 53, 117, 14);
        textboxValBlk = new JTextField();
        textboxValBlk.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxValBlk.setBounds(143, 50, 32, 20);
        jLabel11 = new JLabel();
        jLabel11.setFont(new Font("Verdana", Font.PLAIN, 10));
        jLabel11.setBounds(16, 82, 117, 14);
        textboxValSrc = new JTextField();
        textboxValSrc.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxValSrc.setBounds(143, 81, 32, 20);
        jLabel12 = new JLabel();
        jLabel12.setFont(new Font("Verdana", Font.PLAIN, 10));
        jLabel12.setBounds(16, 111, 117, 14);
        textboxValTar = new JTextField();
        textboxValTar.setFont(new Font("Verdana", Font.PLAIN, 10));
        textboxValTar.setBounds(143, 108, 32, 20);
        buttonValStore = new JButton();
        buttonValStore.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonValStore.setBounds(248, 20, 120, 23);
        buttonValInc = new JButton();
        buttonValInc.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonValInc.setBounds(248, 49, 120, 23);
        buttonValDec = new JButton();
        buttonValDec.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonValDec.setBounds(248, 77, 120, 23);
        buttonValRead = new JButton();
        buttonValRead.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonValRead.setBounds(248, 107, 120, 23);
        buttonValRes = new JButton();
        buttonValRes.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonValRes.setBounds(248, 136, 120, 23);
        msgPanel = new JPanel();
        msgPanel.setBounds(376, 201, 406, 334);
        scrlPaneMsg = new JScrollPane();
        buttonClear = new JButton();
        buttonClear.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonReset = new JButton();
        buttonReset.setFont(new Font("Verdana", Font.PLAIN, 10));
        buttonQuit = new JButton();
        buttonQuit.setFont(new Font("Verdana", Font.PLAIN, 10));
        
        textboxBinBlk.setDocument(Helper.intFilter());
        textboxBinLen.setDocument(Helper.intFilter());
        textboxValBlk.setDocument(Helper.intFilter());
        textboxValSrc.setDocument(Helper.intFilter());
        textboxValTar.setDocument(Helper.intFilter());

		String[] rdrNameDef = {"Please select reader                   "};

        storeAuthPanel.setBorder(BorderFactory.createTitledBorder("Store Authentication Keys to Device"));
        jLabel1.setText("Key Store No.");
        jLabel2.setText("Key Value Input");
        buttonLoadKey.setText("Load Key");

        GroupLayout gl_keyValPanel = new GroupLayout(keyValPanel);
        gl_keyValPanel.setHorizontalGroup(
        	gl_keyValPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_keyValPanel.createSequentialGroup()
        			.addComponent(textboxKey1, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(textboxKey2, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(textboxKey3, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(textboxKey4, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(textboxKey5, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
        			.addGap(8)
        			.addComponent(textboxKey6, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        gl_keyValPanel.setVerticalGroup(
        	gl_keyValPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_keyValPanel.createParallelGroup(Alignment.BASELINE)
        			.addComponent(textboxKey1, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
        			.addComponent(textboxKey2, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
        			.addComponent(textboxKey3, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
        			.addComponent(textboxKey4, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
        			.addComponent(textboxKey5, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
        			.addComponent(textboxKey6, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
        );
        keyValPanel.setLayout(gl_keyValPanel);

        GroupLayout gl_storeAuthPanel = new GroupLayout(storeAuthPanel);
        gl_storeAuthPanel.setHorizontalGroup(
        	gl_storeAuthPanel.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_storeAuthPanel.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_storeAuthPanel.createParallelGroup(Alignment.TRAILING)
        				.addComponent(buttonLoadKey, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
        				.addGroup(gl_storeAuthPanel.createSequentialGroup()
        					.addGroup(gl_storeAuthPanel.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel2)
        						.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(gl_storeAuthPanel.createParallelGroup(Alignment.LEADING)
        						.addComponent(textboxMemAdd, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
        						.addComponent(keyValPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        			.addGap(19))
        );
        gl_storeAuthPanel.setVerticalGroup(
        	gl_storeAuthPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_storeAuthPanel.createSequentialGroup()
        			.addGroup(gl_storeAuthPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(textboxMemAdd, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel1))
        			.addGroup(gl_storeAuthPanel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_storeAuthPanel.createSequentialGroup()
        					.addGap(12)
        					.addComponent(jLabel2))
        				.addGroup(gl_storeAuthPanel.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(keyValPanel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(buttonLoadKey)
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        storeAuthPanel.setLayout(gl_storeAuthPanel);

        authPanel.setBorder(BorderFactory.createTitledBorder("Authentication Function"));

        jLabel3.setText("Block No. (Dec)");
        jLabel4.setText("Key Store Number");

        keyTypePanel.setBorder(BorderFactory.createTitledBorder("Key Type"));
        bdKeyType.add(radiobuttonKeyA);
        radiobuttonKeyA.setText("Key A");
        bdKeyType.add(radiobuttonKeyB);
        radiobuttonKeyB.setText("Key B");

        GroupLayout gl_keyTypePanel = new GroupLayout(keyTypePanel);
        gl_keyTypePanel.setHorizontalGroup(
        	gl_keyTypePanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_keyTypePanel.createSequentialGroup()
        			.addGap(10)
        			.addGroup(gl_keyTypePanel.createParallelGroup(Alignment.TRAILING)
        				.addComponent(radiobuttonKeyB)
        				.addComponent(radiobuttonKeyA))
        			.addContainerGap(25, Short.MAX_VALUE))
        );
        gl_keyTypePanel.setVerticalGroup(
        	gl_keyTypePanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_keyTypePanel.createSequentialGroup()
        			.addComponent(radiobuttonKeyA)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(radiobuttonKeyB)
        			.addContainerGap(27, Short.MAX_VALUE))
        );
        keyTypePanel.setLayout(gl_keyTypePanel);

        buttonAuthenticate.setText("Authenticate");

        binBlkPanel.setBorder(BorderFactory.createTitledBorder("Binary Block Function"));

        jLabel6.setText("Start Block (Dec)");
        jLabel7.setText("Length (Dec)");
        jLabel8.setText("Data (Text)");
        buttonBinRead.setText("Read Block");
        buttonBinUpdate.setText("Update Block");

        GroupLayout gl_binBlkPanel = new GroupLayout(binBlkPanel);
        gl_binBlkPanel.setHorizontalGroup(
        	gl_binBlkPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_binBlkPanel.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_binBlkPanel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_binBlkPanel.createSequentialGroup()
        					.addGroup(gl_binBlkPanel.createParallelGroup(Alignment.LEADING)
        						.addComponent(textboxBinData, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        						.addGroup(gl_binBlkPanel.createSequentialGroup()
        							.addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(textboxBinBlk, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
        							.addComponent(jLabel7)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(textboxBinLen, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
        						.addComponent(jLabel8))
        					.addContainerGap())
        				.addGroup(Alignment.TRAILING, gl_binBlkPanel.createSequentialGroup()
        					.addComponent(buttonBinRead, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(buttonBinUpdate, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
        					.addGap(50))))
        );
        gl_binBlkPanel.setVerticalGroup(
        	gl_binBlkPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_binBlkPanel.createSequentialGroup()
        			.addGroup(gl_binBlkPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel6)
        				.addComponent(textboxBinBlk, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel7)
        				.addComponent(textboxBinLen, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jLabel8)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(textboxBinData, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(gl_binBlkPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(buttonBinUpdate)
        				.addComponent(buttonBinRead))
        			.addContainerGap(12, Short.MAX_VALUE))
        );
        binBlkPanel.setLayout(gl_binBlkPanel);

        valBlkPanel.setBorder(BorderFactory.createTitledBorder("Value Block Function"));

        jLabel9.setText("Value Amount (Dec)");
        jLabel10.setText("Block No. (Dec)");
        jLabel11.setText("Source Block (Dec)");
        jLabel12.setText("Target Block (Dec)");
        buttonValStore.setText("Store Value");
        buttonValInc.setText("Increment");
        buttonValDec.setText("Decrement");
        buttonValRead.setText("Read Value");
        buttonValRes.setText("Restore Value");

        buttonClear.setText("Clear");
        buttonReset.setText("Reset");
        buttonQuit.setText("Quit");
        
        lblApduLogs = new JLabel("APDU Logs");
        lblApduLogs.setFont(new Font("Verdana", Font.PLAIN, 10));

        GroupLayout gl_msgPanel = new GroupLayout(msgPanel);
        gl_msgPanel.setHorizontalGroup(
        	gl_msgPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_msgPanel.createSequentialGroup()
        			.addGroup(gl_msgPanel.createParallelGroup(Alignment.LEADING)
        				.addGroup(Alignment.TRAILING, gl_msgPanel.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(buttonClear, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(buttonReset, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
        					.addGap(14)
        					.addComponent(buttonQuit, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE))
        				.addGroup(gl_msgPanel.createSequentialGroup()
        					.addGap(14)
        					.addComponent(scrlPaneMsg, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
        				.addGroup(gl_msgPanel.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(lblApduLogs)))
        			.addContainerGap())
        );
        gl_msgPanel.setVerticalGroup(
        	gl_msgPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_msgPanel.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblApduLogs)
        			.addGap(12)
        			.addComponent(scrlPaneMsg, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(gl_msgPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(buttonQuit)
        				.addComponent(buttonClear)
        				.addComponent(buttonReset))
        			.addContainerGap(16, Short.MAX_VALUE))
        );
        mMsg = new JTextArea();
        mMsg.setLineWrap(true);
        mMsg.setEditable(false);
        mMsg.setFont(new Font("Verdana", Font.PLAIN, 10));
        scrlPaneMsg.setViewportView(mMsg);
        
                mMsg.setColumns(20);
                mMsg.setRows(5);
        msgPanel.setLayout(gl_msgPanel);
        buttonReset.setMnemonic(KeyEvent.VK_R);
        buttonQuit.setMnemonic(KeyEvent.VK_Q);
        buttonClear.setMnemonic(KeyEvent.VK_L);
        buttonLoadKey.setMnemonic(KeyEvent.VK_L);
        buttonAuthenticate.setMnemonic(KeyEvent.VK_A);
        buttonBinRead.setMnemonic(KeyEvent.VK_E);
        buttonBinUpdate.setMnemonic(KeyEvent.VK_U);
        buttonValStore.setMnemonic(KeyEvent.VK_S);
        buttonValInc.setMnemonic(KeyEvent.VK_I);
        buttonValDec.setMnemonic(KeyEvent.VK_D);
        buttonValRead.setMnemonic(KeyEvent.VK_E);
        buttonValRes.setMnemonic(KeyEvent.VK_T);
        valBlkPanel.setLayout(null);
        valBlkPanel.add(jLabel9);
        valBlkPanel.add(jLabel10);
        valBlkPanel.add(textboxValAmount);
        valBlkPanel.add(textboxValSrc);
        valBlkPanel.add(textboxValBlk);
        valBlkPanel.add(jLabel11);
        valBlkPanel.add(jLabel12);
        valBlkPanel.add(textboxValTar);
        valBlkPanel.add(buttonValRes);
        valBlkPanel.add(buttonValRead);
        valBlkPanel.add(buttonValDec);
        valBlkPanel.add(buttonValInc);
        valBlkPanel.add(buttonValStore);
        getContentPane().setLayout(null);
        getContentPane().add(authPanel);
        authPanel.setLayout(null);
        authPanel.add(keyTypePanel);
        authPanel.add(buttonAuthenticate);
        authPanel.add(jLabel3);
        authPanel.add(textboxBlockNumber);
        authPanel.add(jLabel4);
        authPanel.add(textboxKeyAdd);
        getContentPane().add(binBlkPanel);
        getContentPane().add(storeAuthPanel);
        getContentPane().add(valBlkPanel);
        getContentPane().add(msgPanel);
        lblReader = new JLabel();
        lblReader.setBounds(10, 27, 77, 14);
        getContentPane().add(lblReader);
        lblReader.setFont(new Font("Verdana", Font.PLAIN, 10));
        
        lblReader.setText("Select Reader");
        comboboxReader = new JComboBox(rdrNameDef);
        comboboxReader.setBounds(91, 24, 281, 20);
        getContentPane().add(comboboxReader);
        comboboxReader.setFont(new Font("Verdana", Font.PLAIN, 10));
        comboboxReader.setModel(new DefaultComboBoxModel(new String[] {" "}));
        comboboxReader.setSelectedIndex(0);
        buttonInitialize = new JButton();
        buttonInitialize.setBounds(253, 55, 119, 23);
        getContentPane().add(buttonInitialize);
        buttonInitialize.setFont(new Font("Verdana", Font.PLAIN, 10));
        
        buttonInitialize.setText("Initialize");
        
                
                buttonInitialize.setMnemonic(KeyEvent.VK_I);
                buttonConnect = new JButton();
                buttonConnect.setBounds(253, 89, 119, 23);
                getContentPane().add(buttonConnect);
                buttonConnect.setFont(new Font("Verdana", Font.PLAIN, 10));
                buttonConnect.setText("Connect");
                buttonConnect.setMnemonic(KeyEvent.VK_C);
                buttonConnect.addActionListener(this);
                
                        buttonInitialize.addActionListener(this);
        buttonReset.addActionListener(this);
        buttonQuit.addActionListener(this);
        buttonClear.addActionListener(this);
        buttonLoadKey.addActionListener(this);
        buttonAuthenticate.addActionListener(this);
        buttonBinRead.addActionListener(this);
        buttonBinUpdate.addActionListener(this);
        buttonValStore.addActionListener(this);
        buttonValInc.addActionListener(this);
        buttonValDec.addActionListener(this);
        buttonValRead.addActionListener(this);
        buttonValRes.addActionListener(this);

        //Add event listener
        textboxMemAdd.addKeyListener(this);
        textboxKey1.addKeyListener(this);
        textboxKey2.addKeyListener(this);
        textboxKey3.addKeyListener(this);
        textboxKey4.addKeyListener(this);
        textboxKey5.addKeyListener(this);
        textboxKey6.addKeyListener(this);
        textboxBlockNumber.addKeyListener(this);
       //textboxBinData.addKeyListener(this);
        textboxKeyAdd.addKeyListener(this);
        textboxBinBlk.addKeyListener(this);
        textboxBinLen.addKeyListener(this);
        textboxValAmount.addKeyListener(this);
        textboxValBlk.addKeyListener(this);
        textboxValSrc.addKeyListener(this);
        textboxValTar.addKeyListener(this);
        
        //Disable copy/paste
        textboxMemAdd.setTransferHandler(null);
        textboxKey1.setTransferHandler(null);
        textboxKey2.setTransferHandler(null);
        textboxKey3.setTransferHandler(null);
        textboxKey4.setTransferHandler(null);
        textboxKey5.setTransferHandler(null);
        textboxKey6.setTransferHandler(null);
        textboxBlockNumber.setTransferHandler(null);
        textboxKeyAdd.setTransferHandler(null);
        textboxBinBlk.setTransferHandler(null);
        textboxBinLen.setTransferHandler(null);
        textboxValAmount.setTransferHandler(null);
        textboxValBlk.setTransferHandler(null);
        textboxValSrc.setTransferHandler(null);
        textboxValTar.setTransferHandler(null);
                
    }

	public void actionPerformed(ActionEvent e) 
	{
		if(buttonInitialize == e.getSource())
		{
			String[] readerList = null;
			
		    try
		    {
			    readerList = readerFunctions.getPcscConnection().listTerminals();					
				if (readerList.length == 0)
				{
					mMsg.append("No PC/SC reader detected");
					return;
				}
				
				comboboxReader.removeAllItems();
					
				for (int i = 0; i < readerList.length; i++)
				{
					if (!readerList.equals(""))	
						comboboxReader.addItem(readerList[i]);
					else
						break;
				}
				
				comboboxReader.setSelectedIndex(0);
				buttonConnect.setEnabled(true);
				
				displayOut(0, 0, "Initialize success\r\n");
			}
		    catch (CardException ex)
		    {		    	
		    	addTitleToLog(PcscProvider.GetScardErrMsg(ex));
		    	showErrorMessage(PcscProvider.GetScardErrMsg(ex));
		    }
		    catch(Exception ex)
		    {
		    	displayOut(0, 0, ex.getMessage().toString());
		    	showErrorMessage(ex.getMessage());		    	
		    }
		} // Init
		
		
		if(buttonConnect == e.getSource())
		{
			try
			{
				if(readerFunctions.getPcscConnection().isConnectionActive())	
					readerFunctions.getPcscConnection().disconnect();
				
				String rdrcon = (String)comboboxReader.getSelectedItem();
				
				readerFunctions.getPcscConnection().connect(rdrcon, "*");
				mifareClassic = new MifareClassic(readerFunctions.getPcscConnection());
				
				displayOut(0, 0, "Successful connection to " + rdrcon);

				currentChipType = readerFunctions.getChipType();
                
				if(currentChipType == ReaderFunctions.CHIP_TYPE.UNKNOWN)
				{
					showErrorMessage("Card is not supported.\nPlease present Mifare Classic card.");
					return;
				}
				else
				{
					String chipType = "";
					if(currentChipType == ReaderFunctions.CHIP_TYPE.MIFARE_1K)
					{
						chipType = "Mifare Standard 1K";
						maxBlocks = 63;		
					}
					else
					{		
						chipType = "Mifare Standard 4K";
						maxBlocks = 255;		
					}
					displayOut(0, 0, "\nChip Type: " + chipType );
				}
				//Store Authentication Keys Controls
				enableLoadPanel(true);
			}
			catch (CardException ex)
			{
				addTitleToLog(PcscProvider.GetScardErrMsg(ex));
				showErrorMessage(PcscProvider.GetScardErrMsg(ex));
			}
			catch (Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
				showErrorMessage(ex.getMessage());
			}
		} // Connect
		
		
		if(buttonClear == e.getSource())
		{			
			mMsg.setText("");			
		} // Clear
		
		if(buttonReset == e.getSource())
		{
			try			
			{
				//disconnect
				if (readerFunctions.getPcscConnection().isConnectionActive())
					readerFunctions.getPcscConnection().disconnect();
				
				resetFields();
				mMsg.setText("");
				comboboxReader.removeAllItems();
				displayOut(0, 0, "Program Ready\r\n");
			}
			catch (Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
				showErrorMessage(ex.getMessage());
			}			
		} // Reset
		
		if(buttonQuit == e.getSource())
		{			
			this.dispose();			
		} // Quit
		
		if(buttonLoadKey == e.getSource())
		{
			
			byte[] key = new byte[6];
			byte keyNumber;
			
			ReaderFunctions.KEY_STRUCTURE keyStructure=ReaderFunctions.KEY_STRUCTURE.VOLATILE;
			
			try
			{	
				if (!(textboxMemAdd.getText().matches("[0-9A-Fa-f]+")))
				{
					showErrorMessage("Please key-in Key Store Number from 00 to 01.");
					textboxMemAdd.requestFocus();
	                return;
				}
				
				keyNumber = (byte)((Integer)Integer.parseInt(textboxMemAdd.getText(), 16)).byteValue();
                
                if(keyNumber > (byte) 0x01 || keyNumber < (byte) 0x00)
                {
                	showErrorMessage("Please key-in Key Store Number from 00 to 01.");
                    textboxMemAdd.requestFocus();
                    return;
                }
				
                if (!validateKeys())
                	return;
                
                key[0] = (byte)((Integer)Integer.parseInt(textboxKey1.getText(), 16)).byteValue();
                key[1] = (byte)((Integer)Integer.parseInt(textboxKey2.getText(), 16)).byteValue();
                key[2] = (byte)((Integer)Integer.parseInt(textboxKey3.getText(), 16)).byteValue();
                key[3] = (byte)((Integer)Integer.parseInt(textboxKey4.getText(), 16)).byteValue();
                key[4] = (byte)((Integer)Integer.parseInt(textboxKey5.getText(), 16)).byteValue();
                key[5] = (byte)((Integer)Integer.parseInt(textboxKey6.getText(), 16)).byteValue();				

                addTitleToLog("Load Authentication Key");
                
                if(readerFunctions.loadAuthKey(keyStructure, keyNumber, key) == false)  
                    showErrorMessage("Load key failed");
                else
                	addMessageToLog("Load Key success");
                
				//Authenticate Keys Controls
                enableAuthenticatePanel(true);			
			}
			catch (CardException ex)
			{
				addTitleToLog(PcscProvider.GetScardErrMsg(ex));
				showErrorMessage(PcscProvider.GetScardErrMsg(ex));
			}
			catch(Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
				showErrorMessage(ex.getMessage());			
			}		
		} // Load Key
		
		
		if(buttonAuthenticate == e.getSource())
		{	
			byte blockNumber;
			byte keyNumber = 0x20;
			ReaderFunctions.KEYTYPES keyType = ReaderFunctions.KEYTYPES.ACR1281_KEYTYPE_A;
			
			try
			{
				//validate input
				if(textboxBlockNumber.getText().equals(""))
				{				
					showErrorMessage("Please key-in numeric value for Block Number.");
					textboxBlockNumber.requestFocus();
					return;				
				}
				
				if(textboxKeyAdd.getText().equals(""))
				{
					showErrorMessage("Please key-in store number from 00 to 01.");
					textboxKeyAdd.requestFocus();
					return;	
				}

                keyNumber = (byte)((Integer)Integer.parseInt(textboxKeyAdd.getText(), 16)).byteValue();
                
                if(keyNumber > (byte) 0x01 || keyNumber < (byte) 0x00)
                {
                	showErrorMessage("Please key-in Key Store Number from 00 to 01.");
					textboxKeyAdd.requestFocus();
                    return;
                }
                
                if(Integer.parseInt(textboxBlockNumber.getText()) > maxBlocks)
                {
                	showErrorMessage("Please key-in valid Block Number. Valid value: 0 - " + maxBlocks + ".");
                	textboxBlockNumber.requestFocus();
                	return;
                }
                
				if(radiobuttonKeyB.isSelected())
                                    keyType = ReaderFunctions.KEYTYPES.ACR1281_KEYTYPE_B;
				
				//blockNumber = (byte)(Integer)Integer.parseInt(textboxBlockNumber.getText(), 16)).byteValue(); 
				
                blockNumber = Byte.parseByte(textboxBlockNumber.getText());
                                
               addTitleToLog("Authenticate Key");
                                
				if(readerFunctions.authenticate(blockNumber, keyType, keyNumber) == false)
				{
					enableBlockFunctionPanel(false);
					showErrorMessage("Authenticate failed");
				}
				else
				{
					enableBlockFunctionPanel(true);
					addMessageToLog("Authenticate success");
				}
			}
			catch (CardException ex)
			{
				addTitleToLog(PcscProvider.GetScardErrMsg(ex));
				showErrorMessage(PcscProvider.GetScardErrMsg(ex));
			}
			catch(Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());				
				showErrorMessage(ex.getMessage());
			}
		}  // Authenticate
		
		if(buttonBinRead == e.getSource())
		{		
			int blockNumber;
			int length;
			byte[] tempStr;
			
			try
			{	
				//validate input				
				if(textboxBinBlk.getText().equals(""))
				{	
					showErrorMessage("Please key-in numeric value for Start Block.");
					textboxBinBlk.requestFocus();
					return;
				}

                if(Integer.parseInt(textboxBinBlk.getText()) > maxBlocks)
                {
                	showErrorMessage("Please key-in valid Start Block. Valid value: 0 - " + maxBlocks + ".");
                	textboxBinBlk.requestFocus();
                	return;
                }
                
                if(textboxBinLen.getText().equals(""))
				{			
					showErrorMessage("Please key-in numeric value for Length.");
					textboxBinLen.requestFocus();
					return;
				}
                
                length = Integer.parseInt(textboxBinLen.getText());
                
                if (length <= 0 || length > 16)
                {
                	showErrorMessage("Invalid Length. Valid value: 1 - 16.");
					textboxBinLen.requestFocus();
					return;
                }

				textboxBinData.setText("");				
				
				blockNumber = Integer.parseInt(textboxBinBlk.getText());
											
				addTitleToLog("Read Binary");
				
				tempStr = mifareClassic.readBinaryBlock((byte)blockNumber, (byte)length);
				
				textboxBinData.setText(Helper.byteArrayToString(tempStr, (byte) length));
				addMessageToLog("Read success");	
			}
			catch (CardException ex)
			{
				addTitleToLog(PcscProvider.GetScardErrMsg(ex));
				showErrorMessage(PcscProvider.GetScardErrMsg(ex));
			}
			catch(Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
				showErrorMessage(ex.getMessage());
			}			
		}  // Read
		
		if(buttonBinUpdate == e.getSource())
		{		
			int blockNumber;
			int length;
			
			try
			{
				String tmpStr="";
				
				//validate input				
				if(textboxBinBlk.getText().equals(""))
				{	
					showErrorMessage("Please key-in numeric value for Start Block");
					textboxBinBlk.requestFocus();
					return;
				}
				
				if(Integer.parseInt(textboxBinBlk.getText()) > maxBlocks)
                {
                	showErrorMessage("Please key-in valid Start Block. Valid value: 0 - " + maxBlocks + ".");
                	textboxBinBlk.requestFocus();
                	return;
                }
	
				if(textboxBinLen.getText().equals(""))
				{			
					showErrorMessage("Please key-in numeric value for Length");
					textboxBinLen.requestFocus();
					return;
				}
				
				if((Integer.parseInt(textboxBinLen.getText())) != 16 || Integer.parseInt(textboxBinLen.getText()) == 0)
				{
					textboxBinLen.requestFocus();
					showErrorMessage("Invalid Length. Length must be 16.");
					return;
				}
				
				if(textboxBinData.getText().equals(""))
				{
					showErrorMessage("Please key-in data to write.");
					textboxBinData.requestFocus();
					return;
				}
				
				if(Integer.parseInt(textboxBinBlk.getText()) <= 127)
                {
					if((Integer.parseInt(textboxBinBlk.getText()) + 1) % 4 == 0)
					{
	                	showErrorMessage("The block to be updated is a sector trailer. Please change the start block number.");
	                	textboxBinBlk.requestFocus();
	                	return;
					}
                }
				
				if(Integer.parseInt(textboxBinBlk.getText()) > 127)
                {
					if((Integer.parseInt(textboxBinBlk.getText()) + 1) % 16 == 0)
					{
	                	showErrorMessage("The block to be updated is a sector trailer. Please change the start block number.");
	                	textboxBinBlk.requestFocus();
	                	return;
					}
                }
		
				blockNumber = Integer.parseInt(textboxBinBlk.getText());
				length = Integer.parseInt(textboxBinLen.getText());                           
                                
                if (blockNumber < 127) {
                    if (((blockNumber + 1) % 4 == 0) || ((blockNumber + 1) % 16 == 0)){
                        int confirmationRes;
                        confirmationRes = JOptionPane.showConfirmDialog(this,"Block Number is a sector trailer!","Sector Trailer Notification",YES_NO_OPTION);
                        if (confirmationRes == JOptionPane.NO_OPTION) {
                            return;
                        }                                        
                    } 
                }
                                
				byte[] buff = new byte[length];
				
				tmpStr = textboxBinData.getText();
				
				for (int i = 0; i < tmpStr.length(); i++)
					buff[i] = (byte)tmpStr.charAt(i);	
				
				addTitleToLog("Update Binary");
				
				mifareClassic.updateBinaryBlock((byte)blockNumber, buff, (byte)length);
				
				addMessageToLog("Update success");	
				
				textboxBinData.setText("");
			}
			catch (CardException ex)
			{
				addTitleToLog(PcscProvider.GetScardErrMsg(ex));
				showErrorMessage(PcscProvider.GetScardErrMsg(ex));
			}
			catch(Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
				showErrorMessage(ex.getMessage());
			}			
		}  // Update
		
		if(buttonValStore==e.getSource())
		{	
			int amount;
			int blockNumber;
			
			try
			{			
				//validate input
				if (textboxValAmount.getText().equals(""))
				{
					showErrorMessage("Please key-in numeric value for Amount.");
					textboxValAmount.requestFocus();
					return;
				}
			
				if(Integer.parseInt(textboxValAmount.getText()) <= 0 || Integer.parseInt(textboxValAmount.getText()) > 2147483647)
				{				
					showErrorMessage("Please key-in valid Amount. Valid value: 1 - 2,147,483,647.");
					textboxValAmount.requestFocus();
					return;				
				}
				
				if(textboxValBlk.getText().equals(""))
				{
					textboxValBlk.requestFocus();
					return;				
				}

				if(Integer.parseInt(textboxValBlk.getText()) > maxBlocks)
                {
                	showErrorMessage("Please key-in valid Block Number. Valid value: 0 - " + maxBlocks + ".");
                	textboxValBlk.requestFocus();
                	return;
                }
				
				blockNumber = Integer.parseInt(textboxValBlk.getText());
				amount = Integer.parseInt(textboxValAmount.getText());	
				
				addTitleToLog("Store Value");
				
				mifareClassic.store((byte)blockNumber, amount);	
				addMessageToLog("Store Value success");
			}
			catch (CardException ex)
			{
				addTitleToLog(PcscProvider.GetScardErrMsg(ex));
				showErrorMessage(PcscProvider.GetScardErrMsg(ex));
			}
			catch(Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
				showErrorMessage(ex.getMessage());
			}
		} // Store Value
		
		if (buttonValInc == e.getSource())
		{	
			try
			{
				int amount;
				
				//validate input
				if (textboxValAmount.getText().equals(""))
				{
					showErrorMessage("Please key-in numeric value for Amount.");
					textboxValAmount.requestFocus();
					return;
				}
		
				if(Integer.parseInt(textboxValAmount.getText()) <= 0 || Integer.parseInt(textboxValAmount.getText()) > 2147483647)
				{				
					showErrorMessage("Please key-in valid Amount. Valid value: 1 - 2,147,483,647.");
					textboxValAmount.requestFocus();
					return;				
				}
				
				if(textboxValBlk.getText().equals(""))
				{
					textboxValBlk.requestFocus();
					return;
				}

				if(Integer.parseInt(textboxValBlk.getText()) > maxBlocks)
                {
                	showErrorMessage("Please key-in valid Block Number. Valid value: 0 - " + maxBlocks + ".");
                	textboxValBlk.requestFocus();
                	return;
                }
				
				addTitleToLog("Increment");
				
				amount = Integer.parseInt(textboxValAmount.getText());
				
				mifareClassic.increment((byte)Integer.parseInt(textboxValBlk.getText()), amount);	
				addMessageToLog("Increment success");
			}
			catch (CardException ex)
			{
				addTitleToLog(PcscProvider.GetScardErrMsg(ex));
				showErrorMessage(PcscProvider.GetScardErrMsg(ex));
			}
			catch(Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
				showErrorMessage(ex.getMessage());
			}	
		}  // Increment Value
		
		if(buttonValDec == e.getSource())
		{	
			try
			{
				int amount;
				
				//validate input
				if (textboxValAmount.getText().equals(""))
				{
					showErrorMessage("Please key-in numeric value for Amount.");
					textboxValAmount.requestFocus();
					return;
				}
	
				if(Integer.parseInt(textboxValAmount.getText()) <= 0 || Integer.parseInt(textboxValAmount.getText()) > 2147483647)
				{				
					showErrorMessage("Please key-in valid Amount. Valid value: 1 - 2,147,483,647.");
					textboxValAmount.requestFocus();
					return;				
				}
				
				if(textboxValBlk.getText().equals(""))
				{
					textboxValBlk.requestFocus();
					return;
				}

				if(Integer.parseInt(textboxValBlk.getText()) > maxBlocks)
                {
                	showErrorMessage("Please key-in valid Block Number. Valid value: 0 - " + maxBlocks + ".");
                	textboxValBlk.requestFocus();
                	return;
                }
				
				addTitleToLog("Decrement");
				
				amount = Integer.parseInt(textboxValAmount.getText());
				
				mifareClassic.decrement((byte)Integer.parseInt(textboxValBlk.getText()), amount);
				addMessageToLog("Decrement success");
			}
			catch (CardException ex)
			{
				addTitleToLog(PcscProvider.GetScardErrMsg(ex));
				showErrorMessage(PcscProvider.GetScardErrMsg(ex));
			}
			catch(Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
				showErrorMessage(ex.getMessage());
			}			
		}  // Decrement Value
		
		if(buttonValRead == e.getSource())
		{	
			try
			{
				int amount = 0;
				
				//validate input
				if(textboxValBlk.getText().equals(""))
				{	
					showErrorMessage("Please key-in numeric value for Block Number");
					textboxValBlk.requestFocus();
					return;
				}

				if(Integer.parseInt(textboxValBlk.getText()) > maxBlocks)
                {
                	showErrorMessage("Please key-in valid Block Number. Valid value: 0 - " + maxBlocks + ".");
                	textboxValBlk.requestFocus();
                	return;
                }
				
				addTitleToLog("Read Value");
				
				amount = mifareClassic.inquireAmount((byte)Integer.parseInt(textboxValBlk.getText()));	
				
				textboxValAmount.setText("" + amount);
				addMessageToLog("Read Value success");
			}
			catch (CardException ex)
			{
				addTitleToLog(PcscProvider.GetScardErrMsg(ex));
				showErrorMessage(PcscProvider.GetScardErrMsg(ex));
			}
			catch(Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
				showErrorMessage(ex.getMessage());
			}
			
		} // Read Value
		
		if(buttonValRes == e.getSource())
		{	
			try
			{
				//validate input
				if(textboxValSrc.getText().equals(""))
				{				
					showErrorMessage("Please key-in numeric value for Source Block");
					textboxValSrc.requestFocus();
					return;				
				}
				
				if(textboxValTar.getText().equals(""))
				{	
					showErrorMessage("Please key-in numeric value for Target Block");
					textboxValTar.requestFocus();
					return;				
				}

				if(Integer.parseInt(textboxValSrc.getText()) > maxBlocks)
                {
                	showErrorMessage("Please key-in valid Source Block. Valid value: 0 - " + maxBlocks + ".");
                	textboxValSrc.requestFocus();
                	return;
                }
				
				if(Integer.parseInt(textboxValTar.getText()) > maxBlocks)
                {
                	showErrorMessage("Please key-in valid Target Block. Valid value: 0 - " + maxBlocks + ".");
                	textboxValTar.requestFocus();
                	return;
                }
				
				addTitleToLog("Restore Value");
				mifareClassic.restoreAmount((byte) Integer.parseInt(textboxValSrc.getText()), (byte) Integer.parseInt(textboxValTar.getText()));
				addMessageToLog("Restore Value success");
			}
			catch (CardException ex)
			{
				addTitleToLog(PcscProvider.GetScardErrMsg(ex));
				showErrorMessage(PcscProvider.GetScardErrMsg(ex));
			}
			catch(Exception ex)
			{
				displayOut(0, 0, ex.getMessage().toString());
				showErrorMessage(ex.getMessage());
			}
		}  // Restore Value
			
	}
	
	
	public void displayOut(int mType, int msgCode, String printText)
	{
		switch(mType)
		{			
			case 2: mMsg.append("<< " + printText + "\n"); break;
			case 3: mMsg.append(">> " + printText + "\n"); break;
			default: mMsg.append(printText + "\n");
		}		
	}
	
	void addTitleToLog(String message)
	{
		mMsg.append("\r\n" + message + "\r\n");
	}
	
	void addMessageToLog(String message)
	{
		mMsg.append(message + "\r\n");
	}
	
	private boolean validateKeys()
	{
		if(textboxMemAdd.getText().equals(""))
        {
        	showErrorMessage("Please key-in Key Store Number from 00 to 01.");
            textboxMemAdd.requestFocus();
            return false;
        }

        if(textboxKey1.getText().equals(""))
        {
        	showErrorMessage("Please key-in hex value for Key Value.");
        	textboxKey1.requestFocus();
        	return false;				
        }
        
        if(textboxKey1.getText().length() != 2)
        {
        	showErrorMessage("Please key-in hex value for Key Value.");
        	textboxKey1.requestFocus();
        	return false;				
        }

        if(textboxKey2.getText().equals(""))
        {
        	showErrorMessage("Please key-in hex value for Key Value.");
        	textboxKey2.requestFocus();
        	return false;				
        }
        
        if(textboxKey2.getText().length() != 2)
        {
        	showErrorMessage("Please key-in hex value for Key Value.");
        	textboxKey2.requestFocus();
        	return false;				
        }

        if(textboxKey3.getText().equals(""))
        {	
        	showErrorMessage("Please key-in hex value for Key Value.");
            textboxKey3.requestFocus();
            return false;				
        }
        
        if(textboxKey3.getText().length() != 2)
        {
        	showErrorMessage("Please key-in hex value for Key Value.");
        	textboxKey3.requestFocus();
        	return false;				
        }

        if(textboxKey4.getText().equals(""))
        {
        	showErrorMessage("Please key-in hex value for Key Value.");
            textboxKey4.requestFocus();
            return false;
        }
        
        if(textboxKey4.getText().length() != 2)
        {
        	showErrorMessage("Please key-in hex value for Key Value.");
        	textboxKey4.requestFocus();
        	return false;				
        }

        if(textboxKey5.getText().equals(""))
        {
        	showErrorMessage("Please key-in hex value for Key Value.");
            textboxKey5.requestFocus();
            return false;
        }
        
        if(textboxKey5.getText().length() != 2)
        {
        	showErrorMessage("Please key-in hex value for Key Value.");
        	textboxKey5.requestFocus();
        	return false;				
        }

        if(textboxKey6.getText().equals(""))
        {
        	showErrorMessage("Please key-in hex value for Key Value.");
            textboxKey6.requestFocus();
            return false;				
        }	
        
        if(textboxKey6.getText().length() != 2)
        {
        	showErrorMessage("Please key-in hex value for Key Value.");
        	textboxKey6.requestFocus();
        	return false;				
        }
        
		return true;
	}
	
	private void resetFields()
	{
		enablePanels(false);
		
		
		buttonConnect.setEnabled(false);
		buttonInitialize.setEnabled(true);		
		buttonReset.setEnabled(true);
	
		mMsg.setText("");
		
		textboxMemAdd.setText("");
		textboxKey1.setText("");
		textboxKey2.setText("");
		textboxKey3.setText("");
		textboxKey4.setText("");
		textboxKey5.setText("");
		textboxKey6.setText("");
		
		textboxBlockNumber.setText("");
		textboxKeyAdd.setText("");
		radiobuttonKeyA.setSelected(true);
		
		textboxBinBlk.setText("");
		textboxBinLen.setText("");
		textboxBinData.setText("");
		
		displayOut(0, 0, "Program Ready\r\n");
		textboxValAmount.setText("");
		textboxValBlk.setText("");
		textboxValSrc.setText("");
		textboxValTar.setText("");
	}
	
	private void enablePanels(boolean isEnable)
	{
		enableLoadPanel(isEnable);
		enableAuthenticatePanel(isEnable);
		enableBlockFunctionPanel(isEnable);
	}
	
	private void enableLoadPanel(boolean isEnable)
	{
		storeAuthPanel.setEnabled(isEnable);
		
		for (Component controls : storeAuthPanel.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		textboxKey1.setEnabled(isEnable);
		textboxKey2.setEnabled(isEnable);
		textboxKey3.setEnabled(isEnable);
		textboxKey4.setEnabled(isEnable);
		textboxKey5.setEnabled(isEnable);
		textboxKey6.setEnabled(isEnable);
	}
	
	private void enableAuthenticatePanel(boolean isEnable)
	{
		authPanel.setEnabled(isEnable);
		
		for (Component controls : authPanel.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		radiobuttonKeyA.setEnabled(isEnable);
		radiobuttonKeyB.setEnabled(isEnable);
	}
	
	private void enableBlockFunctionPanel(boolean isEnable)
	{
		binBlkPanel.setEnabled(isEnable);
		valBlkPanel.setEnabled(isEnable);
		
		for (Component controls : binBlkPanel.getComponents())
		{
			controls.setEnabled(isEnable);
		}
		
		for (Component controls : valBlkPanel.getComponents())
		{
			controls.setEnabled(isEnable);
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
  		if(textboxMemAdd.isFocusOwner() || textboxBlockNumber.isFocusOwner() || textboxBinBlk.isFocusOwner() ||
  				textboxBinLen.isFocusOwner() || textboxValBlk.isFocusOwner() || textboxValSrc.isFocusOwner() ||
  				textboxValTar.isFocusOwner() || textboxKeyAdd.isFocusOwner() || textboxValAmount.isFocusOwner())
  		{	
  			if (VALIDCHARS.indexOf(x) == -1 ) 
  				ke.setKeyChar(empty);
  		}
  		else
  		{	
  			if (VALIDCHARSHEX.indexOf(x) == -1 ) 
  				ke.setKeyChar(empty);  			
  		}
  					  
		//Limit character length
  		if(textboxBlockNumber.isFocusOwner() || textboxBinBlk.isFocusOwner() || textboxValBlk.isFocusOwner() || 
                        textboxValSrc.isFocusOwner() || textboxValTar.isFocusOwner())
  		{	
  			if (((JTextField)ke.getSource()).getText().length() >= 3 ) 
  			{
  				ke.setKeyChar(empty);	
  				return;
  			}  			
  		}
  		else if(textboxValAmount.isFocusOwner())
  		{	
  			if (((JTextField)ke.getSource()).getText().length() >= 10 ) 
  			{		
  				ke.setKeyChar(empty);	
  				return;  				
  			}  			
  		}
                else if (textboxBinData.isFocusOwner())
                {
                    if (((JTextField)ke.getSource()).getText().length() >= 16)
                    {
                        ke.setKeyChar(empty);
                        return;
                    }
                }
  		else
  		{  			
  			if (((JTextField)ke.getSource()).getText().length() >= 2 ) 
  			{		
  				ke.setKeyChar(empty);	
  				return;  				
  			}  			
  		}		
	}
	
	
	public void onSendCommand(ReaderEvents.TransmitApduEventArg event) 
	{
		displayOut(2, 0, event.getAsString(true));
	}

	public void onReceiveCommand(ReaderEvents.TransmitApduEventArg event) 
	{
		displayOut(3, 0, event.getAsString(true));
	}
	
	void showErrorMessage(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
    
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MifareCardProgramming().setVisible(true);
            }
        });
    }



}
