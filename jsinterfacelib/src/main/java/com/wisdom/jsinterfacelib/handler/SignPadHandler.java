package com.wisdom.jsinterfacelib.handler;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.utils.ImageUtil;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.model.SignEvent;
import com.wisdom.jsinterfacelib.model.SignModel;
import com.wisdom.jsinterfacelib.weight.SignViewDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Tsing on 2020/8/19
 * 签字板
 */
public class SignPadHandler extends BridgeHandler {
    private SignViewDialog signViewDialog;
    private Context context;
    private CallBackFunction function;

    @Override
    public void handler(final Context context, String data, final CallBackFunction function) {

        EventBus.getDefault().register(SignPadHandler.this);
        this.context = context;
        this.function = function;

        signViewDialog = new SignViewDialog(context) {
            @Override
            public void onComplete(String path) {
                //完成签名，拿到签名路径
                EventBus.getDefault().unregister(SignPadHandler.this);
                String picData = "data:image/png;base64," + ImageUtil.ImageToBase64Compress(path);
                SignModel signModel=new SignModel();
                signModel.setSignData(picData);
                BaseModel baseModel = new BaseModel("获取签名图片成功", 0, signModel);
                function.onCallBack(GsonUtils.toJson(baseModel));

            }
        };
        signViewDialog.show();
        WindowManager windowManager = ((AppCompatActivity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = signViewDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        signViewDialog.getWindow().setAttributes(lp);
        signViewDialog.getWindow().setGravity(Gravity.BOTTOM);

        signViewDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                EventBus.getDefault().unregister(SignPadHandler.this);
            }
        });
        /**
         Intent intent = new Intent();
         intent.putExtra("color", "#ff0000");
         intent.setClass(context, DrawingBoardActivity.class);
         new AvoidOnResult(((AppCompatActivity) context)).startForResult(intent, new AvoidOnResult.Callback() {
        @Override public void onActivityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_CODE_DRAWING) {
        if (data != null) {
        String path = data.getStringExtra("path");
        String picData = "data:image/png;base64," + ImageUtil.ImageToBase64Compress(path);
        BaseModel baseModel = new BaseModel("获取签名图片成功", 0, picData);
        function.onCallBack(GsonUtils.toJson(baseModel));
        } else {
        BaseModel baseModel = new BaseModel("返回签字图片数据异常", -1, "返回签字图片数据异常");
        function.onCallBack(GsonUtils.toJson(baseModel));

        }

        } else {
        BaseModel baseModel = new BaseModel("用户取消了操作", -1, "用户取消了操作");
        function.onCallBack(GsonUtils.toJson(baseModel));
        }
        }
        });
         **/
    }

    @Subscribe
    public void onSignEvent(SignEvent event) {

        if (signViewDialog != null && signViewDialog.isShowing()) {
            signViewDialog.dismiss();
        }
        EventBus.getDefault().unregister(SignPadHandler.this);
        String picData = "data:image/png;base64," + ImageUtil.ImageToBase64Compress(event.path);
        SignModel signModel=new SignModel();
        signModel.setSignData(picData);
        BaseModel baseModel = new BaseModel("获取签名图片成功", 0, signModel);
        function.onCallBack(GsonUtils.toJson(baseModel));
    }

}
