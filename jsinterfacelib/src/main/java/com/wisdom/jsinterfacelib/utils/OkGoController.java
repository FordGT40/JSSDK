package com.wisdom.jsinterfacelib.utils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;

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

}
