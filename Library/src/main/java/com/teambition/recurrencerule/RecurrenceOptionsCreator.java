package com.teambition.recurrencerule;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.teambition.recurrence.R;
import com.teambition.recurrencerule.adapter.RecurrenceOptionAdapter;
import com.teambition.recurrencerule.untils.DateUtil;
import com.teambition.recurrencerule.untils.RRuleContants;
import com.teambition.recurrencerule.untils.SUtils;
import com.teambition.recurrencerule.views.MonthButton;
import com.teambition.recurrencerule.views.RecurrenceOptionsLayout;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class RecurrenceOptionsCreator extends FrameLayout implements AdapterView.OnItemSelectedListener, View.OnClickListener, RecurrenceOptionAdapter.OnButtonCheckedChangeListener {

    private static final String TAG = "RecurrenceOptionsCreator";

    private Spinner mFreqSpinner;
//    private SwitchCompat mFreqSwitch;

    private Spinner mIntervalSpinner;

    private TextView mIntervalEndTv;
    private TextView mRecurrenceTipTv;


    private Button mPositiveButton, mNegativeButton;

    private RecurrenceOptionsLayout mWeekGroup, mMonthGroup, mYearGroup;


    private OnRecurrenceSetListener onRecurrenceSetListener;
    private Date startDate;

    private RecurrenceModel mModel = new RecurrenceModel();

    private RRule rRuleInstance;


    public RecurrenceOptionsCreator(Context context) {
        this(context, null);
    }

    public RecurrenceOptionsCreator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecurrenceOptionsCreator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeLayout();
    }

    @Override
    public void onButtonCheckedChange(CompoundButton view, int position, boolean isChecked) {

        switch (mModel.freq) {
            case RecurrenceModel.FREQ_WEEKLY: {
                mWeekGroup.onButtonCheckedChange(mModel, position, isChecked);
                displayRecurrenceTip();
            }
            break;
            case RecurrenceModel.FREQ_MONTHLY: {
                mMonthGroup.onButtonCheckedChange(mModel, position, isChecked);
                displayRecurrenceTip();
            }
            break;
            case RecurrenceModel.FREQ_YEARLY: {
                mYearGroup.onButtonCheckedChange(mModel, position, isChecked);
                displayRecurrenceTip();
            }
            break;
        }
    }

    public interface OnRecurrenceSetListener {
        void onRecurrenceSet(String rrule);

        void onCancelled();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void initializeLayout() {

        LayoutInflater.from(getContext()).inflate(R.layout.recurrence_picker, this);

        mFreqSpinner = (Spinner) findViewById(R.id.freqSpinner);
        mFreqSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> freqAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.recurrence_freq, R.layout.item_recurrence_freq_spinner);
        freqAdapter.setDropDownViewResource(R.layout.item_recurrence_spinner_dropdown);
        mFreqSpinner.setAdapter(freqAdapter);
        Drawable freqSpinnerBg = ContextCompat.getDrawable(getContext(), R.drawable.abc_spinner_mtrl_am_alpha);
        PorterDuffColorFilter cfFreqSpinner
                = new PorterDuffColorFilter(Color.rgb(0x75, 0x75, 0x75),
                PorterDuff.Mode.SRC_IN);
        if (freqSpinnerBg != null) {
            freqSpinnerBg.setColorFilter(cfFreqSpinner);
            mFreqSpinner.setBackground(freqSpinnerBg);
        }

//        mFreqSwitch = (SwitchCompat) findViewById(R.id.enableSwitch);
//        mFreqSwitch.setOnCheckedChangeListener(this);


        mIntervalSpinner = (Spinner) findViewById(R.id.intevalSpinner);
        mIntervalSpinner.setOnItemSelectedListener(this);

        String[] intervalStrs = new String[7];
        for (int i = 0, length = intervalStrs.length; i < length; i++) {
            intervalStrs[i] = String.valueOf(i + 1);
        }
        ArrayAdapter<CharSequence> intervalAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.item_recurrence_freq_spinner, intervalStrs);
        intervalAdapter.setDropDownViewResource(R.layout.item_recurrence_spinner_dropdown);
        mIntervalSpinner.setAdapter(intervalAdapter);

        mIntervalEndTv = (TextView) findViewById(R.id.tvIntervalEnd);

        mRecurrenceTipTv = (TextView) findViewById(R.id.tvRecurrenceTips);

        mWeekGroup = (RecurrenceOptionsLayout) findViewById(R.id.weekGroup);
        mMonthGroup = (RecurrenceOptionsLayout) findViewById(R.id.monthGroup);
        mYearGroup = (RecurrenceOptionsLayout) findViewById(R.id.yearGroup);

        mPositiveButton = (Button) findViewById(R.id.btnOk);
        mNegativeButton = (Button) findViewById(R.id.btnCancel);

        TypedArray a = getContext().obtainStyledAttributes(new int[]{R.attr.colorButtonNormal, R.attr.colorControlHighlight});
        int bgColor = a.getColor(0, Color.TRANSPARENT);
        int colorHighlight = a.getColor(1, Color.TRANSPARENT);
        a.recycle();
        SUtils.setViewBackground(mPositiveButton, SUtils.createButtonBg(getContext(), bgColor, colorHighlight));
        SUtils.setViewBackground(mNegativeButton, SUtils.createButtonBg(getContext(), bgColor, colorHighlight));
        mPositiveButton.setOnClickListener(this);
        mNegativeButton.setOnClickListener(this);


    }


    public void initializeData(RecurrenceModel model, Date startDate, @NonNull OnRecurrenceSetListener listener, RRule rRule) {
        onRecurrenceSetListener = listener;
        this.startDate = startDate;
        this.rRuleInstance = rRule;
        if (model != null) {
            mModel = model;
        }

        mWeekGroup.show(4, mModel, generateDayOfWeekString(), this);
        mMonthGroup.show(7, mModel, null, this);
        mYearGroup.show(4, mModel, generateMonthOfYearString(), this);

        if (mModel.freq >= mFreqSpinner.getAdapter().getCount()) {
            mModel.freq = RecurrenceModel.FREQ_WEEKLY;
        }
        mFreqSpinner.setSelection(mModel.freq);
        mIntervalSpinner.setSelection(mModel.interval - 1);

        updateDialog();
    }

    public String[] generateDayOfWeekString() {
        String[] dayOfWeekString = new DateFormatSymbols().getShortWeekdays();
        //because method getShortWeekdays() the first element is null
        List<String> list = new ArrayList<>(Arrays.asList(dayOfWeekString));
        list.remove(0);
        dayOfWeekString = list.toArray(dayOfWeekString);
        return dayOfWeekString;

    }

    public String[] generateMonthOfYearString() {
        String[] dayOfWeekString = new DateFormatSymbols().getShortMonths();
        return dayOfWeekString;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mFreqSpinner) {
            mModel.freq = position;
            updateDialog();
        } else if (parent == mIntervalSpinner) {
            mModel.interval = position + 1;
            displayRecurrenceTip();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnOk) {
            if (onRecurrenceSetListener != null) {
                onRecurrenceSetListener.onRecurrenceSet(generateRRule(mModel, startDate));
            }

        } else if (i == R.id.btnCancel) {
            if (onRecurrenceSetListener != null) {
                onRecurrenceSetListener.onCancelled();
            }

        }
    }

    private void updateDialog() {

        switch (mModel.freq) {
            case RecurrenceModel.FREQ_DAILY:
                mWeekGroup.setVisibility(View.GONE);
                mMonthGroup.setVisibility(View.GONE);
                mYearGroup.setVisibility(View.GONE);

                mIntervalEndTv.setText(R.string.recurrence_day);

                break;
            case RecurrenceModel.FREQ_WEEKLY: {
                mWeekGroup.setVisibility(View.VISIBLE);
                mMonthGroup.setVisibility(View.GONE);
                mYearGroup.setVisibility(View.GONE);

                int limitRepeatDay = 0;
                for (boolean isRepeat : mModel.weeklyByDayOfWeek) {
                    if (isRepeat) {
                        limitRepeatDay++;
                    }
                }
                if (limitRepeatDay == 0) {
                    Calendar calendar = new GregorianCalendar();
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    mModel.weeklyByDayOfWeek[dayOfWeek - 1] = true;
                }

                mIntervalEndTv.setText(R.string.recurrence_week);
            }
            break;
            case RecurrenceModel.FREQ_MONTHLY: {
                mWeekGroup.setVisibility(View.GONE);
                mMonthGroup.setVisibility(View.VISIBLE);
                mYearGroup.setVisibility(View.GONE);

                int limitRepeatDay = 0;
                for (boolean isRepeat : mModel.monthlyByDayOfMonth) {
                    if (isRepeat) {
                        limitRepeatDay++;
                    }
                }
                if (limitRepeatDay == 0) {
                    Calendar calendar = new GregorianCalendar();
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    mModel.monthlyByDayOfMonth[dayOfMonth - 1] = true;
                }


                mIntervalEndTv.setText(R.string.recurrence_month);
            }
            break;
            case RecurrenceModel.FREQ_YEARLY:
                mWeekGroup.setVisibility(View.GONE);
                mMonthGroup.setVisibility(View.GONE);
                mYearGroup.setVisibility(View.VISIBLE);

                int limitRepeatMonth = 0;
                for (boolean isRepeat : mModel.yearlyByMonthOfYear) {
                    if (isRepeat) {
                        limitRepeatMonth++;
                    }
                }
                if (limitRepeatMonth == 0) {
                    Calendar calendar = new GregorianCalendar();
                    int monthOfYear = calendar.get(Calendar.MONTH); // from 0 ----> 11
                    mModel.yearlyByMonthOfYear[monthOfYear] = true;
                }


                mIntervalEndTv.setText(R.string.recurrence_year);
                break;

        }

        displayRecurrenceTip();
    }

    private void bindMonthButtons(MonthButton[] monthButtons) {
        if (monthButtons == null || monthButtons.length < 31) {
            return;
        }

        for (int i = 0; i < 31; i++) {
            int resId = this.getResources().getIdentifier(String.format("month_day_%d", i + 1), "id", this.getContext().getPackageName());
            monthButtons[i] = (MonthButton) findViewById(resId);
        }
    }


    private void displayRecurrenceTip() {
        String strRecurrenceTip = makeRecurrenceTip(getContext(), mModel);
        mRecurrenceTipTv.setText(strRecurrenceTip);
    }

    public static String makeRecurrenceTip(Context context, RecurrenceModel mModel) {
        if (mModel == null) {
            return "";
        }
        String strRecurrenceTip = "";
        switch (mModel.freq) {
            case RecurrenceModel.FREQ_DAILY:
                if (mModel.interval == 1) {
                    strRecurrenceTip = context.getResources().getString(R.string.recurrence_interval_one_day_tip);
                } else {
                    strRecurrenceTip = context.getResources().getString(R.string.recurrence_interval_other_day_tip, mModel.interval);
                }
                break;
            case RecurrenceModel.FREQ_WEEKLY:
                if (mModel.interval == 1) {
                    strRecurrenceTip = context.getResources().getString(R.string.recurrence_interval_one_week_tip, makeWeekTipInfo(context, mModel));
                } else {
                    strRecurrenceTip = context.getResources().getString(R.string.recurrence_interval_other_week_tip, mModel.interval, makeWeekTipInfo(context, mModel));
                }
                break;
            case RecurrenceModel.FREQ_MONTHLY:
                if (mModel.interval == 1) {
                    strRecurrenceTip = context.getResources().getString(R.string.recurrence_interval_one_month_tip, makeMonthTipInfo(context, mModel));
                } else {
                    strRecurrenceTip = context.getResources().getString(R.string.recurrence_interval_other_month_tip, mModel.interval, makeMonthTipInfo(context, mModel));
                }
                break;
            case RecurrenceModel.FREQ_YEARLY:
                if (mModel.interval == 1) {
                    strRecurrenceTip = context.getResources().getString(R.string.recurrence_interval_one_year_tip, makeYearTipInfo(context, mModel));
                } else {
                    strRecurrenceTip = context.getResources().getString(R.string.recurrence_interval_other_year_tip, mModel.interval, makeYearTipInfo(context, mModel));
                }
                break;
        }
        return strRecurrenceTip;
    }

    private static String makeWeekTipInfo(Context context, RecurrenceModel mModel) {
        String str = "";
        String[] dayOfWeekString = new DateFormatSymbols().getWeekdays();
        int idx = DateUtil.getFirstDayOfWeek();
        for (int i = 0; i < mModel.weeklyByDayOfWeek.length; i++) {
            if (mModel.weeklyByDayOfWeek[i]) {
                if (TextUtils.isEmpty(str)) {
                    str = dayOfWeekString[RRuleContants.TIME_DAY_TO_CALENDAR_DAY[idx]];
                } else {
                    str = str + ", " + dayOfWeekString[RRuleContants.TIME_DAY_TO_CALENDAR_DAY[idx]];
                }
            }
            if (++idx >= 7) {
                idx = 0;
            }
        }

        str = str.replaceAll(", (?=[^, ]*$)", context.getResources().getString(R.string.recurrence_interval_and));

        return str;
    }

    private static String makeMonthTipInfo(Context context, RecurrenceModel mModel) {
        String str = "";
        for (int i = 0; i < mModel.monthlyByDayOfMonth.length; i++)
            if (mModel.monthlyByDayOfMonth[i]) {
                String dayStr = "";
                if (Locale.getDefault().equals(Locale.CHINA)) {
                    dayStr = (i + 1) + "日";
                } else if ("en".equals(Locale.getDefault().getLanguage())) {
                    dayStr = String.valueOf(i + 1) + getDayOfMonthSuffix(i + 1);
                }
                if (TextUtils.isEmpty(str)) {
                    str = dayStr;
                } else {
                    str = str + ", " + dayStr;
                }
            }

        str = str.replaceAll(", (?=[^, ]*$)", context.getResources().getString(R.string.recurrence_interval_and));

        return str;
    }

    private static String getDayOfMonthSuffix(final int n) {
        if (n < 1 || n > 31) {
            return "";
        }
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    private static String makeYearTipInfo(Context context, RecurrenceModel mModel) {
        String str = "";
        String[] monthOfYearString = new DateFormatSymbols().getMonths();
        for (int i = 0; i < mModel.yearlyByMonthOfYear.length; i++) {
            if (mModel.yearlyByMonthOfYear[i]) {
                if (TextUtils.isEmpty(str)) {
                    str = monthOfYearString[RRuleContants.TIME_MONTH_TO_CALENDAR_MONTH[i]];
                } else {
                    str = str + ", " + monthOfYearString[RRuleContants.TIME_MONTH_TO_CALENDAR_MONTH[i]];
                }
            }
        }

        str = str.replaceAll(", (?=[^, ]*$)", context.getResources().getString(R.string.recurrence_interval_and));

        return str;
    }

    private String generateRRule(RecurrenceModel mModel, Date startDate) {
        String rrule = "";
        switch (mModel.freq) {
            case RecurrenceModel.FREQ_DAILY: {
                rrule = rRuleInstance.generateRRule(mModel, startDate);
            }
            break;
            case RecurrenceModel.FREQ_WEEKLY: {
                ArrayList<Integer> selectedDays = new ArrayList<>();
                for (int i = 0; i < mModel.weeklyByDayOfWeek.length; i++) {
                    if (mModel.weeklyByDayOfWeek[i]) {
                        selectedDays.add(DateUtil.calendarDay2Day(i + 1));
                    }
                }
                mModel.byDay = new int[selectedDays.size()];
                for (int i = 0; i < selectedDays.size(); i++) {
                    mModel.byDay[i] = selectedDays.get(i);
                }
                mModel.byDayNum = new int[mModel.byDay.length];
                mModel.byDayCount = mModel.byDay.length;

                rrule = rRuleInstance.generateRRule(mModel, startDate);
            }
            break;
            case RecurrenceModel.FREQ_MONTHLY: {

                ArrayList<Integer> selectedDays = new ArrayList<>();
                for (int i = 0; i < mModel.monthlyByDayOfMonth.length; i++) {
                    if (mModel.monthlyByDayOfMonth[i]) {
                        selectedDays.add(i + 1);
                    }
                }
                mModel.byMonthDay = new int[selectedDays.size()];
                for (int i = 0; i < selectedDays.size(); i++) {
                    mModel.byMonthDay[i] = selectedDays.get(i);
                }
                mModel.byMonthDayCount = mModel.byMonthDay.length;

                rrule = rRuleInstance.generateRRule(mModel, startDate);
            }
            break;
            case RecurrenceModel.FREQ_YEARLY: {

                ArrayList<Integer> selectedMonths = new ArrayList<>();
                for (int i = 0; i < mModel.yearlyByMonthOfYear.length; i++) {
                    if (mModel.yearlyByMonthOfYear[i]) {
                        selectedMonths.add(i + 1);
                    }
                }
                mModel.byMonth = new int[selectedMonths.size()];
                for (int i = 0; i < selectedMonths.size(); i++) {
                    mModel.byMonth[i] = selectedMonths.get(i);
                }
                mModel.byMonthCount = mModel.byMonth.length;

                rrule = rRuleInstance.generateRRule(mModel, startDate);
            }
            break;
        }

        return rrule;
    }


}
