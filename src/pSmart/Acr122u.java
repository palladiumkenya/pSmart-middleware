package pSmart;

import java.util.Arrays;


public class Acr122u extends PcscReader
{
	private static final int RESPONSE_INDEX = 1; 
	public static final int FILE_DEVICE_SMARTCARD = 0x310000; // Reader action IOCTLs
    public static final int IOCTL_SMARTCARD_ACR122U_ESCAPE_COMMAND = FILE_DEVICE_SMARTCARD + 3500 * 4;

    public enum ANTENNA_STATUS
    {
    	ON((byte)0x01),
        OFF((byte)0x00);
        
        private final int ID;
        ANTENNA_STATUS(int id) {this.ID = id;}
        public int getAntennaStatus() {return ID;}
    }
    
    public enum BUZZER_OUTPUT
    {
    	ON((byte)0xFF),
        OFF((byte)0x00);
        
        private final int ID;
        BUZZER_OUTPUT(int id) {this.ID = id;}
        public int getBuzzerOutput() {return ID;}
    }
	
	public enum PARAMETER_OPTION
    {
    	SKIP((byte)0x00),
    	DETECT((byte)0x01);
        
        private final int ID;
        PARAMETER_OPTION(int id) {this.ID = id;}
        public int getParameterOption() {return ID;}
    }
    
    public enum POLLING_INTERVAL
    { 
    	MS_250((byte)0x01),
    	MS_500((byte)0x00);
        
        private final int ID;
        POLLING_INTERVAL(int id) {this.ID = id;}
        public int getPollingInterval() {return ID;}
    }
    
    public enum PICC_POLLING
    {
    	ENABLED((byte)0x01),
    	DISABLED((byte)0x00);
        
        private final int ID;
        PICC_POLLING(int id) {this.ID = id;}
        public int getPiccPolling() {return ID;}
    }
    
    public enum ATS_GENERATION
    {
    	ENABLED((byte)0x01),
    	DISABLED((byte)0x00);
        
        private final int ID;
        ATS_GENERATION(int id) {this.ID = id;}
        public int getAtsGeneration() {return ID;}
    }
	
    public enum LED_STATE
    {
    	ON((byte)0x01),
        OFF((byte)0x00);
        
        private final int ID;
        LED_STATE(int id) {this.ID = id;}
        public int getLedState() {return ID;}
    }
    
    public enum LED_STATE_MASK
    {
    	ON((byte)0x01),
        OFF((byte)0x00);
        
        private final int ID;
        LED_STATE_MASK(int id) {this.ID = id;}
        public int getLedStateMask() {return ID;}
    }
    
    public enum INITIAL_LED_BLINKING_STATE
    {
    	ON((byte)0x01),
        OFF((byte)0x00);
        
        private final int ID;
        INITIAL_LED_BLINKING_STATE(int id) {this.ID = id;}
        public int getInitialLedBlinkingState() {return ID;}
    }
    
    public enum LED_BLINKING_MASK
    {
    	ON((byte)0x01),
        OFF((byte)0x00);
        
        private final int ID;
        LED_BLINKING_MASK(int id) {this.ID = id;}
        public int getLedBlinkingMask() {return ID;}
    }
    
    public enum LED_BUZZER_BEHAVIOR
    {
    	ENABLED((byte)0x01),
    	DISABLED((byte)0x00);
        
        private final int ID;
        LED_BUZZER_BEHAVIOR(int id) {this.ID = id;}
        public int getLedBuzzerBehavior() {return ID;}
    }
    
    public enum LINK_TO_BUZZER
    {
    	OFF((byte)0x00),
    	T1_DURATION((byte)0x01),
    	T2_DURATION((byte)0x02),
    	T1_AND_T2_DURATION((byte)0x03);
        
        private final int ID;
        LINK_TO_BUZZER(int id) {this.ID = id;}
        public int getLinkToBuzzer() {return ID;}
    }
    
    public static class LedAndBuzzerControl
    {
        private LED_STATE _redLedState = LED_STATE.OFF;
        private LED_STATE _greenLedState = LED_STATE.OFF;
        private LED_STATE_MASK _redLedStateMask = LED_STATE_MASK.OFF;
        private LED_STATE_MASK _greenLedStateMask = LED_STATE_MASK.OFF;
        private INITIAL_LED_BLINKING_STATE _redLedBlinkingState = INITIAL_LED_BLINKING_STATE.OFF;
        private INITIAL_LED_BLINKING_STATE _greenLedBlinkingState = INITIAL_LED_BLINKING_STATE.OFF;
        private LED_BLINKING_MASK _redLedBlinkingMask = LED_BLINKING_MASK.OFF;
        private LED_BLINKING_MASK _greenLedBlinkingMask = LED_BLINKING_MASK.OFF;
        private byte[] _blinkingDuration = new byte[4];

