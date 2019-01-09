package com.findyou.findyoueverywhere.controls;

import android.view.View;

public interface TabLayoutCallback {
    public void onClickItem(View view, int position);
    public void onNormalStyle(View view);
    public void onHoverStyle(View view);
}