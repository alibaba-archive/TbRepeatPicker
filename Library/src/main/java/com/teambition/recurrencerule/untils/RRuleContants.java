package com.teambition.recurrencerule.untils;

import java.util.Calendar;

public class RRuleContants {

    public static final int SECONDLY = 1;
    public static final int MINUTELY = 2;
    public static final int HOURLY = 3;
    public static final int DAILY = 4;
    public static final int WEEKLY = 5;
    public static final int MONTHLY = 6;
    public static final int YEARLY = 7;

    public static final int SU = 0x00010000;
    public static final int MO = 0x00020000;
    public static final int TU = 0x00040000;
    public static final int WE = 0x00080000;
    public static final int TH = 0x00100000;
    public static final int FR = 0x00200000;
    public static final int SA = 0x00400000;


    public static final int[] TIME_DAY_TO_CALENDAR_DAY = new int[]{
            Calendar.SUNDAY,
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
    };


    public  static final int[] TIME_MONTH_TO_CALENDAR_MONTH = new int[]{
            Calendar.JANUARY,
            Calendar.FEBRUARY,
            Calendar.MARCH,
            Calendar.APRIL,
            Calendar.MAY,
            Calendar.JUNE,
            Calendar.JULY,
            Calendar.AUGUST,
            Calendar.SEPTEMBER,
            Calendar.OCTOBER,
            Calendar.NOVEMBER,
            Calendar.DECEMBER
    };

}
