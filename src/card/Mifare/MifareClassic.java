package card.Mifare;

public class MifareClassic
{
	public enum MIFARE_TYPE
	{
		Mifare_1K(0),
		Mifare_4K(1);
		
		private final int _id;
		MIFARE_TYPE(int id) {this._id = id;}
	}
	
	public enum MIFARE_ERROR_CODES
	{
		READ_VALUE_FAILED(1),
        READ_BINARY_FAILED(2),
        WRITE_VALUE_FAILED(3),
        WRITE_BINARY_FAILED(4),
        STORE_VALUE_FAILED(5),
        INCREMENT_FAILED(6),
        DECREMENT_FAILED(7),
        RESTORE_VALUE_FAILED(8);
        
        private final int _id;
		MIFARE_ERROR_CODES(int id) {this._id = id;}
	}
	
	public MifareClassic(PcscReader pcscConnection)
	{
		_PcscConnection = pcscConnection;
	}
	
	private PcscReader _PcscConnection;
	public PcscReader getPcscConnection() {return this._PcscConnection;}
	public void setPcscConnection(PcscReader pcscConnection) {this._PcscConnection = pcscConnection;}
	
	public enum KEYTYPES
	{
		ACR122_KEYTYPE_A(0x60),
        ACR122_KEYTYPE_B(0x61);
		
		private final int _id;
		KEYTYPES(int id) {this._id = id;}
	}
	
	public enum VALUEBLOCKOPERATION
    {
        STORE(0x00),
        INCREMENT(0x01),
        DECREMENT(0x02);
        
        private final int _id;
        VALUEBLOCKOPERATION(int id) {this._id = id;}
    }
	
	public void valueBlock(byte blockNum, VALUEBLOCKOPERATION transType, int amount) throws Exception
	{
		Apdu apdu;
				
		apdu = new Apdu();
		apdu.setCommand(new byte[] { (byte)0xFF, (byte)0xD7, (byte)0x00, blockNum, (byte)0x05 });
		
		apdu.setSendData(new byte[]{ (byte)transType._id, 
									 Helper.intToByte(amount)[0], 
									 Helper.intToByte(amount)[1], 
									 Helper.intToByte(amount)[2], 
									 Helper.intToByte(amount)[3]});
		
		getPcscConnection().sendApduCommand(apdu);		
		
		if(apdu.getSw()[0] != (byte)0x90)
			throw new Exception("Value block operation failed");			
	}
	
	public void store(byte blockNumber, int amount) throws Exception
	{
		valueBlock(blockNumber, VALUEBLOCKOPERATION.STORE, amount);
	}
	
	public void decrement(byte blockNumber, int amount) throws Exception
	{
		valueBlock(blockNumber, VALUEBLOCKOPERATION.DECREMENT, amount);
	}
	
	public void increment(byte blockNumber, int amount) throws Exception
	{
		valueBlock(blockNumber, VALUEBLOCKOPERATION.INCREMENT, amount);
	}
	
	public int inquireAmount(byte blockNum) throws Exception
	{
		Apdu apdu;
		
		apdu = new Apdu();
		apdu.setCommand(new byte[] {(byte)0xFF, (byte)0xB1, (byte)0x00, blockNum, (byte)0x04});
		
		apdu.setLengthExpected(4);
		
		getPcscConnection().sendApduCommand(apdu);
		
		if(apdu.getSw()[0] != (byte)0x90)
			throw new Exception("Read value failed");
		
		return Helper.byteToInt(apdu.getReceiveData());
	}
	
	public void restoreAmount(byte sourceBlock, byte targetBlock) throws Exception
	{
		Apdu apdu;
		
		apdu = new Apdu();
		apdu.setCommand(new byte[] {(byte)0xFF, (byte)0xD7, (byte)0x00, sourceBlock, (byte)0x02});		
		apdu.setSendData(new byte[] {(byte)0x03, targetBlock});
		
		apdu.setLengthExpected(2);
		
		getPcscConnection().sendApduCommand(apdu);
		
		if(apdu.getSw()[0] != (byte)0x90)
			throw new Exception("Restore value failed");
	}
	
	public byte[] readBinaryBlock(byte blockNum, byte length) throws Exception	
	{
		Apdu apdu;		
		byte[] tmpArray = new byte[16];
		
		apdu = new Apdu();
		apdu.setCommand(new byte[] { (byte)0xFF, (byte)0xB0, 0x00, blockNum, length });
		apdu.setSendData(new byte[0]);
		
		apdu.setLengthExpected(length);
		
		getPcscConnection().sendApduCommand(apdu);
		
		if(apdu.getSw()[0] != (byte)0x90)
			throw new Exception("Read failed");		
		
		System.arraycopy(apdu.getReceiveData(), 0, tmpArray, 0, length);
			
		return tmpArray;
	}
	
	public void updateBinaryBlock(byte blockNum, byte[] data, byte length) throws Exception
	{
		Apdu apdu;
		
		if(data.length > 48)
			throw new Exception("Data has invalid length");
		
		/*if(data.length != 16)
		{
			byte[]tmpStorageArray = new byte[16];
			
			// resize the array
			for(int i = 0; i < 16; i++)
				tmpStorageArray[i] = data[i];
				
			data = new byte[16];
				
			for(int i = 0; i < 16; i++)
				data[i] = tmpStorageArray[i];			
		}*/
		
		if((length % 16) != 0)
			throw new Exception("Data length must be multiple of 16");
		
		apdu = new Apdu();
		apdu.setCommand(new byte[] { (byte)0xFF, (byte)0xD6, (byte)0x00, blockNum, length });		
		apdu.setSendData(new byte[data.length]);
		apdu.setSendData(data);
		
		getPcscConnection().sendApduCommand(apdu);
		
		if(apdu.getSw()[0] != (byte)0x90)
			throw new Exception("Update failed");
	}
	
	public String getErrorMessage(byte[] sw1sw2)
	{
		if(sw1sw2.length < 2)
			return "Unknown Status Word (" + Helper.byteAsString(sw1sw2, false) + ")";		
		else if (sw1sw2[0] == (byte)0x63 && sw1sw2[1] == (byte)0x00)
			return "Command failed";
		else
			return "Unknown Status Word (" + Helper.byteAsString(sw1sw2, false) + ")";		
	}
	
	public static boolean isMifareClassic(byte[] atr)
	{
		byte[] tmpArray = new byte[3];
		
		for(int i = 0; i < 3; i++)
			tmpArray[i] = atr[i + 4];
		
		if(atr != null && atr.length > 8 && Helper.byteArrayIsEqual(tmpArray, new byte[] { (byte)0x80, (byte)0x4F, (byte)0x0C }))
			return true;
		else
			return false;
	}
}
