package com.findyou.findyoueverywhere.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.findyou.findyoueverywhere.app.app;

public class UIUtils {
    public static int SCREEN_WIDTH_PIXELS;
    public static int SCREEN_HEIGHT_PIXELS;
    public static float SCREEN_DENSITY;
    public static float SCALED_DENSITY;
    public static int NAV_BAR_HEIGHT_PIXELS; // 虚拟栏 高度

    public static void init(){
        //初始化屏幕尺寸
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) app.getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        int navBarHeight = 0;
        Resources resources = app.getContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        boolean hasPhysicalHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        int screen_height;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Point size = new Point();
            wm.getDefaultDisplay().getRealSize(size);
            screen_height = size.y;
        } else if (hasPhysicalHomeKey) {
            screen_height = dm.heightPixels;
        } else {
            screen_height = dm.heightPixels + navBarHeight;
        }

        SCREEN_WIDTH_PIXELS = dm.widthPixels;
        SCREEN_HEIGHT_PIXELS = screen_height;
        NAV_BAR_HEIGHT_PIXELS = screen_height > dm.heightPixels ? screen_height - dm.heightPixels : 0;
        SCREEN_DENSITY = dm.density;
        SCALED_DENSITY = dm.scaledDensity;
    }

    public static int getVirtalBarHeight(){
        return NAV_BAR_HEIGHT_PIXELS;
    }

    public static int getStatusHeight(Context context) {
        int statusHeight = 0;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static int getScreenWidth(Context context) {
        if(context == null){
            return 0;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //得到Activity的RootView = ContentView
    public static View getContentView(Activity context) {
        if(context == null){
            return null;
        }
        ViewGroup view = (ViewGroup) context.getWindow().getDecorView();
        LinearLayout content = (LinearLayout) view.getChildAt(0);
        return content.getChildAt(0);
    }

    public static String getString(int resid){
        return app.getContext().getResources().getString(resid);
    }
}
