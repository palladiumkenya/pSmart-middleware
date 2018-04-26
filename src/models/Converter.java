package models;

import javafx.util.StringConverter;

public class Converter extends StringConverter {
    String purpose = "";

    public Converter(String purpose) {
        this.purpose = purpose;
    }

    @Override
    public String toString(Object object) {
        String converted = "";

        if(object != null && this.purpose.startsWith("HTTP")) {
            converted = object.toString().startsWith("http") ? object.toString() : "http://"+object.toString();
        } else {
            converted = object.toString();
        }
        return converted;
    }

    @Override
    public Object fromString(String string) {
        return string;
    }
}