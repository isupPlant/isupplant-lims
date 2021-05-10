package com.supcon.mes.module_lims.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.constant.LimsConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.supcon.mes.middleware.ui.view.AddFileListView.getDataColumn;
import static com.supcon.mes.module_lims.constant.LimsConstant.ReportState.REPORTED;
import static com.supcon.mes.module_lims.constant.LimsConstant.ReportState.REPORTING;
import static com.supcon.mes.module_lims.constant.LimsConstant.ReportState.TESTING;
import static com.supcon.mes.module_lims.constant.LimsConstant.ReportState.TEST_COMPLETE;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
public class Util implements LimsConstant.SampleState{
    /**
     * 浮点型保留两位小数
     *
     * @param d
     * @return
     */
    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static Intent openFile(Context context, String filePath) {

        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        String authority = context.getPackageName() + ".fileprovider";
        Uri fileUri = FileProvider.getUriForFile(context, authority, file);
        /* 取得扩展名 */
        String fileName = file.getName();
        String end = fileName.substring(file.getName().lastIndexOf(".") + 1,
                file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        Intent intent = null;
        if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            intent = getImageFileIntent(filePath, fileUri);
        } else if (end.equals("ppt")) {
            intent = getPptFileIntent(filePath, fileUri);
        } else if (end.equals("xls") || end.equals("xlsx") || end.equals("xml")) {
            intent = getExcelFileIntent(filePath, fileUri);
        } else if (end.equals("doc")) {
            intent = getWordFileIntent(filePath, fileUri);
        } else if (end.equals("docx")) {
            intent = getWordFileIntent(filePath, fileUri);
        } else if (end.equals("pdf")) {
            intent = getPdfFileIntent(filePath, fileUri);
        } else if (end.equals("chm")) {
            intent = getChmFileIntent(filePath, fileUri);
        } else if (end.equals("txt")) {
            intent = getTextFileIntent(filePath, fileUri);
        }
        if (end.equals("mp4")) {
            intent = getMp4FileIntent(filePath, fileUri);
        }
        return intent;
    }

    public static String getFileType(File file) {
        String fileName = file.getName();
        return fileName.substring(file.getName().lastIndexOf(".") + 1,
                file.getName().length()).toLowerCase();
    }


    // 播放音乐
    public static Intent openMusic(String filePath, Uri fileUri) {

        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        /* 取得扩展名 */
        String fileName = file.getName();
        String end = fileName.substring(file.getName().lastIndexOf(".") + 1,
                file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        Intent intent = null;
        intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(new File(filePath));
        Uri uri = uriString(filePath, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }


    private static Intent getMp4FileIntent(String param, Uri filUri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = uriString(param, filUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "video/mp4");
        return intent;
    }

    public static Intent getImageFileIntent(String param, Uri filUri) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(new File(param));
        Uri uri = uriString(param, filUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    // Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param, Uri filUri) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION );
        Uri uri = uriString(param, filUri);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param, Uri filUri) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = uriString(param, filUri);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param, Uri filUri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = uriString(param, filUri);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    // Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param, Uri filUri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = uriString(param, filUri);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    // Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, Uri filUri) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = uriString(param, filUri);
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    // Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param, Uri filUri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = uriString(param, filUri);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public static Uri uriString(String param, Uri filUri) {
        Uri uri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //data是file类型,忘了复制过来
            uri = filUri;
        } else {
            uri = Uri.fromFile(new File(param));
        }
        return uri;
    }

