package pSmart;

public class PcscException extends Exception
{	
	protected PcscProvider.CODES _readerResponse;
	public PcscProvider.CODES getReaderResponse(){ return this._readerResponse;}
	
	protected String _message;
	public String getMessage(){ return getErrorMessage();}
	
	public PcscException(PcscProvider.CODES errorCode)
	{
		_readerResponse = errorCode;
	}
	
	private String getErrorMessage()
    {
        switch (getReaderResponse())
        {
        case SCARD_E_CANCELLED:
            return ("The action was canceled by an SCardCancel request.");
        case SCARD_E_CANT_DISPOSE:
            return ("The system could not dispose of the media in the requested manner.");
        case SCARD_E_CARD_UNSUPPORTED:
            return ("The smart card does not meet minimal requirements for support.");
        case SCARD_E_DUPLICATE_READER:
            return ("The reader driver didn't produce a unique reader name.");
        case SCARD_E_INSUFFICIENT_BUFFER:
            return ("The data buffer for returned data is too small for the returned data.");
        case SCARD_E_INVALID_ATR:
            return ("An ATR string obtained from the registry is not a valid ATR string.");
        case SCARD_E_INVALID_HANDLE:
            return ("The supplied handle was invalid.");
        case SCARD_E_INVALID_PARAMETER:
            return ("One or more of the supplied parameters could not be properly interpreted.");
        case SCARD_E_INVALID_TARGET:
            return ("Registry startup information is missing or invalid.");
        case SCARD_E_INVALID_VALUE:
            return ("One or more of the supplied parameter values could not be properly interpreted.");
        case SCARD_E_NOT_READY:
            return ("The reader or card is not ready to accept commands.");
        case SCARD_E_NOT_TRANSACTED:
            return ("An attempt was made to end a non-existent transaction.");
        case SCARD_E_NO_MEMORY:
            return ("Not enough memory available to complete this command.");
        case SCARD_E_NO_SERVICE:
            return ("The smart card resource manager is not running.");
        case SCARD_E_NO_SMARTCARD:
            return ("The operation requires a smart card, but no smart card is currently in the device.");
        case SCARD_E_PCI_TOO_SMALL:
            return ("The PCI receive buffer was too small.");
        case SCARD_E_PROTO_MISMATCH:
            return ("The requested protocols are incompatible with the protocol currently in use with the card.");
        case SCARD_E_READER_UNAVAILABLE:
            return ("The specified reader is not currently available for use.");
        case SCARD_E_READER_UNSUPPORTED:
            return ("The reader driver does not meet minimal requirements for support.");
        case SCARD_E_SERVICE_STOPPED:
            return ("The smart card resource manager has shut down.");
        case SCARD_E_SHARING_VIOLATION:
            return ("The smart card cannot be accessed because of other outstanding connections.");
        case SCARD_E_SYSTEM_CANCELLED:
            return ("The action was canceled by the system, presumably to log off or shut down.");
        case SCARD_E_TIMEOUT:
            return ("The user-specified timeout value has expired.");
        case SCARD_E_UNKNOWN_CARD:
            return ("The specified smart card name is not recognized.");
        case SCARD_E_UNKNOWN_READER:
            return ("The specified reader name is not recognized.");
        case SCARD_E_NO_READERS_AVAILABLE:
            return ("No smart card reader is available.");
        case SCARD_F_COMM_ERROR:
            return ("An internal communications error has been detected.");
        case SCARD_F_INTERNAL_ERROR:
            return ("An internal consistency check failed.");
        case SCARD_F_UNKNOWN_ERROR:
            return ("An internal error has been detected, but the source is unknown.");
        case SCARD_F_WAITED_TOO_LONG:
            return ("An internal consistency timer has expired.");
        case SCARD_S_SUCCESS:
            return ("No error was encountered.");
        case SCARD_E_DIR_NOT_FOUND:
            return ("The identified directory does not exist in the smart card..");
        case SCARD_W_RESET_CARD:
            return ("The smart card has been reset, so any shared state information is invalid.");
        case SCARD_W_UNPOWERED_CARD:
            return ("Power has been removed from the smart card, so that further communication is not possible.");
        case SCARD_W_UNRESPONSIVE_CARD:
            return ("The smart card is not responding to a reset.");
        case SCARD_W_UNSUPPORTED_CARD:
            return ("The reader cannot communicate with the card, due to ATR string configuration conflicts.");
        case SCARD_W_REMOVED_CARD:
            return ("The smart card has been removed, so further communication is not possible.");
        case ACS_NFC_ERR_IO:
            return ("Device Error");
        case ACS_NFC_ERR_INVALID:
            return ("Parameter Invalid");
        case ACS_NFC_ERR_DEV_NOT_SUPP:
            return ("Device Mode not support the feature");
        case ACS_NFC_ERR_NOT_SUCH_DEV:
            return ("Destinate Device not find");
        case ACS_NFC_ERR_NOT_DATA:
            return ("Response cannot receive");
        case ACS_NFC_ERR_TIMEOUT:
            return ("Timeout occurs");
        case ACS_NFC_ERR_FAIL:
            return ("NFC Error Occurs");
        case ACS_NFC_ERR_RF_TRANS:
            return ("Data Received not follow ISO18092");
        case ACS_NFC_ERR_SOFT:
            return ("Unknown Error");
        default:
            return ("Undocumented error");
    
        }
    }
	
	public PcscException()
	{
		super();
	}
	
	public PcscException(String message)
	{
		super(message);
	}
	
	public PcscException(String message, byte[] readerResponse)
	{
		super(message + "\nResponse : " + Helper.byteAsString(readerResponse, true));
	}
 }
