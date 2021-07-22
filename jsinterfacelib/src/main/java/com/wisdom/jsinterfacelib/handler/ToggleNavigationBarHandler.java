package com.wisdom.jsinterfacelib.handler;

import android.content.Context;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;

/**
 * Created by Tsing on 2020/8/19
 * 是否隐藏导航栏
 */
public class ToggleNavigationBarHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json："+data);
        BaseModel baseModel;
        try {
            ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
            if (actionBar != null) {
                if (Boolean.parseBoolean(data)) {
                    actionBar.hide();
                    baseModel = new BaseModel("标题栏隐藏成功", 0, "标题栏隐藏成功");
                } else {
                    actionBar.show();
                    baseModel = new BaseModel("标题栏显示成功", 0, "标题栏显示成功");
                }
            }else{
                baseModel = new BaseModel("标题栏隐藏成功", 0, "标题栏已经是隐藏状态");
            }

        } catch (Exception e) {
            baseModel = new BaseModel("标题栏隐藏失败", -1, "WebView所在Activity需继承AppCompatActivity");
            e.printStackTrace();
        }
        function.onCallBack(GsonUtils.toJson(baseModel));
    }
}
