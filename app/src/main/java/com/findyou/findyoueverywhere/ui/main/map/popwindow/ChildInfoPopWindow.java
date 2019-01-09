package com.findyou.findyoueverywhere.ui.main.map.popwindow;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ChildInfoPopWindow extends BasePopWindow {
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

    @BindView(R.id.iv_select_mode)
    ImageView iv_select_mode;

    public int getLayoutId() {
        mIsShowBackground = true;
        mIsOutsideClose = false;
        return R.layout.child_info_pop_window;
    }

    public ChildInfoPopWindow(Activity activity){
        super(activity);
    }

    public void initView(){
        displayNormal();
        displayHover(1);
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
    }

    @OnClick(R.id.iv_child_info_2)
    public void iv_child_info_2_Click(View view) {
        displayHover(2);
    }

    @OnClick(R.id.iv_child_info_3)
    public void iv_child_info_3_Click(View view) {
        displayHover(3);
    }

    @OnClick(R.id.iv_child_info_4)
    public void iv_child_info_4_Click(View view) {
        displayHover(4);
    }

    @OnClick(R.id.iv_child_info_5)
    public void iv_child_info_5_Click(View view) {
        displayHover(5);
    }

    @OnClick(R.id.iv_select_mode)
    public void iv_select_mode_Click(View view){
        Activity activity = ActivityManagerUtils.getInstance().getCurrentActivity();
        SelectDeviceModePopWindow popWindow = new SelectDeviceModePopWindow(activity);
        popWindow.showFromCenter();
    }
}
