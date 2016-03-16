package com.teambition.recurrencerule.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.teambition.recurrence.R;
import com.teambition.recurrencerule.RecurrenceModel;
import com.teambition.recurrencerule.untils.DensityUtil;
import com.teambition.recurrencerule.untils.RRuleContants;
import com.teambition.recurrencerule.views.CheckableDrawable;
import com.teambition.recurrencerule.views.MonthButton;
import com.teambition.recurrencerule.views.WeekButton;

public class RecurrenceOptionAdapter extends RecyclerView.Adapter {

    private static final int DAILYCOUNT = 0;
    private static final int WEEKLYCOUNT = 7;
    private static final int MONTHLYCOUNT = 31;
    private static final int YEARLYCOUNT = 12;


    private final int TYPE_DAILY = 1;
    private final int TYPE_WEEKLY = 2;
    private final int TYPE_MONTHLY = 3;
    private final int TYPE_YEARLY = 4;


    public interface OnButtonCheckedChangeListener {

        void onButtonCheckedChange(CompoundButton view, int position, boolean isChecked);
    }


    private Context context;
    private String[] optionsString;

    private OnButtonCheckedChangeListener onButtonCheckedChangeListener;
    private static final int buttonSelectedCircleColor = Color.rgb(0x03, 0xA9, 0xF4);
    //    private static final int buttonUnselectedTextColor = Color.rgb(0xA2, 0xA2, 0xA2);
    private static final int buttonUnselectedTextColor = Color.rgb(0x38, 0x38, 0x38);
    private static final int buttonSelectedTextColor = Color.WHITE;

    private RecurrenceModel model;


    public RecurrenceOptionAdapter(Context context, String[] optionsString, RecurrenceModel model, OnButtonCheckedChangeListener onButtonCheckedChangeListener) {
        this.context = context;
        this.model = model;
        this.optionsString = optionsString;
        this.onButtonCheckedChangeListener = onButtonCheckedChangeListener;

//        int expandMonthWidthHeight = DensityUtil.dip2px(getContext(), 34);
    }

    public void update(RecurrenceModel model) {
        this.model = model;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        int type;
        switch (model.freq) {
            case RecurrenceModel.FREQ_DAILY:
                type = TYPE_DAILY;
                break;
            case RecurrenceModel.FREQ_WEEKLY:
                type = TYPE_WEEKLY;
                break;
            case RecurrenceModel.FREQ_MONTHLY:
                type = TYPE_MONTHLY;
                break;
            case RecurrenceModel.FREQ_YEARLY:
                type = TYPE_YEARLY;
                break;
            default:
                type = TYPE_WEEKLY;
        }
        return type;
    }

