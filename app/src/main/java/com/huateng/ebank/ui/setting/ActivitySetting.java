package com.huateng.ebank.ui.setting;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.huateng.ebank.BR;
import com.huateng.ebank.R;
import com.huateng.ebank.app.Paths;
import com.huateng.ebank.databinding.ActivitySettingBinding;
import com.huateng.ebank.ui.base.BaseSupportActivity;
import com.huateng.ebank.ui.setting.vm.SettingViewModel;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.tools.utils.AppUtils;

import cn.iwgang.simplifyspan.SimplifySpanBuild;
import cn.iwgang.simplifyspan.customspan.CustomClickableSpan;
import cn.iwgang.simplifyspan.other.OnClickableSpanListener;
import cn.iwgang.simplifyspan.other.SpecialGravity;
import cn.iwgang.simplifyspan.unit.SpecialClickableUnit;
import cn.iwgang.simplifyspan.unit.SpecialTextUnit;

/**
 * Created by shanyong on 2019/11/11.
 */
@Route(path = Paths.PAGE_SETTING)
public class ActivitySetting extends BaseSupportActivity<ActivitySettingBinding, SettingViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_setting;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        immersiveStatusBar(binding.rxTitle);
        binding.rxTitle.setLeftFinish(this);

        //版本号
        SimplifySpanBuild countSpan = new SimplifySpanBuild();
        countSpan.append(new SpecialTextUnit("版本 " + AppUtils.getAppVersionName(), 0xFF999999).showUnderline().setGravity(binding.tvVersion, SpecialGravity.CENTER)
                .setClickableUnit(new SpecialClickableUnit(binding.tvVersion, new OnClickableSpanListener() {
                    @Override
                    public void onClick(TextView tv, CustomClickableSpan clickableSpan) {
                        ARouter.getInstance().build(Paths.PAGE_ABOUT_US).navigation();
                    }
                }).showUnderline()));

        binding.tvVersion.setText(countSpan.build());
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.logoutEvent.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void aVoid) {


                MessageDialog.show(ActivitySetting.this, "提示", "确定退出登录？").setOkButton("确定", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        viewModel.requestLogout();
                        return false;
                    }
                }).setCancelButton("取消").setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        return false;
                    }
                }).setCancelable(false);
            }
        });
    }
}
