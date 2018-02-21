package models;

import javafx.beans.property.SimpleStringProperty;

public class HIVTest {
    public final SimpleStringProperty testDate;
    public final SimpleStringProperty result;
    public final SimpleStringProperty type;
    public final SimpleStringProperty facility;
    public final SimpleStringProperty strategy;

    public HIVTest(String testDate, String result, String type, String facility, String strategy) {
        this.testDate = new SimpleStringProperty(testDate);
        this.result = new SimpleStringProperty(result);
        this.type = new SimpleStringProperty(type);
        this.facility = new SimpleStringProperty(facility);
        this.strategy = new SimpleStringProperty(strategy);
    }

    public String getTestDate() {
        return testDate.get();
    }

    public SimpleStringProperty testDateProperty() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate.set(testDate);
    }

    public String getResult() {
        return result.get();
    }

    public SimpleStringProperty resultProperty() {
        return result;
    }

    public void setResult(String result) {
        this.result.set(result);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getFacility() {
        return facility.get();
    }

    public SimpleStringProperty facilityProperty() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility.set(facility);
    }

    public String getStrategy() {
        return strategy.get();
    }

    public SimpleStringProperty strategyProperty() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy.set(strategy);
    }
}
