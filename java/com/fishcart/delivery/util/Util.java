package com.fishcart.delivery.util;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public static Date getDate(String date,String time){
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return df.parse(date+" "+time);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static String previousDate(Date date){

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, -1);
            return df.format(c.getTime());
    
    }
    public static String getSqlDate(Date date){ 
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(date);
    }
    public static String getIndianTime(Timestamp time){
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(time.getTime());
        return cl.getTime().toString();
   

    }
//     public static String getIndianTime(Timestamp time){
//        Date today = Date.from(time.toInstant());
//        //displaying this date on IST timezone
//        DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
//        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
//        return df.format(today);
//
//    }


}
