/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonvalidator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonvalidator.mapper.SHR;

import java.io.IOException;

/**
 *
 * @author tedb19
 */
public class SHRUtils {

    public static SHR getSHR(String SHRStr) {
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

    public static String getJSON(SHR shr) {
        ObjectMapper mapper = new ObjectMapper();
        String JSONStr = "";
        try {
            JSONStr = mapper.writeValueAsString(shr);
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }
        return JSONStr;
    }

    public static String getHivTestSampleData () {
        return "\"HIV_TEST\":[{\"DATE\":\"20180101\",\"RESULT\":\"INCONCLUSIVE\",\"TYPE\":\"CONFIRMATORY\",\"FACILITY\":\"10829\",\"STRATEGY\":\"HP\",\"PROVIDER_DETAILS\":{\"NAME\":\"MATTHEW NJOROGE, MD\",\"ID\":\"12345-67890-abcde\"}}]";
    }

    public static String getPatientDemographicsSampleData () {
        return  "{\n" +
                "\t\"DEMOGRAPHICS\": {\n" +
                "\t\t\"FIRST_NAME\": \"JOHN\",\n" +
                "\t\t\"MIDDLE_NAME\": \"PARKER\",\n" +
                "\t\t\"LAST_NAME\": \"DBOO\",\n" +
                "\t\t\"DATE_OF_BIRTH\": \"20121010\",\n" +
                "\t\t\"SEX\": \"F\",\n" +
                "\t\t\"PHONE_NUMBER\": \"254720278654\",\n" +
                "\t\t\"MARITAL_STATUS\": \"SINGLE\"\n" +
                "\t}\n" +
                "}";


    }
    public static String getPatientName() {
        return "\"PATIENT_NAME\":{\"FIRST_NAME\":\"THERESA\",\"MIDDLE_NAME\":\"MAY\",\"LAST_NAME\":\"WAIRIMU\"},\"DATE_OF_BIRTH\":\"20171111\",\"DATE_OF_BIRTH_PRECISION\":\"ESTIMATED/EXACT\",\"SEX\":\"F\",\"DEATH_DATE\":\"\",\"DEATH_INDICATOR\":\"N\"";
    }

    public static String getExternalPatientIdentifier () {
        return "\"EXTERNAL_PATIENT_ID\":{\"ID\":\"110ec58a-a0f2-4ac4-8393-c866d813b8d1\",\"IDENTIFIER_TYPE\":\"GODS_NUMBER\",\"ASSIGNING_AUTHORITY\":\"MPI\",\"ASSIGNING_FACILITY\":\"10829\"}";
    }

    public static String getInternalPatientIdentifiers () {
        return "\"INTERNAL_PATIENT_ID\":[{\"ID\":\"12345678\",\"IDENTIFIER_TYPE\":\"HEI_NUMBER\",\"ASSIGNING_AUTHORITY\":\"MCH\",\"ASSIGNING_FACILITY\":\"10829\"},{\"ID\":\"001\",\"IDENTIFIER_TYPE\":\"HTS_NUMBER\",\"ASSIGNING_AUTHORITY\":\"HTS\",\"ASSIGNING_FACILITY\":\"10829\"}]";
    }

    public static String getPatientIdentifiersSampleData () {
        return "{\"IDENTIFIERS\":[{\"ID\":\"12345678-ADFGHJY-0987654-NHYI890\",\"TYPE\":\"CARD\",\"FACILITY\":\"40829\"},{\"ID\":\"37645678\",\"TYPE\":\"HEI\",\"FACILITY\":\"10829\"},{\"ID\":\"12345678\",\"TYPE\":\"CCC\",\"FACILITY\":\"10829\"},{\"ID\":\"001\",\"TYPE\":\"HTS\",\"FACILITY\":\"10829\"}]}";
    }

    public static String getPatientAddressSampleData () {
        return "\"PATIENT_ADDRESS\":{\"PHYSICAL_ADDRESS\":{\"VILLAGE\":\"KWAKIMANI\",\"WARD\":\"KIMANINI\",\"SUB_COUNTY\":\"KIAMBU EAST\",\"COUNTY\":\"KIAMBU\",\"NEAREST_LANDMARK\":\"KIAMBU EAST\"}";
    }

    public static String getCardDetails () {
        return "\"CARD_DETAILS\":{\"STATUS\":\"ACTIVE/INACTIVE\",\"REASON\":\"LOST/DEATH/DAMAGED\",\"LAST_UPDATED\":\"20180101\",\"LAST_UPDATED_FACILITY\":\"10829\"}";
    }

    public static String getImmunizationDetails () {
        return "\"IMMUNIZATION\":[{\"NAME\":\"BCG/OPV_AT_BIRTH/OPV1/OPV2/OPV3/PCV10-1/PCV10-2/PCV10-3/PENTA1/PENTA2/PENTA3/MEASLES6/MEASLES9/MEASLES18/ROTA1/ROTA2\",\"DATE_ADMINISTERED\":\"20180101\"}]";
    }


   public static String getFullSHR () {
       return "{\"VERSION\":\"1.0.0\",\"PATIENT_IDENTIFICATION\":{\"EXTERNAL_PATIENT_ID\":{\"ID\":\"110ec58a-a0f2-4ac4-8393-c866d813b8d1\",\"IDENTIFIER_TYPE\":\"GODS_NUMBER\",\"ASSIGNING_AUTHORITY\":\"MPI\",\"ASSIGNING_FACILITY\":\"10829\"},\"INTERNAL_PATIENT_ID\":[{\"ID\":\"12345678-ADFGHJY-0987654-NHYI890\",\"IDENTIFIER_TYPE\":\"CARD_SERIAL_NUMBER\",\"ASSIGNING_AUTHORITY\":\"CARD_REGISTRY\",\"ASSIGNING_FACILITY\":\"10829\"},{\"ID\":\"12345678\",\"IDENTIFIER_TYPE\":\"HEI_NUMBER\",\"ASSIGNING_AUTHORITY\":\"MCH\",\"ASSIGNING_FACILITY\":\"10829\"},{\"ID\":\"12345678\",\"IDENTIFIER_TYPE\":\"CCC_NUMBER\",\"ASSIGNING_AUTHORITY\":\"CCC\",\"ASSIGNING_FACILITY\":\"10829\"},{\"ID\":\"001\",\"IDENTIFIER_TYPE\":\"HTS_NUMBER\",\"ASSIGNING_AUTHORITY\":\"HTS\",\"ASSIGNING_FACILITY\":\"10829\"},{\"ID\":\"ABC567\",\"IDENTIFIER_TYPE\":\"ANC_NUMBER\",\"ASSIGNING_AUTHORITY\":\"ANC\",\"ASSIGNING_FACILITY\":\"10829\"}],\"PATIENT_NAME\":{\"FIRST_NAME\":\"THERESA\",\"MIDDLE_NAME\":\"MAY\",\"LAST_NAME\":\"WAIRIMU\"},\"DATE_OF_BIRTH\":\"20171111\",\"DATE_OF_BIRTH_PRECISION\":\"ESTIMATED/EXACT\",\"SEX\":\"F\",\"DEATH_DATE\":\"\",\"DEATH_INDICATOR\":\"N\",\"PATIENT_ADDRESS\":{\"PHYSICAL_ADDRESS\":{\"VILLAGE\":\"KWAKIMANI\",\"WARD\":\"KIMANINI\",\"SUB_COUNTY\":\"KIAMBU EAST\",\"COUNTY\":\"KIAMBU\",\"NEAREST_LANDMARK\":\"KIAMBU EAST\"},\"POSTAL_ADDRESS\":\"789 KIAMBU\"},\"PHONE_NUMBER\":\"254720278654\",\"MARITAL_STATUS\":\"\",\"MOTHER_DETAILS\":{\"MOTHER_NAME\":{\"FIRST_NAME\":\"WAMUYU\",\"MIDDLE_NAME\":\"MARY\",\"LAST_NAME\":\"WAITHERA\"},\"MOTHER_IDENTIFIER\":[{\"ID\":\"1234567\",\"IDENTIFIER_TYPE\":\"NATIONAL_ID\",\"ASSIGNING_AUTHORITY\":\"GOK\",\"ASSIGNING_FACILITY\":\"\"},{\"ID\":\"12345678\",\"IDENTIFIER_TYPE\":\"NHIF\",\"ASSIGNING_AUTHORITY\":\"NHIF\",\"ASSIGNING_FACILITY\":\"\"},{\"ID\":\"12345-67890\",\"IDENTIFIER_TYPE\":\"CCC_NUMBER\",\"ASSIGNING_AUTHORITY\":\"CCC\",\"ASSIGNING_FACILITY\":\"10829\"},{\"ID\":\"12345678\",\"IDENTIFIER_TYPE\":\"PMTCT_NUMBER\",\"ASSIGNING_AUTHORITY\":\"PMTCT\",\"ASSIGNING_FACILITY\":\"10829\"}]}},\"NEXT_OF_KIN\":[{\"NOK_NAME\":{\"FIRST_NAME\":\"WAIGURU\",\"MIDDLE_NAME\":\"KIMUTAI\",\"LAST_NAME\":\"WANJOKI\"},\"RELATIONSHIP\":\"**AS DEFINED IN GREENCARD\",\"ADDRESS\":\"4678 KIAMBU\",\"PHONE_NUMBER\":\"25489767899\",\"SEX\":\"F\",\"DATE_OF_BIRTH\":\"19871022\",\"CONTACT_ROLE\":\"T\"}],\"HIV_TEST\":[{\"DATE\":\"20180101\",\"RESULT\":\"POSITIVE/NEGATIVE/INCONCLUSIVE\",\"TYPE\":\"SCREENING/CONFIRMATORY\",\"FACILITY\":\"10829\",\"STRATEGY\":\"HP/NP/VI/VS/HB/MO/O\",\"PROVIDER_DETAILS\":{\"NAME\":\"MATTHEW NJOROGE, MD\",\"ID\":\"12345-67890-abcde\"}}],\"IMMUNIZATION\":[{\"NAME\":\"BCG/OPV_AT_BIRTH/OPV1/OPV2/OPV3/PCV10-1/PCV10-2/PCV10-3/PENTA1/PENTA2/PENTA3/MEASLES6/MEASLES9/MEASLES18/ROTA1/ROTA2\",\"DATE_ADMINISTERED\":\"20180101\"}],\"CARD_DETAILS\":{\"STATUS\":\"ACTIVE/INACTIVE\",\"REASON\":\"LOST/DEATH/DAMAGED\",\"LAST_UPDATED\":\"20180101\",\"LAST_UPDATED_FACILITY\":\"10829\"}}";
   }

   public static String getSlicedSHR () {
       return "{\"VERSION\":\"1.0.0\",\"PATIENT_IDENTIFICATION\":\"},";
   }
}
