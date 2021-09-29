package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.utils.OkGoController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * |类型	|参数	|是否必须	|说明|
 * |-------|-------|------|------|
 * |String	|url	|是	|上传的服务器路径|
 * |Array	|files	|是	| 上传的文件 |
 * |Object	|ext	|否	| 额外需要上传的参数 |
 * <p>
 * #### files说明
 * |类型	|参数	|是否必须	|说明|
 * |-------|-------|------|------|
 * |String	|filekey	|是	|服务器接收文件的字段名称 |
 * |String	|fileUrl	|是	|本地文件路径 通过选择视频、录制视频获取到的本地文件路径 |
 */
public class UpLoadFileHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
            PermissionX.init(((AppCompatActivity) context)).permissions(Manifest.permission.INTERNET).request(new RequestCallback() {
                @Override
                public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                    if(allGranted){
                        if(NetworkUtils.isConnected()){
                           //有网络可以正常进行
                            //解析数据并将解析的数据上传到指定服务器
                            analyticalDataAndUpLoad(data,function);
                        }else{
                            //没有网络，不能够正常进行
                            BaseModel baseModel = new BaseModel("网络异常", -1, "网络异常");
                            function.onCallBack(GsonUtils.toJson(baseModel));
                        }
                    }else{
                        BaseModel baseModel = new BaseModel("未授权网络访问权限", -1, "未授权网络访问权限");
                        function.onCallBack(GsonUtils.toJson(baseModel));
                    }
                }
            });



    }


    /**
     * 解析传入的数据源，并且将数据源拼装成HttpParamas对象进行上传操作
     * @param data
     * @param function
     */
    private void analyticalDataAndUpLoad(String data, CallBackFunction function) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String url = jsonObject.optString("url");
            JSONArray files = jsonObject.optJSONArray("files");
            JSONObject ext = jsonObject.optJSONObject("ext");
            //拼装参数
            HttpParams params = new HttpParams();
            //遍历文件数组（支持多文件上传）
            if (files.length() > 0) {
                for (int i = 0; i < files.length(); i++) {
                    JSONObject fileObject = files.getJSONObject(i);
                    String fileKey = fileObject.optString("fileKey");
                    String fileUrl = fileObject.optString("fileUrl");
                    params.put(fileKey, new File(fileUrl));
                }
            }
            //其他参数（键值对的形式）
            if (ext != null) {
                Iterator<String> it = ext.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = ext.getString(key);
                    params.put(key, value);
                }
            }
            upLoadFiles(url,params,function);
        } catch (Exception e) {
            e.printStackTrace();
            BaseModel baseModel = new BaseModel("解析数据失败", -1, e.getMessage());
            function.onCallBack(GsonUtils.toJson(baseModel));
        }
    }


    /**
     * 上传文件的方法（支持多文件上传）
     * @param url  文件服务器的地址
     * @param params 参数对象
     * @param function 回调方法（结果回调给js端）
     */
    private void upLoadFiles(String url, HttpParams params, CallBackFunction function) {
        OkGoController.create().postFile(url, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, okhttp3.Response response) {
                BaseModel baseModel = new BaseModel("上传成功", 0, s);
                function.onCallBack(GsonUtils.toJson(baseModel));
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                BaseModel baseModel = new BaseModel("上传失败", -1, "上传失败返回结果："+response.body());
                function.onCallBack(GsonUtils.toJson(baseModel));
            }
        });

    }


}
