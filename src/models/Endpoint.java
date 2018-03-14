package models;

public class Endpoint {
    private String endpointUrl;
    private String endpointPurpose;
    private String endpointUsername;
    private String endpointPassword;

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getEndpointPurpose() {
        return endpointPurpose;
    }

    public void setEndpointPurpose(String endpointPurpose) {
        this.endpointPurpose = endpointPurpose;
    }

    public String getEndpointUsername() {
        return endpointUsername;
    }

    public void setEndpointUsername(String endpointUsername) {
        this.endpointUsername = endpointUsername;
    }

    public String getEndpointPassword() {
        return endpointPassword;
    }

    public void setEndpointPassword(String endpointPassword) {
        this.endpointPassword = endpointPassword;
    }
}
