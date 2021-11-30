package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.UriUtils;
import com.huantansheng.easyphotos.ui.dialog.LoadingDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.utils.Base64Util;
import com.wisdom.jsinterfacelib.utils.GlideEngine;
import com.wisdom.jsinterfacelib.utils.ImageUtil;
import com.wisdom.jsinterfacelib.utils.UriUtil;
import com.wisdom.jsinterfacelib.utils.avoidonresult.AvoidOnResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * 选择图片
 */
public class ChooseImageHandler extends BridgeHandler {

    @Override
    public void handler(final Context context, final String data, final CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        final int[] count = {0};
        final BaseModel[] baseModel = new BaseModel[1];
        final Boolean[] hasCamaro = {false};
        final Boolean[] hasAlbum = {false};
        final Boolean[] isCompressed = {false};//是否压缩视频
        PermissionX.init(((AppCompatActivity) context))
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            try {
                                JSONObject json = new JSONObject(data);
                                count[0] = json.optInt("count");
                                JSONArray sizeType = json.optJSONArray("sizeType");
                                JSONArray sourceType = json.optJSONArray("sourceType");
                                if (count[0] == 0) {
                                    //如果传入的count=0或者count没给传的话，赋值为1
                                    count[0] = 1;
                                }
                                for (int j = 0; j < sourceType.length(); j++) {
                                    if (sourceType.getString(j).equals("camera")) {
                                        hasCamaro[0] = true;
                                    }
                                }
                                for (int j = 0; j < sourceType.length(); j++) {
                                    if (sourceType.getString(j).equals("album")) {
                                        hasAlbum[0] = true;
                                    }
                                }
                                for (int k = 0; k < sizeType.length(); k++) {
                                    if (sizeType.getString(k).equals("compressed")) {
                                        isCompressed[0] = true;
                                    }
                                }
                                final Boolean finalIsCompressed = isCompressed[0];
                                if (hasCamaro.length > 0) {

                                    if(hasCamaro[0]&&hasAlbum[0]){
                                        //相册和相机都有
                                        showImageSelect(context, true, count[0], finalIsCompressed, baseModel, function);
                                    }else{
                                        //只有相册或者相机
                                        if (hasCamaro[0]) {
                                            //打开相机拍照
                                            openCamare(context, finalIsCompressed, function);
                                        }
                                        else {
                                            //相册选择
                                            showImageSelect(context, hasCamaro[0], count[0], finalIsCompressed, baseModel, function);
                                        }
                                    }

                                } else {
                                    //相册选择
                                    showImageSelect(context, hasCamaro[0], count[0], finalIsCompressed, baseModel, function);
                                }
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


    private void showImageSelect(final Context context, Boolean hasCamaro, int count, final Boolean finalIsCompressed, final BaseModel[] baseModel, final CallBackFunction function) {
        PictureSelector.create((AppCompatActivity) context)
                .openGallery(PictureMimeType.ofImage())
                .isCamera(hasCamaro)
                .maxSelectNum(count)
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        if (result.size() > 0) {
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < result.size(); i++) {
                                //是否压缩
                                if (finalIsCompressed) {
                                    list.add("data:image/png;base64," + ImageUtil.ImageToBase64Compress(context, result.get(i).getRealPath()));
                                } else {
                                    list.add("data:image/png;base64," + ImageUtil.ImageToBase64(result.get(i).getRealPath()));
                                }
                            }
                            baseModel[0] = new BaseModel("获取成功", 0, list);
                        } else {
                            baseModel[0] = new BaseModel("获取失败", -1, "获取失败，返回的图片数组为空");
                        }
                        function.onCallBack(GsonUtils.toJson(baseModel[0]));
                    }

                    @Override
                    public void onCancel() {
                        baseModel[0] = new BaseModel("获取失败", -1, "用户未选择图片，关闭选择图片页面");
                        function.onCallBack(GsonUtils.toJson(baseModel[0]));
                    }
                });
    }

    /**
     * 打开相机，拍照
     *
     * @param context
     * @param function
     */
    private void openCamare(final Context context, final Boolean isCompressed, final CallBackFunction function) {
        PermissionX.init(((AppCompatActivity) context))
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        //获得权限，打开相机
                        try {
                            File outputImage = new File(context.getExternalCacheDir(), "output_image.jpg");
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                            Uri imageUri = null;
                            if (Build.VERSION.SDK_INT >= 24) {
                                imageUri = FileProvider.getUriForFile(context,
                                        "com.wisdom.jsinterfacelib.fileprovider", outputImage);
                            } else {
                                imageUri = Uri.fromFile(outputImage);
                            }
                            // 启动相机
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);


                            final Uri finalImageUri = imageUri;
                            new AvoidOnResult(((AppCompatActivity) context))
                                    .startForResult(intent, (resultCode, data) -> {
                                        if (resultCode == RESULT_OK) {
                                            try {
                                                List<String> list = new ArrayList<>();
                                                //根据图片uri获得图片的绝对路径
                                                String filePath=UriUtil.getRealFilePathForPhoto(context,finalImageUri);
                                                //根据图片uri获得图片的bitmap
                                                Bitmap bitmap=ImageUtil.getBitmapFromUri(context,finalImageUri);
                                                //根据图片的绝对路径获得图片应该旋转的角度
                                                int degree=ImageUtil.getBitmapDegree(filePath);
                                                //对图片进行相应角度的旋转操作
                                                Bitmap roteBitmap=ImageUtil.rotateBitmap(bitmap,degree);
                                                //将旋转后的bitMap保存本地，并返回保存路径
                                                String filePathLocal= ImageUtil.saveMyBitmapLocal(context,roteBitmap,System.currentTimeMillis()+".jpg",0);
                                                if (isCompressed) {
                                                    //压缩图片
                                                    list.add("data:image/png;base64," + ImageUtil.ImageToBase64Compress(context,filePathLocal));
                                                } else {
                                                    //不压缩图片
                                                    list.add("data:image/png;base64," + ImageUtil.ImageToBase64(filePathLocal));
                                                }
                                                BaseModel baseModel = new BaseModel("拍照成功", 0, list);
                                                function.onCallBack(GsonUtils.toJson(baseModel));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                BaseModel baseModel = new BaseModel("拍照失败", -1, "系统错误");
                                                function.onCallBack(GsonUtils.toJson(baseModel));
                                            }
                                        } else if (resultCode == RESULT_CANCELED) {
                                            //用户取消了操作
                                            BaseModel baseModel = new BaseModel("取消拍照", -1, "取消拍照");
                                            function.onCallBack(GsonUtils.toJson(baseModel));
                                        } else {
                                            //其他返回值，未知错误（可能是系统不兼容等）
                                            BaseModel baseModel = new BaseModel("打开相机失败", -1, "未知错误（可能是系统不兼容等）");
                                            function.onCallBack(GsonUtils.toJson(baseModel));
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                            BaseModel baseModel = new BaseModel("获取相机失败", -1, "系统错误");
                            function.onCallBack(GsonUtils.toJson(baseModel));
                        }
                    } else {
                        BaseModel baseModel = new BaseModel("获取权限失败", -1, "用户拒接授权");
                        function.onCallBack(GsonUtils.toJson(baseModel));
                    }
                });
    }


}
