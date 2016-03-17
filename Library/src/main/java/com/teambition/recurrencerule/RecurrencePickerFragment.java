package com.teambition.recurrencerule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teambition.recurrence.R;

import java.util.Date;

public class RecurrencePickerFragment extends DialogFragment implements OnRecurrenceSetListener {


    private RecurrencePicker recurrencePickerView;

    private RRule rRuleInstance;

    public static RecurrencePickerFragment newInstance(Date startDate, String rrule, RRule rRuleInstance) {
        Bundle bundle = new Bundle();
        bundle.putString("RecurrenceRule", rrule);
        bundle.putSerializable("StartDate", startDate);
        bundle.putSerializable("RRule", rRuleInstance);

        RecurrencePickerFragment instance = new RecurrencePickerFragment();
        instance.setArguments(bundle);
        return instance;
    }

    private String rrule;
    private Date startDate;

    private OnRecurrenceSetListener callback;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rrule = getArguments().getString("RecurrenceRule");
            startDate = (Date) getArguments().getSerializable("StartDate");
            rRuleInstance = (RRule) getArguments().getSerializable("RRule");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        recurrencePickerView = (RecurrencePicker) inflater.inflate(R.layout.fragment_recurrence_picker, container);
        RecurrenceModel model = rRuleInstance.parseRRule(rrule);
        recurrencePickerView.initializeData(this, model, startDate, rRuleInstance);
        recurrencePickerView.updateView();
        return recurrencePickerView;
    }


    public void setRecurrenceCallback(OnRecurrenceSetListener callback) {
        this.callback = callback;
    }

    @Override
    public void onRecurrenceSet(String recurrenceRule, String parsedStr) {
        dismiss();
        if (callback != null) {
            if (TextUtils.isEmpty(parsedStr)) {
                RecurrenceModel model = rRuleInstance.parseRRule(recurrenceRule);
                parsedStr = rRuleInstance.displayRRule(this.getActivity(), model);
            }
            callback.onRecurrenceSet(recurrenceRule, parsedStr);
        }
    }
}
