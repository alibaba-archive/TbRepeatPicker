package com.teambition.recurrencerule;

import android.content.Context;

import java.util.Date;

public interface RRule {


    RecurrenceModel parseRRule(String[] rRules);

    String displayRRule(Context context, RecurrenceModel model);

    String generateRRule(RecurrenceModel model, Date startDate);

}
