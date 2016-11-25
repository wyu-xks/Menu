package eegsmart.jnitest;

/**
 * Created by Xie on 2016/9/21.
 */
public class JNIUtils {
    static {
        System.loadLibrary("sleepalgo-jni");
    }
    public native static String sleepalgo(int a , char[] paths);
}
