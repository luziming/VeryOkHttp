# VeryOkHttp
凯哥巅峰之作/业界良心,一度被葬爱家族称作超越OkHttp的存在~

* 所谓前人栽树,后人乘凉。
* 首先要感谢这位大神的OkHttp  >>JakeWharton：https://github.com/square/okhttp
* 其次是鸿神的OkHttpUtils     >>https://github.com/hongyangAndroid/okhttputils
* 目前对应的OkHttp版本3.3.1,OkHttpUtils的版本2.6.2
* 大神封装的已经很好了,不过还是感觉挺繁琐的,简单的封装一下,可以一行代码请求网络,何乐而不为?

# 使用方法
1.在root project的build.gradle中添加中央仓库

  maven{ url 'https://jitpack.io'}
 
  ![image](https://github.com/luziming/VeryOkHttp/raw/master/images/maven.png)
  
2.添加依赖

  compile 'com.github.luziming:VeryOkHttp:v1.0.1'
  
  ![image](https://github.com/luziming/VeryOkHttp/raw/master/images/compile.png)
  
  
# 在Application中初始化

 
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
                OkHttpUtils.initClient(okHttpClient);
        
# 普通get请求


     // 初始化对象,指定target
        VeryOkHttp veryOkHttp = new VeryOkHttp(this);
     // 添加参数
          HttpParams params = new HttpParams();
     // 请求头
          params.addHeader(token);

          params.put("key","value");

          veryOkHttp.get(URL_GET,params,GET_CODE,this);
          
# postJson请求
    
    
         HttpParams params = new HttpParams();
         params.addHeader(token);
         //通过Gson将been对象转换为Json请求
         UserParams userParams = new UserParams(new UserParams.UserBean(name,desc,""));
         veryOkHttp.postJson(API.ADD_PRODUCTS_URL,params, API.ADD_PRODUCTS_CODE,userParams,this);
     
# 其他请求方式delete/patch,用法大致相同
 
# 监听
 
 
       @Override
          public void onSuccess(String response, int id) {
              //可以通过id来区别response
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
    
    
    
  # 取消请求
  
      @Override
        protected void onDestroy() {
            super.onDestroy();
            veryOkHttp.cancel(this);
        }
