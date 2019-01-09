package com.findyou.findyoueverywhere.ui.main.custody.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiWarning;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.bean.WarningInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.ui.main.custody.popwindow.WarnDistanceSettingPopWindow;
import com.findyou.findyoueverywhere.ui.main.custody.popwindow.WarnTimeSettingPopWindow;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.http.QueryString;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class CustodySettingItem7Fragment extends BaseFragment {
    @BindView(R.id.iv_isrange)
    ImageView iv_isrange;

    @BindView(R.id.iv_isdistance)
    ImageView iv_isdistance;

    @BindView(R.id.iv_istime)
    ImageView iv_istime;

    @BindView(R.id.iv_ispower)
    ImageView iv_ispower;

    @BindView(R.id.tv_dis)
    TextView tv_dis;

    @BindView(R.id.tv_time)
    TextView tv_time;

    private boolean isrange;
    private boolean isdistance;
    private boolean istime;
    private boolean ispower;
    private int id;
    private WarningInfoBean info;

    public int getLayoutId() {
        setTitle("报警设置");
        registerEvent();
        return R.layout.fragment_custody_setting_item7;
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
        initSaveBar();
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        ApiWarning.getItem(id, (res)->{
            info = JsonUtils.convert(res.data, WarningInfoBean.class);
            if(info == null){
                info = new WarningInfoBean();
            }
            if(info != null){
                if(info.isrange){
                    iv_isrange.setImageResource(R.drawable.ic_select_hover);
                }else{
                    iv_isrange.setImageResource(R.drawable.ic_select_normal);
                }
                if(info.isdistance){
                    iv_isdistance.setImageResource(R.drawable.ic_select_hover);
                }else{
                    iv_isdistance.setImageResource(R.drawable.ic_select_normal);
                }
                if(info.istime){
                    iv_istime.setImageResource(R.drawable.ic_select_hover);
                }else{
                    iv_istime.setImageResource(R.drawable.ic_select_normal);
                }
                if(info.ispower){
                    iv_ispower.setImageResource(R.drawable.ic_select_hover);
                }else{
                    iv_ispower.setImageResource(R.drawable.ic_select_normal);
                }
                tv_dis.setText(info.distance + "米");
                tv_time.setText(info.time + "天");
            }else{

            }
        });
    }

    @OnClick(R.id.iv_isrange)
    public void iv_isrange_Click(View view){
        info.isrange = !info.isrange;
        if(info.isrange){
            iv_isrange.setImageResource(R.drawable.ic_select_hover);
        }else{
            iv_isrange.setImageResource(R.drawable.ic_select_normal);
        }
    }

    @OnClick(R.id.iv_isdistance)
    public void iv_isdistance_Click(View view){
        info.isdistance = !info.isdistance;
        if(info.isdistance){
            iv_isdistance.setImageResource(R.drawable.ic_select_hover);
        }else{
            iv_isdistance.setImageResource(R.drawable.ic_select_normal);
        }
    }

    @OnClick(R.id.iv_istime)
    public void iv_istime_Click(View view){
        info.istime = !info.istime;
        if(info.istime){
            iv_istime.setImageResource(R.drawable.ic_select_hover);
        }else{
            iv_istime.setImageResource(R.drawable.ic_select_normal);
        }
    }

    @OnClick(R.id.iv_ispower)
    public void iv_ispower_Click(View view){
        info.ispower = !info.ispower;
        if(info.ispower){
            iv_ispower.setImageResource(R.drawable.ic_select_hover);
        }else{
            iv_ispower.setImageResource(R.drawable.ic_select_normal);
        }
    }

    @OnClick(R.id.lin_distance)
    public void lin_distance_Click(View view){
        WarnDistanceSettingPopWindow popWindow = new WarnDistanceSettingPopWindow(getActivity());
        popWindow.showFromBottom();
    }

    @OnClick(R.id.lin_time)
    public void lin_time_Click(View view){
        WarnTimeSettingPopWindow popWindow = new WarnTimeSettingPopWindow(getActivity());
        popWindow.showFromCenter();
    }

    public void onSuhmit(){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", id);
        q.add("isrange", info.isrange);
        q.add("range", info.range);
        q.add("isdistance", info.isdistance);
        q.add("distance", info.distance);
        q.add("istime", info.istime);
        q.add("time", info.time);
        q.add("ispower", info.ispower);
        q.add("power", info.power);
        ApiWarning.setting(q, (res)->{
            ToastUtils.showToast(getContext(), res.message);
        });
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_CHANGE_WARN_DISTANCE:
                info.distance = Integer.parseInt(event.obj.toString());
                if(tv_dis != null) {
                    tv_dis.setText(info.distance + "米");
                }
                break;
            case EventConst.API_CHANGE_WARN_TIME:
                info.time = Integer.parseInt(event.obj.toString());
                if(tv_time != null) {
                    tv_time.setText(info.time + "天");
                }
                break;
        }
    }
}
