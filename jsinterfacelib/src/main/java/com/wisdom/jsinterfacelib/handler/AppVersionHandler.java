package com.wisdom.jsinterfacelib.handler;

import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;

/**
 * ### 获取App版本号
 */
public class AppVersionHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        try {
            BaseModel baseModel = new BaseModel("获取版本号成功", 0, AppUtils.getAppVersionName());
            function.onCallBack(GsonUtils.toJson(baseModel));
        } catch (Exception e) {
            e.printStackTrace();
            BaseModel baseModel = new BaseModel("获取版本号失败", -1, e.getMessage());
            function.onCallBack(GsonUtils.toJson(baseModel));
        }
    }


}
