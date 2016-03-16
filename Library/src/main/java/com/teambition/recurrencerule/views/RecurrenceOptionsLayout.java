package com.teambition.recurrencerule.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.teambition.recurrence.R;
import com.teambition.recurrencerule.RecurrenceModel;
import com.teambition.recurrencerule.adapter.RecurrenceOptionAdapter;


public class RecurrenceOptionsLayout extends FrameLayout {

    private RecyclerView recyclerView;

    public RecurrenceOptionsLayout(Context context) {
        this(context, null);
    }

    public RecurrenceOptionsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecurrenceOptionsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }


    public void initLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.options_recyclerview, this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    }

    public void show(int spanCount, RecurrenceModel model, String[] optionStrs, RecurrenceOptionAdapter.OnButtonCheckedChangeListener onButtonCheckedChangeListener) {

        RecurrenceOptionAdapter adapter = new RecurrenceOptionAdapter(getContext(), optionStrs, model, onButtonCheckedChangeListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
//        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
    }


    public void onButtonCheckedChange(RecurrenceModel model, int position, boolean isChecked) {
        int repeatCounts = 0;
        switch (model.freq) {
            case RecurrenceModel.FREQ_WEEKLY:
                model.weeklyByDayOfWeek[position] = isChecked;

                for (boolean isRepeat : model.weeklyByDayOfWeek) {
                    if (isRepeat) {
                        repeatCounts++;
                    }
                }
                if (repeatCounts == 0) {
                    RecurrenceOptionAdapter.WeeklyItem weeklyItem = (RecurrenceOptionAdapter.WeeklyItem) recyclerView.findViewHolderForAdapterPosition(position);
                    weeklyItem.weekButton.setCheckedNoAnimate(true);
                    model.weeklyByDayOfWeek[position] = true;
                }
                break;
            case RecurrenceModel.FREQ_MONTHLY:
                model.monthlyByDayOfMonth[position] = isChecked;
                for (boolean isRepeat : model.monthlyByDayOfMonth) {
                    if (isRepeat) {
                        repeatCounts++;
                    }
                }
                if (repeatCounts == 0) {
                    RecurrenceOptionAdapter.MonthlyItem monthlyItem = (RecurrenceOptionAdapter.MonthlyItem) recyclerView.findViewHolderForAdapterPosition(position);
                    monthlyItem.monthButton.setCheckedNoAnimate(true);
                    model.monthlyByDayOfMonth[position] = true;
                }
                break;
            case RecurrenceModel.FREQ_YEARLY:
                model.yearlyByMonthOfYear[position] = isChecked;
                for (boolean isRepeat : model.yearlyByMonthOfYear) {
                    if (isRepeat) {
                        repeatCounts++;
                    }
                }
                if (repeatCounts == 0) {
                    RecurrenceOptionAdapter.WeeklyItem weeklyItem = (RecurrenceOptionAdapter.WeeklyItem) recyclerView.findViewHolderForAdapterPosition(position);
                    weeklyItem.weekButton.setCheckedNoAnimate(true);
                    model.yearlyByMonthOfYear[position] = true;
                }
                break;
        }
    }



}
