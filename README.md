# JSSDK
jsSDK最新版本：[![](https://jitpack.io/v/FordGT40/JSSDK.svg)](https://jitpack.io/#FordGT40/JSSDK)

使用前需要在Application中进行sdk的初始化
InitJsSDKUtil.init(this);


为js调用原生功能提供支持，目前支持的原生功能有：
1.选择图片（相机拍摄或者相册）
2.选择视频（相机拍摄或者相册）
3.获取App版本号
4.获取设备信息（App名字，版本号，设备厂商等等，具体见js文档）
5.断当前客户端版本是否支持指定 JavaScript 接口（单个）
6.判断当前客户端版本是否支持指定 JavaScript 接口（多个）
7.阿里人脸识别功能（判断是不是活体人脸，如果是，返回base64）
8.指纹识别
9.获取App的Token
10.预览图片
11.获取当前位置GPS（当前位置信息）
12.设置导航栏标题
13.获取网络状态
14.打开通讯录
15.跳转至新 WebView 并加载
16.打电话
17.播放视频
18.设置token
19.签字板（签字后返回签名图片的base64）
20.提示弹窗（toast和dialog）
21.是否隐藏导航栏
22.退出当前 WebView
23.返回上一页面（当网页在第一页时，会直接退出当前控制器，其他则是返回上一个网页）


注：有些功能是扩展包提供的，比如获取区域id，扫身份证等。
![图片](https://user-images.githubusercontent.com/9582221/128480369-5c354023-3026-4027-94ca-8d7b073b60fa.png)

