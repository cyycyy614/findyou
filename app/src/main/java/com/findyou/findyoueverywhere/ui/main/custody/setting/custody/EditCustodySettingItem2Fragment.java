package com.findyou.findyoueverywhere.ui.main.custody.setting.custody;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiGuardian;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.bean.GuardianInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class EditCustodySettingItem2Fragment extends BaseFragment {
    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.iv_status)
    ImageView iv_status;

    @BindView(R.id.tv_child_name)
    TextView tv_child_name;

    @BindView(R.id.tv_name)
    EditText tv_name;

    @BindView(R.id.tv_phone)
    EditText tv_phone;

    private boolean status = true;
    int id;
    String childname;

    public int getLayoutId() {
        setTitle("授权监护人信息");
        return R.layout.fragment_add_custody_setting_item2;
    }

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

    public void initView(){
        initSaveBar();
        Bundle bundle = getArguments();
        String json = bundle.getString("obj");
        childname = bundle.getString("childname");
        if(tv_child_name != null){
            tv_child_name.setText(childname);
        }
        GuardianInfoBean bean = JsonUtils.convert(json, GuardianInfoBean.class);
        if(bean != null){
            if(tv_name != null){
                tv_name.setText(bean.name);
            }
            if(tv_phone != null){
                tv_phone.setText(bean.phone);
            }
            if(tv_time != null){
                tv_time.setText(bean.auth_time);
            }
            if(bean.status == 1){
                iv_status.setImageResource(R.drawable.ic_select_hover);
            }else{
                iv_status.setImageResource(R.drawable.ic_select_normal);
            }
        }
        if(bean != null){
            id = bean.id;
        }

//        if(tv_time != null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String dateString = sdf.format(new Date());
//            tv_time.setText(dateString);
//        }
        //getItem

    }

    @OnClick(R.id.iv_status)
    public void iv_status_Click(View view){
        status = !status;
        if(status){
            iv_status.setImageResource(R.drawable.ic_select_hover);
        }else{
            iv_status.setImageResource(R.drawable.ic_select_normal);
        }
    }

    public void onSubmit(){
        String name = tv_name.getText().toString();
        String phone = tv_phone.getText().toString();
        int s;
        if(status){
            s = 1;
        }else {
            s = 0;
        }
        ApiGuardian.update(id, name, phone, s, (res)->{
            ToastUtils.showToast(getContext(),res.message);
            postEvent(new EventMessenger(EventConst.API_UPDATE_GUARDIAN_SUCCESSFUL));
            if(getActivity() != null) {
                getActivity().finish();
            }
        });
    }

}
