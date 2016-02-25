package com.fishcart.delivery.util;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author arsh
 */
public class Util {
    public static String getCurrentTime(){
        Date today = new Date();
      
        //displaying this date on IST timezone
        DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return df.format(today);

    }
     public static String getIndianTime(Timestamp time){
        Date today = Date.from(time.toInstant());
        //displaying this date on IST timezone
        DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return df.format(today);

    }
}
