package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.utils.GlideEngine;
import com.wisdom.jsinterfacelib.utils.ImageUtil;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.model.VideoBackModel;
import com.wisdom.jsinterfacelib.utils.UriUtil;
import com.wisdom.jsinterfacelib.utils.avoidonresult.AvoidOnResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

/**
 * 选择视频
 */
public class ChooseVideoHandler extends BridgeHandler {


    @Override
    public void handler(final Context context, final String data, final CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
//        {"maxDuration":0,"sourceType":["album","camera"],"camera":"front"}
        final BaseModel[] baseModel = new BaseModel[1];
        final Boolean[] hasCamaro = {false};//选择图片是否带有拍照功能
        PermissionX.init(((AppCompatActivity) context))
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            try {
                                JSONObject json = new JSONObject(data);
                                JSONArray sourceType = json.optJSONArray("sourceType");
                                int maxDuration = json.optInt("maxDuration");
                                String camera = json.optString("camera");
                                for (int j = 0; j < sourceType.length(); j++) {
                                    if (sourceType.getString(j).equals("camera")) {
                                        hasCamaro[0] = true;
                                    }
                                }
                                showVideoSelect(context, hasCamaro[0], 1, baseModel, maxDuration * 1000, function, camera);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                baseModel[0] = new BaseModel("获取失败", -1, "js入参格式解析失败，请参考文档传参");
                                function.onCallBack(GsonUtils.toJson(baseModel[0]));
                            }
                        } else {
                            baseModel[0] = new BaseModel("获取失败", -1, "用户拒接授权");
                            function.onCallBack(GsonUtils.toJson(baseModel[0]));
                        }
                    }

                });

    }


    /**
     * 打开相册或者打开摄像头进行拍摄操作
     * @param context
     * @param hasCamaro 是否打开摄像头拍摄
     * @param count 每次选择视频的数量
     * @param baseModel 返回页面的数据结构
     * @param maxDuration 最大支持时长（筛选）
     * @param function 回调方法
     * @param camera 前置还是后置摄像头：默认拉起的是前置('front')或者后置摄像头('back')
     */
    private void showVideoSelect(final Context context, Boolean hasCamaro, int count, final BaseModel[] baseModel, final int maxDuration, final CallBackFunction function, final String camera) {
        if (maxDuration == 0) {
            baseModel[0] = new BaseModel("获取失败", -1, "未设置视频指定长度");
            function.onCallBack(GsonUtils.toJson(baseModel[0]));
            return;
        }
        if (hasCamaro) {
            //用摄像头拍摄
            PermissionX.init(((AppCompatActivity) context))
                    .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request(new RequestCallback() {
                        @Override
                        public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                            if (allGranted) {
                                //权限通过，打开摄像头录像
                                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                if (!"back".equals(camera)) {
                                    // 调用前置摄像头
                                    intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                                    intent.putExtra("android.intent.extras.USE_FRONT_CAMERA", true);
                                } else {
                                    // 调用后置摄像头
                                    intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                                    intent.putExtra("android.intent.extras.USE_FRONT_CAMERA", false);
                                }
                                intent.putExtra("autofocus", true); // 自动对焦
                                intent.putExtra("fullScreen", false); // 全屏
                                intent.putExtra("showActionIcons", false);
                                //拍摄视频完成后走的回调数据
                                new AvoidOnResult(((AppCompatActivity) context)).startForResult(intent, new AvoidOnResult.Callback() {
                                    @Override
                                    public void onActivityResult(int resultCode, Intent data) {
                                        if (resultCode == Activity.RESULT_OK) {
                                            //拿到视频保存地址
                                            if (data.getData() != null) {
                                                VideoBackModel model = getVideoInfo(context, data.getData());
                                                if (model != null) {
                                                    BaseModel baseModel = new BaseModel("视频录制成功", 0, model);
                                                    function.onCallBack(GsonUtils.toJson(baseModel));
                                                } else {
                                                    BaseModel baseModel = new BaseModel("视频录制失败", -1, "视频数据转换异常");
                                                    function.onCallBack(GsonUtils.toJson(baseModel));
                                                }
                                            }
                                        } else {
                                            BaseModel baseModel = new BaseModel("用户取消录制", -1, "用户取消录制");
                                            function.onCallBack(GsonUtils.toJson(baseModel));
                                        }
                                    }
                                });
                            } else {
                                //权限拒绝
                                BaseModel baseModel = new BaseModel("获取失败", -1, "用户拒接授权");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            }
                        }
                    });

        } else {
            //不用摄像头拍摄
            PictureSelector.create((AppCompatActivity) context)
                    .openGallery(PictureMimeType.ofVideo())
                    .isCamera(false)
                    .videoMaxSecond(maxDuration / 1000)
                    .maxSelectNum(count)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(List<LocalMedia> result) {
                            if (result.size() > 0) {

                                if (result.get(0).getDuration() > maxDuration) {
                                    baseModel[0] = new BaseModel("获取失败", -1, "获取失败，视频超过指定长度");
                                } else {
                                    VideoBackModel videoBackModel = new VideoBackModel();
                                    videoBackModel.setDuration(result.get(0).getDuration());
                                    videoBackModel.setHeight(result.get(0).getHeight());
                                    videoBackModel.setWidth(result.get(0).getWidth());
                                    videoBackModel.setSize(result.get(0).getSize());
                                    videoBackModel.setTempFilePath(result.get(0).getRealPath());
                                    videoBackModel.setVideoData(ImageUtil.file2Base64(result.get(0).getRealPath()));
                                    baseModel[0] = new BaseModel("获取成功", 0, videoBackModel);
                                }
                            } else {
                                baseModel[0] = new BaseModel("获取失败", -1, "获取失败，返回的视频数组为空");
                            }
                            function.onCallBack(GsonUtils.toJson(baseModel[0]));
                        }

                        @Override
                        public void onCancel() {
                            baseModel[0] = new BaseModel("获取失败", -1, "用户未选择视频，关闭选择视频页面");
                            function.onCallBack(GsonUtils.toJson(baseModel[0]));
                        }
                    });
        }
    }

    /**
     * 获取录制视频文件的相关信息属性等
     *
     * @param context
     * @param mUri
     * @return
     */
    private VideoBackModel getVideoInfo(Context context, Uri mUri) {
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        try {
            mmr.setDataSource(context, mUri);
            String duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//时长(毫秒)
            String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//宽
            String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//高
//            LogUtils.i("duration：" + duration);
            InputStream input = context.getContentResolver().openInputStream(mUri);
            int size = input.available();
            String path = UriUtil.getVideoRealPathFromURI(context, mUri);
            VideoBackModel videoBackModel = new VideoBackModel();
            videoBackModel.setDuration(Long.parseLong(duration));
            videoBackModel.setHeight(Integer.parseInt(height));
            videoBackModel.setWidth(Integer.parseInt(width));
            videoBackModel.setSize(size);
            videoBackModel.setTempFilePath(path);
            //TODO 视频如果过长，转Base64会出问题暂时注释掉
//            videoBackModel.setVideoData(Base64Util.fileToBase64(new File(path)));

            return videoBackModel;
        } catch (Exception ex) {
            LogUtils.i("MediaMetadataRetriever exception " + ex);
        } finally {
            mmr.release();
        }
        return null;
    }
}
