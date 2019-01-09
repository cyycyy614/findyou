package com.findyou.findyoueverywhere.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.findyou.findyoueverywhere.app.app;

import java.io.InputStream;

public class AssertUtils {

    private static DisplayMetrics metric = null;
    private static float L_DPI = (float) 0.75;
    private static float M_DPI = (float) 1.0;
    private static float H_DPI = (float) 1.5;
    private static float XH_DPI = (float) 2.0;
    private static float XXH_DPI = (float) 3.0;

    private static Object obj = new Object();

    public static Bitmap getBitmap(String path) {
        Bitmap result = null;
        try {
            synchronized (obj) {
                InputStream is = app.getContext().getAssets().open(path);
                Bitmap source = BitmapFactory.decodeStream(is);
                float scale = getScale();
                result = getScaleBitemap(source, scale, scale);
                if (result != source) {
                    source.recycle();
                }
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap getOriginalBitmap(String path) {
        Bitmap result = null;
        try {
            synchronized (obj) {
                InputStream is = app.getContext().getAssets().open(path);
                result = BitmapFactory.decodeStream(is);
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static DisplayMetrics getMetric() {
        if (metric == null) {
            metric = new DisplayMetrics();
            Context mContext = app.getContext();
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metric);
        }
        return metric;
    }

    public static float getScale() {
        DisplayMetrics m = getMetric();
        float scale = m.density / XH_DPI;
        return scale;
    }

    public static Bitmap getScaleBitemap(Bitmap src, float xScale, float yScale) {
        Matrix matrix = new Matrix();
        matrix.postScale(xScale, yScale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        matrix = null;
        return resizeBmp;
    }
}
