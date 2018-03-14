package models;

import javafx.util.StringConverter;

public class Converter extends StringConverter {

    @Override
    public String toString(Object object) {
        String converted = "";
        if(object != null) {
            converted = object.toString().startsWith("http") ? object.toString() : "http://"+object.toString();
        }
        return converted;
    }

    @Override
    public Object fromString(String string) {
        return string;
    }
}