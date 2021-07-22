package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.content.Context;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.R;
import com.wisdom.jsinterfacelib.model.BaseModel;

import org.json.JSONObject;

import java.util.List;

/**
 *### 获取网络状态
 * `hz.networkStatus(Object object, Function callback)`
 */
public class NetworkStatusHandler extends BridgeHandler {
    @Override
    public void handler(final Context context, final String data, final CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        PermissionX.init(((AppCompatActivity) context))
                .permissions(Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CHANGE_NETWORK_STATE)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                final String action = jsonObject.optString("action");//networkStatusChange 网络变化时调用此方法
                                int status = 0;
                                if (NetworkUtils.isConnected()) {
                                    //在联网的状态下，不是wifi就是数据流量
                                    if (NetworkUtils.isWifiConnected()) {
                                        status = 2;
                                    } else {
                                        status = 1;
                                    }
                                }
                                JSONObject jsonObject1 = new JSONObject();
                                jsonObject1.put("status", status);
                                if (!"".equals(action)) {
                                    final WebView webView = ((AppCompatActivity) context).findViewById(R.id.wv_webview);
                                    NetworkUtils.registerNetworkStatusChangedListener(new NetworkUtils.OnNetworkStatusChangedListener() {
                                        @Override
                                        public void onDisconnected() {
                                            webView.loadUrl("javascript:" + action + "()");
                                        }

                                        @Override
                                        public void onConnected(NetworkUtils.NetworkType networkType) {
                                            webView.loadUrl("javascript:" + action + "()");
                                        }
                                    });
                                }

                                BaseModel baseModel = new BaseModel("获取获取网络状态成功", 0, jsonObject1.toString());
                                function.onCallBack(GsonUtils.toJson(baseModel).replaceAll("\\\\",""));
                            } catch (Exception e) {
                                e.printStackTrace();
                                BaseModel baseModel = new BaseModel("获取网络状态失败", -1, "数据解析异常，请检查传参");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            }
                        } else {
                            BaseModel baseModel = new BaseModel("获取网络状态失败", -1, "获取网络状态失败,授权被拒绝");
                            function.onCallBack(GsonUtils.toJson(baseModel));
                        }
                    }
                });


    }


}
