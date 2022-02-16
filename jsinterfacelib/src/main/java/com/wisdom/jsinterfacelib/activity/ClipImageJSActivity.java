package com.wisdom.jsinterfacelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.wisdom.jsinterfacelib.R;
import com.wisdom.jsinterfacelib.weight.ClipView;
import com.wisdom.jsinterfacelib.weight.ClipViewLayout;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 头像裁剪Activity
 */
public class ClipImageJSActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ClipImclipViewLayout2ageActivity";
    private ClipViewLayout clipViewLayout1;
    private ClipViewLayout clipViewLayout2;
    private ImageView back;
    private TextView btnCancel;
    private TextView btnOk;
    private int width = 200;
    private int height = 200;
    //类别 1: qq, 2: weixin
    private int type;


    /**
     * 打开截图界面
     */
    public static void gotoClipActivity(Activity activity, Uri uri, int width, int height, int requestCode) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity, ClipImageJSActivity.class);
        intent.putExtra("type", 2); // 1: qq, 2: weixin
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.setData(uri);
//         REQUEST_CROP_PHOTO
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_image);
        type = getIntent().getIntExtra("type", 1);
        width = getIntent().getIntExtra("width", 200);
        height = getIntent().getIntExtra("height", 200);
        initView();
    }

    /**
     * 初始化组件
     */
    public void initView() {
        clipViewLayout1 = (ClipViewLayout) findViewById(R.id.clipViewLayout1);
        clipViewLayout2 = (ClipViewLayout) findViewById(R.id.clipViewLayout2);
        back = (ImageView) findViewById(R.id.iv_back);
        btnCancel = (TextView) findViewById(R.id.btn_cancel);
        btnOk = (TextView) findViewById(R.id.bt_ok);
        //设置点击事件监听器
        back.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        //设置裁剪框
        clipViewLayout2.addClipView(width,height);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type == 1) {
            clipViewLayout1.setVisibility(View.VISIBLE);
            clipViewLayout2.setVisibility(View.GONE);
            //设置图片资源
            clipViewLayout1.setImageSrc(getIntent().getData());
        } else {
            clipViewLayout2.setVisibility(View.VISIBLE);
            clipViewLayout1.setVisibility(View.GONE);
            //设置图片资源
            clipViewLayout2.setImageSrc(getIntent().getData());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back || id == R.id.btn_cancel) {
            finish();
        } else if (id == R.id.bt_ok) {
            generateUriAndReturn();
        }
    }


    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    private void generateUriAndReturn() {
        //调用返回剪切图
        Bitmap zoomedCropBitmap;
        if (type == 1) {
            zoomedCropBitmap = clipViewLayout1.clip(width, height);
        } else {
            zoomedCropBitmap = clipViewLayout2.clip(width, height);
        }
        if (zoomedCropBitmap == null) {
            Log.e("android", "zoomedCropBitmap == null");
            return;
        }
        Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".png"));
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                }
            } catch (IOException ex) {
                Log.e("android", "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Intent intent = new Intent();
            intent.setData(mSaveUri);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
