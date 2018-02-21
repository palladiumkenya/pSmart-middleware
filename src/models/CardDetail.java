package models;

import javafx.beans.property.SimpleStringProperty;

public class CardDetail {
    private final SimpleStringProperty status;
    private final SimpleStringProperty reason;
    private final SimpleStringProperty lastUpdated;
    private final SimpleStringProperty lastUpdatedFacility;

    public CardDetail(String status, String reason, String lastUpdated, String lastUpdatedFacility) {
        this.status = new SimpleStringProperty(status);
        this.reason = new SimpleStringProperty(reason);
        this.lastUpdated = new SimpleStringProperty(lastUpdated);
        this.lastUpdatedFacility = new SimpleStringProperty(lastUpdatedFacility);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getReason() {
        return reason.get();
    }

    public SimpleStringProperty reasonProperty() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason.set(reason);
    }

    public String getLastUpdated() {
        return lastUpdated.get();
    }

    public SimpleStringProperty lastUpdatedProperty() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated.set(lastUpdated);
    }

    public String getLastUpdatedFacility() {
        return lastUpdatedFacility.get();
    }

    public SimpleStringProperty lastUpdatedFacilityProperty() {
        return lastUpdatedFacility;
    }

    public void setLastUpdatedFacility(String lastUpdatedFacility) {
        this.lastUpdatedFacility.set(lastUpdatedFacility);
    }
}