package com.findyou.findyoueverywhere.ui.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiUser;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseActivity;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.bean.common.UserInfoBean;
import com.findyou.findyoueverywhere.controls.dialog.CommomDialog;
import com.findyou.findyoueverywhere.controls.dialog.LoadingDialog;
import com.findyou.findyoueverywhere.ui.main.MainActivity;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.LoadingUtils;
import com.findyou.findyoueverywhere.utils.LocalStorage;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;
import com.findyou.findyoueverywhere.utils.WindowUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {
    @BindView(R.id.et_phoneNumber)
    EditText et_phoneNumber;

    @BindView(R.id.et_password)
    EditText et_password;

    public int getLayoutId(){
        showTitleBar(false);
        showStatusBar(false);
        return R.layout.fragment_login;
    }

    public void initView(){
    }

    @OnClick(R.id.tv_login)
    public void tv_login_Click(View view){
        String username = et_phoneNumber.getText().toString();
        String password = et_password.getText().toString();
        if(TextUtils.isEmpty(username)){
            ToastUtils.showToast(getContext(), "请输入用户名!");
            return;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtils.showToast(getContext(), "请输入密码!");
            return;
        }
        LoadingUtils.show((BaseActivity) getActivity(),"正在登录...");
        //密码转md5密码
        String md5password = ToolUtils.MD5(password);
        //用户的登录请滶
        ApiUser.login(username, md5password, "mobile", (res)->{
            LoadingUtils.close();
            //服务端返回的json
            String json = res.data.toString();
            //返回的用户信息,转成实体类
            UserInfoBean bean = JsonUtils.convert(json, UserInfoBean.class);
            app.me = bean;
            //提示
            ToastUtils.showToast(getContext(), "登录成功!");
            //保存用户名,密码
            LocalStorage.setItem("username", username);
            LocalStorage.setItem("password", md5password);
            //跳转到主界面
            ActivityManagerUtils.startActivity(getContext(), MainActivity.class, null);
            //关闭登录界面
            if(getActivity() != null) {
                getActivity().finish();
            }
        }, null);
    }

    @OnClick(R.id.tv_register)
    public void tv_register_Click(View view){
        ActivityManagerUtils.startActivity(getContext(), RegisterFragment.class, null);
    }

    @OnClick(R.id.tv_forgetPassword)
    public void tv_forgetPassword_Click(View view){
        ActivityManagerUtils.startActivity(getContext(), ForgetFragment.class, null);
    }

    @OnClick(R.id.tv_serviceAgreement)
    public void tv_serviceAgreement_Click(View view){
        ActivityManagerUtils.startActivity(getContext(), SoftProtocolFragment.class, null);
    }

    public void onBackPressed() {
        //返回键处理
        CommomDialog dlg = new CommomDialog(getActivity(), "信息提示", "是否退出App?", (obj)->{
            //OK
            ActivityManagerUtils.getInstance().exitApp(); //退出app.
        },(obj1)->{
            //取消
        });
        //dlg.setCancelBtnText("退出");
        dlg.showFromCenter();
    }
}
