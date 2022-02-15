package com.wisdom.jsinterfacelib.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageInfo
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lzy.okgo.callback.StringCallback
import com.permissionx.guolindev.PermissionX.init
import com.wisdom.jsinterfacelib.R
import com.wisdom.jsinterfacelib.model.LocationInfoResultModel
import okhttp3.Call
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


/**
 * @author HanXueFeng
 * @ProjectName project： HITADOA
 * @class
 * @class describe：
 * 系统方法获取定位工具类，调用“getLocationData” ，
 * 从GPS定位与Net定位中择优选取结果，需要引入郭霖的permissionX：
 *  implementation 'com.permissionx.guolindev:permissionx:1.1.1'
 *  反地理编码请调用以下接口查询：
 *  http://restapi.amap.com/v3/geocode/regeo?key=d575350e55b289b9babea2a3b605cd8a&location=126.535224,45.726034&radius=1000&extensions=all&batch=false&roadlevel=0
 * @time 2021/1/5 0005 13:56
 * @change
 */
object LocationUtil {
    private val AMAP_WEB_KEY = "8da7935cbad9bf9e1cf53c32bd8da59a";
    private val TWO_MINUTES = 1000 * 60 * 2
    private var currentLocation: Location? = null

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getLocationData(activity: FragmentActivity?, listener: OnLocationCompleteListener) {
        init(activity!!)
                .permissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .explainReasonBeforeRequest()
                .onExplainRequestReason { scop, deniedList ->
                    listener.onLocationFail("获取定位权限失败")
                    scop.showRequestReasonDialog(
                            listOf(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            ), "获取位置信息需要授权获取定位权限", "允许", "拒绝"
                    )
                }
                .onForwardToSettings { scop, deniedList ->
                    listener.onLocationFail("获取定位权限失败，需手动开启定位权限")
                    scop.showForwardToSettingsDialog(
                            listOf(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            ), "您需要去应用程序设置当中手动开启权限", "去开启", "取消"
                    )

                }
                .request { allGranted, grantedList, deniedList ->
                    val map = if (allGranted) {
                        //开始定位
                        val locationManager =
                                activity.getSystemService(LOCATION_SERVICE) as LocationManager
                        val criteria = Criteria()
                        criteria.accuracy = Criteria.ACCURACY_FINE
                        criteria.isAltitudeRequired = false
                        criteria.isBearingRequired = true
                        criteria.isCostAllowed = true
                        criteria.isSpeedRequired = true
                        criteria.powerRequirement = Criteria.POWER_LOW
                        criteria.bearingAccuracy = Criteria.ACCURACY_HIGH
                        criteria.speedAccuracy = Criteria.ACCURACY_HIGH
                        criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
                        criteria.verticalAccuracy = Criteria.ACCURACY_HIGH
                        val bestProvider = locationManager.getBestProvider(criteria, true)
                        val locationListener = object : LocationListener {
                            override fun onLocationChanged(location: Location) {
                                currentLocation = location
                            }

                            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
                            override fun onProviderEnabled(s: String) {}
                            override fun onProviderDisabled(s: String) {}
                        }
                        locationManager.requestLocationUpdates(
                                bestProvider!!,
                                10.toLong(),
                                0.toFloat(),
                                locationListener
                        )
                        val gsLocation =
                                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        val netLocation =
                                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        var bestLocation = gsLocation
                        bestLocation = if (isBetterLocation(
                                        netLocation!!,
                                        bestLocation
                                )
                        ) netLocation else bestLocation
                        if (currentLocation != null) {
                            bestLocation = if (isBetterLocation(
                                            currentLocation!!,
                                            bestLocation
                                    )
                            ) currentLocation!! else bestLocation
                        }
                        with(bestLocation) {
//                        listener.onLocationSuccess(latitude, longitude)
                            locationConvert(this!!.longitude, latitude, listener)
                        }
                    } else {
                        listener.onLocationFail("您未授权获取位置信息权限，定位失败")
                        LogUtils.i("您拒绝了如下权限：$deniedList")
                    }

                }

    }


