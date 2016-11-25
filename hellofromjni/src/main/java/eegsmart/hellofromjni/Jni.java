package eegsmart.hellofromjni;

/**
 * Created by Xie on 2016/9/21.
 */
public class Jni {
    static {
        System.loadLibrary("sleepalgo");
    }

    public native static String sleepAlgo(int a ,char[] paths);
}
