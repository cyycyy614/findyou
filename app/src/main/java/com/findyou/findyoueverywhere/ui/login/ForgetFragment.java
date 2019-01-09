package com.findyou.findyoueverywhere.ui.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiUser;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.json.JObject;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetFragment extends BaseFragment {
    @BindView(R.id.et_phoneNumber)
    EditText et_phoneNumber;

    @BindView(R.id.et_authCode)
    EditText et_authCode;

    @BindView(R.id.tv_sendAuthCode)
    TextView tv_authCode;

    private boolean isSendCode;
    private int time_second = 60;
    private String mAuthCode = "123456789";

    public int getLayoutId(){
        setTitle("忘记密码");
        return R.layout.fragment_forget;
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
                tv_authCode.setTextColor(Color.parseColor("#000000"));
                tv_authCode.setEnabled(true);
                time_second = 60;
                tv_authCode.setText("发送验证码");
                isSendCode = false;
            }
        }
    };

    public void initView(){
        isSendCode = false;
        tv_authCode.setEnabled(false);
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
                    tv_authCode.setTextColor(Color.parseColor("#000000"));
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

    @OnClick(R.id.tv_submit)
    public void tv_submit_Click(View view){
        String authCode = et_authCode.getText().toString();
        String phoneNumber = et_phoneNumber.getText().toString();
        if(TextUtils.isEmpty(phoneNumber)){
            ToastUtils.showToast(getContext(), "请输入手机号!");
            return;
        }
        if(TextUtils.isEmpty(authCode)){
            ToastUtils.showToast(getContext(), "请输入验证码!");
            return;
        }
        if(mAuthCode.equals(authCode)){
            Bundle bundle = new Bundle();
            bundle.putString("username", et_phoneNumber.getText().toString());
            //bundle.putString("authCode", mAuthCode);
            ActivityManagerUtils.startActivity(getContext(), ResetPasswordFragment.class, bundle);
        }else{
            ToastUtils.showToast(getContext(), "验证码错误!");
        }
    }

    @OnClick(R.id.tv_sendAuthCode)
    public void tv_sendAuthCode_Click(View view){
        String phoneNumber = et_phoneNumber.getText().toString();
        isSendCode = true;
        tv_authCode.removeCallbacks(runnable);
        ApiUser.sendAuthCode(phoneNumber, AuthCodeType.ForgetPassword.ordinal(), (res)->{
            if(res.code == 200){
                JObject jo = new JObject(res.data);
                if(jo != null) {
                    mAuthCode = jo.getString("code");
                }
                //tv_authCode.setBackgroundResource(R.drawable.login_auth_code_bk_normal);
                tv_authCode.setTextColor(Color.parseColor("#cccccc"));
                tv_authCode.setEnabled(false);
                tv_authCode.setText(String.valueOf(time_second) + "S");
                tv_authCode.postDelayed(runnable, 1000);
            }else{
                isSendCode = false;
                ToastUtils.showToast(getContext(), res.message);
            }
        });
    }
}
