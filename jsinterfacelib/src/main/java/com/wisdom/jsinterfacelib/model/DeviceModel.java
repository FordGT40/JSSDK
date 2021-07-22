package com.wisdom.jsinterfacelib.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： JsbridjeTest
 * @class package：com.wisdom.jsinterfacelib.model
 * @class describe：
 * @time 2021/7/17 0017 11:13
 * @change
 */
public class DeviceModel implements Serializable {
   private String	deviceName;//	|否	|设备名称，如：iPhone X
    private int	sysType;//	|否	|系统，如：0:iOS，1:Android，2-N:Android-华为...
    private String	sysVersion;//	|否	|系统版本，如：11.0
    private String	appName;//	|否	|App 名称，
    private String	appVersion;//	|否	|App 版本号，如：1.0.0
    private String	appID;//	|否	|包名，如：com.xxx.xxx
    private String	IDFV;//	|否	|供应商 ID，如：com.abc.0 和 com.abc.1 的 IDFV 相同，同一供应商的所有 App 删除后，这个值会变
    private String	IMEI;//	|否	|设备唯一标识，手机恢复出厂设置后，这个值会变
    private String	hybridVersion;//	|否	|Hybrid 框架版本，如：1.0.0
    private String	channelID;//	|否	|渠道号，如：iOS：App Store, Android：待定

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getSysType() {
        return sysType;
    }

    public void setSysType(int sysType) {
        this.sysType = sysType;
    }

    public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getIDFV() {
        return IDFV;
    }

    public void setIDFV(String IDFV) {
        this.IDFV = IDFV;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getHybridVersion() {
        return hybridVersion;
    }

    public void setHybridVersion(String hybridVersion) {
        this.hybridVersion = hybridVersion;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }
}
