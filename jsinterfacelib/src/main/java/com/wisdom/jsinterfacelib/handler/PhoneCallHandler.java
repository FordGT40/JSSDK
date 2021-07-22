package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 打电话
 */
public class PhoneCallHandler extends BridgeHandler {
    @Override
    public void handler(Context context, final String data, final CallBackFunction function) {
        PermissionX.init(((AppCompatActivity) context)).permissions(Manifest.permission.CALL_PHONE).request(new RequestCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if(allGranted){
                    try {
                        LogUtils.i("接到的json:"+data);
                        BaseModel baseModel;
                        if ("".equals(data)) {
                            //电话空
                            baseModel = new BaseModel("电话号码不能为空", -1, "电话号码不能为空");
                        } else {
                            //正常拨打电话
                        PhoneUtils.call(data);
                            baseModel = new BaseModel("拨打电话成功", 0, "拨打电话成功");
                        }
                        function.onCallBack(GsonUtils.toJson(baseModel));
                    } catch (Exception e) {
                        e.printStackTrace();
                        BaseModel  baseModel = new BaseModel("拨打电话失败", -1, "入参解析失败，请查看文档");
                        function.onCallBack(GsonUtils.toJson(baseModel));
                    }
                }else{
                    BaseModel  baseModel = new BaseModel("拨打电话失败", -1, "用户权限被拒");
                    function.onCallBack(GsonUtils.toJson(baseModel));
                }
            }
        });

    }
}
