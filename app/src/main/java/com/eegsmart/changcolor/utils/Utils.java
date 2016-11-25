package com.eegsmart.changcolor.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Xie on 2016/7/27.
 */
public class Utils {

    public static int getMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max)
                max = arr[i];
        }

        return max;
    }

    public static int getMin(int[] arr) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min)
                min = arr[i];
        }
        return min;
    }

    public static double getAvg(double[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum / arr.length;
    }

    public static double getVar(double[] arr, double avg) {
        double sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += Math.pow(arr[i] - avg, 2);
        }
        return sum / arr.length;
    }

    public static double getFront35(double[] arr) {
        float sum = 0;
        for (int i = 0; i < 35; i++) {
            sum += Math.pow(arr[i], 2);
        }
        return sum;
    }

    public static double getFront4(double[] arr) {
        double sum = 0;
        for (int i = 0; i < 4; i++) {
            sum += Math.pow(arr[i], 2);
        }
        return sum;
    }


    public static double getFront(double[] arr, int start, int end) {
        double sum = 0;
        for (int i = 0; i < end; i++) {
            if (i >= start - 1) {
                sum += Math.pow(arr[i], 2);
            }
        }
        return sum;
    }

    //是否连接WIFI
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 验证手机号码格式
     */
    public static boolean verifyMobileNO(String phoneNumber) {

        String verify = "[1][0-9]\\d{9}";
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        } else {
            return phoneNumber.matches(verify);
        }
    }

    /**
     * 验证验证码
     */
    public static boolean verifyCode(String verifyCode) {

        String verify = "[0-9][0-9]\\d{5}";
        if (TextUtils.isEmpty(verifyCode))
            return false;
        else
            return true;//verifyCode.matches(verify);
    }

    /**
     * 验证内容是否为空
     */
    public static boolean isNull(String content) {
        if (TextUtils.isEmpty(content)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern
                .compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            Matcher mc = pattern.matcher(email);
            return mc.matches();
        }
    }

    /**
     * 验证密码格式
     */
    public static boolean isValidPassWord(String passWord) {
        String verify = "^[0-9a-zA-Z]{6,20}$";
        if (TextUtils.isEmpty(passWord)) {
            return false;
        } else {
            return passWord.matches(verify);
        }
    }

    /**
     * 获取apk的版本名 appVersionName
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        String appVersionName = "0.1";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch blockd
            e.printStackTrace();
        }
        return appVersionName;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 数组里面是否含有连续的10个0
     *
     * @param data
     * @return
     */
    public static boolean isContinuous0(int[] data) {
        boolean isContinuous = false;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                isContinuous = true;
                for (int j = i; j < i + 10; j++) {
                    if (i + 9 >= data.length || data[j] != 0) {
                        isContinuous = false;
                        break;
                    }
                }
                if (isContinuous) {
                    break;
                }
            }
        }
        return isContinuous;
    }

    /**
     * 数组里面含有两个连续11改为10
     *
     * @param data
     * @return
     */
    public static int[] transe11To10(int[] eye) {
        for (int i = 0; i < eye.length - 1; i++) {
            if (eye[i] == 1 && eye[i + 1] == 1) {
                eye[i + 1] = 0;
            }
        }
        return eye;
    }


    /**
     * 获取6个（三分钟）数据 里面的最多类型数
     * @param arr
     * @return
     */
    public static int getTypeMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max){
                max = arr[i];
            }else if(arr[i] == max && max !=0){
                return -1;
            }
        }
        return max;
    }
}