        public LedAndBuzzerControl()
        {

        }

        public LedAndBuzzerControl(byte ledState)
        {
            if ((ledState & 0x01) == 0x01)
                _redLedState = LED_STATE.ON;

            if ((ledState & 0x02) == 0x02)
                _greenLedState = LED_STATE.ON;

            if ((ledState & 0x04) == 0x04)
                _redLedStateMask = LED_STATE_MASK.ON;

            if ((ledState & 0x08) == 0x08)
                _greenLedStateMask = LED_STATE_MASK.ON;

            if ((ledState & 0x10) == 0x10)
                _redLedBlinkingState = INITIAL_LED_BLINKING_STATE.ON;

            if ((ledState & 0x20) == 0x20)
                _greenLedBlinkingState = INITIAL_LED_BLINKING_STATE.ON;

            if ((ledState & 0x40) == 0x40)
                _redLedBlinkingMask = LED_BLINKING_MASK.ON;

            if ((ledState & 0x80) == 0x80)
                _greenLedBlinkingMask = LED_BLINKING_MASK.ON;
        }

        public LED_STATE getRedLedState() { return this._redLedState; }
        public void setRedLedState(LED_STATE value) { _redLedState = value; }
        
        public LED_STATE getGreenLedState() { return this._greenLedState; }
        public void setGreenLedState(LED_STATE value) { _greenLedState = value; }

        public LED_STATE_MASK getRedLedStateMask() { return _redLedStateMask; }
        public void setRedLedStateMask(LED_STATE_MASK value) { _redLedStateMask = value; }
        
        public LED_STATE_MASK getGreenLedStateMask() { return _greenLedStateMask; }
        public void setGreenLedStateMask(LED_STATE_MASK value) { _greenLedStateMask = value; }

        public INITIAL_LED_BLINKING_STATE getRedLedBlinkingState() { return _redLedBlinkingState; }
        public void setRedLedBlinkingState(INITIAL_LED_BLINKING_STATE value) { _redLedBlinkingState = value; }
        
        public INITIAL_LED_BLINKING_STATE getGreenLedBlinkingState() { return _greenLedBlinkingState; }
        public void setGreenLedBlinkingState(INITIAL_LED_BLINKING_STATE value) { _greenLedBlinkingState = value; }
  
        public LED_BLINKING_MASK getRedLedBlinkingMask() { return _redLedBlinkingMask; }
        public void setRedLedBlinkingMask(LED_BLINKING_MASK value) { _redLedBlinkingMask = value; }
        
        public LED_BLINKING_MASK getGreenLedBlinkingMask() { return _greenLedBlinkingMask; }
        public void setGreenLedBlinkingMask(LED_BLINKING_MASK value) { _greenLedBlinkingMask = value; }
        
        public byte[] getBlinkingDuration() { return _blinkingDuration; }
        public void setBlinkingDuration(byte[] value) { _blinkingDuration = value; }
        
        public byte getRawLedStatus()
        {
            byte ledStatus = 0x00;

            if (getRedLedState() == LED_STATE.ON)
                ledStatus |= 0x01;

            if (getGreenLedState() == LED_STATE.ON)
                ledStatus |= 0x02;

            if (getRedLedStateMask() == LED_STATE_MASK.ON)
                ledStatus |= 0x04;

            if (getGreenLedStateMask() == LED_STATE_MASK.ON)
                ledStatus |= 0x08;

            if (getRedLedBlinkingState() == INITIAL_LED_BLINKING_STATE.ON)
                ledStatus |= 0x10;

            if (getGreenLedBlinkingState() == INITIAL_LED_BLINKING_STATE.ON)
                ledStatus |= 0x20;

            if (getRedLedBlinkingMask() == LED_BLINKING_MASK.ON)
                ledStatus |= 0x40;

            if (getGreenLedBlinkingMask() == LED_BLINKING_MASK.ON)
                ledStatus |= 0x80;

            return ledStatus;
        }
    }

