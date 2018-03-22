package models;

import javafx.beans.property.SimpleStringProperty;

public class Immunization {
    private final SimpleStringProperty name;
    private final SimpleStringProperty dateAdministered;

    public Immunization(String name, String dateAdministered) {
        this.name = new SimpleStringProperty(name);
        this.dateAdministered = new SimpleStringProperty(dateAdministered);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDateAdministered() {
        return dateAdministered.get();
    }

    public SimpleStringProperty dateAdministeredProperty() {
        return dateAdministered;
    }

    public void setDateAdministered(String dateAdministered) {
        this.dateAdministered.set(dateAdministered);
    }
}