package models;

import javafx.beans.property.SimpleStringProperty;

public class EligiblePerson {
    private final SimpleStringProperty patientId;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty middleName;
    private final SimpleStringProperty gender;
    private final SimpleStringProperty age;

    public EligiblePerson(String patientId, String firstName, String lastName, String middleName, String gender, String age) {
        this.patientId = new SimpleStringProperty(patientId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.middleName = new SimpleStringProperty(middleName);
        this.gender = new SimpleStringProperty(gender);
        this.age = new SimpleStringProperty(age);
    }

    public String getPatientId() {
        return patientId.get();
    }

    public SimpleStringProperty patientIdProperty() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId.set(patientId);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public SimpleStringProperty middleNameProperty() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    public String getGender() {
        return gender.get();
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getAge() {
        return age.get();
    }

    public SimpleStringProperty ageProperty() {
        return age;
    }

    public void setAge(String age) {
        this.age.set(age);
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getMiddleName() + " " + this.getLastName();
    }
}
