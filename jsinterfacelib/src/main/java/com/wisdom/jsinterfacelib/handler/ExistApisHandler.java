package com.wisdom.jsinterfacelib.handler;

import android.content.Context;

import com.blankj.utilcode.util.GsonUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.utils.JsInterfaceBridge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 判断当前客户端版本是否支持指定 JavaScript 接口（多个）
 */
public class ExistApisHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        try {
            JSONObject jsonObject= new JSONObject(data);
            JSONArray array = jsonObject.optJSONArray("paths");
            String unSupportApi = "";
            for (int i = 0; i < array.length(); i++) {
                String api = (String) array.opt(i);
                if (!JsInterfaceBridge.map.keySet().contains("WISDOM.app." + api)) {
                    unSupportApi += api + ",";
                }
            }
            if (!unSupportApi.equals("")) {
                unSupportApi = unSupportApi.substring(0, unSupportApi.length() - 1);
            }

            BaseModel baseModel;
            if (unSupportApi.equals("")) {
                baseModel = new BaseModel("该版本全部Api均支持", 0, "支持");
            } else {
                baseModel = new BaseModel(unSupportApi, -1, "不支持");
            }
            function.onCallBack(GsonUtils.toJson(baseModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
