package com.huateng.ebank.network.helper;

import android.os.Environment;


import com.huateng.ebank.R;
import com.huateng.ebank.network.ApiConstants;
import com.huateng.ebank.network.NetworkConfig;
import com.huateng.ebank.network.update.UpdateAppBean;
import com.huateng.ebank.network.update.UpdateAppManager;
import com.huateng.ebank.network.update.UpdateCallback;
import com.huateng.ebank.network.update.listener.ExceptionHandler;
import com.huateng.ebank.network.update.listener.IUpdateDialogFragmentListener;
import com.huateng.ebank.network.update.utils.AppUpdateUtils;
import com.huateng.ebank.utils.GsonUtils;
import com.kongzue.dialog.v3.TipDialog;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * Created by Sumincy on 2018/8/26.
 * 更新帮助类
 */
public class UpdateHelper {
    /**
     * @param context
     * @param isActive true主动调用 显示是否有更新  显示进度条   false 不显示进度条 不显示查询信息
     */
    public static void checkupdate(final BaseActivity context, final boolean isActive) {

        //子线程中 检查更新  恢复推送
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String path;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
                    path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                } else {
                    path = context.getCacheDir().getAbsolutePath();
                }
                emitter.onNext(path);
            }
        }).compose(RxUtils.schedulersTransformer()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String path) throws Exception {

                Map<String, String> params = new HashMap<>();
                final int code = AppUpdateUtils.getVersionCode(context);
                //版本号
                params.put("versionState", "1");
                //平台 android
                params.put("app_type", "0");

                String updateUrl = String.format("%s/%s", NetworkConfig.C.getBaseURL(), ApiConstants.API_CLOSE_ACCOUNT);

                new UpdateAppManager
                        .Builder()
                        //必须设置，当前Activity
                        .setActivity(context)
                        //必须设置，实现httpManager接口的对象
                        .setHttpManager(new UpdateManager())
                        //必须设置，更新地址
                        .setUpdateUrl(updateUrl)
                        //全局异常捕获
                        .handleException(new ExceptionHandler() {
                            @Override
                            public void onException(Exception e) {
                                e.printStackTrace();
                            }
                        })
                        //以下设置，都是可选
                        //设置请求方式，默认get
                        .setPost(true)
                        //不显示通知栏进度条
//                .dismissNotificationProgress()
                        //是否忽略版本
//                .showIgnoreVersion()
                        //添加自定义参数，默认version=1.0.0（app的versionName）；
                        .setParams(params)
                        //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度，如果是强制更新，则设置无效
//                .hideDialogOnDownloading()
                        //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
                        .setTopPic(R.mipmap.top_3)
                        //为按钮，进度条设置颜色。
                        .setThemeColor(0xFF39C0E8)
                        //设置apk下载路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
                        .setTargetPath(path)
                        .setUpdateDialogFragmentListener(new IUpdateDialogFragmentListener() {
                            @Override
                            public void onUpdateNotifyDialogCancel(UpdateAppBean updateApp) {
                                //用户点击关闭按钮，取消了更新，如果是下载完，用户取消了安装，则可以在 onActivityResult 监听到。

                            }
                        })
                        //忽略 参数appkey
                        .setIgnoreDefParams(true)
                        .build()
                        //检测是否有新版本
                        .checkNewApp(new UpdateCallback() {
                            /**
                             * 解析json,自定义协议
                             *
                             * @param json 服务器返回的json
                             * @return UpdateAppBean
                             */
                            @Override
                            protected UpdateAppBean parseJson(String json) {
                                RespVerionInfo version = GsonUtils.fromJson(json, RespVerionInfo.class);
                                RespVerionInfo.DataBean dataBean = version.getData();

                                UpdateAppBean updateAppBean = new UpdateAppBean();
                                try {

                                    if (null != dataBean) {
                                        boolean isContraint = "1".equals(dataBean.getIs_force());
                                        String title;

                                        if (isContraint) {
                                            title = String.format("升级到%s版本？", dataBean.getVersion_code());
                                        } else {
                                            title = String.format("是否升级到%s版本？", dataBean.getVersion_code());
                                        }

                                        updateAppBean
                                                //（必须）是否更新Yes,No
                                                .setUpdate(code < Integer.valueOf(dataBean.getVERSIONID()) ? "Yes" : "No")
                                                //（必须）新版本号，
                                                .setNewVersion(dataBean.getVersion_code())
                                                //（必须）下载地址
                                                //测试下载路径是重定向路径
                                                .setApkFileUrl(dataBean.getDownload_url())

                                                .setUpdateDefDialogTitle(title)
                                                //（必须）更新内容
                                                //测试内容
                                                .setUpdateLog(dataBean.getDEF2())
                                                //大小，不设置不显示大小，可以不设置
                                                // .setTargetSize(jsonObject.optString("target_size"))
                                                //是否强制更新，可以不设置
                                                .setConstraint(isContraint);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return updateAppBean;
                            }

                            @Override
                            protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                                updateAppManager.showDialogFragment();
                            }

                            /**
                             * 网络请求之前
                             */
                            @Override
                            public void onBefore() {
                                if (isActive) {
                                    context.showDialog();
                                }
                            }

                            /**
                             * 网络请求之后
                             */
                            @Override
                            public void onAfter() {
                                if (isActive) {
                                    context.dismissDialog();
                                }
                            }

                            /**
                             * 没有新版本
                             */
                            @Override
                            public void noNewApp(String error) {
                                if (isActive) {
                                    TipDialog.show(context, "没有新版本", TipDialog.TYPE.SUCCESS);
                                }
                            }
                        });
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }
}
