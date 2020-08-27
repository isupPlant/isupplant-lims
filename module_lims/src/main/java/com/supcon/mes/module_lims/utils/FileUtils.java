package com.supcon.mes.module_lims.utils;

import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by wanghaidong on 2020/8/26
 * Email:wanghaidong1@supcon.com
 */
public class FileUtils {
    public static boolean imageFile(File file){
       String end=getFileEnd(file);
       return (end.equals("jpg") || end.equals("gif") || end.equals("png")
               || end.equals("jpeg") || end.equals("bmp"));
    }

    public static boolean videoFile(File file){
        String end=getFileEnd(file);
        return "mp4".equals(end);
    }
    private static String getFileEnd(File file){
        String fileName = file.getName();
        return fileName.substring(file.getName().lastIndexOf(".") + 1,
                file.getName().length()).toLowerCase();
    }
    public static Uri uriString(String param, Uri filUri) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //data是file类型,忘了复制过来
            uri = filUri;
        } else {
            uri = Uri.fromFile(new File(param));
        }
        return uri;
    }
}
