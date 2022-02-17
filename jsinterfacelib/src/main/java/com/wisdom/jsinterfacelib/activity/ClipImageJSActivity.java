package com.wisdom.jsinterfacelib.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.blankj.utilcode.util.LogUtils;
import com.wisdom.jsinterfacelib.R;
import com.wisdom.jsinterfacelib.weight.ClipViewLayoutJS;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 头像裁剪Activity
 */
public class ClipImageJSActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ClipImclipViewLayout2ageActivity";
    private ClipViewLayoutJS clipViewLayoutJS1;
    private ClipViewLayoutJS clipViewLayoutJS2;
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
        setContentView(R.layout.activity_clip_image_js);
        type = getIntent().getIntExtra("type", 1);
        width = getIntent().getIntExtra("width", 800);
        height = getIntent().getIntExtra("height", 800);
        initView();
    }

    /**
     * 初始化组件
     */
    public void initView() {
        clipViewLayoutJS1 = (ClipViewLayoutJS) findViewById(R.id.clipViewLayout1_js);
        clipViewLayoutJS2 = (ClipViewLayoutJS) findViewById(R.id.clipViewLayout2_js);
        back = (ImageView) findViewById(R.id.iv_back_js);
        btnCancel = (TextView) findViewById(R.id.btn_cancel_js);
        btnOk = (TextView) findViewById(R.id.bt_ok_js);
        //设置点击事件监听器
        back.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        //设置裁剪框
        //计算裁剪框的大小，按照比例计算
        int screenW = getScreenWidth(this);
        int screenH = (screenW * height) / width;
        LogUtils.i("剪裁框宽高：" + screenW + "*" + screenH);
        LogUtils.i("传进来的高度：" + width + "*" + height);

        clipViewLayoutJS2.addClipView(screenW, screenH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type == 1) {
            clipViewLayoutJS1.setVisibility(View.VISIBLE);
            clipViewLayoutJS2.setVisibility(View.GONE);
            //设置图片资源
            clipViewLayoutJS1.setImageSrc(getIntent().getData());
        } else {
            clipViewLayoutJS2.setVisibility(View.VISIBLE);
            clipViewLayoutJS1.setVisibility(View.GONE);
            //设置图片资源
            clipViewLayoutJS2.setImageSrc(getIntent().getData());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back_js || id == R.id.btn_cancel_js) {
            finish();
        } else if (id == R.id.bt_ok_js) {
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
            zoomedCropBitmap = clipViewLayoutJS1.clip(width, height);
        } else {
            zoomedCropBitmap = clipViewLayoutJS2.clip(width, height);
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

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
