package com.wisdom.jsinterfacelib.handler;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.ConstantString;
import com.wisdom.jsinterfacelib.R;
import com.wisdom.jsinterfacelib.model.BaseModel;

/**
 * Created by Tsing on 2020/8/19
 * 是否隐藏导航栏
 */
public class ToggleNavigationBarHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        BaseModel baseModel;
        try {
            ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                View view = LayoutInflater.from(context).inflate(R.layout.action_bar, null, false);
                ImageView back = ((ImageView) view.findViewById(R.id.back));
                back.setOnClickListener(view1 -> {
                    ((AppCompatActivity) context).finish();
                });
                if(!"".equals(ConstantString.ACTION_BAR_RGB)){
                    view.setBackgroundColor(Color.parseColor(ConstantString.ACTION_BAR_RGB));
                }
                actionBar.setCustomView(view);
                if (Boolean.parseBoolean(data)) {
                    actionBar.hide();
                    baseModel = new BaseModel("标题栏隐藏成功", 0, "标题栏隐藏成功");
                } else {
                    actionBar.show();
                    baseModel = new BaseModel("标题栏显示成功", 0, "标题栏显示成功");
                }
            } else {
                baseModel = new BaseModel("标题栏隐藏成功", 0, "标题栏已经是隐藏状态");
            }
        } catch (Exception e) {
            baseModel = new BaseModel("标题栏隐藏失败", -1, "WebView所在Activity需继承AppCompatActivity");
            e.printStackTrace();
        }
        function.onCallBack(GsonUtils.toJson(baseModel));
    }
}