    /**
     *  @describe 将gps坐标转换成高德坐标
     *  @return
     *  @author HanXueFeng
     *  @time 2021/2/18 0018  10:23
     */
    private fun locationConvert(
            longitude: Double,
            latitude: Double,
            listener: OnLocationCompleteListener
    ) {
//https://restapi.amap.com/v3/geocode/regeo?location=126.694041883681,45.694532877605&key=d575350e55b289b9babea2a3b605cd8a
        //https://restapi.amap.com/v3/assistant/coordinate/convert?key=d575350e55b289b9babea2a3b605cd8a&locations=126.687954,45.69248&coordsys=gps
        var url = "https://restapi.amap.com/v3/assistant/coordinate/convert"
        val urlPara="${url}?key=d575350e55b289b9babea2a3b605cd8a&coordsys=gps&locations=$longitude,$latitude"

//        val params = mapOf(
//                Pair("key", "d575350e55b289b9babea2a3b605cd8a"),
//                Pair("coordsys", "gps"),
//                Pair("locations", "$longitude,$latitude")
//        )

//        val params=HashMap<String,String>().apply {
//            put("key", "d575350e55b289b9babea2a3b605cd8a")
//            put("coordsys", "gps")
//            put("locations", "$longitude,$latitude")
//        }



        /**
        OkGoController.create().get(url, params, object : StringCallback() {
            override fun onSuccess(t: String?, call: Call?, response: okhttp3.Response?) {
                try {
                    val json = JSONObject(t)
                    val status = json.optString("status")
                    if (status == "1") {
                        //坐标转换成功，将坐标回传给前台
                        val locations = json.optString("locations")
                        val locationPoint = locations.split(",")
                        listener.onLocationSuccess(
                            locationPoint[1].toDouble(),
                            locationPoint[0].toDouble()
                        )
                    } else {
                        //坐标转换失败
                        listener.onLocationFail("定位信息获取失败")
                    }
                } catch (exception: Exception) {
                    listener.onLocationFail("定位信息获取失败")
                }
            }
        })
        **/
        //实例化Handler对象，用于在子线程发送消息到主线程，并在主线程进行消息处理
        val SHOW_RESPONSE = 1001
        var handler: Handler? = object : Handler() {
            //handleMessage方法运行在主线程，处理子线程发送回来的数据。
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                when (msg.what) {
                    SHOW_RESPONSE -> {
                        val response = msg.obj as String


                    }
                    else -> {

                    }
                }
            }
        }



        //开启线程来发起网络请求
        Thread {
            var connection: HttpURLConnection? = null
            try {
                val url = URL(url)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                val `in`: InputStream = connection.inputStream
                //下面对获取到的输入流进行读取
                val reader = BufferedReader(InputStreamReader(`in`))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                //实例化Message对象
                val message = Message()
                message.what = SHOW_RESPONSE
                //将服务器返回的结果存放到Message中
                message.obj = response.toString()
                //sendMessage方法运行在子线程
                handler!!.sendMessage(message)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }.start()
    }


