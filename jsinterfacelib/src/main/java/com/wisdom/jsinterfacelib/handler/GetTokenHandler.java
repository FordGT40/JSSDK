package com.wisdom.jsinterfacelib.handler;

import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.model.TokenModel;

import org.json.JSONObject;

/**
 *### 获取App的Token
 */
public class GetTokenHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        try{
            String token= SPUtils.getInstance().getString("token");
            if(token==null||"".equals(token)){
                BaseModel baseModel = new BaseModel("操作失败，请先设置token", -1, "请先设置token");
                function.onCallBack(GsonUtils.toJson(baseModel));
            }else {
                TokenModel tokenModel=new TokenModel();
                tokenModel.setToken(token);
                BaseModel baseModel = new BaseModel("操作成功", 0, tokenModel);
//                function.onCallBack(GsonUtils.toJson(baseModel).replaceAll("\\\\",""));
                function.onCallBack(GsonUtils.toJson(baseModel));
            }
        }catch (Exception e){
            BaseModel baseModel = new BaseModel("操作失败", -1, "请先设置token");
            function.onCallBack(GsonUtils.toJson(baseModel));
        }
    }


}
