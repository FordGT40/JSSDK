package com.wisdom.jsinterfacelib.utils;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.smallbuer.jsbridge.core.Bridge;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.BridgeJavascritInterface;

import java.util.Map;

/**
 * Created by Tsing on 2020/8/24
 */
public class  JsInterfaceBridge {

    public static Map<String, BridgeHandler> map;
    public static void init(Context context, Map<String, BridgeHandler> bridgeHandlerMap){
        Utils.init((Application) context);
        map = bridgeHandlerMap;
        Bridge.INSTANCE.registerHandler(bridgeHandlerMap);
    }

}
