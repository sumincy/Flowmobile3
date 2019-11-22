package com.huateng.ebank.ui.main.vm;

import android.app.Application;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * Created by shanyong on 2019/9/24.
 */
public class HomeViewModel extends BaseViewModel {

    private CountDownTimer timer;
    private int position = 0;
    private List<String> platformMsges = new ArrayList<>();
    public SingleLiveEvent<String> platformMsgChangeEvent = new SingleLiveEvent<>();
    public List recommandProducts = new ArrayList();

    public HomeViewModel(@NonNull Application application) {
        super(application);

        //滚动消息
        platformMsges.add("text1");
        platformMsges.add("text2");
        platformMsges.add("text3");

        timer = new CountDownTimer(Integer.MAX_VALUE, 2500) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (platformMsges.size() > 0) {
                    if (position >= platformMsges.size()) {
                        position = 0;
                    }
                    String content = platformMsges.get(position);
                    platformMsgChangeEvent.setValue(content);
                    position++;
                }
            }

            @Override
            public void onFinish() {

            }
        };

        //理财推荐
        recommandProducts.add(new Object());
        recommandProducts.add(new Object());
        recommandProducts.add(new Object());

    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != timer) {
            timer.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != timer) {
            timer.cancel();
        }
    }
}
