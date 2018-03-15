/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonvalidator.apiclient;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author tedb19
 */
public class APIClient {
    private static final String USER_AGENT = "Mozilla/5.0";

    public static String fetchData(String SHRURL) {
        String SHRJsonStr = "";
        try {
            URL url = new URL(SHRURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            String username = "admin";
            String password = "Admin123";
            BASE64Encoder enc = new sun.misc.BASE64Encoder();
            String userpassword = username + ":" + password;
            String encodedAuthorization = enc.encode( userpassword.getBytes() );
            conn.setRequestProperty("Authorization", "Basic "+
                    encodedAuthorization);
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
    
    public static String postData(String SHRURL, String SHRStr) {
        StringBuffer response = new StringBuffer();
        try {
            URL url = new URL(SHRURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            String username = "admin";
            String password = "Admin123";
            BASE64Encoder enc = new sun.misc.BASE64Encoder();
            String userpassword = username + ":" + password;
            String encodedAuthorization = enc.encode( userpassword.getBytes() );
            conn.setRequestProperty("Authorization", "Basic "+
                    encodedAuthorization);
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(SHRStr.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            br.close();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
               response.append("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            System.out.println(response.toString());
            conn.disconnect();
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return response.toString();
    }

}
