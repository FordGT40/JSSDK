package com.wisdom.jsinterfacelib.face;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.alibaba.android.mnnkit.actor.FaceDetector;
import com.alibaba.android.mnnkit.entity.FaceDetectConfig;
import com.alibaba.android.mnnkit.entity.FaceDetectionReport;
import com.alibaba.android.mnnkit.entity.MNNCVImageFormat;
import com.alibaba.android.mnnkit.entity.MNNFlipType;
import com.alibaba.android.mnnkit.intf.InstanceCreatedListener;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.cunoraz.gifview.library.GifView;
import com.wisdom.jsinterfacelib.R;
import com.wisdom.jsinterfacelib.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class FaceDetectionActivity extends VideoBaseActivity {

    private Timer timer = new Timer();
    private int second = 0;
    private int actionCount = 1;
    private TimerTask timerTask;
    private SurfaceHolder mDrawSurfaceHolder;
    protected CameraView mCameraView;
    protected MediaPlayer mediaPlayer;
    public static final int RESULT_CODE_FACE_DETECTION = 0x115;

    private TextView tv_info;
    private GifView gif_view;
    private NavigationBar navigationbar;
    private Boolean isNext = true;

    private Boolean isSpeak1 = true;
    private Boolean isSpeak2 = false;
    private Boolean isDetection = false;
    private Boolean isDetection2 = false;
    private Boolean isFaceSuccess = false;
    public static final String TAG = FaceDetectionActivity.class.getSimpleName();

//    private Switch mOrderSwitch;
//    private TextView mTimeCost;
//    private TextView mFaceAction;
//    private TextView mYPR;

    // ???????????????????????????
    protected int mActualPreviewWidth;
    protected int mActualPreviewHeight;

    private FaceDetector mFaceDetector;

    private Paint KeyPointsPaint = new Paint();
    private Paint PointOrderPaint = new Paint();
    private Paint ScorePaint = new Paint();

    private final static int MAX_RESULT = 10;
    private float[] scores = new float[MAX_RESULT];// ?????????
    private float[] rects = new float[MAX_RESULT * 4];// ????????????
    private float[] keypts = new float[MAX_RESULT * 2 * 106];// ???106?????????
    private List<String> listAction = new ArrayList();
    String action1 = "";
    String action2 = "";
    String loginType = "";//????????????
    String loginContent = "";//????????????????????????????????????

    private String startTag = "";//????????????????????????????????????????????????
    private String tempAction = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarLightMode(this, true);
//        BarUtils.setStatusBarVisibility(this, false);
        BarUtils.transparentStatusBar(this);

        //?????????????????????
//        listAction.add("????????????");
//        listAction.add("????????????");
        tv_info.setText("??????????????????");

        listAction.add("??????");
        listAction.add("??????");
        listAction.add("??????");
        listAction.add("??????");
        listAction.add("??????");
        listAction.add("??????");
        listAction.add("??????");
        listAction.add("??????");
        listAction.add("??????");
//        listAction.add("??????");
//        listAction.add("??????");
        //????????????????????????
        action1 = listAction.get(new Random().nextInt(8));
        action2 = "";
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    action2 = listAction.get(new Random().nextInt(8));
                    if (!action1.equals(action2)) {
                        return;
                    }
                }
            }
        }.start();
        //
        KeyPointsPaint.setColor((Color.GREEN));
        KeyPointsPaint.setStyle(Paint.Style.FILL);
        KeyPointsPaint.setStrokeWidth(2);

        PointOrderPaint.setColor(Color.GREEN);
        PointOrderPaint.setStyle(Paint.Style.STROKE);
        PointOrderPaint.setStrokeWidth(2f);
        PointOrderPaint.setTextSize(18);

        ScorePaint.setColor(Color.WHITE);
        ScorePaint.setStrokeWidth(2f);
        ScorePaint.setTextSize(40);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if (item.getItemId() == R.id.action_switch_camera) {
//            mCameraView.switchCamera();
//        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    void createKitInstance() {

        FaceDetector.FaceDetectorCreateConfig createConfig = new FaceDetector.FaceDetectorCreateConfig();
        createConfig.mode = FaceDetector.FaceDetectMode.MOBILE_DETECT_MODE_VIDEO;
        FaceDetector.createInstanceAsync(this, createConfig, new InstanceCreatedListener<FaceDetector>() {
            @Override
            public void onSucceeded(FaceDetector faceDetector) {
                mFaceDetector = faceDetector;
            }

            @Override
            public void onFailed(int i, Error error) {
                LogUtils.i("create face detetector failed: " + error);
            }
        });
    }

    @Override
    String actionBarTitle() {
        return "????????????";
    }


    @Override
    void doRun() {
        ViewStub stub = findViewById(R.id.viewStub);
        stub.setLayoutResource(R.layout.activity_face_detection);
        stub.inflate();
        // point view
        SurfaceView drawView = findViewById(R.id.points_view);
        drawView.setZOrderOnTop(true);
        drawView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mDrawSurfaceHolder = drawView.getHolder();
        gif_view = findViewById(R.id.gif_view);

        navigationbar = findViewById(R.id.navigationbar);
        navigationbar.showBackImage().setOnClickListener(view -> FaceDetectionActivity.this.finish());

        //????????????
        String title = getIntent().getStringExtra("title");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
        navigationbar.setTitle(title);
        actionCount = getIntent().getIntExtra("actionCount", 1);
        second = getIntent().getIntExtra("second", 0);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (second > 0) {
                    second--;
                }
                if (second == 0) {
                    Intent intent = new Intent();
                    intent.putExtra("code", -1);
                    intent.putExtra("msg", getIntent().getIntExtra("second", 0) + "????????????????????????");
                    setResult(RESULT_CODE_FACE_DETECTION, intent);
                    FaceDetectionActivity.this.finish();
                }

            }
        };
        if (second > 0) {
            //?????????????????????????????????????????????
            cutDown(second);
        }


        mCameraView = findViewById(R.id.camera_view);
        tv_info = findViewById(R.id.tv_info);
        mCameraView.setPreviewCallback(new CameraView.PreviewCallback() {
            @Override
            public void onGetPreviewOptimalSize(int optimalWidth, int optimalHeight, int cameraOrientation, int deviecAutoRotateAngle) {

                // w??????????????????h?????????
                int w = optimalWidth;
                int h = optimalHeight;
                if (cameraOrientation == 90 || cameraOrientation == 270) {
                    w = optimalHeight;
                    h = optimalWidth;
                }

                // ????????????
                DisplayMetrics metric = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metric);
                int screenW = metric.widthPixels;
                int screenH = metric.heightPixels;

                int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;

                RelativeLayout layoutVideo = findViewById(R.id.videoLayout);
                FrameLayout frameLayout = layoutVideo.findViewById(R.id.videoContentLayout);
                if (deviecAutoRotateAngle == 0 || deviecAutoRotateAngle == 180) {
                    int fixedScreenH = screenW * h / w;// ????????????????????????????????????

                    ViewGroup.LayoutParams params = frameLayout.getLayoutParams();
                    params.height = fixedScreenH;
                    frameLayout.setLayoutParams(params);
                    mActualPreviewWidth = screenW;
                    mActualPreviewHeight = fixedScreenH;
                } else {
                    int previewHeight = screenH - contentTop - statusBarHeight;
                    int fixedScreenW = previewHeight * h / w;// ?????????????????????????????????

                    ViewGroup.LayoutParams params = frameLayout.getLayoutParams();
                    params.width = fixedScreenW;
                    frameLayout.setLayoutParams(params);

                    mActualPreviewWidth = fixedScreenW;
                    mActualPreviewHeight = previewHeight;

                    // re layout
                    RelativeLayout.LayoutParams componentLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    componentLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.videoContentLayout);

//                    RelativeLayout componentLayout  = findViewById(R.id.componentLayout);
//                    componentLayout.setLayoutParams(componentLayoutParams);

//                    RelativeLayout.LayoutParams yprLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    yprLayoutParams.addRule(RelativeLayout.BELOW, R.id.costTime);
//                    mYPR.setPadding(24,0,0,0);
//                    mYPR.setLayoutParams(yprLayoutParams);

//                    RelativeLayout.LayoutParams faceActionLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    faceActionLayoutParams.addRule(RelativeLayout.BELOW, R.id.ypr);
//                    mFaceAction.setPadding(24,0,0,0);
//                    mFaceAction.setLayoutParams(faceActionLayoutParams);
                }

            }

            @Override
            public void onPreviewFrame(byte[] data, int width, int height, int cameraOrientation) {
                if (mFaceDetector == null) {
                    return;
                }

                // ????????????
                int inAngle = mCameraView.isFrontCamera() ? (cameraOrientation + 360 - rotateDegree) % 360 : (cameraOrientation + rotateDegree) % 360;
                // ????????????
                int outAngle = 0;

                if (!screenAutoRotate()) {
                    outAngle = mCameraView.isFrontCamera() ? (360 - rotateDegree) % 360 : rotateDegree % 360;
                }

                long start = System.currentTimeMillis();
                long detectConfig = FaceDetectConfig.ACTIONTYPE_EYE_BLINK | FaceDetectConfig.ACTIONTYPE_MOUTH_AH | FaceDetectConfig.ACTIONTYPE_HEAD_YAW | FaceDetectConfig.ACTIONTYPE_HEAD_PITCH | FaceDetectConfig.ACTIONTYPE_BROW_JUMP;
                FaceDetectionReport[] results = mFaceDetector.inference(data, width, height, MNNCVImageFormat.YUV_NV21, detectConfig, inAngle, outAngle, mCameraView.isFrontCamera() ? MNNFlipType.FLIP_Y : MNNFlipType.FLIP_NONE);

                String timeCostText = "0 ms";
                String yprText = "";
                String faceActionText = "";

                int faceCount = 0;
                if (results != null && results.length > 0) {

                    faceCount = results.length;
                    gif_view.play();
                    tv_info.setText(tempAction);

                    // time cost
                    timeCostText = (System.currentTimeMillis() - start) + "ms";
                    // ypr
                    FaceDetectionReport firstReport = results[0];
                    yprText = "yaw: " + firstReport.yaw + "\npitch: " + firstReport.pitch + "\nroll: " + firstReport.roll + "\n";
//
                    for (int i = 0; i < results.length && i < MAX_RESULT; i++) {
                        // key points ???????????????
//                        System.arraycopy(results[i].keyPoints, 0, keypts, i*106*2, 106*2);
                        // face rect
                        rects[i * 4] = results[i].rect.left;
                        rects[i * 4 + 1] = results[i].rect.top;
                        rects[i * 4 + 2] = results[i].rect.right;
                        rects[i * 4 + 3] = results[i].rect.bottom;
                        // score  ?????????????????????
//                        scores[i] = results[i].score;
                    }
                    //???????????????????????????????????????
                    if (isSpeak1) {
                        isSpeak1 = false;
                        playSound(action1);
                        isDetection = true;
                        tv_info.setText("???" + action1);
                        tempAction = "???" + action1;
                        setGif(action1);
                    }

                    if (actionCount == 2) {
                        //?????????????????????
                        if (isDetection) {
                            if (faceActionDesc(firstReport.faceActionMap).contains(action1)) {
                                isDetection = false;
                                isSpeak2 = true;
                            }
                        }
                        //?????????????????????
                        if (isSpeak2) {
                            isSpeak2 = false;
                            playSound(action2);
                            isDetection2 = true;
                            tv_info.setText("???" + action2);
                            tempAction = "???" + action2;
                            setGif(action2);
                        }
                    }

                    //?????????????????????
                    if (actionCount == 1 ? isDetection : isDetection2) {
                        if (faceActionDesc(firstReport.faceActionMap).contains(actionCount == 1 ? action1 : action2)) {
                            isDetection2 = false;
                            if (actionCount == 1) {
                                isDetection = false;
                            }
                            playSound("????????????");
                            tv_info.setText("??????????????????????????????");
                            timerTask.cancel();
                            gif_view.pause();
                            gif_view.setVisibility(View.GONE);
                            isFaceSuccess = true;
                        }
                    }
                    if (isFaceSuccess) {
                        //???????????????????????????????????????
                        FaceDetectionReport report = results[0];
                        if (report.pitch > 0.3) {
                            tv_info.setText("?????????");
                        } else if (report.pitch < -0.15) {
                            tv_info.setText("?????????");
                        } else if (report.yaw > 0.3) {
                            tv_info.setText("???????????????");
                        } else if (report.yaw < -0.3) {
                            tv_info.setText("???????????????");
                        } else if (report.roll > 0.3) {
                            tv_info.setText("???????????????");
                        } else if (report.roll < -0.3) {
                            tv_info.setText("???????????????");
                        } else {
                            //????????????
                            if (Math.abs(report.pitch) < 0.3 && Math.abs(report.roll) < 0.3 && Math.abs(report.yaw) < 0.3) {
                                Bitmap bitmap = ImageUtil.getBitmapView(mCameraView, data);
                                bitmap = ImageUtil.rotateBitmap(bitmap, 270);
                                tv_info.setText("??????????????????????????????");
                                try {
                                    FileUtils.delete(Environment.getExternalStorageDirectory().toString() + "/faceInfo1.jpg");
                                    FileUtils.delete(Environment.getExternalStorageDirectory().toString() + "/faceInfo.jpg");
                                    String filePath = ImageUtil.saveMyBitmapLocal(FaceDetectionActivity.this, bitmap, "faceInfo.jpg",2);
                                    // ??????????????????
                                    Intent intent = new Intent();
                                    intent.putExtra("code", 0);
                                    intent.putExtra("msg", "???????????????");
                                    intent.putExtra("filePath", filePath);
                                    setResult(RESULT_CODE_FACE_DETECTION, intent);
                                    FaceDetectionActivity.this.finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Intent intent = new Intent();
                                    intent.putExtra("code", -1);
                                    intent.putExtra("msg", "??????????????????");
                                    setResult(RESULT_CODE_FACE_DETECTION, intent);
                                    FaceDetectionActivity.this.finish();
                                }
                                mCameraView.onPause();
                            } else {
                                tv_info.setText("????????????????????????");
                                gif_view.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
//                    if (hasFace) {
////                        tv_info.setText("?????????????????????????????????");
//                    } else {
                    tv_info.setText("??????????????????");
//                    }
                    gif_view.pause();
                }

//????????????????????????
                DrawResult(scores, rects, keypts, faceCount, cameraOrientation, rotateDegree, data);
            }

        });

    }

    private String faceActionDesc(Map<String, Boolean> faceActionMap) {

        String desc = "";
        if (faceActionMap.size() == 0) {
            return desc;
        }

        List<String> actions = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : faceActionMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

            Boolean bActing = entry.getValue();
            if (!bActing) continue;

            if (entry.getKey().equals("HeadYaw")) {
                actions.add("??????");
            }
            if (entry.getKey().equals("BrowJump")) {
                actions.add("????????????");
            }
            if (entry.getKey().equals("EyeBlink")) {
                actions.add("??????");
            }
            if (entry.getKey().equals("MouthAh")) {
                actions.add("??????");
            }
            if (entry.getKey().equals("HeadPitch")) {
                actions.add("??????");
            }
        }

        for (int i = 0; i < actions.size(); i++) {
            String action = actions.get(i);
            if (i > 0) {
                desc += "???" + action;
                continue;
            }
            desc = action;
        }

        return desc;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFaceDetector != null) {
            mFaceDetector.release();
        }
        if (mCameraView != null) {
            mCameraView.setPreviewCallback(null);
            mCameraView.onPause();

        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

    }


    private void DrawResult(float[] scores, float[] rects, float[] facePoints,
                            int faceCount, int cameraOrientation, int rotateDegree, byte[] data) {
        Canvas canvas = null;
        try {
            canvas = mDrawSurfaceHolder.lockCanvas();
            if (canvas == null) {
                return;
            }
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            float kx = 0.0f, ky = 0.0f;

            // ?????????????????????????????????90/270?????????????????????????????????????????????????????????
            if (90 == cameraOrientation || 270 == cameraOrientation) {
                if (!screenAutoRotate()) {
                    kx = ((float) mActualPreviewWidth) / mCameraView.getPreviewSize().height;
                    ky = (float) mActualPreviewHeight / mCameraView.getPreviewSize().width;
                } else {
                    if ((0 == rotateDegree) || (180 == rotateDegree)) {// ????????????????????????
                        kx = ((float) mActualPreviewWidth) / mCameraView.getPreviewSize().height;
                        ky = ((float) mActualPreviewHeight) / mCameraView.getPreviewSize().width;
                    } else if (90 == rotateDegree || 270 == rotateDegree) {// ????????????????????????
                        kx = ((float) mActualPreviewWidth) / mCameraView.getPreviewSize().width;
                        ky = ((float) mActualPreviewHeight) / mCameraView.getPreviewSize().height;
                    }
                }
            }

            // ?????????????????????
            for (int i = 0; i < faceCount; i++) {
                for (int j = 0; j < 106; j++) {
                    float keyX = facePoints[i * 106 * 2 + j * 2];
                    float keyY = facePoints[i * 106 * 2 + j * 2 + 1];
                    canvas.drawCircle(keyX * kx, keyY * ky, 4.0f, KeyPointsPaint);
//                    if (mOrderSwitch.isChecked()) {
//                        canvas.drawText(j+"", keyX * kx, keyY * ky, PointOrderPaint); //??????106??????????????????
//                    }
                }

                float left = rects[0];
                float top = rects[1];
                float right = rects[2];
                float bottom = rects[3];
                canvas.drawLine(left * kx, top * ky,
                        right * kx, top * ky, KeyPointsPaint);
                canvas.drawLine(right * kx, top * ky,
                        right * kx, bottom * ky, KeyPointsPaint);
                canvas.drawLine(right * kx, bottom * ky,
                        left * kx, bottom * ky, KeyPointsPaint);
                canvas.drawLine(left * kx, bottom * ky,
                        left * kx, top * ky, KeyPointsPaint);
/**
 if (isFaceSuccess) {
 //??????????????????
 int startX = (int) (left * kx);
 int startY = (int) (top * ky);

 int widthOrignal = (int) (left * kx - right * kx);
 int width = Math.abs(widthOrignal);

 int heigtOrignal = (int) (bottom * ky - top * ky);
 int heigt = Math.abs(heigtOrignal);

 int[] arrayPoint = new int[2];
 arrayPoint[0] = startX;
 arrayPoint[1] = startY;

 Bitmap bitmap = BitmapUtils.getBitmap(this, arrayPoint, width, heigt);
 ImageUtils.save(bitmap, "hitadoa", Bitmap.CompressFormat.JPEG);
 ToastUtils.showShort("???????????????");
 }
 **/
// ??????????????????????????????????????????????????????
                canvas.drawText(scores[i] + "", left * kx, top * ky - 10, ScorePaint);
            }


        } catch (Throwable t) {
            LogUtils.i("Draw result error: %s", t);
        } finally {
            if (canvas != null) {
                mDrawSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }


    /**
     * ??????????????????gif??????
     *
     * @param action
     */
    private void setGif(String action) {
        switch (action) {
            case "??????": {
//                gif_view.setGifImage(R.drawable.shake_head);
                gif_view.setGifResource(R.drawable.shake_head);
            }
            break;
            case "??????": {
//                gif_view.setGifImage(R.drawable.nod);
                gif_view.setGifResource(R.drawable.nod);
            }
            break;
            case "??????": {
//                gif_view.setGifImage(R.drawable.blink);
                gif_view.setGifResource(R.drawable.blink);
            }
            break;
            case "??????": {
//                gif_view.setGifImage(R.drawable.open_mouth);
                gif_view.setGifResource(R.drawable.open_mouth);
            }
            break;
        }

    }


    private void playSound(String action) {
        try {
            AssetFileDescriptor fd = null;
            switch (action) {
                case "??????": {
                    fd = getAssets().openFd("?????????.mp3");
                }
                break;
                case "??????": {
                    fd = getAssets().openFd("?????????.mp3");
                }
                break;
                case "??????": {
                    fd = getAssets().openFd("?????????.mp3");
                }
                break;
                case "??????": {
                    fd = getAssets().openFd("?????????.mp3");
                }
                break;
                case "????????????": {
                    fd = getAssets().openFd("????????????.mp3");
                }
                break;
            }
            if (fd != null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mediaPlayer.prepare();
                mediaPlayer.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * ???????????????
     */
    private void cutDown(long second) {
        if (timer != null) {
            final long[] localSecond = {second};
            timer.schedule(timerTask, 0, 1000);
        }
    }

}
