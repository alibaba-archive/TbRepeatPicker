package com.teambition.recurrencerule;

import android.content.Context;
import android.text.TextUtils;

import com.teambition.recurrence.R;
import com.teambition.recurrencerule.untils.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TbRrule implements RRule {
    private static final String RRULE = "RRULE:";
    private static final String FREQ = "FREQ=";
    private static final String INTERVAL = "INTERVAL=";
    private static final String DTSTART = "DTSTART=";
    private static final String BYDAY = "BYDAY=";
    private static final String BYMONTHDAY = "BYMONTHDAY=";
    private static final String BYMONTH = "BYMONTH=";

    private static final String DAILY = "DAILY";
    private static final String WEEKLY = "WEEKLY";
    private static final String MONTHLY = "MONTHLY";
    private static final String YEARLY = "YEARLY";

    private static final String SU = "SU";
    private static final String MO = "MO";
    private static final String TU = "TU";
    private static final String WE = "WE";
    private static final String TH = "TH";
    private static final String FR = "FR";
    private static final String SA = "SA";

    private static final String DATE_FORMAT_DTSTART = "yyyyMMdd'T'HHmmss'Z'";


    @Override
    public RecurrenceModel parseRRule(String[] ruleArray) {
        if (ruleArray == null || ruleArray.length == 0) {
            return null;
        }
        String freq = "";
        Date dtStart = null;
        int interval = 1;
        String[] byDay = null;
        String[] byMonthDay = null;
        String[] byMonth = null;

        for (int i = 0; i < ruleArray.length; i++) {
            String rRuleStr = ruleArray[i];
            if (rRuleStr.contains(RRULE)) {
                String[] strs = rRuleStr.replace(RRULE, "").split(";");
                for (String rule : strs) {
                    if (rule.contains(FREQ)) {
                        freq = rule.replace(FREQ, "");
                    } else if (rule.contains(DTSTART)) {
                        String dateStr = rule.replace(DTSTART, "");
                        dtStart = DateUtil.parseISO8601(dateStr, DATE_FORMAT_DTSTART);
                    } else if (rule.contains(INTERVAL)) {
                        interval = Integer.parseInt(rule.replace(INTERVAL, ""));
                    } else if (rule.contains(BYDAY)) {
                        String strByday = rule.replace(BYDAY, "");
                        byDay = strByday.split(",");
                    } else if (rule.contains(BYMONTHDAY)) {
                        String strBymonthday = rule.replace(BYMONTHDAY, "");
                        byMonthDay = strBymonthday.split(",");
                    } else if (rule.contains(BYMONTH)) {
                        String strBymonth = rule.replace(BYMONTH, "");
                        byMonth = strBymonth.split(",");
                    }
                }
                break;
            }
        }
        RecurrenceModel model = new RecurrenceModel();
        if (DAILY.equalsIgnoreCase(freq)) {
            model.freq = RecurrenceModel.FREQ_DAILY;
        } else if (WEEKLY.equalsIgnoreCase(freq)) {
            model.freq = RecurrenceModel.FREQ_WEEKLY;
        } else if (MONTHLY.equalsIgnoreCase(freq)) {
            model.freq = RecurrenceModel.FREQ_MONTHLY;
        } else if (YEARLY.equalsIgnoreCase(freq)) {
            model.freq = RecurrenceModel.FREQ_YEARLY;
        }

        model.interval = interval;
        model.start = dtStart;

        if (byDay != null && byDay.length > 0) {
            for (String day : byDay) {
                if (SU.equalsIgnoreCase(day)) {
                    model.weeklyByDayOfWeek[0] = true;
                } else if (MO.equalsIgnoreCase(day)) {
                    model.weeklyByDayOfWeek[1] = true;
                } else if (TU.equalsIgnoreCase(day)) {
                    model.weeklyByDayOfWeek[2] = true;
                } else if (WE.equalsIgnoreCase(day)) {
                    model.weeklyByDayOfWeek[3] = true;
                } else if (TH.equalsIgnoreCase(day)) {
                    model.weeklyByDayOfWeek[4] = true;
                } else if (FR.equalsIgnoreCase(day)) {
                    model.weeklyByDayOfWeek[5] = true;
                } else if (SA.equalsIgnoreCase(day)) {
                    model.weeklyByDayOfWeek[6] = true;
                }
            }
        }

        if (byMonthDay != null && byMonthDay.length > 0) {
            for (String day : byMonthDay) {
                int dayOfMonth = Integer.parseInt(day);
                if (dayOfMonth < 1 || dayOfMonth > 31) {
                    continue;
                }
                model.monthlyByDayOfMonth[dayOfMonth - 1] = true;
            }
        }

        if (byMonth != null && byMonth.length > 0) {
            for (String day : byMonth) {
                int monthOfYear = Integer.parseInt(day);
                if (monthOfYear < 1 || monthOfYear > 12) {
                    continue;
                }
                model.yearlyByMonthOfYear[monthOfYear - 1] = true;
            }
        }

        return model;
    }

    @Override
    public String displayRRule(Context context, RecurrenceModel model) {
        final String noRepeatStr = context.getString(R.string.recurrence_no_repeat);
        final String everyDayStr = context.getString(R.string.recurrence_every_day);
        final String everyWeekStr = context.getString(R.string.recurrence_every_week);
        final String everyMonthStr = context.getString(R.string.recurrence_every_month);
        final String everyYearStr = context.getString(R.string.recurrence_every_year);
        final String everyWeekdayStr = context.getString(R.string.recurrence_every_weekday);
        final String custom = context.getString(R.string.recurrence_custom);


        String displayInfo = noRepeatStr;

        GregorianCalendar calendar = new GregorianCalendar();
        if (model != null && model.start != null) {
            calendar.setTime(model.start);
        }
        if (model == null) {
            displayInfo = noRepeatStr;
        } else if (model.freq == RecurrenceModel.FREQ_DAILY) {
            if (model.interval == 1) {
                displayInfo = everyDayStr;
            } else {
                displayInfo = custom;
            }
        } else if (model.freq == RecurrenceModel.FREQ_WEEKLY) {
            if (model.interval == 1) {

                displayInfo = custom; //default.

                int repeatDayCounts = 0;
                for (boolean isRepeat : model.weeklyByDayOfWeek) {
                    repeatDayCounts = repeatDayCounts + (isRepeat ? 1 : 0);
                }
                if (repeatDayCounts == 1) {
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //from 1 ---> 7;
                    if (model.weeklyByDayOfWeek[dayOfWeek - 1]) {
                        displayInfo = everyWeekStr; //repeat every week.
                    }
                }
                if (repeatDayCounts == 5) {
                    if (model.weeklyByDayOfWeek[1] && // Monday
                        model.weeklyByDayOfWeek[2] && // Tuesday
                        model.weeklyByDayOfWeek[3] && // Wednesday
                        model.weeklyByDayOfWeek[4] && // Thursday
                        model.weeklyByDayOfWeek[5]) { // Friday
                        displayInfo = everyWeekdayStr; // repeat every weekday.
                    }
                }
            } else {
                displayInfo = custom;
            }
        } else if (model.freq == RecurrenceModel.FREQ_MONTHLY) {
            displayInfo = custom; //default.
            if (model.interval == 1) {
                int repeatDayCounts = 0;
                for (boolean isRepeat : model.monthlyByDayOfMonth) {
                    repeatDayCounts = repeatDayCounts + (isRepeat ? 1 : 0);
                }
                if (repeatDayCounts == 1) {
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); //from 1 ---> 31
                    if (model.monthlyByDayOfMonth[dayOfMonth - 1]) {
                        displayInfo = everyMonthStr; //repeat every month.
                    }
                }
            }
        } else if (model.freq == RecurrenceModel.FREQ_YEARLY) {
            displayInfo = custom; // default.
            if (model.interval == 1) {
                int repeatMonthCounts = 0;
                for (boolean isRepeat : model.yearlyByMonthOfYear) {
                    repeatMonthCounts = repeatMonthCounts + (isRepeat ? 1 : 0);
                }
                if (repeatMonthCounts == 1) {
                    int monthOfYear = calendar.get(Calendar.MONTH);
                    if (model.yearlyByMonthOfYear[monthOfYear]) {
                        displayInfo = everyYearStr; //repeat every year.
                    }
                }
            }
        }
        return displayInfo;
    }


    @Override
    public String generateRRule(RecurrenceModel model, Date startDate) {
        StringBuilder s = new StringBuilder();

        s.append("FREQ=");
        switch (model.freq) {
//            case RecurrenceModel.FREQ_SECONDLY:
//                s.append("SECONDLY");
//                break;
//            case RecurrenceModel.FREQ_MINUTELY:
//                s.append("MINUTELY");
//                break;
//            case RecurrenceModel.FREQ_HOURLY:
//                s.append("HOURLY");
//                break;
            case RecurrenceModel.FREQ_DAILY:
                s.append("DAILY");
                break;
            case RecurrenceModel.FREQ_WEEKLY:
                s.append("WEEKLY");
                break;
            case RecurrenceModel.FREQ_MONTHLY:
                s.append("MONTHLY");
                break;
            case RecurrenceModel.FREQ_YEARLY:
                s.append("YEARLY");
                break;
        }

        if (!TextUtils.isEmpty(model.until)) {
            s.append(";UNTIL=");
            s.append(model.until);
        }

        if (model.count != 0) {
            s.append(";COUNT=");
            s.append(model.count);
        }

        if (model.interval != 0) {
            s.append(";INTERVAL=");
            s.append(model.interval);
        }

        if (model.wkst != 0) {
            s.append(";WKST=");
            s.append(DateUtil.day2String(model.wkst));
        }

        appendNumbers(s, ";BYSECOND=", model.bySecondCount, model.bySecond);
        appendNumbers(s, ";BYMINUTE=", model.byMinuteCount, model.byMinute);
        appendNumbers(s, ";BYSECOND=", model.byHourCount, model.byHour);

        // day
        int count = model.byDayCount;
        if (count > 0) {
            s.append(";BYDAY=");
            count--;
            for (int i = 0; i < count; i++) {
                appendByDay(model, s, i);
                s.append(",");
            }
            appendByDay(model, s, count);
        }

        appendNumbers(s, ";BYMONTHDAY=", model.byMonthDayCount, model.byMonthDay);
        appendNumbers(s, ";BYYEARDAY=", model.byYeardayCount, model.byYearDay);
        appendNumbers(s, ";BYWEEKNO=", model.byWeeknoCount, model.byWeekno);
        appendNumbers(s, ";BYMONTH=", model.byMonthCount, model.byMonth);
        appendNumbers(s, ";BYSETPOS=", model.bySetPosCount, model.bySetPos);

        String rrule = s.toString();
        if (!TextUtils.isEmpty(rrule) && !rrule.contains("RRULE")) {
            rrule = "RRULE:" + rrule;
        }
        if (!TextUtils.isEmpty(rrule) && !rrule.contains("DTSTART")) {
            rrule = addDTSTART(rrule, startDate);
        }

        return rrule;
    }


    private void appendNumbers(StringBuilder s, String label,
                               int count, int[] values) {
        if (count > 0) {
            s.append(label);
            count--;
            for (int i = 0; i < count; i++) {
                s.append(values[i]);
                s.append(",");
            }
            s.append(values[count]);
        }
    }

    private void appendByDay(RecurrenceModel model, StringBuilder s, int i) {
        int n = model.byDayNum[i];
        if (n != 0) {
            s.append(n);
        }

        String str = DateUtil.day2String(model.byDay[i]);
        s.append(str);
    }


    private String addDTSTART(String rrule, Date startDate) {
        if (TextUtils.isEmpty(rrule)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        final String REGEX = "FREQ=(\\w+);";
        final String REPLACE = "FREQ=$1;" + "DTSTART=" + DateUtil.formatISO8601(startDate, "yyyyMMdd'T'HHmmss'Z'") + ";";

        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(rrule);
        while (m.find()) {
            m.appendReplacement(sb, REPLACE);
        }
        m.appendTail(sb);
        return sb.toString();
    }


}
