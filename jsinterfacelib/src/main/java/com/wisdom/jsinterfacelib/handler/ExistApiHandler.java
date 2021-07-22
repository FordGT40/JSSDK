package com.wisdom.jsinterfacelib.handler;

import android.content.Context;

import com.blankj.utilcode.util.GsonUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.utils.JsInterfaceBridge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tsing on 2020/8/27
 *判断当前客户端版本是否支持指定 JavaScript 接口（单个）
 */
public class ExistApiHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String api = jsonObject.optString("path");
            BaseModel baseModel;
            if (JsInterfaceBridge.map.keySet().contains("WISDOM.app."+api)) {
                baseModel = new BaseModel("该版本支持此Api", 0, "支持");
            } else {
                baseModel = new BaseModel("该版本暂不支持此Api", -1, "不支持");
            }
            function.onCallBack(GsonUtils.toJson(baseModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
