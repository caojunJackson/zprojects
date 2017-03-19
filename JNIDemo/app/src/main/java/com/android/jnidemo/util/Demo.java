package com.android.jnidemo.util;

/**
 * Created by root on 17-2-16.
 */

public class Demo {
    public String strDemo;
    public int res;

    private static Demo demo;

    private Demo(){}

    public static Demo getInstance(){
        if(demo == null){
            demo = new Demo();
        }
        return demo;
    }

    public int getRes() {
        return res;
    }

    public String getDemo() {
        return strDemo;
    }

    public void setDemo(String demo) {
        this.strDemo = demo;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
