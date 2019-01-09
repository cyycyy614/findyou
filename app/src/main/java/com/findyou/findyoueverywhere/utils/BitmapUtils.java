package com.findyou.findyoueverywhere.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.CommonCallback;
import com.findyou.findyoueverywhere.utils.http.HttpClient;

import java.io.File;
import java.io.InputStream;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class BitmapUtils {
    //图片缩放
    public static String scale(String fileName, int maxWidth, int maxHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap src = BitmapFactory.decodeFile(fileName, null);
        float scale = 1.0f; //等比
        int width = src.getWidth();
        int height = src.getHeight();
        if(width > height){
            //按宽缩放
            if(width > maxWidth) {
                scale = (float) maxWidth / (float) width;
            }
        }else {
            if (height > maxHeight) {
                scale = (float) maxHeight / (float) height;
            }
        }

        Bitmap des = null;
        String saveFileName = "";
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileName1 = "temp/" + ToolUtils.getGuid() + "." + suffix;
        if(scale != 1.0f) {
            des = AssertUtils.getScaleBitemap(src, scale, scale);
            SDCardUtils.saveBitmap(fileName1, des);
        } else {
            //des = AssertUtils.getScaleBitemap(src, scale, scale);
            SDCardUtils.saveBitmap(fileName1, src);
            //saveFileName = fileName;
        }
        saveFileName = SDCardUtils.getExternalRootPath() + fileName1;

        //释放资源
        if(src != null && !src.isRecycled()){
            src.recycle();
            src = null;
        }
        if(des != null && !des.isRecycled()){
            des.recycle();
            des = null;
        }
        return saveFileName;
    }

    //图片压缩
    public static void compress(Context context, String filePath, int maxSize, CommonCallback callback){
        Luban.with(context)
                .load(filePath)
                .ignoreBy(maxSize)
                .setTargetDir(SDCardUtils.getExternalRootPath() + "temp/")
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onSuccess(File file) {
                        String str = file.getName();
                        //压缩完成后上传
                        if(callback != null) {
                            callback.apply(file);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }).launch();
    }

    public static Bitmap getBitmap(String imageUrl){
        try {
            Object obj = HttpClient.doGetSync(imageUrl);
            if (obj == null) {
                return null;
            }
            InputStream inputStream = HttpClient.getInputStream(obj);
            if (inputStream == null) {
                return null;
            }
            //头像bigmap
            Bitmap headbitmap = BitmapFactory.decodeStream(inputStream);
            return headbitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.default_128);
    }
}