    public static class PiccOperatingParameter
    {   	
    	private PARAMETER_OPTION _iso14443TypeA = PARAMETER_OPTION.SKIP;
        private PARAMETER_OPTION _iso14443TypeB = PARAMETER_OPTION.SKIP;
        private PARAMETER_OPTION _felica212 = PARAMETER_OPTION.SKIP;
        private PARAMETER_OPTION _felica424 = PARAMETER_OPTION.SKIP;
        private PARAMETER_OPTION _topaz = PARAMETER_OPTION.SKIP;
        private POLLING_INTERVAL _pollingInterval = POLLING_INTERVAL.MS_500;
        private PICC_POLLING _piccPolling = PICC_POLLING.DISABLED;
        private ATS_GENERATION _atsGeneration = ATS_GENERATION.DISABLED;

        public PiccOperatingParameter()
        {

        }

        public PiccOperatingParameter(byte rawOperatingParameter)
        {
            if ((rawOperatingParameter & 0x01) == 0x01)
                setIso14443TypeA(PARAMETER_OPTION.DETECT);

            if ((rawOperatingParameter & 0x02) == 0x02)
                setIso14443TypeB(PARAMETER_OPTION.DETECT);

            if ((rawOperatingParameter & 0x04) == 0x04)
                setTopaz(PARAMETER_OPTION.DETECT);

            if ((rawOperatingParameter & 0x08) == 0x08)
                setFelica212(PARAMETER_OPTION.DETECT);

            if ((rawOperatingParameter & 0x10) == 0x10)
                setFelica424(PARAMETER_OPTION.DETECT);

            if ((rawOperatingParameter & 0x20) == 0x20)
                setPollingInterval(POLLING_INTERVAL.MS_250);

            if ((rawOperatingParameter & 0x40) == 0x40)
                setAutoAtsGeneration(ATS_GENERATION.ENABLED);

            if ((rawOperatingParameter & 0x80) == 0x80)
                setAutoPiccPolling(PICC_POLLING.ENABLED);
        }
        
        public PARAMETER_OPTION getIso14443TypeA() { return _iso14443TypeA; }
        public void setIso14443TypeA(PARAMETER_OPTION value) { this._iso14443TypeA = value; }
        
        public PARAMETER_OPTION getIso14443TypeB() { return _iso14443TypeB; }
        public void setIso14443TypeB(PARAMETER_OPTION value) { this._iso14443TypeB = value; }

        public PARAMETER_OPTION getFelica212() { return _felica212; }
        public void setFelica212(PARAMETER_OPTION value) { this._felica212 = value; }
        
        public PARAMETER_OPTION getFelica424() { return _felica424; }
        public void setFelica424(PARAMETER_OPTION value) { this._felica424 = value; }
        
        public PARAMETER_OPTION getTopaz() { return _topaz; }
        public void setTopaz(PARAMETER_OPTION value) { this._topaz = value; }
        
        public POLLING_INTERVAL getPollingInterval() { return _pollingInterval; }
        public void setPollingInterval(POLLING_INTERVAL value) { this._pollingInterval = value; }
        
        public PICC_POLLING getAutoPiccPolling() { return _piccPolling; }
        public void setAutoPiccPolling(PICC_POLLING value) { this._piccPolling = value; }
        
        public ATS_GENERATION getAutoAtsGeneration() { return _atsGeneration; }
        public void setAutoAtsGeneration(ATS_GENERATION value) { this._atsGeneration = value; }
        
        public byte getRawOperatingParameter()
        {
            byte operatingParameter = 0x00;

            if (getIso14443TypeA() == PARAMETER_OPTION.DETECT)
                operatingParameter |= 0x01;

            if (getIso14443TypeB() == PARAMETER_OPTION.DETECT)
                operatingParameter |= 0x02;

            if (getTopaz() == PARAMETER_OPTION.DETECT)
                operatingParameter |= 0x04;

            if (getFelica212() == PARAMETER_OPTION.DETECT)
                operatingParameter |= 0x08;

            if (getFelica424() == PARAMETER_OPTION.DETECT)
                operatingParameter |= 0x10;

            if (getPollingInterval() == POLLING_INTERVAL.MS_250)
                operatingParameter |= 0x20;

            if (getAutoAtsGeneration() == ATS_GENERATION.ENABLED)
                operatingParameter |= 0x40;

            if (getAutoPiccPolling() == PICC_POLLING.ENABLED)
                operatingParameter |= 0x80;

            return operatingParameter;
        }
    }
    
