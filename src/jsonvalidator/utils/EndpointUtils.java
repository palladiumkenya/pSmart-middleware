package jsonvalidator.utils;

import dbConnection.DBConnection;
import models.Endpoint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EndpointUtils {
    public static String getURL(String purpose) {
        String url = "";
        for (Endpoint endpoint : getEndPoints()) {
            if(endpoint.getEndpointPurpose().equals(purpose)){
                if(!(endpoint.getEndpointUrl().isEmpty() || endpoint.getEndpointUrl() == null)) {
                    url = endpoint.getEndpointUrl();
                }
            }
        }
        return url;
    }

    public static List<Endpoint> getEndPoints() {
        Endpoint endpoint;
        List<Endpoint> endpoints = new ArrayList<Endpoint>();
        try {
            Connection dbConn = DBConnection.connect();
            String sql = "Select * from endpoints WHERE void=0";
            ResultSet rs = dbConn.createStatement().executeQuery(sql);
            while(rs.next()){
                endpoint = new Endpoint();
                endpoint.setEndpointPurpose(rs.getString("endpointPurpose"));
                endpoint.setEndpointUrl(rs.getString("endpointUrl"));
                endpoint.setEndpointUsername(rs.getString("username"));
                endpoint.setEndpointPassword(rs.getString("password"));
                endpoints.add(endpoint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return endpoints;
    }
}
