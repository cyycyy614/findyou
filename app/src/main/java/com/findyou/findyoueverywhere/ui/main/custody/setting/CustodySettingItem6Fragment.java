package com.findyou.findyoueverywhere.ui.main.custody.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiDeviceSetting;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.bean.DeviceSettingInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.ui.main.custody.setting.device.DailyPopWindow;
import com.findyou.findyoueverywhere.ui.main.custody.setting.device.TrackPopWindow;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.http.QueryString;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.google.gson.JsonObject;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

//
public class CustodySettingItem6Fragment extends BaseFragment {
    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_track)
    TextView tv_track;

    @BindView(R.id.tv_daily)
    TextView tv_daily;

    @BindView(R.id.tv_timer)
    TextView tv_timer;

    @BindView(R.id.iv_item_radio_1)
    ImageView iv_item_radio_1;

    @BindView(R.id.iv_item_radio_2)
    ImageView iv_item_radio_2;

    @BindView(R.id.iv_item_radio_3)
    ImageView iv_item_radio_3;

    private DeviceSettingInfoBean info;
    private int id;
    private String name;

    public int getLayoutId() {
        setTitle("终端设置");
        registerEvent();
        return R.layout.fragment_custody_setting_item6;
    }

    public void initSaveBar(){
        TextView textView = (TextView)getRightView(ViewType.TextView);
        if(textView != null){
            textView.setText("保存");
            textView.setTextColor(Color.WHITE);
            textView.setOnClickListener((View view)-> {
                onSuhmit();
            });
        }
    }

    public void initView(){
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        name = bundle.getString("name");
        tv_name.setText(name);
        initSaveBar();
        ApiDeviceSetting.getItem(id, (res)->{
            info = JsonUtils.convert(res.data, DeviceSettingInfoBean.class);
            if(info == null){
                info = new DeviceSettingInfoBean();
                info.track = 10;
                info.daily = 60;
                info.timer = 20;
                info.mode = -1;
            }
            if(tv_track != null){
                tv_track.setText(info.track + "秒");
            }
            if(tv_daily != null){
                tv_daily.setText(info.daily + "分");
            }
            setMode();
        });
    }

    public void setMode(){
        if(iv_item_radio_1 != null){
            iv_item_radio_1.setImageResource(R.drawable.ic_radio_normal);
        }
        if(iv_item_radio_2 != null){
            iv_item_radio_2.setImageResource(R.drawable.ic_radio_normal);
        }
        if(iv_item_radio_3 != null){
            iv_item_radio_3.setImageResource(R.drawable.ic_radio_normal);
        }
        switch (info.mode){
            case -1:
                break;
            case 0:
                iv_item_radio_1.setImageResource(R.drawable.ic_radio_hover);
                break;
            case 1:
                iv_item_radio_2.setImageResource(R.drawable.ic_radio_hover);
                break;
            case 2:
                iv_item_radio_3.setImageResource(R.drawable.ic_radio_hover);
                break;
        }
    }

    public void onSuhmit(){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", id);
        q.add("track", info.track);
        q.add("daily", info.daily);
        q.add("timer", info.timer);
        q.add("mode", info.mode);
        ApiDeviceSetting.setItem(q, (res)->{
            ToastUtils.showToast(getContext(), res.message);
        });
    }
    @OnClick(R.id.lin_item_11)
    public void lin_item_11_Click(View view){
        TrackPopWindow popWindow = new TrackPopWindow(getActivity());
        popWindow.showFromBottom();
    }

    @OnClick(R.id.lin_item_22)
    public void lin_item_22_Click(View view){
        DailyPopWindow popWindow = new DailyPopWindow(getActivity());
        popWindow.showFromBottom();
    }

    @OnClick(R.id.lin_item_1)
    public void lin_item_1_Click(View view){
        if(info == null){
            return;
        }
        info.mode = 0;
        setMode();
    }

    @OnClick(R.id.lin_item_2)
    public void lin_item_2_Click(View view){
        if(info == null){
            return;
        }
        info.mode = 1;
        setMode();
    }

    @OnClick(R.id.lin_item_3)
    public void lin_item_3_Click(View view){
        if(info == null){
            return;
        }
        info.mode = 2;
        setMode();
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_DEVICE_SET_TRACK:
                info.track = Integer.parseInt(event.obj.toString());
                tv_track.setText(info.track + "秒");
                break;
            case EventConst.API_DEVICE_SET_DAILY:
                info.daily = Integer.parseInt(event.obj.toString());
                tv_daily.setText(info.daily + "分");
                break;
        }
    }
}