    public static class LedBuzzerBehaviorPicc
    {
    	private LED_BUZZER_BEHAVIOR _piccCardOperationBlinkingLed = LED_BUZZER_BEHAVIOR.DISABLED;
        private LED_BUZZER_BEHAVIOR _piccPollingLed = LED_BUZZER_BEHAVIOR.DISABLED;
        private LED_BUZZER_BEHAVIOR _piccActivationLed = LED_BUZZER_BEHAVIOR.DISABLED;
        private LED_BUZZER_BEHAVIOR _piccCardInsertionRemovalBuzzer = LED_BUZZER_BEHAVIOR.DISABLED;
        private LED_BUZZER_BEHAVIOR _piccPn512ResetIndicationBuzzer = LED_BUZZER_BEHAVIOR.DISABLED;
        private LED_BUZZER_BEHAVIOR _piccColorSelectRed = LED_BUZZER_BEHAVIOR.DISABLED;
        private LED_BUZZER_BEHAVIOR _piccColorSelectGreen = LED_BUZZER_BEHAVIOR.DISABLED;

        public LedBuzzerBehaviorPicc()
        {
        
        }

        public LedBuzzerBehaviorPicc(byte behaviorSettings)
        {
            if ((behaviorSettings & 0x01) == 0x01)
                _piccCardOperationBlinkingLed = LED_BUZZER_BEHAVIOR.ENABLED;

            if ((behaviorSettings & 0x02) == 0x02)
            	
                _piccPollingLed = LED_BUZZER_BEHAVIOR.ENABLED;

            if ((behaviorSettings & 0x04) == 0x04)
                _piccActivationLed = LED_BUZZER_BEHAVIOR.ENABLED;

            if ((behaviorSettings & 0x08) == 0x08)
            	_piccCardInsertionRemovalBuzzer = LED_BUZZER_BEHAVIOR.ENABLED;

            if ((behaviorSettings & 0x20) == 0x20)
                _piccPn512ResetIndicationBuzzer = LED_BUZZER_BEHAVIOR.ENABLED;

            if ((behaviorSettings & 0x80) == 0x80)
            	_piccColorSelectRed = LED_BUZZER_BEHAVIOR.ENABLED;
            
            if ((behaviorSettings & 0x40) == 0x40)
            	_piccColorSelectGreen = LED_BUZZER_BEHAVIOR.ENABLED;
        }

        public LED_BUZZER_BEHAVIOR getPiccCardOperationBlinkingLed() { return _piccCardOperationBlinkingLed; }
        public void setPiccCardOperationBlinkingLed(LED_BUZZER_BEHAVIOR value) { this._piccCardOperationBlinkingLed = value; }
        
        public LED_BUZZER_BEHAVIOR getPiccPollingLed() { return _piccPollingLed; }
        public void setPiccPollingLed(LED_BUZZER_BEHAVIOR value) { this._piccPollingLed = value; }
        
        public LED_BUZZER_BEHAVIOR getPiccActivationLed() { return _piccActivationLed; }
        public void setPiccActivationLed(LED_BUZZER_BEHAVIOR value) { this._piccActivationLed = value; }

        public LED_BUZZER_BEHAVIOR getPiccCardInsertionRemovalBuzzer() { return _piccCardInsertionRemovalBuzzer; }
        public void setPiccCardInsertionRemovalBuzzer(LED_BUZZER_BEHAVIOR value) { this._piccCardInsertionRemovalBuzzer = value; }

        public LED_BUZZER_BEHAVIOR getPiccPn512ResetIndicationBuzzer() { return _piccPn512ResetIndicationBuzzer; }
        public void setPiccPn512ResetIndicationBuzzer(LED_BUZZER_BEHAVIOR value) { this._piccPn512ResetIndicationBuzzer = value; }

        public LED_BUZZER_BEHAVIOR getPiccColorSelectRed() { return _piccColorSelectRed; }
        public void setPiccColorSelectRed(LED_BUZZER_BEHAVIOR value) { this._piccColorSelectRed = value; }
        
        public LED_BUZZER_BEHAVIOR getPiccColorSelectGreen() { return _piccColorSelectGreen; }
        public void setPiccColorSelectGreen(LED_BUZZER_BEHAVIOR value) { this._piccColorSelectGreen = value; }

