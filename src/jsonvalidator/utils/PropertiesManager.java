package jsonvalidator.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Endpoint;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesManager {
    public static final String FILE_SEPARATOR = File.separator;
    public static final String CONF_PATH = "conf" + FILE_SEPARATOR;
    private static final String STATIC_PROPERTIES_FILE = "psmart.properties";
    private static final String JSON_FILE = ".endpoints.json";
    private static Properties properties;

    public static final String DB_PASSWORD = getProperties().getProperty("password");
    public static final String DB_USERNAME = getProperties().getProperty("username");
    public static final String DB_HOST = getProperties().getProperty("host");
    public static final String DB_PORT = getProperties().getProperty("port");
    public static final String DB_NAME = getProperties().getProperty("dbName");

    /*
     * Reads all the properties from the iqcarecompanion file,
     * and stores them in memory.
     * Hits the properties file only once
     */
    protected static synchronized Properties getProperties(){

        if (properties != null) {
            return properties;
        }
        String propFileLocation = CONF_PATH + STATIC_PROPERTIES_FILE;
        try{
            FileInputStream input = new FileInputStream(propFileLocation);
            properties = new Properties();
            properties.load(input);

        } catch(IOException ex){
            StringBuilder sb = new StringBuilder();
            sb.append(" Oooops! The ")
                    .append(STATIC_PROPERTIES_FILE)
                    .append(" file could not be found at ")
                    .append(CONF_PATH);
            System.out.println(sb.toString());
        }
        return properties;
    }

    public static List<Endpoint> readJSONFile(){
        List<Endpoint> endpoints = new ArrayList<>();
        try {
            File file = new File(CONF_PATH + JSON_FILE);
            ObjectMapper mapper = new ObjectMapper();
            if(file.exists()) {
                byte[] jsonData = Files.readAllBytes(Paths.get(CONF_PATH + JSON_FILE));
                endpoints = mapper.readValue(jsonData, new TypeReference<List<Endpoint>>() {});
            } else {
                System.out.println("The json file does not exist. Attempting to create it... ");
                String content = "[{\"endpointUrl\":\"\",\"endpointPurpose\":\"HTTP POST - Push Authentication credentials to EMR, get back an auth token\"},{\"endpointUrl\":\"\",\"endpointPurpose\":\"HTTP GET - Fetch SHR from EMR. Takes Card serial as parameter\"},{\"endpointUrl\":\"\",\"endpointPurpose\":\"HTTP GET - Fetch eligible list from EMR\"},{\"endpointUrl\":\"\",\"endpointPurpose\":\"HTTP POST - Push SHR to EMR\"},{\"endpointUrl\":\"\",\"endpointPurpose\":\"HTTP GET - Fetch inactive cards from Registry\"},{\"endpointUrl\":\"\",\"endpointPurpose\":\"HTTP POST - Push encrypted SHR to EMR\"},{\"endpointUrl\":\"\",\"endpointPurpose\":\"HTTP POST - Push the card assignment details to EMR\"}]";
                writeFile(content);
                endpoints = mapper.readValue(content.getBytes(), new TypeReference<List<Endpoint>>(){});
            }
        } catch (Exception ex) {
            System.out.println("An error occurred while reading the json file: " + ex);
        }
        return endpoints;
    }

    public static void writeFile(String content) {
        File file = new File(CONF_PATH + JSON_FILE);

        try (FileOutputStream fop = new FileOutputStream(file)) {

            if (!file.exists()) {
                boolean isCreated = file.getCanonicalFile().createNewFile();
                if(!isCreated) {
                    System.out.println("The file not created");
                }
            }

            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
