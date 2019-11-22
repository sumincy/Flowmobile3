package com.huateng.ebank.ui.widget.wheel;

import com.bigkoo.pickerview.configure.PickerOptions;

/**
 * Created by Sumincy on 2018/6/21.
 */
public class RangePickerOptions extends PickerOptions {

    public OnRangeTimeSelectListener rangeTimeSelectListener;

    public OnRangeTimeSelectChangeListener rangeTimeSelectChangeListener;

    public RangePickerOptions(int buildType) {
        super(buildType);
    }
}
