package com.findyou.findyoueverywhere.ui.main.map.popwindow;

import android.app.Activity;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.utils.LocalStorage;
import com.findyou.findyoueverywhere.utils.ToolUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectDeviceModePopWindow extends BasePopWindow {
    @BindView(R.id.iv_mode_1)
    ImageView iv_mode_1;
    @BindView(R.id.iv_mode_2)
    ImageView iv_mode_2;
    @BindView(R.id.iv_mode_3)
    ImageView iv_mode_3;
    @BindView(R.id.iv_mode_4)
    ImageView iv_mode_4;

    public int getLayoutId() {
        mIsShowBackground = true;
        mIsOutsideClose = false;
        return R.layout.select_device_mode_pop_window;
    }

    public SelectDeviceModePopWindow(Activity activity){
        super(activity);
    }

    public void initView(){
        String str = LocalStorage.getItem("SelectDeviceModePopWindow");
        if(ToolUtils.isNumeric(str)) {
            int index = Integer.parseInt(str);
            displayNormalImage();
            displayHoverlImage(index);
        }else{
            displayNormalImage();
            displayHoverlImage(2);
        }
    }

    public void displayNormalImage(){
        iv_mode_1.setImageResource(R.drawable.lin_mode_1_normal);
        iv_mode_2.setImageResource(R.drawable.lin_mode_2_normal);
        iv_mode_3.setImageResource(R.drawable.lin_mode_3_normal);
        iv_mode_4.setImageResource(R.drawable.lin_mode_4_normal);
    }

    public void displayHoverlImage(int index){
        switch (index){
            case 1:
                iv_mode_1.setImageResource(R.drawable.lin_mode_1_hover);
                break;
            case 2:
                iv_mode_2.setImageResource(R.drawable.lin_mode_2_hover);
                break;
            case 3:
                iv_mode_3.setImageResource(R.drawable.lin_mode_3_hover);
                break;
            case 4:
                iv_mode_4.setImageResource(R.drawable.lin_mode_4_hover);
                break;
        }
        LocalStorage.setItem("SelectDeviceModePopWindow", index + "");
    }

    @OnClick(R.id.lin_mode_1)
    public void lin_mode_1_Click(View view){
        displayNormalImage();
        displayHoverlImage(1);
        postEvent(new EventMessenger(EventConst.API_MAP_SELECT_DEVICE_MODE, 1));
        close();
    }

    @OnClick(R.id.lin_mode_2)
    public void lin_mode_2_Click(View view){
        displayNormalImage();
        displayHoverlImage(2);
        postEvent(new EventMessenger(EventConst.API_MAP_SELECT_DEVICE_MODE, 2));
        close();
    }

    @OnClick(R.id.lin_mode_3)
    public void lin_mode_3_Click(View view){
        displayNormalImage();
        displayHoverlImage(3);
        postEvent(new EventMessenger(EventConst.API_MAP_SELECT_DEVICE_MODE, 3));
        close();
    }

    @OnClick(R.id.lin_mode_4)
    public void lin_mode_4_Click(View view){
        displayNormalImage();
        displayHoverlImage(4);
        postEvent(new EventMessenger(EventConst.API_MAP_SELECT_DEVICE_MODE, 4));
        close();
    }
}
