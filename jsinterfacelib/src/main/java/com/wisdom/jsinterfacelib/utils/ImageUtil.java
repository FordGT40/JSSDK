package com.wisdom.jsinterfacelib.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.wisdom.jsinterfacelib.face.CameraView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import Decoder.BASE64Decoder;

/**
 * @author HanXueFeng
 * @ProjectName project： hrbzwt
 * @class package：com.wisdom.hrbzwt.util
 * @class describe：
 * @time 2019/5/30 15:38
 * @change
 */
public class ImageUtil {

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {

        int i;

        int j;

        if (bmp.getHeight() > bmp.getWidth()) {

            i = bmp.getWidth();

            j = bmp.getWidth();

        } else {

            i = bmp.getHeight();

            j = bmp.getHeight();

        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);

        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {

            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);

            if (needRecycle)

                bmp.recycle();

            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();

            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,

                    localByteArrayOutputStream);

            localBitmap.recycle();

            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();

            try {

                localByteArrayOutputStream.close();

                return arrayOfByte;

            } catch (Exception e) {

                // F.out(e);

            }

            i = bmp.getHeight();

            j = bmp.getHeight();

        }

    }


    public static byte[] bmpToByteArray(Bitmap bmp) {
        int bytes = bmp.getByteCount();

        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buf);

        byte[] byteArray = buf.array();
        return byteArray;
    }

    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
// 解密
            byte[] b = decoder.decodeBuffer(imgStr);
