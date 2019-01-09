package com.findyou.findyoueverywhere.ui.main.map.popwindow;

import android.app.Activity;
import android.view.View;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiGuardian;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.ui.main.custody.setting.child.AddChildSettingItem1Fragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;

import butterknife.OnClick;

public class BindDeviceTipsPopWindow extends BasePopWindow {
    public int getLayoutId() {
        mIsShowBackground = true;
        mIsOutsideClose = false;
        return R.layout.bind_device_tips_popwindow;
    }

    public BindDeviceTipsPopWindow(Activity activity){
        super(activity);
    }

    @OnClick(R.id.iv_close)
    public void iv_close_Click(View view){
        close();
    }

    @OnClick(R.id.tv_add_chile)
    public void tv_add_chile_Click(View view){
        ActivityManagerUtils.startActivity(getContext(), AddChildSettingItem1Fragment.class, null);
    }
}
