package com.findyou.findyoueverywhere.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.findyou.findyoueverywhere.utils.SDCardUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AppUpdateOnlineHelper {

    private final  static  String APK_SAVE_NAME = "app_install_android.apk";
    private Context mContext;
    private final OkHttpClient.Builder builder = new OkHttpClient.Builder();
    private static OkHttpClient httpClient = null;
//    private static final int INSTALL_APP = 1000;
//    private static final int UPDATE_DOWNLOAD_PROCESS = 1001;

    public AppUpdateOnlineHelper(Activity context){
        mContext = context;
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

    public void downloadFile(String downloadUrl, final OnDownloadListener listener) {
        Date d = new Date();
        String fileName = downloadUrl + "?d=" + d.getTime();
        Request request = new Request.Builder().url(downloadUrl).build();
        getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                download(response, listener);
            }
        });
    }

    private void download(Response response, final OnDownloadListener listener){
        long total = response.body().contentLength();
        InputStream is = response.body().byteStream();
        File file = createFile(APK_SAVE_NAME);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            //开始下载
            byte[] buffer = new byte[1024 * 4];
            int len;
            long length = 0;
            while ((len = is.read(buffer)) != -1) {
                //保存文件
                fos.write(buffer, 0, len);
                length += len;
                int progress = (int) (length * 100.0f / total);
                listener.onDownloading(progress);
            }
            fos.flush();
            is.close();
            fos.close();
            listener.onDownloadSuccess();
        }catch (Exception e){
            e.printStackTrace();
            listener.onDownloadFailed();
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
                if(fos != null){
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public static File createFile(String fileName){
        SDCardUtils.makedir("download/apk/");
        String rootPath = SDCardUtils.getExternalRootPath();
        File file = SDCardUtils.createNewFile("download/apk/" + fileName);
        return file;
    }

    public void install() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = createFile(APK_SAVE_NAME);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public OkHttpClient getHttpClient() {
        if (httpClient == null) {
            try {
                httpClient = builder.connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return httpClient;
    }
}
