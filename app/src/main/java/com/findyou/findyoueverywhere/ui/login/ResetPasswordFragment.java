package com.findyou.findyoueverywhere.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiUser;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ResetPasswordFragment extends BaseFragment {

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.et_newPassword)
    EditText et_newPassword;

    private int mAuthCode;
    private String mobile;

    public int getLayoutId(){
        setTitle("输入新密码");
        return R.layout.fragment_reset_password;
    }

    public void initView(){
        Bundle bundle = getArguments();
        mobile = bundle.getString("username");
    }

    @OnClick(R.id.tv_submit)
    public void tv_submit_Click(View view){
        String newPassword = et_newPassword.getText().toString();
        String password = et_password.getText().toString();
        if(TextUtils.isEmpty(newPassword)){
            ToastUtils.showToast(getContext(),"请输入新密码!");
            return;
        }
        if(!newPassword.equals(password)){
            ToastUtils.showToast(getContext(),"两次输入的密码不一致!");
            return;
        }
        String md5password = ToolUtils.MD5(password);
        ApiUser.resetPassword(mobile, md5password, (res)->{
            ToastUtils.showToast(getContext(), "修改密码成功!");
        });
    }
}
