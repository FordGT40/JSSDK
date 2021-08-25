package com.wisdom.jsinterfacelib;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.smallbuer.jsbridge.core.Bridge;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.wisdom.jsinterfacelib.handler.AppVersionHandler;
import com.wisdom.jsinterfacelib.handler.CameraCustomHandler;
import com.wisdom.jsinterfacelib.handler.ChooseImageHandler;
import com.wisdom.jsinterfacelib.handler.ChooseVideoHandler;
import com.wisdom.jsinterfacelib.handler.DeviceInfoHandler;
import com.wisdom.jsinterfacelib.handler.ExistApiHandler;
import com.wisdom.jsinterfacelib.handler.ExistApisHandler;
import com.wisdom.jsinterfacelib.handler.FaceRecognitionHandler;
import com.wisdom.jsinterfacelib.handler.FingerPrintCompareHandler;
import com.wisdom.jsinterfacelib.handler.GetTokenHandler;
import com.wisdom.jsinterfacelib.handler.ImgePreviewHandler;
import com.wisdom.jsinterfacelib.handler.LocationHandler;
import com.wisdom.jsinterfacelib.handler.NavigationBarTitleHandler;
import com.wisdom.jsinterfacelib.handler.NetworkStatusHandler;
import com.wisdom.jsinterfacelib.handler.OpenContactHandler;
import com.wisdom.jsinterfacelib.handler.OpenNewWebViewHandler;
import com.wisdom.jsinterfacelib.handler.PhoneCallHandler;
import com.wisdom.jsinterfacelib.handler.PlayVideoHandler;
import com.wisdom.jsinterfacelib.handler.SetTokenHandler;
import com.wisdom.jsinterfacelib.handler.SignPadHandler;
import com.wisdom.jsinterfacelib.handler.ToastHandler;
import com.wisdom.jsinterfacelib.handler.ToggleNavigationBarHandler;
import com.wisdom.jsinterfacelib.handler.UpLoadFileHandler;
import com.wisdom.jsinterfacelib.handler.WebViewCloseHandler;
import com.wisdom.jsinterfacelib.handler.WebViewGoBackHandler;
import com.wisdom.jsinterfacelib.utils.JsInterfaceBridge;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * @author HanXueFeng
 * @ProjectName project： jssdk_basic
 * @class package：com.wisdom.jsinterfacelib.utils
 * @class describe：
 * @time 2021/7/17 0017 12:31
 * @change
 */
public class InitJsSDKUtil {

    /**
     * 初始化jssdk方法，在主项目的Application中进行调用
     *
     * @param application
     */
    public static void init(Application application,Boolean initOkGoOrNot) {
        Bridge.INSTANCE.openLog();
        Map<String, BridgeHandler> bridgeHandlerMap = new HashMap();
        bridgeHandlerMap.put("WISDOM.app.toast", new ToastHandler());
        bridgeHandlerMap.put("WISDOM.app.chooseImage", new ChooseImageHandler());
        bridgeHandlerMap.put("WISDOM.app.isExistApi", new ExistApiHandler());
        bridgeHandlerMap.put("WISDOM.app.isExistApis", new ExistApisHandler());
        bridgeHandlerMap.put("WISDOM.app.toggleNavigationBar", new ToggleNavigationBarHandler());
        bridgeHandlerMap.put("WISDOM.app.setNavigationBarTitle", new NavigationBarTitleHandler());
        bridgeHandlerMap.put("WISDOM.app.chooseVideo", new ChooseVideoHandler());
        bridgeHandlerMap.put("WISDOM.app.phoneCall", new PhoneCallHandler());
        bridgeHandlerMap.put("WISDOM.app.getAppVersion", new AppVersionHandler());
        bridgeHandlerMap.put("WISDOM.app.previewImages", new ImgePreviewHandler());
        bridgeHandlerMap.put("WISDOM.app.playVideo", new PlayVideoHandler());
        bridgeHandlerMap.put("WISDOM.app.authID", new FingerPrintCompareHandler());
        bridgeHandlerMap.put("WISDOM.app.signature", new SignPadHandler());
        bridgeHandlerMap.put("WISDOM.app.contact", new OpenContactHandler());
        bridgeHandlerMap.put("WISDOM.app.getGpsInfo", new LocationHandler());
        bridgeHandlerMap.put("WISDOM.app.getDeviceInfo", new DeviceInfoHandler());
        bridgeHandlerMap.put("WISDOM.app.networkStatus", new NetworkStatusHandler());
        bridgeHandlerMap.put("WISDOM.app.goback", new WebViewGoBackHandler());
        bridgeHandlerMap.put("WISDOM.app.openNewWebView", new OpenNewWebViewHandler());
        bridgeHandlerMap.put("WISDOM.app.close", new WebViewCloseHandler());
        bridgeHandlerMap.put("WISDOM.app.setToken", new SetTokenHandler());
        bridgeHandlerMap.put("WISDOM.app.getToken", new GetTokenHandler());
        bridgeHandlerMap.put("WISDOM.app.mnnFaceVerification", new FaceRecognitionHandler());
        bridgeHandlerMap.put("WISDOM.app.recordVideo", new CameraCustomHandler());
        bridgeHandlerMap.put("WISDOM.app.uploadFiles", new UpLoadFileHandler());
        JsInterfaceBridge.init(application, bridgeHandlerMap);
        //初始化okgo
        if (initOkGoOrNot) {
            initOkGo(application);
        }
    }
    private static void initOkGo(Application application) {
        OkGo.getInstance().init(application);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        if (Common.isDebug) {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
////log打印级别，决定了log显示的详细程度
//            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
////log颜色级别，决定了log在控制台显示的颜色
//            loggingInterceptor.setColorLevel(Level.INFO);
//            builder.addInterceptor(loggingInterceptor);
//        }
//全局的读取超时时间
        builder.readTimeout(15000, TimeUnit.MILLISECONDS);
//全局的写入超时时间
        builder.writeTimeout(15000, TimeUnit.MILLISECONDS);
//全局的连接超时时间
        builder.connectTimeout(15000, TimeUnit.MILLISECONDS);
        OkGo.getInstance().init(application)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(2);                          //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params);                       //全局公共参数
    }

}
