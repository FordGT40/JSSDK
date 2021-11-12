package com.wisdom.jsinterfacelib;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.smallbuer.jsbridge.core.Bridge;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.wisdom.jsinterfacelib.handler.AppVersionHandler;
import com.wisdom.jsinterfacelib.handler.CameraCustomHandler;
import com.wisdom.jsinterfacelib.handler.ChooseImageHandler;
import com.wisdom.jsinterfacelib.handler.ChooseVideoHandler;
import com.wisdom.jsinterfacelib.handler.InvokeMethodHandler;
import com.wisdom.jsinterfacelib.handler.DeviceInfoHandler;
import com.wisdom.jsinterfacelib.handler.ExistApiHandler;
import com.wisdom.jsinterfacelib.handler.ExistApisHandler;
import com.wisdom.jsinterfacelib.handler.FaceRecognitionHandler;
import com.wisdom.jsinterfacelib.handler.FingerPrintCompareHandler;
import com.wisdom.jsinterfacelib.handler.GetTokenHandler;
import com.wisdom.jsinterfacelib.handler.HookNavigationBackActionHandler;
import com.wisdom.jsinterfacelib.handler.ImgePreviewHandler;
import com.wisdom.jsinterfacelib.handler.LocationHandler;
import com.wisdom.jsinterfacelib.handler.NavigationBarTitleHandler;
import com.wisdom.jsinterfacelib.handler.NetworkStatusHandler;
import com.wisdom.jsinterfacelib.handler.OpenContactHandler;
import com.wisdom.jsinterfacelib.handler.OpenNewWebViewHandler;
import com.wisdom.jsinterfacelib.handler.PhoneCallHandler;
import com.wisdom.jsinterfacelib.handler.PlayVideoHandler;
import com.wisdom.jsinterfacelib.handler.SetTokenHandler;
import com.wisdom.jsinterfacelib.handler.SignPadHandler;
import com.wisdom.jsinterfacelib.handler.ToastHandler;
import com.wisdom.jsinterfacelib.handler.ToggleNavigationBarHandler;
import com.wisdom.jsinterfacelib.handler.UpLoadFileHandler;
import com.wisdom.jsinterfacelib.handler.WebViewCloseHandler;
import com.wisdom.jsinterfacelib.handler.WebViewGoBackHandler;
import com.wisdom.jsinterfacelib.utils.JsInterfaceBridge;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author HanXueFeng
 * @ProjectName project： jssdk_basic
 * @class package：com.wisdom.jsinterfacelib.utils
 * @class describe：
 * @time 2021/7/17 0017 12:31
 * @change
 */
public class InitJsSDKUtil {

