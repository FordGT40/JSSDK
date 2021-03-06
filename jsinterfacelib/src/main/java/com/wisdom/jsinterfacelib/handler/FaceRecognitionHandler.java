package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.permissionx.guolindev.PermissionX;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.face.FaceDetectionActivity;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.model.FaceDetectionModel;
import com.wisdom.jsinterfacelib.utils.ImageUtil;
import com.wisdom.jsinterfacelib.utils.avoidonresult.AvoidOnResult;

import org.json.JSONObject;

import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static com.wisdom.jsinterfacelib.face.FaceDetectionActivity.RESULT_CODE_FACE_DETECTION;

/**
 * 阿里人脸识别功能
 */
public class FaceRecognitionHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
        try {
            //检测权限
            PermissionX.init(((AppCompatActivity) context))
                    .permissions(Manifest.permission.CAMERA)
                    .request((allGranted, grantedList, deniedList) -> {
                        if (allGranted) {
                            //解析数据
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                String title = jsonObject.optString("title");
                                int second = jsonObject.optInt("second");
                                int actionCount = jsonObject.optInt("actionCount");
                                //打开人脸识别界面并且识别人脸
                                Intent intent = new Intent(context, FaceDetectionActivity.class);
                                intent.putExtra("title", title);
                                intent.putExtra("second", second);
                                intent.putExtra("actionCount", actionCount);
                                new AvoidOnResult(((AppCompatActivity) context))
                                        .startForResult(intent, (resultCode, data1) -> {
                                            //人脸识别返回结果
                                            if (resultCode == RESULT_CANCELED) {
                                                //用户点击返回键取消
                                                BaseModel baseModel = new BaseModel("用户主动关闭人脸识别", 70001, "用户主动关闭人脸识别");
                                                function.onCallBack(GsonUtils.toJson(baseModel));
                                            } else if (resultCode == RESULT_CODE_FACE_DETECTION) {
                                                //识别人脸完成（超时和识别成功两种情况）
                                                int code = data1.getIntExtra("code", -1);
                                                String msg = data1.getStringExtra("msg");
                                                if (code == 0) {
                                                    String filePath = data1.getStringExtra("filePath");
                                                    //人脸识别成功
                                                    try {
                                                        //根据图片的绝对路径获得图片应该旋转的角度
                                                        int degree = 0;
                                                        if (android.hardware.Camera.getNumberOfCameras() > 1) {
                                                            //如果带前后摄像头的，那么计算图片旋转角度，否则只有一个摄像头固定顺时针旋转90度
                                                            degree = ImageUtil.getBitmapDegree(filePath);
                                                        }
                                                        //对图片进行相应角度的旋转操作
                                                        Bitmap roteBitmap = ImageUtil.rotateBitmap(ImageUtils.getBitmap(filePath), degree);

                                                        //将旋转后的bitMap保存本地，并返回保存路径
                                                        String filePathLocal = ImageUtil.saveMyBitmapLocal(context, roteBitmap, System.currentTimeMillis() + ".jpg",2);

                                                        FaceDetectionModel faceDetectionModel = new FaceDetectionModel("data:image/png;base64," + ImageUtil.ImageToBase64Compress(context, filePathLocal));
                                                        BaseModel baseModel = new BaseModel(msg, 0, faceDetectionModel);
                                                        function.onCallBack(GsonUtils.toJson(baseModel));
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                        //人脸识别失败
                                                        BaseModel baseModel = new BaseModel("人脸识别失败", 70002, e.getMessage());
                                                        function.onCallBack(GsonUtils.toJson(baseModel));
                                                    }
                                                } else {
                                                    //人脸识别失败
                                                    BaseModel baseModel = new BaseModel(msg, 70002, "人脸识别失败");
                                                    function.onCallBack(GsonUtils.toJson(baseModel));
                                                }

                                            }

                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                                BaseModel baseModel = new BaseModel("调用人脸检测失败", -1, e.getMessage());
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            }
                        } else {
                            BaseModel baseModel = new BaseModel("调用人脸检测失败", -1, "用户未授权使用相机");
                            function.onCallBack(GsonUtils.toJson(baseModel));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            BaseModel baseModel = new BaseModel("调用人脸检测失败", -1, e.getMessage());
            function.onCallBack(GsonUtils.toJson(baseModel));
        }


    }


}
