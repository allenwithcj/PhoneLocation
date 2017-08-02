package com.wy.lwl.phonelocation.mvp;

/**
 * Created by lwl on 2017/8/1.
 */

public interface MvpMainView extends MvpLoadingView{
    void showToast(String msg);
    void updateView();
}
