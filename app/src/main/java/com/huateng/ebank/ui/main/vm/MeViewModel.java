package com.huateng.ebank.ui.main.vm;

import android.app.Application;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.huateng.ebank.app.Paths;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Created by shanyong on 2019/9/24.
 */
public class MeViewModel extends BaseViewModel {
    public MeViewModel(@NonNull Application application) {
        super(application);
    }

    public BindingCommand billsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(Paths.PAGE_BILLS).navigation();
        }
    });

    public BindingCommand accountClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(Paths.PAGE_MY_ACCOUNT).navigation();
        }
    });

}
