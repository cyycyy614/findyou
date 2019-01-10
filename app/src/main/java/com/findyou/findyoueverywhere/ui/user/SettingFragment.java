package com.findyou.findyoueverywhere.ui.user;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiCommon;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.bean.common.AppInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.ui.user.popwindow.AppUpdateOnlinePopWindow;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment {
    @BindView(R.id.tv_version)
    TextView tv_version;

    @BindView(R.id.tv_new)
    TextView tv_new;

    public AppInfoBean appInfo;

    public int getLayoutId() {
        setTitle("设置");
        registerEvent();
        return R.layout.fragment_setting;
    }

    public void initView(){
        if(tv_version != null){
            tv_version.setText("当前版本:v" + AppUtils.getAppVersionName());
        }
        ApiCommon.getAppInfo((res)->{
            appInfo = JsonUtils.convert(res.data, AppInfoBean.class);
            if(appInfo == null){
                return;
            }
            int versionCode = AppUtils.getAppVersionCode();
            try{
                if(versionCode < appInfo.versionCode){
                    tv_new.setText("new");
                }else {
                    tv_new.setText("");
                }
            }catch (Exception ex){

            }
        });
    }

    @OnClick(R.id.lin_changePassword)
    public void lin_changePassword_Click(View view){
        ActivityManagerUtils.startActivity(getContext(), ChangePasswordFragment.class, null);
    }

    @OnClick(R.id.lin_accountManager)
    public void lin_accountManager_Click(View view){
        ActivityManagerUtils.startActivity(getContext(), AccountManagerFragment.class, null);
    }

    @OnClick(R.id.lin_updateOnline)
    public void lin_updateOnline_Click(View view){
        int versionCode = AppUtils.getAppVersionCode();
        if(appInfo == null){
            return;
        }
        if(versionCode < appInfo.versionCode){
            AppUpdateOnlinePopWindow popWindow = new AppUpdateOnlinePopWindow(getActivity(), appInfo);
            popWindow.showFromCenter();
        }
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
