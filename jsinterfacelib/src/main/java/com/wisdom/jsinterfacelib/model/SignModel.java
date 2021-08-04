package com.wisdom.jsinterfacelib.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： jssdk_basic
 * @class package：com.wisdom.jsinterfacelib.model
 * @class describe：
 * @time 2021/7/15 0015 09:15
 * @change
 */
public class SignModel implements Serializable {

    private String  signData;

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }
}