    /**
     * 初始化jssdk方法，在主项目的Application中进行调用
     *
     * @param application
     */
    public static void init(Application application,Boolean initOkGoOrNot) {
        Bridge.INSTANCE.openLog();
        Map<String, BridgeHandler> bridgeHandlerMap = new HashMap();
        bridgeHandlerMap.put("WISDOM.app.toast", new ToastHandler());
        bridgeHandlerMap.put("WISDOM.app.chooseImage", new ChooseImageHandler());
        bridgeHandlerMap.put("WISDOM.app.isExistApi", new ExistApiHandler());
        bridgeHandlerMap.put("WISDOM.app.isExistApis", new ExistApisHandler());
        bridgeHandlerMap.put("WISDOM.app.toggleNavigationBar", new ToggleNavigationBarHandler());
        bridgeHandlerMap.put("WISDOM.app.setNavigationBarTitle", new NavigationBarTitleHandler());
        bridgeHandlerMap.put("WISDOM.app.chooseVideo", new ChooseVideoHandler());
        bridgeHandlerMap.put("WISDOM.app.phoneCall", new PhoneCallHandler());
        bridgeHandlerMap.put("WISDOM.app.getAppVersion", new AppVersionHandler());
        bridgeHandlerMap.put("WISDOM.app.previewImages", new ImgePreviewHandler());
        bridgeHandlerMap.put("WISDOM.app.playVideo", new PlayVideoHandler());
        bridgeHandlerMap.put("WISDOM.app.authID", new FingerPrintCompareHandler());
        bridgeHandlerMap.put("WISDOM.app.signature", new SignPadHandler());
        bridgeHandlerMap.put("WISDOM.app.contact", new OpenContactHandler());
        bridgeHandlerMap.put("WISDOM.app.getGpsInfo", new LocationHandler());
        bridgeHandlerMap.put("WISDOM.app.getDeviceInfo", new DeviceInfoHandler());
        bridgeHandlerMap.put("WISDOM.app.networkStatus", new NetworkStatusHandler());
        bridgeHandlerMap.put("WISDOM.app.goback", new WebViewGoBackHandler());
        bridgeHandlerMap.put("WISDOM.app.openNewWebView", new OpenNewWebViewHandler());
        bridgeHandlerMap.put("WISDOM.app.close", new WebViewCloseHandler());
        bridgeHandlerMap.put("WISDOM.app.setToken", new SetTokenHandler());
        bridgeHandlerMap.put("WISDOM.app.getToken", new GetTokenHandler());
        bridgeHandlerMap.put("WISDOM.app.mnnFaceVerification", new FaceRecognitionHandler());
        bridgeHandlerMap.put("WISDOM.app.recordVideo", new CameraCustomHandler());
        bridgeHandlerMap.put("WISDOM.app.uploadFiles", new UpLoadFileHandler());
        bridgeHandlerMap.put("WISDOM.app.hookNavigationBackAction", new HookNavigationBackActionHandler());
        JsInterfaceBridge.init(application, bridgeHandlerMap);
        //初始化okgo
        if (initOkGoOrNot) {
            initOkGo(application);
        }
    }
    public static void init(Application application, Boolean initOkGoOrNot, JSCallInterface JSCallInterface) {
        Bridge.INSTANCE.openLog();
        Map<String, BridgeHandler> bridgeHandlerMap = new HashMap();
        bridgeHandlerMap.put("WISDOM.app.toast", new ToastHandler());
        bridgeHandlerMap.put("WISDOM.app.chooseImage", new ChooseImageHandler());
        bridgeHandlerMap.put("WISDOM.app.isExistApi", new ExistApiHandler());
        bridgeHandlerMap.put("WISDOM.app.isExistApis", new ExistApisHandler());
        bridgeHandlerMap.put("WISDOM.app.toggleNavigationBar", new ToggleNavigationBarHandler());
        bridgeHandlerMap.put("WISDOM.app.setNavigationBarTitle", new NavigationBarTitleHandler());
        bridgeHandlerMap.put("WISDOM.app.chooseVideo", new ChooseVideoHandler());
        bridgeHandlerMap.put("WISDOM.app.phoneCall", new PhoneCallHandler());
        bridgeHandlerMap.put("WISDOM.app.getAppVersion", new AppVersionHandler());
        bridgeHandlerMap.put("WISDOM.app.previewImages", new ImgePreviewHandler());
        bridgeHandlerMap.put("WISDOM.app.playVideo", new PlayVideoHandler());
        bridgeHandlerMap.put("WISDOM.app.authID", new FingerPrintCompareHandler());
        bridgeHandlerMap.put("WISDOM.app.signature", new SignPadHandler());
        bridgeHandlerMap.put("WISDOM.app.contact", new OpenContactHandler());
        bridgeHandlerMap.put("WISDOM.app.getGpsInfo", new LocationHandler());
        bridgeHandlerMap.put("WISDOM.app.getDeviceInfo", new DeviceInfoHandler());
        bridgeHandlerMap.put("WISDOM.app.networkStatus", new NetworkStatusHandler());
        bridgeHandlerMap.put("WISDOM.app.goback", new WebViewGoBackHandler());
        bridgeHandlerMap.put("WISDOM.app.openNewWebView", new OpenNewWebViewHandler());
        bridgeHandlerMap.put("WISDOM.app.close", new WebViewCloseHandler());
        bridgeHandlerMap.put("WISDOM.app.setToken", new SetTokenHandler());
        bridgeHandlerMap.put("WISDOM.app.getToken", new GetTokenHandler());
        bridgeHandlerMap.put("WISDOM.app.mnnFaceVerification", new FaceRecognitionHandler());
        bridgeHandlerMap.put("WISDOM.app.recordVideo", new CameraCustomHandler());
        bridgeHandlerMap.put("WISDOM.app.uploadFiles", new UpLoadFileHandler());
        bridgeHandlerMap.put("WISDOM.app.hookNavigationBackAction", new HookNavigationBackActionHandler());
        bridgeHandlerMap.put("WISDOM.app.InvokeMethod", new InvokeMethodHandler(JSCallInterface));
        JsInterfaceBridge.init(application, bridgeHandlerMap);
        //初始化okgo
        if (initOkGoOrNot) {
            initOkGo(application);
        }
    }
    public static void init(Application application,Boolean initOkGoOrNot,String actionBarRGB) {
        Bridge.INSTANCE.openLog();
        ConstantString.ACTION_BAR_RGB=actionBarRGB;
        Map<String, BridgeHandler> bridgeHandlerMap = new HashMap();
        bridgeHandlerMap.put("WISDOM.app.toast", new ToastHandler());
        bridgeHandlerMap.put("WISDOM.app.chooseImage", new ChooseImageHandler());
        bridgeHandlerMap.put("WISDOM.app.isExistApi", new ExistApiHandler());
        bridgeHandlerMap.put("WISDOM.app.isExistApis", new ExistApisHandler());
        bridgeHandlerMap.put("WISDOM.app.toggleNavigationBar", new ToggleNavigationBarHandler());
        bridgeHandlerMap.put("WISDOM.app.setNavigationBarTitle", new NavigationBarTitleHandler());
        bridgeHandlerMap.put("WISDOM.app.chooseVideo", new ChooseVideoHandler());
        bridgeHandlerMap.put("WISDOM.app.phoneCall", new PhoneCallHandler());
        bridgeHandlerMap.put("WISDOM.app.getAppVersion", new AppVersionHandler());
        bridgeHandlerMap.put("WISDOM.app.previewImages", new ImgePreviewHandler());
        bridgeHandlerMap.put("WISDOM.app.playVideo", new PlayVideoHandler());
        bridgeHandlerMap.put("WISDOM.app.authID", new FingerPrintCompareHandler());
        bridgeHandlerMap.put("WISDOM.app.signature", new SignPadHandler());
        bridgeHandlerMap.put("WISDOM.app.contact", new OpenContactHandler());
        bridgeHandlerMap.put("WISDOM.app.getGpsInfo", new LocationHandler());
        bridgeHandlerMap.put("WISDOM.app.getDeviceInfo", new DeviceInfoHandler());
        bridgeHandlerMap.put("WISDOM.app.networkStatus", new NetworkStatusHandler());
        bridgeHandlerMap.put("WISDOM.app.goback", new WebViewGoBackHandler());
        bridgeHandlerMap.put("WISDOM.app.openNewWebView", new OpenNewWebViewHandler());
        bridgeHandlerMap.put("WISDOM.app.close", new WebViewCloseHandler());
        bridgeHandlerMap.put("WISDOM.app.setToken", new SetTokenHandler());
        bridgeHandlerMap.put("WISDOM.app.getToken", new GetTokenHandler());
        bridgeHandlerMap.put("WISDOM.app.mnnFaceVerification", new FaceRecognitionHandler());
        bridgeHandlerMap.put("WISDOM.app.recordVideo", new CameraCustomHandler());
        bridgeHandlerMap.put("WISDOM.app.uploadFiles", new UpLoadFileHandler());
        JsInterfaceBridge.init(application, bridgeHandlerMap);
        //初始化okgo
        if (initOkGoOrNot) {
            initOkGo(application);
        }
    }

