package com.wisdom.jsinterfacelib.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.wisdom.jsinterfacelib.R;

import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videoplayer.player.VideoView;


/**
 * 前置摄像头录像后，预览录像效果的类，定制鸡西不动产项目录视频的预览页面
 */
public class VideoPreviewActivity extends AppCompatActivity {
    private VideoView videoView;
    public static final int RESULT_CODE_OK = 0x113;
    private ImageView iv_ok;
    private ImageView iv_cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        videoView = findViewById(R.id.videoView);
        iv_ok = findViewById(R.id.iv_ok);
        iv_cancle = findViewById(R.id.iv_cancle);
        String URL_VOD = getIntent().getStringExtra("url");
        videoView.setUrl(URL_VOD); //设置视频地址
        StandardVideoController controller = new StandardVideoController(this);
        controller.addDefaultControlComponent("标题", false);
        videoView.setVideoController(controller); //设置控制器
        videoView.start(); //开始播放，不调用则不自动播放.
        //取消按钮点击事件
        iv_cancle.setOnClickListener(view -> {
            setResult(0x111);
            finish();
        });
        //确定按钮点击事件
        iv_ok.setOnClickListener(view -> {
            setResult(RESULT_CODE_OK);
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.release();
    }


    @Override
    public void onBackPressed() {
        if (!videoView.onBackPressed()) {
            super.onBackPressed();
        }
    }
}