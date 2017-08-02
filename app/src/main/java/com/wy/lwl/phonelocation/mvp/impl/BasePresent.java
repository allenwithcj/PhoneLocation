package com.wy.lwl.phonelocation.mvp.impl;

import android.content.Context;

/**
 * Created by lwl on 2017/8/1.
 */

public class BasePresent {
    Context mcontext;
    public void onCreate(Context context){
        mcontext = context;
    };
    public void onPause(){};
    public void onResume(){};
    public void onDestory(Context context){
        mcontext =context;
    };
}
