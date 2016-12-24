package net.genius.com.http;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.HasParamsable;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

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
public class VeryOkHttp extends StringCallback {

    //Target
    private Object obj;

    public VeryOkHttp(Object obj) {
        this.obj = obj;
    }


    public interface OnResponseListener {
        /**
         * 请求成功
         * @param response
         * @param id
         */
        void onSuccess(String response, int id);
        /**
         * 请求失败
         */
        void onError(Call call, Exception e, int id);
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        synchronized (VeryOkHttp.class) {
            for (int i = 0; i < listenerList.size(); i++) {
                listenerList.get(i).onError(call,e,id);
            }
        }
    }

    @Override
    public void onResponse(String response, int id) {
        synchronized (VeryOkHttp.class) {
            for (int i = 0; i < listenerList.size(); i++) {
                listenerList.get(i).onSuccess(response,id);
            }
        }
    }

    /**
     * 监听器列表
     */
    private volatile List<OnResponseListener> listenerList = new ArrayList<>();

    /**
     * 添加一个监听
     * @param listener
     */
    public void registerResponseListener(OnResponseListener listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    /**
     * 普通post请求
     */
    public void post(String url, HttpParams params, int id, OnResponseListener listener) {
        //添加一个监听器
        registerResponseListener(listener);
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        builder = (PostFormBuilder) addHeaders(builder, params);
        builder = (PostFormBuilder) addParams(builder, params);
        builder.id(id).tag(obj).build().execute(this);
    }

    /**
     * get请求
     */
    public void get(String url, HttpParams params, int id, OnResponseListener listener) {
        //添加一个监听器
        registerResponseListener(listener);
        GetBuilder builder = OkHttpUtils.get().url(url);
        builder = (GetBuilder) addHeaders(builder, params);
        builder = (GetBuilder) addParams(builder, params);
        builder.id(id).tag(obj).build().execute(this);
    }

    /**
     * delete请求
     */
    public void delete(String url, HttpParams params, int id, OnResponseListener listener) {
        //添加一个监听器
        registerResponseListener(listener);
        OtherRequestBuilder builder = OkHttpUtils.delete().url(url);
        builder = (OtherRequestBuilder) addHeaders(builder, params);
        builder.id(id).tag(obj).build().execute(this);
    }

    /**
     * Json请求
     */
    public <T> void postJson(String url, HttpParams params, int id, T t, OnResponseListener listener) {
        //添加一个监听器
        registerResponseListener(listener);
        PostStringBuilder builder = OkHttpUtils
                .postString()
                .url(url);
        builder = (PostStringBuilder) addHeaders(builder, params);
        builder.content(new Gson().toJson(t))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .id(id)
                .tag(obj)
                .build()
                .execute(this);
    }

    /**
     * 取消被obj标记的所有请求
     */
    public void cancel(Object obj) {
        OkHttpUtils.getInstance().cancelTag(obj);
        //默认在取消请求的时候移除所有监听器
        listenerList.clear();
    }

    /**
     * 是否需要添加http头
     * @param builder
     * @param params
     */
    private OkHttpRequestBuilder addHeaders(OkHttpRequestBuilder builder, HttpParams params) {
        //添加http头
        Map<String, String> headersMap = Collections.EMPTY_MAP;
        if (params != null) {
            headersMap = params.getHeaders();
            if (headersMap.size() == 1) {
                Map.Entry<String, String> next = headersMap.entrySet().iterator().next();
                builder.addHeader(next.getKey(), next.getValue());
            }
        }
        return builder;
    }

    /**
     * 是否需要添加参数
     *
     * @param builder
     * @param params
     * @return 只有实现了HasParamsable的builder才可以添加Params, 面向父类编程
     * 返回自身,便于链式调用
     */
    private HasParamsable addParams(HasParamsable builder, HttpParams params) {
        Map<String, String> paramsMap = null;//默认为null
        if (params != null) {
            paramsMap = params.getParams();
            for (Map.Entry<String, String> entrys : paramsMap.entrySet()) {
                builder.addParams(entrys.getKey(), entrys.getValue());
            }
        }
        return builder;
    }
}
