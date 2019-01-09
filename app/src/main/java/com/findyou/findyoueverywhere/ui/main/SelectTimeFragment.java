package com.findyou.findyoueverywhere.ui.main;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.ui.main.map.popwindow.SelectTimePopWindow;
import com.findyou.findyoueverywhere.utils.DateTimeUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.squareup.otto.Subscribe;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectTimeFragment extends BaseFragment {

    @BindView(R.id.lin_item_1)
    LinearLayout lin_item_1;
    @BindView(R.id.lin_item_2)
    LinearLayout lin_item_2;
    @BindView(R.id.lin_item_3)
    LinearLayout lin_item_3;
    @BindView(R.id.lin_item_4)
    LinearLayout lin_item_4;
    @BindView(R.id.lin_item_5)
    LinearLayout lin_item_5;
    @BindView(R.id.tv_item_1)
    TextView tv_item_1;
    @BindView(R.id.tv_item_2)
    TextView tv_item_2;
    @BindView(R.id.tv_item_3)
    TextView tv_item_3;
    @BindView(R.id.tv_item_4)
    TextView tv_item_4;
    @BindView(R.id.tv_item_5)
    TextView tv_item_5;

    @BindView(R.id.tv_start_time)
    TextView tv_start_time;

    @BindView(R.id.tv_end_time)
    TextView tv_end_time;

    int timeType = -1;

    public int getLayoutId() {
        setTitle("时间选择");
        registerEvent();
        return R.layout.fragment_select_time;
    }

    public void initView(){
    }

    public void displayNormal(){
        lin_item_1.setBackgroundResource(R.drawable.pop_window_corner_radius_small_border_normal);
        lin_item_2.setBackgroundResource(R.drawable.pop_window_corner_radius_small_border_normal);
        lin_item_3.setBackgroundResource(R.drawable.pop_window_corner_radius_small_border_normal);
        lin_item_4.setBackgroundResource(R.drawable.pop_window_corner_radius_small_border_normal);
        lin_item_5.setBackgroundResource(R.drawable.pop_window_corner_radius_small_border_normal);
        tv_item_1.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_item_2.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_item_3.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_item_4.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_item_5.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    public void displayHover(int index){
        displayNormal();
        switch (index){
            case 1:
                lin_item_1.setBackgroundResource(R.drawable.pop_window_corner_radius_small_border_hover);
                tv_item_1.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                lin_item_2.setBackgroundResource(R.drawable.pop_window_corner_radius_small_border_hover);
                tv_item_2.setTextColor(getResources().getColor(R.color.white));
                break;

            case 3:
                lin_item_3.setBackgroundResource(R.drawable.pop_window_corner_radius_small_border_hover);
                tv_item_3.setTextColor(getResources().getColor(R.color.white));
                break;
            case 4:
                lin_item_4.setBackgroundResource(R.drawable.pop_window_corner_radius_small_border_hover);
                tv_item_4.setTextColor(getResources().getColor(R.color.white));
                break;
            case 5:
                lin_item_5.setBackgroundResource(R.drawable.pop_window_corner_radius_small_border_hover);
                tv_item_5.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    String start_time;
    String end_time;

    @OnClick(R.id.lin_item_1)
    public void lin_item_1_Click(View view){
        //一小时前
        displayHover(1);
        start_time = DateTimeUtils.addHours(new Date(), -1);
        end_time = DateTimeUtils.toString(new Date());
        timeType = 0;
        ToastUtils.showToast(getContext(), start_time + "-" + end_time);
    }

    @OnClick(R.id.lin_item_2)
    public void lin_item_2_Click(View view){
        //前天
        displayHover(2);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY) + 2 * 24;
        start_time = DateTimeUtils.addHours(new Date(), -hour);
        end_time = DateTimeUtils.toString(new Date());
        timeType = 0;
    }

    @OnClick(R.id.lin_item_3)
    public void lin_item_3_Click(View view){
        //昨天
        displayHover(3);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY) + 1 * 24;
        start_time = DateTimeUtils.addHours(new Date(), -hour);
        end_time = DateTimeUtils.toString(new Date());
        timeType = 0;
    }

    @OnClick(R.id.lin_item_4)
    public void lin_item_4_Click(View view){
        //今天
        displayHover(4);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        start_time = DateTimeUtils.addHours(new Date(), -hour);
        end_time = DateTimeUtils.toString(new Date());
        timeType = 0;
    }

    @OnClick(R.id.lin_item_5)
    public void lin_item_5_Click(View view){
        //自定义时间
        displayHover(5);
        timeType = 1;
    }

    @OnClick(R.id.lin_start_time)
    public void lin_start_time_Click(View view){
        SelectTimePopWindow popWindow = new SelectTimePopWindow(getActivity(),"请选择开始时间", 0);
        popWindow.showFromBottom();
    }

    @OnClick(R.id.lin_end_time)
    public void lin_end_time_Click(View view){
        SelectTimePopWindow popWindow = new SelectTimePopWindow(getActivity(),"请选择结束时间", 1);
        popWindow.showFromBottom();
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_SELECT_START_TIME:
                tv_start_time.setText(event.obj.toString());
                start_time = event.obj.toString();
                break;
            case EventConst.API_SELECT_END_TIME:
                tv_end_time.setText(event.obj.toString());
                end_time = event.obj.toString();
                break;
        }
    }

    @OnClick(R.id.lin_ok)
    public void lin_ok_Click(View view){
        if(timeType == -1){
            ToastUtils.showToast(getContext(),"请选择时间!");
            return;
        }
        //...

    }
}
