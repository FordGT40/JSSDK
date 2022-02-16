package com.wisdom.jsinterfacelib.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author HanXueFeng
 * @ProjectName project： HITADOA
 * @class package：com.wisdom.hitadoa.utils
 * @class describe：
 * @time 2020/7/24 0024 08:53
 * @change
 */
public class FileUtilCustom {


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        }
        else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 检查文件是否存在
     */
    public static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }
    public static int getFileLength(File file) {
        FileInputStream fis = null;
        int length=0;
        try {
            if (file!=null) {
                if (file.exists() && file.isFile()) {
                    fis = new FileInputStream(file);
                    length=fis.available();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  length;
    }

    /**
     * 删除List中重复元素，并保持顺序
     * @param list 待去重的list
     * @return 去重后的list
     */
    public static <T> List<T> removeDuplicateKeepOrder(List<T> list){
        Set set = new HashSet();
        List<T> newList = new ArrayList<>();
        for (T element : list) {
            //t能添加进去就代表不是重复的元素
            if (set.add(element)) newList.add(element);
        }
        list.clear();
        list.addAll(newList);
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

        Map<Object,Boolean> seen = new ConcurrentHashMap<>();

//putIfAbsent方法添加键值对，如果map集合中没有该key对应的值，则直接添加，并返回null，如果已经存在对应的值，则依旧为原来的值。

//如果返回null表示添加数据成功(不重复)，不重复(null==null :TRUE)

        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;

    }
}