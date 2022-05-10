package com.wisdom.jsinterfacelib.handler;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
 * 设置导航栏标题
 */
public class NavigationBarTitleHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        BaseModel baseModel;
        try {
            ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();

            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View view = LayoutInflater.from(context).inflate(R.layout.action_bar, null, false);
            ImageView back = ((ImageView) view.findViewById(R.id.back));
            TextView tv_title = ((TextView) view.findViewById(R.id.tv_title_js));
            if(!"".equals(ConstantString.ACTION_BAR_RGB)){
                view.setBackgroundColor(Color.parseColor(ConstantString.ACTION_BAR_RGB));
            }
            back.setOnClickListener(view1 -> {
                ((AppCompatActivity) context).finish();
            });
            actionBar.setCustomView(view);

            if (actionBar != null) {
                String titleStr=data.substring(1,data.length()-1);
                actionBar.setTitle(titleStr);
                tv_title.setText(titleStr);
                baseModel = new BaseModel("导航栏标题设置成功", 0, "导航栏标题设置成功");
            } else {
                baseModel = new BaseModel("导航栏标题设置失败", 0, "标题栏已经是隐藏状态");
            }
        } catch (Exception e) {
            baseModel = new BaseModel("导航栏标题设置失败", -1, "WebView所在Activity需继承AppCompatActivity");
            e.printStackTrace();
        }
        function.onCallBack(GsonUtils.toJson(baseModel));
    }

}