    @Override
    public int getItemCount() {
        int count;
        switch (model.freq) {
            case RecurrenceModel.FREQ_DAILY:
                count = DAILYCOUNT;
                break;
            case RecurrenceModel.FREQ_WEEKLY:
                count = WEEKLYCOUNT;
                break;
            case RecurrenceModel.FREQ_MONTHLY:
                count = MONTHLYCOUNT;
                break;
            case RecurrenceModel.FREQ_YEARLY:
                count = YEARLYCOUNT;
                break;
            default:
                count = WEEKLYCOUNT;
        }

        return count;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_DAILY) {
            return null;
        } else if (viewType == TYPE_WEEKLY) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_weekbutton, parent, false);
            return new WeeklyItem(view, new WeeklyItem.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, int position, boolean isChecked) {
                    if (onButtonCheckedChangeListener != null) {
                        onButtonCheckedChangeListener.onButtonCheckedChange(view, position, isChecked);
                    }
                }
            });
        } else if (viewType == TYPE_MONTHLY) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_monthbutton, parent, false);
            return new MonthlyItem(view, new MonthlyItem.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, int position, boolean isChecked) {
                    if (onButtonCheckedChangeListener != null) {
                        onButtonCheckedChangeListener.onButtonCheckedChange(view, position, isChecked);
                    }
                }
            });
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_weekbutton, parent, false);
            return new WeeklyItem(view, new WeeklyItem.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, int position, boolean isChecked) {
                    if (onButtonCheckedChangeListener != null) {
                        onButtonCheckedChangeListener.onButtonCheckedChange(view, position, isChecked);
                    }
                }
            });
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);

        if (type == TYPE_WEEKLY) {
            WeeklyItem viewHolderItem = (WeeklyItem) holder;
            viewHolderItem.weekButton.setTextOff(optionsString[RRuleContants.TIME_DAY_TO_CALENDAR_DAY[position] - 1]);
            viewHolderItem.weekButton.setTextOn(optionsString[RRuleContants.TIME_DAY_TO_CALENDAR_DAY[position] - 1]);
            viewHolderItem.weekButton.setCheckedNoAnimate(model.weeklyByDayOfWeek[position]);
        } else if (type == TYPE_MONTHLY) {
            MonthlyItem viewHolderItem = (MonthlyItem) holder;
            viewHolderItem.monthButton.setTextOff(String.valueOf(position + 1));
            viewHolderItem.monthButton.setTextOn(String.valueOf(position + 1));
            viewHolderItem.monthButton.setCheckedNoAnimate(model.monthlyByDayOfMonth[position]);
        } else if (type == TYPE_YEARLY) {
            WeeklyItem viewHolderItem = (WeeklyItem) holder;
            viewHolderItem.weekButton.setTextOff(optionsString[RRuleContants.TIME_MONTH_TO_CALENDAR_MONTH[position]]);
            viewHolderItem.weekButton.setTextOn(optionsString[RRuleContants.TIME_MONTH_TO_CALENDAR_MONTH[position]]);
            viewHolderItem.weekButton.setCheckedNoAnimate(model.yearlyByMonthOfYear[position]);
        }

    }


    public static class WeeklyItem extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        interface OnCheckedChangeListener {

            void onCheckedChanged(CompoundButton buttonView, int postition, boolean isChecked);
        }

        public WeekButton weekButton;
        private OnCheckedChangeListener listener;
        public int expandedWithHeight;
        private CheckableDrawable checkableDrawable;

        public WeeklyItem(View itemView, OnCheckedChangeListener listener) {
            super(itemView);
            weekButton = (WeekButton) itemView.findViewById(R.id.weekButton);
            expandedWithHeight = DensityUtil.dip2px(itemView.getContext(), 44);
            checkableDrawable = new CheckableDrawable(buttonSelectedCircleColor, false, expandedWithHeight);
            weekButton.setStateColors(buttonUnselectedTextColor, buttonSelectedTextColor);
            weekButton.setBackgroundDrawable(checkableDrawable);
            weekButton.setTextColor(buttonUnselectedTextColor);
            weekButton.setOnCheckedChangeListener(this);
            this.listener = listener;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (listener != null) {
                listener.onCheckedChanged(buttonView, getAdapterPosition(), isChecked);
            }
        }
    }


    public static class MonthlyItem extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        interface OnCheckedChangeListener {

            void onCheckedChanged(CompoundButton buttonView, int postition, boolean isChecked);
        }

        public MonthButton monthButton;
        private OnCheckedChangeListener listener;
        public int expandedWithHeight;
        private CheckableDrawable checkableDrawable;

        public MonthlyItem(View itemView, OnCheckedChangeListener listener) {
            super(itemView);
            monthButton = (MonthButton) itemView.findViewById(R.id.monthButton);
            expandedWithHeight = DensityUtil.dip2px(itemView.getContext(), 34);
            checkableDrawable = new CheckableDrawable(buttonSelectedCircleColor, false, expandedWithHeight, true);
            monthButton.setStateColors(buttonUnselectedTextColor, buttonSelectedTextColor);
            monthButton.setBackgroundDrawable(checkableDrawable);
            monthButton.setTextColor(buttonUnselectedTextColor);
            monthButton.setOnCheckedChangeListener(this);
            this.listener = listener;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (listener != null) {
                listener.onCheckedChanged(buttonView, getAdapterPosition(), isChecked);
            }
        }
    }


}
