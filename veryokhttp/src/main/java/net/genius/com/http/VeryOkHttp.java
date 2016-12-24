package net.genius.com.http;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
public class VeryOkHttp {

    private Object obj;

    public VeryOkHttp(Object obj) {
        this.obj = obj;
    }

    public interface OnResponseListener {

        void onSuccess(String response, int id);

        void onError(Call call, Exception e, int id);
    }
    /**
     * 监听器列表
     */
    public List<OnResponseListener> listenerList = new ArrayList<>();
    /**
     * 普通post请求
     */
    public void post(String url,HttpParams params, int id, OnResponseListener listener) {
        //添加一个监听器
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        Map<String, String> paramsMap = null;//默认为null
        Map<String, String> headersMap = Collections.EMPTY_MAP;
        if (params != null) {
            paramsMap = params.getParams();
            for (Map.Entry<String, String> entrys : paramsMap.entrySet()) {
                builder.addParams(entrys.getKey(), entrys.getValue());
            }
            headersMap = params.getHeaders();
            if (headersMap.size() == 1) {
                Map.Entry<String, String> next = headersMap.entrySet().iterator().next();
                builder.addHeader(next.getKey(), next.getValue());
            }
        }
        builder.id(id).tag(obj).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                for (OnResponseListener mOnResponseListener : listenerList) {
                    mOnResponseListener.onError(call, e, id);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                for (OnResponseListener mOnResponseListener : listenerList) {
                    mOnResponseListener.onSuccess(response, id);
                }
            }
        });
    }

    /**
     * get请求
     */
    public void get(String url, HttpParams params,int id,OnResponseListener listener){
        //添加一个监听器
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }

        GetBuilder builder = OkHttpUtils.get().url(url);
        Map<String, String> paramsMap = null;//默认为null
        Map<String, String> headersMap = Collections.EMPTY_MAP;
        if (params != null) {
            paramsMap = params.getParams();
            for (Map.Entry<String, String> entrys : paramsMap.entrySet()) {
                builder.addParams(entrys.getKey(), entrys.getValue());
            }
            headersMap = params.getHeaders();
            if (headersMap.size() == 1) {
                Map.Entry<String, String> next = headersMap.entrySet().iterator().next();
                builder.addHeader(next.getKey(), next.getValue());
            }
        }
        builder.id(id).tag(obj).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                for (OnResponseListener mOnResponseListener : listenerList) {
                    mOnResponseListener.onError(call, e, id);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                for (OnResponseListener mOnResponseListener : listenerList) {
                    mOnResponseListener.onSuccess(response, id);
                }
            }
        });
    }

    /**
     * delete请求
     */
    public void delete(String url,HttpParams params,int id,OnResponseListener listener) {
        //添加一个监听器
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
        OtherRequestBuilder builder = OkHttpUtils.delete().url(url);
        Map<String, String> paramsMap = null;//默认为null
        Map<String, String> headersMap = Collections.EMPTY_MAP;
        if (params != null) {
            headersMap = params.getHeaders();
            if (headersMap.size() == 1) {
                Map.Entry<String, String> next = headersMap.entrySet().iterator().next();
                builder.addHeader(next.getKey(), next.getValue());
            }
        }
        builder.id(id).tag(obj).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                for (OnResponseListener mOnResponseListener : listenerList) {
                    mOnResponseListener.onError(call, e, id);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                for (OnResponseListener mOnResponseListener : listenerList) {
                    mOnResponseListener.onSuccess(response, id);
                }
            }
        });
    }
    /**
     * 取消被obj标记的所有请求
     */
    public void cancel(Object obj) {
        OkHttpUtils.getInstance().cancelTag(obj);
        //默认在取消请求的时候移除所有监听器
        listenerList.clear();
    }
}
