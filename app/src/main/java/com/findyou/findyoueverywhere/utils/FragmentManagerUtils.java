package com.findyou.findyoueverywhere.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentManagerUtils {
    private FragmentManager fragmentManager;

    public FragmentManagerUtils(FragmentManager fm){
        this.fragmentManager = fm;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void replaceFragment(int resid, Fragment fragment) {
        if(null == fragment) return;
        replaceFragment(resid, fragment, fragment.getClass().getSimpleName());
    }

    public void replaceFragment(int resid, Fragment fragment, String tag) {
        if(null == fragment) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(resid, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    public void addFragment(int resid, Fragment fragment) {
        if(null == fragment) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(resid, fragment, fragment.getClass().getSimpleName());
        ft.commitAllowingStateLoss();
    }

    public void showFragment(Fragment fragment) {
        if (null == fragment) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    public void hideFragment(Fragment fragment) {
        if(null == fragment) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.hide(fragment);
        ft.commitAllowingStateLoss();
    }

    public void removeFragment(Fragment fragment) {
        if(null == fragment) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }
}
