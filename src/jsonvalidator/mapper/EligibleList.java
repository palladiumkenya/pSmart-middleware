package jsonvalidator.mapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EligibleList {
    private final String patientId;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String gender;
    private final String age;

    @JsonCreator
    public EligibleList(@JsonProperty("PATIENTID") String patientId, @JsonProperty("FIRSTNAME") String firstName, @JsonProperty("MIDDLENAME") String middleName, @JsonProperty("LASTNAME") String lastName, @JsonProperty("GENDER") String gender, @JsonProperty("AGE") String age) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.gender = gender;
        this.age = age;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }
}
