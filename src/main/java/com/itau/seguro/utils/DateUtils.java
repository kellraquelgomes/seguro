package com.itau.seguro.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

public class DateUtils {

    public static DateTime converterStringParaDateTime(String data){

        return DateTime.parse(data, DateTimeFormat.forPattern("yyyy-MM-dd"));

    }

    public static DateTime converterDategParaDateTime(Date data){
        Date date = data;
        DateTime dateTime = new DateTime(date);
        return dateTime;

    }


}
