package com.wisdom.jsinterfacelib;

import android.app.Application;

import com.smallbuer.jsbridge.core.Bridge;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.wisdom.jsinterfacelib.handler.AppVersionHandler;
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
import com.wisdom.jsinterfacelib.handler.WebViewCloseHandler;
import com.wisdom.jsinterfacelib.handler.WebViewGoBackHandler;
import com.wisdom.jsinterfacelib.utils.JsInterfaceBridge;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HanXueFeng
 * @ProjectName project： JsbridjeTest
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
    public static void init(Application application) {
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
        JsInterfaceBridge.init(application, bridgeHandlerMap);
    }
}
