package com.wisdom.jsinterfacelib.handler;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.codersun.fingerprintcompat.AonFingerChangeCallback;
import com.codersun.fingerprintcompat.FingerManager;
import com.codersun.fingerprintcompat.SimpleFingerCheckCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.weight.MyFingerDialog;
import com.wisdom.jsinterfacelib.model.BaseModel;

/**
 * 指纹识别
 */
public class FingerPrintCompareHandler extends BridgeHandler {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void handler(Context context, String data, final CallBackFunction function) {
        BaseModel baseModel;
        switch (FingerManager.checkSupport(context))
        {
            case DEVICE_UNSUPPORTED:
                baseModel = new BaseModel("您的设备不支持指纹", -1, "您的设备不支持指纹");
                function.onCallBack(GsonUtils.toJson(baseModel));
                break;
            case SUPPORT_WITHOUT_DATA:
                baseModel = new BaseModel("请在系统录入指纹后再验证", -1, "请在系统录入指纹后再验证");
                function.onCallBack(GsonUtils.toJson(baseModel));
                break;
            case SUPPORT:
                FingerManager.build().setApplication(((AppCompatActivity) context).getApplication())
                        .setTitle("指纹验证")
                        .setDes("请按下指纹")
                        .setNegativeText("取消")
                        .setFingerDialogApi23(new MyFingerDialog())//如果你需要自定义android P 一下系统弹窗就设置,不设置会使用默认弹窗
                        .setFingerCheckCallback(new SimpleFingerCheckCallback()
                        {
                            @Override
                            public void onSucceed()
                            {
                                BaseModel  baseModel = new BaseModel("验证成功", 0, "验证成功");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            }


                            @Override
                            public void onError(String error)
                            {
                                BaseModel  baseModel = new BaseModel("验证失败", -1, "验证失败");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            }

                            @Override
                            public void onCancel()
                            {
                                BaseModel  baseModel = new BaseModel("您取消了识别", -1, "您取消了识别");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            }
                        })
                        .setFingerChangeCallback(new AonFingerChangeCallback()
                        {

                            @Override
                            protected void onFingerDataChange()
                            {
                                BaseModel  baseModel = new BaseModel("指纹数据发生了变化", -1, "指纹数据发生了变化");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            }
                        })
                        .create()
                        .startListener(((AppCompatActivity) context));
                break;
        }

    }


}
