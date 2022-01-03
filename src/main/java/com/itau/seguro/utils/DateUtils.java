package com.itau.seguro.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateUtils {

    public static DateTime converterStringParaDateTime(String data){

        return DateTime.parse(data, DateTimeFormat.forPattern("yyyy-MM-dd"));
    }

}
