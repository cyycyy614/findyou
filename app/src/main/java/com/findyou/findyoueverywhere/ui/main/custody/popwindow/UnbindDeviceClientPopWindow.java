package com.findyou.findyoueverywhere.ui.main.custody.popwindow;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiChild;
import com.findyou.findyoueverywhere.api.http.ApiGuardian;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class UnbindDeviceClientPopWindow extends BasePopWindow {
    @BindView(R.id.tv_name)
    TextView tv_name;

    private int id;
    private String name;
    private String deviceId;

    public int getLayoutId() {
        mIsShowBackground = true;
        mIsOutsideClose = false;
        return R.layout.pop_window_unbind_device_client;
    }

    public UnbindDeviceClientPopWindow(Activity activity, int id, String name, String deviceId){
        super(activity);
        this.id = id;
        this.name = name;
        this.deviceId = deviceId;
    }

    public void initView(){
        if(tv_name != null){
            tv_name.setText(name);
        }
    }

    @OnClick(R.id.iv_close)
    public void iv_close_Click(View view){
        close();
    }

    @OnClick(R.id.iv_cancel)
    public void iv_cancel_Click(View view){
        close();
    }

    @OnClick(R.id.iv_ok)
    public void iv_ok_Click(View view){
        ApiChild.unbind(id, name, deviceId, (res)->{
            ToastUtils.showToast(getContext(), "解绑成功!");
            postEvent(new EventMessenger(EventConst.API_UNBIND_DEVICE));
            close();
        });
    }
}
