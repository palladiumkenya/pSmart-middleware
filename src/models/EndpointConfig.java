package models;

import javafx.beans.property.SimpleStringProperty;

public class EndpointConfig  {

    public SimpleStringProperty endpointType=new SimpleStringProperty();
    public SimpleStringProperty endpointName=new SimpleStringProperty();
    public SimpleStringProperty endpointUrl=new SimpleStringProperty();
    public SimpleStringProperty endpointAction =new SimpleStringProperty();
    public SimpleStringProperty endpointPurpose=new SimpleStringProperty();
    public SimpleStringProperty endpointUsername=new SimpleStringProperty();
    public SimpleStringProperty endpointPassword =new SimpleStringProperty();


    public String getEndpointType(){
        return endpointType.get();
    }

    public String getEndpointName(){
        return endpointName.get();
    }

    public String getEndpointUrl(){
        return endpointUrl.get();
    }

    public String getEndpointAction(){
        return endpointAction.get();
    }

    public String getEndpointPurpose(){
        return endpointPurpose.get();
    }

    public String getEndpointUsername(){
        return   endpointUsername.get();
    }

    public String getEndpointPassword(){
        return endpointPassword.get();
    }

}