        public byte getRawLedBuzzerBehaviorPicc()
        {
            byte ledBuzzerBehavior = 0x00;

            if (getPiccCardOperationBlinkingLed() == LED_BUZZER_BEHAVIOR.ENABLED)
                ledBuzzerBehavior |= 0x01;

            if (getPiccPollingLed() == LED_BUZZER_BEHAVIOR.ENABLED)
                ledBuzzerBehavior |= 0x02;

            if (getPiccActivationLed() == LED_BUZZER_BEHAVIOR.ENABLED)
                ledBuzzerBehavior |= 0x04;

            if (getPiccCardInsertionRemovalBuzzer() == LED_BUZZER_BEHAVIOR.ENABLED)
                ledBuzzerBehavior |= 0x08;

            if (getPiccPn512ResetIndicationBuzzer() == LED_BUZZER_BEHAVIOR.ENABLED)
                ledBuzzerBehavior |= 0x20;

            if (getPiccColorSelectRed() == LED_BUZZER_BEHAVIOR.ENABLED)
            	ledBuzzerBehavior |= 0x80;

            if (getPiccColorSelectGreen() == LED_BUZZER_BEHAVIOR.ENABLED)
            	ledBuzzerBehavior |= 0x40;

            return ledBuzzerBehavior;
        }
    }	
    	
    public class AutoPiccPollingSettings
    {
    	private PICC_POLLING _autoPiccPolling = PICC_POLLING.DISABLED;
        private ATS_GENERATION _autoAtsGeneration = ATS_GENERATION.DISABLED;
        private POLLING_INTERVAL _pollingInterval = POLLING_INTERVAL.MS_250;
        private PARAMETER_OPTION _detectFelica212 = PARAMETER_OPTION.DETECT;
        private PARAMETER_OPTION _detectFelica424 = PARAMETER_OPTION.DETECT;
        private PARAMETER_OPTION _detectTopaz = PARAMETER_OPTION.DETECT;
        private PARAMETER_OPTION _detectIso14443TypeB = PARAMETER_OPTION.DETECT;
        private PARAMETER_OPTION _detectIso14443TypeA = PARAMETER_OPTION.DETECT;
        
        public AutoPiccPollingSettings()
        {

        }

        public AutoPiccPollingSettings(byte rawPiccPollingSetting)
        {
            setRawPollingSetting(rawPiccPollingSetting);
        }

        public PICC_POLLING getAutoPiccPolling() { return _autoPiccPolling; }
        public void setAutoPiccPolling(PICC_POLLING value) { this._autoPiccPolling = value; }
        
        public ATS_GENERATION getAutoAtsGeneration() { return _autoAtsGeneration; }
        public void setAutoAtsGeneration(ATS_GENERATION value) { this._autoAtsGeneration = value; }
        
        public POLLING_INTERVAL getPollingInterval() { return _pollingInterval; }
        public void setPollingInterval(POLLING_INTERVAL value) { this._pollingInterval = value; }
        
        public PARAMETER_OPTION getDetectFelica212() { return _detectFelica212; }
        public void setDetectFelica212(PARAMETER_OPTION value) { this._detectFelica212 = value; }
        
        public PARAMETER_OPTION getDetectFelica424() { return _detectFelica424; }
        public void setDetectFelica424(PARAMETER_OPTION value) { this._detectFelica424 = value; }
        
        public PARAMETER_OPTION getDetectTopaz() { return _detectTopaz; }
        public void setDetectTopaz(PARAMETER_OPTION value) { this._detectTopaz = value; }
        
        public PARAMETER_OPTION getDetectIso14443TypeB() { return _detectIso14443TypeB; }
        public void setDetectIso14443TypeB(PARAMETER_OPTION value) { this._detectIso14443TypeB = value; }
        
        public PARAMETER_OPTION getDetectIso14443TypeA() { return _detectIso14443TypeA; }
        public void setDetectIso14443TypeA(PARAMETER_OPTION value) { this._detectIso14443TypeA = value; }

        public void setRawPollingSetting(byte rawPollingSetting)
        {
            if ((rawPollingSetting & 0x01) == 0x01)
                setDetectIso14443TypeA(PARAMETER_OPTION.DETECT);

            if ((rawPollingSetting & 0x02) == 0x02)
                setDetectIso14443TypeB(PARAMETER_OPTION.DETECT);

            if ((rawPollingSetting & 0x04) == 0x04)
                setDetectTopaz(PARAMETER_OPTION.DETECT);

            if ((rawPollingSetting & 0x08) == 0x08)
                setDetectFelica212(PARAMETER_OPTION.DETECT);

            if ((rawPollingSetting & 0x10) == 0x10)
                setDetectFelica424(PARAMETER_OPTION.DETECT);

            if ((rawPollingSetting & 0x20) == 0x20)
                setPollingInterval(POLLING_INTERVAL.MS_500);

            if ((rawPollingSetting & 0x40) == 0x40)
                setAutoAtsGeneration(ATS_GENERATION.ENABLED);

            if ((rawPollingSetting & 0x80) == 0x80)
                setAutoPiccPolling(PICC_POLLING.ENABLED);
        }

