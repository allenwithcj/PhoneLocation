package com.wy.lwl.phonelocation.business;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lwl on 2017/8/1.
 */

public class HttpUtil {
    String mUrl;
    Map<String,String> mParam;
    HttpResponse mHttpRespones;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    Handler mHandler = new Handler(Looper.getMainLooper());


    public HttpUtil(HttpResponse mHttpRespones) {
        this.mHttpRespones = mHttpRespones;
    }

    public interface HttpResponse{
        void onSuccess(Object object);
        void onfail(String error);
    }
    public void sendPostHttp(String url, Map<String,String> param){
        sendHttp(url,param,true);
    }
    public void sendGetHttp(String url, Map<String,String> param){
        sendHttp(url,param,false);
    }
    public void sendHttp(String url, Map<String,String> param,boolean isPost){
        mUrl = url;
        mParam =param;
        run(isPost);
    }
    private void run(boolean isPost){
        Request request = creatRequest(isPost);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(mHttpRespones != null){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHttpRespones.onfail("请求错误");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response){
                if(mHttpRespones == null)return;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(response.isSuccessful()){
                                try {
                                    mHttpRespones.onSuccess(response.body().string());
                                }catch (Exception e){
                                    e.printStackTrace();
                                    mHttpRespones.onfail("结果转换失败");
                                }
                            }else{
                                mHttpRespones.onfail("请求失败code:"+response);
                            }
                        }
                    });

            }
        });
    }

    private Request creatRequest(boolean isPost){
        Request request;
        if(isPost){
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
            requestBodyBuilder.setType(MultipartBody.FORM);
            Iterator<Map.Entry<String,String>> iterator = mParam.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String,String> entry = iterator.next();
                requestBodyBuilder.addFormDataPart(entry.getKey(),entry.getValue());
            }
            request = new Request.Builder().url(mUrl).post(requestBodyBuilder.build()).build();
        }else{
            String urlStr = mUrl+"?"+MapParamToString(mParam);
            request = new Request.Builder().url(urlStr).build();
        }
        return request;
    }

    private String MapParamToString(Map<String,String> param){
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String,String>>iterator = param.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String>entry = iterator.next();
            sb.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        String str = sb.toString().substring(0,sb.length()-1);
        return str;
    }

}
