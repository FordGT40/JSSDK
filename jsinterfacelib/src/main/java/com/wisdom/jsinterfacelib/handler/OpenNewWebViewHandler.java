package com.wisdom.jsinterfacelib.handler;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.activity.WebViewActivity;
import com.wisdom.jsinterfacelib.model.BaseModel;

import org.json.JSONObject;

/**
 *### 跳转至新 WebView 并加载
 * `hz.openNewWebView(Object object)`
 */
public class OpenNewWebViewHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String url = jsonObject.optString("url");
            Boolean hideNavBar = jsonObject.optBoolean("hideNavBar");
            Boolean closeCurWeb = jsonObject.optBoolean("closeCurWeb");
            if ("".equals(url)) {
                BaseModel baseModel = new BaseModel("url不能为空", -1, "Api调用失败，url不能为空");
                function.onCallBack(GsonUtils.toJson(baseModel));
            } else {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("hideNavBar", hideNavBar);
                context.startActivity(intent);
                BaseModel baseModel = new BaseModel("Api调用成功", 0, "Api调用成功");
                function.onCallBack(GsonUtils.toJson(baseModel));
                if (closeCurWeb) {
                    ((AppCompatActivity) context).finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            BaseModel baseModel = new BaseModel("调用失败，解析异常", -1, "Api调用失败，调用失败，解析异常");
            function.onCallBack(GsonUtils.toJson(baseModel));
        }


    }


}