        public byte getRawPollingSettings()
        {
            byte rawPollingSetting = 0x00;

            if (getDetectIso14443TypeA() == PARAMETER_OPTION.DETECT)
                rawPollingSetting |= 0x01;

            if (getDetectIso14443TypeB() == PARAMETER_OPTION.DETECT)
                rawPollingSetting |= 0x02;

            if (getDetectTopaz() == PARAMETER_OPTION.DETECT)
                rawPollingSetting |= 0x04;

            if (getDetectFelica212() == PARAMETER_OPTION.DETECT)
                rawPollingSetting |= 0x08;

            if (getDetectFelica424() == PARAMETER_OPTION.DETECT)
                rawPollingSetting |= 0x10;

            if (getPollingInterval() == POLLING_INTERVAL.MS_500)
                rawPollingSetting |= 0x20;

            if (getAutoAtsGeneration() == ATS_GENERATION.ENABLED)
                rawPollingSetting |= 0x40;

            if (getAutoPiccPolling() == PICC_POLLING.ENABLED)
                rawPollingSetting |= 0x80;

            return rawPollingSetting;
        }
    }
    	
	public Acr122u()
	{
		super();
		this.setControlCode(IOCTL_SMARTCARD_ACR122U_ESCAPE_COMMAND);
	}

	
	public byte[] getStatus() throws Exception
	{
		byte[] response;
	    byte[] command = new byte[] { (byte) 0xFF, 0x00, 0x00, 0x00, 0x02, (byte) 0xD4, 0x04};
	    
	    sendControlCommand(command);
	    
	    response = getControlResponse();
	    
	    return Arrays.copyOfRange(response, 0, response.length);
	}
	
	public void setTimeoutParameter(byte timeoutParameter) throws Exception
	{
	    byte[] command = new byte[] { (byte) 0xFF, 0x00, 0x41, timeoutParameter, 0x00 };
	    
	    sendControlCommand(command);
	}
	
	public void setBuzzerOutput(BUZZER_OUTPUT buzzerOutput) throws Exception
	{
		byte status = 0x00;
		byte[] command = new byte[5];
		
		if (buzzerOutput == BUZZER_OUTPUT.OFF)
			status = 0x00;
		else
			status = (byte) 0xFF;
		
		command[0] = (byte)0XFF;
		command[1] = 0x00;
		command[2] = 0x52;
		command[3] = (byte)status;
		command[4] = 0x00;
		
	    sendControlCommand(command);
	}
	
	public byte[] setLedStatus(LedAndBuzzerControl ledAndBuzzerControl) throws Exception
	{
		byte[] response;
		byte[] tempBlinkDuration;
		byte[] command = new byte[9];
		
		tempBlinkDuration = ledAndBuzzerControl.getBlinkingDuration();
		
		command[0] = (byte)0XFF;
		command[1] = 0x00;
		command[2] = 0x40;
		command[3] = ledAndBuzzerControl.getRawLedStatus();
		command[4] = 0x40;
		command[5] = tempBlinkDuration[0];
		command[6] = tempBlinkDuration[1];
		command[7] = tempBlinkDuration[2];
		command[8] = tempBlinkDuration[3];
	    
	    sendControlCommand(command);
	    
	    response = getControlResponse();
	    
	    return Arrays.copyOfRange(response, 0, response.length);
	}
	
	public PiccOperatingParameter getPiccOperatingParameter() throws Exception
	{
		byte[] response;
	    byte[] command = new byte[] { (byte) 0xFF, 0x00, 0x50, 0x00, 0x00};
	    
	    sendControlCommand(command);
	    
	    response = getControlResponse();
	    
	    return new PiccOperatingParameter(response[RESPONSE_INDEX]);
	}
	
	public PiccOperatingParameter setPiccOperatingParameter(PiccOperatingParameter piccOperatingParameter) throws Exception
	{
		byte[] response;
	    byte[] command = new byte[] { (byte) 0xFF, 0x00, 0x51, piccOperatingParameter.getRawOperatingParameter(), 0x00};
	   
	    sendControlCommand(command);
	    
	    response = getControlResponse();
	    
	    return new PiccOperatingParameter(response[RESPONSE_INDEX]);
	}
}
