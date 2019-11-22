package com.huateng.ebank.ui.widget;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.huateng.ebank.R;
import com.huateng.ebank.utils.DateUtils;
import com.tools.utils.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by shanyong on 2019/4/4.
 */

public class TimePickerHelper {
    private TimePickerView pvTime;
    private Date lastSelDate;
    public static final int START = -1;
    public static final int END = 1;


    public TimePickerView create(Context context, final ObservableField<String> observableText, final ObservableField<Date> observableDate) {

        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1, 0, 0, 0);
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

        Calendar endDate = Calendar.getInstance();

        //时间选择器
        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String date1 = String.format("%s-%s-%s %s:%s:%s", calendar.get(Calendar.YEAR), StringUtils.repairDouble((calendar.get(Calendar.MONTH) + 1)),
                        StringUtils.repairDouble(calendar.get(Calendar.DAY_OF_MONTH)), StringUtils.repairDouble(calendar.get(Calendar.HOUR_OF_DAY)), StringUtils.repairDouble(calendar.get(Calendar.MINUTE)), StringUtils.repairDouble(calendar.get(Calendar.SECOND)));

                observableDate.set(date);
                observableText.set(date1);
            }
        }).setLayoutRes(R.layout.pickerview_time, new CustomListener() {

            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
                tvTitle.setText("请选择日期");
                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.returnData();
                        pvTime.dismiss();
                    }
                });
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.dismiss();
                    }
                });
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒") //设置空字符串以隐藏单位提示   hide label
                .setContentTextSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .build();

        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
        return pvTime;
    }

    //交易记录条件查询使用  可设置结束时间  并根据日期变化 调整结束时间
    public TimePickerView create(final int mode, Context context, final ObservableField<String> observableText, final ObservableField<Date> observableDate, @NonNull Calendar selDate) {

        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1, 0, 0, 0);

        Calendar endDate = Calendar.getInstance();

        lastSelDate = new Date();

        //时间选择器
        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String date1 = String.format("%s-%s-%s %s:%s:%s", calendar.get(Calendar.YEAR), StringUtils.repairDouble((calendar.get(Calendar.MONTH) + 1)),
                        StringUtils.repairDouble(calendar.get(Calendar.DAY_OF_MONTH)), StringUtils.repairDouble(calendar.get(Calendar.HOUR_OF_DAY)), StringUtils.repairDouble(calendar.get(Calendar.MINUTE)), StringUtils.repairDouble(calendar.get(Calendar.SECOND)));

                observableDate.set(date);
                observableText.set(date1);
            }
        }).setLayoutRes(R.layout.pickerview_time, new CustomListener() {

            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
                tvTitle.setText("请选择日期");
                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.returnData();
                        pvTime.dismiss();
                    }
                });
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.dismiss();
                    }
                });
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {

                Calendar calendar = Calendar.getInstance();
                if (DateUtils.getDaysBetween(date, calendar.getTime()) == 0 && DateUtils.getDaysBetween(lastSelDate, date) != 0) {
                    if (START == mode) {
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                    } else {
                        pvTime.setDate(calendar);
                    }
                } else if (DateUtils.getDaysBetween(lastSelDate, date) != 0) {
                    calendar.setTime(date);
                    if (START == mode) {
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                    } else {
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                    }
                    pvTime.setDate(calendar);
                }

                lastSelDate.setTime(date.getTime());
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒") //设置空字符串以隐藏单位提示   hide label
                .setContentTextSize(20)
                .setDate(selDate)
                .setRangDate(startDate, endDate)
                .build();

        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
        return pvTime;
    }

    public TimePickerView createSimple(Context context, final ObservableField<String> observableText, final ObservableField<Date> observableDate) {

        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar selectedDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        //时间选择器
        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String date1 = String.format("%s-%s-%s", calendar.get(Calendar.YEAR), StringUtils.repairDouble((calendar.get(Calendar.MONTH) + 1)),
                        StringUtils.repairDouble(calendar.get(Calendar.DAY_OF_MONTH)));

                observableDate.set(date);
                observableText.set(date1);
            }
        }).setLayoutRes(R.layout.pickerview_time, new CustomListener() {

            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
                tvTitle.setText("请选择日期");
                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.returnData();
                        pvTime.dismiss();
                    }
                });
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.dismiss();
                    }
                });
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setContentTextSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .build();

        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
        return pvTime;
    }

    public TimePickerView getPvTime() {
        return pvTime;
    }

}
