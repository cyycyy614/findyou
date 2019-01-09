package com.findyou.findyoueverywhere.ui.user;

import android.view.View;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.controls.dialog.CommomDialog;
import com.findyou.findyoueverywhere.ui.login.LoginFragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.LocalStorage;
import com.findyou.findyoueverywhere.utils.PicUtils;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountManagerFragment extends BaseFragment {
    @BindView(R.id.iv_headimage)
    CircleImageView iv_headimage;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_phone)
    TextView tv_phone;

    public int getLayoutId() {
        setTitle("帐号管理");
        return R.layout.fragment_account_manager;
    }

    public void initView(){
        PicUtils.loadImage(getContext(), app.me.headimage, iv_headimage);
        tv_nickname.setText(app.me.nickname);
        tv_phone.setText(app.me.phone);
    }

    @OnClick(R.id.tv_submit)
    public void tv_submit_Click(View view){
        CommomDialog dlg = new CommomDialog(getActivity(), "信息提示", "是否退出当前帐号?", (obj)->{
            //OK
            LocalStorage.setItem("username", "");
            LocalStorage.setItem("password", "");
            ActivityManagerUtils.startActivity(getContext(), LoginFragment.class, null);
            //ActivityManagerUtils.getInstance().finishAllActivity();
            postEvent(new EventMessenger(EventConst.API_USER_LOOUT));
            if(getActivity() != null) {
                getActivity().finish();
            }
        },(obj1)->{
            //取消
        });
        dlg.showFromCenter();
    }
}
