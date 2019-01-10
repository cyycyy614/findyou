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

        int pdFlag = ToolUtils.isCorrectPassWord(password);
        switch (pdFlag){
            case 0:
                break;
            case -1:
                ToastUtils.showToast(getContext(), "密码中需要包含数字!");
                break;
            case -2:
                ToastUtils.showToast(getContext(), "密码中需要包含小写字母!");
                break;
            case -3:
                ToastUtils.showToast(getContext(), "密码中需要包含大写字母!");
                break;
            case -4:
                ToastUtils.showToast(getContext(), "密码中有除了大写字母、小写字母和数字之外的字符了!");
                break;
            case -5:
                ToastUtils.showToast(getContext(), "密码的长度为6-20位!");
                break;
        }

        if(pdFlag != 0)
            return;

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
