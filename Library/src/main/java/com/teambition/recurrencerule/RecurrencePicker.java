package com.teambition.recurrencerule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.teambition.recurrence.R;
import com.teambition.recurrencerule.untils.DateUtil;
import com.teambition.recurrencerule.untils.RRuleContants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RecurrencePicker extends FrameLayout implements View.OnClickListener, RecurrenceOptionsCreator.OnRecurrenceSetListener {


    private enum CurrentView {
        RECURRENCE_OPTIONS_MENU, RECURRENCE_CREATOR
    }

    RecurrenceModel.RecurrenceOption mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.DOES_NOT_REPEAT;
    CurrentView mCurrentView = CurrentView.RECURRENCE_OPTIONS_MENU;

    int mSelectedStateTextColor = Color.rgb(0x01, 0xA9, 0xF4), mUnselectedStateTextColor = Color.rgb(0x38, 0x38, 0x38);

    Drawable mCheckmarDrawable;


    private View recurrenceOptionsMenu;
    private RecurrenceOptionsCreator recurrenceOptionsCreator;

    OnRecurrenceSetListener mCallback;

    private Date startDate;

    private ArrayList<TextView> mRepeatOptionTextViews;
    private TextView tvCustomTip;
    private RRule rRuleInstance;
    private RecurrenceModel model;


    public RecurrencePicker(Context context) {
        this(context, null);
    }

    public RecurrencePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecurrencePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeLayout();
    }

    void initializeLayout() {
        Context context = getContext();
        LayoutInflater.from(context).inflate(R.layout.recurrence_sublime_picker, this);
        recurrenceOptionsMenu = findViewById(R.id.recurrenceOptionsMenu);
        recurrenceOptionsCreator = (RecurrenceOptionsCreator) findViewById(R.id.recurrenceOptionCreator);

        mCheckmarDrawable = ContextCompat.getDrawable(context, R.drawable.ic_checkmark);
        mCheckmarDrawable.setColorFilter(mSelectedStateTextColor, PorterDuff.Mode.MULTIPLY);


        // Options/Views
        mRepeatOptionTextViews = new ArrayList<>();
        mRepeatOptionTextViews.add(
            (TextView) findViewById(R.id.tvDoesNotRepeat));
        mRepeatOptionTextViews.add(
            (TextView) findViewById(R.id.tvDaily));
        mRepeatOptionTextViews.add(
            (TextView) findViewById(R.id.tvWeekly));
        mRepeatOptionTextViews.add(
            (TextView) findViewById(R.id.tvMonthly));
        mRepeatOptionTextViews.add(
            (TextView) findViewById(R.id.tvYearly));
        mRepeatOptionTextViews.add(
            (TextView) findViewById(R.id.tvWeekday));
        mRepeatOptionTextViews.add(
            (TextView) findViewById(R.id.tvCustom));

        tvCustomTip = (TextView) findViewById(R.id.tvCustomTip);
    }

    public void initializeData(OnRecurrenceSetListener callback, RecurrenceModel model, Date startDate, RRule rRule) {

        mCallback = callback;
        this.startDate = startDate;
        if (model != null) {
            this.model = model;
        } else {
            this.model = new RecurrenceModel();
        }

        this.rRuleInstance = rRule;
        String displayInfo = rRule.displayRRule(getContext(), model);

        final String noRepeatStr = getContext().getString(R.string.recurrence_no_repeat);
        final String everyDayStr = getContext().getString(R.string.recurrence_every_day);
        final String everyWeekStr = getContext().getString(R.string.recurrence_every_week);
        final String everyMonthStr = getContext().getString(R.string.recurrence_every_month);
        final String everyYearStr = getContext().getString(R.string.recurrence_every_year);
        final String everyWeekdayStr = getContext().getString(R.string.recurrence_every_weekday);
        final String custom = getContext().getString(R.string.recurrence_custom);

        if (noRepeatStr.equals(displayInfo)) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.DOES_NOT_REPEAT;
        } else if (everyDayStr.equals(displayInfo)) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.DAILY;
        } else if (everyWeekStr.equals(displayInfo)) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.WEEKLY;
        } else if (everyMonthStr.equals(displayInfo)) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.MONTHLY;
        } else if (everyYearStr.equals(displayInfo)) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.YEARLY;
        } else if (everyWeekdayStr.equals(displayInfo)) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.WEEKDAY;
        } else if (custom.equals(displayInfo)) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.CUSTOM;
        }

        if (mCurrentRecurrenceOption == RecurrenceModel.RecurrenceOption.CUSTOM) {
            tvCustomTip.setVisibility(View.VISIBLE);
            tvCustomTip.setText(RecurrenceOptionsCreator.makeRecurrenceTip(getContext(), model));
        } else {
            tvCustomTip.setVisibility(View.GONE);
        }

        recurrenceOptionsCreator.initializeData(model, startDate, this, rRule);

    }

    public void updateView() {
        if (mCurrentView == CurrentView.RECURRENCE_OPTIONS_MENU) {
            recurrenceOptionsCreator.setVisibility(View.GONE);
            recurrenceOptionsMenu.setVisibility(View.VISIBLE);

            updateFlowLayout(mCurrentRecurrenceOption);

        } else if (mCurrentView == CurrentView.RECURRENCE_CREATOR) {
            recurrenceOptionsMenu.setVisibility(View.GONE);
            recurrenceOptionsCreator.setVisibility(View.VISIBLE);
        }
    }

    void updateFlowLayout(RecurrenceModel.RecurrenceOption recurrenceOption) {
        int viewIdToSelect;
        switch (recurrenceOption) {
            case DOES_NOT_REPEAT:
                viewIdToSelect = R.id.tvDoesNotRepeat;
                break;
            case DAILY:
                viewIdToSelect = R.id.tvDaily;
                break;
            case WEEKLY:
                viewIdToSelect = R.id.tvWeekly;
                break;
            case MONTHLY:
                viewIdToSelect = R.id.tvMonthly;
                break;
            case YEARLY:
                viewIdToSelect = R.id.tvYearly;
                break;
            case WEEKDAY:
                viewIdToSelect = R.id.tvWeekday;
                break;
            case CUSTOM:
                viewIdToSelect = R.id.tvCustom;
                break;
            default:
                viewIdToSelect = R.id.tvDoesNotRepeat;
        }

        for (TextView tv : mRepeatOptionTextViews) {
            tv.setOnClickListener(this);

            // Selected option
            if (tv.getId() == viewIdToSelect) {
                tv.setCompoundDrawablesWithIntrinsicBounds(null, null, mCheckmarDrawable, null);
                tv.setTextColor(mSelectedStateTextColor);
                continue;
            }
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            tv.setTextColor(mUnselectedStateTextColor);
        }
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.tvDoesNotRepeat) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.DOES_NOT_REPEAT;
        } else if (viewId == R.id.tvDaily) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.DAILY;
        } else if (viewId == R.id.tvWeekly) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.WEEKLY;
        } else if (viewId == R.id.tvMonthly) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.MONTHLY;
        } else if (viewId == R.id.tvYearly) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.YEARLY;
        } else if (viewId == R.id.tvWeekday) {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.WEEKDAY;
        } else if (viewId == R.id.tvCustom) {
            mCurrentView = CurrentView.RECURRENCE_CREATOR;
            updateView();
            return;
        } else {
            mCurrentRecurrenceOption = RecurrenceModel.RecurrenceOption.DOES_NOT_REPEAT;
        }

        if (mCallback != null) {
            String[] ruleArray = null;
            String ruleStr = generateRRule(mCurrentRecurrenceOption);
            if (!TextUtils.isEmpty(ruleStr)) {
                ruleArray = new String[]{ruleStr};
            }
            mCallback.onRecurrenceSet(ruleArray, "");
        }

    }

    @Override
    public void onRecurrenceSet(String rrule) {
        if (mCallback != null) {
            String[] ruleArray = null;
            if (!TextUtils.isEmpty(rrule)) {
                ruleArray = new String[]{rrule};
            }
            mCallback.onRecurrenceSet(ruleArray, "");
        }
    }

    @Override
    public void onCancelled() {
        mCurrentView = CurrentView.RECURRENCE_OPTIONS_MENU;
        updateView();
    }


    private String generateRRule(RecurrenceModel.RecurrenceOption option) {
        String rrule = null;
        switch (option) {
            case CUSTOM:
            case DOES_NOT_REPEAT:
                break;
            case DAILY: {
                model.freq = RecurrenceModel.FREQ_DAILY;
                model.interval = 1;
                rrule = rRuleInstance.generateRRule(model, startDate);
            }
            break;
            case WEEKLY: {
                model.freq = RecurrenceModel.FREQ_WEEKLY;
                model.interval = 1;
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(startDate);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                model.byDay = new int[]{DateUtil.calendarDay2Day(dayOfWeek)};
                model.byDayNum = new int[model.byDay.length];
                model.byDayCount = model.byDay.length;

                rrule = rRuleInstance.generateRRule(model, startDate);
            }
            break;
            case MONTHLY: {
                model.freq = RecurrenceModel.FREQ_MONTHLY;
                model.interval = 1;
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(startDate);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                model.byMonthDay = new int[]{dayOfMonth};
                model.byMonthDayCount = model.byMonthDay.length;

                rrule = rRuleInstance.generateRRule(model, startDate);
            }
            break;
            case YEARLY: {
                model.freq = RecurrenceModel.FREQ_YEARLY;
                model.interval = 1;
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(startDate);
                int monthOfYear = calendar.get(Calendar.MONTH);
                model.byMonth = new int[]{monthOfYear + 1};
                model.byMonthCount = model.byMonth.length;

                rrule = rRuleInstance.generateRRule(model, startDate);
            }
            break;
            case WEEKDAY: {
                model.freq = RecurrenceModel.FREQ_WEEKLY;
                model.interval = 1;
                model.byDay = new int[]{RRuleContants.MO, RRuleContants.TU, RRuleContants.WE, RRuleContants.TH, RRuleContants.FR};
                model.byDayNum = new int[model.byDay.length];
                model.byDayCount = model.byDay.length;

                rrule = rRuleInstance.generateRRule(model, startDate);
            }
            break;
        }
        return rrule;
    }

}
