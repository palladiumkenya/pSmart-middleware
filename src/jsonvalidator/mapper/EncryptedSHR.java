package jsonvalidator.mapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class EncryptedSHR {
    public final String sHR;
    public final ADDENDUM aDDENDUM;

    @JsonCreator
    public EncryptedSHR(@JsonProperty("sHR") String sHR, @JsonProperty("aDDENDUM") ADDENDUM aDDENDUM){
        this.sHR = sHR;
        this.aDDENDUM = aDDENDUM;
    }

    public static final class ADDENDUM {
        public SHR.CARD_DETAILS cARD_DETAILS;
        public SHR.PATIENT_IDENTIFICATION.INTERNAL_PATIENT_ID iDENTIFIERS[];

        @JsonCreator
        public ADDENDUM(@JsonProperty("cARD_DETAILS") SHR.CARD_DETAILS cARD_DETAILS, @JsonProperty("iDENTIFIERS") SHR.PATIENT_IDENTIFICATION.INTERNAL_PATIENT_ID[] iDENTIFIERS){
            this.cARD_DETAILS = cARD_DETAILS;
            this.iDENTIFIERS = iDENTIFIERS;
        }
    }
}