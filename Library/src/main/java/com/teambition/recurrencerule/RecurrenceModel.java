package com.teambition.recurrencerule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecurrenceModel {

    public static final String SU = "SU";
    public static final String MO = "MO";
    public static final String TU = "TU";
    public static final String WE = "WE";
    public static final String TH = "TH";
    public static final String FR = "FR";
    public static final String SA = "SA";


    public enum RecurrenceOption {
        DOES_NOT_REPEAT("DOES NOT REPEAT"),
        DAILY("DAILY"), WEEKLY("WEEKLY"),
        MONTHLY("MONTHLY"), YEARLY("YEARLY"), WEEKDAY("WEEKDAY"), CUSTOM("CUSTOM"),
        ;

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


    public void setWeeklyByDayOfWeek(String[] byDay) {
        for (String day : byDay) {
            if (SU.equalsIgnoreCase(day)) {
                weeklyByDayOfWeek[0] = true;
            } else if (MO.equalsIgnoreCase(day)) {
                weeklyByDayOfWeek[1] = true;
            } else if (TU.equalsIgnoreCase(day)) {
                weeklyByDayOfWeek[2] = true;
            } else if (WE.equalsIgnoreCase(day)) {
                weeklyByDayOfWeek[3] = true;
            } else if (TH.equalsIgnoreCase(day)) {
                weeklyByDayOfWeek[4] = true;
            } else if (FR.equalsIgnoreCase(day)) {
                weeklyByDayOfWeek[5] = true;
            } else if (SA.equalsIgnoreCase(day)) {
                weeklyByDayOfWeek[6] = true;
            }
        }
    }

    public static List<String> getByWeekDay(RecurrenceModel model) {
        ArrayList<String> byS = new ArrayList<>();
        for (int i = 0; i < model.weeklyByDayOfWeek.length; i++) {
            if (model.weeklyByDayOfWeek[i]) {
                if (i == 0) {
                    byS.add(SU);
                } else if (i == 1) {
                    byS.add(MO);
                } else if (i == 2) {
                    byS.add(TU);
                } else if (i == 3) {
                    byS.add(WE);
                } else if (i == 4) {
                    byS.add(TH);
                } else if (i == 5) {
                    byS.add(FR);
                } else if (i == 6) {
                    byS.add(SA);
                }
            }
        }
        return byS;
    }


}
