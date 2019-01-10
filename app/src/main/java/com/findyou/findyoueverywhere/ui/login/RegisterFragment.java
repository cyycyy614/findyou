package com.findyou.findyoueverywhere.ui.login;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiUser;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.controls.InterceptedLinearLayout;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.KeyboardUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterFragment extends BaseFragment {

    @BindView(R.id.et_phoneNumber)
    EditText et_phoneNumber;

    @BindView(R.id.et_authCode)
    EditText et_authCode;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.tv_sendAuthCode)
    TextView tv_authCode;

    @BindView(R.id.view_root)
    InterceptedLinearLayout view_root;

    private boolean isSendCode;
    private int time_second = 60;
    private String mAuthCode = "123456789";

    public int getLayoutId(){
        showTitleBar(false);
        showStatusBar(false);
        return R.layout.fragment_register;
    }

    public void initView(){
        //焦点事件
        isSendCode = false;
        tv_authCode.setEnabled(false);
        view_root.setOnInterceptTouchListener((ev)-> {
            hideKeybord();
            return false;
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isSendCode) {
                    return;
                }
                if(s.length() == 11){
                    // 此处为得到焦点时的处理内容
                    //tv_authCode.setBackgroundResource(R.drawable.login_auth_code_bk_hover);
                    tv_authCode.setTextColor(Color.parseColor("#ffffff"));
                    tv_authCode.setEnabled(true);
                } else {
                    // 此处为失去焦点时的处理内容
                    //tv_authCode.setBackgroundResource(R.drawable.login_auth_code_bk_normal);
                    tv_authCode.setTextColor(Color.parseColor("#cccccc"));
                    tv_authCode.setEnabled(false);
                }
            }
        };
        et_phoneNumber.addTextChangedListener(textWatcher);
    }

    private void hideKeybord(){
        if(et_phoneNumber.isFocusable()) {
            KeyboardUtils.hideSoftInput(et_phoneNumber);
        }
        if(et_password.isFocusable()) {
            KeyboardUtils.hideSoftInput(et_password);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time_second--;
            if(tv_authCode == null){
                return;
            }
            tv_authCode.setText(String.valueOf(time_second) + "S");
            if(time_second > 0){
                tv_authCode.postDelayed(runnable, 1000);
            }else {
                //tv_authCode.setBackgroundResource(R.drawable.login_auth_code_bk_hover);
                tv_authCode.setTextColor(Color.parseColor("#ffffff"));
                tv_authCode.setEnabled(true);
                time_second = 60;
                tv_authCode.setText("发送验证码");
                isSendCode = false;
            }
        }
    };

    @OnClick(R.id.tv_sendAuthCode)
    public void tv_sendAuthCode_Click(View view){
        String phoneNumber = et_phoneNumber.getText().toString();
        if(!ToolUtils.isPhoneNumber(phoneNumber)){
            ToastUtils.showToast(getContext(), "请输入正确的手机号!");
            return;
        }
        isSendCode = true;
        tv_authCode.removeCallbacks(runnable);
        ApiUser.sendAuthCode(phoneNumber, AuthCodeType.Register.ordinal(), (res)->{
            if(res.code == 200){
                mAuthCode = res.data;
                tv_authCode.setEnabled(false);
                tv_authCode.setText(String.valueOf(time_second) + "S");
                tv_authCode.postDelayed(runnable, 1000);
            }else{
                isSendCode = false;
                ToastUtils.showToast(getContext(), res.message);
            }
        });
    }

    @OnClick(R.id.tv_submit)
    public void tv_submit_Click(View view){
        String phoneNumber = et_phoneNumber.getText().toString();
        String authCode = et_authCode.getText().toString();
        String password = et_password.getText().toString();
        if(TextUtils.isEmpty(phoneNumber)){
            ToastUtils.showToast(getContext(), "请输入手机号!");
            return;
        }
        if(!ToolUtils.isPhoneNumber(phoneNumber)){
            ToastUtils.showToast(getContext(), "请输入正确的手机号!");
            return;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtils.showToast(getContext(), "请输入密码!");
            return;
        }

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

        if(TextUtils.isEmpty(authCode)){
            ToastUtils.showToast(getContext(), "请输入验证码!");
            return;
        }
        if(!mAuthCode.equals(et_authCode.getText().toString())){
            ToastUtils.showToast(getContext(), "验证码无效!");
            return;
        }
        String md5password = ToolUtils.MD5(password);
        ApiUser.register(phoneNumber, md5password, (res)->{
            if(res.data == null){
                return;
            }
            ToastUtils.showToast(getContext(), "注册成功,请登录!");
            tv_authCode.removeCallbacks(runnable);
            if(getActivity() != null) {
                getActivity().finish();
            }
            //注册成功后直接登录
            //onLogin();
        });
    }

    @OnClick(R.id.tv_login)
    public void tv_login_Click(View view){
        if(getActivity() != null) {
            getActivity().finish();
        }
    }
}
