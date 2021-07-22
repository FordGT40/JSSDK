package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.model.LocationInfoResultModel;
import com.wisdom.jsinterfacelib.utils.LocationUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *获取当前位置GPS
 */
public class LocationHandler extends BridgeHandler {
    @Override
    public void handler(final Context context, String data, final CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        if (!NetworkUtils.isConnected()) {
            //没有网络连接无法访问高德的地理编码和反地理编码
            BaseModel baseModel = new BaseModel("网络连接失败", -1, "网络连接失败，请打开网络");
            function.onCallBack(GsonUtils.toJson(baseModel));
            return;
        }

        PermissionX.init(((AppCompatActivity) context))
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            //权限通过
                            if (!LocationUtil.INSTANCE.hasGPSService(context)) {
                                //无法获取到GPS服务
                                BaseModel baseModel = new BaseModel("GPS位置信息获取失败，请打开GPS服务", -1, "未打开gps服务");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                                return;
                            }
                            LocationUtil.getLocationData(((AppCompatActivity) context), new LocationUtil.OnLocationCompleteListener() {
                                @Override
                                public void onLocationFail(@NotNull String msg) {
                                    //定位失败
                                    BaseModel baseModel = new BaseModel("GPS位置信息获取失败", -1, msg);
                                    function.onCallBack(GsonUtils.toJson(baseModel));
                                }

                                @Override
                                public void onLocationSuccess(double latitude, double longitude) {
                                    //定位成功，拿到经纬度，去查询经纬度所在位置信息 反地理信息编码
                                    LocationUtil.getLocationInfo(longitude, latitude, new LocationUtil.OnLocationInfoGetListener() {
                                        @Override
                                        public void onLocationInfoGetSuccess(@NotNull LocationInfoResultModel model) {
                                            BaseModel baseModel = new BaseModel("位置信息获取成功", 0, model);
                                            function.onCallBack(GsonUtils.toJson(baseModel));
                                        }

                                        @Override
                                        public void onLocationInfoGetFail(@NotNull String msg) {
                                            BaseModel baseModel = new BaseModel("位置信息获取失败", -1, msg);
                                            function.onCallBack(GsonUtils.toJson(baseModel));
                                        }
                                    });
                                }
                            });
                        } else {
                            BaseModel baseModel = new BaseModel("位置信息获取失败，请授权GPS定位权限", -1, "位置信息获取失败，请授权GPS定位权限");
                            function.onCallBack(GsonUtils.toJson(baseModel));
                        }
                    }
                });


    }


}
