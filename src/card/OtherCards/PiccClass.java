package card.OtherCards;

import javax.smartcardio.CardException;


public class PiccClass {

	private PcscReader _pcscConnection;
	public PcscReader getPcscConnection() {return this._pcscConnection;}
	public void setPcscConnection(PcscReader pcscConnection) {this._pcscConnection = pcscConnection;}
	
	public PiccClass(PcscReader pcscConnection)
	{
		_pcscConnection = pcscConnection;
	}
	
	public byte[] getData(byte uIso14443A) throws Exception
	{
		if(_pcscConnection == null)
			throw new Exception("PCSC Connection is not yet established");
		
		Apdu apdu = new Apdu();
		apdu.setCommand(new byte[] {(byte)0xFF, (byte)0xCA, uIso14443A, (byte)0x00, (byte)0x00});
		apdu.setLengthExpected(255);
		
		_pcscConnection.sendApduCommand(apdu);
		
		if(apdu.swEqualTo(new byte[]{(byte)0x90, 0x00}) == false)
			throw new CardException(getErrorMessage(apdu.getSw()));
		
		return apdu.getReceiveData();
	}
	
	public byte[] sendCommand(int iCaseType, byte uCla, byte uIns, byte uP1, byte uP2, byte uLc, byte uLe, byte[] uData) throws Exception
	{
		if(_pcscConnection == null)
			throw new Exception("PCSC Connection is not yet established");
		
		Apdu apdu = new Apdu();
		
		switch (iCaseType) {
		case 0:
		case 2:
		case 3:
			apdu.setCommand(new byte[]{uCla, uIns, uP1, uP2, uLc});
			break;

		case 1:
			apdu.setCommand(new byte[]{uCla, uIns, uP1, uP2, uLe});
			break;
			
		default:
			apdu.setCommand(new byte[]{uCla, uIns, uP1, uP2, uLc});
			break;
		}
		
		apdu.setSendData(uData);
		apdu.setLengthExpected(uLe);
		
		_pcscConnection.sendApduCommand(apdu);
		
		if(!apdu.swEqualTo(new byte[]{(byte)0x90, 0x00}))
			throw new CardException(getErrorMessage(apdu.getSw()));
		
		return apdu.getReceiveData();
	}
	
	public String getErrorMessage(byte[] sw1sw2)
	{
		if(sw1sw2.length < 2)
			return "Unknown Status Word (statusWord)";
		else if(sw1sw2[0] == (byte)0x6A && sw1sw2[1] == (byte)0x81)
			return "The function is not supported.";
		else if(sw1sw2[0] == (byte)0x63 && sw1sw2[1] == (byte)0x00)
			return "The operation failed.";
		else
			return "Unknown Status Word (statusWord)";
	}
}
