package pSmart;
//===========================================================================================
// 
//  Author          : Teosseth G. Altar
// 
//  File            : PcscReader.java
// 
//  Copyright (C)   : Advanced Card Systems Ltd.
// 
//  Description     : Base class for all the PC/SC smart card readers.
//				      Wrapper class for the use of Java Smartcard IO class libraries
// 
//  Date            : October 28, 2011
// 
//  Revision Trail : [Author] / [Date of modification] / [Details of Modifications done]
// 
// 
//=========================================================================================

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class PcscReader 
{
	protected TerminalFactory _terminalFactory;
	protected List<CardTerminal> _cardTerminalList;
	protected CardTerminal _activeTerminal;
	protected Card _card;
	protected CardChannel _cardChannel;
	protected CommandAPDU _commandApdu;
	protected ResponseAPDU _responseApdu;
	protected byte[] _controlCommand, _controlResponse;
	protected String _preferredProtocol;

	protected int _controlCode;
	protected boolean _connectionActive;
	private int returnCode;

	protected ReaderEvents _eventHandler;

	// Default constructor
	public PcscReader()
	{
		setTerminalFactory(TerminalFactory.getDefault());
		setPreferredProtocol("*");
		setConnectionActive(false);
		
		// Set a system property for the protocol of the card so that "GET RESPONSE" should be called manually
		System.setProperty("sun.security.smartcardio.t0GetResponse", "false");
		System.setProperty("sun.security.smartcardio.t1GetResponse", "false");
	}

	public TerminalFactory getTerminalFactory() { return this._terminalFactory;	}
	public void setTerminalFactory(TerminalFactory terminalFactory) { this._terminalFactory = terminalFactory; }

	public List<CardTerminal> getCardTerminalList() { return this._cardTerminalList; }
	public void setCardTerminalList(List<CardTerminal> cardTerminalList) { this._cardTerminalList = cardTerminalList; }

	public CardTerminal getCardTerminal(int index) { return this._cardTerminalList.get(index); }

	public Card getCard() { return this._card; }
	public void setCard(Card card) { this._card = card; }

	public CardChannel getCardChannel() { return this._cardChannel; }
	public void setCardChannel(CardChannel cardChannel) { this._cardChannel = cardChannel; }

	public CommandAPDU getCommandApdu() { return this._commandApdu; }
	public void setCommandApdu(CommandAPDU commandApdu) { this._commandApdu = commandApdu; }

	public ResponseAPDU getResponseApdu() { return this._responseApdu; }
	public void setResponseApdu(ResponseAPDU responseApdu) { this._responseApdu = responseApdu; }

	public void setControlCommand(byte[] controlCommand) { this._controlCommand = controlCommand; }
	public byte[] getControlCommand() { return this._controlCommand; }

	public void setControlResponse(byte[] controlResponse) { this._controlResponse = controlResponse; }
	public byte[] getControlResponse() { return this._controlResponse; }

	public void setControlCode(int controlCode)	{ this._controlCode = controlCode; }
	public int getControlCode() { return this._controlCode; }

	public void setActiveTerminal(CardTerminal activeTerminal) { this._activeTerminal = activeTerminal; }
	public CardTerminal getActiveTerminal() { return this._activeTerminal; }

	public void setPreferredProtocol(String preferredProtocol) { this._preferredProtocol = preferredProtocol; }
	public String getPreferredProtocol() { return this._preferredProtocol; }

	public ReaderEvents getEventHandler() { return this._eventHandler; }
	public void setEventHandler(ReaderEvents eventHandler) { this._eventHandler = eventHandler; }

	public boolean isConnectionActive() { return this._connectionActive; }
	public void setConnectionActive(boolean connectionActive) { this._connectionActive = connectionActive; }

	// List the available smart card readers
	public String[] listTerminals() throws Exception
	{
		try
		{
			setCardTerminalList(getTerminalFactory().terminals().list());
		}
		catch(CardException ex)
		{
			if(ex.getCause().getMessage().equals(PcscProvider.CODES.SCARD_E_SERVICE_STOPPED.toString()))
			{
				establishContext();
				setCardTerminalList(getTerminalFactory().terminals().list());
			}
			else
			{
				throw ex;
			}
		}
		
		String[] terminals = new String[getCardTerminalList().size()]; 

		for (int i = 0; i < getCardTerminalList().size(); i++)
			terminals[i] = getCardTerminalList().get(i).getName(); 

		return terminals;
	}

	// Connect to the smart card through the specified smart card reader (overloaded function)
	public int connect(int terminalNumber, String preferredProtocol) throws Exception
	{
		setActiveTerminal(getCardTerminalList().get(terminalNumber));
		setPreferredProtocol(preferredProtocol);

		return connect();
	}
	
	// Connect to the smart card through the specified smart card reader (overloaded function)
	public int connect(String readerName, String preferredProtocol) throws Exception
	{
		
		for (int i = 0; i < getCardTerminalList().size(); i++)
		{
			if (getCardTerminalList().get(i).getName().indexOf(readerName) > -1)
			{
				setActiveTerminal(getCardTerminalList().get(i));
				setPreferredProtocol(preferredProtocol);
				break;
			}
		}

		return connect();
	}

	// Connect to the smart card through the specified smart card reader (overloaded function)
	public int connect(int terminalNumber) throws Exception
	{
		setActiveTerminal(getCardTerminalList().get(terminalNumber));

		return connect();
	}

	// Connect to the smart card through the specified smart card reader
	public int connect() throws Exception
	{
		try
		{
			setCard(getActiveTerminal().connect(getPreferredProtocol()));
			setCardChannel(getCard().getBasicChannel());
		}
		catch(CardException ex)
		{
			if(ex.getCause().getMessage().equals(PcscProvider.CODES.SCARD_E_SERVICE_STOPPED.toString()))
			{
				establishContext();
				
				String previousSelectedTerminal = getActiveTerminal().getName();
				List<CardTerminal> previousCardTerminals = getCardTerminalList();

				String[] newTerminals = listTerminals();
				
				int terminalIndex = -1;
				for(int i = 0; i < newTerminals.length; i++)
				{
					if(newTerminals[i].equals(previousSelectedTerminal))
					{
						terminalIndex = i;							
						break;
					}
				}
				
				if(terminalIndex == -1)
				{
					setCardTerminalList(previousCardTerminals);
					throw new PcscException(PcscProvider.CODES.SCARD_E_UNKNOWN_READER);
				}
				
				setActiveTerminal(getCardTerminalList().get(terminalIndex));
				setCard(getActiveTerminal().connect(getPreferredProtocol()));
				setCardChannel(getCard().getBasicChannel());
			}
			else
			{
				throw ex;
			}
		}
		
		setConnectionActive(true);
		
		return 0;
	}

	// Connect directly to the smart card reader	
	public int connectDirect(int terminalNumber, boolean isSetTerminalNumber) throws Exception
	{
		try
		{
			if(isSetTerminalNumber)
				setActiveTerminal(getCardTerminalList().get(terminalNumber));
			
			setCard(getActiveTerminal().connect("direct"));
			setConnectionActive(true);
		}
		catch(CardException ex)
		{			
			if(ex.getCause().getMessage().equals(PcscProvider.CODES.SCARD_E_SERVICE_STOPPED.toString()))
			{
				establishContext();
				
				String previousSelectedTerminal = getActiveTerminal().getName();
				List<CardTerminal> previousCardTerminals = getCardTerminalList();

				String[] newTerminals = listTerminals();
				
				int terminalIndex = -1;
				if(isSetTerminalNumber)
				{					
					for(int i = 0; i < newTerminals.length; i++)
					{
						if(newTerminals[i].equals(previousSelectedTerminal))
						{
							terminalIndex = i;							
							break;
						}
					}
				}
				if(terminalIndex == -1)
				{
					setCardTerminalList(previousCardTerminals);
					throw new PcscException(PcscProvider.CODES.SCARD_E_UNKNOWN_READER);
				}
					
				setActiveTerminal(getCardTerminalList().get(terminalIndex));
				setCard(getActiveTerminal().connect("direct"));
				setConnectionActive(true);
			}
			else
			{
				throw ex;
			}
		}
		
		return 0;		
	}

	// Disconnect from the smart card
	public int disconnect() throws Exception
	{
		//The disconnect method of Card.java has a reset parameter which is used as follows:		
		//SCardDisconnect(cardId, (reset ? SCARD_LEAVE_CARD : SCARD_RESET_CARD));
		//So if reset is true, the card is not being reset, if false, the card is being reset.
		getCard().disconnect(false);
		
		setConnectionActive(false);

		return returnCode;
	}
	
	// Send APDU commands to the smart card (overloaded function)
	public int sendApduCommand(Apdu apdu) throws Exception
	{
		int sendApduCommand = 0;
		int sendDataLength = 0;	
		if (apdu.getSendData() != null)
			sendDataLength = apdu.getSendData().length; 
		
		byte[] commandApdu = new byte[5 + sendDataLength];
		
		System.arraycopy(new byte[] { apdu.getCla(), apdu.getIns(), apdu.getP1(), apdu.getP2(), apdu.getP3() }, 0, commandApdu, 0, 5);
		
		if (sendDataLength > 0)
			System.arraycopy(apdu.getSendData(), 0, commandApdu, 5, apdu.getSendData().length);

		try
		{
			sendApduCommand = sendApduCommand(commandApdu);
			
			apdu.setSw(new byte[] { (byte) getResponseApdu().getSW1(), (byte) getResponseApdu().getSW2() });
			if (getResponseApdu().getData().length > 0)
				apdu.setReceiveData(getResponseApdu().getData());
		} 
		catch(CardException ex)
		{
			if(ex.getCause().getMessage().equals(PcscProvider.CODES.SCARD_E_SERVICE_STOPPED.toString()))
			{
				establishContext();	
				
				sendApduCommand = sendApduCommand(commandApdu);
				
				apdu.setSw(new byte[] { (byte) getResponseApdu().getSW1(), (byte) getResponseApdu().getSW2() });
				
				if (getResponseApdu().getData().length > 0)
					apdu.setReceiveData(getResponseApdu().getData());
			}
			else
			{
				ex.printStackTrace();
				throw ex;

			}
		}

		return sendApduCommand;
	}
	

	// Send APDU commands to the smart card (overloaded function)
	public int sendApduCommand(byte[] apdu) throws Exception
	{
		setCommandApdu(new CommandAPDU(apdu));

		return sendApduCommand();
	}

	// Send APDU commands to the smart card
	public int sendApduCommand() throws Exception
	{
		try
		{
			getEventHandler().sendCommandData(getCommandApdu().getBytes());
			setResponseApdu(getCardChannel().transmit(getCommandApdu()));
			getEventHandler().receiveCommandData(getResponseApdu().getBytes());
		}
		catch(CardException ex)
		{
			if(ex.getCause().getMessage().equals(PcscProvider.CODES.SCARD_E_SERVICE_STOPPED.toString()))
			{
				establishContext();

				getEventHandler().sendCommandData(getCommandApdu().getBytes());
				setResponseApdu(getCardChannel().transmit(getCommandApdu()));
				getEventHandler().receiveCommandData(getResponseApdu().getBytes());
			}
			else
			{
				throw ex;
			}			
		}
		return returnCode;
	}

	// Send direct control commands to the smart card reader (overloaded function)
	public int sendControlCommand(int controlCode, byte[] controlCommand) throws Exception
	{
		setControlCode(controlCode);
		setControlCommand(controlCommand);

		return sendControlCommand();
	}

	// Send direct control commands to the smart card reader (overloaded function)
	public int sendControlCommand(byte[] controlCommand) throws Exception
	{
		setControlCommand(controlCommand);

		return sendControlCommand();
	}

	// Send direct control commands to the smart card reader 
	public int sendControlCommand() throws Exception
	{
		try
		{	
			getEventHandler().sendCommandData(getControlCommand());
			setControlResponse(getCard().transmitControlCommand(getControlCode(), getControlCommand()));
			getEventHandler().receiveCommandData(getControlResponse());
		}
		catch(CardException ex)
		{
			if(ex.getCause().getMessage().equals(PcscProvider.CODES.SCARD_E_SERVICE_STOPPED.toString()))
			{
				establishContext();

				getEventHandler().sendCommandData(getControlCommand());
				setControlResponse(getCard().transmitControlCommand(getControlCode(), getControlCommand()));
				getEventHandler().receiveCommandData(getControlResponse());
			}
			else
			{
				throw ex;
			}		
			
		}
		return returnCode;
	}
	
	// Get the ATR of the smart card	
	public byte[] getAtr() throws Exception
	{		
		return getCard().getATR().getBytes();
	}
	
	// Get the protocol in use of the card
	public String getCardProtocol() throws Exception
	{
		return getCard().getProtocol();
	}
	
	// Re-establish resource manager context
	public void establishContext() throws Exception
	{
		if (isConnectionActive())
			disconnect();
		
		Class pcscTerminal = Class.forName("sun.security.smartcardio.PCSCTerminals");
        Field contextId = pcscTerminal.getDeclaredField("contextId");
        contextId.setAccessible(true);

        if(contextId.getLong(pcscTerminal) != 0L)
        {
            // First get a new context value
            Class pcsc = Class.forName("sun.security.smartcardio.PCSC");
            Method SCardEstablishContext = pcsc.getDeclaredMethod("SCardEstablishContext", new Class[] { Integer.TYPE });
            SCardEstablishContext.setAccessible(true);

            Field SCARD_SCOPE_USER = pcsc.getDeclaredField("SCARD_SCOPE_USER");
            SCARD_SCOPE_USER.setAccessible(true);

            long newId = ((Long)SCardEstablishContext.invoke(pcsc, 
                    new Object[] { SCARD_SCOPE_USER.getInt(pcsc) }
            ));
            contextId.setLong(pcscTerminal, newId);

            // Then clear the terminals in cache
            TerminalFactory factory = TerminalFactory.getDefault();
            CardTerminals terminals = factory.terminals();
            Field fieldTerminals = pcscTerminal.getDeclaredField("terminals");
            fieldTerminals.setAccessible(true);
            Class classMap = Class.forName("java.util.Map");
            Method clearMap = classMap.getDeclaredMethod("clear");

            clearMap.invoke(fieldTerminals.get(terminals));
        }       

		setConnectionActive(false);
	}
}
