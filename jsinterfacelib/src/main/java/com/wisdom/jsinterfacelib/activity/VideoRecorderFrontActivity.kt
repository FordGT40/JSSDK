package com.wisdom.jsinterfacelib.activity


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Camera
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.os.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wisdom.jsinterfacelib.R
import java.io.File
import java.util.*
import kotlin.math.floor


class VideoRecorderFrontActivity : AppCompatActivity() {
    var h = Handler()
    var i = 0
    var r: Runnable = object : Runnable {
        override fun run() {
            val height: Int = tv_hint!!.height
            val scrollY: Int =  tv_hint!!.scrollY
            val lineHeight: Int =  tv_hint!!.lineHeight
            val lineCount: Int =  tv_hint!!.lineCount //总行数
            val maxY: Int =
                ( tv_hint!!.lineCount *  tv_hint!!.lineHeight +  tv_hint!!.paddingTop +  tv_hint!!.paddingBottom
                        -  tv_hint!!.height)
//            Log.e("=maxY=", maxY.toString() + "")
//            Log.e("=height=", height.toString() + "")
//            Log.e("=lineCount=",  tv_hint!!.lineCount.toString() + "")
            val viewCount = floor((height / lineHeight).toDouble()) //可见区域最大显示多少行
            if (lineCount > viewCount) { //总行数大于可见区域显示的行数时则滚动
                if (scrollY >= maxY) {
                    tv_hint!!.scrollBy(0, maxY)
                } else {
                    tv_hint!!.scrollBy(0, lineHeight)
                }
                h.postDelayed(this, 2000)
            }
        }
    }


    private var btn_stop: TextView? = null
    private var tv_hint:TextView? = null
    private var tv_title:TextView? = null
    private var btn: Button? = null
    private var recLen = 11
    private var isRecording = false
    private var mSurfaceHolder: SurfaceHolder? = null
    private var mediaRecorder: MediaRecorder? = null
    private var mCamera: Camera? = null
    private var mVecordFilepath: String? = null
    private var notice: String? = ""
    private var title: String? = ""
    private var textColor: String? = ""
    private var intentNext: Intent? = null
    var timer: Timer = Timer()

    companion object {
        const val RESULT_CODE_ERROR = 0x123
    }

