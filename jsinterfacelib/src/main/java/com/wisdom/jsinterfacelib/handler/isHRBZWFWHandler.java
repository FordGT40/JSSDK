package com.wisdom.jsinterfacelib.handler;

import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;

/**
 * Created by Tsing on 2020/8/19
 */
public class isHRBZWFWHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        if (AppUtils.isAppForeground()) {
            BaseModel baseModel = new BaseModel("获取成功", 0, "当前在APP内");
            String json = GsonUtils.toJson(baseModel);
            function.onCallBack(json);
        } else {
            BaseModel baseModel = new BaseModel("获取成功", 0, "当前不在APP内");
            String json = GsonUtils.toJson(baseModel);
            function.onCallBack(json);
        }
    }
}
