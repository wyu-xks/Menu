package com.eegsmart.changcolor;

import android.app.Application;
import android.content.Context;

import java.io.File;


/**
 * 创建者     xks
 * 创建时间   2016/7/21 10:27
 * 描述	      ${TODO}
 */
public class ChangColor extends Application {

    private static final String TAG = ChangColor.class.getSimpleName();
    private static ChangColor app;
    private static Context sContext;
    private File file;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        sContext = getApplicationContext();
    }

    public static ChangColor getInstance() {
        return app;
    }

    public  void setSaveFile(File file){
        this.file = file;
    }

    public  File getSaveFile(){
        return file;
    }

}
