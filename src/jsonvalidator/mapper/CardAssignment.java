package jsonvalidator.mapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class CardAssignment {
    private final String PATIENTID;
    private final String CARD_SERIAL_NO;

    @JsonCreator
    public CardAssignment(@JsonProperty("PATIENTID") String PATIENTID, @JsonProperty("CARD_SERIAL_NO") String CARD_SERIAL_NO) {
        this.PATIENTID = PATIENTID;
        this.CARD_SERIAL_NO = CARD_SERIAL_NO;
    }

    @JsonProperty("PATIENTID")
    public String getPATIENTID() {
        return this.PATIENTID;
    }

    @JsonProperty("CARD_SERIAL_NO")
    public String getCARD_SERIAL_NO() {
        return this.CARD_SERIAL_NO;
    }
}