    public static String getPath(Context context, Uri uri) {

        final boolean isKitKat = true;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }


        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String big2(Float d) {
        if (d == null || d == 0) {
            return "";
        }
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        // 四舍五入,保留2位小数
        return d1.divide(d2, 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String big(Float d) {
        if (d == null || d == 0) {
            return "";
        }
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        // 四舍五入,保留2位小数
        return d1.divide(d2, 1, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 浮点型保留两位小数
     *
     * @param d
     * @return
     */
    public static String big3(Float d) {
        if (d == null || d == 0) {
            return "";
        }
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        // 四舍五入,保留2位小数
        return d1.divide(d2, 3, BigDecimal.ROUND_HALF_UP).toString();
    }


    public static boolean parseStrInRange(String number, String range) {
        if (TextUtils.isEmpty(number) && TextUtils.isEmpty(range))
            return false;
        try {
            char firstCh = range.charAt(0);
            char lastCh = range.charAt(range.length() - 1);
            double min, max;
            double rangeNumber = Double.parseDouble(number);
            min = Double.parseDouble(range.substring(1, range.indexOf(',')));
            max = Double.parseDouble(range.substring(range.indexOf(',') + 1, range.length() - 1));
            if (firstCh == '(' && lastCh == ')') {
                return rangeNumber > min && rangeNumber < max;
            } else if (firstCh == '(' && lastCh == ']') {
                return rangeNumber > min && rangeNumber <= max;
            } else if (firstCh == '[' && lastCh == ')') {
                return rangeNumber >= min && rangeNumber < max;
            } else if (firstCh == '[' && lastCh == ']') {
                return rangeNumber >= min && rangeNumber <= max;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean parseInRangeStr(String reportValue, String dispValue) {
        if (TextUtils.isEmpty(reportValue) && TextUtils.isEmpty(reportValue))
            return false;

        try {
            if (dispValue.contains(",")) {
                String strArr[] = dispValue.split(",");
                for (String str : strArr) {
                    if (str.equals(reportValue))
                        return true;
                }
            } else {
                return reportValue.equals(dispValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean isNumeric(String str) {
        try {
            if (str.equals("")){
                return true;
            }else {
                Double.parseDouble(str);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 传入一个数列x计算平均值
     *
     * @param x
     * @return 平均值
     */
    public static double average(List<Double> x) {
        int n = x.size();            //数列元素个数
        double sum = 0;
        for (double i : x) {        //求和
            sum += i;
        }
        return sum / n;
    }
    /**
     * 传入一个数列x计算平均值
     *
     * @param x
     * @return 平均值
     */
    public static double sum(List<Double> x) {
        int n = x.size();            //数列元素个数
        double sum = 0;
        for (double i : x) {        //求和
            sum += i;
        }
        return sum;
    }

    /**
     * 传入一个数列x计算方差
     * 方差s^2=[（x1-x）^2+（x2-x）^2+......（xn-x）^2]/（n）（x为平均数）
     *
     * @param x 要计算的数列
     * @return 方差
     */
    public static double variance(List<Double> x) {
        int n = x.size();            //数列元素个数
        double avg = average(x);    //求平均值
        double var = 0;
        for (double i : x) {
            var += (i - avg) * (i - avg);    //（x1-x）^2+（x2-x）^2+......（xn-x）^2
        }
        return var / n;
    }

    /**
     * 传入一个数列x计算标准差
     * 标准差σ=sqrt(s^2)，即标准差=方差的平方根
     *
     * @param x 要计算的数列
     * @return 标准差
     */
    public static double standardDiviation(List<Double> x) {
        return Math.sqrt(variance(x));
    }

    public static double getAvg(List<Double> list) {
        double result = 0d;
        if (list.size() == 0) {
            return result;
        }
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
        }

        return result / list.size();
    }

    public static double getSum(List<Double> list) {
        double result = 0d;

        if (list.size() == 0) {
            return result;
        }

        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
        }
        return result;
    }

    public static double getMax(List<Double> list) {
        double result = 0d;

        if (list.size() == 0) {
            return result;
        } else {
            result = list.get(0);
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > result) {
                result = list.get(i);
            }

        }
        return result;
    }

    public static double getMin(List<Double> list) {
        double result = 0d;

        if (list.size() == 0) {
            return result;
        } else {
            result = list.get(0);
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < result) {
                result = list.get(i);
            }

        }
        return result;
    }

    public static String getSampleState(String value){
        if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_wait_get_sample))){
            return NOT_COLLECTED;
        }else if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_wait_received_sample))){
            return NOT_RECEIVED;
        }else if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_wait_handover))){
            return NOT_HANDOVER;
        }else if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_wait_test))){
            return NOT_TESTED;
        }else if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_some_have_been_inspected))){
            return HALF_TESTED;
        }else if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_already_test))){
            return TESTED;
        }else if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_already_reviewed))){
            return CHECKED;
        }else if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_already_refuse))){
            return REFUSED;
        }else if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_already_cancel))){
            return CANCELED;
        }
        return "";
    }

    public static String getReportState(String value){
        if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_testing))){
            return TESTING;
        }else if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_already_test))){
            return TEST_COMPLETE;
        }else if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_report_formation))){
            return REPORTING;
        }else if (value.equals(SupPlantApplication.getAppContext().getResources().getString(R.string.lims_report_review))){
            return REPORTED;
        }
        return "";
    }

    public static ArrayList<Double> getDoubleArrFromStringArr(List<String> strings) {
        ArrayList<Double> doubles = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            doubles.add(Double.valueOf(strings.get(i)));
        }
        return doubles;
    }
}