    /**
     *  @describe google 官方提供的方法，判别哪一种定位结果更准确
     *  @return
     *  @author HanXueFeng
     *  @time 2021/1/5 0005  16:04
     */
    private fun isBetterLocation(location: Location, currentBestLocation: Location?): Boolean {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true
        }
        // Check whether the new location fix is newer or older
        val timeDelta = location.time - currentBestLocation.time
        val isSignificantlyNewer = timeDelta > TWO_MINUTES
        val isSignificantlyOlder = timeDelta < -TWO_MINUTES
        val isNewer = timeDelta > 0
        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false
        }
        // Check whether the new location fix is more or less accurate
        val accuracyDelta = (location.accuracy - currentBestLocation.accuracy).toInt()
        val isLessAccurate = accuracyDelta > 0
        val isMoreAccurate = accuracyDelta < 0
        val isSignificantlyLessAccurate = accuracyDelta > 200
        // Check if the old and new location are from the same provider
        val isFromSameProvider = isSameProvider(
                location.provider,
                currentBestLocation.provider
        )
        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true
        } else if (isNewer && !isLessAccurate) {
            return true
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true
        }
        return false
    }


    /** Checks whether two providers are the same  */
    private fun isSameProvider(provider1: String?, provider2: String?): Boolean {
        return if (provider1 == null) {
            provider2 == null
        } else provider1 == provider2
    }


    /**
     *  @describe 当定位完成后，将定位结果回传给调用方法的监听
     *  @return
     *  @author HanXueFeng
     *  @time 2021/1/5 0005  16:35
     */
    interface OnLocationCompleteListener {
        fun onLocationFail(msg: String)
        fun onLocationSuccess(latitude: Double, longitude: Double)
    }

    /**
     *  @describe 通过返回一张地图图片响应HTTP请求，使用户能够将高德地图以图片形式嵌入自己的网页中
     *  zoom 地图级别 地图缩放级别:[1,17]
     *  size 地图大小 图片宽度*图片高度。最大值为1024*1024
     *  scale 普通/高清 1:返回普通图； 2:调用高清图，图片高度和宽度都增加一倍，zoom也增加一倍（当zoom为最大值时，zoom不再改变）。
     *  @return
     *  @author HanXueFeng
     *  @time 2021/1/14 0014  13:20
     */
    @JvmStatic
    fun getAmapImage(lat: String, lng: String, zoom: String, size: String, scale: String): String? {
        return "https://restapi.amap.com/v3/staticmap?scale=$scale&location=$lng,$lat&zoom=$zoom&size=$size&markers=mid,,A:$lng,$lat&key=${AMAP_WEB_KEY}"
    }


    /**
     * 检测地图应用是否安装
     *
     * @param context
     * @param packagename
     * @return
     */
    private fun checkMapAppsIsExist(context: Context, packagename: String?): Boolean {
        var packageInfo: PackageInfo?
        try {
            packageInfo = context.packageManager.getPackageInfo(packagename!!, 0)
        } catch (e: Exception) {
            packageInfo = null
            e.printStackTrace()
        }
        return packageInfo != null
    }

    /**
     * 打开高德地图（公交出行，起点位置使用地图当前位置）
     *
     * t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）
     *
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    @JvmStatic
    fun openGaoDeMap(context: Context, dlat: Double, dlon: Double, dname: String) {
        if (checkMapAppsIsExist(context, "com.autonavi.minimap")) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setPackage("com.autonavi.minimap")
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data = Uri.parse(
                    "androidamap://route?sourceApplication=" + R.string.app_name
                            .toString() + "&sname=我的位置&dlat=" + dlat
                            .toString() + "&dlon=" + dlon
                            .toString() + "&dname=" + dname + "&dev=0&m=0&t=1"
            )
            context.startActivity(intent)
        } else {
            ToastUtils.showShort("未安装高德地图")
        }
    }

    /* 打开百度地图（公交出行，起点位置使用地图当前位置）
    *
    * mode = transit（公交）、driving（驾车）、walking（步行）和riding（骑行）. 默认:driving
    * 当 mode=transit 时 ： sy = 0：推荐路线 、 2：少换乘 、 3：少步行 、 4：不坐地铁 、 5：时间短 、 6：地铁优先
    *
    * @param dlat  终点纬度
    * @param dlon  终点经度
    * @param dname 终点名称
    */
    @JvmStatic
    open fun openBaiduMap(context: Context, dlat: Double, dlon: Double, dname: String) {
        if (checkMapAppsIsExist(context, "com.baidu.BaiduMap")) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(
                    "baidumap://map/direction?origin=我的位置&destination=name:"
                            + dname
                            + "|latlng:" + dlat + "," + dlon
                            + "&mode=transit&sy=3&index=0&target=1"
            )
            context.startActivity(intent)
        } else {
            ToastUtils.showShort("未安装百度地图")
        }
    }

    /**
     * 打开腾讯地图（公交出行，起点位置使用地图当前位置）
     *
     * 公交：type=bus，policy有以下取值
     * 0：较快捷 、 1：少换乘 、 2：少步行 、 3：不坐地铁
     * 驾车：type=drive，policy有以下取值
     * 0：较快捷 、 1：无高速 、 2：距离短
     * policy的取值缺省为0
     *
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    @JvmStatic
    fun openTencent(context: Context, dlat: Double, dlon: Double, dname: String) {
        if (checkMapAppsIsExist(context, "com.tencent.map")) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(
                    "qqmap://map/routeplan?type=bus&from=我的位置&fromcoord=0,0"
                            + "&to=" + dname
                            + "&tocoord=" + dlat + "," + dlon
                            + "&policy=1&referer=myapp"
            )
            context.startActivity(intent)
        } else {
            ToastUtils.showShort("未安装腾讯地图")
        }
    }


    /**
     *  @describe 判断是否启用GPS
     *  @return
     *  @author HanXueFeng
     *  @time 2021/2/24 0024  18:01
     */
    fun hasGPSService(context: Context): Boolean {
        val alm = context.getSystemService(LOCATION_SERVICE) as LocationManager
        return alm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     *  @describe 根据经纬度获得地点信息
     *  @return
     *  @author HanXueFeng
     *  @time 2021/7/17 0017  09:28
     */
    @JvmStatic
    fun getLocationInfo(longitude: Double, latitude: Double, listener: OnLocationInfoGetListener) {
        val url = "http://restapi.amap.com/v3/geocode/regeo?key=d575350e55b289b9babea2a3b605cd8a&location=${longitude},${latitude}&radius=1000&extensions=all&batch=false&roadlevel=0"
        val map = HashMap<String, String>()
        OkGoController.create().get(url, map, object : StringCallback() {
            override fun onError(
                call: Call?,
                response: okhttp3.Response?,
                e: java.lang.Exception?
            ) {
                super.onError(call, response, e)
                listener.onLocationInfoGetFail("反地理编码失败")
            }

            override fun onSuccess(t: String?, call: Call?, response: okhttp3.Response?) {
                try {
                    val locationInfoModel=LocationInfoResultModel()
                    //解析高德返回的数据
                    val jsonObject = JSONObject(t)
                    val dataJson = jsonObject.optJSONObject("regeocode")
                    dataJson.apply {
                        locationInfoModel.address=optString("formatted_address")
                        locationInfoModel.latitude=latitude
                        locationInfoModel.longitude=longitude
                        optJSONObject("addressComponent").apply {
                            locationInfoModel.country = optString("country")
                            locationInfoModel.city = optString("city")
                            locationInfoModel.province = optString("province")
                            locationInfoModel.adcode = optString("adcode")
                            locationInfoModel.district = optString("district")
                            locationInfoModel.cityCode = optString("citycode")
                        }
                    }
                    listener.onLocationInfoGetSuccess(locationInfoModel)

                } catch (e: Exception) {
                    listener.onLocationInfoGetFail("反地理编码数据解析失败")
                }
            }
        })
    }

    interface OnLocationInfoGetListener {
        fun onLocationInfoGetSuccess(model: LocationInfoResultModel)
        fun onLocationInfoGetFail(msg: String)
    }


}