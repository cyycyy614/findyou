package com.findyou.findyoueverywhere.utils.http;

import android.os.Handler;

import com.findyou.findyoueverywhere.utils.LoadingUtils;
import com.google.gson.Gson;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.constant.ApiConst;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.findyou.findyoueverywhere.utils.log.log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/9 0009.
 */

public class HttpClient {
    private static final String TAG = "HttpClient";

    public static final MediaType JSON = MediaType.parse("application/json");
    public static final MediaType XWWWFormUrlencoded = MediaType.parse("application/x-www-form-urlencoded");
    public static final MediaType GB2312 = MediaType.parse("text/html; charset=gb2312");
    private static final OkHttpClient.Builder builder = new OkHttpClient.Builder();
    private static Handler mHandler = new Handler(app.getContext().getMainLooper());
    private static boolean isUsed = true;

    public static boolean isUsed() {
        return isUsed;
    }

    public static byte[] getBytes(Object body) {
        okhttp3.ResponseBody responseBody = (okhttp3.ResponseBody) body;
        byte[] bytes = null;
        try {
            bytes = responseBody.bytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static String getString(Object body) {
        okhttp3.ResponseBody responseBody = (okhttp3.ResponseBody) body;
        String str = null;
        try {
            str = responseBody.string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static InputStream getInputStream(Object body) {
        okhttp3.ResponseBody responseBody = (okhttp3.ResponseBody) body;
        InputStream inputStream = null;
        try {
            inputStream = responseBody.byteStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    //同步http请求
    public static Object doGetSync(String url) {
        Object object = null;
        OkHttpClient client = getHttpClient();
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                object = response.body();
            } else {
                object = response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    private static OkHttpClient httpClient = null;

    public static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            try {
                httpClient = builder.connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return httpClient;
    }

    //网络图片下载 & 资源, 异步 GET请求
    public static void doGet(String url, final HttpCallback callback) {
        try {
            OkHttpClient client = getHttpClient();
            Request request = new Request.Builder().url(url).build();
            //可以省略，默认是GET请求
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onExceptionHandler(url, e);
                }

                @Override
                public void onResponse(Call call, Response res) throws IOException {
                    try {
                        String jsonData = res.body().string();
                        Gson gson = new Gson();
                        if (res.code() == 200) {
                            if (mHandler != null) {
                                mHandler.post(() -> {
                                    if (callback != null) {
                                        //callback.apply(jsonData);
                                    }
                                });
                            }
                        } else {
                            //吐丝
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doPost(String url, QueryString parameters, final HttpCallback callback) {
        if(!isUsed()){
            if(callback != null) {
                callback.apply(null);
            }
            return;
        }
        doPost(url, parameters, callback, null);
    }

    public static void doPost(String url, QueryString parameters, final HttpCallback callback, final HttpCallback errorCallback) {
        try {
            if(!isUsed()){
                if(callback != null) {
                    callback.apply(null);
                }
                return;
            }
            OkHttpClient client = getHttpClient();
            RequestBody body = RequestBody.create(JSON, parameters.toString()); //访问webservice用

            Request.Builder builder = new Request.Builder().url(url).post(body);
            //API接口是否需要验证
            boolean isAuthorize = parameters.getBoolean("isAuthorize");

            Request request = builder.build(); //Post请求

            //可以省略，默认是GET请求
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onExceptionHandler(url, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.code() == 200) {
                            String jsonData = response.body().string();
                            HttpResponse res = JsonUtils.convert(jsonData, HttpResponse.class);
                            if(res.code == 200) {
                                if (callback != null) {
                                    if (mHandler != null) {
                                        mHandler.post(() -> {
                                            callback.apply(res);
                                        });
                                    }
                                }
                            }else {
                                if(errorCallback == null) {
                                    if (res.message != "" || res.message != null) {
                                        LoadingUtils.close();
                                        ToastUtils.showToast(app.getContext(), res.message);
                                    }
                                }else {
                                    LoadingUtils.close();
                                    errorCallback.apply(res);
                                }
                            }
                        } else {
                            //吐丝
                            LoadingUtils.close();
                            ToastUtils.showToast(app.getContext(), "请求异常,请检查网络!");
                            log.write("异常请求接口:" + url);
                        }
                    } catch (Exception e) {
                        LoadingUtils.close();
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //同步http请求
    public static String doPostSync(String url, QueryString pams) {
        String object = "";
        OkHttpClient client = getHttpClient();
        try {
            RequestBody body = RequestBody.create(JSON, pams.toString());
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                object = response.body().string();
            } else {
                //吐丝
            }
        } catch (IOException e) {
            e.printStackTrace();
            onExceptionHandler(url, e);
        }
        return object;
    }

    public static void onExceptionHandler(String url, IOException e) {
        String message = "";
        if (e instanceof SocketTimeoutException) {
            message = "请求超时,请稍后重试!";
        } else if (e instanceof ConnectException) {
            message = "网络异常,请检查网络!";
        } else {
            message = "未知异常:" + e.getMessage();
        }
        LoadingUtils.close();
        //吐丝显示信息
        ToastUtils.showToast(app.getContext(), message);
    }

    //不带参数的文件上传
    public static void uploadImages(List<File> images, Map<String, String> map, HttpCallback callback){
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(int i=0; i<images.size(); i++){
            File file = images.get(i);
            long len = file.length();
            if(file.exists()){
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                mbody.addFormDataPart("file", file.getName(), fileBody);
            }
        }
        //带参数
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                mbody.addFormDataPart(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        //String url = "http://192.168.0.105:50164/WebForm1.aspx"; // ApiConst.getModuleUrl(ApiConst.API_MODULE_UP_IMAGES);
        String url = ApiConst.getModuleUrl(ApiConst.API_MODULE_UP_IMAGES);;
        RequestBody requestBody = mbody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient client  = httpBuilder
                //设置超时
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        //OkHttpClient client = getHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onExceptionHandler(url, e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.code() == 200) {
                        String str = response.body().string();
                        HttpResponse res = JsonUtils.convert(str, HttpResponse.class);
                        if(callback != null){
                            callback.apply(res);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
