package com.wy.lwl.phonelocation.mvp.impl;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.wy.lwl.phonelocation.business.HttpUtil;
import com.wy.lwl.phonelocation.model.Phone;
import com.wy.lwl.phonelocation.mvp.MvpMainView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lwl on 2017/8/1.
 */

public class MvpPresent extends BasePresent{
    String mUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    MvpMainView mvpMainView;
    Phone mPhone;

    public MvpPresent(MvpMainView mvpMainView) {
        this.mvpMainView = mvpMainView;
    }
    public Phone getPhoneInfo(){
        return mPhone;
    }

     public void searchPhone(String phone){
         if(phone.length() != 11){
            mvpMainView.showToast("请输入正确的手机号码");
             return;
         }
         mvpMainView.showLoding();
         sendHttpRequest(phone);
     }
    private void sendHttpRequest(String phone){
        Map<String,String> map = new HashMap<>();
        map.put("tel",phone);
        HttpUtil httpUtil = new HttpUtil(new HttpUtil.HttpResponse() {
            @Override
            public void onSuccess(Object object) {
                String json = object.toString();
                int index = json.indexOf("{");
                json = json.substring(index,json.length());
                mPhone = parseModelWithFastJson(json);
                mvpMainView.hideLoading();
                mvpMainView.updateView();
            }

            @Override
            public void onfail(String error) {
                mvpMainView.showToast(error);
                mvpMainView.hideLoading();
            }
        });
        httpUtil.sendGetHttp(mUrl,map);
    }

    private Phone parseModelWithFastJson(String json){
        Phone phone = JSONObject.parseObject(json,Phone.class);
        return phone;
    }
}
