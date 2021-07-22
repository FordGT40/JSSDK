package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.content.Context;

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
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.utils.GlideEngine;
import com.wisdom.jsinterfacelib.utils.ImageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                                for (int k = 0; k < sizeType.length(); k++) {
                                    if (sizeType.getString(k).equals("compressed")) {
                                        isCompressed[0] = true;
                                    }
                                }
                                final Boolean finalIsCompressed = isCompressed[0];
                                showImageSelect(context, hasCamaro[0], count[0], finalIsCompressed, baseModel, function);
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


    private void showImageSelect(Context context, Boolean hasCamaro, int count, final Boolean finalIsCompressed, final BaseModel[] baseModel, final CallBackFunction function) {
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
                                    list.add("data:image/png;base64," + ImageUtil.ImageToBase64Compress(result.get(i).getRealPath()));
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
}