// 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * @return
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ImgeUtil", "getImageStr: " + e);
        }
        // 返回Base64编码过的字节数组字符串
        String result = Base64.encodeToString(data, Base64.NO_WRAP);
        return result;
    }

    /* 本地图片转换Base64的方法
     * @param imgPath
     */
    public static String ImageToBase64(Context context,String imgPath) {
        InputStream in = null;
        byte[] data = null;
// 读取图片字节数组
        try {
            if(imgPath.startsWith("content://")){
                in = new FileInputStream(getRealPathFromUri(context,imgPath));
            }else{
                in = new FileInputStream(imgPath);
            }

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
// 返回Base64编码过的字节数组字符串
        String result = Base64.encodeToString(data, Base64.NO_WRAP);
        return result;
    }


    /**
     * 查询内容解析器，找到文件存储地址

     */
    public static String getRealPathFromUri(Context context, String myImageUrl) {
        Uri contentUri = Uri.parse(myImageUrl);
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    /* 本地图片转换Base64的方法
     * @param imgPath
     */
    public static String ImageToBase64(File imgFile) {
        InputStream in = null;
        byte[] data = null;
// 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
// 返回Base64编码过的字节数组字符串
        String result = Base64.encodeToString(data, Base64.NO_WRAP);
        return result;
    }
    public static String ImageToBase64Compress(Context context,String imgPath) {
        InputStream in = null;
        byte[] data = null;
// 读取图片字节数组
        try {
            in = new FileInputStream(saveMyBitmapForCompress(context,imgPath));
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
// 返回Base64编码过的字节数组字符串
        String result = Base64.encodeToString(data, Base64.NO_WRAP);
        return result;
    }


    /*
     * bitmap转base64
     * */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //暂时注释掉，扫脸登录之后保存照片到本地的操作
//        try {
//            Boolean isSuccess=saveMyBitmap(bitmap,"a.jpg");
//            Log.i("****", "bitmapToBase64: "+isSuccess);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return result;
    }

    public static boolean saveMyBitmap(Context context,Bitmap bmp, String bitName) throws IOException {
        boolean flag = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

// 获得存储卡的路径
            String sdpath = context.getExternalCacheDir() + "/";
            File f = new File(sdpath, bitName);
            f.createNewFile();
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                flag = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fOut.flush();
                fOut.close();
                compress(context,2,bitName);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return flag;
    }

    /**
     * 保存图片到本地，并且返回保存地址
     * @param context
     * @param bmp
     * @param bitName
     * @return
     * @throws IOException
     */
    public static String saveMyBitmapLocal(Context context,Bitmap bmp, String bitName,int compressSize) throws IOException {
        boolean flag = false;

// 获得存储卡的路径
            String sdpath = context.getExternalCacheDir() + "/";
            File f = new File(sdpath, bitName);
            f.createNewFile();
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                flag = true;
                fOut.flush();
                fOut.close();
                compress(context,compressSize,bitName);
            } catch (Exception e) {
                e.printStackTrace();
            }

        return f.getAbsolutePath();
    }


    public static String saveMyBitmapForCompress(Context context,String path) throws IOException {
        boolean flag = false;
        String pathResult="";
        String sdpath =context.getExternalCacheDir() + "/";
        File f = new File(sdpath, "Compress.jpg");
//        if (Environment.getExternalStorageState().equals(Environment.DIRECTORY_PICTURES)) {

// 获得存储卡的路径
            Bitmap bmp=BitmapFactory.decodeFile(path);
            f.createNewFile();
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                flag = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fOut.flush();
                fOut.close();
                pathResult=compress(context,2,"Compress.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }


//        }
        if (flag) {
            if (!pathResult.equals("")) {
                return pathResult;
            } else {
                return path;
            }
        } else {
            return path;
        }
    }


    /**
     * 将view转换为图片
     * @param v
     * @return
     */
    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }


    /**
     * 定制方法，获得人脸识别后的图片 （扫脸登录专用）
     * @param mCameraView
     * @param data
     * @return
     */
//    public static Bitmap getBitmapView(CameraView mCameraView, byte[] data) {
//        //获取bitmap
//        Camera.Size previewSize = mCameraView.getPreviewSize();//获取尺寸,格式转换的时候要用到
//        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        newOpts.inJustDecodeBounds = true;
//        YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height,
//                null);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高
//        byte[] rawImage = baos.toByteArray();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//        return BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
//    }


    /**
     * 旋转变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    public  static Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }


    /**
     *采样率压缩
     *
     1、inSampleSize小于等于1会按照1处理

     2、inSampleSize只能设置为2的平方，不是2的平方则最终会减小到最近的2的平方数，如设置7会按4进行压缩，设置15会按8进行压缩。

     * @param inSampleSize  可以根据需求计算出合理的inSampleSize
     */
    public static String compress(Context context,int inSampleSize, String fileName) {
        File sdFile = context.getExternalCacheDir();
        File originFile = new File(sdFile, fileName);
        try {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置此参数是仅仅读取图片的宽高到options中，不会将整张图片读到内存中，防止oom
        String path="";
        if("".equals(originFile.getAbsolutePath()) && originFile.getAbsolutePath()!=null){
            path=originFile.getPath();
        }else{
            path=originFile.getAbsolutePath();
        }
        options.inJustDecodeBounds = true;
        Bitmap emptyBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap resultBitmap = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            FileOutputStream fos = new FileOutputStream(new File(sdFile, fileName));
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(sdFile, fileName).getPath();
    }

    /**
     * 文件转Base64.
     * @param filePath
     * @return
     */
    public static String file2Base64(Context context,String filePath) {
        FileInputStream objFileIS = null;
        try {
            if(filePath.startsWith("content://")){
                objFileIS = new FileInputStream(getRealPathFromUri(context,filePath));
            }else{
                objFileIS = new FileInputStream(filePath);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream objByteArrayOS = new ByteArrayOutputStream();
        byte[] byteBufferString = new byte[1024];
        try {
            for (int readNum; (readNum = objFileIS.read(byteBufferString)) != -1; ) {
                objByteArrayOS.write(byteBufferString, 0, readNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String videodata = Base64.encodeToString(objByteArrayOS.toByteArray(), Base64.DEFAULT);
        return videodata;
    }





    //拍照后图片旋转角度相关内容
    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 根据uri获取bitmap
     * @param mContext
     * @param uri
     * @return
     */
    public static Bitmap getBitmapFromUri(Context mContext, Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return bmp;
    }

    /**
     * 定制方法，获得人脸识别后的图片 （扫脸登录专用）
     * @param mCameraView
     * @param data
     * @return
     */
    public static Bitmap getBitmapView(CameraView mCameraView, byte[] data) {
        //获取bitmap
        Camera.Size previewSize = mCameraView.getPreviewSize();//获取尺寸,格式转换的时候要用到
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height,
                null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高
        byte[] rawImage = baos.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
    }
}

