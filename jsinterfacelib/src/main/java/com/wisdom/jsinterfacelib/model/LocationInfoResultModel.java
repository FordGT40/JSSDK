package com.wisdom.jsinterfacelib.model;

/**
 * @author HanXueFeng
 * @ProjectName project： jssdk_basic
 * @class package：com.wisdom.jsinterfacelib.model
 * @class describe：
 * @time 2021/7/17 0017 10:10
 * @change
 */
public class LocationInfoResultModel {
 private  Double	longitude;//	|否	|经度
    private Double	latitude;//	|否	|纬度
    private String	country;//	|否	|国家
    private String	province;//	|否	|省
    private String	city;//	|否	|市
    private String	district;//	|否	|区
    private String	address;//	|否	|详细地址
    private String	cityCode;//	|否	|城市编码
    private String	adcode;//	|否	|区域编码

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }
}
