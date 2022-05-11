package com.wisdom.jsinterfacelib.handler;

import android.content.Context;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.R;
import com.wisdom.jsinterfacelib.model.BaseModel;

/**
 *### 返回上一页面
 * `hz.goback()`
 *
 * 当网页在第一页时，会直接退出当前控制器，其他则是返回上一个网页
 */
public class WebViewGoBackHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        try{
            LogUtils.i("返回1");
             WebView webView = ((AppCompatActivity) context).findViewById(R.id.wv_webview);
             if(webView.canGoBack()){
                 webView.goBack();
                 LogUtils.i("返回2");
             }else{
                 ((AppCompatActivity) context).finish();
                 LogUtils.i("返回3");
             }
            BaseModel baseModel = new BaseModel("Api调用成功", 0, "Api调用成功");
            function.onCallBack(GsonUtils.toJson(baseModel));
            LogUtils.i("返回4");
        }catch (Exception e){
            BaseModel baseModel = new BaseModel("Api调用失败", -1, "Api调用失败，控件实例化失败");
            function.onCallBack(GsonUtils.toJson(baseModel));
            LogUtils.i("返回5:"+e.getMessage());
        }
    }


}
