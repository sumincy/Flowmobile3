package com.huateng.ebank.ui.widget.wheel;

import android.view.View;

import com.bigkoo.pickerview.listener.ISelectTimeCallback;
import com.contrarywind.view.WheelView;
import com.huateng.ebank.R;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Lenovo on 2018/6/21.
 * 开始时间 结束时间
 */

public class wheelRangeTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private View view;

    private TimeController startTimeController;
    private TimeController endTimeController;

    private int gravity;

    private boolean[] type;
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;
    private int textSize;

    //文字的颜色和分割线的颜色
    private int textColorOut;
    private int textColorCenter;
    private int dividerColor;

    private float lineSpacingMultiplier;
    private WheelView.DividerType dividerType;

    public wheelRangeTime(View view, boolean[] type, int gravity, int textSize) {
        super();
        this.view = view;
        this.type = type;
        this.gravity = gravity;
        this.textSize = textSize;
        setView(view);
        initView();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getStartYear() {
        if (startTimeController != null) {
            return startTimeController.getStartYear();
        }
        return DEFAULT_START_YEAR;
    }

    public void setStartYear(int startYear) {
        if (startTimeController != null) {
            startTimeController.setStartYear(startYear);
        }
        if (endTimeController != null) {
            endTimeController.setStartYear(startYear);
        }
    }

    public int getEndYear() {
        if (startTimeController != null) {
            return startTimeController.getEndYear();
        }
        return DEFAULT_END_YEAR;
    }

    public void setEndYear(int endYear) {
        if (startTimeController != null) {
            startTimeController.setEndYear(endYear);
        }
        if (endTimeController != null) {
            endTimeController.setEndYear(endYear);
        }
    }

    //初始化控件
    private void initView() {
        WheelView wv_start_year = view.findViewById(R.id.start_year);
        WheelView wv_start_month = view.findViewById(R.id.start_month);
        WheelView wv_start_day = view.findViewById(R.id.start_day);
        WheelView wv_start_hours = view.findViewById(R.id.start_hour);
        WheelView wv_start_minutes = view.findViewById(R.id.start_min);
        WheelView wv_start_seconds = view.findViewById(R.id.start_second);
        //开始时间控制
        startTimeController = new TimeController(wv_start_year, wv_start_month, wv_start_day, wv_start_hours, wv_start_minutes, wv_start_seconds);

        WheelView wv_end_year = view.findViewById(R.id.end_year);
        WheelView wv_end_month = view.findViewById(R.id.end_month);
        WheelView wv_end_day = view.findViewById(R.id.end_day);
        WheelView wv_end_hours = view.findViewById(R.id.end_hour);
        WheelView wv_end_minutes = view.findViewById(R.id.end_min);
        WheelView wv_end_seconds = view.findViewById(R.id.end_second);
        //结束时间控制
        endTimeController = new TimeController(wv_end_year, wv_end_month, wv_end_day, wv_end_hours, wv_end_minutes, wv_end_seconds);

        startTimeController.setContentTextSize(textSize);
        endTimeController.setContentTextSize(textSize);

        startTimeController.setType(type);
        endTimeController.setType(type);

        startTimeController.setGravity(gravity);
        endTimeController.setGravity(gravity);

    }


    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0, 0);
    }

    public void setPicker(int year, final int month, int day, int h, int m, int s) {
        startTimeController.init(view, year, month, day, h, m, s);
        endTimeController.init(view, year, month, day, h, m, s);
    }


    private void setTextColorOut() {
        startTimeController.setTextColorOut(textColorOut);
        endTimeController.setTextColorCenter(textColorOut);
    }

    private void setTextColorCenter() {
        startTimeController.setTextColorCenter(textColorCenter);
        endTimeController.setTextColorCenter(textColorCenter);
    }

    private void setDividerColor() {
        startTimeController.setDividerColor(dividerColor);
        endTimeController.setDividerColor(dividerColor);
    }

    private void setDividerType() {
        startTimeController.setDividerType(dividerType);
        endTimeController.setDividerType(dividerType);
    }

    private void setLineSpacingMultiplier() {
        startTimeController.setLineSpacingMultiplier(lineSpacingMultiplier);
        endTimeController.setLineSpacingMultiplier(lineSpacingMultiplier);
    }

    public void setLabels(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
        startTimeController.setLabels(label_year, label_month, label_day
                , label_hours, label_mins, label_seconds);
        endTimeController.setLabels(label_year, label_month, label_day
                , label_hours, label_mins, label_seconds);
    }

    public void setTextXOffset(int x_offset_year, int x_offset_month, int x_offset_day,
                               int x_offset_hours, int x_offset_minutes, int x_offset_seconds) {

        startTimeController.setTextXOffset(x_offset_year, x_offset_month, x_offset_day,
                x_offset_hours, x_offset_minutes, x_offset_seconds);

        endTimeController.setTextXOffset(x_offset_year, x_offset_month, x_offset_day,
                x_offset_hours, x_offset_minutes, x_offset_seconds);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        startTimeController.setCyclic(cyclic);
        endTimeController.setCyclic(cyclic);
    }

    //TODO
    public SelectedDate getTime() {

        Calendar sCalendar = startTimeController.getTime();

        Calendar eCalendar = endTimeController.getTime();

        SelectedDate selectedDate = new SelectedDate(sCalendar, eCalendar);

        return selectedDate;
    }

    public void setRangDate(Calendar startDate, Calendar endDate) {
        startTimeController.setRangDate(startDate, endDate);
        endTimeController.setRangDate(startDate, endDate);
    }

    /**
     * 设置间距倍数,但是只能在1.0-4.0f之间
     *
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        setLineSpacingMultiplier();
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        setDividerColor();
    }

    /**
     * 设置分割线的类型
     *
     * @param dividerType
     */
    public void setDividerType(WheelView.DividerType dividerType) {
        this.dividerType = dividerType;
        setDividerType();
    }

    /**
     * 设置分割线之间的文字的颜色
     *
     * @param textColorCenter
     */
    public void setTextColorCenter(int textColorCenter) {
        this.textColorCenter = textColorCenter;
        setTextColorCenter();
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut
     */
    public void setTextColorOut(int textColorOut) {
        this.textColorOut = textColorOut;
        setTextColorOut();
    }

    /**
     * @param isCenterLabel 是否只显示中间选中项的
     */
    public void isCenterLabel(boolean isCenterLabel) {
        startTimeController.isCenterLabel(isCenterLabel);
        endTimeController.isCenterLabel(isCenterLabel);
    }

    public void setSelectChangeCallback(ISelectTimeCallback mSelectChangeCallback) {
        startTimeController.setSelectChangeCallback(mSelectChangeCallback);
    }
}
