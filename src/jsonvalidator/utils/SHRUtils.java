/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonvalidator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonvalidator.mapper.EligibleList;
import jsonvalidator.mapper.SHR;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author tedb19
 */
public class SHRUtils {

    public static SHR.CARD_DETAILS getCardDetailsObj(String SHRStr) {
        ObjectMapper mapper = new ObjectMapper();
        SHR.CARD_DETAILS card_details = null;
        try {
            card_details = mapper.readValue(SHRStr, new TypeReference<SHR.CARD_DETAILS>() {
            });
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return card_details;
    }

    public static SHR getSHRObj(String SHRStr) {
        System.out.println(SHRStr);
        ObjectMapper mapper = new ObjectMapper();
        SHR shr = null;
        try {
            shr = mapper.readValue(SHRStr, new TypeReference<SHR>() {
            });
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return shr;
    }

    public static List<EligibleList> getEligibleList(String eligibleListStr) {
        ObjectMapper mapper = new ObjectMapper();
        List<EligibleList> eligibleList = null;
        try {
            eligibleList = mapper.readValue(eligibleListStr, new TypeReference<List<EligibleList>>() {
            });
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return eligibleList;
    }

    public static String getJSON(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String JSONStr = "";
        try {
            JSONStr = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }
        return JSONStr;
    }

    public static String getCardSerialNo(SHR shr) {
        String cardSerialNo = "";
        for (SHR.PATIENT_IDENTIFICATION.INTERNAL_PATIENT_ID internal_patient_id : shr.pATIENT_IDENTIFICATION.iNTERNAL_PATIENT_ID) {
            if(internal_patient_id.iDENTIFIER_TYPE.equals("CARD_SERIAL_NUMBER")){
                cardSerialNo = internal_patient_id.iD;
            }
        }
        return cardSerialNo;
    }

}
