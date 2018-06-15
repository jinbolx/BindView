package com.example.jinbolx.retrofitdemo;

import android.os.Environment;
import java.io.File;

public class FileUtil {

    public static String getSD(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
    public static String getAppPath(){
        return getSD()+ File.separator+ThisApplication.getInstance.getPackageName()+File.separator;
    }
}
