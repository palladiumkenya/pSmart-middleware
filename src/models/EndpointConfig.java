package models;

import javafx.beans.property.SimpleStringProperty;

public class EndpointConfig  {

    public SimpleStringProperty endpointUrl=new SimpleStringProperty();
    public SimpleStringProperty endpointPurpose=new SimpleStringProperty();

    public String getEndpointUrl(){
        return endpointUrl.get();
    }

    public String getEndpointPurpose(){
        return endpointPurpose.get();
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl.set(endpointUrl);
    }
}
