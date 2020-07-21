/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonvalidator.apiclient;
import jsonvalidator.utils.SHRUtils;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static jsonvalidator.utils.EndpointUtils.getURL;

/**
 *
 * @author tedb19
 */
public class APIClient {
    private static final String USER_AGENT = "Mozilla/5.0";

    public static Map<String, String> fetchData(String SHRURL) {
        Map<String, String> response = new HashMap<>();

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
                response.put("response", conn.getResponseMessage());
                response.put("success", "false");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output = "";
            while ((output = br.readLine()) != null) {
                SHRJsonStr += output;
            }
            conn.disconnect();
            response.put("response", SHRJsonStr);
            response.put("success", "true");
        } catch (MalformedURLException e) {
            response.put("response", "MalformedURLException: "+ e.getMessage());
            response.put("success", "false");
        } catch (IOException e) {
            response.put("response", "IOException: " + e.getMessage());
            response.put("success", "false");
        }
        return response;
    }

    public static String postObject(Object obj, String purpose) {
        String url = getURL(purpose);
        String response;
        if(!url.isEmpty()){
            String objStr = SHRUtils.getJSON(obj);
            response = postData(url, objStr);
        } else {
            response = "\nPlease specify the `"+purpose+"` endpoint url!";
        }
        return response;
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
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            br.close();
            conn.disconnect();
        } catch (MalformedURLException e) {
            response.append(e.getMessage());
        } catch (IOException e) {
            response.append(e.getMessage());
        }
        return response.toString();
    }

}
