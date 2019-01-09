package com.findyou.findyoueverywhere.ui.main.map.view;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BusManager;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.bean.ChildInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.ui.main.custody.CustodySettingFragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.CustodySettingItem9Fragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.child.EditChildSettingItem1Fragment;
import com.findyou.findyoueverywhere.ui.main.map.popwindow.SelectDeviceModePopWindow;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.DateTimeUtils;
import com.findyou.findyoueverywhere.utils.LocalStorage;
import com.findyou.findyoueverywhere.utils.PicUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChildInfoView extends LinearLayout {
    @BindView(R.id.iv_child_info_1)
    ImageView iv_child_info_1;
    @BindView(R.id.iv_child_info_2)
    ImageView iv_child_info_2;
    @BindView(R.id.iv_child_info_3)
    ImageView iv_child_info_3;
    @BindView(R.id.iv_child_info_4)
    ImageView iv_child_info_4;
    @BindView(R.id.iv_child_info_5)
    ImageView iv_child_info_5;

    @BindView(R.id.tv_child_info_1)
    TextView tv_child_info_1;
    @BindView(R.id.tv_child_info_2)
    TextView tv_child_info_2;
    @BindView(R.id.tv_child_info_3)
    TextView tv_child_info_3;
    @BindView(R.id.tv_child_info_4)
    TextView tv_child_info_4;
    @BindView(R.id.tv_child_info_5)
    TextView tv_child_info_5;

    @BindView(R.id.iv_headimage)
    ImageView iv_headimage;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.iv_select_mode)
    ImageView iv_select_mode;

    public int getLayoutId() {
        return R.layout.child_info_pop_window;
    }

    public ChildInfoView(Activity activity){
        super(activity);
    }

    public ChildInfoView(Context context) {
        super(context);
        init();
    }

    public ChildInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChildInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){
        View view = inflate(getContext(), R.layout.child_info_pop_window, this);
        ButterKnife.bind(this, view);
        BusManager.register(this);
        displayNormal();
        //displayHover(1);
        //显示模式选择按钮
        String str = LocalStorage.getItem("SelectDeviceModePopWindow");
        if(ToolUtils.isNumeric(str)) {
            int index = Integer.parseInt(str);
            displaySelectDeviceModeBtn(index);
        }
    }

    private ChildInfoBean infoBean;
    public void setChildInfo(ChildInfoBean info){
        infoBean = info;
        tv_name.setText(info.name);
        tv_address.setText(info.address);
        PicUtils.loadImage(getContext(), info.headimage, iv_headimage);
    }

    @OnClick(R.id.iv_headimage)
    public void iv_headimage_Click(View view){
//        Bundle bundle = new Bundle();
//        bundle.putInt("id", infoBean.id);
//        ActivityManagerUtils.startActivity(getContext(), EditChildSettingItem1Fragment.class, bundle);

        ChildInfoBean bean = infoBean;
        Bundle bundle = new Bundle();
        bundle.putInt("id", bean.id);
        bundle.putString("name", bean.name);
        bundle.putString("imei", bean.imei);
        bundle.putString("headimage", bean.headimage);
        bundle.putString("deviceId", bean.deviceId);
        bundle.putString("device_address", bean.device_address);
        ActivityManagerUtils.startActivity(getContext(), CustodySettingFragment.class, bundle);
    }

    public void displayNormal(){
        if(iv_child_info_1 != null){
            iv_child_info_1.setImageResource(R.drawable.ic_child_info_item_1_normal);
        }
        if(iv_child_info_2 != null){
            iv_child_info_2.setImageResource(R.drawable.ic_child_info_item_2_normal);
        }
        if(iv_child_info_3 != null){
            iv_child_info_3.setImageResource(R.drawable.ic_child_info_item_3_normal);
        }
        if(iv_child_info_4 != null){
            iv_child_info_4.setImageResource(R.drawable.ic_child_info_item_4_normal);
        }
        if(iv_child_info_5 != null){
            iv_child_info_5.setImageResource(R.drawable.ic_child_info_item_5_normal);
        }
        ///////////////////////////////////////////////////////////////////////////////////
        if(tv_child_info_1 != null){
            tv_child_info_1.setTextColor(app.getContext().getColor(R.color.colorGray6));
        }
        if(tv_child_info_2 != null){
            tv_child_info_2.setTextColor(app.getContext().getColor(R.color.colorGray6));
        }
        if(tv_child_info_3 != null){
            tv_child_info_3.setTextColor(app.getContext().getColor(R.color.colorGray6));
        }
        if(tv_child_info_4 != null){
            tv_child_info_4.setTextColor(app.getContext().getColor(R.color.colorGray6));
        }
        if(tv_child_info_5 != null){
            tv_child_info_5.setTextColor(app.getContext().getColor(R.color.colorGray6));
        }
    }

    public void displayHover(int item){
        displayNormal();
        switch (item){
            case 1:
                iv_child_info_1.setImageResource(R.drawable.ic_child_info_item_1_hover);
                tv_child_info_1.setTextColor(app.getContext().getColor(R.color.colorBlack));
                break;
            case 2:
                iv_child_info_2.setImageResource(R.drawable.ic_child_info_item_2_hover);
                tv_child_info_2.setTextColor(app.getContext().getColor(R.color.colorBlack));
                break;
            case 3:
                iv_child_info_3.setImageResource(R.drawable.ic_child_info_item_3_hover);
                tv_child_info_3.setTextColor(app.getContext().getColor(R.color.colorBlack));
                break;
            case 4:
                iv_child_info_4.setImageResource(R.drawable.ic_child_info_item_4_hover);
                tv_child_info_4.setTextColor(app.getContext().getColor(R.color.colorBlack));
                break;
            case 5:
                iv_child_info_5.setImageResource(R.drawable.ic_child_info_item_5_hover);
                tv_child_info_5.setTextColor(app.getContext().getColor(R.color.colorBlack));
                break;
        }
    }

    @OnClick(R.id.iv_child_info_1)
    public void iv_child_info_1_Click(View view) {
        displayHover(1);
        BusManager.postEvent(new EventMessenger(EventConst.API_MAP_DISPLAY_MODE_TRACK,1));
    }

    @OnClick(R.id.iv_child_info_2)
    public void iv_child_info_2_Click(View view) {
        displayHover(2);
        BusManager.postEvent(new EventMessenger(EventConst.API_MAP_DISPLAY_MODE_OTHER,2));
    }

    @OnClick(R.id.iv_child_info_3)
    public void iv_child_info_3_Click(View view) {
        displayHover(3);
        BusManager.postEvent(new EventMessenger(EventConst.API_MAP_DISPLAY_MODE_OTHER,3));
    }

    @OnClick(R.id.iv_child_info_4)
    public void iv_child_info_4_Click(View view) {
        displayHover(4);
        BusManager.postEvent(new EventMessenger(EventConst.API_MAP_DISPLAY_MODE_OTHER,4));
    }

    @OnClick(R.id.iv_child_info_5)
    public void iv_child_info_5_Click(View view) {
        displayHover(5);
        BusManager.postEvent(new EventMessenger(EventConst.API_MAP_DISPLAY_MODE_OTHER,5));
        //历史
        ActivityManagerUtils.startActivity(getContext(), CustodySettingItem9Fragment.class, null);
    }

    @OnClick(R.id.view_root)
    public void view_root_Click(View view){
    }

    SelectDeviceModePopWindow popWindow;
    //选择模式
    @OnClick(R.id.iv_select_mode)
    public void iv_select_mode_Click(View view){
        Activity activity = ActivityManagerUtils.getInstance().getCurrentActivity();
        popWindow = new SelectDeviceModePopWindow(activity);
        popWindow.showFromCenter();
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_MAP_SELECT_DEVICE_MODE:
                int index = Integer.parseInt(event.obj.toString());
                displaySelectDeviceModeBtn(index);
                break;
            case EventConst.API_GET_BAIDU_MAP_ADDRESS:
                Date date = new Date();
                tv_time.setText(DateTimeUtils.toString(date));
                tv_address.setText(event.obj.toString());
                break;
        }
    }

    public void displaySelectDeviceModeBtn(int index){
        switch (index){
            case 1:
                iv_select_mode.setImageResource(R.drawable.lin_mode_1_hover);
                break;
            case 2:
                iv_select_mode.setImageResource(R.drawable.lin_mode_2_hover);
                break;
            case 3:
                iv_select_mode.setImageResource(R.drawable.lin_mode_3_hover);
                break;
            case 4:
                iv_select_mode.setImageResource(R.drawable.lin_mode_4_hover);
                break;
        }
    }

}
