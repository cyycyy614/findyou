package com.findyou.findyoueverywhere.ui.main.custody.setting.efence;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiEfence;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.bean.EfenceInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.ui.main.map.AddressOfMapFragment;
import com.findyou.findyoueverywhere.ui.main.map.AddressOfMapFragmentV2;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.AndroidBug5497Workaround;
import com.findyou.findyoueverywhere.utils.FragmentManagerUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.http.QueryString;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class AddEfenceFragment extends BaseFragment {
    @BindView(R.id.sb_range)
    SeekBar sb_range;

    @BindView(R.id.tv_radius)
    TextView tv_radius;

    @BindView(R.id.tv_name)
    EditText tv_name;

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.lin_map)
    LinearLayout lin_map;

    @BindView(R.id.iv_status)
    ImageView iv_status;

    private EfenceInfoBean info;
    private int id;
    private int radius;
    private FragmentManagerUtils fragmentManagerUtils;
    private AddressOfMapFragmentV2 fragment;

    public void initSaveBar(){
        TextView textView = (TextView)getRightView(ViewType.TextView);
        if(textView != null){
            textView.setText("保存");
            textView.setTextColor(Color.WHITE);
            textView.setOnClickListener((View view)-> {
                onSubmit();
            });
        }
    }

    public int getLayoutId() {
        setTitle("添加电子围栏");
        registerEvent();
        return R.layout.add_efence_fragment;
    }

    public void initView(){
        initSaveBar();
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        radius = 500;
        tv_radius.setText(radius + "米");
        sb_range.setMax(5000);
        sb_range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = progress;
                tv_radius.setText(progress + "米");
                postEvent(new EventMessenger(EventConst.API_UPDATE_EFENCE_RANGE, radius));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sb_range.setProgress(radius);
        AndroidBug5497Workaround.assistActivity(getActivity(), new AndroidBug5497Workaround.OnKeyboardToggleListener() {
            @Override
            public void onKeyboardShow(int keyboardSize) {
            }
            @Override
            public void onKeyboardHide(int keyboardSize) {
            }
        });
        fragment = new AddressOfMapFragmentV2();
        fragment.setRadius(radius);
        fragmentManagerUtils = new FragmentManagerUtils(getChildFragmentManager());
        fragmentManagerUtils.replaceFragment(R.id.lin_map, fragment);
    }

    public void onSubmit(){
        String name = tv_name.getText().toString();
//        String radius = tv_radius.getText().toString();
        String address = tv_address.getText().toString();
//        int radius_int = 0;
//        if(!TextUtils.isEmpty(radius)){
//            radius_int = Integer.parseInt(radius);
//        }
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", id);
        q.add("name", name);
        q.add("address", address);
        q.add("radius", radius);
        q.add("latitude", fragment.getLatitude());
        q.add("longitude", fragment.getLongitude());
        q.add("status", status);
        ApiEfence.add(q, (res)->{
            ToastUtils.showToast(getContext(), res.message);
            postEvent(new EventMessenger(EventConst.API_UPDATE_EFENCE_TIME));
        });
    }

    @OnClick(R.id.lin_address)
    public void lin_address_Click(View view){

    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_GET_BAIDU_MAP_ADDRESS:
                tv_address.setText(event.obj.toString());
                break;
        }
    }

    boolean status = true;
    @OnClick(R.id.iv_status)
    public void iv_status_Click(View view){
        status = !status;
        if(status){
            iv_status.setImageResource(R.drawable.ic_select_hover);
        }else {
            iv_status.setImageResource(R.drawable.ic_select_normal);
        }
    }
}
