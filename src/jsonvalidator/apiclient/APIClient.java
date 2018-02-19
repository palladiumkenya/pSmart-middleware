/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonvalidator.apiclient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author tedb19
 */
public class APIClient {

    public static String getSHRStr(String SHRURL, String cardSerial) {
        String SHRJsonStr = "";
        try {
            //cardSerial will be passed to the GET endpoint
            URL url = new URL(SHRURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            System.out.println("Output from Server .... \n");
            String output = "";
            while ((output = br.readLine()) != null) {
                //System.out.println(SHRJsonStr);
                SHRJsonStr += output;
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            System.out.println("MalformedURLException: " + e.getMessage());

        } catch (IOException e) {

            System.out.println("IOException: " + e.getMessage());

        }
        return SHRJsonStr;
    }
    
    public static String postSHR(String SHRURL, String SHRStr) {
        String response = "";
        try {
            URL url = new URL(SHRURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(SHRStr.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                response += output;
            }

            conn.disconnect();
            
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return response;
    }

}
