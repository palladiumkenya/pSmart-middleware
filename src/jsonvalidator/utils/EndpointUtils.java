package jsonvalidator.utils;

import models.Endpoint;

public class EndpointUtils {
    public static String getURL(String purpose) {
        String url = "";
        for (Endpoint endpoint : PropertiesManager.readJSONFile()) {
            if(endpoint.getEndpointPurpose().equals(purpose)){
                if(!(endpoint.getEndpointUrl().isEmpty() || endpoint.getEndpointUrl() == null)) {
                    url = endpoint.getEndpointUrl();
                }
            }
        }
        return url;
    }
}
