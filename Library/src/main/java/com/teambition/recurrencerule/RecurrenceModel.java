package com.teambition.recurrencerule;

import java.util.Date;

public class RecurrenceModel {

    public enum RecurrenceOption {
        DOES_NOT_REPEAT("DOES NOT REPEAT"),
        DAILY("DAILY"), WEEKLY("WEEKLY"), TWICE_WEEKLY("TWICE_WEEKLY"),
        MONTHLY("MONTHLY"), YEARLY("YEARLY"), WEEKDAY("WEEKDAY"), CUSTOM("CUSTOM"),;

        private final String optionName;

        RecurrenceOption(String name) {
            this.optionName = name;
        }

        public String toString() {
            return optionName;
        }
    }


//    public static final int FREQ_SECONDLY = 1;
//    public static final int FREQ_MINUTELY = 2;
//    public static final int FREQ_HOURLY = 3;
//    public static final int FREQ_DAILY = 4;
//    public static final int FREQ_WEEKLY = 5;
//    public static final int FREQ_MONTHLY = 6;
//    public static final int FREQ_YEARLY = 7;


    public static final int FREQ_DAILY = 0;
    public static final int FREQ_WEEKLY = 1;
    public static final int FREQ_MONTHLY = 2;
    public static final int FREQ_YEARLY = 3;

    static final int INTERVAL_DEFAULT = 1;


    public boolean[] weeklyByDayOfWeek = new boolean[7];

    public boolean[] monthlyByDayOfMonth = new boolean[31];

    public boolean[] yearlyByMonthOfYear = new boolean[12];

    public Date start;


    public int freq = INTERVAL_DEFAULT;
    public int interval = INTERVAL_DEFAULT;
    public String until;
    public int count;
    public int wkst;          // SU, MO, TU, etc.

    /* lists with zero entries may be null references */
    public int[] bySecond;
    public int bySecondCount;
    public int[] byMinute;
    public int byMinuteCount;
    public int[] byHour;
    public int byHourCount;
    public int[] byDay;
    public int[] byDayNum;
    public int byDayCount;
    public int[] byMonthDay;
    public int byMonthDayCount;
    public int[] byYearDay;
    public int byYeardayCount;
    public int[] byWeekno;
    public int byWeeknoCount;
    public int[] byMonth;
    public int byMonthCount;
    public int[] bySetPos;
    public int bySetPosCount;


}
