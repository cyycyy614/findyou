package com.findyou.findyoueverywhere.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.findyou.findyoueverywhere.app.app;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Created by gy on 16/1/22.
 * <p/>
 * 设备工具类
 * 用来获取设备信息
 * imei nettype screensize
 */
public class DeviceUtils {

    private static float L_DPI = (float) 0.75;
    private static float M_DPI = (float) 1.0;
    private static float H_DPI = (float) 1.5;
    private static float XH_DPI = (float) 2.0;
    private static float XXH_DPI = (float) 3.0;

    private static DeviceUtils sDeviceManager = null;
    private static Context mContext;
    private int screenHeight = 0;
    private int screenWidth = 0;
    private int statusBarHeight = 0;
    private String sImeiCode = "";
    private DisplayMetrics metric = null;

    private DeviceUtils(Context mContext) {
        this.mContext = mContext;
    }

    public static DeviceUtils getInstance() {
        if (sDeviceManager == null) {
            sDeviceManager = new DeviceUtils(app.getContext());
        }
        return sDeviceManager;
    }

    /**
     * 获取imei
     *
     * @return
     */
    public String getImeiCode() {
        if (!TextUtils.isEmpty(sImeiCode)) {
            return sImeiCode;
        }
        if(ContextCompat.checkSelfPermission(app.getContext(),Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return sImeiCode;
        }
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        sImeiCode = telephonyManager.getDeviceId();
        if (TextUtils.isEmpty(sImeiCode)) {// imei码为空时
            String property = LocalStorage.getItem("imei_code");
            if (!TextUtils.isEmpty(property)) {
                sImeiCode = property;
            } else {// 生成标识
                sImeiCode = ToolUtils.MD5(System.nanoTime() + "imei_code");
                LocalStorage.setItem("imei_code", sImeiCode);
            }
        }
        return sImeiCode;
    }

    //得到当前手机分辨率
    /*
    4:3
    VGA   640*480 (Video Graphics Array)
    QVGA  320*240 (Quarter VGA)
    HVGA  480*320 (Half-size VGA)
    SVGA  800*600 (Super VGA)

    5:3
    WVGA  800*480 (Wide VGA)

    16:9
    FWVGA 854*480 (Full Wide VGA)
    HD          1920*1080 High Definition
    QHD         960*540
    720p        1280*720  标清
    1080p       1920*1080 高清

    手机:
    iphone 4/4s    960*640 (3:2)
    iphone5        1136*640
    小米1          854*480(FWVGA)
    小米2          1280*720

    1.2 分辨率对应DPI
    "HVGA    mdpi
    "WVGA    hdpi
    "FWVGA   hdpi
    "QHD     hdpi
    "720P    xhdpi
    "1080P   xxhdpi
     4k      xxxhdpi 4K屏

    xxhdpi: 3.0
    xhdpi: 2.0
    hdpi: 1.5
    mdpi: 1.0 (baseline)
    ldpi: 0.75

    drawable-ldpi、drawable-mdpi、drawable-hdpi 精度分别为低、中（android默认）、高。
    对应的图片大小为：36×36、48×48、72×72。
    xxhdpi: 144*144
    xhdpi:96*96
    hdpi:72*72
    mdpi:48*48
    ldpi:36*36
    */
    public DisplayMetrics getMetric() {
        if (metric == null) {
            metric = new DisplayMetrics();
            Context mContext = app.getContext();
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metric);
        }
        return metric;
    }

    public float getDefaultScale() {
        float density = getMetric().density;
        float scale = density / XH_DPI;
        return scale;
    }

    public static String getSDKVersion() {
        return Build.VERSION.RELEASE;
    }


    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = getMac(context);

            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMac(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context);
        } else {
            mac = getMacByJavaAPI();
            if (TextUtils.isEmpty(mac)){
                mac = getMacBySystemInterface(context);
            }
        }
        return mac;

    }

    @TargetApi(9)
    private static String getMacByJavaAPI() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                if ("wlan0".equals(netInterface.getName()) || "eth0".equals(netInterface.getName())) {
                    byte[] addr = netInterface.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        return null;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static String getMacBySystemInterface(Context context) {
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Throwable e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Throwable e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

}
