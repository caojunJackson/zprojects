package com.android.jnidemo.util;

/**
 * Created by root on 17-2-15.
 */

public class JniUtil {

    static{
        System.loadLibrary("helloworld");
    }


    public static native void print(); //直接调用jni

    public static native String getStringFromC(String string); //传入一个字串返回一个新的字串

    public static native int sumArray(int[] array);  //传入一维数组 计算和 返回 值

    public static native int [][] initInt2Darray(int size); //传入size 返回一个初始后的二维数组

    public static native void accessField(Demo demo); //访问字段

}
