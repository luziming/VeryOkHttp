# VeryOkHttp
凯哥巅峰之作/业界良心,一度被葬爱家族称作超越OkHttp的存在~


#使用方法
1.在root project的build.gradle中添加中央仓库

  maven{ url 'https://jitpack.io'}
  
2.添加依赖

  compile 'com.github.luziming:VeryOkHttp:v1.0.1'
  
#在Application中初始化

OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        
#普通get请求
 //初始化对象,指定target
 VeryOkHttp veryOkHttp = new VeryOkHttp(this);
 //添加参数
 HttpParams params = new HttpParams();
 //请求头
 params.addHeader();
 
 params.put("key","value");
 
 veryOkHttp.get(URL_GET,params,GET_CODE,this);
 
 
 //监听
 @Override
    public void onSuccess(String response, int id) {
        switch (id) {
            case GET_CODE:
            case POST_CODE:
                break;
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        switch (id) {
            case GET_CODE:
            case POST_CODE:
                break;
        }
    }
    
    
    
  #取消请求
  
  @Override
    protected void onDestroy() {
        super.onDestroy();
        veryOkHttp.cancel(this);
    }
