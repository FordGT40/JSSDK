package com.wisdom.jsinterfacelib.handler;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;

import org.json.JSONObject;

import static com.kongzue.dialog.util.DialogSettings.STYLE.STYLE_IOS;
import static com.kongzue.dialog.util.DialogSettings.STYLE.STYLE_MATERIAL;


/**
 * 提示弹窗
 * Created by Tsing on 2020/8/19
 */
public class ToastHandler extends BridgeHandler {

    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        //{"type":"errorText","text":"点击了","detailText":"呵呵呵附件阿卡丽事件东方卡拉胶风口浪尖阿卡丽"}
        BaseModel baseModel;
        try {
            JSONObject jsonObject = new JSONObject(data);
            String type = jsonObject.optString("type");
            String text = jsonObject.optString("text");
            String detailText = jsonObject.optString("detailText");
            if (type!=null&&!"".equals(type)) {
                switch (type) {
                    //默认 text：普通文本 successText：带成功标志的文本 errorText：带错误标志的文本
                    case "successText":
                    case "errorText": {
                        MessageDialog.
                                show((AppCompatActivity) context, text,detailText, "确定")
                                .setOkButton(new OnDialogButtonClickListener() {  //仅需要对需要处理的按钮进行操作
                                    @Override
                                    public boolean onClick(BaseDialog baseDialog, View v) {
                                        //处理确定按钮事务
                                        return false;    //可以通过 return 决定点击按钮是否默认自动关闭对话框
                                    }
                                });

                    }
                    break;
                    case "text": {
                        Toast.makeText(context, detailText, Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            } else {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
            baseModel = new BaseModel("消息提示成功", 0, "消息提示成功");
        } catch (Exception e) {
            baseModel = new BaseModel("消息解析失败：" + data, -1, e.getMessage());
        }
        function.onCallBack(GsonUtils.toJson(baseModel));
    }


}