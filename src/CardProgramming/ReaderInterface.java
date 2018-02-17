package CardProgramming;

import java.util.Arrays;

import javax.swing.JOptionPane;

public class ReaderInterface extends PcscReader{
    	
	public enum CHIP_TYPE
	{		
		UNKNOWN(0x01),
		ACOS3(0x02),
		ACOS3COMBI(0x03),
		ACOS3CONTACTLESS(0x04),
		ERROR(0x00);

		private final int _id;
		CHIP_TYPE(int id){this._id = id;}
	}
	
	public CHIP_TYPE getChipType() 
    {	        
        byte[] atr;
        byte[] cardVersion;
        byte[] cardVersionFirstFiveBytes = new byte[5];
        byte[] acos3VersionFirstFiveBytes = {0x41, 0x43, 0x4F, 0x53, 0x03};
        CHIP_TYPE cardType = CHIP_TYPE.UNKNOWN;
        Acos3 acos3 = new Acos3(this);

		try
		{
			atr = this.getAtr();
			
			if (atr.length == 8)
			{
		        // contactless
		        if(atr[4] != (byte)0x41)
		        	return CHIP_TYPE.UNKNOWN;
		        
		        cardVersion = acos3.getCardInfo(Acos3.CARD_INFO_TYPE.VERSION_NUMBER);
		        
		        if(cardVersion != null)
		        {
		        	System.arraycopy(cardVersion, 0, cardVersionFirstFiveBytes, 0, cardVersionFirstFiveBytes.length);
		        	if(Arrays.equals(cardVersionFirstFiveBytes, acos3VersionFirstFiveBytes))
		        	{
		        		cardType = CHIP_TYPE.ACOS3;
		        	}
		        	else
		        	{
		        		cardType = CHIP_TYPE.UNKNOWN;
		        	}
		        	
		        	/*if(cardVersion[4] != (byte)0x03)
		        		cardType = CHIP_TYPE.UNKNOWN;
		        	else
		        	{
	        			cardType = CHIP_TYPE.ACOS3;
		        	}*/
		        } else {
		        	System.out.println("Null card version");
				}
			}
			else
			{
		        if(atr[5] != (byte)0x41)
		        	return CHIP_TYPE.UNKNOWN;
		        
		        cardVersion = acos3.getCardInfo(Acos3.CARD_INFO_TYPE.VERSION_NUMBER);
		        
		        if(cardVersion != null)
		        {
		        	System.arraycopy(cardVersion, 0, cardVersionFirstFiveBytes, 0, cardVersionFirstFiveBytes.length);
		        	if(Arrays.equals(cardVersionFirstFiveBytes, acos3VersionFirstFiveBytes))
		        	{
		        		cardType = CHIP_TYPE.ACOS3;
		        	}
		        	else
		        	{
		        		cardType = CHIP_TYPE.UNKNOWN;
		        	}
		        	
		        	/*if(cardVersion[4] != (byte)0x03)
		        		cardType = CHIP_TYPE.UNKNOWN;
		        	else
		        	{
	        			cardType = CHIP_TYPE.ACOS3;
		        	}*/
		        }
			}
		}
		catch (Exception ex)
		{
			if(ex.getMessage().contains("No data available; the INQUIRE ACCOUNT command was not executed immediately prior to the GET RESPONSE command; Mutual authentication not successfully completed prior to the SUBMIT Code command; No file selected"))
			{
				cardType = CHIP_TYPE.UNKNOWN;
				return cardType;
			}
			else if(ex.getMessage().contains("Invalid CLA"))
			{
				cardType = CHIP_TYPE.UNKNOWN;
				return cardType;
			}
			else if(ex.getMessage().contains("Unknown Status Word"))
			{
				cardType = CHIP_TYPE.UNKNOWN;
				return cardType;
			}
			else
			{
				cardType = CHIP_TYPE.ERROR;
				JOptionPane.showMessageDialog(null,ex.getMessage().toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return cardType;
    }

}
