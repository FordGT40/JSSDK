package com.wisdom.jsbridjetest;

import com.uuzuche.lib_zxing.ZApplication;
import com.wisdom.jsinterfacelib.InitJsSDKUtil;

/**
 * @author
 * @ProjectName project：
 * @class package：
 * @class describe：
 * @time
 * @change
 */
public class MyApplication extends ZApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        InitJsSDKUtil.init(this);
    }
}
