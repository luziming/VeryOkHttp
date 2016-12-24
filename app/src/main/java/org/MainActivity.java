package org;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.genius.com.http.HttpParams;
import net.genius.com.http.VeryOkHttp;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, VeryOkHttp.OnResponseListener {




    public static final String URL_GET = "http://apis.juhe.cn/mobile/get?phone=13812345678&key=daf8fa858c330b22e342c882bcbac622";
    public final int GET_CODE = 0;
    public final int POST_CODE = 1;


    public static final String URL_POST = "http://apis.juhe.cn/mobile/get ";

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String json = (String) msg.obj;
                    try {
                        JSONObject obj = new JSONObject(json);
                        JSONObject result = obj.getJSONObject("result");
                        String province = result.getString("province");
                        error.setText(province);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    String json1 = (String) msg.obj;
                    try {
                        JSONObject obj = new JSONObject(json1);
                        JSONObject result = obj.getJSONObject("result");
                        String province = result.getString("province");
                        success.setText(province);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private Button bt_get;
    private Button bt_post;
    private TextView success;
    private TextView error;
    private VeryOkHttp veryOkHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.R.layout.activity_main);

        bt_get = (Button) findViewById(R.id.get);
        bt_post = (Button) findViewById(R.id.post);
        success = (TextView) findViewById(R.id.success);
        error = (TextView) findViewById(R.id.error);

        bt_get.setOnClickListener(this);
        bt_post.setOnClickListener(this);

        //初始化对象,指定target
        veryOkHttp = new VeryOkHttp(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get:
                //Get请求
                veryOkHttp.get(URL_GET,getParams(),GET_CODE,this);
                break;
            case R.id.post:
                //Post请求
                veryOkHttp.post(URL_POST,getParams(),GET_CODE,this);
                break;
        }
    }

    public HttpParams getParams(){
        return new HttpParams()
                .put("phone","13812345678")
                .put("key","daf8fa858c330b22e342c882bcbac622");
    }

    @Override
    public void onSuccess(String response, int id) {
        switch (id) {
            case GET_CODE:
            case POST_CODE:
                try {
                    JSONObject obj = new JSONObject(response);
                    obj = obj.getJSONObject("result");
                    success.setText(obj.getString("province"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        switch (id) {
            case GET_CODE:
            case POST_CODE:
                error.setText(e.toString());
                break;
        }
    }
}
