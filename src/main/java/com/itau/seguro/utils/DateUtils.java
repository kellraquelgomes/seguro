package com.itau.seguro.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    public static DateTime converterStringParaDateTime(String data){

        return DateTime.parse(data, DateTimeFormat.forPattern("yyyy-MM-dd"));
    }

    public static DateTime converterDateParaDateTime(Date data){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZone(ZoneOffset.UTC);
        String utcFormatted = formatter.format(data.toInstant());
//
//        ZonedDateTime utcDatetime = data.toInstant().atZone(ZoneOffset.UTC);
//        String utcFormatted2 = utcDatetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).replace("T","");
//
//        ZonedDateTime localDatetime = data.toInstant().atZone(ZoneId.systemDefault());
//        String localFormatted = localDatetime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME).replace("T" , " ");

        DateTime dateTime = new DateTime(utcFormatted);
        return dateTime;
    }

}
