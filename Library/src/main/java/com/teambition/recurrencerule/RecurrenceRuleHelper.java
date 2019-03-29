package com.teambition.recurrencerule;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import java.util.Date;

public class RecurrenceRuleHelper {


    private OnRecurrenceSetListener recurrenceCallback;
    private String[] rRules;
    private Date startDate = new Date();
    private final RRule rRuleInstance;
    private Context context;

    public RecurrenceRuleHelper(Context context, OnRecurrenceSetListener recurrenceCallback) {
        this.context = context;
        this.recurrenceCallback = recurrenceCallback;
        this.rRuleInstance = new TbRrule();
    }

    public RecurrenceRuleHelper(Context context, RRule rRule, OnRecurrenceSetListener recurrenceCallback) {
        this.context = context;
        this.recurrenceCallback = recurrenceCallback;
        this.rRuleInstance = rRule;
    }

    public void startSetRecurrence() {
        RecurrencePickerFragment recurrencePickerFragment = RecurrencePickerFragment.newInstance(startDate, rRules, rRuleInstance);
        recurrencePickerFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        if (context instanceof FragmentActivity) {
            recurrencePickerFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "RECURRENCE_PICKER");
        }
        recurrencePickerFragment.setRecurrenceCallback(recurrenceCallback);
    }


    public String getDisPlayInfo(String[] recurrencRules) {

        RecurrenceModel recurrenceModel = rRuleInstance.parseRRule(recurrencRules);
        String displayInfo = rRuleInstance.displayRRule(context, recurrenceModel);
        return displayInfo;
    }

    public String getDetailDisPlayInfo(String[] recurrencRules) {
        RecurrenceModel recurrenceModel = rRuleInstance.parseRRule(recurrencRules);
        String displayInfo = RecurrenceOptionsCreator.makeRecurrenceTip(context, recurrenceModel);
        return displayInfo;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String[] getrRules() {
        return rRules;
    }

    public void setrRules(String[] rRules) {
        this.rRules = rRules;
    }
}
