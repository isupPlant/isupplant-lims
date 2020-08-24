package com.supcon.mes.module_lims.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
public class Util {
    /**
     * 浮点型保留两位小数
     *
     * @param d
     * @return
     */
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

    public static boolean parseStrInRange(String number,String range){
        if (TextUtils.isEmpty(number) && TextUtils.isEmpty(range))
            return false;
        try {
            char firstCh=range.charAt(0);
            char lastCh=range.charAt(range.length()-1);
            double min,max;
            double rangeNumber=Double.parseDouble(number);
            min=Double.parseDouble(range.substring(1,range.indexOf(',')));
            max=Double.parseDouble(range.substring(range.indexOf(',')+1,range.length()-1));
            if (firstCh=='(' && lastCh==')'){
                return rangeNumber>min && rangeNumber<max;
            }else if (firstCh=='(' && lastCh==']'){
                return rangeNumber>min && rangeNumber<=max;
            }else if (firstCh=='[' && lastCh==')'){
                return rangeNumber>=min && rangeNumber<max;
            }else if (firstCh=='[' && lastCh==']'){
                return rangeNumber>=min && rangeNumber<=max;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean parseInRangeStr(String reportValue,String dispValue){
        if (TextUtils.isEmpty(reportValue) && TextUtils.isEmpty(reportValue))
            return false;

        try {
           if (dispValue.contains(",")){
               String strArr[]=dispValue.split(",");
               for(String str:strArr){
                   if (str.equals(reportValue))
                       return true;
               }
           }else {
               return reportValue.equals(dispValue);
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
