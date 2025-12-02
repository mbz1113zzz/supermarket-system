package org.cityu.supermarket.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Date management utility
 *
 * @version 0.0.1
 * @date 2025-11-14
 */
public class DateManageSystem {
    /**
     * returns last week's Mon-Sun as string dates (time zeroed)
     * @return String[]
     * @throws ParseException
     */
    public String[] getLastWeek()  {
        Calendar cal = Calendar.getInstance();
        String[] week = new String[7];
        // n = week offset: 1=this week, -1=last week, 2=next week, etc
        int n = -1;
        cal.add(Calendar.DATE, n * 7);
        // pass Calendar.MONDAY, TUESDAY, etc for the day you want
        for (int i = 0; i < 7; i++) {
            cal.set(Calendar.DAY_OF_WEEK, 2 + i);
            // clear hour, minute, second, millisecond fields
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            week[i] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        }
        return week;
    }
/*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Calendar c = Calendar.getInstance();
1.past 7 days
c.setTime(new Date());
c.add(Calendar.DATE, - 7);
Date d = c.getTime();
String day = format.format(d);
System.out.println("past 7 days:"+day);
2.past one month
c.setTime(new Date());
c.add(Calendar.MONTH, -1);
Date m = c.getTime();
String mon = format.format(m);
System.out.println("past one month:"+mon);
3.past three months
c.setTime(new Date());
c.add(Calendar.MONTH, -3);
Date m3 = c.getTime();
String mon3 = format.format(m3);
System.out.println("past three months:"+mon3);
4.past one year
c.setTime(new Date());
c.add(Calendar.YEAR, -1);
Date y = c.getTime();
String year = format.format(y);
System.out.println("past one year:"+year);
5.current time in 24-hour format
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String nowDate=format.format(new Date());
System.out.println(nowDate);
6.start of today
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
Calendar c = Calendar.getInstance();
String start = format.format(c.getTime())+" 00:00:00";
System.out.println(start);
7.end of today
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
Calendar calendar = Calendar.getInstance();
String end = format.format(calendar.getTime())+" 23:59:59";
System.out.println(end);
8.start of one week ago
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
Calendar c = Calendar.getInstance();
c.add(Calendar.DAY_OF_MONTH, -6);
String start = format.format(c.getTime())+" 00:00:00";
System.out.println(start);
9.one month before today
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
Calendar c = Calendar.getInstance();
c.add(Calendar.MONTH, -1);    //get the previous month
String start = format.format(c.getTime())+" 00:00:00";
System.out.println(start);
10.start of one year ago
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
Calendar c = Calendar.getInstance();
c.add(Calendar.YEAR, -1); //subtract 1 year
String start =format.format(c.getTime())+" 00:00:00";
System.out.println(start);
11.Monday and Sunday of current week using setFirstDayOfWeek() method
SimpleDateFormat format  = new SimpleDateFormat("YYYY-MM-dd ");
Calendar c = Calendar.getInstance();
c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
String weekStart = format.format(c.getTime())+" 00:00:00";
System.out.println(weekStart);
Calendar ca = Calendar.getInstance();
ca.setFirstDayOfWeek(Calendar.MONDAY);
ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek() + 6); // Sunday
String weekEnd = format.format(ca.getTime())+" 23:59:59";
System.out.println(weekEnd);
12.first and last day of current month
SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
Calendar c = Calendar.getInstance();
c.set(Calendar.DAY_OF_MONTH,1);//set to 1st, current date is the first day of this month
String monthStart = format.format(c.getTime())+" 00:00:00";
System.out.println(monthStart);
Calendar ca = Calendar.getInstance();
ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
String monthEnd = format.format(ca.getTime())+" 23:59:59";
System.out.println(monthEnd);
13.start and end of current year
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
Calendar c = Calendar.getInstance();
c.set(c.get(Calendar.YEAR) ,0, 1);//start date
String yearStart = format.format(c.getTime())+" 00:00:00";
System.out.println(yearStart);
Calendar ca = Calendar.getInstance();
ca.set(ca.get(Calendar.YEAR) ,11, ca.getActualMaximum(Calendar.DAY_OF_MONTH));//end date
String yearEnd = format.format(ca.getTime())+" 23:59:59";
System.out.println(yearEnd);
14.calculate time difference in milliseconds
//get time difference in milliseconds
long val = calendarEnd.getTimeInMillis() - calendarBegin.getTimeInMillis();
//convert to days
long day = val / (1000 * 60 * 60 * 24);
15.get Monday and Sunday of last week for a given time
Calendar cal = Calendar.getInstance();
//n = week offset: 1=this week, -1=last week, 2=next week, etc
int n = 1;
String monday;
cal.add(Calendar.DATE, n*7);
//pass Calendar.MONDAY, TUESDAY, etc for the day you want
cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
monday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
System.out.println(monday);*/
}
