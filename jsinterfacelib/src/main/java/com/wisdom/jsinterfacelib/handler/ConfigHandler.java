package com.wisdom.jsinterfacelib.handler;

import android.content.Context;
import android.widget.Toast;

import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;

/**
 * Created by Tsing on 2020/8/19
 */
public class ConfigHandler extends BridgeHandler {

    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        Toast.makeText(context, "data:" + data, Toast.LENGTH_SHORT).show();
    }
}