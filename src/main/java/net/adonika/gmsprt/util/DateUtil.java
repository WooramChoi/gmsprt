package net.adonika.gmsprt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

public class DateUtil {

    public DateUtil() {
    }

    public static String dateToString(Date date) {
        return dateToString(date, null);
    }

    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat sdf;
        if (StringUtils.hasText(pattern)) {
            sdf = new SimpleDateFormat(pattern);
        } else {
            sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        }
        return sdf.format(date);
    }

    public static Date stringToDate(String txtDt) throws ParseException {
        return stringToDate(txtDt, null);
    }

    public static Date stringToDate(String txtDt, String pattern) throws ParseException {
        SimpleDateFormat sdf;
        if (StringUtils.hasText(pattern)) {
            sdf = new SimpleDateFormat(pattern);
        } else {
            sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        }
        return sdf.parse(txtDt);
    }
}
