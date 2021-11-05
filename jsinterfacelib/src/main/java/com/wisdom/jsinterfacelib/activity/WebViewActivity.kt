package com.wisdom.jsinterfacelib.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.smallbuer.jsbridge.core.BridgeWebView
import com.wisdom.jsinterfacelib.ConstantString.CAN_BACK_KEY_USEFUL
import com.wisdom.jsinterfacelib.ConstantString.JS_FUN_NAME
import com.wisdom.jsinterfacelib.R

open class WebViewActivity : AppCompatActivity() {

    public var webView: BridgeWebView? = null


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
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
        val hideNavBar = intent.getBooleanExtra("hideNavBar", false)
        if (hideNavBar) {
            //是否隐藏actionBar
            supportActionBar?.hide()
        }

        if (!url.isNullOrBlank()) {
            webView!!.loadUrl(url)
        }
//            //访问网页


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
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //使用WebView加载显示url
                view.loadUrl(url)
                //返回true
                return true
            }
        }
    }


    /**
     * 屏蔽物理返回按键
     * */
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action != KeyEvent.ACTION_UP) {
            //do something.
            if (CAN_BACK_KEY_USEFUL) {
                if (!JS_FUN_NAME.isNullOrBlank()) {
                    webView?.loadUrl(JS_FUN_NAME)
                }
                CAN_BACK_KEY_USEFUL
            } else {
                super.dispatchKeyEvent(event)
            }
        } else {
            super.dispatchKeyEvent(event)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CAN_BACK_KEY_USEFUL = false
    }
}