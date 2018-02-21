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
        return "'HIV_TEST':[{'DATE':'20180101','RESULT':'POSITIVE/NEGATIVE/INCONCLUSIVE','TYPE':" +
                "'SCREENING/CONFIRMATORY','FACILITY':'10829','STRATEGY':'HP/NP/VI/VS/HB/MO/O'," +
                "'PROVIDER_DETAILS':{'NAME':'AFYA JIJINI'}}]";
    }

    public static String getPatientDemographicsSampleData () {
        return "'DEMOGRAPHICS': {\n" +
                "      'FIRST_NAME': 'THERESA',\n" +
                "      'MIDDLE_NAME': 'MAY',\n" +
                "      'LAST_NAME': 'WAIRIMU',\n" +
                "      'DATE_OF_BIRTH': '20171111',\n" +
                "      'SEX': 'F',\n" +
                "      'PHONE_NUMBER': '254720278654',\n" +
                "      'MARITAL_STATUS': 'MARRIED'\n" +
                "    }";
    }

    public static String getPatientIdentifiersSampleData () {
        return "'IDENTIFIERS':[{'ID':'12345678-ADFGHJY-0987654-NHYI890','TYPE':'CARD','FACILITY':'10829'}," +
                "{'ID':'12345678','TYPE':'HEI','FACILITY':'10829'},{'ID':'12345678','TYPE':'CCC','FACILITY':'10829'}," +
                "{'ID':'001','TYPE':'HTS','FACILITY':'10829'}]";
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
