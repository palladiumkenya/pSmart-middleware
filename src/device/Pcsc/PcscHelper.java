package device.Pcsc;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class PcscHelper 
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
//	private int returnCode;

	protected ReaderEvents _eventHandler;

	// Default constructor
	public PcscHelper()
	{
		setTerminalFactory(TerminalFactory.getDefault());
	}

	public TerminalFactory getTerminalFactory() { return this._terminalFactory;	}
	public void setTerminalFactory(TerminalFactory terminalFactory) { this._terminalFactory = terminalFactory; }

	public List<CardTerminal> getCardTerminalList() { return this._cardTerminalList; }
	public void setCardTerminalList(List<CardTerminal> cardTerminalList) { this._cardTerminalList = cardTerminalList; }

	public CardTerminal getCardTerminal(int index) { return this._cardTerminalList.get(index); }

	// List the available smart card readers
	public String[] getAllReaders() throws Exception
	{
		setCardTerminalList(getTerminalFactory().terminals().list());

		String[] terminals = new String[getCardTerminalList().size()]; 

		for (int i = 0; i < getCardTerminalList().size(); i++)
			terminals[i] = getCardTerminalList().get(i).getName(); 

		return terminals;
	}
}