    /**
    private static void initOkGo(Application application) {
        OkGo.getInstance().init(application);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        if (Common.isDebug) {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
////log打印级别，决定了log显示的详细程度
//            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
////log颜色级别，决定了log在控制台显示的颜色
//            loggingInterceptor.setColorLevel(Level.INFO);
//            builder.addInterceptor(loggingInterceptor);
//        }
//全局的读取超时时间
        builder.readTimeout(15000, TimeUnit.MILLISECONDS);
//全局的写入超时时间
        builder.writeTimeout(15000, TimeUnit.MILLISECONDS);
//全局的连接超时时间
        builder.connectTimeout(15000, TimeUnit.MILLISECONDS);
        OkGo.getInstance().init(application)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(2);                          //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params);                       //全局公共参数
    }
***/

    private static void initOkGo(Application application) {
        OkGo.init(application);
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()
                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    .debug("OkGo", Level.INFO, true)
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)//全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)

                    //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
//              .setCookieStore(new MemoryCookieStore())            //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore())        //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置
                    .setCertificates();                                //方法一：信任所有证书,不安全有风险
//              .setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
//              .setCertificates(getAssets().open("srca.cer"))      //方法三：使用预埋证书，校验服务端证书（自签名证书）
//              //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//               .setCertificates(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"))//

            //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//               .setHostnameVerifier(new SafeHostnameVerifier())

            //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

            //这两行同上，不需要就不要加入
//                    .addCommonHeaders(headers)  //设置全局公共头
//                    .addCommonParams(params);   //设置全局公共参数

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
