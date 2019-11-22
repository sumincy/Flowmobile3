package com.huateng.ebank.ui.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.google.gson.reflect.TypeToken;
import com.huateng.ebank.R;
import com.huateng.ebank.app.Constants;
import com.huateng.ebank.app.MainApplication;
import com.huateng.ebank.app.Perference;
import com.huateng.ebank.db.DbUtil;
import com.huateng.ebank.db.DicHelper;
import com.huateng.ebank.entity.orm.Dic;
import com.huateng.ebank.receiver.HomeWatcherReceiver;
import com.huateng.ebank.service.TokenLoginService;
import com.huateng.ebank.ui.widget.SplashView;
import com.huateng.ebank.utils.AssetsUtil;
import com.huateng.ebank.utils.GsonUtils;
import com.orhanobut.logger.Logger;
import com.tools.utils.RxActivityUtils;
import com.tools.utils.StringUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.utils.RxUtils;


/**
 * <p>开屏窗口</p>
 * Edited by Alian Lee on 2016-01-13.
 */
public class SplashActivity extends AppCompatActivity implements HomeWatcherReceiver.OnHomeKeyPressd {

    private static final String TAG = "splash";
    private static final int SPLASH_DISMISS = 100;

    private HomeWatcherReceiver mHomeKeyReceiver;
    private SplashView mSplashView;

    // 为true表示上次登录成功,本次采用token登录
    private boolean isTokenLoginMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 创建开屏容器
        setContentView(R.layout.activity_splash);

        //设置开屏
        mSplashView = setupSplashAd();

        //子线程初始化 SP
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Perference.init(getApplicationContext());
//                Config.init(getApplicationContext());
                emitter.onNext("onNext");
            }
        }).compose(RxUtils.schedulersTransformer()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String o) throws Exception {

                //启动activity时置为false
                Perference.setBoolean(Constants.TOKEN_AUTH, false);

                //token 登录
                if (StringUtils.isNotEmpty(Perference.get(Perference.TOKEN))) {
                    isTokenLoginMode = true;
                    //token 存在则启动intentservice 进行token 登录
                    TokenLoginService.start(MainApplication.getApplication());
                } else {
                    isTokenLoginMode = false;
                }

                if (null != mSplashView) {
                    mSplashView.showSkipButton();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });

    }

    /**
     * 设置开屏广告
     */
    private SplashView setupSplashAd() {

        SplashView splashView = new SplashView(this);
        splashView.showSplashViewInfinite(this, 3, R.mipmap.ic_splash, new SplashView.OnSplashViewActionListener() {
            @Override
            public void onDoSomething() {
                initDics();
            }

            @Override
            public void onSplashImageClick(String actionUrl) {

            }

            @Override
            public void onSplashViewDismiss(boolean initiativeDismiss) {
                startMainActivity();
            }
        });

        return splashView;
    }

    private void initDics() {
        String dicsJsonData = AssetsUtil.readAssets(this, "dics.json");
        List<Dic> dics = GsonUtils.fromJson(dicsJsonData, new TypeToken<List<Dic>>() {
        }.getType());
        DicHelper dicHelper = DbUtil.getDicHelper();
        dicHelper.deleteAll();
        dicHelper.save(dics);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerHomeKeyReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterHomeKeyReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSplashView != null) {
            mSplashView.cancelSplash();
            mSplashView = null;
        }
    }

    private void startMainActivity() {

        Logger.i("存在TOKEN:%s", String.valueOf(isTokenLoginMode));

        //token 存在去主页  不存在去登录界面
        if (isTokenLoginMode) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            Intent intent = new Intent(this, ActivityMain.class);
            //
            intent.putExtra(Constants.LOGIN_TYPE, Constants.TOKEN_LOGIN);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            //取消全屏
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        finish();
    }


    private void registerHomeKeyReceiver() {
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mHomeKeyReceiver.setOnHomeKeyPressd(this);

        this.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    private void unregisterHomeKeyReceiver() {
        if (null != mHomeKeyReceiver) {
            this.unregisterReceiver(mHomeKeyReceiver);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exit();
    }

    @Override
    public void onHomeKeyPressd() {
        exit();
    }


    //在splash 界面按home键或back键的处理
    public void exit() {
        if (mSplashView != null) {
            mSplashView.cancelSplash();
            mSplashView = null;
            RxActivityUtils.AppExit(this);
        }
    }
}
