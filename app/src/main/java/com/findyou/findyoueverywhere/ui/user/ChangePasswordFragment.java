package com.findyou.findyoueverywhere.ui.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiUser;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.controls.InterceptedLinearLayout;
import com.findyou.findyoueverywhere.utils.AndroidBug5497Workaround;
import com.findyou.findyoueverywhere.utils.KeyboardUtils;
import com.findyou.findyoueverywhere.utils.LocalStorage;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordFragment extends BaseFragment {

    @BindView(R.id.et_oldpwd)
    EditText et_oldpwd;

    @BindView(R.id.et_newpwd)
    EditText et_newpwd;

    @BindView(R.id.et_confirmpwd)
    EditText et_confirmpwd;

    @BindView(R.id.view_root)
    InterceptedLinearLayout view_root;

    private boolean isKeyboardShow = false;

    public int getLayoutId(){
        setTitle("修改密码");
        return R.layout.fragment_change_password;
    }

    public void initView(){
        AndroidBug5497Workaround.assistActivity(getActivity(), new AndroidBug5497Workaround.OnKeyboardToggleListener() {
            @Override
            public void onKeyboardShow(int keyboardSize) {
                isKeyboardShow = true;
            }
            @Override
            public void onKeyboardHide(int keyboardSize) {
                isKeyboardShow = false;
            }
        });
        view_root.setOnInterceptTouchListener((ev)-> {
            if(et_newpwd.isFocusable()) {
                if(isKeyboardShow) {
                    KeyboardUtils.hideSoftInput(et_newpwd);
                }
            }
            if(et_confirmpwd.isFocusable()) {
                if(isKeyboardShow) {
                    KeyboardUtils.hideSoftInput(et_confirmpwd);
                }
            }
            return false;
        });
    }

    @OnClick(R.id.tv_submit)
    public void tv_submit_Click(View view){
        if(et_newpwd == null){
            return;
        }
        if(et_confirmpwd == null){
            return;
        }
        String oldPassword = et_oldpwd.getText().toString();
        String newPassword = et_newpwd.getText().toString();
        String confirmpwd = et_confirmpwd.getText().toString();

        if(TextUtils.isEmpty(oldPassword)){
            ToastUtils.showToast(getContext(), "请输入旧密码!");
            return;
        }
        if(TextUtils.isEmpty(newPassword)){
            ToastUtils.showToast(getContext(), "请输入新密码!");
            return;
        }
        String pwd = LocalStorage.getItem("password");
        oldPassword = ToolUtils.MD5(oldPassword).toLowerCase();
        if(!pwd.equals(oldPassword)){
            ToastUtils.showToast(getContext(), "旧密码输入错误!");
            return;
        }
        if(!newPassword.equals(confirmpwd)){
            ToastUtils.showToast(getContext(), "两次输入的密码不一致!");
            return;
        }
        if(app.me == null){
            return;
        }
        final String md5password = ToolUtils.MD5(newPassword).toLowerCase();
        int userid = app.me.uid;
        ApiUser.changePassword(userid, md5password, (res)->{
            et_oldpwd.setText("");
            et_newpwd.setText("");
            et_confirmpwd.setText("");
            LocalStorage.setItem("password", md5password);
            ToastUtils.showToast(getContext(), res.message);
            if(et_newpwd.isFocusable()) {
                if(isKeyboardShow) {
                    KeyboardUtils.hideSoftInput(et_newpwd);
                }
            }
            if(et_confirmpwd.isFocusable()) {
                if(isKeyboardShow) {
                    KeyboardUtils.hideSoftInput(et_confirmpwd);
                }
            }
        });
    }
}
