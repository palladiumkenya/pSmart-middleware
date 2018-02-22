package models;

import javafx.beans.property.SimpleStringProperty;

public class Identifier {
    private final SimpleStringProperty identifier;
    private final SimpleStringProperty identifierType;
    private final SimpleStringProperty assigningAuthority;
    private final SimpleStringProperty assigningFacility;

    public Identifier(String identifier, String identifierType, String assigningAuthority, String assigningFacility) {
        this.identifier = new SimpleStringProperty(identifier);
        this.identifierType = new SimpleStringProperty(identifierType);
        this.assigningAuthority = new SimpleStringProperty(assigningAuthority);
        this.assigningFacility = new SimpleStringProperty(assigningFacility);
    }

    public String getIdentifier() {
        return identifier.get();
    }

    public SimpleStringProperty identifierProperty() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier.set(identifier);
    }

    public String getIdentifierType() {
        return identifierType.get();
    }

    public SimpleStringProperty identifierTypeProperty() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType.set(identifierType);
    }

    public String getAssigningAuthority() {
        return assigningAuthority.get();
    }

    public SimpleStringProperty assigningAuthorityProperty() {
        return assigningAuthority;
    }

    public void setAssigningAuthority(String assigningAuthority) {
        this.assigningAuthority.set(assigningAuthority);
    }

    public String getAssigningFacility() {
        return assigningFacility.get();
    }

    public SimpleStringProperty assigningFacilityProperty() {
        return assigningFacility;
    }

    public void setAssigningFacility(String assigningFacility) {
        this.assigningFacility.set(assigningFacility);
    }
}
