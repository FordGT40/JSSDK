package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.SPUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;

import org.json.JSONObject;

import java.util.List;

/**
 * ### 设置token
 */
public class SetTokenHandler extends BridgeHandler {
    @Override
    public void handler(Context context, final String data, final CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        PermissionX.init(((AppCompatActivity) context))
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            String token = jsonObject.optString("token");
                            if ("".equals(token)) {
                                BaseModel baseModel = new BaseModel("操作失败", -1, "token不能为空，请检查传参");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            } else {
                                SPUtils.getInstance().put("token",token);
                                BaseModel baseModel = new BaseModel("操作成功", 0, "操作成功");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            BaseModel baseModel = new BaseModel("操作失败", -1, "数据解析异常，请检查传参");
                            function.onCallBack(GsonUtils.toJson(baseModel));
                        }
                    } else {
                        BaseModel baseModel = new BaseModel("操作失败", -1, "授权被拒绝");
                        function.onCallBack(GsonUtils.toJson(baseModel));
                    }
                });
    }


}
