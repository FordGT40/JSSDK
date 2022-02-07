package com.wisdom.jssdk_basic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;


import com.smallbuer.jsbridge.core.BridgeWebView;
import com.wisdom.jsinterfacelib.ConstantString;
import com.wisdom.jsinterfacelib.activity.WebViewActivity;

public class MainActivity extends WebViewActivity {

    private LinearLayout ll_parent;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll_parent = findViewById(R.id.ll_parent);
        ll_parent.setBackgroundColor(Color.parseColor("#00ffffff"));



//        WebViewActivity.Companion.setUrl("http://192.168.111.173:8000/index.html");

//        WebViewActivity.Companion.getWebView().loadUrl("http://192.168.111.173:8000/index.html");
        /***
         //获得控件
         webView = (BridgeWebView) findViewById(R.id.wv_webview);
         //        tv_1 = (TextView)findViewById(R.id.tv_1);
         //        tv_2 = (TextView)findViewById(R.id.tv_2);
         //        DisplayMetrics dm = getResources().getDisplayMetrics();
         //
         //        int screenWidth = dm.widthPixels;
         //        tv_1.setText(screenWidth+"");
         //        int screenHeight = dm.heightPixels;
         //        tv_2.setText(screenHeight+"");

         // url = getIntent().getStringExtra("url");
         //访问网页
         webView.loadUrl("http://192.168.111.173:8000/index.html");
         //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
         WebSettings webSettings = webView.getSettings();
         // 设置与Js交互的权限
         webSettings.setJavaScriptEnabled(true);
         String ua = webView.getSettings().getUserAgentString();
         webSettings.setUserAgentString(ua+"/openweb=wisdomhybrid/HRBZWFW_ANDROID,VERSION:1.1.0");
         //        Bridge.INSTANCE.registerHandler("toast", new ToastHandler());
         // 通过addJavascriptInterface()将Java对象映射到JS对象
         //参数1：Javascript对象名
         //参数2：Java对象名
         webView.setWebViewClient(new WebViewClient(){
        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //使用WebView加载显示url
        view.loadUrl(url);
        //返回true
        return true;
        }
        });
         ***/


        Intent intent=new Intent(this, WebViewActivity.class);
//        intent.putExtra("url","http://192.168.1.39:8090/jstest/index.html");

//        intent.putExtra("url","http://192.168.1.105:8080/");
        intent.putExtra("url","http://192.168.111.173:8000/index.html");
//        intent.putExtra("url","http://36.134.125.179:8989/xyh-app/alumniGarden?xyhId=b4ac393dbb8c4858b681d28b2bd0890a&schoolId=af8a2c81af3e45359da274b290d898c2");
        startActivity(intent);

    }


}