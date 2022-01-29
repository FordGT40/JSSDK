package com.wisdom.jssdk_basic;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.uuzuche.lib_zxing.ZApplication;
import com.wisdom.jsinterfacelib.JSCallInterface;
import com.wisdom.jsinterfacelib.InitJsSDKUtil;
import com.wisdom.jsinterfacelib.MainFunctionCallback;

/**
 * @author
 * @ProjectName project：
 * @class package：
 * @class describe：
 * @time
 * @change
 */
public class MyApplication extends ZApplication {
    @Override
    public void onCreate() {
        super.onCreate();


        JSCallInterface JSCallInterface = new JSCallInterface() {

            @Override
            public void onFunctionCompleted(Context context, String methodName, String args, MainFunctionCallback callback) {
                super.onFunctionCompleted(context, methodName, args, callback);
                LogUtils.i("收到的方法名：" + methodName);
                LogUtils.i("收到的参数列：" + args);
                Intent intent = new Intent(MyApplication.this, ScanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                callback.functionCallBack("回调到jssdk里面了");
            }
        };
        InitJsSDKUtil.init(this, true, JSCallInterface);
    }
}
