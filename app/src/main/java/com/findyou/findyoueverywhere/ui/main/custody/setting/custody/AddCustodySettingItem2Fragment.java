package com.findyou.findyoueverywhere.ui.main.custody.setting.custody;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class AddCustodySettingItem2Fragment extends BaseFragment {
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
    String name;

    public int getLayoutId() {
        setTitle("添加授权监护人");
        return R.layout.fragment_add_custody_setting_item2;
    }

    public void initSaveBar(){
        TextView textView = (TextView)getRightView(ViewType.TextView);
        if(textView != null){
            textView.setText("完成");
            textView.setTextColor(Color.WHITE);
            textView.setOnClickListener((View view)-> {
                onSubmit();
            });
        }
    }

    public void initView(){
        initSaveBar();
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        name = bundle.getString("name");

        if(tv_child_name != null){
            tv_child_name.setText(name);
        }

        if(tv_time != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = sdf.format(new Date());
            tv_time.setText(dateString);
        }
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
        if(TextUtils.isEmpty(name)){
            ToastUtils.showToast(getContext(), "请输入姓名!");
            return;
        }
        if(TextUtils.isEmpty(phone)){
            ToastUtils.showToast(getContext(), "请输入电话号码!");
            return;
        }else{
            if(!ToolUtils.isPhoneNumber(phone)){
                ToastUtils.showToast(getContext(), "请输入有效的电话号码!");
                return;
            }
        }
        int s;
        if(status){
            s = 1;
        }else {
            s = 0;
        }
        ApiGuardian.add(id, name, phone, s, (res)->{
            ToastUtils.showToast(getContext(),res.message);
            postEvent(new EventMessenger(EventConst.API_ADD_GUARDIAN_SUCCESSFUL));
            if(getActivity() != null) {
                getActivity().finish();
            }
        });
    }
}
