package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.model.DeviceModel;

import java.util.List;

/**
 * 获取设备信息
 */
public class DeviceInfoHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, final CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        PermissionX.init(((AppCompatActivity) context))
                .permissions(Manifest.permission.READ_PHONE_STATE)
                .request(new RequestCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            try {
                                DeviceModel model = new DeviceModel();
                                model.setAppID(AppUtils.getAppPackageName());
                                model.setAppName(AppUtils.getAppName());
                                model.setAppVersion(AppUtils.getAppVersionName());
                                model.setDeviceName(DeviceUtils.getModel());
                                model.setSysVersion(DeviceUtils.getSDKVersionCode() + "");
                                model.setHybridVersion(DeviceUtils.getManufacturer());
                                model.setIMEI(PhoneUtils.getIMEI());
                                model.setChannelID("");
                                model.setSysType(2);
                                try {
                                    //获取MAC地址可能会出问题，所以单独拦截处理。
                                    WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                    WifiInfo wi = wm.getConnectionInfo();
                                    String macAddr = wi.getMacAddress();
                                    model.setMac(macAddr);
                                } catch (Exception e) {
                                    model.setMac("");
                                    e.printStackTrace();
                                }
                                BaseModel baseModel = new BaseModel("设备信息获取成功", 0, model);
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            } catch (Exception e) {
                                BaseModel baseModel = new BaseModel("设备信息获取失败", -1, "设备信息获取失败,数据解析失败");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            }
                        } else {
                            BaseModel baseModel = new BaseModel("设备信息获取失败，权限被拒绝", -1, "设备信息获取失败,权限被拒绝");
                            function.onCallBack(GsonUtils.toJson(baseModel));
                        }
                    }
                });


    }


}
