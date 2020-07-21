package jsonvalidator.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormater {

    public static final String formatDate(String date_s) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
        String dateStr = "";
        try {
            Date date = dt.parse(date_s);
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy MMM dd");
            dateStr = dt1.format(date);
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }
        return dateStr;
    }
}
