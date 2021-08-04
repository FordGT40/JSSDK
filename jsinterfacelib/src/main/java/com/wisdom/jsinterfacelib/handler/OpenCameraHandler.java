package com.wisdom.jsinterfacelib.handler;

import android.app.Activity;
import android.content.Context;

import com.huantansheng.easyphotos.EasyPhotos;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;

/**
 * @author $
 * @ProjectName project：
 * @class package：$
 * @class describe：$
 * @time $
 * @change
 */
public class OpenCameraHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        EasyPhotos.createCamera(((Activity) context),false)//参数说明：上下文,是否使用宽高数据（false时宽高数据为0，扫描速度更快）
                .setFileProviderAuthority("com.wisdom.jssdk_basic.fileprovider")//参数说明：见下方`FileProvider的配置`
                .start(0);

    }
}
