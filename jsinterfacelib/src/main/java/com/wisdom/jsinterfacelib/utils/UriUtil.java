package com.wisdom.jsinterfacelib.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

/**
 * @author HanXueFeng
 * @ProjectName project： JsbridjeTest
 * @class package：com.wisdom.jsinterfacelib.utils.avoidonresult
 * @class describe：
 * @time 2021/7/14 0014 13:46
 * @change
 */
public class UriUtil {
    /**
     * 根据视频的Uri获取图片的绝对路径(适配多种API)
     *
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getVideoRealPathFromURI(Context context,Uri contentUri) {

        String res = null;String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);

        if(cursor.moveToFirst()){;

//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

            res = cursor.getString(column_index);

        }

        cursor.close();

        return res; }
    /**
     * 根据图片的Uri获取图片的绝对路径(适配多种API)
     *
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getImageRealPathFromURI(Context context,Uri contentUri) {

        String res = null;String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);

        if(cursor.moveToFirst()){;

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

            res = cursor.getString(column_index);

        }

        cursor.close();

        return res; }




}
