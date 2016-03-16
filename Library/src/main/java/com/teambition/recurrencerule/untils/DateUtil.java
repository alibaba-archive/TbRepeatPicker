package com.teambition.recurrencerule.untils;

import android.text.TextUtils;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static Date parseISO8601(String dateStr, String format) {
        Date date = null;
        if (!TextUtils.isEmpty(dateStr)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            dateFormat.setTimeZone(timeZone);
            try {
                date = dateFormat.parse(dateStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }


    public static String formatISO8601(Date date, String format) {
        String result = "";
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            dateFormat.setTimeZone(timeZone);
            try {
                result = dateFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
        }
        return result;
    }

    /**
     * Converts one of the Calendar.SUNDAY constants to the SU, MO, etc.
     * constants.  btw, I think we should switch to those here too, to
     * get rid of this function, if possible.
     */
    public static int calendarDay2Day(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                return RRuleContants.SU;
            case Calendar.MONDAY:
                return RRuleContants.MO;
            case Calendar.TUESDAY:
                return RRuleContants.TU;
            case Calendar.WEDNESDAY:
                return RRuleContants.WE;
            case Calendar.THURSDAY:
                return RRuleContants.TH;
            case Calendar.FRIDAY:
                return RRuleContants.FR;
            case Calendar.SATURDAY:
                return RRuleContants.SA;
            default:
                throw new RuntimeException("bad day of week: " + day);
        }
    }

    /**
     * Get first day of week as android.text.format.Time constant.
     *
     * @return the first day of week in android.text.format.Time
     */
    public static int getFirstDayOfWeek() {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();

        if (startDay == Calendar.SATURDAY) {
            return Time.SATURDAY;
        } else if (startDay == Calendar.MONDAY) {
            return Time.MONDAY;
        } else {
            return Time.SUNDAY;
        }
    }

    public static String day2String(int day) {
        switch (day) {
            case RRuleContants.SU:
                return "SU";
            case RRuleContants.MO:
                return "MO";
            case RRuleContants.TU:
                return "TU";
            case RRuleContants.WE:
                return "WE";
            case RRuleContants.TH:
                return "TH";
            case RRuleContants.FR:
                return "FR";
            case RRuleContants.SA:
                return "SA";
            default:
                throw new IllegalArgumentException("bad day argument: " + day);
        }
    }
}