    val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    btn_stop?.setText("还可拍摄" + recLen + "秒")
                    if (recLen < 0) {
                        btn_stop?.setVisibility(View.GONE)
                    }
                }
            }
        }
    }


    var task: TimerTask = object : TimerTask() {
        override fun run() {
            recLen--
            val message = Message()
            message.what = 1
            handler.sendMessage(message)

            //当倒计时时间到1的时候关闭录像
            if (recLen == 1) {
                stopRecord()
                stopCamera()
                cancel()
                //打开视频预览界面
                startActivityForResult(intentNext, 0x119)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == VideoPreviewActivity.RESULT_CODE_OK) {
            setResult(VideoPreviewActivity.RESULT_CODE_OK)
            finish()
        }
        if (resultCode == 0x111) {
            //用户在预览页面取消了，希望从新拍摄
            btn?.text = "拍摄"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_recorder_front)
        recLen = intent.getIntExtra("duration", 11)
        notice = intent.getStringExtra("notice")
        title = intent.getStringExtra("title")
        textColor = intent.getStringExtra("textColor")

        intentNext = Intent(this, VideoPreviewActivity::class.java)
        btn_stop = findViewById<TextView>(R.id.luxiangtimer)
        tv_hint = findViewById<TextView>(R.id.tv_hint)
        tv_title = findViewById<TextView>(R.id.tv_title)
        btn = findViewById<Button>(R.id.luxiangbtn)
        //设置字体颜色
        if (!textColor.isNullOrBlank()) {
            Color.parseColor(textColor).apply {
                btn_stop?.setTextColor(this)
                tv_hint?.setTextColor(this)
                tv_title?.setTextColor(this)
            }
        } else {
            Color.parseColor("#333333").apply {
                btn_stop?.setTextColor(this)
                tv_hint?.setTextColor(this)
                tv_title?.setTextColor(this)
            }
        }

        tv_title?.text = title
        val mSurfaceview: SurfaceView = findViewById<View>(R.id.surfaceview_luxiang) as SurfaceView
        mSurfaceHolder = mSurfaceview.holder
        tv_hint?.text = notice

        tv_hint?.requestFocus()



        // 屏幕长宽
        val metric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metric)
        val screenW = metric.widthPixels
        val screenH = metric.heightPixels
        val fixedScreenH: Int = screenW * screenH / screenW // 宽度不变，等比缩放的高度
        val params: ViewGroup.LayoutParams = mSurfaceview.layoutParams
        params.height = fixedScreenH
        mSurfaceview.layoutParams = params



        checkPermission(this)
        initCamera()
        btn?.setOnClickListener {
            recodeVideo()
            h.postDelayed(r, 5000)
        }

    }


    private fun recodeVideo() {
        if (Build.VERSION.SDK_INT >= 23) {
            val REQUEST_CODE_CONTACT = 101
        }
        if (isRecording && mediaRecorder != null) {
            //人为停止录像
            btn?.text = "拍摄"
            stopRecord()
            stopCamera()
            btn_stop?.visibility = View.GONE
            startActivityForResult(intentNext, 0x119)
//            AvoidOnResult(this@VideoRecorderFrontActivity).startForResult(
//                intentNext
//            ) { resultCode, data ->
//                if (resultCode == VideoPreviewActivity.RESULT_CODE_OK) {
//                    setResult(VideoPreviewActivity.RESULT_CODE_OK)
//                    finish()
//                }
//            }
            task.cancel()
        } else {
            //开始录像
            btn?.text = "完成"

            //这是是判断视频文件有没有创建,如果没有就返回
            val creakOk = createRecordDir()
            if (!creakOk) {
                return
            }

            try {
                initCamera()
                mCamera?.unlock()
                setConfigRecord()
                mediaRecorder?.prepare()
                mediaRecorder?.start()
            } catch (e: Exception) {
                val intentErr = Intent()
                intentErr.putExtra("msg", "相机相关参数初始化失败")
                setResult(RESULT_CODE_ERROR, intentErr)
                finish()
            }
            isRecording = true
            try {
                timer.schedule(task, 1000, 1000)
            } catch (e: Exception) {
                recLen = intent.getIntExtra("duration", 11)
                task = object : TimerTask() {
                    override fun run() {
                        recLen--
                        val message = Message()
                        message.what = 1
                        handler.sendMessage(message)

                        //当倒计时时间到1的时候关闭录像
                        if (recLen == 1) {
                            stopRecord()
                            stopCamera()
                            cancel()
                            //打开视频预览界面
                            startActivityForResult(intentNext, 0x119)
                        }
                    }
                }
                timer.schedule(task, 1000, 1000)
                btn_stop?.visibility = View.VISIBLE
            }

        }
    }

    private fun setConfigRecord() {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.reset()
        mediaRecorder!!.setCamera(mCamera)
        mediaRecorder!!.setOnErrorListener { _, _, _ ->

        }

        //录像角度
        mediaRecorder!!.setOrientationHint(270)
        //使用SurfaceView预览
        mediaRecorder!!.setPreviewDisplay(mSurfaceHolder!!.surface)
        //1.设置采集声音
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        //设置采集图像
        mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.CAMERA)
        //2.设置视频，音频的输出格式 mp4
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
        //3.设置音频的编码格式
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        //设置图像的编码格式
        mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        val mProfile: CamcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P)
        mediaRecorder!!.setAudioEncodingBitRate(44100)
        if (mProfile.videoBitRate > 2 * 1024 * 1024) {
            mediaRecorder!!.setVideoEncodingBitRate(2 * 1024 * 1024)
        } else {
            mediaRecorder!!.setVideoEncodingBitRate(1024 * 1024)
        }
        mediaRecorder!!.setVideoFrameRate(mProfile.videoFrameRate)
        mediaRecorder!!.setVideoSize(1280, 720)
        mediaRecorder!!.setOutputFile(mVecordFilepath)
    }

    private fun initCamera() {

        try {
            mCamera = Camera.open(findFrontCamera()) //①
            mCamera!!.setDisplayOrientation(90)
            mCamera!!.setPreviewDisplay(mSurfaceHolder)
            mCamera!!.cancelAutoFocus() //此句加上 可自动聚焦 必须加
            val parameters: Camera.Parameters = mCamera!!.parameters
            //查询摄像头支持的分辨率
            parameters.supportedPreviewSizes
            for (i in 0 until parameters.supportedPreviewSizes.size) {
                Log.i(
                    "<><><><>Width",
                    parameters.supportedPreviewSizes[i].width.toString() + ""
                )
                Log.i(
                    "<><><><>Height",
                    parameters.supportedPreviewSizes[i].height.toString() + ""
                )
            }
            //设置分辨率
            parameters.setPreviewSize(1280, 720)
            //设置聚焦模式
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
            //缩短Recording启动时间
            parameters.setRecordingHint(true)
            //是否支持影像稳定能力，支持则开启
            if (parameters.isVideoStabilizationSupported) parameters.videoStabilization = true
            mCamera!!.parameters = parameters
            mCamera!!.startPreview()
        } catch (e: Exception) {
//            val intentErr = Intent()
//            intentErr.putExtra("msg", "相机初始化失败")
//            setResult(RESULT_CODE_ERROR, intentErr)
//            finish()
        }
    }

    private fun createRecordDir(): Boolean {
        try {
            mVecordFilepath =
                this@VideoRecorderFrontActivity.externalCacheDir?.path + "/recorder.mp4"
            intentNext?.putExtra("url", mVecordFilepath)
            val f = File(mVecordFilepath)
            if (!f.exists()) {
                f.createNewFile()
            }
        } catch (e: Exception) {
            val intentErr = Intent()
            intentErr.putExtra("msg", "MP4文件存储地址初始化失败")
            setResult(RESULT_CODE_ERROR, intentErr)
            finish()
        }
        return true
    }

    private fun stopRecord() {
        try {
            if (isRecording && mediaRecorder != null) {
                mediaRecorder?.setOnErrorListener(null)
                mediaRecorder?.setPreviewDisplay(null)
                mediaRecorder?.stop()
                mediaRecorder?.reset()
                mediaRecorder?.release()
                mediaRecorder = null
                isRecording = false
            }
        } catch (ex: Exception) {
            val intentErr = Intent()
            intentErr.putExtra("msg", "终止录像出错失败")
            setResult(RESULT_CODE_ERROR, intentErr)
            finish()
        }
    }

    private fun stopCamera() {
        if (mCamera != null) {
            mCamera?.setPreviewCallback(null)
            mCamera?.stopPreview()
            mCamera?.release()
            mCamera = null
        }
    }

    private fun checkPermission(activity: AppCompatActivity?) {
        if (Build.VERSION.SDK_INT >= 23) {
            val checkPermission =
                (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.RECORD_AUDIO)
                        + ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_PHONE_STATE
                )
                        + ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                        + ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ))
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                //动态申请
                ActivityCompat.requestPermissions(
                    activity, arrayOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 123
                )
                return
            } else {
                return
            }
        }
        return
    }

    fun surfaceCreated(@NonNull holder: SurfaceHolder?) {
        Log.d("111", "surfaceCreated")
    }

    fun surfaceChanged(@NonNull holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.d("111", "surfaceChanged")
    }

    fun surfaceDestroyed(@NonNull holder: SurfaceHolder?) {
        Log.d("111", "surfaceDestroyed")
    }


    /**
     *  @describe 找到前置摄像头id
     *  @return
     *  @author HanXueFeng
     *  @time 2021/8/9 0009  13:45
     */
    private fun findFrontCamera(): Int {
        try {
            var cameraCount = 0
            val cameraInfo = Camera.CameraInfo()
            cameraCount = Camera.getNumberOfCameras()
            for (camIdx in 0 until cameraCount) {
                Camera.getCameraInfo(camIdx, cameraInfo)
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    return camIdx
                }
            }
        } catch (e: Exception) {
            val intentErr = Intent()
            intentErr.putExtra("msg", "获取前置摄像头失败")
            setResult(RESULT_CODE_ERROR, intentErr)
            finish()
        }
        return -1
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            stopRecord()
            stopCamera()
            task.cancel()
        } catch (e: Exception) {
            val intentErr = Intent()
            intentErr.putExtra("msg", "终止倒计时失败")
            setResult(RESULT_CODE_ERROR, intentErr)
            finish()
        }
    }
}
