package com.supcon.mes.module_lims.utils;

import java.math.BigDecimal;
import java.util.List;

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

    public static boolean isNumeric(String str){
        try {
            Double.parseDouble(str);
        }catch (Exception e){
            return false;
        }
      return true;
    }

    /**
     * 传入一个数列x计算平均值
     * @param x
     * @return 平均值
     */
    public static double average(List<Double> x) {
        int n = x.size();            //数列元素个数
        double sum = 0;
        for (double i : x) {        //求和
            sum+=i;
        }
        return sum/n;
    }

    /**
     * 传入一个数列x计算方差
     * 方差s^2=[（x1-x）^2+（x2-x）^2+......（xn-x）^2]/（n）（x为平均数）
     * @param x 要计算的数列
     * @return 方差
     */
    public static double variance(List<Double> x) {
        int n = x.size();            //数列元素个数
        double avg = average(x);    //求平均值
        double var = 0;
        for (double i : x) {
            var += (i-avg)*(i-avg);    //（x1-x）^2+（x2-x）^2+......（xn-x）^2
        }
        return var/n;
    }

    /**
     * 传入一个数列x计算标准差
     * 标准差σ=sqrt(s^2)，即标准差=方差的平方根
     * @param x 要计算的数列
     * @return 标准差
     */
    public static double standardDiviation(List<Double> x) {
        return  Math.sqrt(variance(x));
    }

    public static double getAvg(List<Double> list){
        double result=0d;
        if (list.size()==0){
            return result;
        }
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
        }

        return result/list.size();
    }

    public static double getSum(List<Double> list){
        double result = 0d;

        if (list.size()==0){
            return result;
        }

        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
        }
        return result;
    }

    public static double getMax(List<Double> list){
        double result = 0d;

        if (list.size()==0){
            return result;
        }else {
            result = list.get(0);
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > result){
                result = list.get(i);
            }

        }
        return result;
    }

    public static double getMin(List<Double> list){
        double result = 0d;

        if (list.size()==0){
            return result;
        }else {
            result = list.get(0);
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < result){
                result = list.get(i);
            }

        }
        return result;
    }

}
