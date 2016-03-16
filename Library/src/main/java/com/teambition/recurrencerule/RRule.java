package com.teambition.recurrencerule;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;

public abstract class RRule implements Serializable {


    abstract RecurrenceModel parseRRule(String rRules);

    abstract String displayRRule(Context context, RecurrenceModel model);

    abstract String generateRRule(RecurrenceModel model, Date startDate);

}
