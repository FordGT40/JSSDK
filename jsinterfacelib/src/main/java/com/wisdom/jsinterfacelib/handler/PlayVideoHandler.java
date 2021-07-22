package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.activity.VideoPlayActivity;
import com.wisdom.jsinterfacelib.model.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import xyz.doikki.videoplayer.player.AndroidMediaPlayerFactory;
import xyz.doikki.videoplayer.player.VideoView;
import xyz.doikki.videoplayer.player.VideoViewConfig;
import xyz.doikki.videoplayer.player.VideoViewManager;

/**
 * ### 播放视频
 */
public class PlayVideoHandler extends BridgeHandler {
    @Override
    public void handler(final Context context, final String data, final CallBackFunction function) {
        LogUtils.i("接到的json：" + data);

        //权限监测
        PermissionX.init(((AppCompatActivity) context))
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if(allGranted){
                            try {  VideoViewManager.setConfig(VideoViewConfig.newBuilder()
//                //使用使用IjkPlayer解码
//                .setPlayerFactory(IjkPlayerFactory.create())
//                //使用ExoPlayer解码
//                .setPlayerFactory(ExoMediaPlayerFactory.create())
                                    //使用MediaPlayer解码
                                    .setPlayerFactory(AndroidMediaPlayerFactory.create())
                                    .build());

                                JSONObject jsonObject = new JSONObject(data);
                                String virtualSrc = jsonObject.optString("virtualSrc");
                                String src = jsonObject.optString("src");
                                Intent intent=new Intent(context, VideoPlayActivity.class);
                                if ("".equals(virtualSrc) && "".equals(src)) {
                                    //没有传递视频预览地址进来
                                    BaseModel   baseModel = new BaseModel("播放视频地址为空", -1, "播放视频地址不能为空");
                                    function.onCallBack(GsonUtils.toJson(baseModel));
                                } else if (!"".equals(virtualSrc) && "".equals(src)) {
                                    //线上地址
                                    intent.putExtra("url",virtualSrc);
                                    context.startActivity(intent);
                                    BaseModel  baseModel = new BaseModel("播放视频成功", 0, "播放视频地址为虚拟地址");
                                    function.onCallBack(GsonUtils.toJson(baseModel));
                                } else if ("".equals(virtualSrc) && !"".equals(src)) {
                                    //网络地址
                                    intent.putExtra("url",src);
                                    context.startActivity(intent);
                                    BaseModel  baseModel = new BaseModel("播放视频成功", 0, "播放视频地址为互联网地址");
                                    function.onCallBack(GsonUtils.toJson(baseModel));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                BaseModel baseModel = new BaseModel("视频播放失败", -1, "数据解析或播放器加载有误");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            }
                        }
                        else{
                            BaseModel baseModel = new BaseModel("视频播放失败", -1, "用户授权被拒绝");
                            function.onCallBack(GsonUtils.toJson(baseModel));
                        }
                    }
                });





    }


}
