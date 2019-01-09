package com.findyou.findyoueverywhere.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.blankj.utilcode.util.AppUtils;
import com.findyou.findyoueverywhere.app.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/9/7 0007.
 */
public class SDCardUtils {
    private static final String TAG = "SDCardUtil";

    //SD卡根目录
    public static String ROOT = "." + AppUtils.getAppPackageName(); //包名
    public static String mLocalExternalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String defaultRootPath = mLocalExternalPath.concat("/").concat(ROOT).concat("/");

    public static void initSDCard(){
    }

    public static void makedir(String dir) {
        try {
            File file = new File(defaultRootPath + dir);

            if (!file.getParentFile().exists()) {
                boolean mkdirs = file.getParentFile().mkdirs();
                boolean isOk = mkdirs;
            }
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                //-----------------
                if(!mkdirs) { // 如果 建立文件夹失败 将父文件夹删除
                    File parentFile = new File(defaultRootPath);
                    if(parentFile.exists()) {
                        boolean delete = parentFile.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mkdirs(String path){
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getExternalRootPath(){
        return defaultRootPath;
    }

    public static String getExternalDir(){
        return ROOT;
    }

    public static File createNewFile(String fileName){
        String fullName = defaultRootPath + fileName;
        File file = new File(fullName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static boolean isExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean removeFile(String filePath) {
        File file = new File(filePath);
        return file.isFile() ? file.delete() : false;
    }

    //将一个InoutStream里面的数据写入到SD卡中
    public static void saveFile(String fileName, InputStream input){
        File file = null;
        OutputStream output = null;
        try {
            //创建文件
            file = createNewFile(fileName);
            //写数据流
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];//每次存4K
            int temp;
            //写入数据
            while((temp = input.read(buffer)) != -1){
                output.write(buffer,0,temp);
            }
            output.flush();
        } catch (Exception e) {
            System.out.println("写数据异常:" + e);
        }
        finally{
            try {
                output.close();
                input.close();
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
    }

    public static File getFile(String fileName){
        String fullName = defaultRootPath + fileName;
        File file = new File(fullName);
        if(!file.exists()){
            return null;
        }else {
            return file;
        }
    }

    public static File[] getFiles(String dir){
        String path = defaultRootPath + dir;
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        return file.listFiles();
    }

    //从SDK上得到InputStream
    public static InputStream getInputStream(String fileName){
        String path = defaultRootPath + fileName;
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return inputStream;
    }

    public static Bitmap getBitmap(String filePath){
        String path = defaultRootPath + filePath;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    public static Drawable getDrawable(String filePath){
        Bitmap bitmap = null;
        try {
            String path = defaultRootPath + filePath;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeFile(path, options);
            if(bitmap == null){
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Drawable drawable = new BitmapDrawable(app.getContext().getResources(), bitmap);
        return drawable;
    }

    public static boolean removeFolder(String folder) {
        File file = new File(folder);
        if (!file.isDirectory()) {
            return false;
        } else {
            boolean success = true;
            File[] files = file.listFiles();
            File[] arr$ = files;
            int len$ = files.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                File f = arr$[i$];
                if (f.isDirectory()) {
                    removeFolder(f.getPath());
                } else {
                    success = f.delete();
                }
            }
            if (success) {
                file.delete();
            }
            return success;
        }
    }

    public static boolean deleteDirectory(File file) {
        if (file == null) {
            return false;
        } else if (file.isFile()) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; ++i) {
                    deleteDirectory(files[i]);
                }
            }
            return file.delete();
        }
    }

    public static boolean removeFiles(File file) {
        if(!file.exists()){
            return true;
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; ++i) {
                files[i].delete();
            }
        }
        return true;
    }

    public static String writeTextFile(InputStream input){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = input.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input.toString();
    }

    public static boolean writeTextFile(String content, String fileName) {
        String filePath = defaultRootPath + fileName;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(content);
            writer.flush();
            writer.close();
            writer = null;
        } catch (IOException var13) {
            //NSLog.i("FileUtil", var13.getStackTrace());
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                    writer = null;
                    return true;
                }
            } catch (IOException var12) {
                //NSLog.i("FileUtil", var12.getStackTrace());
            }
        }
        return false;
    }

    public static String readTextFile(String fileName) {
        String filePath = defaultRootPath + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte buf[] = new byte[4 * 1024];
            int len;

            while ((len = fileInputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }

    public static long getFileCount(File file){
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFileCount(fileList[i]);
                }else{
                    size = size + 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static void saveBitmap(String fileName, Bitmap bitmap){
        FileOutputStream fos = null;
        try {
            Bitmap out = null;
            //保存图片
            File file = createNewFile(fileName);
            fos = new FileOutputStream(file);
            if(fileName.contains("png")) {
                //把透明色的背景变成白色
                out = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                Canvas canvas = new Canvas(out);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bitmap, 0, 0, null);
                out.compress(Bitmap.CompressFormat.PNG, 80, fos);
            }else if(fileName.contains("jpg")){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            }
            fos.flush();
            fos.close();
            if(out != null && !out.isRecycled()){
                out.recycle();
                out = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
