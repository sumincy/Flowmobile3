package com.huateng.ebank.network.utils;

import android.content.Context;
import android.text.TextUtils;

import com.huateng.ebank.BuildConfig;
import com.huateng.ebank.app.Perference;
import com.huateng.ebank.entity.api.RespBase;
import com.huateng.ebank.network.ApiConstants;
import com.huateng.ebank.network.CommonApiService;
import com.huateng.ebank.network.HttpMethod;
import com.huateng.ebank.network.NetworkConfig;
import com.huateng.ebank.network.RequestCallback;
import com.huateng.ebank.utils.CommonUtils;
import com.huateng.ebank.utils.GsonUtils;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.http.cookie.CookieJarImpl;
import me.goldze.mvvmhabit.http.cookie.store.PersistentCookieStore;
import me.goldze.mvvmhabit.http.interceptor.BaseInterceptor;
import me.goldze.mvvmhabit.http.interceptor.CacheInterceptor;
import me.goldze.mvvmhabit.http.interceptor.logging.Level;
import me.goldze.mvvmhabit.http.interceptor.logging.LoggingInterceptor;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.Utils;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by goldze on 2017/5/10.
 * RetrofitClient封装单例类, 实现网络请求
 */
public class RetrofitClient {
    //超时时间
    private static final int DEFAULT_TIMEOUT = 60;
    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
//    //服务端根路径
//    public static String baseUrl = "";

    private static Context mContext = Utils.getContext();

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    private Cache cache = null;
    private File httpCacheDirectory;

    private CommonApiService defaultApiService;

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        if (null == SingletonHolder.INSTANCE) {
            SingletonHolder.INSTANCE = new RetrofitClient();
        }
        return SingletonHolder.INSTANCE;
    }

    private RetrofitClient() {
        this(null, null);
    }

    private RetrofitClient(String url, Map<String, String> headers) {

        if (TextUtils.isEmpty(url)) {
            url = NetworkConfig.C.getBaseURL();
//            Logger.i(url);
        }

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "jcb_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            KLog.e("Could not create http cache", e);
        }

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
//                .cache(cache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CacheInterceptor(mContext))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(BuildConfig.DEBUG) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .build()
                )
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为15s
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();

        defaultApiService = retrofit.create(CommonApiService.class);
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(ActivityMain.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(ActivityMain.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return null;
    }

    /**
     * /api/
     *
     * @param paramMap
     */
    private Observable<ResponseBody> request(LifecycleProvider lifecycleProvider, String api, String token, Map paramMap) {
        final String data = GsonUtils.toJson(paramMap);
        okhttp3.RequestBody body = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data);
        Observable<ResponseBody> observable = defaultApiService
                .post(api, token, body)
                .compose(RxUtils.bindToLifecycle(lifecycleProvider)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()); // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
        return observable;
    }

    public Observable<ResponseBody> request(LifecycleProvider lifecycleProvider, String httpMethod, String api, String token, Map paramMap) {
        Observable<ResponseBody> observable = null;

        if (HttpMethod.POST.equals(httpMethod)) {
            observable = request(lifecycleProvider, api, token, paramMap);
        } else if (HttpMethod.GET.equals(httpMethod)) {
            observable = defaultApiService
                    .get(api, token, paramMap)
                    .compose(RxUtils.bindToLifecycle(lifecycleProvider)) //请求与View周期同步
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .compose(RxUtils.exceptionTransformer()); //网络错误的异常转换
        }

        return observable;
    }


    //不绑定生命周期
    public <T extends RespBase> void requestNetWork(String api, Map<String, String> paramMap, final RequestCallback<T> requestCallback) {
        final String data = GsonUtils.toJson(paramMap);
        okhttp3.RequestBody body = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data);

        defaultApiService
                .post(api, Perference.get(Perference.TOKEN), body)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        requestCallback.beforeRequest();
                    }
                }).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody resp) throws Exception {
                String res = resp.string();
                if (!CommonUtils.isJson(res)) {
                    requestCallback.requestError("1001", res);
                    return;
                }

                Type type = ((ParameterizedType) requestCallback.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                T data = GsonUtils.fromJson(res, type);

                if (ApiConstants.RETURN_STATUS_SUC.equals(data.getResponseCode())) {
                    requestCallback.response(data);
                } else if (ApiConstants.RETURN_STATUS_EOR.equals(data.getResponseCode()) || ApiConstants.RETURN_STATUS_OUTDATE.equals(data.getResponseCode())) {
                    requestCallback.requestError(data.getResponseCode(), data.getResponseMsg());
                }

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                ResponseThrowable responseThrowable = (ResponseThrowable) throwable;
                requestCallback.requestError(String.valueOf(responseThrowable.code), responseThrowable.message);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                requestCallback.requestComplete();
            }
        });
    }

    public static void clear() {
        SingletonHolder.INSTANCE = null;
    }
}
