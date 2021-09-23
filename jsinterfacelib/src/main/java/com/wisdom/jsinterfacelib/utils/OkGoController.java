package com.wisdom.jsinterfacelib.utils;

import com.blankj.utilcode.util.SPUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import java.io.File;
import java.util.Map;

/**
 *
 */
public class OkGoController {

    private OkGoController() {
    }

    private static OkGoController instance;

    public static OkGoController create() {

        if (instance == null) {
            instance = new OkGoController();
        }
        return instance;
    }

    public  void get(String url, Map<String, String> map, Callback<String> callback) {
        OkGo.<String>get(url)
                .headers("Content-Type", "multipart/form-data")
                .params(map)
                .execute(callback);
    }
    public void get(String url, Map<String, String> map, StringCallback stringCallBack) {
        OkGo.<String>get(url)
                .headers("Content-Type", "multipart/form-data")
                .params(map)
                .execute(stringCallBack);
    }
    public  void postFile(String url, HttpParams map, StringCallback stringCallBack) {
        OkGo.<String>post(url)
                .headers("Content-Type", "multipart/form-data")
                .isMultipart(true)
                .params(map)
                .execute(stringCallBack);
    }
}
