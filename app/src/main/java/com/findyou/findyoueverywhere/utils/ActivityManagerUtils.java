package com.findyou.findyoueverywhere.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.findyou.findyoueverywhere.base.EmptyActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ActivityManagerUtils {

    private static ActivityManagerUtils sInstance = new ActivityManagerUtils();
    private ArrayList<Activity> activitiesList = new ArrayList<>();
    private WeakReference<Activity> sCurrentActivityWeakRef;

    private ActivityManagerUtils() {
    }

    public static ActivityManagerUtils getInstance() {
        return sInstance;
    }

    public void addActivity(Activity activity) {
        if (!activitiesList.contains(activity)) {
            activitiesList.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        activitiesList.remove(activity);
    }

    //@SuppressWarnings("unchecked")
    public <T extends Activity> T getActivity(Class<T> cls) {
        for (Activity act : activitiesList) {
            if (act.getClass() == cls) {
                return (T) act;
            }
        }
        return null;
    }


    public ArrayList<Activity> getActivitiesList() {
        return activitiesList;
    }

    public Activity getCurrentActivity() {
        return null == sCurrentActivityWeakRef ? null : sCurrentActivityWeakRef.get();
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<>(activity);
    }

    static boolean isExist(String fragmentName) {
        Fragment fragment = EmptyActivity.fragments.get(fragmentName);
        if (fragment != null) {
            return true;
        } else {
            return false;
        }
    }

    //不加参数的跳转
//    public static void startActivity(Context context, String fragmentName) {
//        if (isExist(fragmentName)) {
//            return;
//        }
//        Intent intent = new Intent(context, EmptyActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("FragmentName", fragmentName);
//        //把附加的数据放到意图当中
//        intent.putExtras(bundle);
//        //执行意图
//        context.startActivity(intent);
//    }
//
//    public static void startActivity(Context context, String fragmentName, Bundle bundle) {
//        Intent intent = new Intent(context, EmptyActivity.class);
//        if (bundle == null) {
//            return;
//        }
//        bundle.putString("FragmentName", fragmentName);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
//    }

    public static void startActivity(Context context, Class<?> cls, Bundle bundle){
        String name = cls.getName();
        if(name.contains("Activity")){
            Intent intent = new Intent();
            intent.setClass(context, cls);
            if(bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
            return;
        }
        if(bundle == null){
            bundle = new Bundle();
        }
        bundle.putString("FragmentName", name);
        Intent intent = new Intent(context, EmptyActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

//    public static void startActivity(Context context, String className, Bundle bundle){
//        String name = className;
//        if(bundle == null){
//            bundle = new Bundle();
//        }
//        bundle.putString("FragmentName", name);
//        Intent intent = new Intent(context, EmptyActivity.class);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
//    }

//    public static void startActivity(Context context, Class<?> target) {
//        Intent intent = new Intent();
//        intent.setClass(context, target);
//        context.startActivity(intent);
//    }

    /**
     * 退出应用程序
     * 这里关闭的是所有的Activity，没有关闭Activity之外的其他组件;
     * android.os.Process.killProcess(android.os.Process.myPid())
     * 杀死进程关闭了整个应用的所有资源，有时候是不合理的，通常是用
     * 堆栈管理Activity;System.exit(0)杀死了整个进程，这时候活动所占的
     * 资源也会被释放,它会执行所有通过Runtime.addShutdownHook注册的shutdown hooks.
     * 它能有效的释放JVM之外的资源,执行清除任务，运行相关的finalizer方法终结对象，
     * 而finish只是退出了Activity。
     */
    public void exitApp() {
        try {
            finishAllActivity();
            //DalvikVM的本地方法
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            //这些方法如果是放到主Activity就可以退出应用，如果不是主Activity
            //就是退出当前的Activity
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activitiesList.size(); i++) {
            if (null != activitiesList.get(i)) {
                Activity activity = activitiesList.get(i);
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
        activitiesList.clear();
    }
}