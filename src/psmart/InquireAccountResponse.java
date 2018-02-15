package psmart;

public class InquireAccountResponse
{	
	public InquireAccountResponse(byte[] rawDataFromCard)
	{
		transactionType_ = rawDataFromCard[4];
		System.arraycopy(rawDataFromCard, 0, mac4_, 0, 4);
		System.arraycopy(rawDataFromCard, 5, balance_, 0, 3);
		System.arraycopy(rawDataFromCard, 8, atref_, 0, 6);
		System.arraycopy(rawDataFromCard, 14, maximumBalanceAllowed_, 0, 3);
		System.arraycopy(rawDataFromCard, 17, ttrefc_, 0, 4);
		System.arraycopy(rawDataFromCard, 21, ttrefd_, 0, 4);
	}
	
	public InquireAccountResponse()
	{
	
	}

	private byte[] mac4_ = new byte[4];
	public byte[] getMac4() { return this.mac4_; }
	public void setMac4(byte[] mac4) { this.mac4_ = mac4; }
	
	private byte transactionType_ = 0x00;
	public byte getTransactionType() { return this.transactionType_; }
	public void setTransactionType(byte transactionType) { this.transactionType_ = transactionType; }
	
	private byte[] balance_ = new byte[3];
	public byte[] getBalance() { return this.balance_; }
	public void setBalance(byte[] balance) { this.balance_ = balance; }
		
	public int getBalanceInt(){ return Helper.byteToInt(balance_); }
	public void setBalanceInt(int balance) { this.balance_ = Helper.intToByte(balance); }

	private byte[] atref_ = new byte[6];
	public byte[] getAtref() { return this.atref_; }
	public void setAtref(byte[] atref) { this.atref_ = atref; }

	private byte[] maximumBalanceAllowed_ = new byte[3];
	public byte[] getMaximumBalanceAllowed() { return this.maximumBalanceAllowed_; }
	public void setMaximumBalanceAllowed(byte[] maximumBalanceAllowed) { this.maximumBalanceAllowed_ = maximumBalanceAllowed; }

	public int getMaximumBalanceAllowedInt(){ return Helper.byteToInt(maximumBalanceAllowed_); }
	public void setMaximumBalanceAllowedInt(int maximumBalanceAllowed) { this.maximumBalanceAllowed_ = Helper.intToByte(maximumBalanceAllowed); }
	
	private byte[] ttrefc_ = new byte[4];
	public byte[] getTtrefc() { return this.ttrefc_; }
	public void setTtrefc(byte[] ttrefc) { this.ttrefc_ = ttrefc; }
	
	private byte[] ttrefd_ = new byte[4];
	public byte[] getTtrefd() { return this.ttrefd_; }
	public void setTtrefd(byte[] ttrefd) { this.ttrefd_ = ttrefd; }	
	                                
}
