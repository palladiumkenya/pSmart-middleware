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

    public static String getHivTestSampleData () {
        return "'HIV_TEST':[{'DATE':'20180101','RESULT':'POSITIVE/NEGATIVE/INCONCLUSIVE','TYPE':" +
                "'SCREENING/CONFIRMATORY','FACILITY':'10829','STRATEGY':'HP/NP/VI/VS/HB/MO/O'," +
                "'PROVIDER_DETAILS':{'NAME':'AFYA JIJINI'}}]";
    }

    public static String getPatientDemographicsSampleData () {
        return  "{\n" +
                "\t\"DEMOGRAPHICS\": {\n" +
                "\t\t\"FIRST_NAME\": \"THERESA\",\n" +
                "\t\t\"MIDDLE_NAME\": \"MAY\",\n" +
                "\t\t\"LAST_NAME\": \"WAIRIMU\",\n" +
                "\t\t\"DATE_OF_BIRTH\": \"20171111\",\n" +
                "\t\t\"SEX\": \"F\",\n" +
                "\t\t\"PHONE_NUMBER\": \"254720278654\",\n" +
                "\t\t\"MARITAL_STATUS\": \"MARRIED\"\n" +
                "\t}\n" +
                "}";


    }

    public static String getPatientIdentifiersSampleData () {
        return "{\"IDENTIFIERS\":[{\"ID\":\"12345678-ADFGHJY-0987654-NHYI890\",\"TYPE\":\"CARD\",\"FACILITY\":\"10829\"},{\"ID\":\"12345678\",\"TYPE\":\"HEI\",\"FACILITY\":\"10829\"},{\"ID\":\"12345678\",\"TYPE\":\"CCC\",\"FACILITY\":\"10829\"},{\"ID\":\"001\",\"TYPE\":\"HTS\",\"FACILITY\":\"10829\"}]}";
    }

    public static String getPatientAddressSampleData () {
        return "'ADDRESS': {\n" +
                "\t'VILLAGE': 'KWAKIMANI',\n" +
                "        'WARD': 'KIMANINI',\n" +
                "        'SUB_COUNTY': 'KIAMBU EAST',\n" +
                "        'COUNTY': 'KIAMBU',\n" +
                "        'NEAREST_LANDMARK': 'KIAMBU EAST'\n" +
                "}";
    }

    public static String getCardDetails () {
        return "'CARD_DETAILS': {\n" +
                "    'STATUS': 'ACTIVE/INACTIVE',\n" +
                "    'REASON': 'LOST/DEATH/DAMAGED',\n" +
                "    'LAST_UPDATED': '20180101',\n" +
                "    'LAST_UPDATED_FACILITY': '10829'\n" +
                "  }";
    }
   /*public static boolean isValidJSON(String SHRStr) {
        try {
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createParser(SHRStr);

            
            while (!parser.isClosed()) {
                JsonToken jsonToken = parser.nextToken();

                System.out.println("jsonToken = " + jsonToken);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }*/
}
