package com.findyou.findyoueverywhere.controls;

import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;

/**
 * @author MapleDev
 * @time 17/06/01  20:05
 * @desc ${TODD}
 */
public abstract class NoDoubleItemClickListener implements AdapterView.OnItemClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    protected abstract void onNoDoubleItemClick(AdapterView<?> parent, View view, int position, long id);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = SystemClock.uptimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleItemClick(parent, view, position, id);
        }
    }
}
