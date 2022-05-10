package com.wisdom.jsinterfacelib.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.smallbuer.jsbridge.core.BridgeWebView
import com.wisdom.jsinterfacelib.ConstantString.CAN_BACK_KEY_USEFUL
import com.wisdom.jsinterfacelib.ConstantString.JS_FUN_NAME
import com.wisdom.jsinterfacelib.R

open class WebViewActivity : AppCompatActivity() {

    public var webView: BridgeWebView? = null
    var titleJs=""


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        //重置这个变量
        CAN_BACK_KEY_USEFUL=false
        //获得控件
        webView = findViewById<View>(R.id.wv_webview) as BridgeWebView

//        webViewInstance(webView!!)
        //        tv_1 = (TextView)findViewById(R.id.tv_1);
//        tv_2 = (TextView)findViewById(R.id.tv_2);
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//
//        int screenWidth = dm.widthPixels;
//        tv_1.setText(screenWidth+"");
//        int screenHeight = dm.heightPixels;
//        tv_2.setText(screenHeight+"");

        val url = intent.getStringExtra("url")
        titleJs = intent.getStringExtra("titleJs").toString()
        LogUtils.i("接到的标题2：$titleJs")
        if(titleJs.isNotBlank()){
            findViewById<TextView>(R.id.tv_title_js).text = titleJs
        }
        val hideNavBar = intent.getBooleanExtra("hideNavBar", false)
        var tv_title: TextView? = null
        //访问网页
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        val webSettings = webView!!.settings
        // 设置与Js交互的权限
        webSettings.javaScriptEnabled = true
        val ua = webView!!.settings.userAgentString
        webSettings.userAgentString = "$ua/openweb=wisdomhybrid/HRBZWFW_ANDROID,VERSION:1.1.0"
        //        Bridge.INSTANCE.registerHandler("toast", new ToastHandler());
        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        webView!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (tv_title != null) {
                    if (!view?.title.isNullOrBlank()) {
                        tv_title.text = view?.title.toString()
                    }

                }
                val back = findViewById<View>(R.id.back) as ImageView
                val head_close = findViewById<View>(R.id.head_close) as ImageView
                back.setOnClickListener {
                    if (CAN_BACK_KEY_USEFUL) {
                        //屏蔽了返回键，返回事件交给js处理
                        LogUtils.i("屏蔽了返回键，返回事件交给js处理")
                        val webSettings = webView!!.settings
                        // 设置与Js交互的权限
                        webSettings.javaScriptEnabled = true
                        if(JS_FUN_NAME.isNullOrBlank()||webView==null){
                            if (webView!!.canGoBack()) {
                                webView!!.goBack()
                            } else {
                                finish()
                            }
                        }else{
                            webView?.loadUrl(JS_FUN_NAME)
                        }
                    } else {
                        //没有屏蔽返回键
                        if (webView!!.canGoBack()) {
                            webView!!.goBack()
                        } else {
                            finish()
                        }
                    }
                }
                head_close.setOnClickListener {
                    this@WebViewActivity.finish()
                }

            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //使用WebView加载显示url
                view.loadUrl(url)
                //返回true
                return true
            }
        }
        //展示出自定义actionbar
        try {
            val actionBar = supportActionBar

            /***
            if (actionBar != null) {
            actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            val view = LayoutInflater.from(this).inflate(R.layout.action_bar, null, false)
            val back = view.findViewById<View>(R.id.back) as ImageView
            tv_title = view.findViewById(R.id.tv_title) as TextView
            back.setOnClickListener { view1: View? ->finish() }
            if ("" != ConstantString.ACTION_BAR_RGB) {
            view.setBackgroundColor(Color.parseColor(ConstantString.ACTION_BAR_RGB))
            }
            actionBar.customView = view
            actionBar.show()
            }
             ***/


        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (hideNavBar) {
            //是否隐藏actionBar
            supportActionBar?.hide()
            findViewById<RelativeLayout>(R.id.ll_bg).visibility = View.GONE
        } else {
            findViewById<RelativeLayout>(R.id.ll_bg).visibility = View.VISIBLE
        }





        if (!url.isNullOrBlank()) {
            webView!!.loadUrl(url)
        }
    }

    public fun hideParentActionBar() {
        findViewById<RelativeLayout>(R.id.ll_bg).visibility = View.GONE
    }

    /**
     * 屏蔽物理返回按键
     * */
    /**
    @SuppressLint("SetJavaScriptEnabled")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        when (event.keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (CAN_BACK_KEY_USEFUL) {
                    val webSettings = webView!!.settings
                    // 设置与Js交互的权限
                    webSettings.javaScriptEnabled = true
                    if (!JS_FUN_NAME.isNullOrBlank()) {
                        LogUtils.i("屏蔽1：JS_FUN_NAME:$JS_FUN_NAME")
//                        runOnUiThread {
//                            webView?.loadUrl("javascript:back()")
//                        }
                        if(webView?.canGoBack() == true){
                            webView!!.goBack()
                        }else{
                            finish()
                        }
                    }else{
                        if(webView?.canGoBack() == true){
                            webView!!.goBack()
                        }else{
                            finish()
                        }
                    }
                    LogUtils.i("屏蔽2：")
                    return true
                }
            }
        }
        LogUtils.i("屏蔽3：")
        return super.dispatchKeyEvent(event)

    }
         **/
    override fun onDestroy() {
        super.onDestroy()
        CAN_BACK_KEY_USEFUL = false
    }
}