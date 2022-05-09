package com.wisdom.jsinterfacelib.handler;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.BridgeWebView;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.ConstantString;
import com.wisdom.jsinterfacelib.R;
import com.wisdom.jsinterfacelib.model.BaseModel;

import static com.wisdom.jsinterfacelib.ConstantString.CAN_BACK_KEY_USEFUL;

/**
 * 拦截原生导航栏的返回事件，由vue处理
 */
public class HookNavigationBackActionHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        try {
            if (data != null) {
                if ("".equals(data)) {
                    //不屏蔽物理返回键
                    CAN_BACK_KEY_USEFUL = false;
                    BaseModel baseModel = new BaseModel("操作成功", 0, "操作成功");
                    function.onCallBack(GsonUtils.toJson(baseModel));
                    ((AppCompatActivity) context).finish();
                } else {
                    String newData = data.substring(1, data.length() - 1);
                    String jsFunction = "javascript:" + newData + "()";
                    LogUtils.i("接到的js方法："+jsFunction);
                    ConstantString.JS_FUN_NAME = jsFunction;
                    //获取页面的webView
                    BridgeWebView webView = ((AppCompatActivity) context).findViewById(R.id.wv_webview);
                    //屏蔽上导航的返回按钮
                    ImageView back = ((AppCompatActivity) context).findViewById(R.id.back);
                    /**
                    if (back != null) {
                        back.setOnClickListener(view -> {
                            //什么都不做
//                           交给js方法处理，后面调用了指定的js方法
                            webView.loadUrl(jsFunction);
                        });
                    }
                     **/
                    //屏蔽物理返回键
                    CAN_BACK_KEY_USEFUL = true;
                    BaseModel baseModel = new BaseModel("操作成功", 0, "操作成功");
                    function.onCallBack(GsonUtils.toJson(baseModel));
                }
            } else {
                //不屏蔽物理返回键
                CAN_BACK_KEY_USEFUL = false;
                BaseModel baseModel = new BaseModel("操作成功", 0, "操作成功");
                function.onCallBack(GsonUtils.toJson(baseModel));
                ((AppCompatActivity) context).finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //不屏蔽物理返回键
            CAN_BACK_KEY_USEFUL = false;
            BaseModel baseModel = new BaseModel("解析异常", -1, e.getMessage());
            function.onCallBack(GsonUtils.toJson(baseModel));
        }
    }


}
