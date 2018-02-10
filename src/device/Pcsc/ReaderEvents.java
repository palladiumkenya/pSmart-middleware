package device.Pcsc;
//===========================================================================================
// 
//  Author          : Teosseth G. Altar
// 
//  File            : ReaderEvents.java
// 
//  Copyright (C)   : Advanced Card Systems Ltd.
// 
//  Description     : Container class for the event class & event generator class
// 
//  Date            : October 28, 2011
// 
//  Revision Trail : [Author] / [Date of modification] / [Details of Modifications done]
// 
//=========================================================================================

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;


public class ReaderEvents 
{
	@SuppressWarnings("serial")
	public class TransmitApduEventArg extends EventObject
	{	
		private byte[] _data;
		
		public byte[] getData()
		{
			return this._data;
		}
		
		public void setData(byte[] data)
		{
			this._data = data;
		}
		
	    public TransmitApduEventArg(Object sender, byte[] data)
	    {
	    	super(sender);
	    	this._data = data;
	    }
	    
	    public String getAsString(boolean spaceInBetween)
		{
			if(this.getData() == null)
				return "";
			
			return Helper.byteAsString(this.getData(), spaceInBetween);
		}
	}       
	        
	public interface TransmitApduHandler
	{
		public void onSendCommand(TransmitApduEventArg event);
		public void onReceiveCommand(TransmitApduEventArg event);
	}
	
	private List<TransmitApduHandler> _listeners = new ArrayList<TransmitApduHandler>();

	public List<TransmitApduHandler> getListeners()
	{
		return this._listeners;
	}
	
	public void setListeners(List<TransmitApduHandler> listeners)
	{
		this._listeners = listeners;
	}
	
	public synchronized void addEventListener(TransmitApduHandler listener) 
	{
		this.getListeners().add(listener);
	}
    
    public synchronized void removeEventListener(TransmitApduHandler listener) 
    {
    	this.getListeners().remove(listener);
    }
    
    public synchronized void sendCommandData(byte[] data) 
    {
        TransmitApduEventArg event = new TransmitApduEventArg(this, data);
        Iterator<TransmitApduHandler> listeners = this.getListeners().iterator();
        
        while (listeners.hasNext()) 
        {
            ((TransmitApduHandler) listeners.next()).onSendCommand(event);
        }
    }
    
    public synchronized void receiveCommandData(byte[] data) 
    {
        TransmitApduEventArg event = new TransmitApduEventArg( this, data);
        Iterator<TransmitApduHandler> listeners = this.getListeners().iterator();
        
        while (listeners.hasNext()) 
        {
            ((TransmitApduHandler) listeners.next()).onReceiveCommand(event);
        }
    }
}
