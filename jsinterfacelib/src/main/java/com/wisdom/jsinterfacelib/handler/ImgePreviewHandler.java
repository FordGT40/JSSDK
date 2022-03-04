package com.wisdom.jsinterfacelib.handler;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
//import com.jelly.mango.Mango;
//import com.jelly.mango.MultiplexImage;
import com.draggable.library.extension.ImageViewerHelper;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.activity.ImagePreviewActivityJs;
import com.wisdom.jsinterfacelib.model.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *### 预览图片
 */
public class ImgePreviewHandler extends BridgeHandler {
    @Override
    public void handler(Context context, String data, CallBackFunction function) {
        LogUtils.i("接到的json：" + data);
//        Mango.setImages();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.optJSONArray("urls");
            int index = jsonObject.optInt("index");
            boolean download = jsonObject.optBoolean("download",false);
            boolean rotate = jsonObject.optBoolean("rotate",false);
            if (jsonArray.length() > 0) {
//                整理图片数据
                ArrayList<String> imageList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    imageList.add(jsonArray.optString(i));
                }
                /**
//                预览图片开始
                ImageViewerHelper.INSTANCE.showImages(context,imageList,index,true);
               **/
                ImagePreviewActivityJs.goImagePreview(context,download,rotate,index,imageList);
                BaseModel baseModel = new BaseModel("预览图片成功", 0, "预览图片成功");
                function.onCallBack(GsonUtils.toJson(baseModel));
            } else {
                BaseModel baseModel = new BaseModel("未获得任何图片url", -1, "传入图片url为空");
                function.onCallBack(GsonUtils.toJson(baseModel));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w("图片解析预览异常："+e.getMessage());
            BaseModel baseModel = new BaseModel("参数解析错误", -1, e.getMessage());
            function.onCallBack(GsonUtils.toJson(baseModel));
        }
    }


}
