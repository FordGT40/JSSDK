package com.wisdom.jsinterfacelib.model;

/**
 * Created by Tsing on 2020/8/19
 */
public class BaseModel {
    public String message;
    public int code;
    public Object data;

    public BaseModel(String msg, int code, Object data) {
        this.message = msg;
        this.code = code;
        this.data = data;
    }
}
