package com.wisdom.jsinterfacelib.handler;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;

/**
 *### 退出当前 WebView
 * `hz.close(Function callback)`
 */
public class WebViewCloseHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        try {
            ((AppCompatActivity) context).finish();
            BaseModel baseModel = new BaseModel("Api调用成功", 0, "Api调用成功");
            function.onCallBack(GsonUtils.toJson(baseModel));
        } catch (Exception e) {
            e.printStackTrace();
            BaseModel baseModel = new BaseModel("Api调用失败", -1, "Api调用失败");
            function.onCallBack(GsonUtils.toJson(baseModel));
        }
    }


}
