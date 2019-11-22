package com.huateng.ebank.ui.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.huateng.ebank.R;
import com.huateng.ebank.app.Paths;
import com.orhanobut.logger.Logger;
import com.tools.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import me.yokeyword.fragmentation.SupportActivity;

@Route(path = Paths.PAGE_MAIN)
public class ActivityMain extends SupportActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestCameraPermissions();
    }

    private void requestCameraPermissions() {
        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "手机状态", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储卡", R.drawable.permission_ic_storage));
//        permissionItems.add(new PermissionItem(Manifest.permission.CALL_PHONE, "拨打电话", R.drawable.permission_ic_phone));

        HiPermission.create(this)
                .permissions(permissionItems)
                .style(R.style.PermissionBlueStyle)
                .filterColor(ContextCompat.getColor(this, R.color.colorBlue))//图标的颜色
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        finish();
                        Logger.e("onClose");
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        Logger.e("onDeny");
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });
    }

    @Override
    public void onBackPressedSupport() {
        //将app切换到后台
        if (RxToast.doubleClickExit()) {
            if (isTaskRoot()) {
                moveTaskToBack(false);
            } else {
                // 如果用户点击了系统返回键，则模拟HOME键
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        }
    }
}
