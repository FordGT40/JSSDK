package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.permissionx.guolindev.PermissionX;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.activity.VideoPreviewActivity;
import com.wisdom.jsinterfacelib.activity.VideoRecorderFrontActivity;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.model.VideoBackModel;
import com.wisdom.jsinterfacelib.utils.Base64Util;
import com.wisdom.jsinterfacelib.utils.avoidonresult.AvoidOnResult;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

import static android.app.Activity.RESULT_CANCELED;
import static com.wisdom.jsinterfacelib.activity.VideoRecorderFrontActivity.RESULT_CODE_ERROR;

/**
 * @author HanXueFeng
 * @ProjectName project： JSSDK_Basic
 * @class package：com.wisdom.jsinterfacelib.handler
 * @class describe：前置摄像头录制视频并且带可选文字跑马灯
 * @time 2021/8/9 0009 09:03
 * @change
 */
public class CameraCustomHandler extends BridgeHandler {

    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        PermissionX.init(((AppCompatActivity) context))
                .permissions(Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.RECORD_AUDIO)
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        //权限通过
                        //解析json
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            String notice = jsonObject.optString("notice");
                            String title = jsonObject.optString("title");
                            String textColor = jsonObject.optString("textColor");
                            String textBackColor = jsonObject.optString("textBackColor");
                            int font = jsonObject.optInt("font");
                            int duration = jsonObject.optInt("duration", 30);

                            getVideoData(context, duration, notice,title,textColor,font,textBackColor, function);
                        } catch (Exception e) {
                            e.printStackTrace();
                            BaseModel baseModel = new BaseModel("数据解析异常", -1, "数据解析异常");
                            function.onCallBack(GsonUtils.toJson(baseModel));
                        }
                    } else {
                        //未授权
                        BaseModel baseModel = new BaseModel("相机权限获取失败", -1, "用户未授权相机或存储");
                        function.onCallBack(GsonUtils.toJson(baseModel));
                    }
                });

    }


    /**
     * 打开前置摄像头录制视频的方法
     *  @param context
     * @param duration
     * @param notice
     * @param title
     * @param textColor
     * @param function
     */
    private void getVideoData(Context context, int duration, String notice, String title, String textColor,int fontSize,String textBackColor, CallBackFunction function) {

        Intent intentNext = new Intent(context, VideoRecorderFrontActivity.class);
        intentNext.putExtra("duration", duration);
        intentNext.putExtra("notice", notice);
        intentNext.putExtra("title", title);
        intentNext.putExtra("textColor", textColor);
        intentNext.putExtra("fontSize", fontSize);
        intentNext.putExtra("textBackColor", textBackColor);
        new AvoidOnResult(((AppCompatActivity) context)).startForResult(intentNext, (resultCode, data1) -> {
            //判断返回码
            BaseModel baseModel;
            if (resultCode == RESULT_CANCELED) {
                //用户主动取消
                baseModel = new BaseModel("用户取消了拍摄", -1, "用户取消了拍摄");
                function.onCallBack(GsonUtils.toJson(baseModel));
            } else if (resultCode == VideoPreviewActivity.RESULT_CODE_OK) {
                //用户正常返回，拿到了返回的视频信息
                //视频的地址是固定地址
                String filePath = context.getExternalCacheDir().getPath() + "/recorder.mp4";
                android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
                try {
                    Uri mUri = Uri.parse(filePath);
                    mmr.setDataSource(context, mUri);
                    String durations = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//时长(毫秒)
                    String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//宽
                    String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//高
                    long size = new File(filePath).length();
                    VideoBackModel videoBackModel = new VideoBackModel();
                    videoBackModel.setDuration(Long.parseLong(durations));
                    videoBackModel.setHeight(Integer.parseInt(height));
                    videoBackModel.setWidth(Integer.parseInt(width));
                    videoBackModel.setSize(size);
                    videoBackModel.setTempFilePath(filePath);
                    videoBackModel.setVideoData(Base64Util.videoToBase64(new File(filePath)));
                    baseModel = new BaseModel("拍摄成功", 0, videoBackModel);
                    function.onCallBack(GsonUtils.toJson(baseModel));
                } catch (Exception e) {
                    e.printStackTrace();
                    baseModel = new BaseModel("视频数据解析错误", -1, "视频数据解析错误，视频参数解析错误");
                    function.onCallBack(GsonUtils.toJson(baseModel));
                }
            }else if(resultCode==RESULT_CODE_ERROR){
                String error_msg=data1.getStringExtra("msg");
//                baseModel = new BaseModel("拍摄出错，请重试", -1, error_msg);
                baseModel = new BaseModel(error_msg, -1, error_msg);
                function.onCallBack(GsonUtils.toJson(baseModel));
            }
            else {
                //其他情况
                baseModel = new BaseModel("拍摄失败，系统错误", -1, "拍摄失败，系统错误，返回的code未知");
                function.onCallBack(GsonUtils.toJson(baseModel));
            }

        });
    }


}
