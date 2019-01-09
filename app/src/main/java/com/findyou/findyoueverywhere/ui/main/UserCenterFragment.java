package com.findyou.findyoueverywhere.ui.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.WebViewFragment;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.ui.user.HelpFragment;
import com.findyou.findyoueverywhere.ui.user.SettingFragment;
import com.findyou.findyoueverywhere.ui.user.edit.MeFragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.BitmapUtils;
import com.findyou.findyoueverywhere.utils.PicUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserCenterFragment extends BaseFragment {
    @BindView(R.id.system_status_bar)
    LinearLayout system_status_bar;

    @BindView(R.id.iv_headimage)
    CircleImageView iv_headimage;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.tv_child)
    TextView tv_child;

    @BindView(R.id.tv_main_guar)
    TextView tv_main_guar;

    @BindView(R.id.tv_guar)
    TextView tv_guar;

    public int getLayoutId() {
        registerEvent();
        return R.layout.fragment_user_center;
    }

    public void initView(){
        int statusHeight = UIUtils.getStatusHeight(getContext());
        system_status_bar.getLayoutParams().height = statusHeight;
    }

    public void initUI(){
        if(tv_nickname != null){
            tv_nickname.setText(app.me.nickname);
        }
        PicUtils.loadImage(getContext(), app.me.headimage, iv_headimage);
        tv_child.setText(app.me.child + "");
        tv_main_guar.setText(app.me.main_guar + "");
        int guar = app.me.guardian - app.me.main_guar;
        tv_guar.setText(guar + "");
    }

    public void onVisibleChanged(boolean isVisible){
        if(isVisible){
            initUI();
        }
    }

    @OnClick(R.id.lin_edit_me)
    public void lin_edit_me_Click(View view){
        ActivityManagerUtils.startActivity(getContext(), MeFragment.class, null);
    }

    @OnClick(R.id.lin_about)
    public void lin_about_Click(View view){
        Bundle bundle = new Bundle();
        bundle.putString("title", "企业信息");
        bundle.putString("url", "http://www.baidu.com");
        ActivityManagerUtils.startActivity(getContext(), WebViewFragment.class, bundle);
    }

    @OnClick(R.id.lin_help)
    public void lin_help_Click(View view){
//        Bundle bundle = new Bundle();
//        bundle.putString("title", "帮助中心");
//        bundle.putString("url", "http://www.baidu.com");
        ActivityManagerUtils.startActivity(getContext(), HelpFragment.class, null);
    }

    @OnClick(R.id.lin_setting)
    public void lin_setting_Click(View view){
        ActivityManagerUtils.startActivity(getContext(), SettingFragment.class, null);
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_USER_LOOUT:
                if(getActivity() != null){
                    getActivity().finish();
                }
                break;
        }
    }
}
