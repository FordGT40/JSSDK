package com.wisdom.jsinterfacelib.model;

import java.io.Serializable;

/**
 * @author HanXueFeng
 * @ProjectName project： jssdk_basic
 * @class package：com.wisdom.jsinterfacelib.model
 * @class describe：
 * @time 2021/7/9 0009 14:59
 * @change
 */
public class VideoBackModel implements Serializable {
    private String	tempFilePath;//	|是	|选定视频的临时文件路径|
    private       long	duration;//	|是	|选定视频的时间长度|
    private       long	size;//	|是	|选定视频的数据量大小，单位：B|
    private      int	height;//	|是	|返回选定视频的高度|
    private      int	width;//	|是	|返回选定视频的宽度|
    private     String	videoData;//	|是	|选定视频的数据，base64或blob，默认base64|

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getVideoData() {
        return videoData;
    }

    public void setVideoData(String videoData) {
        this.videoData = videoData;
    }
}
