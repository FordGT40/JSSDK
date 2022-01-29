package com.wisdom.jsinterfacelib.handler;

import android.content.Context;



import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.JSCallInterface;
import com.wisdom.jsinterfacelib.MainFunctionCallback;
import com.wisdom.jsinterfacelib.model.BaseModel;

import org.json.JSONObject;

/**
 * ### 获取App版本号
 */
public class InvokeMethodHandler extends BridgeHandler {
    private JSCallInterface JSCallInterface;

    public InvokeMethodHandler(JSCallInterface JSCallInterface) {
        this.JSCallInterface = JSCallInterface;
    }

    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        try {
            //{"methodName":"invokeMethod","data":{}}
            LogUtils.i("接到的json：" + data);
            JSONObject jsonObject=new JSONObject(data);
            String methodName=jsonObject.getString("methodName");
            JSONObject jsonObjectData=jsonObject.optJSONObject("data");
           Object object = jsonObject.opt("data");


            JSCallInterface.onFunctionCompleted(context, methodName,jsonObjectData.toString() , new MainFunctionCallback(){
                @Override
                public void functionCallBack(Object obj) {
                    super.functionCallBack(obj);
                    LogUtils.i("收到主工程回调："+obj.toString());
                    BaseModel baseModel = new BaseModel("调用成功", 0, obj);
                    function.onCallBack(GsonUtils.toJson(baseModel));
                }
            });

            JSCallInterface.onFunctionCompleted(context, methodName,object, new MainFunctionCallback(){
                @Override
                public void functionCallBack(Object obj) {
                    super.functionCallBack(obj);
                    LogUtils.i("收到主工程回调："+obj.toString());
                    BaseModel baseModel = new BaseModel("调用成功", 0, obj);
                    function.onCallBack(GsonUtils.toJson(baseModel));
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            BaseModel baseModel = new BaseModel("调用失败", -1, e.getMessage());
            function.onCallBack(GsonUtils.toJson(baseModel));
        }
        /***
         LogUtils.i("接到的json：" + data);
         try {
         BaseModel baseModel = new BaseModel("获取版本号成功", 0, AppUtils.getAppVersionName());
         function.onCallBack(GsonUtils.toJson(baseModel));
         } catch (Exception e) {
         e.printStackTrace();
         BaseModel baseModel = new BaseModel("获取版本号失败", -1, e.getMessage());
         function.onCallBack(GsonUtils.toJson(baseModel));
         }
         }
         ***/
    }
}
